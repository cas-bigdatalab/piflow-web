package cn.cnic.component.system.service;

import cn.cnic.component.system.vo.SysUserVo;
import org.springframework.stereotype.Service;


@Service
public interface ISysUserService {

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
    public String getUserListPage( String username,boolean isAdmin, Integer offset, Integer limit, String param, String name, String email, String company);


    /**
     * get UserVo by id
     *
     * @param isAdmin is admin
     * @param username username
     * @param id       user id
     * @return json
     */
    public String getUserById(boolean isAdmin, String username, String id);

    String getByUsername();

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
     * Update user
     *
     * @param username   username
     * @param oldPassword   old password
     * @param password   new  password
     * @return json
     */
    public String updatePassword(String username, String oldPassword, String password);

    /**
     * Delete user
     *
     * @param isAdmin is admin
     * @param username username
     * @param id       user id
     * @return json
     */
    public String delUser(boolean isAdmin, String username, String id);

    public String checkUserName(String username);

    public String registerUser(SysUserVo sysUserVo, String ecosystemTypeIds);

    public String jwtLogin(String username, String password);

    String beforeLogin(String username);

    public int deleteUser(String id);

    public String bindDeveloperAccessKey(boolean isAdmin, String username, String accessKey);

    String updateInfo(SysUserVo sysUserVo);

    String addUser(SysUserVo sysUserVo);
    String updateRole(SysUserVo sysUserVo);

    String getAllRole();
    String jwtLogout(String username);
}
