package cn.cnic.third;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;


public class IMarketTest extends ApplicationTests {

    private static Logger logger = LoggerUtil.getLogger();

    @Test
    public void testAddComponentUrl() {
        //String componentUrl = "https://market.casdc.cn/api/v2/component";
        String componentUrl = "http://10.0.82.139:30088/v2/component";
        Map<String, String> headerParam = new HashMap<>();
        //accessKey: User unique ID
        headerParam.put("accessKey", "d36fed02-48b3-11ed-b8af-c63dee8a33ef");
        //headerParam.put("accessKey", "621f4987583197d50685102b");
        Map<String, String> param = new HashMap<>();
        // * file: Uploaded files
        // * name: Component Name
        // * logo: Component LOGO
        // * description: Component introduction
        // * category: Component Classification
        // * bundle: --
        // * software: 621f4987583197d50685102b(fixed)
        File file = new File("/home/nature/Downloads/ExcelRead.jar");
        param.put("software", "621f4987583197d50685102b");
        param.put("bundle", "cn.cnic.bundle.ExcelRead");
        param.put("category", "ExcelRead");
        param.put("description", "Create a DataFrame from an Excel file");
        param.put("logo", "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\\nRU5ErkJggg==");
        param.put("name", "ExcelRead");
        String doPost0 = HttpUtils.doPostComCustomizeHeaderAndFile(componentUrl, headerParam, param, file, 50000);
        logger.info("-------------------------doPost0------------------------------------");
        logger.info(doPost0);

    }

    @Test
    public void testComponentUrlList() {
        //String componentUrl = "https://market.casdc.cn/api/v2/component";
        String componentUrl = "http://10.0.82.139:30088/v2/component";
        Map<String, String> headerParam = new HashMap<>();
        //accessKey: User unique ID
        headerParam.put("accessKey", "d36fed02-48b3-11ed-b8af-c63dee8a33ef");
        //headerParam.put("accessKey", "621f4987583197d50685102b");
        Map<String, Object> param = new HashMap<>();
        //software: piflow(固定)
        //name: 组件名称
        //category: 组件分类
        //authorName: 作者名称
        //sort: 排序方式 0.默认排序 1.下载较多 2.较早发布 3.较新发布
        //page: 分页
        //pageSize: 分页
        //param.put("software", "piflow");
        //param.put("name", "");
        //param.put("category", "");
        param.put("sort", "0");
        param.put("page", "1");
        param.put("pageSize", "10");
        String doGet0 = HttpUtils.doGetComCustomizeHeader(componentUrl, param, 50000, headerParam);
        logger.info("-------------------------doGet0------------------------------------");
        logger.info(doGet0);

    }

    @Test
    public void testDownloadComponentUrl() {
        //String componentUrl = "https://market.casdc.cn/api/v2/component/621f4987583197d50685102b/ExcelRead.jar";
        String componentUrl = "http://10.0.82.139:30088/v2/component/621f4987583197d50685102b/ExcelRead.jar?bundle=cn.cnic.bundle.ExcelRead";
        //String componentUrl = "https://market.casdc.cn/api/v2/component/:Bucket/:Key";
        String doGet0 = HttpUtils.doGetComCustomizeHeader(componentUrl, null, 50000, null);
        logger.info("-------------------------doGet0------------------------------------");
        logger.info(doGet0);

    }

}
