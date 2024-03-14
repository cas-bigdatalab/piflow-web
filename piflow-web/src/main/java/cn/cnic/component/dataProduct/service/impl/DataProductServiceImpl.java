package cn.cnic.component.dataProduct.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.*;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.dataProduct.domain.DataProductDomain;
import cn.cnic.component.dataProduct.domain.DataProductTypeDomain;
import cn.cnic.component.dataProduct.entity.DataProduct;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.dataProduct.entity.ProductUser;
import cn.cnic.component.dataProduct.service.IDataProductService;
import cn.cnic.component.dataProduct.vo.DataProductVo;
import cn.cnic.component.dataProduct.vo.ProductUserVo;
import cn.cnic.component.system.domain.FileDomain;
import cn.cnic.component.system.domain.SysUserDomain;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.service.IFileService;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class DataProductServiceImpl implements IDataProductService {

    private Logger logger = LoggerUtil.getLogger();

    private final DataProductDomain dataProductDomain;
    private final DataProductTypeDomain dataProductTypeDomain;
    private final FileDomain fileDomain;
    private final IFileService fileServiceImpl;
    private final SnowflakeGenerator snowflakeGenerator;
    private final SysUserDomain sysUserDomain;

    @Autowired
    public DataProductServiceImpl(DataProductDomain dataProductDomain, DataProductTypeDomain dataProductTypeDomain, FileDomain fileDomain, IFileService fileServiceImpl, SnowflakeGenerator snowflakeGenerator, SysUserDomain sysUserDomain) {
        this.dataProductDomain = dataProductDomain;
        this.dataProductTypeDomain = dataProductTypeDomain;
        this.fileDomain = fileDomain;
        this.fileServiceImpl = fileServiceImpl;
        this.snowflakeGenerator = snowflakeGenerator;
        this.sysUserDomain = sysUserDomain;
    }

    @Override
    public String save(MultipartFile file, DataProductVo dataProductVo) {
        //校验，原有的状态是否是待发布,是否有重名的数据产品
        DataProduct isExist = dataProductDomain.getById(Long.parseLong(dataProductVo.getId()));
        if(ObjectUtils.isEmpty(isExist)){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Not find the data product!, please check your id where input");
        }
        if (!DataProductState.TO_PUBLISH.getValue().equals(isExist.getState())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The publish is not allowed!! state is not TO_PUBLISH");
        }
        List<DataProduct> sameNameData = dataProductDomain.getListByName(dataProductVo.getName());
        if(CollectionUtils.isNotEmpty(sameNameData)){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Duplicate name!!");
        }
        //数据产品记录是在进程运行完成后自动生成的，只能编辑数据产品
        //更新数据产品表，数据产品类型关联表，文件表
        String username = SessionUserUtil.getCurrentUsername();
        Date now = new Date();
        DataProduct dataProduct = new DataProduct();
        BeanUtils.copyProperties(dataProductVo, dataProduct);
        dataProduct.setId(Long.parseLong(dataProductVo.getId()));
        //查看发布者是不是管理员，如果是，直接发布，如果不是，状态改成待审核
        boolean isAdmin = SessionUserUtil.isAdmin();
        if(isAdmin){
            dataProduct.setState(DataProductState.PUBLISHED.getValue());
            dataProduct.setOpinion("sdPublisher is admin, no audit is required for the publishing operation.");
        }else {
            dataProduct.setState(DataProductState.TO_CHECK.getValue());
        }
        dataProduct.setLastUpdateDttm(now);
        dataProduct.setLastUpdateUser(username);
        dataProduct.setVersion(dataProductVo.getVersion());
        dataProductDomain.update(dataProduct);

        ProductTypeAssociate associate = dataProductTypeDomain.getAssociateByAssociateId(dataProduct.getId().toString());
        if (ObjectUtils.isEmpty(associate)) {
            associate = new ProductTypeAssociate();
            associate.setId(snowflakeGenerator.next());
            associate.setProductTypeId(dataProduct.getProductTypeId());
            associate.setProductTypeName(dataProduct.getProductTypeName());
            associate.setAssociateId(dataProduct.getId().toString());
            associate.setAssociateType(ProductTypeAssociateType.DATA_PRODUCT.getValue());
            associate.setState(ProductTypeAssociateState.USABLE.getValue());
            dataProductTypeDomain.insertAssociate(associate);
        } else {
            associate.setProductTypeId(dataProduct.getProductTypeId());
            associate.setProductTypeName(dataProduct.getProductTypeName());
            dataProductTypeDomain.updateAssociate(associate);
        }

        //删除并新增数据产品类型封面
        if (ObjectUtils.isNotEmpty(file)) {
            try {
                fileServiceImpl.uploadFile(file, FileAssociateType.DATA_PRODUCT_COVER.getValue(), dataProduct.getId().toString());
            } catch (Exception e) {
                logger.error("upload data product cover error!! e:{}", e.getMessage());
                return ReturnMapUtils.setFailedMsgRtnJsonStr("upload data product cover error!!");
            }
        }

        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
    }

    @Override
    public String permissionForPublishing(DataProductVo dataProductVo) {
        String username = SessionUserUtil.getCurrentUsername();
        Date now = new Date();
        //更新数据产品表
        DataProduct dataProduct = new DataProduct();
        dataProduct.setId(Long.parseLong(dataProductVo.getId()));
        dataProduct.setState(dataProductVo.getState());
        dataProduct.setOpinion(dataProductVo.getOpinion());
        dataProduct.setLastUpdateDttm(now);
        dataProduct.setLastUpdateUser(username);
        int i = dataProductDomain.updatePermission(dataProduct);
        if (i > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
        }
    }

    @Override
    public String delete(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        //校验，是否可以删除 生成中的状态不可删除
        DataProduct dataProduct = dataProductDomain.getBasicInfoById(Long.parseLong(id));
        if (ObjectUtils.isEmpty(dataProduct)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG() + " data product has been deleted!!");
        }
        if (DataProductState.CREATING.getValue().equals(dataProduct.getState())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG() + " data product is creating, operation is not allowed!!");
        }
        //逻辑删除数据产品记录，逻辑删除封面文件、资源文件记录
        Date now = new Date();
        dataProduct.setState(DataProductState.DELETED.getValue());
        dataProduct.setLastUpdateDttm(now);
        dataProduct.setLastUpdateDttmStr(DateUtils.dateTimeSecToStr(now));
        dataProduct.setEnableFlag(false);
        dataProduct.setEnableFlagNum(0);
        int i = dataProductDomain.delete(dataProduct);

        fileDomain.fakeDeleteByAssociateId(dataProduct.getId().toString(), username);

        if (i > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.DELETE_SUCCEEDED_MSG());
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG() + " state update failed!!");
        }
    }

    @Override
    public String down(DataProductVo dataProductVo) {
        String username = SessionUserUtil.getCurrentUsername();
        Date now = new Date();
        //校验状态是否是已发布
        DataProduct dataProduct = dataProductDomain.getBasicInfoById(Long.parseLong(dataProductVo.getId()));
        if (ObjectUtils.isEmpty(dataProduct)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DOWN_ERROR_MSG + " data product has been deleted or down!!");
        }
        if (!DataProductState.PUBLISHED.getValue().equals(dataProduct.getState())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DOWN_ERROR_MSG + " data product's state is not published!!");
        }
        //逻辑下架数据产品，逻辑删除封面文件、资源文件记录
        dataProduct.setState(DataProductState.DOWN.getValue());
        dataProduct.setDownReason(dataProductVo.getDownReason());
        dataProduct.setLastUpdateDttm(now);
        dataProduct.setLastUpdateDttmStr(DateUtils.dateTimeToStr(now));
        dataProduct.setLastUpdateUser(username);
        dataProduct.setEnableFlagNum(0);
        dataProduct.setEnableFlag(false);
        int i = dataProductDomain.down(dataProduct);

        fileDomain.fakeDeleteByAssociateId(dataProduct.getId().toString(), username);

        if (i > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.DOWN_SUCCESS_MSG);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DOWN_ERROR_MSG + " update data product's state failed!!");
        }
    }

    //分页查询数据产品，仅供首页的数据产品列表查看
    @Override
    public String getByPage(DataProductVo dataProductVo) {
        String username = SessionUserUtil.getCurrentUsername();
        //无论是登录状态还是未登录状态，都能查到所有已发布的数据产品，无论是公开的还是需要授权的
        Page<DataProductVo> page = PageHelper.startPage(dataProductVo.getPage(), dataProductVo.getLimit(), "last_update_dttm desc");
        dataProductDomain.getByPageForHomePage(dataProductVo, username);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    /**
     * @param dataProductVo:
     * @return String
     * @author tianyao
     * @description 管理员发布管理菜单 获取待审核的以及自己已发布的,被拒绝发布的，已下架的，，，如果是普通用户，获取自己已发布的，被拒绝发布的，已下架的
     * @date 2024/2/28 22:25
     */
    @Override
    public String getByPageForPublishing(DataProductVo dataProductVo) {
        String username = SessionUserUtil.getCurrentUsername();
        dataProductVo.setCrtUser(username);
        boolean admin = SessionUserUtil.isAdmin();
        if(admin){
            Page<DataProduct> page = PageHelper.startPage(dataProductVo.getPage(), dataProductVo.getLimit(), "last_update_dttm desc");
            dataProductDomain.getByPageForPublishingWithAdmin(dataProductVo);
            Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
            return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
        }else {
            Page<DataProduct> page = PageHelper.startPage(dataProductVo.getPage(), dataProductVo.getLimit(), "last_update_dttm desc");
            dataProductDomain.getByPageForPublishingWithSdPublisher(dataProductVo);
            Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
            return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
        }
    }

    @Override
    public String applyPermission(ProductUserVo productUserVo) {
        String username = SessionUserUtil.getCurrentUsername();
        SysUser currentUser = sysUserDomain.getSysUserByUserName(username);
        //校验  是否有申请，如果有申请且正在审核中，就提醒用户已经申请过了，正在审核中。如果被审核拒绝了，也提醒他，被拒绝了，拒绝原因是什么？是否要重新申请 另外自己的数据产品无需申请
        ProductUser applyInfo = dataProductDomain.getApplyInfoByUserName(currentUser.getUsername());
        if (ObjectUtils.isNotEmpty(applyInfo)) {
            Integer state = applyInfo.getState();
            if (ProductUserState.TO_CHECK.getValue().equals(state)) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("You have already applied for the data product and the current progress is: Under review");
            } else if (ProductUserState.CHECK_SUCCESS.getValue().equals(state)) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("You have already applied for this data product and your application has been rejected for the following reasons:\n" +
                        applyInfo.getOpinion() + "...\n Whether you need to reapply??");
            }else {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("The application has been approved. You do not need to repeat operations.Download the data product directly");
            }
        }
        DataProduct dataProduct = dataProductDomain.getById(Long.parseLong(productUserVo.getProductId()));
        if(ObjectUtils.isEmpty(dataProduct)){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Not Find the data product directly");
        }
        //新增记录
        ProductUser insertApply = new ProductUser();
        Date now = new Date();
        insertApply.setId(snowflakeGenerator.next());
        insertApply.setProductId(dataProduct.getId());
        insertApply.setProductName(dataProduct.getName());
        insertApply.setUserId(currentUser.getId());
        insertApply.setUserName(currentUser.getUsername());
        insertApply.setState(ProductUserState.TO_CHECK.getValue());
//        insertApply.setUserEmail(currentUser.getE);
        insertApply.setReason(productUserVo.getReason());
        insertApply.setLastUpdateDttm(now);
        insertApply.setLastUpdateUser(currentUser.getUsername());
        int i = dataProductDomain.insertApplyInfo(insertApply);
        if(i>0){
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.ADD_SUCCEEDED_MSG());
        }else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ADD_ERROR_MSG()+"insert database error");
        }
    }

    @Override
    public String permissionForUse(ProductUserVo productUserVo) {
        //校验是否是待审核
        ProductUser isExists = dataProductDomain.getPermissionById(Long.parseLong(productUserVo.getId()));
        if(ObjectUtils.isEmpty(isExists)){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Not find this record, please check your id");
        }
        if(!ProductUserState.TO_CHECK.getValue().equals(isExists.getState())){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("not allowed! the state is not TO_CHECK!");
        }
        String username = SessionUserUtil.getCurrentUsername();
        Date now = new Date();
        ProductUser update = new ProductUser();
        update.setId(Long.parseLong(productUserVo.getId()));
        update.setState(productUserVo.getState());
        update.setOpinion(productUserVo.getOpinion());
        update.setLastUpdateUser(username);
        update.setLastUpdateDttm(now);
        update.setLastUpdateDttmStr(DateUtils.dateTimeToStr(now));
        dataProductDomain.permissionForUse(update);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    public String getByPageForPermission(ProductUserVo productUserVo) {
        String username = SessionUserUtil.getCurrentUsername();
        Page<ProductUserVo> page = PageHelper.startPage(productUserVo.getPage(), productUserVo.getLimit(), "last_update_dttm desc");
        dataProductDomain.getByPageForPermission(productUserVo, username);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    @Override
    public String getById(String id) {
        DataProductVo result = dataProductDomain.getFullInfoById(Long.parseLong(id));
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data",result);
    }
}