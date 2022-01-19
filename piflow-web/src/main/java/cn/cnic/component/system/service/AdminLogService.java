package cn.cnic.component.system.service;

import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.system.entity.SysLog;

public interface AdminLogService {

    /**
     * @param username username
     * @param isAdmin admin
     * @param offset offset
     * @param limit limit
     * @param param param
     * @return
     */
    public String getLogListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param);

    public void add(SysLog log);

}
