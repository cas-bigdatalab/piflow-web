package cn.cnic.component.stopsComponent.service;

import org.springframework.web.multipart.MultipartFile;

public interface IStopsHubService {

    /**
     * Upload jar file and save
     *
     * @param username
     * @param file
     * @param type Component type:Python/Scala
     * @param languageVersion
     * @return
     */
    public String uploadStopsHubFile(String username, MultipartFile file, String type, String languageVersion);

    /**
     * mount stopsHub
     *
     * @param username
     * @param id
     * @return
     */
    public String mountStopsHub(String username, Boolean isAdmin, String id);

    /**
     * unmount stopsHub
     *
     * @param username
     * @param id
     * @return
     */
    public String unmountStopsHub(String username, Boolean isAdmin, String id);

    /**
     * stopsHub list page
     *
     * @param username username
     * @param isAdmin  is admin
     * @param page     Number of pages
     * @param limit    Number each page
     * @param param    Search content
     * @return json
     */
    public String stopsHubListPage(String username, Boolean isAdmin, Integer page, Integer limit, String param);

    /**
     * del stopsHub
     *
     * @param username username
     * @param id       id
     * @return json
     */
    public String delStopsHub(String username, Boolean isAdmin, String id);

    /**
     * stopsHub publishing
     *
     * @param username username
     * @param id       id
     * @return json
     */
    public String stopsHubPublishing(String username, Boolean isAdmin, String id);


}
