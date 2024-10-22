package cn.cnic.component.visual.service.impl;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.component.system.domain.SysUserDomain;
import cn.cnic.component.visual.entity.GraphConf;
import cn.cnic.component.visual.mapper.piflowdb.GraphConfMapper;
import cn.cnic.component.visual.service.GraphConfService;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private SysUserDomain sysUserDomain;


    @Override
    public ResponseResult<List<GraphConf>> getGraphConfList(RequestData requestData) {
        String queryContent = requestData.getQueryContent();
        int pageSize = requestData.getPageSize();
        int pageNum = requestData.getPageNum();
        List<GraphConf> list = null;
        //分页
        Page<GraphConf> page = new Page<>(pageNum,pageSize);
        QueryWrapper<GraphConf> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(queryContent)) {
            wrapper.like("name", queryContent);
        }
        if (StringUtils.isNotBlank(requestData.getName())) {
            wrapper.like("name", requestData.getName());
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
        Page<GraphConf> page1 = graphConfMapper.selectPage(page,wrapper);
        list = page1.getRecords();
        int total = Math.toIntExact(graphConfMapper.selectCount(wrapper));
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
        String userid = sysUserDomain.findUserByUserName(SessionUserUtil.getCurrentUser().getUsername()).getId();
        graphConf.setCompany(sysUserDomain.getSysUserCompanyById(userid));
        graphConf.setUserName(SessionUserUtil.getCurrentUsername());
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
