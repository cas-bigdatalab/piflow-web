package cn.cnic.component.visual.service.impl;

import cn.cnic.component.visual.entity.ExcelNameAsso;
import cn.cnic.component.visual.entity.GraphConf;
import cn.cnic.component.visual.entity.GraphTemplate;
import cn.cnic.component.visual.mapper.piflowdata.ExcelSourceMapper;
import cn.cnic.component.visual.mapper.piflowdb.ExcelNameAssoMapper;
import cn.cnic.component.visual.mapper.piflowdb.GraphConfMapper;
import cn.cnic.component.visual.mapper.piflowdb.GraphTemplateMapper;
import cn.cnic.component.visual.service.GraphTemplateService;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * TODO
 * author:hmh
 * date:2024-02-02
 */
@Service
public class GraphTemplateServiceImpl implements GraphTemplateService {
    private GraphTemplateMapper graphTemplateMapper;
    private GraphConfMapper graphConfMapper;
    private ExcelNameAssoMapper excelNameAssoMapper;
    private ExcelSourceServiceImpl excelSourceService;
    private ExcelSourceMapper excelSourceMapper;

    public GraphTemplateServiceImpl(GraphTemplateMapper graphTemplateMapper, GraphConfMapper graphConfMapper, ExcelNameAssoMapper excelNameAssoMapper, ExcelSourceServiceImpl excelSourceService, ExcelSourceMapper excelSourceMapper) {
        this.graphTemplateMapper = graphTemplateMapper;
        this.graphConfMapper = graphConfMapper;
        this.excelNameAssoMapper = excelNameAssoMapper;
        this.excelSourceService = excelSourceService;
        this.excelSourceMapper = excelSourceMapper;
    }

    @Override
    public ResponseResult<List<GraphTemplate>> getGraphTemplateList(RequestData requestData) {
        String queryContent = requestData.getQueryContent();
        int pageSize = requestData.getPageSize();
        int pageNum = requestData.getPageNum();
        List<GraphTemplate> graphTemplates = null;
        //分页
        Page<GraphTemplate> page = new Page<>(pageNum,pageSize);
        QueryWrapper<GraphTemplate> wrapper = new QueryWrapper<>();
        //不为null时，拼接条件
        if((null != queryContent) && (!"".equals(queryContent.trim()))){
            wrapper.like("name",queryContent);
        }
        wrapper.orderByDesc("update_time");
        Page<GraphTemplate> page1 = graphTemplateMapper.selectPage(page, wrapper);
        graphTemplates = page1.getRecords();
        int total = Math.toIntExact(graphTemplateMapper.selectCount(wrapper));
        return ResponseResult.success(graphTemplates,total);
    }

    @Override
    public ResponseResult<GraphTemplate> getGraphTemplateInfo(GraphTemplate graphTemplate) {
        GraphTemplate graphTemplate1 = graphTemplateMapper.selectById(graphTemplate.getId());
        return ResponseResult.success(graphTemplate1);
    }

    @Override
    public ResponseResult addGraphTemplate(GraphTemplate graphTemplate) {
        //添加模板名称校验
        String templateName = graphTemplate.getName();
        if((null == templateName) || ("".equals(templateName))){
            return ResponseResult.error("该模板名称不能为空！");
        }else{
            //判断模板名是否已存在
            if(excelSourceService.checkTemplateName(templateName)){
                return ResponseResult.error("该模板名称已存在！");
            }
        }
        //校验dataBaseId
        if((null == graphTemplate.getDataBaseId()) || ("".equals(graphTemplate.getDataBaseId()))){
            return ResponseResult.error("该模板所用数据库不能为空！");
        }
        if((null == graphTemplate.getTableName()) || ("".equals(graphTemplate.getTableName()))){
            return ResponseResult.error("该模板所用表的表名不能为空！");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(formatter);
        graphTemplate.setCreateTime(format);
        graphTemplate.setUpdateTime(format);
        int insert = graphTemplateMapper.insert(graphTemplate);
        if(insert == 1){
            return ResponseResult.success();
        }else {
            return ResponseResult.error("新增图表模板失败！");
        }
    }
//    public ResponseResult addGraphConfByOne(GraphTemplate graphTemplate) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        String format = now.format(formatter);
//        graphTemplate.setCreateTime(format);
//        graphTemplate.setUpdateTime(format);
//        graphTemplate.setDataBaseId(1);
//        int insert = graphTemplateMapper.insert(graphTemplate);
//        if(insert == 1){
//            QueryWrapper<GraphTemplate> wrapper = new QueryWrapper<>();
//            wrapper.eq("name",graphTemplate.getName());
//            wrapper.eq("table_name",graphTemplate.getTableName());
//            wrapper.eq("description",graphTemplate.getDescription());
//            wrapper.eq("type",graphTemplate.getType());
//            wrapper.eq("data_base_id",1);//用本地数据库
//            wrapper.eq("update_time",format);
//            wrapper.eq("create_time",format);
//            GraphTemplate insertGraphTemplate = graphTemplateMapper.selectOne(wrapper);
//            //新增图表配置
//            GraphConf graphConf = new GraphConf();
//            graphConf.setCreateTime(format);
//            graphConf.setUpdateTime(format);
//            graphConf.setGraphTemplateId(insertGraphTemplate.getId());
//            graphConf.setName(insertGraphTemplate.getName());//用模板名
//            graphConf.setDescription(insertGraphTemplate.getDescription());//用模板配置
//            graphConf.setConfigInfo("");//用空值
//            int insert1 = graphConfMapper.insert(graphConf);
//            if(insert1 == 1){
//                return ResponseResult.success();
//            }else {
//                return ResponseResult.error("一键生成图表配置失败！");
//            }
//        }else {
//            return ResponseResult.error("一键生成图表配置失败！");
//        }
//    }
    @Transactional
    @Override
    public ResponseResult delGraphTemplate(GraphTemplate graphTemplate) {
        //判断图表模板中是否有使用该连接
        Integer count = getGraphConfCunt(graphTemplate.getId());
        if(count  ==  0){
            GraphTemplate graphTemplateDelete = graphTemplateMapper.selectById(graphTemplate.getId());
            Integer assoId = graphTemplateDelete.getExcelAssoId();
            String type = graphTemplateDelete.getType();
            //未使用则删除
            int insert = graphTemplateMapper.deleteById(graphTemplate.getId());
            if(insert == 1){
                //如果删除的是excel模板，则需要删除excel关联表信息，再删除excel表
                if((null != type) && ("excel".equals(type))){
                    ExcelNameAsso excelNameAsso = excelNameAssoMapper.selectById(assoId);
                    excelNameAssoMapper.deleteById(assoId);
                    excelSourceMapper.delDataTable(excelNameAsso.getAssoName());
                }
                return ResponseResult.success();
            }else {
                return ResponseResult.error("删除图表模板失败！");
            }
        }else {
            return ResponseResult.error("已经有图表使用该模板，无法删除！");
        }
    }

    @Override
    public ResponseResult updateGraphTemplate(GraphTemplate graphTemplate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(formatter);
        graphTemplate.setUpdateTime(format);
        int num = graphTemplateMapper.updateById(graphTemplate);
        if (num == 1) {
            return ResponseResult.success();
        } else {
            return ResponseResult.error("修改图表模板失败！");
        }
    }

    @Override
    public GraphTemplate selectById(Integer id) {
        return graphTemplateMapper.selectById(id);
    }

    public Integer getGraphConfCunt(int id){
        QueryWrapper<GraphConf> wrapper = new QueryWrapper<>();
        wrapper.eq("graph_template_id",id);
        Integer count = graphConfMapper.selectCount(wrapper);
        return count;
    }
}
