package cn.cnic.controller.system;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.common.constant.MessageConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "system language", tags = "system language")
@Controller
@RequestMapping("/language")
public class ChangeLanguageCtrl {

    @RequestMapping(value = "/changZH", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="changZH", notes="chang system language ZH")
    public String changZH() {
        MessageConfig.LANGUAGE = MessageConfig.LANGUAGE_TYPE_ZH;
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @RequestMapping(value = "/changEN", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="changEN", notes="chang system language ZH")
    public String changEN(Integer page, Integer limit, String param) {
        MessageConfig.LANGUAGE = MessageConfig.LANGUAGE_TYPE_EN;
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

}
