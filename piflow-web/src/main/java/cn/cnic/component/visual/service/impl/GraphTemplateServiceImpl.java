package cn.cnic.component.visual.service.impl;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.component.system.domain.SysUserDomain;
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
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

    private Logger logger = LoggerUtil.getLogger();

    private GraphTemplateMapper graphTemplateMapper;
    private GraphConfMapper graphConfMapper;
    private ExcelNameAssoMapper excelNameAssoMapper;
    private ExcelSourceServiceImpl excelSourceService;
    private ExcelSourceMapper excelSourceMapper;
    private SysUserDomain sysUserDomain;

    public GraphTemplateServiceImpl(GraphTemplateMapper graphTemplateMapper, GraphConfMapper graphConfMapper, ExcelNameAssoMapper excelNameAssoMapper, ExcelSourceServiceImpl excelSourceService, ExcelSourceMapper excelSourceMapper, SysUserDomain sysUserDomain) {
        this.graphTemplateMapper = graphTemplateMapper;
        this.graphConfMapper = graphConfMapper;
        this.excelNameAssoMapper = excelNameAssoMapper;
        this.excelSourceService = excelSourceService;
        this.excelSourceMapper = excelSourceMapper;
        this.sysUserDomain = sysUserDomain;
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
        if (StringUtils.isNotBlank(queryContent)) {
            wrapper.like("name", queryContent);
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
        } else if (StringUtils.equalsIgnoreCase(role, SysRoleType.ORS_ADMIN.getValue())) {  // 管理员看到自己所属单位的数据
            String userid = sysUserDomain.findUserByUserName(SessionUserUtil.getCurrentUser().getUsername()).getId();
            String company = sysUserDomain.getSysUserCompanyById(userid);
            wrapper.like("company", company);
        }
        else {  // 普通用户只能看到自己创建的数据
            wrapper.like("user_name", SessionUserUtil.getCurrentUsername());
        }
        // 按更新时间降序排序
        wrapper.orderByDesc("update_time");
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
        String userid = sysUserDomain.findUserByUserName(SessionUserUtil.getCurrentUser().getUsername()).getId();
        String company = sysUserDomain.getSysUserCompanyById(userid);
        graphTemplate.setCompany(company);
        graphTemplate.setUserName(SessionUserUtil.getCurrentUsername());
        logger.info("addGraphTemplate_create_user:{} ,company:{} " , SessionUserUtil.getCurrentUser().getUsername(), company);
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
        Long count = getGraphConfCunt(graphTemplate.getId());
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

    public long getGraphConfCunt(int id){
        QueryWrapper<GraphConf> wrapper = new QueryWrapper<>();
        wrapper.eq("graph_template_id",id);
        Integer count = graphConfMapper.selectCount(wrapper);
        return count;
    }
}
