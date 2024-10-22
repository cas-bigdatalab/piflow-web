package cn.cnic.component.visual.service.impl;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.component.system.domain.SysUserDomain;
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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private SysUserDomain sysUserDomain;
//    @Override
//    public ResponseResult<List<DataBaseInfo>> getDatabaseList(RequestData requestData) {
//        String queryContent = requestData.getQueryContent();
//        int pageSize = requestData.getPageSize();
//        int pageNum = requestData.getPageNum();
//        List<DataBaseInfo> dataBaseInfos = null;
//        //分页
//        Page<DataBaseInfo> page = new Page<>(pageNum,pageSize);
//        QueryWrapper<DataBaseInfo> wrapper = new QueryWrapper<>();
//        if(StringUtils.isNotBlank(queryContent)){
//            wrapper.like("db_name",queryContent);
//        }
//        if (StringUtils.isNotBlank(requestData.getName())) {
//            wrapper.like("description", requestData.getName());
//        }
//        if (StringUtils.isNotBlank(requestData.getCreateUser())) {
//            wrapper.like("user_name", requestData.getCreateUser());
//        }
//        if (StringUtils.isNotBlank(requestData.getCompany())) {
//            String role = SessionUserUtil.getCurrentUserRole().getValue();
//            if (StringUtils.equalsIgnoreCase(role, SysRoleType.ADMIN.getValue())) {
//                wrapper.like("company", requestData.getCompany());
//            }
//            // 台站管理员只能看到本台站
//            if (StringUtils.equalsIgnoreCase(role, SysRoleType.ORS_ADMIN.getValue())) {
//                String userid = sysUserDomain.findUserByUserName(SessionUserUtil.getCurrentUser().getUsername()).getId();
//                String company = sysUserDomain.getSysUserCompanyById(userid);
//                wrapper.like("company", company);
//            }
//        }
//        wrapper.orderByDesc("update_time");
//        Page<DataBaseInfo> page1 = dataBaseInfoMapper.selectPage(page,wrapper);
//        dataBaseInfos = page1.getRecords();
//        int total = Math.toIntExact(dataBaseInfoMapper.selectCount(wrapper));
//        return ResponseResult.success(dataBaseInfos,total);
//    }

    @Override
    public ResponseResult<List<DataBaseInfo>> getDatabaseList(RequestData requestData) {
        String queryContent = requestData.getQueryContent();
        int pageSize = requestData.getPageSize();
        int pageNum = requestData.getPageNum();

        Page<DataBaseInfo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<DataBaseInfo> wrapper = new QueryWrapper<>();

        // 模糊查询条件
        if (StringUtils.isNotBlank(queryContent)) {
            wrapper.like("db_name", queryContent);
        }
        if (StringUtils.isNotBlank(requestData.getName())) {
            wrapper.like("description", requestData.getName());
        }
        if (StringUtils.isNotBlank(requestData.getCreateUser())) {
            wrapper.like("user_name", requestData.getCreateUser());
        }
        String role = SessionUserUtil.getCurrentUserRole().getValue();
        //管理员可以看到所有数据，且可以按单位搜索
        if (StringUtils.equalsIgnoreCase(role, SysRoleType.ADMIN.getValue())) {
            if (StringUtils.isNotBlank(requestData.getCompany())) {
                wrapper.like("company", requestData.getCompany());
            }
        } else { // 其他角色只能看到自己所属单位的数据
            String userid = sysUserDomain.findUserByUserName(SessionUserUtil.getCurrentUser().getUsername()).getId();
            String company = sysUserDomain.getSysUserCompanyById(userid);
            wrapper.like("company", company);
        }
        // 按更新时间降序排序
        wrapper.orderByDesc("update_time");

        // 执行分页查询
        Page<DataBaseInfo> page1 = dataBaseInfoMapper.selectPage(page, wrapper);
        List<DataBaseInfo> dataBaseInfos = page1.getRecords();
        int total = Math.toIntExact(page1.getTotal());

        // 返回分页结果
        return ResponseResult.success(dataBaseInfos, total);
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
        String userid = sysUserDomain.findUserByUserName(SessionUserUtil.getCurrentUser().getUsername()).getId();
        String company = sysUserDomain.getSysUserCompanyById(userid);
        dataBaseInfo.setCompany(company);
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

    public long getGraphTemplateCount(int id){
        QueryWrapper<GraphTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("data_base_id",id);
        Integer count = graphTemplateMapper.selectCount(wrapper);
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
