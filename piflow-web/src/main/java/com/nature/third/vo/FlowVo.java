package com.nature.third.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlowVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String uuid;
	private List<StopVo> stops = new ArrayList<StopVo>();
	private List<PathVo> paths = new ArrayList<PathVo>();

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

	public List<StopVo> getStops() {
		return stops;
	}

	public void setStops(List<StopVo> stops) {
		this.stops = stops;
	}

	public List<PathVo> getPaths() {
		return paths;
	}

	public void setPaths(List<PathVo> paths) {
		this.paths = paths;
	}

}
