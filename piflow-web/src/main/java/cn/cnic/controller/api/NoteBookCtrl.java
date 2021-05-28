package cn.cnic.controller.api;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.livy.service.INoteBookService;
import cn.cnic.component.testData.service.ITestDataService;
import cn.cnic.controller.requestVo.RequestTestDataVo;
import cn.cnic.controller.requestVo.TestDataSchemaValuesSaveVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Api(value = "noteBoot api")
@RestController
@RequestMapping(value = "/noteBoot")
public class NoteBookCtrl {

    @Resource
    private INoteBookService noteBookServiceImpl;


}
