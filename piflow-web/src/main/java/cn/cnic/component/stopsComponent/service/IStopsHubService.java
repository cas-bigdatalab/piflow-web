package cn.cnic.component.stopsComponent.service;

import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.stopsComponent.model.StopsHub;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IStopsHubService {

    /**
     * Upload jar file and save
     *
     * @param username
     * @param file
     * @return
     */
    public String uploadStopsHubFile(String username, MultipartFile file);

    /**
     * mount stopsHub
     *
     * @param username
     * @param id
     * @param stopsHubName
     * @return
     */
    public String mountStopsHub(String username, Boolean isAdmin,String id, String stopsHubName);



}
