package cn.cnic.controller.api.dataProduct;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.component.dataProduct.service.IDataProductTypeService;
import cn.cnic.component.dataProduct.service.IEcosystemTypeService;
import cn.cnic.component.dataProduct.vo.DataProductTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "ecosystem type api", tags = "ecosystem type api")
@Controller
@RequestMapping("/ecosystemType")
public class EcosystemTypeCtrl {

    private final IEcosystemTypeService ecosystemTypeServiceImpl;

    @Autowired
    public EcosystemTypeCtrl(IEcosystemTypeService ecosystemTypeServiceImpl) {
        this.ecosystemTypeServiceImpl = ecosystemTypeServiceImpl;
    }


//    /**
//     * @param :
//     * @return String
//     * @author tianyao
//     * @description 数据产品类型新增或编辑
//     * @date 2024/2/19 13:46
//     */
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    @ResponseBody
//    @ApiOperation(value = "save", notes = "数据产品类型新增或编辑")
//    public String save(MultipartFile file, Long id, Long parentId, String name, String description, Integer level) {
//        DataProductTypeVo dataProductTypeVo = new DataProductTypeVo();
//        dataProductTypeVo.setId(id);
//        if(null == parentId){
//            return ReturnMapUtils.setFailedMsgRtnJsonStr("parent id is blank!");
//        }
//        dataProductTypeVo.setParentId(parentId);
//        if(null == level){
//            return ReturnMapUtils.setFailedMsgRtnJsonStr("level is blank!");
//        }
//        dataProductTypeVo.setLevel(level);
//        if(StringUtils.isBlank(name)){
//            return ReturnMapUtils.setFailedMsgRtnJsonStr("name is blank!");
//        }
//        dataProductTypeVo.setName(name);
//
//        dataProductTypeVo.setDescription(description);
//        return ecosystemTypeServiceImpl.save(file, dataProductTypeVo);
//    }
//
//    /**
//     * @param id:
//     * @return String
//     * @author tianyao
//     * @description 删除数据产品分类
//     * @date 2024/2/19 15:25
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ResponseBody
//    @ApiOperation(value = "delete", notes = "删除数据产品分类")
//    public String delete(Long id) {
//        return ecosystemTypeServiceImpl.delete(id);
//    }

    /**
     * @param :
     * @return String
     * @author tianyao
     * @description 查询数据产品分类,只包含非封面实体文件信息，封面实体用其他接口获取
     * @date 2024/2/27 10:57
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "get", notes = "查询生态系统类型")
    public String get() {
        return ecosystemTypeServiceImpl.get();
    }

}
