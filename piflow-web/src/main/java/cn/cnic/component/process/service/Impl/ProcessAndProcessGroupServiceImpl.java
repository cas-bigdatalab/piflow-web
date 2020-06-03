package cn.cnic.component.process.service.Impl;

import cn.cnic.base.util.*;
import cn.cnic.component.process.service.IProcessAndProcessGroupService;
import cn.cnic.mapper.custom.ProcessAndProcessGroupMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ProcessAndProcessGroupServiceImpl implements IProcessAndProcessGroupService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessAndProcessGroupMapper processAndProcessGroupMapper;

    /**
     * Query ProcessAndProcessGroupList (parameter space-time non-paging)
     *
     * @param offset Number of pages
     * @param limit  Number each page
     * @param param  Search content
     * @return json
     */
    @Override
    public String getProcessAndProcessGroupListPage(Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(ReturnMapUtils.ERROR_MSG);
        }
        Page<Process> page = PageHelper.startPage(offset, limit);
        if (SessionUserUtil.isAdmin()) {
            processAndProcessGroupMapper.getProcessAndProcessGroupList(param);
        } else {
            String currentUsername = SessionUserUtil.getCurrentUsername();
            processAndProcessGroupMapper.getProcessAndProcessGroupListByUser(param, currentUsername);
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }

}