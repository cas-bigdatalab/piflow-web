package cn.cnic.controller.visual;


import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.visual.entity.GraphConf;
import cn.cnic.component.visual.entity.GraphTemplate;
import cn.cnic.component.visual.entity.ProductTemplateGraphAssoDto;
import cn.cnic.component.visual.service.ExcelSourceService;
import cn.cnic.component.visual.service.GraphConfService;
import cn.cnic.component.visual.service.ProductTemplateGraphAssoService;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.cnic.base.utils.DateTimeUtils.getCurrentTimeMiles;

/**
 * TODO
 * author:hmh
 * date:2023-11-03
 */
@Api(tags = "表格数据管理")
@RestController
@RequestMapping("/visual")
public class ExcelSourceController {

    @Autowired
    private ExcelSourceService excelSourceService;

    @Resource(name = "graphConfServiceImpl")
    private GraphConfService graphConfService;

    @Autowired
    private ProductTemplateGraphAssoService  productTemplateGraphAssoService;


    /**
     * 图表上传
     * @param file 二进制文件
     * @param graphTemplate-包含name/type/description
     * @return
     */
    @PostMapping(value = "/uploadExcel")
    @ApiOperation("上传excel表格")
    public ResponseResult uploadExcel(MultipartFile file, GraphTemplate graphTemplate){
        return excelSourceService.uploadExcel(file,graphTemplate);
    }

    /**
     *1. 根据path和信息配置数据源 2. 根据信息配置增加可视化
     * @param path
     * @param graphTemplate-包含name/type/description
     * @return
     */
    @PostMapping(value = "/uploadExcelFromPath")
    @ApiOperation("根据HDFS path上传excel表格")
    @Transactional
    public ResponseResult uploadExcelFromPath(String productId, String path, GraphTemplate graphTemplate) {
        String username = SessionUserUtil.getCurrentUsername();

        ResponseResult result1 = excelSourceService.uploadExcelFromPath(path, graphTemplate);
        if (result1.getCode() != 200) {
            return ResponseResult.error("路径" + path + "的文件上传失败!" + result1.getMsg() + " 请联系管理员处理!");
        }
        int graphTemplateId = (Integer)result1.getData();
        //上传成功后直接配置可视化.名字,描述等和新建数据源时传入的一样
        GraphConf conf = new GraphConf();
        conf.setName(graphTemplate.getName());
        conf.setDescription(graphTemplate.getDescription());
        conf.setConfigInfo(""); //fixme: 似乎没有让用户填这个字段
        conf.setGraphTemplateId(graphTemplateId);
        conf.setAddFlag("add");
        ResponseResult result2 = graphConfService.addGraphConf(conf);
        if (result2.getCode() != 200) {
            return ResponseResult.error("路径" + path + "的文件创建可视化失败!" + result2.getMsg() + " 请联系管理员处理!");
        }
        int graphConfId = (Integer)result2.getData();
        ProductTemplateGraphAssoDto assoDto = new ProductTemplateGraphAssoDto();
        assoDto.setProductId(productId);
        assoDto.setOwner(username);
        assoDto.setPath(path);
        assoDto.setType(0);
        assoDto.setGraphTemplateId(graphTemplateId);
        assoDto.setGraphConfId(graphConfId);
        assoDto.setCreateTime(getCurrentTimeMiles());
        ResponseResult result3 = productTemplateGraphAssoService.addProductTemplateGraphAsso(assoDto);
        if (result3.getCode() != 200) {
            return ResponseResult.error("路径" + path + "的文件创建关联关系失败!" + result2.getMsg() + " 请联系管理员处理!");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", result3.getData());
        return ResponseResult.success(map);
    }


    @PostMapping(value = "/checkProductConfExist")
    public ResponseResult checkProductExist(String productId, String path){
        String username = SessionUserUtil.getCurrentUsername();
        return excelSourceService.checkProductExist(productId, path, username);
    }

    //图表删除
    @PostMapping("/delExcel")
    @ApiOperation("图表删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "tableName",value = "表格名(不加文件后缀)")})
    public ResponseResult delExcel(@RequestBody RequestData requestData){
        return excelSourceService.delExcel(requestData);
    }
    //图表中数据查询
    @PostMapping("/selectExcelData")
    @ApiOperation("图表中数据查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "tableName",value = "表格名(不加文件后缀)")})
    public ResponseResult<List<Map>> selectExcelData(@RequestBody RequestData requestData){
        return excelSourceService.selectExcelData(requestData);
    }
    //图表数量查询
    @PostMapping("/selectExcel")
    @ApiOperation("图表数量查询")
    public ResponseResult<List<String>> selectExcel(){
        return excelSourceService.selectExcel();
    }
    //图表字段查询
    @PostMapping("/getExcelCol")
    @ApiOperation("图表字段查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "tableName",value = "表格名(不加文件后缀)")})
    public ResponseResult<List<String>> getExcelCol(@RequestBody RequestData requestData){
        return excelSourceService.getExcelCol(requestData);
    }


//    @PostMapping("/testTransactional")
//    public String testTransactional(){
//        return excelSourceService.testTransactional();
//    }
}
