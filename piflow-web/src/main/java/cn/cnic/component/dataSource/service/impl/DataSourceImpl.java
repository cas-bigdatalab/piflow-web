package cn.cnic.component.dataSource.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.dataSource.domain.DataSourceDomain;
import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.entity.DataSourceProperty;
import cn.cnic.component.dataSource.service.IDataSource;
import cn.cnic.component.dataSource.utils.DataSourceUtils;
import cn.cnic.component.dataSource.vo.DataSourcePropertyVo;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.flow.domain.StopsDomain;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class DataSourceImpl implements IDataSource {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final DataSourceDomain dataSourceDomain;
    private final StopsDomain stopsDomain;

    @Autowired
    public DataSourceImpl(DataSourceDomain dataSourceDomain, StopsDomain stopsDomain) {
        this.dataSourceDomain = dataSourceDomain;
        this.stopsDomain = stopsDomain;
    }

    @Override
    public String saveOrUpdate(String username, boolean isAdmin, DataSourceVo dataSourceVo, boolean isSynchronize) {
        // Determine if the incoming parameter is empty
        if (null != dataSourceVo) {
            String id = dataSourceVo.getId();
            // Determine whether the id is empty. If it is empty, add it, otherwise modify it.
            if (StringUtils.isBlank(id)) {
                return addDataSource(username, dataSourceVo);
            } else {
                return updateDataSource(username, isAdmin, dataSourceVo, isSynchronize);
            }
        }
        return null;
    }

    private String addDataSource(String username, DataSourceVo dataSourceVo) {
        // Determine if current user obtained are empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        // Determine if the incoming parameter obtained are empty
        if (null == dataSourceVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }

        // Used for the new "datasource"
        DataSource dataSource = new DataSource();
        // Copy pass the parameter "dataSourceVo" to "dataSource"
        BeanUtils.copyProperties(dataSourceVo, dataSource);
        // set id
        dataSource.setId(UUIDUtils.getUUID32());
        // set creator
        dataSource.setCrtUser(username);
        // set last updater
        dataSource.setLastUpdateUser(username);
        // Take the attribute of "datasource" from the incoming parameter
        List<DataSourcePropertyVo> dataSourcePropertyVoList = dataSourceVo.getDataSourcePropertyVoList();
        // Determine whether to pass the attribute of "datasource"
        if (null != dataSourcePropertyVoList && dataSourcePropertyVoList.size() > 0) {
            // List for storing the "datasource" attribute
            List<DataSourceProperty> dataSourcePropertyList = new ArrayList<>();
            // Used to copy the attributes in the passed argument
            DataSourceProperty dataSourceProperty;
            // Loop copy the properties of the passed datasource
            int i = 0;
            for (DataSourcePropertyVo dataSourcePropertyVo : dataSourcePropertyVoList) {
                dataSourceProperty = new DataSourceProperty();
                // Copy data "dataSourcePropertyVo" to "dataSourceProperty"
                BeanUtils.copyProperties(dataSourcePropertyVo, dataSourceProperty);
                // set id
                dataSourceProperty.setId(UUIDUtils.getUUID32());
                // set creator
                dataSourceProperty.setCrtUser(username);
                dataSourceProperty.setCrtDttm(DateUtils.addSecond(new Date(), i));
                // set last updater
                dataSourceProperty.setLastUpdateUser(username);
                dataSourceProperty.setLastUpdateDttm(new Date());
                // link "datasource"
                dataSourceProperty.setDataSource(dataSource);
                // Add to list
                dataSourcePropertyList.add(dataSourceProperty);
                i++;
            }
            dataSource.setDataSourcePropertyList(dataSourcePropertyList);
        }
        // save "datasource"
        try {
            dataSourceDomain.insertDataSource(dataSource);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.ADD_SUCCEEDED_MSG());
        } catch (Exception e) {
            logger.error(MessageConfig.ADD_ERROR_MSG() + ":", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }

    }

    private String updateDataSource(String username, boolean isAdmin, DataSourceVo dataSourceVo, boolean isSynchronize) {
        // Determine if current user obtained are empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        // Determine if the incoming parameter obtained are empty
        if (null == dataSourceVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String id = dataSourceVo.getId();
        // Determine if the id is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        // Query "datasource" by id
        DataSource dataSourceById = dataSourceDomain.getDataSourceById(username, isAdmin, id);
        // Judge empty
        if (null == dataSourceById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(id));
        }
        //update or delete old dataSourceProperty
        Boolean isDeleteDataSourceProperty = false;
        if (!dataSourceById.getDataSourceType().equals(dataSourceVo.getDataSourceType())){
            isDeleteDataSourceProperty = true;
        }
        // Copy pass the parameter "dataSourceVo" to "dataSource"
        BeanUtils.copyProperties(dataSourceVo, dataSourceById);
        // Set last updater
        dataSourceById.setLastUpdateUser(username);
        // Set last update time
        dataSourceById.setLastUpdateDttm(new Date());

        if (isDeleteDataSourceProperty){
            List<DataSourceProperty> dataSourcePropertyList = new ArrayList<>();
            //delete old dataSourceProperty,and insert new dataSourceProperty
            dataSourceDomain.updateEnableFlagByDatasourceId(username,id);
            List<DataSourcePropertyVo> dataSourcePropertyVoList = dataSourceVo.getDataSourcePropertyVoList();
            List<DataSourceProperty> dataSourcePropertyListAdd = new ArrayList<>();
            DataSourceProperty dataSourcePropertyAdd;
            for (DataSourcePropertyVo dataSourcePropertyVo : dataSourcePropertyVoList) {
                if (null == dataSourcePropertyVo) {
                    continue;
                }
                dataSourcePropertyAdd = new DataSourceProperty();
                BeanUtils.copyProperties(dataSourcePropertyVo, dataSourcePropertyAdd);
                dataSourcePropertyAdd.setCrtDttm(new Date());
                dataSourcePropertyAdd.setCrtUser(username);
                dataSourcePropertyAdd.setLastUpdateDttm(new Date());
                dataSourcePropertyAdd.setLastUpdateUser(username);
                dataSourcePropertyAdd.setDataSource(dataSourceById);
                dataSourcePropertyListAdd.add(dataSourcePropertyAdd);
            }
            if (null == dataSourcePropertyList) {
                dataSourcePropertyList = new ArrayList<>();
            }
            dataSourcePropertyList.addAll(dataSourcePropertyListAdd);
            dataSourceById.setDataSourcePropertyList(dataSourcePropertyList);
        }else {
            //DataSourceType not change ,only update dataSourceProperty
            List<DataSourcePropertyVo> dataSourcePropertyVoList = dataSourceVo.getDataSourcePropertyVoList();
            Map<String, DataSourcePropertyVo> dataSourcePropertyVoMap = new HashMap<>();
            if (null != dataSourcePropertyVoList && dataSourcePropertyVoList.size() > 0) {
                for (DataSourcePropertyVo dataSourcePropertyVo : dataSourcePropertyVoList) {
                    if (null == dataSourcePropertyVo || StringUtils.isEmpty(dataSourcePropertyVo.getId())) {
                        continue;
                    }
                    dataSourcePropertyVoMap.put(dataSourcePropertyVo.getId(), dataSourcePropertyVo);
                }
            }
            List<DataSourceProperty> dataSourcePropertyList = dataSourceById.getDataSourcePropertyList();
            for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                DataSourcePropertyVo dataSourcePropertyVo = dataSourcePropertyVoMap.get(dataSourceProperty.getId());
                if (null != dataSourcePropertyVo && StringUtils.isNotBlank(dataSourcePropertyVo.getId())) {
                    //update
                    BeanUtils.copyProperties(dataSourcePropertyVo, dataSourceProperty);
                    dataSourceProperty.setLastUpdateDttm(new Date());
                    dataSourceProperty.setLastUpdateUser(username);
                    dataSourcePropertyVoMap.remove(dataSourceProperty.getId());
                }
            }
        }
        //Get the attribute of "datasource" in the incoming parameter
        /*List<DataSourcePropertyVo> dataSourcePropertyVoList = dataSourceVo.getDataSourcePropertyVoList();
        // dataSourcePropertyVoList to map
        Map<String, DataSourcePropertyVo> dataSourcePropertyVoMap = new HashMap<>();
        if (null != dataSourcePropertyVoList && dataSourcePropertyVoList.size() > 0) {
            for (DataSourcePropertyVo dataSourcePropertyVo : dataSourcePropertyVoList) {
                if (null == dataSourcePropertyVo) {
                    continue;
                }
                dataSourcePropertyVoMap.put(dataSourcePropertyVo.getId(), dataSourcePropertyVo);
            }
        }
        // dataSourcePropertyList
        // Get the properties of "datasource" in the database
        List<DataSourceProperty> dataSourcePropertyList = dataSourceById.getDataSourcePropertyList();
        if (null != dataSourcePropertyList && dataSourcePropertyList.size() > 0) {
            for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                DataSourcePropertyVo dataSourcePropertyVo = dataSourcePropertyVoMap.get(dataSourceProperty.getId());
                if (null != dataSourcePropertyVo && StringUtils.isNotBlank(dataSourcePropertyVo.getId())) {
                    //update
                    BeanUtils.copyProperties(dataSourcePropertyVo, dataSourceProperty);
                    dataSourceProperty.setLastUpdateDttm(new Date());
                    dataSourceProperty.setLastUpdateUser(username);

                    dataSourcePropertyVoMap.remove(dataSourceProperty.getId());
                } else {
                    // delete
                    dataSourceProperty.setEnableFlag(false);
                    dataSourceProperty.setLastUpdateDttm(new Date());
                    dataSourceProperty.setLastUpdateUser(username);
                }
            }
        }
        List<DataSourcePropertyVo> dataSourcePropertyVos = new ArrayList<>(dataSourcePropertyVoMap.values());
        if (dataSourcePropertyVoMap.values().size() > 0) {
            List<DataSourceProperty> dataSourcePropertyListAdd = new ArrayList<>();
            DataSourceProperty dataSourcePropertyAdd;
            for (DataSourcePropertyVo dataSourcePropertyVo : dataSourcePropertyVos) {
                if (null == dataSourcePropertyVo) {
                    continue;
                }
                dataSourcePropertyAdd = new DataSourceProperty();
                BeanUtils.copyProperties(dataSourcePropertyVo, dataSourcePropertyAdd);
                dataSourcePropertyAdd.setCrtDttm(new Date());
                dataSourcePropertyAdd.setCrtUser(username);
                dataSourcePropertyAdd.setLastUpdateDttm(new Date());
                dataSourcePropertyAdd.setLastUpdateUser(username);
                dataSourcePropertyAdd.setDataSource(dataSourceById);

                dataSourcePropertyListAdd.add(dataSourcePropertyAdd);
            }
            if (null == dataSourcePropertyList) {
                dataSourcePropertyList = new ArrayList<>();
            }
            dataSourcePropertyList.addAll(dataSourcePropertyListAdd);
            dataSourceById.setDataSourcePropertyList(dataSourcePropertyList);
        }*/
        try {
            dataSourceDomain.updateDataSource(dataSourceById);
            /*if (isSynchronize) {
                // get stops by datasource Id
                List<Stops> stopsListByDatasourceId = stopsDomain.getStopsListByDatasourceId(id);
                if (null == stopsListByDatasourceId || stopsListByDatasourceId.size() <= 0) {
                    return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
                }
                // datasource Property Map(Key is the attribute name)
                Map<String, String> dataSourcePropertyMap = new HashMap<>();
                // Get Database all attributes
                List<DataSourceProperty> dataSourcePropertyList = dataSourceById.getDataSourcePropertyList();
                // Loop "datasource" attribute to map
                for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                    // "datasource" attribute name
                    String dataSourcePropertyName = dataSourceProperty.getName();
                    // Judge empty and lowercase
                    if (StringUtils.isNotBlank(dataSourcePropertyName)) {
                        dataSourcePropertyName = dataSourcePropertyName.toLowerCase();
                    }
                    dataSourcePropertyMap.put(dataSourcePropertyName, dataSourceProperty.getValue());
                }
                for (Stops stops : stopsListByDatasourceId) {
                    if (null == stops) {
                        continue;
                    }
                    // Loop fill "stop"
                    stops = StopsUtils.fillStopsPropertiesByDatasource(stops, dataSourceById, username);
                    stopsDomain.updateStops(stops);
                }
            }*/
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
        } catch (Exception e) {
            logger.error(MessageConfig.UPDATE_ERROR_MSG() + ":", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
    }

    @Override
    public DataSourceVo dataSourceVoById(String username, boolean isAdmin, String id) {
        if (!isAdmin && StringUtils.isBlank(username)) {
            return null;
        }
        if (StringUtils.isNotBlank(id)) {
            return null;
        }
        DataSource dataSourceById = dataSourceDomain.getDataSourceById(username, isAdmin, id);
        DataSourceVo dataSourceVo = null;
        if (null != dataSourceById) {
            dataSourceVo = DataSourceUtils.dataSourcePoToVo(dataSourceById, true);
        }
        return dataSourceVo;
    }

    @Override
    public String getDataSourceVoById(String username, boolean isAdmin, String id) {
        if (!isAdmin && StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        DataSource dataSourceById = dataSourceDomain.getDataSourceById(username, isAdmin, id);
        DataSourceVo dataSourceVo = null;
        if (null != dataSourceById) {
            dataSourceVo = DataSourceUtils.dataSourcePoToVo(dataSourceById, true);
        }
        if (null != dataSourceVo) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", dataSourceVo);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(id));
        }
    }

    @Override
    public String getDataSourceVoList(String username, boolean isAdmin) {
        if (!isAdmin && StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        List<DataSource> dataSourceList = dataSourceDomain.getDataSourceList(username, isAdmin);
        List<DataSourceVo> dataSourceVoList = DataSourceUtils.dataSourceListPoToVo(dataSourceList, false);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", dataSourceVoList);
    }

    @Override
    public List<DataSourceVo> getDataSourceTemplateList() {
        return DataSourceUtils.dataSourceListPoToVo(dataSourceDomain.getDataSourceTemplateList(), true);
    }

    @Override
    public String getDataSourceVoListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (!isAdmin && StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        Page<DataSourceVo> page = PageHelper.startPage(offset, limit);
        dataSourceDomain.getDataSourceVoListParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    @Override
    public String deleteDataSourceById(String username, boolean isAdmin, String id) {
        // Determine if current user obtained are empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        // Determine if the user is empty id is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        DataSource dataSource = dataSourceDomain.getDataSourceById(username, isAdmin, id);
        if (isAdmin) {
            username = StringUtils.isNotBlank(username) ? username : "admin";
        }
        if (null == dataSource) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(id));
        }
        dataSource.setEnableFlag(false);
        dataSource.setLastUpdateDttm(new Date());
        dataSource.setLastUpdateUser(username);
        try {
            dataSourceDomain.saveOrUpdate(dataSource);
        } catch (Exception e) {
            logger.error("error: ", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("counts", 1);
    }

    @Override
    public String getDataSourceInputPageData(String username, boolean isAdmin, String dataSourceId) {
        DataSourceVo dataSourceVo = null;
        if (StringUtils.isNotBlank(dataSourceId)) {
            DataSource dataSourceById = dataSourceDomain.getDataSourceById(username, isAdmin, dataSourceId);
            if (null != dataSourceById) {
                dataSourceVo = DataSourceUtils.dataSourcePoToVo(dataSourceById, true);
            }
        }
        List<DataSourceVo> dataSourceTemplateList = DataSourceUtils.dataSourceListPoToVo(dataSourceDomain.getDataSourceTemplateList(), true);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("templateList", dataSourceTemplateList);
        if (null != dataSourceVo) {
            return ReturnMapUtils.appendValuesToJson(rtnMap, "dataSourceVo", dataSourceVo);
        }
        return JsonUtils.toFormatJsonNoException(rtnMap);
    }

    @Override
    public String checkLinked(String datasourceId) {
        if (StringUtils.isBlank(datasourceId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG(datasourceId));
        }
        List<String> stopsNamesByDatasourceId = stopsDomain.getStopsNamesByDatasourceId(datasourceId);
        if (null == stopsNamesByDatasourceId || stopsNamesByDatasourceId.size() <= 0) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("isLinked", false);
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("isLinked", false);
        return ReturnMapUtils.appendValuesToJson(rtnMap, "stopsNameList", stopsNamesByDatasourceId);
    }

}
