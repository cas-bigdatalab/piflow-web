package cn.cnic.third.market.service;

import java.io.File;
import java.util.Map;

public interface IMarket {

    /**
     * Publish Components
     *
     * @param bundle
     * @param category
     * @param description
     * @param logo
     * @param name
     * @param file
     * @return
     */
    public Map<String, Object> publishComponents(String accessKey, String bundle, String category, String description, String logo, String name, File file);

    /**
     * Search Components
     *
     * @param page
     * @param pageSize
     * @param param
     * @param sort
     * @return
     */
    public String searchComponents(String page, String pageSize, String param, String sort);

    /**
     * Download Components
     *
     * @param jarName
     * @param bundle
     * @return
     */
    public String downloadComponents(String jarName, String bundle);

}
