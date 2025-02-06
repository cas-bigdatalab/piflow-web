package cn.cnic.component.system.service;

import cn.cnic.component.system.vo.UserOpiniontVo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IUserOpinionService {
    /**
     * 分页查询
     * 中心管理员查询全部的，非中心管理员查询个人的
     *
     * @param userOpiniontVo
     * @return
     */
    String getByPage(UserOpiniontVo userOpiniontVo);


    /**
     * 新增或编辑用户反馈，表单输入
     *
     * @param userOpinionVo
     * @return
     */
    String save(UserOpiniontVo userOpinionVo);

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 删除
     */
    String delete(String id);


    /**
     * 中心管理员功能
     *
     * @param userOpinionVo
     * @return
     */
    String updateState(UserOpiniontVo userOpinionVo);
}
