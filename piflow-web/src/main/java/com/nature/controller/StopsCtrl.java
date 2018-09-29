package com.nature.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nature.component.workFlow.model.StopGroup;

@Controller
@RequestMapping("/stops/*")
public class StopsCtrl {

	public List<StopGroup> getStopGroup() {
		return null;
	}

}
