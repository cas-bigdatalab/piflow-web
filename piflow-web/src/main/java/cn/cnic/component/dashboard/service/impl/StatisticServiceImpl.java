package cn.cnic.component.dashboard.service.impl;

import cn.cnic.component.dashboard.mapper.StatisticMapper;
import cn.cnic.component.dashboard.service.IStatisticService;
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
        List<Map<String, String>> processStatisticList= statisticMapper.getFlowProcessStatisticInfo();
        Map<String, String> processInfoMap = convertList2Map(processStatisticList, "STATE", "COUNT");
        Map<String, String> flowInfoMap = new HashMap<>();
        flowInfoMap.put("PROCESSOR_STARTED_COUNT", processInfoMap.getOrDefault("STARTED","0"));
        flowInfoMap.put("PROCESSOR_COMPETED_COUNT", processInfoMap.getOrDefault("COMPLETED","0"));
        flowInfoMap.put("PROCESSOR_FAILED_COUNT", processInfoMap.getOrDefault("FAILED","0"));
        flowInfoMap.put("PROCESSOR_KILLED_COUNT", processInfoMap.getOrDefault("KILLED","0"));
        int otherStateCount = flowInfoMap.values().stream().mapToInt(Integer::parseInt).sum();
        int processorCount = processInfoMap.values().stream().mapToInt(Integer::parseInt).sum();
        flowInfoMap.put("PROCESSOR_OTHER_COUNT", String.valueOf(processorCount - otherStateCount));
        flowInfoMap.put("PROCESSOR_COUNT", String.valueOf(processorCount));
        flowInfoMap.put("FLOW_COUNT", String.valueOf(statisticMapper.getFlowCount()));
        return flowInfoMap;
    }

    @Override
    public Map<String, String> getGroupStatisticInfo() {
        List<Map<String, String>> processStatisticList= statisticMapper.getGroupProcessStatisticInfo();
        Map<String, String> processInfoMap = convertList2Map(processStatisticList, "STATE", "COUNT");
        Map<String, String> groupInfoMap = new HashMap<>();
        groupInfoMap.put("PROCESSOR_STARTED_COUNT", processInfoMap.getOrDefault("STARTED","0"));
        groupInfoMap.put("PROCESSOR_COMPETED_COUNT", processInfoMap.getOrDefault("COMPLETED","0"));
        groupInfoMap.put("PROCESSOR_FAILED_COUNT", processInfoMap.getOrDefault("FAILED","0"));
        groupInfoMap.put("PROCESSOR_KILLED_COUNT", processInfoMap.getOrDefault("KILLED","0"));
        int otherStateCount = groupInfoMap.values().stream().mapToInt(Integer::parseInt).sum();
        int processorCount = processInfoMap.values().stream().mapToInt(Integer::parseInt).sum();
        groupInfoMap.put("PROCESSOR_OTHER_COUNT", String.valueOf(processorCount - otherStateCount));
        groupInfoMap.put("PROCESSOR_COUNT", String.valueOf(processorCount));

        groupInfoMap.put("GROUP_COUNT", String.valueOf(statisticMapper.getGroupCount()));
        return groupInfoMap;
    }

    @Override
    public Map<String, String> getScheduleStatisticInfo() {
        List<Map<String, String>> scheduleStatisticList= statisticMapper.getScheduleStatisticInfo();
        Map<String, String> scheduleStatusMap = convertList2Map(scheduleStatisticList, "STATUS", "COUNT");
        int scheduleCount = scheduleStatusMap.values().stream().mapToInt(Integer::parseInt).sum();
        Map<String, String> scheduleInfoMap = new HashMap<>();
        scheduleInfoMap.put("SCHEDULE_COUNT", String.valueOf(scheduleCount));
        scheduleInfoMap.put("SCHEDULE_INIT_COUNT", scheduleStatusMap.getOrDefault("INIT","0"));
        scheduleInfoMap.put("SCHEDULE_RUNNING_COUNT", scheduleStatusMap.getOrDefault("RUNNING","0"));
        scheduleInfoMap.put("SCHEDULE_STOP_COUNT", scheduleStatusMap.getOrDefault("STOP","0"));
        return scheduleInfoMap;

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
