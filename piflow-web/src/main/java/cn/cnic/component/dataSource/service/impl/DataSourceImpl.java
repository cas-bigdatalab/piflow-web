package cn.cnic.component.dataSource.service.impl;

import cn.cnic.base.util.*;
import cn.cnic.component.dataSource.domain.DataSourceDomain;
import cn.cnic.component.dataSource.model.DataSource;
import cn.cnic.component.dataSource.model.DataSourceProperty;
import cn.cnic.component.dataSource.service.IDataSource;
import cn.cnic.component.dataSource.utils.DataSourceUtils;
import cn.cnic.component.dataSource.vo.DataSourcePropertyVo;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.mapper.dataSource.DataSourceMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DataSourceImpl implements IDataSource {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private DataSourceDomain dataSourceDomain;

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Override
    @Transactional
    public String saveOrUpdate(String username, boolean isAdmin, DataSourceVo dataSourceVo) {
        // Determine if the incoming parameter is empty
        if (null != dataSourceVo) {
            String id = dataSourceVo.getId();
            // Determine whether the id is empty. If it is empty, add it, otherwise modify it.
            if (StringUtils.isBlank(id)) {
                return addDataSource(username, dataSourceVo);
            } else {
                return updateDataSource(username, isAdmin, dataSourceVo);
            }
        }
        return null;
    }

    private String addDataSource(String username, DataSourceVo dataSourceVo) {
        // Determine if current user obtained are empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("user Illegality");
        }
        // Determine if the incoming parameter obtained are empty
        if (null == dataSourceVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param error");
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
            DataSourceProperty dataSourceProperty = null;
            // Loop copy the properties of the passed datasource
            for (DataSourcePropertyVo dataSourcePropertyVo : dataSourcePropertyVoList) {
                dataSourceProperty = new DataSourceProperty();
                // Copy data "dataSourcePropertyVo" to "dataSourceProperty"
                BeanUtils.copyProperties(dataSourcePropertyVo, dataSourceProperty);
                // set id
                dataSourceProperty.setId(UUIDUtils.getUUID32());
                // set creator
                dataSourceProperty.setCrtUser(username);
                // set last updater
                dataSourceProperty.setLastUpdateUser(username);
                // link "datasource"
                dataSourceProperty.setDataSource(dataSource);
                // Add to list
                dataSourcePropertyList.add(dataSourceProperty);
            }
            dataSource.setDataSourcePropertyList(dataSourcePropertyList);
        }
        // save "datasource"
        dataSourceDomain.saveOrUpdate(dataSource);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("add success.");
    }

    private String updateDataSource(String username, boolean isAdmin, DataSourceVo dataSourceVo) {
        // Determine if current user obtained are empty
        if (!isAdmin || StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("user Illegality");
        }
        // Determine if the incoming parameter obtained are empty
        if (null == dataSourceVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param error");
        }
        String id = dataSourceVo.getId();
        // Determine if the id is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Id is empty and cannot be updated");
        }
        // Query "datasource" by id
        DataSource dataSourceById = null;
        if (isAdmin) {
            username = StringUtils.isNotBlank(username) ? username : "admin";
            dataSourceById = dataSourceDomain.getDataSourceById(id);
        } else {
            dataSourceById = dataSourceDomain.getDataSourceByIdAndCreateUser(id, username);
        }
        // Judge empty
        if (null == dataSourceById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Cannot find data with id '" + id + "'");
        }
        // Copy pass the parameter "dataSourceVo" to "dataSource"
        BeanUtils.copyProperties(dataSourceVo, dataSourceById);
        // Set last updater
        dataSourceById.setLastUpdateUser(username);
        // Set last update time
        dataSourceById.setLastUpdateDttm(new Date());

        //Get the attribute of "datasource" in the incoming parameter
        List<DataSourcePropertyVo> dataSourcePropertyVoList = dataSourceVo.getDataSourcePropertyVoList();
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
        }
        dataSourceDomain.saveOrUpdate(dataSourceById);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("update success.");
    }

    @Override
    public DataSourceVo dataSourceVoById(String username, boolean isAdmin, String id) {
        if (!isAdmin && StringUtils.isBlank(username)) {
            return null;
        }
        if (StringUtils.isNotBlank(id)) {
            return null;
        }
        DataSource dataSourceById = null;
        if (isAdmin) {
            dataSourceById = dataSourceDomain.getDataSourceById(id);
        } else {
            dataSourceById = dataSourceDomain.getDataSourceByIdAndCreateUser(id, username);
        }
        DataSourceVo dataSourceVo = null;
        if (null != dataSourceById) {
            dataSourceVo = DataSourceUtils.dataSourcePoToVo(dataSourceById, true);
        }
        return dataSourceVo;
    }

    @Override
    public String getDataSourceVoById(String username, boolean isAdmin, String id) {
        if (!isAdmin && StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isNotBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data was queried");
        }
        DataSource dataSourceById = null;
        if (isAdmin) {
            dataSourceById = dataSourceDomain.getDataSourceById(id);
        } else {
            dataSourceById = dataSourceDomain.getDataSourceByIdAndCreateUser(id, username);
        }
        DataSourceVo dataSourceVo = null;
        if (null != dataSourceById) {
            dataSourceVo = DataSourceUtils.dataSourcePoToVo(dataSourceById, true);
        }
        if (null != dataSourceVo) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", dataSourceVo);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data was queried");
        }
    }

    @Override
    public String getDataSourceVoList(String username, boolean isAdmin) {
        if (!isAdmin && StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        List<DataSource> dataSourceList = dataSourceMapper.getDataSourceList(username, isAdmin);
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
        }
        Page<DataSourceVo> page = PageHelper.startPage(offset, limit);
        dataSourceMapper.getDataSourceVoListParam(username, isAdmin, param);
        Map<String, Object> rtnMap = PageHelperUtils.setLayTableParam(page, null);
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    @Transactional
    public String deleteDataSourceById(String username, boolean isAdmin, String id) {
        // Determine if current user obtained are empty
        if (!isAdmin || StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("user Illegality");
        }
        // Determine if the user is empty id is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("delete failed,param error");
        }
        DataSource dataSource = null;
        if (isAdmin) {
            username = StringUtils.isNotBlank(username) ? username : "admin";
            dataSource = dataSourceDomain.getDataSourceById(id);
        } else {
            dataSource = dataSourceDomain.getDataSourceByIdAndCreateUser(id, username);
        }
        if (null == dataSource) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("delete failed, no data");
        }
        dataSource.setEnableFlag(false);
        dataSource.setLastUpdateDttm(new Date());
        dataSource.setLastUpdateUser(username);
        dataSourceDomain.saveOrUpdate(dataSource);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("counts", 1);
    }

    @Override
    public String getDataSourceInputPageData(String username, boolean isAdmin, String dataSourceId) {
        Map<String, Object> rtnMap = new HashMap<>();
        DataSource dataSourceById;
        if (isAdmin) {
            dataSourceById = dataSourceDomain.getDataSourceById(dataSourceId);
        } else {
            dataSourceById = dataSourceDomain.getDataSourceByIdAndCreateUser(dataSourceId, username);
        }
        DataSourceVo dataSourceVo = null;
        if (null != dataSourceById) {
            dataSourceVo = DataSourceUtils.dataSourcePoToVo(dataSourceById, true);
        }
        if (null != dataSourceVo) {
            rtnMap.put("dataSourceVo", dataSourceVo);
        }
        List<DataSourceVo> dataSourceTemplateList = DataSourceUtils.dataSourceListPoToVo(dataSourceDomain.getDataSourceTemplateList(), true);
        rtnMap.put("templateList", dataSourceTemplateList);
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        return JsonUtils.toFormatJsonNoException(rtnMap);
    }

}
