package cn.cnic.component.stopsComponent.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * base_image_info：python算子基础镜像信息(web使用)
 * @author zjcheng
 * @date 2024-05-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseImageInfo {

    private static final long serialVersionUID = 1L;
    private String baseImageName;
    private String baseImageVersion;
    private String baseImageDescription;
    private String harborUser;
    private String harborPassword;
    private String crtDttm;
    private String crtUser;
}

