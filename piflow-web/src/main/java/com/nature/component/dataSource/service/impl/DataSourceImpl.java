package com.nature.component.dataSource.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.dataSource.model.DataSource;
import com.nature.component.dataSource.model.DataSourceProperty;
import com.nature.component.dataSource.service.IDataSource;
import com.nature.component.dataSource.utils.DataSourceUtil;
import com.nature.component.dataSource.vo.DataSourcePropertyVo;
import com.nature.component.dataSource.vo.DataSourceVo;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.domain.dataSource.DataSourceDomain;
import com.nature.domain.dataSource.DataSourcePropertyDomain;
import com.nature.domain.flow.StopsDomain;
import com.nature.mapper.dataSource.DataSourceMapper;
import com.nature.mapper.dataSource.DataSourcePropertyMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DataSourceImpl implements IDataSource {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Resource
    private DataSourcePropertyMapper dataSourcePropertyMapper;

    @Autowired
    private DataSourceDomain dataSourceDomain;

    @Autowired
    private DataSourcePropertyDomain dataSourcePropertyDomain;

    @Autowired
    private StopsDomain stopsDomain;

    @Override
    @Transactional
    public String saveOrUpdate(DataSourceVo dataSourceVo) {
        // Determine if the incoming parameter is empty
        if (null != dataSourceVo) {
            String id = dataSourceVo.getId();
            // Determine whether the id is empty. If it is empty, add it, otherwise modify it.
            if (StringUtils.isBlank(id)) {
                return addDataSource(dataSourceVo);
            } else {
                return updateDataSource(dataSourceVo);
            }
        }
        return null;
    }

    private String addDataSource(DataSourceVo dataSourceVo) {
        // Returned "map"
        Map<String, Object> rtnMap = new HashMap<>();
        // Setting failed "code" as default
        rtnMap.put("code", 500);
        // Get current user
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        // Operands to the database
        int operationsCounts = 0;
        // Determine if the incoming parameter and the current user obtained are empty
        if (null != dataSourceVo && null != currentUser) {
            // Used for the new "datasource"
            DataSource dataSource = new DataSource();
            // Copy pass the parameter "dataSourceVo" to "dataSource"
            BeanUtils.copyProperties(dataSourceVo, dataSource);
            // set id
            dataSource.setId(SqlUtils.getUUID32());
            // set creator
            dataSource.setCrtUser(currentUser.getUsername());
            // set last updater
            dataSource.setLastUpdateUser(currentUser.getUsername());
            // save "datasource"
            operationsCounts = dataSourceMapper.addDataSource(dataSource);
            // Determine whether the operation is successful, greater than 0, indicating that the operation is successful.
            if (operationsCounts > 0) {
                // Take the attribute of "datasource" from the incoming parameter
                List<DataSourcePropertyVo> dataSourcePropertyVoList = dataSourceVo.getDataSourcePropertyVoList();
                // Determine whether to pass the attribute of "datasource"
                if (null != dataSourcePropertyVoList && dataSourcePropertyVoList.size() > 0) {
                    // List for storing the "datasource" attribute
                    List<DataSourceProperty> dataSourcePropertyList = new ArrayList<>();
                    // Used to copy the attributes in the passed argument
                    DataSourceProperty dataSourceProperty = null;
                    // Loop copy the properties of the passed datasource
                    for (DataSourcePropertyVo dataSourcePropertyVo : dataSourcePropertyVoList) {
                        dataSourceProperty = new DataSourceProperty();
                        // Copy data "dataSourcePropertyVo" to "dataSourceProperty"
                        BeanUtils.copyProperties(dataSourcePropertyVo, dataSourceProperty);
                        // set id
                        dataSourceProperty.setId(SqlUtils.getUUID32());
                        // set creator
                        dataSourceProperty.setCrtUser(currentUser.getUsername());
                        // set last updater
                        dataSourceProperty.setLastUpdateUser(currentUser.getUsername());
                        // link "datasource"
                        dataSourceProperty.setDataSource(dataSource);
                        // Add to list
                        dataSourcePropertyList.add(dataSourceProperty);
                    }
                    // Save the properties of "datasource" in batches
                    int addDataSourcePropertyList = dataSourcePropertyMapper.addDataSourcePropertyList(dataSourcePropertyList);
                    operationsCounts += addDataSourcePropertyList;
                    logger.info("operationsCounts: " + operationsCounts);
                }
                rtnMap.put("code", 200);
                rtnMap.put("errorMsg", "add success.");
            } else {
                rtnMap.put("errorMsg", "save error");
                logger.warn("save error");
            }
        } else {
            rtnMap.put("errorMsg", "param error or user Illegality");
            logger.warn("param error");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    private String updateDataSource(DataSourceVo dataSourceVo) {
        // Returned "map"
        Map<String, Object> rtnMap = new HashMap<>();
        // Set failure code to default
        rtnMap.put("code", 500);
        // Get current user
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        // Determine if the incoming parameter and the current user obtained are empty
        if (null == dataSourceVo || null == currentUser) {
            rtnMap.put("errorMsg", "param error or user Illegality");
            logger.warn("param error or user Illegality");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        String id = dataSourceVo.getId();
        // Determine if the id is empty
        if (StringUtils.isBlank(id)) {
            rtnMap.put("errorMsg", "Id is empty and cannot be updated");
            logger.warn("Id is empty and cannot be updated");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Query "datasource" by id
        DataSource dataSourceById = dataSourceDomain.getDataSourceById(id);
        // Judge empty
        if (null == dataSourceById) {
            rtnMap.put("errorMsg", "Cannot find data with id '" + id + "'");
            logger.warn("Cannot find data with id '" + id + "'");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Copy pass the parameter "dataSourceVo" to "dataSource"
        BeanUtils.copyProperties(dataSourceVo, dataSourceById);
        // Set last updater
        dataSourceById.setLastUpdateUser(currentUser.getUsername());
        // Set last update time
        dataSourceById.setLastUpdateDttm(new Date());
        // Get the properties of "datasource" in the database
        List<DataSourceProperty> dataSourcePropertyList = dataSourceById.getDataSourcePropertyList();
        //Get the attribute of "datasource" in the incoming parameter
        List<DataSourcePropertyVo> dataSourcePropertyVoList = dataSourceVo.getDataSourcePropertyVoList();
        // Determine whether the "datasource" in the incoming parameter has a value. If there is any operation, delete all the attributes corresponding to the current "datasource" in the database.
        if (null != dataSourcePropertyVoList && dataSourcePropertyVoList.size() > 0) {
            List<DataSourceProperty> dataSourcePropertyListAdd = new ArrayList<>();
            List<DataSourceProperty> dataSourcePropertyListUpdate = new ArrayList<>();
            Map<String, DataSourceProperty> dataSourcePropertyMap = new HashMap<>();
            for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                dataSourcePropertyMap.put(dataSourceProperty.getId(), dataSourceProperty);
            }
            for (DataSourcePropertyVo dataSourcePropertyVo : dataSourcePropertyVoList) {
                if (null != dataSourcePropertyVo) {
                    if (StringUtils.isBlank(dataSourcePropertyVo.getId())) {
                        DataSourceProperty dataSourcePropertyAdd = new DataSourceProperty();
                        BeanUtils.copyProperties(dataSourcePropertyVo, dataSourcePropertyAdd);
                        dataSourcePropertyAdd.setCrtDttm(new Date());
                        dataSourcePropertyAdd.setCrtUser(currentUser.getUsername());
                        dataSourcePropertyAdd.setLastUpdateDttm(new Date());
                        dataSourcePropertyAdd.setLastUpdateUser(currentUser.getUsername());
                        dataSourcePropertyAdd.setDataSource(dataSourceById);
                        dataSourcePropertyListAdd.add(dataSourcePropertyAdd);
                    } else {
                        DataSourceProperty dataSourcePropertyUpdate = dataSourcePropertyMap.get(dataSourcePropertyVo.getId());
                        if (null != dataSourcePropertyUpdate) {
                            BeanUtils.copyProperties(dataSourcePropertyVo, dataSourcePropertyUpdate);
                            dataSourcePropertyUpdate.setLastUpdateDttm(new Date());
                            dataSourcePropertyUpdate.setLastUpdateUser(currentUser.getUsername());
                            dataSourcePropertyListUpdate.add(dataSourcePropertyUpdate);
                            dataSourcePropertyMap.remove(dataSourcePropertyVo.getId());
                        }
                    }
                }
            }
            if (dataSourcePropertyListAdd.size() > 0) {
                dataSourcePropertyDomain.saveOrUpdate(dataSourcePropertyListAdd);
            }
            if (dataSourcePropertyMap.keySet().size() > 0) {
                List<DataSourceProperty> dataSourcePropertyListDel = new ArrayList<>(dataSourcePropertyMap.values());
                dataSourcePropertyDomain.delete(dataSourcePropertyListDel);
            }
            if (dataSourcePropertyListUpdate.size() > 0) {
                dataSourcePropertyListUpdate = dataSourcePropertyDomain.saveOrUpdate(dataSourcePropertyListUpdate);
                dataSourceById.setDataSourcePropertyList(dataSourcePropertyListUpdate);
            }
            dataSourceDomain.saveOrUpdate(dataSourceById);
        } else {
            // Delete all attributes corresponding to the current datasource in the database
            dataSourcePropertyDomain.updateEnableFlagByDatasourceId(dataSourceById, false);
        }
        rtnMap.put("code", 200);
        rtnMap.put("errorMsg", "update success.");

        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public DataSourceVo dataSourceVoById(String id) {
        DataSourceVo dataSourceVo = null;
        if (StringUtils.isNotBlank(id)) {
            DataSource dataSourceById = dataSourceMapper.getDataSourceById(id);
            if (null != dataSourceById) {
                dataSourceVo = DataSourceUtil.dataSourcePoToVo(dataSourceById, true);
            }
        }
        return dataSourceVo;
    }

    @Override
    public String getDataSourceVoById(String id) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        DataSourceVo dataSourceVo = dataSourceVoById(id);
        if (null != dataSourceVo) {
            rtnMap.put("code", 200);
            rtnMap.put("data", dataSourceVo);
        } else {
            rtnMap.put("errorMsg", "No data was queried");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String getDataSourceVoList(boolean isTemplate) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        List<DataSource> dataSourceList = null;
        if (isTemplate) {
            dataSourceList = dataSourceMapper.getDataSourceTemplateList();
        } else {
            dataSourceList = dataSourceMapper.getDataSourceList();
        }
        List<DataSourceVo> dataSourceVoList = DataSourceUtil.dataSourceListPoToVo(dataSourceList, false);
        if (null != dataSourceVoList && dataSourceVoList.size() > 0) {
            rtnMap.put("data", dataSourceVoList);
        }
        rtnMap.put("code", 200);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public List<DataSourceVo> getDataSourceTemplateList() {
        return DataSourceUtil.dataSourceListPoToVo(dataSourceMapper.getDataSourceTemplateList(), true);
    }

    @Override
    public String getDataSourceVoListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        if (null != offset && null != limit) {
            Page<DataSource> page = PageHelper.startPage(offset, limit);
            dataSourceMapper.getDataSourceListParam(param);
            PageInfo<DataSource> info = new PageInfo<DataSource>(page);
            List<DataSourceVo> dataSourceVoList = new ArrayList<>();
            List<DataSource> infoList = info.getList();
            if (null != infoList && infoList.size() > 0) {
                dataSourceVoList = new ArrayList<>();
                DataSourceVo dataSourceVo = null;
                for (DataSource dataSource : infoList) {
                    dataSourceVo = new DataSourceVo();
                    BeanUtils.copyProperties(dataSource, dataSourceVo);
                    dataSourceVoList.add(dataSourceVo);
                }
            }
            rtnMap.put("iTotalDisplayRecords", info.getTotal());
            rtnMap.put("iTotalRecords", info.getTotal());
            rtnMap.put("pageData", dataSourceVoList);//Data collection
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    @Transactional
    public String deleteDataSourceById(String id) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        // Get current user
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        // Determine if the user is empty id is empty
        if (StringUtils.isNotBlank(id) && null != currentUser) {
            int operationCounts = dataSourceMapper.updateEnableFlagById(id);
            if (operationCounts > 0) {
                int updateEnableFlagByDatasourceId = dataSourcePropertyMapper.updateEnableFlagByDatasourceId(id);
                if (updateEnableFlagByDatasourceId > 0) {
                    operationCounts += updateEnableFlagByDatasourceId;
                }
                rtnMap.put("code", 200);
                rtnMap.put("errorMsg", "success");
                rtnMap.put("counts", operationCounts);
            } else {
                rtnMap.put("errorMsg", "delete failed");
                logger.warn("delete failed");
            }
        } else {
            rtnMap.put("errorMsg", "delete failed,param error or user Illegality");
            logger.warn("delete failed,param error");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    @Transactional
    public String fillDatasource(String dataSourceId, String stopId) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 500);
        // Get current user
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null == currentUser) {
            rtnMap.put("errorMsg", "Illegal users");
            logger.warn("Illegal users");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Determine if StopId is empty, if it is, then return, otherwise continue
        if (StringUtils.isBlank(stopId)) {
            rtnMap.put("errorMsg", "fill failed,stopId is null");
            logger.warn("fill failed,stopId is null");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Query Stops by "stopId"
        Stops stopsById = stopsDomain.getStopsById(stopId);
        if (null == stopsById) {
            rtnMap.put("errorMsg", "fill failed,Cannot find Stops with id " + stopId);
            logger.warn("fill failed,Cannot find Stops with id " + stopId);
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // Get Stops all attributes
        List<Property> propertyList = stopsById.getProperties();
        // Determine if the "stop" attribute with ID "stopId" is empty. Returns if it is empty, otherwise continues.
        if (null == propertyList || propertyList.size() <= 0) {
            rtnMap.put("errorMsg", "fill failed,stop property is null");
            logger.warn("fill failed,stop property is null");
            return JsonUtils.toJsonNoException(rtnMap);
        }
        // datasource Property Map(Key is the attribute name)
        Map<String, String> dataSourcePropertyMap = new HashMap<>();
        if (StringUtils.isNotBlank(dataSourceId)) {
            DataSource dataSourceById = dataSourceDomain.getDataSourceById(dataSourceId);
            if (null == dataSourceById) {
                rtnMap.put("errorMsg", "fill failed,Cannot find Datasource with id " + dataSourceId);
                logger.warn("fill failed,Cannot find Datasource with id " + dataSourceId);
                return JsonUtils.toJsonNoException(rtnMap);
            }
            // Get Database all attributes
            List<DataSourceProperty> dataSourcePropertyList = dataSourceById.getDataSourcePropertyList();
            // Determine whether the Datasource attribute whose ID is "dataSourceId" is empty. Returns if it is empty, otherwise it is converted to Map.
            if (null == dataSourcePropertyList || dataSourcePropertyList.size() <= 0) {
                rtnMap.put("errorMsg", "fill failed,dataSource property is null");
                logger.warn("fill failed,dataSource property is null");
                return JsonUtils.toJsonNoException(rtnMap);
            }
            stopsById.setDataSource(dataSourceById);
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
            // Loop fill "stop"
            for (Property property : propertyList) {
                // "stop" attribute name
                String name = property.getName();
                // Judge empty
                if (StringUtils.isNotBlank(name)) {
                    // Go to the map of the "datasource" attribute
                    String value = dataSourcePropertyMap.get(name.toLowerCase());
                    // Judge empty
                    if (StringUtils.isNotBlank(value)) {
                        // Assignment
                        property.setCustomValue(value);
                        property.setIsLocked(true);
                        property.setLastUpdateDttm(new Date());
                        property.setLastUpdateUser(currentUser.getUsername());
                    }
                }
            }
        } else {
            // Loop fill "stop"
            for (Property property : propertyList) {
                // "stop" attribute isSelect
                Boolean isLocked = property.getIsLocked();
                // Judge empty
                if (null != isLocked && isLocked) {
                    // Assignment
                    property.setCustomValue("");
                    property.setIsLocked(false);
                    property.setLastUpdateDttm(new Date());
                    property.setLastUpdateUser(currentUser.getUsername());
                }
            }
            stopsById.setDataSource(null);
        }
        stopsById.setProperties(propertyList);
        stopsDomain.saveOrUpdate(stopsById);
        rtnMap.put("code", 200);
        rtnMap.put("errorMsg", "fill success");

        return JsonUtils.toJsonNoException(rtnMap);
    }

}
