package cn.cnic.third.market.service;

import cn.cnic.component.stopsComponent.vo.PublishComponentVo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.util.Map;

public interface IMarket {

    /**
     * Publish Components
     * //TODO wait to delete
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
     * @Description Publish Components new
     * @Param accessKey
     * @Param publishComponentVo
     * @Param file
     * @Return java.util.Map<java.lang.String, java.lang.Object>
     * @Author TY
     * @Date 15:19 2023/4/3
     **/
    public Map<String, Object> publishComponents(String accessKey, PublishComponentVo publishComponentVo, File file) throws JsonProcessingException;

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

    /**
     * @param :
     * @return void
     * @author tianyao
     * @description send statistics to FairMan
     * @date 2024/1/16 18:45
     */
    void sendStatisticToFairMan();

}
