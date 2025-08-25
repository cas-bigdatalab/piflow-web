package cn.cnic.component.schedule.mapper;

import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.entity.TaskTriggerInstance;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 任务触发实例Repository接口
 */
@Mapper
public interface TaskTriggerInstanceMapper {

    /**
     * 插入任务触发实例
     */
    @Insert("INSERT INTO message_task_trigger_instance ( type, trigger_instance_id,target_workflow_id, target_workflow_name, message_source_id, process_id, protocol_type, trigger_time, status, duration_millis, error_message, raw_message_content, message_id, execution_list, create_time, update_time) " +
            "VALUES (#{type} ,#{triggerInstanceId} ,#{targetWorkflowId},#{targetWorkflowName} ,#{messageSourceId}, #{processId},  #{protocolType}, #{triggerTime}, #{status}, #{durationMillis}, #{errorMessage}, #{rawMessageContent}, #{messageId}, #{executionList}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TaskTriggerInstance instance);

    /**
     * 根据ID获取任务实例
     */
    @Select("SELECT * FROM message_task_trigger_instance WHERE id=#{id}")
    TaskTriggerInstance getById(@Param("id") Long id);

    /**
     * 根据消息源ID和状态分页查询任务实例
     */
    @Select("SELECT * FROM message_task_trigger_instance WHERE message_source_id=#{messageSourceId} AND status=#{status} ORDER BY trigger_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<TaskTriggerInstance> selectByMessageSourceIdAndStatus(@Param("messageSourceId") Long messageSourceId, @Param("status") String status, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 根据消息源ID分页查询任务实例
     */
    @Select("SELECT * FROM message_task_trigger_instance WHERE message_source_id=#{messageSourceId} ORDER BY trigger_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<TaskTriggerInstance> selectByMessageSourceId(@Param("messageSourceId") Long messageSourceId, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 根据状态查询任务实例list
     */
    @Select({
            "<script>",
            "SELECT * FROM message_task_trigger_instance",
            "<where>",
            "   status = #{status}",
            "   <if test='messageSourceId != null'>",
            "       AND message_source_id = #{messageSourceId}",
            "   </if>",
            "</where>",
            "ORDER BY trigger_time DESC",
            "</script>"
    })
    List<TaskTriggerInstance> selectByStatus(@Param("messageSourceId") Long messageSourceId, @Param("status") String status);

    /**
     * 根据消息源ID统计总数
     */
    @Select("SELECT COUNT(*) FROM message_task_trigger_instance WHERE message_source_id=#{messageSourceId}")
    int countByMessageSourceId(@Param("messageSourceId") Long messageSourceId);

    @Update({
            "<script>",
            "UPDATE message_task_trigger_instance",
            "<set>",
            "  <if test=\"instance.triggerInstanceId != null and instance.triggerInstanceId != ''\">",
            "    trigger_instance_id = #{instance.triggerInstanceId},",
            "  </if>",
            "  <if test=\"instance.messageSourceId != null\">",
            "    message_source_id = #{instance.messageSourceId},",
            "  </if>",
            "  <if test=\"instance.messageId != null and instance.messageId != ''\">",
            "    message_id = #{instance.messageId},",
            "  </if>",
            "  <if test=\"instance.protocolType != null and instance.protocolType != ''\">",
            "    protocol_type = #{instance.protocolType},",
            "  </if>",
            "  <if test=\"instance.targetWorkflowId != null and instance.targetWorkflowId != ''\">",
            "    target_workflow_id = #{instance.targetWorkflowId},",
            "  </if>",
            "  <if test=\"instance.processId != null and instance.processId != ''\">",
            "    process_id = #{instance.processId},",
            "  </if>",
            "  <if test=\"instance.triggerTime != null\">",
            "    trigger_time = #{instance.triggerTime},",
            "  </if>",
            "  <if test=\"instance.status != null and instance.status != ''\">",
            "    status = #{instance.status},",
            "  </if>",
            "  <if test=\"instance.durationMillis != null\">",
            "    duration_millis = #{instance.durationMillis},",
            "  </if>",
            "  <if test=\"instance.errorMessage != null\">",
            "    error_message = #{instance.errorMessage},",
            "  </if>",
            "  <if test=\"instance.rawMessageContent != null\">",
            "    raw_message_content = #{instance.rawMessageContent},",
            "  </if>",
            "  <if test=\"instance.createTime != null\">",
            "    create_time = #{instance.createTime},",
            "  </if>",
            "  <if test=\"instance.updateTime != null\">",
            "    update_time = #{instance.updateTime},",
            "  </if>",
            "  <if test=\"instance.executionList != null\">",
            "    execution_list = #{instance.executionList},",
            "  </if>",
            "</set>",
            "WHERE id = #{instance.id}",
            "</script>"
    })
    void update(@Param("instance") TaskTriggerInstance instance);

    @Delete("DELETE FROM message_task_trigger_instance WHERE message_source_id=#{id}")
    void deleteByMessageSourceId(@Param("id") Long id);

    @Select("SELECT COUNT(*) FROM message_task_trigger_instance WHERE message_source_id=#{messageSourceId} and status=#{status}")
    int countByMessageSourceIdAndStatus(Long messageSourceId, String status);

    @Select("SELECT * FROM message_task_trigger_instance WHERE message_id=#{messageId} FOR UPDATE")
    TaskTriggerInstance findByMessageIdWithLock(String messageId);

    @Select("SELECT COUNT(*) FROM message_task_trigger_instance WHERE protocol_type=#{protocol} AND message_id=#{messageId}")
    int checkIsDuplicateMessage(MessageProtocol protocol, String messageId);

}
