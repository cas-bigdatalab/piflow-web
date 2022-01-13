package cn.cnic.component.user.service;

import cn.cnic.base.vo.UserVo;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.component.schedule.mapper.provider.ScheduleMapperProvider;
import cn.cnic.component.schedule.vo.ScheduleVo;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.vo.SysUserVo;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AdminUserService {

    /**
     * Query getScheduleListPage (parameter space-time non-paging)
     *
     * @param isAdmin is admin
     * @param username   username
     * @param offset Number of pages
     * @param limit  Number each page
     * @param param  Search content
     * @return json
     */
    public String getUserListPage( String username,boolean isAdmin, Integer offset, Integer limit, String param);


    /**
     * get UserVo by id
     *
     * @param isAdmin is admin
     * @param username username
     * @param id       user id
     * @return json
     */
    public String getUserById(boolean isAdmin, String username, String id);

    /**
     * Update user
     *
     * @param isAdmin is admin
     * @param username   username
     * @param sysUserVo user
     * @return json
     */
    public String update(boolean isAdmin, String username, SysUserVo sysUserVo);

    /**
     * Delete user
     *
     * @param isAdmin is admin
     * @param username username
     * @param id       user id
     * @return json
     */
    public String delUser(boolean isAdmin, String username, String id);

}
