package cn.cnic.component.system.domain;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.FileUtils;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.common.Eunm.FileAssociateType;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.dataProduct.mapper.DataProductTypeMapper;
import cn.cnic.component.flow.domain.StopsDomain;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.mapper.FlowMapper;
import cn.cnic.component.flow.mapper.PathsMapper;
import cn.cnic.component.flow.utils.FlowGlobalParamsUtils;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.component.mxGraph.domain.MxGraphModelDomain;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.system.entity.File;
import cn.cnic.component.system.mapper.FileMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.h2.mvstore.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class FileDomain {

    @Autowired
    private FileMapper fileMapper;

    public int saveBatch(List<File> files) {
        return fileMapper.insertBatch(files);
    }

    /**
     * @param associateId:
     * @return int
     * @author tianyao
     * @description 物理删除文件记录
     * @date 2024/2/20 16:48
     */
    public int deleteByAssociateId(String associateId, Integer associateType) {
        int i = 0;
        File oldFile = this.getByAssociateId(associateId, associateType);
        if (ObjectUtils.isNotEmpty(oldFile)) {
            try {
                String fileName = oldFile.getFileName();
                String filePath = oldFile.getFilePath();
                if (associateType.equals(FileAssociateType.DATA_PRODUCT_TYPE_COVER.getValue()) || associateType.equals(FileAssociateType.DATA_PRODUCT_COVER.getValue())) {
                    FileUtils.deleteFile(SysParamsCache.FILE_PATH + fileName);
                } else {
                    FileUtils.deleteHdfsFile(filePath,FileUtils.getDefaultFs());
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            i = fileMapper.deleteById(oldFile.getId());
        }
        return i;
    }

    /**
     * @param name:
     * @return File
     * @author tianyao
     * @description name是唯一的，文件表中的name只能有一个
     * @date 2024/2/28 13:49
     */
    public File getByName(String name) {
        return fileMapper.getByName(name);
    }

    /**
     * @param path:
     * @return File
     * @author tianyao
     * @description path是唯一的，文件表中的path只能有一个
     * @date 2024/2/28 13:49
     */
    public File getByPath(String path) {
        return fileMapper.getByPath(path);
    }

    public File getByAssociateId(String associateId, Integer associateType) {
        return fileMapper.getByAssociateId(associateId, associateType);
    }

    public int save(File file) {
        return fileMapper.insert(file);
    }

    public File getById(String id) {
        return fileMapper.getById(id);
    }

    public int fakeDeleteByAssociateId(String associateId, String username) {
        Date now = new Date();
        return fileMapper.fakeDeleteByAssociateId(associateId, username, DateUtils.dateTimesToStr(now));
    }

    public List<File> getListByIds(String ids) {
        return fileMapper.getListByIds(ids);
    }
}
