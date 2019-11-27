package com.nature.third.vo.flowGroup;

import com.nature.third.vo.flowInfo.ThirdFlowInfoStopsVo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.resource.HttpResource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ThirdFlowGroupInfoResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String state;
	private String startTime;
	private String endTime;
	private String progress;

	List<ThirdFlowInfoOutResponse> flows = new ArrayList<>();

}
