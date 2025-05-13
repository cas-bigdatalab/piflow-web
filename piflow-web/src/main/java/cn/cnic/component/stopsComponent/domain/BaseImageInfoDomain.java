package cn.cnic.component.stopsComponent.domain;

import cn.cnic.component.stopsComponent.entity.BaseImageInfo;
import cn.cnic.component.stopsComponent.mapper.BaseImageInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class BaseImageInfoDomain {

    private final BaseImageInfoMapper baseImageInfoMapper;

    @Autowired
    public BaseImageInfoDomain(BaseImageInfoMapper baseImageInfoMapper) {
        this.baseImageInfoMapper = baseImageInfoMapper;
    }

    //添加到基础镜像信息表
    public int addBaseImageInfo(BaseImageInfo baseImageInfo) {
        return baseImageInfoMapper.addBaseImageInfo(baseImageInfo);
    }

    //更新基础镜像信息
    public int updateBaseImageInfo(BaseImageInfo baseImageInfo) {
        return baseImageInfoMapper.updateBaseImageInfo(baseImageInfo);
    }

    //删除基础镜像信息
    public int deleteBaseImageInfo(String username, String baseImageName) {
        return baseImageInfoMapper.deleteBaseImageInfo(username, baseImageName);
    }

    //查询所有基础镜像信息
    public List<BaseImageInfo> getBaseImageInfoList() {
        List<BaseImageInfo> infos = baseImageInfoMapper.getBaseImageInfoList();

        String base_image = System.getenv("python_base_image"); //系统默认基础镜像
        String base_image_version = System.getenv("python_base_image_version"); //系统默认基础镜像版本
        if (base_image != null && base_image.length() != 0) {
            infos.add(0, BaseImageInfo.builder().baseImageName(base_image)
                    .baseImageVersion(base_image_version)
                    .baseImageDescription("python算子默认基础镜像").build());
        }
        return infos;
    }

    //根据名称查询基础镜像信息
    public List<BaseImageInfo> getBaseImageInfoListByName(String name) {
        return baseImageInfoMapper.getBaseImageInfoListByName(name);
    }


}

