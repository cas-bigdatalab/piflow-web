package cn.cnic.base.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Component
public class Log4j2PathResolver {
    public static Path getLogFileBasePath(String appenderName) {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();

        Appender appender = config.getAppender(appenderName);

        if (appender == null) {
            System.err.println("Error: Appender '" + appenderName + "' not found in Log4j2 configuration.");
            return null;
        }

        if (appender instanceof FileAppender) {
            FileAppender fileAppender = (FileAppender) appender;
            String fileName = fileAppender.getFileName(); // 对于 FileAppender，直接是完整的文件名

            return Optional.ofNullable(fileName)
                    .map(Paths::get)
                    .map(Path::getParent) // 获取父目录
                    .map(Path::toAbsolutePath)
                    .orElse(null);

        } else if (appender instanceof RollingFileAppender) {
            RollingFileAppender rollingAppender = (RollingFileAppender) appender;
            String filePattern = rollingAppender.getFilePattern();

            return Optional.ofNullable(filePattern)
                    .map(pattern -> {
                        // 目标是去除所有动态部分 (如 %d{...}, %i)
                        // 寻找第一个动态模式的起始位置
                        int firstPatternIndex = pattern.indexOf('%');
                        if (firstPatternIndex == -1) {
                            // 如果没有 %d 或 %i，说明 pattern 本身可能就是文件名或固定路径
                            // 此时我们仍然需要获取其所在的目录
                            return pattern;
                        }

                        // 尝试截取到第一个动态模式之前的部分
                        String basePathCandidate = pattern.substring(0, firstPatternIndex);

                        // 确保它是一个目录，以 '/' 或 '\' 结尾
                        if (!basePathCandidate.endsWith("/") && !basePathCandidate.endsWith("\\")) {
                            // 如果不是以斜杠结尾，并且前面有斜杠，说明最后是文件名，我们需要它的目录
                            int lastSlash = basePathCandidate.lastIndexOf("/");
                            if (lastSlash == -1) lastSlash = basePathCandidate.lastIndexOf("\\");

                            if (lastSlash != -1) {
                                basePathCandidate = basePathCandidate.substring(0, lastSlash);
                            } else {
                                // 没有斜杠，可能就是当前目录，或者 pattern 本身就是文件名
                                basePathCandidate = "."; // 默认当前目录
                            }
                        }
                        return basePathCandidate;
                    })
                    .map(Paths::get)
                    .map(Path::toAbsolutePath)
                    .orElse(null);

        } else {
            System.err.println("Warning: Appender '" + appenderName + "' is not a FileAppender or RollingFileAppender. Type: " + appender.getClass().getName());
            return null;
        }
    }
}
