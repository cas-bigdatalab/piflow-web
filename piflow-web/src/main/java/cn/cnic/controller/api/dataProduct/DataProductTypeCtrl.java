package cn.cnic.controller.api.dataProduct;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.component.dataProduct.service.IDataProductTypeService;
import cn.cnic.component.dataProduct.vo.DataProductTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "data product type api", tags = "data product type api")
@Controller
@RequestMapping("/dataProductType")
public class DataProductTypeCtrl {

    private final IDataProductTypeService dataProductTypeServiceImpl;

    @Autowired
    public DataProductTypeCtrl(IDataProductTypeService dataProductTypeServiceImpl) {
        this.dataProductTypeServiceImpl = dataProductTypeServiceImpl;
    }


    /**
     * @param :
     * @return String
     * @author tianyao
     * @description 数据产品类型新增或编辑
     * @date 2024/2/19 13:46
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "save", notes = "数据产品类型新增或编辑")
    public String save(MultipartFile file, Long id, Long parentId, String name, String description, Integer level) {
        DataProductTypeVo dataProductTypeVo = new DataProductTypeVo();
        dataProductTypeVo.setId(id);
        if(null == parentId){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("parent id is blank!");
        }
        dataProductTypeVo.setParentId(parentId);
        if(null == level){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("level is blank!");
        }
        dataProductTypeVo.setLevel(level);
        if(StringUtils.isBlank(name)){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("name is blank!");
        }
        dataProductTypeVo.setName(name);

        dataProductTypeVo.setDescription(description);
        return dataProductTypeServiceImpl.save(file, dataProductTypeVo);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 删除数据产品分类
     * @date 2024/2/19 15:25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "delete", notes = "删除数据产品分类")
    public String delete(Long id) {
        return dataProductTypeServiceImpl.delete(id);
    }

    /**
     * @param :
     * @return String
     * @author tianyao
     * @description 查询数据产品分类,只包含非封面实体文件信息，封面实体用其他接口获取
     * @date 2024/2/27 10:57
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "get", notes = "查询数据产品分类")
    public String get() {
        return dataProductTypeServiceImpl.get();
    }

    @RequestMapping(value = "/preference", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "preference", notes = "数据产品类型偏好设置")
    public String preference(Long id, Integer preference) {
        return dataProductTypeServiceImpl.preference(id, preference);
    }

}
