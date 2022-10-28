package cn.cnic.component.process.service.Impl;

import cn.cnic.common.constant.MessageConfig;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.third.service.IVisualDataDirectory;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.service.IProcessStopService;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.process.vo.ProcessStopVo;
import cn.cnic.component.stopsComponent.entity.StopsComponent;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

@Service
public class ProcessStopServiceImpl implements IProcessStopService {

    @Autowired
    private ProcessDomain processDomain;

    @Autowired
    private StopsComponentDomain stopsComponentDomain;

    @Autowired
    private IVisualDataDirectory visualDataDirectoryImpl;

    /**
     * Query processStop based on processId and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    @Override
    public String getProcessStopVoByPageId(String processId, String pageId) {
        if (StringUtils.isAnyEmpty(processId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed in incorrectly");
        }
        ProcessStop processStopByPageId = processDomain.getProcessStopByPageIdAndPageId(processId, pageId);
        if (null == processStopByPageId) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("process stop data is null");
        }
        ProcessStopVo processStopVo = ProcessUtils.processStopPoToVo(processStopByPageId);
        StopsComponent stopsComponentByBundle = stopsComponentDomain.getStopsComponentByBundle(processStopByPageId.getBundel());
        if (null != stopsComponentByBundle) {
            processStopVo.setVisualizationType(stopsComponentByBundle.getVisualizationType());
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processStopVo", processStopVo);
    }

    /**
     * showViewData
     *
     * @param stopId       stopId
     * @return json
     */
    @Override
    public void showViewData(HttpServletResponse response, String stopId) throws Exception {
        try {
            if (StringUtils.isBlank(stopId)) {
                throw new Exception(MessageConfig.PARAM_IS_NULL_MSG("id"));
            }
            String appId = processDomain.getProcessAppIdByStopId(stopId);
            if (StringUtils.isBlank(appId)) {
                throw new Exception(MessageConfig.NO_DATA_BY_ID_XXX_MSG(stopId));
            }
            String stopName = processDomain.getProcessStopNameByStopId(stopId);
            if (StringUtils.isBlank(stopName)) {
                throw new Exception(MessageConfig.NO_DATA_BY_ID_XXX_MSG(stopName));
            }
            Map<String, Object> visualDataDirectoryData = visualDataDirectoryImpl.getVisualDataDirectoryData(appId, stopName);
            if (!"200".equals(visualDataDirectoryData.get(ReturnMapUtils.KEY_CODE).toString())) {
                throw new Exception(MessageConfig.ERROR_MSG());
            }
            byte[] fileContent = (byte[]) visualDataDirectoryData.get("fileContent");
            response.setCharacterEncoding("utf-8");
            downloadFile(response, stopName, fileContent);
        } catch (Exception e) {
            reSetError(response, e.getMessage());
        }
    }

    /**
     * The file is read as a stream
     *
     * @param fileName fileName
     * @param buffer buffer
     * @return
     */
    private void downloadFile(HttpServletResponse response, String fileName, byte[] buffer) throws URISyntaxException, IOException {
        if (null == buffer || StringUtils.isBlank(fileName)) {
            new Exception("data error");
        }
        // Clear response
        response.reset();
        // Set the header of response and UTF-8 code the file name. Otherwise, the file name is garbled and wrong when downloading
        response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode (fileName + ".csv", "UTF-8" ));
        response.addHeader("Content-Length", "" + buffer.length);
        OutputStream toClient = new BufferedOutputStream(
                response.getOutputStream());
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }

    private void reSetError(HttpServletResponse response, String errorMessage) throws Exception {
        // 重置response
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Map<String, Object> map = ReturnMapUtils.setFailedMsg(errorMessage);
        map.put("status", "failure");
        map.put("message", errorMessage);
        response.setStatus(ReturnMapUtils.ERROR_CODE);
        try {
            response.getWriter().println();
        } catch (IOException e) {
            throw new Exception("error");
        }
    }

}