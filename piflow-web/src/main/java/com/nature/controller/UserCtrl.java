package com.nature.controller;

import com.nature.base.util.LoggerUtil;
import com.nature.component.sysUser.model.SysUser;
import com.nature.component.sysUser.service.ISysUserService;
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
	ISysUserService userServiceImpl;

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
		String resultStr = "删除失败";
		int result = userServiceImpl.deleteUser(id);
		if (1 <= result) {
			resultStr = "删除 成功";
		}
		return resultStr;
	}

	@RequestMapping(value = "/saveOrUpdate")
	public String saveOrUpdate(SysUser user) {
		String resultStr = "更新操作失败";
		int result = userServiceImpl.saveOrUpdate(user);
		if (1 <= result) {
			resultStr = "更新操作成功";
		}
		return resultStr;
	}

	@RequestMapping(value = "/getUserList")
	@ResponseBody
	public List<SysUser> getUserList() {
		return userServiceImpl.getUserList();
	}

}
//@RestController是对应的restful风格的控制器，@RequestMapping里面可以对应一个数组哦
