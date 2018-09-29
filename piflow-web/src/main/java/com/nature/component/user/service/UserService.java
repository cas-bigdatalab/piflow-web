package com.nature.component.user.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.user.model.User;
import com.nature.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	public List<User> findByName(String name) {
		if (StringUtils.isBlank(name)) {
			name = "";
		}
		return userMapper.findUserByName(name);
	}

	public List<User> getUserList() {
		List<User> listUser = userMapper.getUserList();
		return listUser;
	}

	public User addUser(User user) {
		userMapper.addUser(user);
		return user;
	}

	public int saveOrUpdate(User user) {
		int update = userMapper.saveOrUpdate(user);
		return update;
	}

	public int deleteUser(int id) {
		int delete = userMapper.deleteUser(id);
		return delete;
	}
}
