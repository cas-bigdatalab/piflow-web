package cn.cnic.controller.api.dataProduct;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.component.dataProduct.service.IDataProductService;
import cn.cnic.component.dataProduct.vo.DataProductVo;
import cn.cnic.component.dataProduct.vo.ProductUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "data product api", tags = "data product api")
@Controller
@RequestMapping("/dataProduct")
public class DataProductCtrl {

    private final IDataProductService dataProductServiceImpl;

    @Autowired
    public DataProductCtrl(IDataProductService dataProductServiceImpl) {
        this.dataProductServiceImpl = dataProductServiceImpl;
    }

    /**
     * @param dataProductVo:
     * @return String
     * @author tianyao
     * @description 分页查询数据产品，仅供首页的数据产品列表查看
     * @date 2024/2/23 16:55
     */
    @RequestMapping(value = "/getByPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getByPage", notes = "分页查询数据产品，仅供首页的数据产品列表查看")
    public String getByPage(@RequestBody DataProductVo dataProductVo) {
        return dataProductServiceImpl.getByPage(dataProductVo);
    }

    /**
     * @param dataProductVo:
     * @return String
     * @author tianyao
     * @description 管理员发布管理菜单 除了已删除、生成中状态的所有   普通用户：授权管理界面
     * @date 2024/2/28 22:21
     */
    @RequestMapping(value = "/getByPageForPublishing", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getByPageForPublishing", notes = "管理员获取数据产品列表除了已删除、生成中状态的所有")
    public String getByPageForPublishing(@RequestBody DataProductVo dataProductVo) {
        return dataProductServiceImpl.getByPageForPublishing(dataProductVo);
    }

    /**
     * @param productUserVo:
     * @return String
     * @author tianyao
     * @description 普通用户：授权管理菜单：获取其他用户申请使用的记录
     * @date 2024/2/28 22:46
     */
    @RequestMapping(value = "/getByPageForPermission", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getByPageForPermission", notes = "普通用户：授权管理菜单：获取其他用户申请使用的记录")
    public String getByPageForPermission(@RequestBody ProductUserVo productUserVo) {
        return dataProductServiceImpl.getByPageForPermission(productUserVo);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 获取数据产品详情，包含封面和数据产品文件
     * @date 2024/3/1 17:39
     */
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getById", notes = "获取数据产品详情")
    public String getById(String id) {
        return dataProductServiceImpl.getById(id);
    }

    /**
     * @param file:
     * @param id:
     * @param name:
     * @param description:
     * @param permission:
     * @param keyword:
     * @param sdPublisher:
     * @param email:
     * @param state:
     * @param version:
     * @return String
     * @author tianyao
     * @description 单个数据产品发布（和编辑做成一个接口  数据产品记录是在进程运行完成后自动生成的，只能编辑数据产品
     * @date 2024/2/28 18:14
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "save", notes = "数据产品发布（和编辑做成一个接口）数据产品记录是在进程运行完成后自动生成的，只能编辑数据产品")
    public String save(MultipartFile file, String id, String name, String description, Integer permission, String keyword, String sdPublisher, String email, Integer state, Long version, Long productTypeId, String productTypeName) {
        DataProductVo dataProductVo = new DataProductVo();
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is blank!!");
        }
        dataProductVo.setId(id);
        if(StringUtils.isBlank(name)){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("name is blank, not allowed!!");
        }
        dataProductVo.setName(name);
        dataProductVo.setDescription(description);
        dataProductVo.setPermission(permission);
        dataProductVo.setKeyword(keyword);
        dataProductVo.setSdPublisher(sdPublisher);
        dataProductVo.setEmail(email);
        if (null == state) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("state is blank, not allowed!!");
        }
        dataProductVo.setState(state);
        if (null == version) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("state is blank, not allowed!!");
        }
        dataProductVo.setVersion(version);
        if (null == productTypeId) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data product type id is blank, not allowed!!");
        }
        dataProductVo.setProductTypeId(productTypeId);
        if (null == productTypeName) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data product type name is blank, not allowed!!");
        }
        dataProductVo.setProductTypeName(productTypeName);
        return dataProductServiceImpl.save(file, dataProductVo);
    }

    /**
     * @param dataProductVo:
     * @return String
     * @author tianyao
     * @description 数据产品发布审核（同意/不同意，管理员使用，新增）
     * @date 2024/2/23 14:25
     */
    @RequestMapping(value = "/permissionForPublishing", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "permissionForPublishing", notes = "数据产品发布审核（同意/不同意，管理员使用，新增）")
    public String permissionForPublishing(@RequestBody DataProductVo dataProductVo) {
        return dataProductServiceImpl.permissionForPublishing(dataProductVo);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 数据产品删除
     * @date 2024/2/23 14:44
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "delete", notes = "数据产品删除")
    public String delete(String id) {
        return dataProductServiceImpl.delete(id);
    }

    /**
     * @param dataProductVo:
     * @return String
     * @author tianyao
     * @description 已发布数据产品下架
     * @date 2024/2/23 17:51
     */
    @RequestMapping(value = "/down", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "down", notes = "已发布数据产品下架")
    public String down(@RequestBody DataProductVo dataProductVo) {
        return dataProductServiceImpl.down(dataProductVo);
    }

    /**
     * @param productUserVo:
     * @return String
     * @author tianyao
     * @description 数据产品在线申请
     * @date 2024/2/23 17:52
     */
    @RequestMapping(value = "/applyPermission", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "applyPermission", notes = "数据产品在线申请")
    public String applyPermission(@RequestBody ProductUserVo productUserVo) {
        return dataProductServiceImpl.applyPermission(productUserVo);
    }

    /**
     * @param productUserVo:
     * @return String
     * @author tianyao
     * @description 数据产品使用审核
     * @date 2024/2/28 22:53
     */
    @RequestMapping(value = "/permissionForUse", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "permissionForUse", notes = "数据产品使用审核")
    public String permissionForUse(@RequestBody ProductUserVo productUserVo) {
        return dataProductServiceImpl.permissionForUse(productUserVo);
    }


}
