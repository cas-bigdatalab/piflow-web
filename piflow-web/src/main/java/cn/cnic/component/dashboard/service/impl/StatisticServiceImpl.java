package cn.cnic.component.dashboard.service.impl;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.component.dashboard.mapper.StatisticMapper;
import cn.cnic.component.dashboard.service.IResourceService;
import cn.cnic.component.dashboard.service.IStatisticService;
import cn.cnic.third.service.IResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements IStatisticService {


    @Resource
    private StatisticMapper statisticMapper;

    @Override
    public Map<String,String> getFlowStatisticInfo() {
        List<Map<String, String>> ProcessStatisticList= statisticMapper.getFlowProcessStatisticInfo();
        Map<String, String> processInfoMap = convertList2Map(ProcessStatisticList, "state", "count");
        processInfoMap.put("PROCESSOR_COUNT", String.valueOf(processInfoMap.values().stream().mapToInt(Integer::parseInt).sum()));
        Map<String, String> flowInfoMap = new HashMap<>();
        flowInfoMap.put("FLOW_COUNT", String.valueOf(statisticMapper.getFlowCount()));
        flowInfoMap.putAll(processInfoMap);
        return flowInfoMap;
    }

    @Override
    public Map<String, String> getGroupStatisticInfo() {
        List<Map<String, String>> ProcessStatisticList= statisticMapper.getGroupProcessStatisticInfo();
        Map<String, String> processInfoMap = convertList2Map(ProcessStatisticList, "state", "count");
        processInfoMap.put("PROCESSOR_COUNT", String.valueOf(processInfoMap.values().stream().mapToInt(Integer::parseInt).sum()));
        Map<String, String> flowInfoMap = new HashMap<>();
        flowInfoMap.put("GROUP_COUNT", String.valueOf(statisticMapper.getGroupCount()));
        flowInfoMap.putAll(processInfoMap);
        return flowInfoMap;
    }

    @Override
    public Map<String, String> getScheduleStatisticInfo() {
        return null;
    }

    @Override
    public Map<String, String> getTemplateAndDataSourceStatisticInfo() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("TEMPLATE_COUNT", String.valueOf(statisticMapper.getTemplateCount()));
        infoMap.put("DATASOURCE_COUNT", String.valueOf(statisticMapper.getDataSourceCount()));
        infoMap.put("STOPSHUB_COUNT",  String.valueOf(statisticMapper.getStopsHubCount()));
        return infoMap;
    }

    @Override
    public Map<String, String> getStopStatisticInfo() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("STOP_COUNT", String.valueOf(statisticMapper.getStopsCount()));
        infoMap.put("STOPGROUP_COUNT", String.valueOf(statisticMapper.getStopsGroupCount()));
        return infoMap;
    }

    private Map<String, String> convertList2Map(List<Map<String, String>> sqlResult, String key, String value){
        Map<String, String> result = sqlResult.stream().collect(Collectors.toMap(s -> (String)s.get(key), s -> String.valueOf(s.get(value))));
        return result;

    }
}
