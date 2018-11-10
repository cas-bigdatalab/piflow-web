package com.nature.third.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ThirdFlowVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String uuid;
	private List<ThirdStopVo> stops = new ArrayList<ThirdStopVo>();
	private List<ThirdPathVo> paths = new ArrayList<ThirdPathVo>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<ThirdStopVo> getStops() {
		return stops;
	}

	public void setStops(List<ThirdStopVo> stops) {
		this.stops = stops;
	}

	public List<ThirdPathVo> getPaths() {
		return paths;
	}

	public void setPaths(List<ThirdPathVo> paths) {
		this.paths = paths;
	}

}
