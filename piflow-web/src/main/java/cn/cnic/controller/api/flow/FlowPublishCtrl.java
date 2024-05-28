package cn.cnic.controller.api.flow;

import cn.cnic.component.flow.service.IFlowPublishService;
import cn.cnic.component.flow.vo.FlowPublishingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(value = "flow publishing api", tags = "flow publishing api")
@Controller
@RequestMapping("/flowPublish")
public class FlowPublishCtrl {

    private final IFlowPublishService flowPublishServiceImpl;

    @Autowired
    public FlowPublishCtrl(IFlowPublishService flowPublishServiceImpl) {
        this.flowPublishServiceImpl = flowPublishServiceImpl;
    }

    /**
     * @param flowPublishingVo:
     * @return String
     * @param:
     * @author tianyao
     * @description 流水线发布或重新编辑发布（新增，多文件多参数发布）
     * @date 2024/2/21 10:57
     */
    @RequestMapping(value = "/publishingStops", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "publishingStops", notes = "流水线发布")
    public String publishingStops(@RequestBody FlowPublishingVo flowPublishingVo) {
        return flowPublishServiceImpl.publishingStops(flowPublishingVo);
    }

    /**
     * @param voList:
     * @return String
     * @author tianyao
     * @description 批量排序发布流水线
     * @date 2024/5/28 13:24
     */
    @RequestMapping(value = "/publishingSort", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "publishingSort", notes = "流水线排序")
    public String publishingSort(@RequestBody List<FlowPublishingVo> voList) {
        return flowPublishServiceImpl.publishingSort(voList);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 获取某一发布的流水线的详情供再次编辑使用
     * @date 2024/2/21 10:57
     */
    @RequestMapping(value = "/getPublishingById", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getPublishingById", notes = "获取某一发布的流水线的详情供再次编辑使用")
    public String getPublishingById(String id) {
        return flowPublishServiceImpl.getPublishingById(id);
    }

    /**
     * @param flowPublishingVo:
     * @return String
     * @author tianyao
     * @description 管理员身份分页获取已发布的流水线列表
     * @date 2024/2/21 11:03
     */
    @RequestMapping(value = "/getListByPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getListByPage", notes = "管理员身份分页获取已发布的流水线列表")
    public String getListByPage(@RequestBody FlowPublishingVo flowPublishingVo) {
        return flowPublishServiceImpl.getListByPage(flowPublishingVo);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 删除流水线发布
     * @date 2024/2/21 11:28
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "delete", notes = "删除流水线发布")
    public String delete(String id) {
        return flowPublishServiceImpl.delete(id);
    }

    /**
     * @param flowPublishingVo:
     * @return String
     * @author tianyao
     * @description 门户 根据数据产品分类分页查询发布流水线(包含分类的子级)
     * @date 2024/2/21 16:13
     */
    @RequestMapping(value = "/getFlowPublishingListPageByProductTypeId", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getFlowPublishingListPageByProductTypeId", notes = "门户根据数据产品分类分页查询发布流水线")
    public String getFlowPublishingListPageByProductTypeId(@RequestBody FlowPublishingVo flowPublishingVo) {
        return flowPublishServiceImpl.getFlowPublishingListPageByProductTypeId(flowPublishingVo);
    }


    /**
     * @param flowPublishingVo:
     * @return String
     * @author tianyao
     * @description 流水线配置页面暂存功能
     * @date 2024/3/6 15:00
     */
    @RequestMapping(value = "/tempSave", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "tempSave", notes = "流水线配置页面暂存")
    public String tempSave(@RequestBody FlowPublishingVo flowPublishingVo) throws Exception {
        return flowPublishServiceImpl.tempSave(flowPublishingVo);
    }

    @RequestMapping(value = "/run", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "run", notes = "运行发布流水线")
    public String run(@RequestBody FlowPublishingVo flowPublishingVo) throws Exception {
        return flowPublishServiceImpl.run(flowPublishingVo);
    }

}
