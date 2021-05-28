package cn.cnic.component.livy.service.impl;

import cn.cnic.base.util.*;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.livy.domain.NoteBookDomain;
import cn.cnic.component.livy.entity.NoteBook;
import cn.cnic.component.livy.mapper.NoteBookMapper;
import cn.cnic.component.livy.service.INoteBookService;
import cn.cnic.component.livy.util.NoteBookUtils;
import cn.cnic.component.testData.domain.TestDataDomain;
import cn.cnic.component.testData.entity.TestData;
import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.entity.TestDataSchemaValues;
import cn.cnic.component.testData.service.ITestDataService;
import cn.cnic.component.testData.utils.TestDataSchemaUtils;
import cn.cnic.component.testData.utils.TestDataSchemaValuesUtils;
import cn.cnic.component.testData.utils.TestDataUtils;
import cn.cnic.component.testData.vo.TestDataSchemaVo;
import cn.cnic.component.testData.vo.TestDataVo;
import cn.cnic.controller.requestVo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
public class NoteBookServiceImpl implements INoteBookService {
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private NoteBookMapper noteBookMapper;

    @Override
    public String saveOrUpdateNoteBook(String username, boolean isAdmin, RequesNoteBookVo noteBookVo, boolean flag) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == noteBookVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        if (StringUtils.isBlank(noteBookVo.getName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        NoteBook noteBook = null;
        String noteBookVoId = noteBookVo.getId();
        if (StringUtils.isNotBlank(noteBookVoId)) {
            noteBook = noteBookMapper.getNoteBookById(isAdmin, username, noteBookVoId);
        }

        Integer returnValue = 0;
        if (null == noteBook) {
            noteBook = NoteBookUtils.setNoteBookBasicInformation(null, false, username);
            returnValue = noteBookMapper.addNoteBook(noteBook);
        }else{
            returnValue = noteBookMapper.updateNoteBook(noteBook);
        }
        if(returnValue >0 )
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Save noteBook succeeded.");
        else
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Save noteBook failed");

    }

    @Override
    public String checkNoteBookName(String username, boolean isAdmin, String noteBookName) {
        List<NoteBook> noteBookList =noteBookMapper.checkNoteBookByName(isAdmin, username, noteBookName);
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("noteBook name can not be empty");
        }

        if (noteBookList.size() > 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("noteBook is already taken");
        } else {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("noteBook is available");
        }

    }

    @Override
    public String deleteNoteBook(String username, boolean isAdmin, String noteBookId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(noteBookId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("testDataId is null");
        }
        int i = noteBookMapper.deleteNoteBookById(isAdmin, username, noteBookId);
        if (i <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("delete failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("delete success");
    }

    @Override
    public String getNoteBookListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
        }
        Page<NoteBook> page = PageHelper.startPage(offset, limit);
        noteBookMapper.getNoteBookList(isAdmin, username, param);
        Map<String, Object> rtnMap = PageHelperUtils.setLayTableParam(page, null);
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        return JsonUtils.toJsonNoException(rtnMap);
    }
}
