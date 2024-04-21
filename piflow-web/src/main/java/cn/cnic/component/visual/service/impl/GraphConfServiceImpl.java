package cn.cnic.component.visual.service.impl;

import cn.cnic.component.visual.entity.GraphConf;
import cn.cnic.component.visual.mapper.piflowdb.GraphConfMapper;
import cn.cnic.component.visual.service.GraphConfService;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * TODO
 * author:hmh
 * date:2023-09-21
 */
@Service
public class GraphConfServiceImpl implements GraphConfService {

    @Autowired
    private GraphConfMapper graphConfMapper;


    @Override
    public ResponseResult<List<GraphConf>> getGraphConfList(RequestData requestData) {
        String queryContent = requestData.getQueryContent();
        int pageSize = requestData.getPageSize();
        int pageNum = requestData.getPageNum();
        List<GraphConf> list = null;
        //分页
        Page<GraphConf> page = new Page<>(pageNum,pageSize);
        QueryWrapper<GraphConf> wrapper = new QueryWrapper<>();
        //不为null时，拼接条件
        if((null != queryContent) && (!"".equals(queryContent.trim()))){
            wrapper.like("name",queryContent);
        }
        wrapper.orderByDesc("update_time");
        Page<GraphConf> page1 = graphConfMapper.selectPage(page,wrapper);
        list = page1.getRecords();
        int total = (int)page1.getTotal();
        return ResponseResult.success(list,total);
    }
    @Override
    public ResponseResult<GraphConf> getGraphConfById(GraphConf graphConf) {
        GraphConf graphConf2 = graphConfMapper.selectById(graphConf.getId());
        return ResponseResult.success(graphConf2,1);
    }

    @Override
    public ResponseResult addGraphConf(GraphConf graphConf) {
        String name = graphConf.getName();
        //校验名称不能重复
        if((null == name) || ("".equals(name))){
            return ResponseResult.error("该图表配置的名称不能为空！");
        }else{
            //判断图表配置的名称是否已存在
            QueryWrapper<GraphConf> wrapper = new QueryWrapper<>();
            wrapper.eq("name",name);
            GraphConf graphConfTemp = graphConfMapper.selectOne(wrapper);
            if(null != graphConfTemp){
                return ResponseResult.error("该图表配置的名称已存在！");
            }
        }
        if((null == graphConf.getGraphTemplateId()) || ("".equals(graphConf.getGraphTemplateId()))){
            return ResponseResult.error("该图表配置所用模板不能为空！");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(formatter);
        graphConf.setCreateTime(format);
        graphConf.setUpdateTime(format);
        int insert = graphConfMapper.insert(graphConf);
        Integer id = graphConf.getId();
        if(insert == 1){
            return ResponseResult.success(id);
        }else {
            return ResponseResult.error("新增配置失败！");
        }
    }

    @Override
    public ResponseResult delGraphConf(GraphConf graphConf) {
        int num = graphConfMapper.deleteById(graphConf.getId());
        if(num == 1){
            return ResponseResult.success();
        }else {
            return ResponseResult.error("删除配置失败！");
        }
    }

    @Override
    public ResponseResult updateGraphConf(GraphConf graphConf) {
        String addFlag = graphConf.getAddFlag();
        if((null == addFlag) || (addFlag.equals(""))){
            return ResponseResult.error("修改配置失败！");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(formatter);
        //标记为--新增
        if(addFlag.equals("add")){
            GraphConf graphConfNew = new GraphConf();
            graphConfNew.setUpdateTime(format);
            graphConfNew.setCreateTime(format);
            graphConfNew.setName(graphConf.getName());
            graphConfNew.setGraphTemplateId(graphConf.getGraphTemplateId());
            graphConfNew.setDescription(graphConf.getDescription());
            graphConfNew.setConfigInfo(graphConf.getConfigInfo());
            int insert = graphConfMapper.insert(graphConfNew);
            if(insert == 1){
                return ResponseResult.success();
            }else {
                return ResponseResult.error("新增配置失败！");
            }
        }else if(addFlag.equals("update")){
            //标记为--修改
            graphConf.setUpdateTime(format);
            int num = graphConfMapper.updateById(graphConf);
            if(num == 1){
                return ResponseResult.success();
            }else {
                return ResponseResult.error("修改配置失败！");
            }
        }else {
            return ResponseResult.error("修改配置失败！");
        }
    }

//    @Override
//    public ResponseResult<List<GraphConf>> getGraphConfByNameAndId(GraphConf graphConf) {
//        //加上查询条件
//        QueryWrapper queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("name",graphConf.getName());
//        queryWrapper.eq("data_base_id",graphConf.getDataBaseId());
//        List<GraphConf> list = graphConfMapper.selectList(queryWrapper);
//        return ResponseResult.success(list,list.size());
//    }

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(formatter);
        System.out.println(format);
    }
}
