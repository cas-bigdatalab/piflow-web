package cn.cnic.controller;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.system.model.SysUser;
import cn.cnic.component.system.service.ISysUserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wangwei on 2016/9/2.
 */
@RestController
@RequestMapping({ "/home" })
public class UserCtrl {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
	Logger logger = LoggerUtil.getLogger();

	@Autowired
	private ISysUserService userServiceImpl;

	@RequestMapping(value = "/user")
	@ResponseBody
	public List<SysUser> user(String name) {
		List<SysUser> userList = userServiceImpl.findByName(name);
		logger.info("111111111111111111111111111");
		return userList;
	}

	@RequestMapping(value = "/addUser")
	public SysUser addUser(SysUser user) {
		return userServiceImpl.addUser(user);
	}

	@RequestMapping(value = "/deleteUser")
	public String deleteUser(String id) {
		String resultStr = "failed to delete";
		int result = userServiceImpl.deleteUser(id);
		if (1 <= result) {
			resultStr = "successfully deleted";
		}
		return resultStr;
	}

	@RequestMapping(value = "/saveOrUpdate")
	public String saveOrUpdate(SysUser user) {
		String resultStr = "Update operation failed";
		int result = userServiceImpl.saveOrUpdate(user);
		if (1 <= result) {
			resultStr = "Update operation succeeded";
		}
		return resultStr;
	}

	@RequestMapping(value = "/getUserList")
	@ResponseBody
	public List<SysUser> getUserList() {
		return userServiceImpl.getUserList();
	}

}
