package com.nature.base.util;

import com.nature.component.process.vo.DebugDataResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;

import java.io.*;
import java.net.URI;
import java.util.*;

public class HdfsUtils {

    private static Logger logger = LoggerUtil.getLogger();

    public static void closedIO(InputStreamReader isr, BufferedReader br) {
        try {
            br.close();
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static String readFlie(String hdfs) {
        StringBuffer json = new StringBuffer();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            logger.warn("start time : " + DateUtils.dateTimesToStr(new Date()));
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(hdfs), conf);
            FSDataInputStream hdfsInStream = fs.open(new Path(hdfs));
            isr = new InputStreamReader(hdfsInStream, "utf-8");
            br = new BufferedReader(isr);
            String line;
            // int k = 0;
            while ((line = br.readLine()) != null) {
                json.append(line);
                logger.debug(line);
            }
            logger.warn("end time : " + DateUtils.dateTimesToStr(new Date()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closedIO(isr, br);
        }
        return json.toString();
    }

    public static DebugDataResponse readPath(String hdfsUrl, String startFileName, int startLine, Integer limit) {
        if (null == limit) {
            limit = 10;
        }
        // Return value
        DebugDataResponse debugDataResponse = new DebugDataResponse();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            logger.info("start time : " + DateUtils.dateTimesToStr(new Date()));
            // Read file content
            List<String> HDFSDataList = new ArrayList<>();
            // Match the start file?
            boolean flagFile = false;
            // Match the start line?
            boolean flagLine = false;
            // Read the file name of the final data
            String lastReadFileName = "";
            int lastReadFileLine = startLine;
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);
            FileStatus[] status = fs.listStatus(new Path(hdfsUrl));
            for (int i = 0; i < status.length; i++) {
                FileStatus file = status[i];
                // file name
                String fileName = file.getPath().getName();
                // Judge whether it is "_SUCCESS", if it is, skip this loop, otherwise continue
                if (fileName.startsWith("_SUCCESS")) {
                    continue;
                }
                // Determine if the incoming start file name is empty and the flagFile is "false"
                // Continue if the incoming start file name is empty or the flagFile is true
                if (StringUtils.isNotBlank(startFileName) && !flagFile) {
                    // Determine if the current file name is the same as the name of the incoming start file name
                    // Change the flagFile if they are the same, otherwise skip this loop
                    if (startFileName.equals(fileName)) {
                        flagFile = true;
                    } else {
                        continue;
                    }
                }
                lastReadFileName = fileName;
                FSDataInputStream hdfsInStream = fs.open(file.getPath());
                isr = new InputStreamReader(hdfsInStream, "utf-8");
                br = new BufferedReader(isr);

                // first line data
                String next, line = br.readLine();
                // current line
                int currentLine = 1;
                for (boolean last = (line == null); !last; line = next) {
                    // Determine if the current line is greater than or equal to the starting line
                    if (!flagLine) {
                        if (currentLine > startLine) {
                            HDFSDataList.add(line);
                            flagLine = true;
                            lastReadFileLine = currentLine;
                        }
                    } else {
                        HDFSDataList.add(line);
                        lastReadFileLine = currentLine;
                    }
                    last = ((next = br.readLine()) == null);
                    if (last && (i+1)==status.length) {
                        debugDataResponse.setEnd(true);
                    }
                    // After reaching the limit, jump out
                    if (HDFSDataList.size() >= limit) {
                        break;
                    }
                    currentLine++;
                }
                flagLine = true;
                if (HDFSDataList.size() >= limit) {
                    break;
                }
            }
            debugDataResponse.setLastReadLine(lastReadFileLine);
            debugDataResponse.setLastFileName(lastReadFileName);

            debugDataResponse.setData(HDFSDataList);
            logger.info("end time : " + DateUtils.dateTimesToStr(new Date()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != isr || null != br) {
                closedIO(isr, br);

            }
        }
        return debugDataResponse;
    }

    public static void main(String[] args) {
        String hdfsUrl = "hdfs://10.0.86.89:9000/xjzhu/piflow/debug/application_1562293222869_0092/Fork/out1";
        String startFile = "part-00000-0110f52e-93bd-46a0-b23b-0f9937f3af58.json";
        DebugDataResponse debugDataResponse = readPath(hdfsUrl, startFile, 1, null);
        Object lastReadLine = debugDataResponse.getLastReadLine();
        Object lastFileName = debugDataResponse.getLastFileName();
        List<String> data = debugDataResponse.getData();
        System.out.println(lastFileName);
        System.out.println(lastReadLine);
        System.out.println(data.size());
    }
}
