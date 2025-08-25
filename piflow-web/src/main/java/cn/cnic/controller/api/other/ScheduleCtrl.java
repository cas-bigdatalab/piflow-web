package cn.cnic.controller.api.other;

import cn.cnic.component.schedule.dto.MessageConfigDTO;
import cn.cnic.component.schedule.listener.IMessageConfig;
import cn.cnic.component.schedule.vo.FileScheduleVo;
import cn.cnic.component.system.service.ILogHelperService;
import cn.cnic.component.visual.util.ResponseResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.schedule.service.IScheduleService;
import cn.cnic.component.schedule.vo.ScheduleVo;
import io.swagger.annotations.Api;
import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;
import cn.cnic.component.schedule.service.Impl.MessageSourceConfigService;
import cn.cnic.component.schedule.service.Impl.TaskTriggerInstanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import cn.cnic.component.schedule.vo.MessageSourceConfigVo;
import cn.cnic.component.schedule.vo.TaskTriggerInstanceVo;
;

@Api(value = "schedule api",tags = "schedule api")
@RestController
@RequestMapping("/schedule")
public class ScheduleCtrl {

    private final IScheduleService scheduleServiceImpl;
    private final ILogHelperService logHelperServiceImpl;
    private final MessageSourceConfigService messageSourceConfigService;
    private final TaskTriggerInstanceService taskTriggerInstanceService;

    @Autowired
    public ScheduleCtrl(IScheduleService scheduleServiceImpl, ILogHelperService logHelperServiceImpl,
                        MessageSourceConfigService messageSourceConfigService,
                        TaskTriggerInstanceService taskTriggerInstanceService) {
        this.scheduleServiceImpl = scheduleServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
        this.messageSourceConfigService = messageSourceConfigService;
        this.taskTriggerInstanceService = taskTriggerInstanceService;
    }

    /**
     * Query and enter the scheduleVo list
     *
     * @param page  page number
     * @param limit page size
     * @param param search param
     * @return json
     */
    @RequestMapping(value = "/getScheduleVoListPage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="getScheduleVoListPage", notes="get ScheduleVo list")
    public String getScheduleVoListPage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return scheduleServiceImpl.getScheduleVoListPage(isAdmin, username, page, limit, param);
    }

    /**
     * create schedule
     *
     * @param scheduleVo
     * @return
     */
    @RequestMapping(value = "/addSchedule", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="addSchedule", notes="add Schedule")
    public String addSchedule(ScheduleVo scheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("addSchedule " + scheduleVo.getScheduleRunTemplateName(),username);
        return scheduleServiceImpl.addSchedule(username, scheduleVo);
    }

    /**
     * get Schedule by id
     *
     * @param scheduleId
     * @return
     */
    @RequestMapping(value = "/getScheduleById", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="getScheduleById", notes="get Schedule by id")
    public String getScheduleById(String scheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return scheduleServiceImpl.getScheduleVoById(isAdmin, username, scheduleId);
    }

    /**
     * update schedule
     *
     * @param scheduleVo
     * @return
     */
    @RequestMapping(value = "/updateSchedule", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="updateSchedule", notes="update Schedule")
    public String updateSchedule(ScheduleVo scheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("updateSchedule " + scheduleVo.getScheduleRunTemplateName(),username);
        return scheduleServiceImpl.updateSchedule(isAdmin, username, scheduleVo);
    }

    /**
     * del schedule
     *
     * @param scheduleId
     * @return
     */
    @RequestMapping(value = "/delSchedule", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="delSchedule", notes="delete Schedule")
    public String delSchedule(String scheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("updateSchedule " + scheduleId,username);
        return scheduleServiceImpl.delSchedule(isAdmin, username, scheduleId);
    }


    /**
     * update schedule
     *
     * @param scheduleId
     * @return
     */
    @RequestMapping(value = "/startSchedule", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="startSchedule", notes="start Schedule")
    public String startSchedule(String scheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return scheduleServiceImpl.startSchedule(isAdmin, username, scheduleId);
    }

    /**
     * update schedule
     *
     * @param scheduleId
     * @return
     */
    @RequestMapping(value = "/stopSchedule", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="stopSchedule", notes="stop Schedule")
    public String stopSchedule(String scheduleId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return scheduleServiceImpl.stopSchedule(isAdmin, username, scheduleId);
    }

    /**
     * @param fileScheduleVo:
     * @return String
     * @author tianyao
     * @description 关键词获取文件触发列表 分页
     * @date 2024/5/14 18:19
     */
    @RequestMapping(value = "/getFileScheduleListByPage", method = RequestMethod.GET)
    @ResponseBody
    public String getFileScheduleListByPage(@ModelAttribute FileScheduleVo fileScheduleVo) {
        return scheduleServiceImpl.getFileScheduleListByPage(fileScheduleVo);
    }

    /**
     * @param fileScheduleVo:
     * @return String
     * @author tianyao
     * @description 新增或编辑文件触发
     * @date 2024/5/14 18:11
     */
    @RequestMapping(value = "/saveFileSchedule", method = RequestMethod.POST)
    @ResponseBody
    public String saveFileSchedule(@RequestBody FileScheduleVo fileScheduleVo) {
        return scheduleServiceImpl.saveFileSchedule(fileScheduleVo);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 根据Id获取文件触发调度记录
     * @date 2024/5/14 18:12
     */
    @RequestMapping(value = "/getFileScheduleById", method = RequestMethod.GET)
    @ResponseBody
    public String getFileScheduleById(String id) {
        return scheduleServiceImpl.getFileScheduleById(id);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 删除文件触发调度
     * @date 2024/5/14 18:13
     */
    @RequestMapping(value = "/delFileSchedule", method = RequestMethod.POST)
    @ResponseBody
    public String delFileSchedule(String id) {
        return scheduleServiceImpl.delFileSchedule(id);
    }


    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 开启文件触发调度
     * @date 2024/5/14 18:14
     */
    @RequestMapping(value = "/startFileSchedule", method = RequestMethod.POST)
    @ResponseBody
    public String startFileSchedule(String id) {
        return scheduleServiceImpl.startFileSchedule(id);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 停止文件触发调度
     * @date 2024/5/14 18:15
     */
    @RequestMapping(value = "/stopFileSchedule", method = RequestMethod.POST)
    @ResponseBody
    public String stopFileSchedule(String id) {
        return scheduleServiceImpl.stopFileSchedule(id);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public String test(String id) {
        return scheduleServiceImpl.test(id);
    }

    /**
     * 获取所有消息源定义列表
     */
    @RequestMapping(value = "/messageSources", method = RequestMethod.GET) // 保持一致性
    @ResponseBody
    @ApiOperation(value = "获取所有消息源定义", notes = "返回所有配置的消息源定义列表")
    public ResponseResult<List<MessageSourceConfigVo>> getAllMessageSources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search) {
        try {
            List<MessageSourceConfigVo> data = messageSourceConfigService.getAllVo(page, limit, search);
            return ResponseResult.success(data, data.size());
        } catch (Exception e) {
            String username = SessionUserUtil.getCurrentUsername();
            return ResponseResult.error("获取所有消息源失败");
        }
    }

    /**
     * 新增消息源定义
     */
    @RequestMapping(value = "/messageSources", method = RequestMethod.POST) // 保持一致性
    @ResponseBody
    @ApiOperation(value = "新增消息源定义", notes = "创建一个新的消息源定义，并返回操作结果")
    public ResponseResult<Void> createMessageSource(@RequestBody MessageSourceConfigVo vo) {
        String username = SessionUserUtil.getCurrentUsername();
        try {
            messageSourceConfigService.create(vo, username);
            return ResponseResult.success();
        } catch (IllegalArgumentException e) {
            return ResponseResult.error("请求参数无效: " + e.getMessage());
        } catch (Exception e) {
            return ResponseResult.error("消息源创建过程中发生错误");
        }
    }

    /**
     * 根据ID获取单个消息源定义详情
     */
    @RequestMapping(value = "/messageSources/{id}", method = RequestMethod.GET) // 保持一致性
    @ResponseBody
    @ApiOperation(value = "根据ID获取消息源定义", notes = "返回指定ID的消息源定义详情")
    public ResponseResult<MessageSourceConfigVo> getMessageSourceById(@PathVariable Long id) {
        try {
            MessageSourceConfigVo data = messageSourceConfigService.getByIdVo(id);
            if (data != null) {
                return ResponseResult.success(data);
            } else {
                return ResponseResult.error("消息源ID为 " + id + " 的定义不存在"); // 404表示资源未找到
            }
        } catch (cn.cnic.component.schedule.exception.ResourceNotFoundException e) {
            return ResponseResult.error(e.getMessage()); // 捕获业务层抛出的资源未找到异常
        } catch (Exception e) {
            String username = SessionUserUtil.getCurrentUsername();
            return ResponseResult.error("获取消息源详情失败");
        }
    }

    /**
     * 更新消息源定义
     */
    @RequestMapping(value = "/messageSources/{id}", method = RequestMethod.PUT) // 保持一致性
    @ResponseBody
    @ApiOperation(value = "更新消息源定义", notes = "更新一个已存在的消息源定义")
    public ResponseResult<Void> updateMessageSource(@PathVariable Long id, @RequestBody MessageSourceConfigVo definition) {
        String username = SessionUserUtil.getCurrentUsername();
        if (definition.getId() == null || !id.equals(definition.getId())) {
            return ResponseResult.error("请求ID不匹配或为空");
        }
        try {
            boolean success = messageSourceConfigService.update(definition, username);
            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("消息源定义更新错误，请检查配置");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新消息源失败");
        }
    }

    /**
     * 删除事件定义
     */
    @RequestMapping(value = "/messageSources/{id}", method = RequestMethod.DELETE) // 保持一致性
    @ResponseBody
    @ApiOperation(value = "删除消息源定义", notes = "删除指定ID的消息源定义")
    public ResponseResult<Void> deleteMessageSource(@PathVariable Long id) {
        String username = SessionUserUtil.getCurrentUsername();
        try {
            boolean success = messageSourceConfigService.delete(id);
            if (success) {
                logHelperServiceImpl.logAuthSucceed("删除消息源定义成功: ID=" + id, username);
                return ResponseResult.success(); // 成功删除，不返回数据
            } else {
                // 如果 service 返回 false 但没有抛异常，可能意味着未找到记录
                return ResponseResult.error("未找到ID为" + id + "的消息源定义进行删除");
            }
        } catch (cn.cnic.component.schedule.exception.ResourceNotFoundException e) {
            return ResponseResult.error(e.getMessage());
        } catch (Exception e) {
            return ResponseResult.error("删除消息源失败");
        }
    }

    /**
     * 激活消息源定义（启动监听器）
     */
    @RequestMapping(value = "/messageSources/{id}/start", method = RequestMethod.POST) // 保持一致性
    @ResponseBody
    @ApiOperation(value = "激活消息源定义", notes = "将指定ID的消息源定义状态设置为激活并启动监听器")
    public ResponseResult<Void> startMessageSource(@PathVariable Long id) {
        String username = SessionUserUtil.getCurrentUsername();
        try {
            boolean success = messageSourceConfigService.activate(id);
            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("消息源定义无法激活，请检查配置");
            }
        } catch (Exception e) {
            return ResponseResult.error("激活消息源失败");
        }
    }

    /**
     * 暂停事件定义监听器
     */
    @RequestMapping(value = "/messageSources/{id}/pause", method = RequestMethod.POST) // 保持一致性
    @ResponseBody
    @ApiOperation(value = "暂停消息源定义", notes = "将指定ID的消息源定义状态设置为暂停并停止监听器")
    public ResponseResult<Void> pauseMessageSource(@PathVariable Long id) {
        String username = SessionUserUtil.getCurrentUsername();
        try {
            boolean success = messageSourceConfigService.pause(id);
            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("未找到ID为" + id + "的消息源定义或无法暂停");
            }
        } catch (cn.cnic.component.schedule.exception.ResourceNotFoundException e) {
            return ResponseResult.error(e.getMessage());
        } catch (Exception e) {
            return ResponseResult.error("暂停消息源失败");
        }
    }

    /**
     * 测试消息队列连接
     */
    @RequestMapping(value = "/messageSources/testConnection", method = RequestMethod.POST) // 保持一致性
    @ResponseBody
    @ApiOperation(value = "测试消息队列连接", notes = "测试消息队列连接，并返回连通结果")
    public ResponseResult<Void> testConnection(@RequestBody MessageConfigDTO messageConfig) {
        String username = SessionUserUtil.getCurrentUsername();
        try {
            messageSourceConfigService.testConnection(messageConfig);
            return ResponseResult.success();
        } catch (IllegalArgumentException e) {
            return ResponseResult.error("请求参数无效");
        } catch (Exception e) {
            return ResponseResult.error("消息队列连接失败, 请检查配置");
        }
    }

    // ================== 任务触发实例 RESTful API ==================

    /**
     * 获取特定事件定义的所有运行实例列表（分页+状态筛选）
     */
    @RequestMapping(value = "/messageSources/{messageSourceId}/instances", method = RequestMethod.GET) // 保持一致性
    @ResponseBody
    @ApiOperation(value = "获取任务触发实例列表", notes = "分页获取指定消息源ID的任务触发实例")
    public ResponseResult<List<TaskTriggerInstanceVo>> pageByMessageSourceId(
            @PathVariable Long messageSourceId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // TaskTriggerInstanceService.pageByMessageSourceIdVo 已经返回 Page<TaskTriggerInstanceVo>
            Page<TaskTriggerInstanceVo> resultPage = taskTriggerInstanceService.pageByMessageSourceIdVo(messageSourceId, status, page, size);
            // ResponseResult 有一个支持分页的 success 方法
            return ResponseResult.success(resultPage.getRecords(), (int) resultPage.getTotal());
        } catch (Exception e) {
            String username = SessionUserUtil.getCurrentUsername();
            return ResponseResult.error("获取任务实例列表失败");
        }
    }

    /**
     * 获取单个任务触发实例日志
     */
    @RequestMapping(value = "/messageSources/instance/logs/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取单个任务触发实例日志", notes = "返回指定ID的任务触发实例日志")
    public ResponseResult<List<String>> getInstanceById(@PathVariable Long id) {
        try {
            List<String> data = taskTriggerInstanceService.getLogById(id);
            if (data != null) {
                return ResponseResult.success(data);
            } else {
                return ResponseResult.error("任务实例ID为 " + id + " 的详情不存在");
            }
        } catch (cn.cnic.component.schedule.exception.ResourceNotFoundException e) {
            return ResponseResult.error(e.getMessage());
        } catch (Exception e) {
            return ResponseResult.error("获取任务实例详情失败");
        }
    }
    /**
     * 重试任务实例
     */
    @RequestMapping(value = "/messageSources/instance/{id}", method = RequestMethod.POST) // 保持一致性
    @ResponseBody
    @ApiOperation(value = "重试任务实例", notes = "进行指定任务实例的重试")
    public ResponseResult<Void> retryInstance(@PathVariable Long id) {
        String userName = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        try {
            boolean success = taskTriggerInstanceService.retryInstance(id, userName, isAdmin);
            if (success) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error("未找到ID为" + id + "的消息源定义或无法暂停");
            }
        } catch (cn.cnic.component.schedule.exception.ResourceNotFoundException e) {
            return ResponseResult.error(e.getMessage());
        } catch (Exception e) {
            return ResponseResult.error("暂停消息源失败");
        }
    }
}