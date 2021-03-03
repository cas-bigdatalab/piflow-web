package cn.cnic.third;

import cn.cnic.base.util.HttpUtils;
import cn.cnic.base.util.LoggerUtil;
import org.slf4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class test {

    static Logger logger = LoggerUtil.getLogger();

    public static void main(String[] args) {
        //在单独的进程中执行指定命令和变量。
        //第一个变量是sh命令，第二个变量是需要执行的脚本路径，从第三个变量开始是我们要传到脚本里的参数。
        String[] path = new String[]{"sh", "/home/nature/Documents/test.sh"};
        execSh(path);

    }

    public static void testFlowGroup() {
        String url_progress = "http://10.0.86.191:8002/flowGroup/progress";
        String url_info = "http://10.0.86.191:8002/flowGroup/info";
        Map<String, String> param = new HashMap<>();
        param.put("groupId", "group_fe835872-8301-45b7-85ac-942a6a23980a");
        String doGet0 = HttpUtils.doGet(url_progress, param, 50000);
        String doGet1 = HttpUtils.doGet(url_info, param, 50000);
        logger.info("-------------------------doGet0------------------------------------");
        logger.info(doGet0);
        logger.info("-------------------------doGet1------------------------------------");
        logger.info(doGet1);
    }

    public static void execSh(String[] path) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process pro = runtime.exec(path);
            int status = pro.waitFor();
            if (status != 0) {
                System.out.println("Failed to call shell's command");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringBuffer strbr = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                strbr.append(line).append("\n");
            }

            String result = strbr.toString();
            System.out.println(result);
        } catch (IOException ec) {
            ec.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
