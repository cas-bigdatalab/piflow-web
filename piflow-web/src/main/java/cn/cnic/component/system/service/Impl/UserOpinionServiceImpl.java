package cn.cnic.component.system.service.Impl;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.PageHelperUtils;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.common.Eunm.UserOpinionState;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.dataProduct.entity.DataProduct;
import cn.cnic.component.system.domain.SysUserDomain;
import cn.cnic.component.system.domain.UserOpinionDomain;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.entity.UserOpinion;
import cn.cnic.component.system.service.IUserOpinionService;
import cn.cnic.component.system.vo.UserOpiniontVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static cn.cnic.common.Eunm.SysRoleType.ADMIN;


@Service
@SuppressWarnings("unused")
public class UserOpinionServiceImpl implements IUserOpinionService {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final SysUserDomain sysUserDomain;
    private final UserOpinionDomain userOpinionDomain;

    @Autowired
    public UserOpinionServiceImpl(SysUserDomain sysUserDomain, UserOpinionDomain userOpinionDomain){
        this.sysUserDomain = sysUserDomain;
        this.userOpinionDomain = userOpinionDomain;
    }

    @Override
    public String getByPage(UserOpiniontVo userOpiniontVo) {
        SysRoleType currentUserRole = SessionUserUtil.getCurrentUserRole();
        //台站管理员和普通用户只看到自己的
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        String id = sysUserDomain.findUserByUserName(currentUser.getUsername()).getId();
        String company = sysUserDomain.getSysUserCompanyById(id);
        if (currentUserRole == ADMIN) {
            Page<DataProduct> page = PageHelper.startPage(userOpiniontVo.getPage(), userOpiniontVo.getLimit(), "last_update_dttm desc");
            userOpinionDomain.getByPageWithAdmin(userOpiniontVo);
            Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("获取成功");
            return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
        } else {
            userOpiniontVo.setCrtUser(currentUser.getUsername());
            Page<DataProduct> page = PageHelper.startPage(userOpiniontVo.getPage(), userOpiniontVo.getLimit(), "last_update_dttm desc");
            userOpinionDomain.getByPage(userOpiniontVo);
            Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg("获取成功");
            return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
        }
    }

    @Override
    public String save(UserOpiniontVo userOpinionVo) {
        String name = userOpinionVo.getName();
        if (StringUtils.isBlank(name)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("标题不能为空");
        }
        String description = userOpinionVo.getDescription();
        if (StringUtils.isBlank(description)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("描述不能为空");
        }
        String username = SessionUserUtil.getCurrentUser().getUsername();
        String userid = sysUserDomain.findUserByUserName(username).getId();
        SysUser user = sysUserDomain.getSysUserById(userid);
        userOpinionVo.setCompany(StringUtils.defaultString(user.getCompany()));
        userOpinionVo.setOrganization(StringUtils.defaultString(user.getOrganization()));

        userOpinionVo.setState(UserOpinionState.TO_FINISH.getValue());
        String id = userOpinionVo.getId();

        if(StringUtils.isBlank(id)){
            //新增
            UserOpinion userOpinion = new UserOpinion();
            BeanUtils.copyProperties(userOpinionVo,userOpinion);
            userOpinion.setEnableFlag(true);
            userOpinion.setCrtUser(username);
            userOpinion.setLastUpdateUser(username);
            userOpinionDomain.save(userOpinion);
        }else {
            //更新
            UserOpinion userOpinion = userOpinionDomain.getById(id);
            userOpinion.setName(name);
            userOpinion.setDescription(description);
            userOpinion.setEnableFlag(true);
            userOpinion.setLastUpdateUser(username);
            userOpinion.setLastUpdateDttm(new Date());
            userOpinionDomain.update(userOpinion);
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("保存成功");
    }

    @Override
    public String delete(String id) {
        int i = userOpinionDomain.delete(id);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("删除成功");
    }

    @Override
    public String updateState(UserOpiniontVo userOpinionVo) {
        int i = userOpinionDomain.updateState(userOpinionVo);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("更新成功");
    }
}
