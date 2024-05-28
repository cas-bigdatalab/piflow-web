package cn.cnic.controller.api.dataProduct;

import cn.cnic.base.utils.*;
import cn.cnic.base.vo.BasePageVo;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.dataProduct.domain.DataProductDomain;
import cn.cnic.component.dataProduct.entity.DataProduct;
import cn.cnic.component.dataProduct.service.IDataProductService;
import cn.cnic.component.dataProduct.vo.*;
import cn.cnic.component.visual.entity.GraphTemplate;
import cn.cnic.component.visual.entity.ProductTemplateGraphAssoDto;
import cn.cnic.component.visual.entity.UserProductVisualView;
import cn.cnic.component.visual.service.GraphTemplateService;
import cn.cnic.component.visual.service.ProductTemplateGraphAssoService;
import cn.cnic.component.visual.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.cnic.base.utils.FileUtils.uploadRtnMap;
import static cn.cnic.component.dataProduct.util.DataProductUtil.getFileNameFromPath;

@Api(value = "data product api", tags = "data product api")
@Controller
@RequestMapping("/dataProduct")
public class DataProductCtrl {

    private static Logger logger = LoggerUtil.getLogger();

    private final IDataProductService dataProductServiceImpl;

    private final ProductTemplateGraphAssoService productTemplateGraphAssoService;

    private final GraphTemplateService graphTemplateService;

    private final DataProductDomain dataProductDomain;


    @Value("${share.platform.url}")
    public String sharePlatformUrl;
    @Value("${share.platform.upload.uri}")
    public String sharePlatformUploadUri;
    @Value("${share.platform.AES.key}")
    public String sharePlatformAES256Key;
    @Value("${share.platform.identification}")
    public String sharePlatformId;
    @Value("${share.platform.user}")
    public String sharePlatformUser;
    @Value("${local.datacenter.url}")
    public String localDatacenterUrl;


    @Autowired
    public DataProductCtrl(IDataProductService dataProductServiceImpl,
                           ProductTemplateGraphAssoService productTemplateGraphAssoService,
                           GraphTemplateService graphTemplateService,
                           DataProductDomain dataProductDomain) {
        this.dataProductServiceImpl = dataProductServiceImpl;
        this.productTemplateGraphAssoService = productTemplateGraphAssoService;
        this.graphTemplateService = graphTemplateService;
        this.dataProductDomain = dataProductDomain;
    }



    /**
     * @param dataProductVo:
     * @return String
     * @author tianyao
     * @description 分页查询数据产品，仅供首页的数据产品列表查看
     * 20240513 修改该接口的逻辑，页面上仅显示自己发布的，不供应别人查看
     * @date 2024/2/23 16:55
     */
    @RequestMapping(value = "/getByPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getByPage", notes = "分页查询数据产品，仅供首页的数据产品列表查看")
    public String getByPage(@RequestBody DataProductVo dataProductVo) {
        return dataProductServiceImpl.getByPage(dataProductVo);
    }


    @RequestMapping(value = "/uploadDocument", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "uploadDocument", notes = "上传说明文档")
    public String uploadDocument(MultipartFile file) {
        return JsonUtils.toJsonNoException(uploadRtnMap(file, SysParamsCache.FILE_PATH,  file.getOriginalFilename()));
    }


    @GetMapping(value = "/getDataProductInfo")
    @ResponseBody
    @ApiOperation(value = "getDataProductInfo", notes = "查看数据产品详情")
    public String getDataProductInfo(String dataProductId) {
        Map<String, Object> dataProductInfo = dataProductServiceImpl.getDataProductInfo(dataProductId);
        if (MapUtils.isEmpty(dataProductInfo)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data product not be found or has been deleted");
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", dataProductInfo);
    }

    @RequestMapping(value = "/uploadToSharePlatform", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "uploadToSharePlatform", notes = "上传数据产品到资源共享平台")
    public String uploadToSharePlatform(@RequestBody DataProductMetaDataView dataProductMetaDataView) throws UnknownHostException {
        logger.error("zzatets_call_share_platform_upload_uri: " + sharePlatformUrl + sharePlatformUploadUri);

        String identifier = dataProductMetaDataView.getIdentifier();
        String filePath = SysParamsCache.CSV_PATH + identifier + ".xlsx";
        try {
            // 先存到MySQL数据库,成功后写excel文件
            if (dataProductDomain.insertDataProductMetaDataVo(dataProductMetaDataView, filePath)) {
                //ExcelUtils.writePojoToExcel(dataProductMetaDataView, filePath);
                DataProductMetaDataExcelVo excelVo = transferToExcelVo(dataProductMetaDataView);
                ExcelUtils.appendPojoToExcel(excelVo, filePath, SysParamsCache.CSV_PATH + "demo.xlsx");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnMapUtils.setFailedMsgRtnJsonStr("上传失败, 请检查");
        }
        // 16个字节的密钥
        Map<String, String> params = new HashMap<>();
        params.put("user", AES256Utils.encrypt(sharePlatformUser, sharePlatformAES256Key));
        params.put("identification", AES256Utils.encrypt(sharePlatformId, sharePlatformAES256Key));
        params.put("dataProductId", identifier);
        params.put("link", AES256Utils.encrypt(localDatacenterUrl, sharePlatformAES256Key));
        logger.info("zzatets_call_share_platform_upload_uri: " + sharePlatformUrl + sharePlatformUploadUri);
        String sendPostData = HttpUtils.doPostParmaMap(sharePlatformUrl + sharePlatformUploadUri, params, 30 * 1000);
        if (StringUtils.isBlank(sendPostData)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
        }
        JSONObject obj = JSONObject.fromObject(sendPostData);
        String code = obj.getString("code");
        if (Integer.parseInt(code) != 200) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("共享服务中心返回[" + obj.getString("message") + "], 请修改");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    /**
     * 转换为元数据Excel的格式
     * @param dataProductMetaDataView
     * @return
     */
    private DataProductMetaDataExcelVo transferToExcelVo(DataProductMetaDataView dataProductMetaDataView) {
        DataProductMetaDataExcelVo excelVo = new DataProductMetaDataExcelVo();
        BeanUtils.copyProperties(dataProductMetaDataView, excelVo);
        excelVo.setIdentifier(dataProductMetaDataView.getInnerIdentifier() + "_" +dataProductMetaDataView.getIdentifier()); // 台站名称+数据集标识
        excelVo.setDocumentationAddress(dataProductMetaDataView.getDocumentationAddress());
        excelVo.setIconAddress(getFileNameFromPath(dataProductMetaDataView.getIconAddress()));
        return excelVo;
    }

    @RequestMapping(value = "/checkUpdateSharePlatformStatus", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "checkUpdateSharePlatformStatus", notes = "查看审核状态")
    public String checkUpdateSharePlatformStatus(String dataProductId) {
        SharePlatformMetadata metadata = dataProductDomain.getDataProductMetaDataById(dataProductId);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", metadata);
    }

        /**
         * 根据数据产品获取可配置的数据源列表
         * @param
         * @return
         */
    @RequestMapping(value = "/getDataSourceListFromProduct", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getDataSourceListFromProduct", notes = "获取数据产品对应的excel列表")
    public String getDataSourceListFromProduct(String id,  String datasetUrl) {
        //参数校验
        if(StringUtils.isAnyBlank(id, datasetUrl)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        Set<String> excels = dataProductServiceImpl.getDataSourceListFromProduct(id, datasetUrl);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", excels);
    }

    @RequestMapping(value = "/getUserProductList", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getUserProductList", notes = "获取用户对应的可视化列表")
    public ResponseResult getDataSourceList(@RequestBody BasePageVo basePageVo) {
        ResponseResult responseResult = productTemplateGraphAssoService.selectByUsername(basePageVo);
        List<ProductTemplateGraphAssoDto> assoDtos = (List<ProductTemplateGraphAssoDto>) responseResult.getData();
        List<UserProductVisualView> userProductVisualViews = assoDtos.stream()
                .map(this::toUserProductView)
                .collect(Collectors.toList());
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("data", userProductVisualViews);
        rtnMap.put("totalCount", responseResult.getTotalCount());
        return ResponseResult.success(userProductVisualViews, responseResult.getTotalCount());
    }

    private UserProductVisualView toUserProductView(ProductTemplateGraphAssoDto dto) {
        GraphTemplate graphTemplate = graphTemplateService.selectById(dto.getGraphTemplateId());
        DataProduct dataProduct = dataProductDomain.getById(dto.getProductId());
        UserProductVisualView view = new UserProductVisualView();
        view.setProductVisualId(dto.getId());
        view.setGraphConfigId(dto.getGraphConfId());
        view.setProductId(dto.getProductId());
        view.setOwner(dto.getOwner());
        view.setType(dto.getType());
        view.setPath(dto.getPath());
        view.setCreateTime(dto.getCreateTime());
        view.setName(graphTemplate.getName());
        view.setDescription(graphTemplate.getDescription());
        view.setProductName(dataProduct == null ? "数据产品" + dto.getId() : dataProduct.getName());
        return view;
    }


    /**
     * @param dataProductVo:
     * @return String
     * @author tianyao
     * @description 管理员发布管理菜单 获取待审核的，，，如果是普通用户，获取已发布的，可以做删除，或者待审核的，取消审核
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
     * @param id:          多个id用英文逗号隔开
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
    public String save(MultipartFile file, String id, String name, String description, Integer permission,
                       String keyword, String sdPublisher, String email, Integer state, Long version,
                       Long productTypeId, String productTypeName) {
        DataProductVo dataProductVo = new DataProductVo();
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is blank!!");
        }
        dataProductVo.setId(id);
        if (StringUtils.isBlank(name)) {
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

    @RequestMapping(value = "/downloadDataset", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "downloadDataset", notes = "数据产品下载")
    public void downloadDataset(HttpServletResponse response, String dataProductId) {
        dataProductServiceImpl.downloadDataset(response, dataProductId);
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
