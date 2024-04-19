package cn.cnic.component.visual.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.cnic.component.visual.entity.DataBaseInfo;
import cn.cnic.component.visual.entity.ExcelNameAsso;
import cn.cnic.component.visual.entity.GraphConf;
import cn.cnic.component.visual.entity.GraphTemplate;
import cn.cnic.component.visual.mapper.piflowdata.ExcelSourceMapper;
import cn.cnic.component.visual.mapper.piflowdb.DataBaseInfoMapper;
import cn.cnic.component.visual.mapper.piflowdb.ExcelNameAssoMapper;
import cn.cnic.component.visual.mapper.piflowdb.GraphTemplateMapper;
import cn.cnic.component.visual.service.DataBaseInfoService;
import cn.cnic.component.visual.util.MybatisUtil;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * author:hmh
 * date:2023-11-13
 */
@Service
public class DataBaseInfoServiceImpl implements DataBaseInfoService {
    @Autowired
    private DataBaseInfoMapper dataBaseInfoMapper;
    @Autowired
    private GraphTemplateMapper graphTemplateMapper;
    @Autowired
    private ExcelNameAssoMapper excelNameAssoMapper;
    @Autowired
    private ExcelSourceMapper excelSourceMapper;
    @Override
    public ResponseResult<List<DataBaseInfo>> getDatabaseList(RequestData requestData) {
        String queryContent = requestData.getQueryContent();
        int pageSize = requestData.getPageSize();
        int pageNum = requestData.getPageNum();
        List<DataBaseInfo> dataBaseInfos = null;
        //分页
        Page<DataBaseInfo> page = new Page<>(pageNum,pageSize);
        QueryWrapper<DataBaseInfo> wrapper = new QueryWrapper<>();
        if((null != queryContent) && (!"".equals(queryContent.trim()))){
            wrapper.like("db_name",queryContent);
        }
        wrapper.orderByDesc("update_time");
        Page<DataBaseInfo> page1 = dataBaseInfoMapper.selectPage(page,wrapper);
        dataBaseInfos = page1.getRecords();
        int total = (int)page1.getTotal();
        return ResponseResult.success(dataBaseInfos,total);
    }

    @Override
    public ResponseResult<DataBaseInfo> getDatabaseInfo(DataBaseInfo dataBaseInfo) {
        DataBaseInfo dataBaseInfo1 = dataBaseInfoMapper.selectById(dataBaseInfo.getId());
        return ResponseResult.success(dataBaseInfo1);
    }

    @Override
    public ResponseResult addDatabase(DataBaseInfo dataBaseInfo) {
        String dbName = dataBaseInfo.getDbName();
        //校验名称不能重复
        if((null == dbName) || ("".equals(dbName))){
            return ResponseResult.error("该数据库名称不能为空！");
        }else{
            //判断数据库连接名是否已存在
            QueryWrapper<DataBaseInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("db_name",dbName);
            DataBaseInfo dataBase = dataBaseInfoMapper.selectOne(wrapper);
            if(null != dataBase){
                return ResponseResult.error("该数据库名称已存在！");
            }
        }
        if((null == dataBaseInfo.getDriverClass()) || ("".equals(dataBaseInfo.getDriverClass()))){
            return ResponseResult.error("该数据库驱动不能为空！");
        }
        if((null == dataBaseInfo.getUrl()) || ("".equals(dataBaseInfo.getUrl()))){
            return ResponseResult.error("该数据库url不能为空！");
        }
        if((null == dataBaseInfo.getUserName()) || ("".equals(dataBaseInfo.getUserName()))){
            return ResponseResult.error("该数据库用户名不能为空！");
        }
        if((null == dataBaseInfo.getPassword()) || ("".equals(dataBaseInfo.getPassword()))){
            return ResponseResult.error("该数据库密码不能为空！");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(formatter);
        dataBaseInfo.setCreateTime(format);
        dataBaseInfo.setUpdateTime(format);
        int insert = dataBaseInfoMapper.insert(dataBaseInfo);
        if(insert == 1){
            return ResponseResult.success();
        }else {
            return ResponseResult.error("新增数据库连接失败！");
        }
    }

    @Override
    public ResponseResult delDatabase(DataBaseInfo dataBaseInfo) {
        //判断图表模板中是否有使用该连接
        Long count = getGraphTemplateCount(dataBaseInfo.getId());
        if(count  ==  0){
            int insert = dataBaseInfoMapper.deleteById(dataBaseInfo.getId());
            if(insert == 1){
                return ResponseResult.success();
            }else {
                return ResponseResult.error("删除数据库连接失败！");
            }
        }else {
            return ResponseResult.error("已经有模板使用该数据库连接，无法删除！");
        }
    }

    @Override
    public ResponseResult updateDatabase(DataBaseInfo dataBaseInfo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(formatter);
        dataBaseInfo.setUpdateTime(format);
        //判断图表模板中是否有使用该连接
        Long count = getGraphTemplateCount(dataBaseInfo.getId());
        if(count  ==  0){
            int num = dataBaseInfoMapper.updateById(dataBaseInfo);
            if(num == 1){
                return ResponseResult.success();
            }else {
                return ResponseResult.error("修改数据库连接失败！");
            }
        }else {
            return ResponseResult.error("已经有模板使用该数据库连接，无法修改！");
        }

    }



    //不同数据库的操作
    @Override
    public ResponseResult<List<Map>> getTableData(GraphConf graphConf) {
        SqlSession sqlSession = null;
        HikariDataSource dataSource = null;
        try{
            Integer graphTemplateId = graphConf.getGraphTemplateId();
            GraphTemplate graphTemplate = graphTemplateMapper.selectById(graphTemplateId);
            String realTableName = null;
            //如果是excel类型模板获取数据，需要从映射表中取得表名
            if("excel".equals(graphTemplate.getType())){
                ExcelNameAsso excelNameAsso = excelNameAssoMapper.selectById(graphTemplate.getExcelAssoId());
                if(null == excelNameAsso){
                    return ResponseResult.error("获取数据失败！");
                }else {
                    realTableName = excelNameAsso.getAssoName();
                }
            }else if("mysql".equals(graphTemplate.getType())){
                //mysql类型模板获取数据
                realTableName = graphTemplate.getTableName();
            }
            if(null != graphTemplate){
                List<Map> maps = new ArrayList<>();
                if("excel".equals(graphTemplate.getType())){
                    maps = excelSourceMapper.selectExcelTableData(realTableName);
                }else if("mysql".equals(graphTemplate.getType())){
                    DataBaseInfo dataBaseInfo = dataBaseInfoMapper.selectById(graphTemplate.getDataBaseId());
                    dataSource = MybatisUtil.getDataSource(dataBaseInfo.getDriverClass(), dataBaseInfo.getUrl(), dataBaseInfo.getUserName(), dataBaseInfo.getPassword());
                    sqlSession = MybatisUtil.getSqlSession(dataSource);
                    DataBaseInfoMapper mapper = sqlSession.getMapper(DataBaseInfoMapper.class);
                    maps = mapper.selectTableData(realTableName);

                }
                return ResponseResult.success(maps,maps.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null != sqlSession){
                sqlSession.close();
                dataSource.close();
            }
        }
        return ResponseResult.error("获取数据失败！");
    }
    @Override
    public ResponseResult<List<String>> showTables(RequestData requestData) {
        SqlSession sqlSession = null;
        HikariDataSource dataSource = null;
        try{
            //获取本链接对应的mapper
            dataSource = MybatisUtil.getDataSource(requestData.getDriverClass(), requestData.getUrl(), requestData.getUserName(), requestData.getPassword());
            sqlSession = MybatisUtil.getSqlSession(dataSource);
            DataBaseInfoMapper mapper = sqlSession.getMapper(DataBaseInfoMapper.class);
            List<String> tableList = mapper.showTables();
            return ResponseResult.success(tableList,tableList.size());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null != sqlSession){
                sqlSession.close();
                dataSource.close();
            }
        }
        return ResponseResult.error("获取表失败！");
    }
    @Override
    public ResponseResult<List<String>> getTableCol(RequestData requestData) {
        SqlSession sqlSession = null;
        HikariDataSource dataSource = null;
        try{
            dataSource = MybatisUtil.getDataSource(requestData.getDriverClass(), requestData.getUrl(), requestData.getUserName(), requestData.getPassword());
            sqlSession = MybatisUtil.getSqlSession(dataSource);
            DataBaseInfoMapper mapper = sqlSession.getMapper(DataBaseInfoMapper.class);
            List<String> tableCol = mapper.getTableCol(requestData.getTableName());
            return ResponseResult.success(tableCol,tableCol.size());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null != sqlSession){
                sqlSession.close();
                dataSource.close();
            }
        }
        return ResponseResult.error("获取表字段失败！");
    }

    public Long getGraphTemplateCount(int id){
        QueryWrapper<GraphTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("data_base_id",id);
        Long count = graphTemplateMapper.selectCount(wrapper);
        return count;
    }

    @Override
    public ResponseResult<List<Map>> getVisualData(RequestData requestData) {
        if((null != requestData.getTableName()) && (!"".equals(requestData.getTableName()))){
            List<Map> maps = excelSourceMapper.selectExcelTableData(requestData.getTableName());
            return ResponseResult.success(maps,maps.size());
        }else {
            return ResponseResult.error("获取数据失败！");
        }
    }
}
