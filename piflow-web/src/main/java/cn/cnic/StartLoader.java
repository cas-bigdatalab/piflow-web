package cn.cnic;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import cn.cnic.base.utils.CheckPathUtils;
import cn.cnic.base.utils.QuartzUtils;
import cn.cnic.common.Eunm.ComponentFileType;
import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import cn.cnic.component.stopsComponent.entity.StopsHub;
import cn.cnic.component.stopsComponent.mapper.StopsComponentMapper;
import cn.cnic.component.stopsComponent.mapper.StopsHubMapper;
import cn.cnic.component.stopsComponent.service.IStopsHubService;
import cn.cnic.component.system.entity.SysSchedule;
import cn.cnic.component.system.mapper.SysScheduleMapper;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Order(value = 1)
public class StartLoader implements ApplicationRunner {

    private final SysScheduleMapper sysScheduleMapper;
    private final Scheduler scheduler;
    private final StopsHubMapper stopsHubMapper;

    private final IStopsHubService stopsHubService;
    private final StopsComponentMapper stopsComponentMapper;

    @Autowired
    public StartLoader(SysScheduleMapper sysScheduleMapper, Scheduler scheduler, StopsHubMapper stopsHubMapper, IStopsHubService stopsHubService, StopsComponentMapper stopsComponentMapper) {
        this.sysScheduleMapper = sysScheduleMapper;
        this.scheduler = scheduler;
        this.stopsHubMapper = stopsHubMapper;
        this.stopsHubService = stopsHubService;
        this.stopsComponentMapper = stopsComponentMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        checkStoragePath();
        startStatusRunning();
        checkTableValueForPython();
    }

    private void checkStoragePath() {
        CheckPathUtils.isChartPathExist(SysParamsCache.IMAGES_PATH);
        CheckPathUtils.isChartPathExist(SysParamsCache.VIDEOS_PATH);
        CheckPathUtils.isChartPathExist(SysParamsCache.XML_PATH);
        CheckPathUtils.isChartPathExist(SysParamsCache.CSV_PATH);
    }

    private void startStatusRunning() {
        List<SysSchedule> sysScheduleByStatusList = sysScheduleMapper.getSysScheduleListByStatus(true, ScheduleState.RUNNING);
        if (null != sysScheduleByStatusList && sysScheduleByStatusList.size() > 0) {
            for (SysSchedule sysSchedule : sysScheduleByStatusList) {
                JobDetail scheduleJobByJobName = QuartzUtils.getScheduleJobByJobName(scheduler, sysSchedule.getJobName());
                if (null != scheduleJobByJobName) {
                    continue;
                }
                QuartzUtils.createScheduleJob(scheduler, sysSchedule);
            }
        }
    }

    private void checkTableValueForPython() {
    /* template关于stops_hub_id  需要在starter里加 通过component_type 空，就先去unmount 再重新执行mount操作  或者
条件 ：component_type 是否为空
空的话，先查询stops)_hub，unmount   重新mount   把jar包的补上了，  然后把template中的其他为空的填DEFAULT  -》升级可以用
初始化部署：init   要改
 reload    先判断stops_hub是否有值，没有值没有jar包，都是默认组件，查看comp_type   是否为空，为空填上default；有jar包先UNmount再mount然后填，查看compone_type有没有空，有空填DEFAULT
*/
        List<StopsHub> stopsHubs = stopsHubMapper.getAllStopsHub();
        if ((stopsHubs.size() > 0) && (stopsHubs.stream().map(StopsHub::getType).collect(Collectors.toList()).size() > 0)) {

        } else {
            List<StopsComponent> stopsComponentList = stopsComponentMapper.getStopsComponentList();
            if (stopsHubs.size() > 0) {
                //type is null
                //update scala component type
                List<String> bundles = stopsHubs.stream().map(StopsHub::getBundles).collect(Collectors.toList());
                List<StopsComponent> scalaStopsComponents = stopsComponentList.stream()
                        .filter(stopsComponent -> bundles.contains(stopsComponent.getBundel()))
                        .map(stopsComponent -> {
                            stopsComponent.setComponentType(ComponentFileType.SCALA);
                            stopsComponent.setLastUpdateDttm(new Date());
                            return stopsComponent;
                        }).collect(Collectors.toList());
                List<String> scalaStopsComponentIds = scalaStopsComponents.stream().map(StopsComponent::getId).collect(Collectors.toList());
                List<StopsComponent> defaultStopsComponents = stopsComponentList.stream()
                        .filter(stopsComponent -> !scalaStopsComponentIds.contains(stopsComponent.getId()))
                        .map(stopsComponent -> {
                            stopsComponent.setComponentType(ComponentFileType.DEFAULT);
                            stopsComponent.setLastUpdateDttm(new Date());
                            return stopsComponent;
                        })
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(scalaStopsComponents)){
                    stopsComponentMapper.updateComponentTypeByIdAndType(scalaStopsComponents);
                }
                if(CollectionUtils.isNotEmpty(defaultStopsComponents)){
                    stopsComponentMapper.updateComponentTypeByIdAndType(defaultStopsComponents);
                }
                //update stops_hub_type
                List<StopsHub> scalaStopsHubs = stopsHubs.stream().map(stopsHub -> {
                    stopsHub.setType(ComponentFileType.SCALA);
                    stopsHub.setLastUpdateDttm(new Date());
                    return stopsHub;
                }).collect(Collectors.toList());
                stopsHubMapper.updateStopHubType(scalaStopsHubs);
            } else {
                if(CollectionUtils.isNotEmpty(stopsComponentList)){
                    List<StopsComponent> defaultStopsComponents = stopsComponentList.stream().map(stopsComponent -> {
                        stopsComponent.setComponentType(ComponentFileType.DEFAULT);
                        stopsComponent.setLastUpdateDttm(new Date());
                        return stopsComponent;
                    }).collect(Collectors.toList());
                    stopsComponentMapper.updateComponentTypeByIdAndType(defaultStopsComponents);
                }
            }
        }

    }

}
