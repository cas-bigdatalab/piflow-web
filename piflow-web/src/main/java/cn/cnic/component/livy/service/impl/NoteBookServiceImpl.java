package cn.cnic.component.livy.service.impl;

import cn.cnic.base.util.*;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.livy.domain.NoteBookDomain;
import cn.cnic.component.livy.service.INoteBookService;
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
import cn.cnic.controller.requestVo.RequestTestDataSchemaVo;
import cn.cnic.controller.requestVo.RequestTestDataVo;
import cn.cnic.controller.requestVo.SchemaValuesVo;
import cn.cnic.controller.requestVo.TestDataSchemaValuesSaveVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
public class NoteBookServiceImpl implements INoteBookService {
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private NoteBookDomain noteBookDomain;

}
