package org.storm.framework.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.storm.framework.base.exception.BusinessException;
import org.storm.framework.sys.model.SysServiceErrorLog;
import org.storm.framework.sys.service.SysServiceErrorLogService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

public class ErrorUtils {

    // 把日志记录到slf4j中输出
    private static final Logger logger = LoggerFactory.getLogger(ErrorUtils.class);

    public final static String LEVEL_DEBUG = "0";
    public final static String LEVEL_INFO = "1";
    public final static String LEVEL_WARN = "2";
    public final static String LEVEL_ERROR = "3";
    public final static String LEVEL_FATAL = "4";

    // 系统级别错误
    public final static String ERROR_100000 = "-1";       // 未知错误
    public final static String ERROR_100001 = "100001";   // mapper映射错误
    public final static String ERROR_100002 = "100002";
    public final static String ERROR_100003 = "100003";
    public final static String ERROR_100004 = "100004";
    public final static String ERROR_100005 = "100005";

    public static String traceInfo(Throwable e) {
        StringBuilder sf = new StringBuilder(4096);
        sf.append(e.toString()).append("\n");
        StackTraceElement[] trace = e.getStackTrace();
        for (int i = 0; i < trace.length; i++)
            sf.append("\tat ").append(trace[i]).append("\n");
        return sf.toString();
    }

    public static void excuteError(String moduleName, String level, String errorCode,
                                   Exception e, HttpServletRequest request,
                                   SysServiceErrorLogService sysServiceErrorLogService) {
        logger.error(traceInfo(e));
        SysServiceErrorLog sysServiceErrorLog = new SysServiceErrorLog();
        Map<String, Object> params = RequestUtils.getParameterMap(request);
        String sysName = request.getContextPath();
        String errorCause = e.getClass().getName() + ",未知错误请查看日志";
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            moduleName = businessException.getModuleName();
            level = LEVEL_INFO;
            errorCode = businessException.getErrorCode();
            errorCause = businessException.getErrorCause();
        } else if (e instanceof BadSqlGrammarException) {
            moduleName = "mapper错误";
            errorCode = ERROR_100001;
            errorCause = "mapper.xml与数据库映射错误,请检查对应的mapper.xml与表";
        }
        sysServiceErrorLog.setSysName(sysName);
        sysServiceErrorLog.setModuleName(moduleName == null ? "未知错误" : moduleName);
        sysServiceErrorLog.setLevel(level == null ? LEVEL_ERROR : level);
        sysServiceErrorLog.setErrorCode(errorCode == null ? ERROR_100000 : errorCode);
        sysServiceErrorLog.setErrorCause(errorCause);
        sysServiceErrorLog.setParams(params.toString());
        sysServiceErrorLog.setCreateDatetime(new Date());
        sysServiceErrorLogService.save(sysServiceErrorLog);
    }

}
