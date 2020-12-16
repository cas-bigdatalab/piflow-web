package cn.cnic.component.sparkJar.service;


import org.springframework.web.multipart.MultipartFile;

public interface ISparkJarService {

    /**
     * Upload spark jar file and save
     *
     * @param username
     * @param file
     * @return
     */
    public String uploadSparkJarFile(String username, MultipartFile file);

    /**
     * mount spark jar
     *
     * @param username
     * @param id
     * @return
     */
    public String mountSparkJar(String username, Boolean isAdmin, String id);

    /**
     * unmount spark jar
     *
     * @param username
     * @param id
     * @return
     */
    public String unmountSparkJar(String username, Boolean isAdmin, String id);

    /**
     * spark jar list page
     *
     * @param username username
     * @param isAdmin  is admin
     * @param page     Number of pages
     * @param limit    Number each page
     * @param param    Search content
     * @return json
     */
    public String sparkJarListPage(String username, Boolean isAdmin, Integer page, Integer limit, String param);

    /**
     * del spark jar
     *
     * @param username username
     * @param id       id
     * @return json
     */
    public String delSparkJar(String username, Boolean isAdmin, String id);


}
