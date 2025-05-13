package cn.cnic.component.stopsComponent.service;

import org.springframework.web.multipart.MultipartFile;

public interface IBaseImageService {

    /**
     * Upload docker base image for python stops
     * @param username
     * @param imageName 基础镜像名称
     * @param imageVersion 基础镜像版本
     * @param description  基础镜像描述
     * @param harborUser harbor仓库用户
     * @param harborPassword harbor仓库密码
     * @return 创建成功 or 创建失败
     */
    public String uploadBaseImage(String username, String imageName, String imageVersion,
                                  String description, String harborUser, String harborPassword);

    /**
     * Update docker base image for python stops
     * @param username
     * @param imageName 基础镜像名称
     * @param imageVersion 基础镜像版本
     * @param description  基础镜像描述
     * @param harborUser harbor仓库用户
     * @param harborPassword harbor仓库密码
     * @return 更新成功 or 更新失败
     */
    public String updateBaseImage(String username, String imageName, String imageVersion,
                                  String description, String harborUser, String harborPassword);

    /**
     * Delete docker base image for python stops
     * @param username
     * @param imageName 基础镜像名称
     * @return 删除成功 or 删除失败
     */
    public String deleteBaseImage(String username, String imageName);

    /**
     * Get All Base Image Info By Page
     *
     * @param page
     * @param limit
     * @return base image info
     */
    public String getBaseImageInfoByPage(Integer page, Integer limit);


}

