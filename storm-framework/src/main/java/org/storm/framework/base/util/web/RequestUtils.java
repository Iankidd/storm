/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.storm.framework.base.util.web;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.storm.framework.base.util.date.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    /**
     * 编辑保存用,null处理成""
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getParameterByEdit(HttpServletRequest request) {
        Map<String, Object> dataMap = new HashMap<>();
        Enumeration<?> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String paramName = (String) enumeration.nextElement();
            String paramValue = getParameter(request, paramName);
            if (StringUtils.isNotEmpty(paramName)) {
                if ("null".equalsIgnoreCase(paramValue)) {
                    continue;
                }
                String tmp = paramName.trim().toLowerCase();
                if (tmp.indexOf("date") > 0) {
                    try {
                        if (StringUtils.isNotBlank(paramValue)) {
                            Date date = DateUtils.toDate(paramValue);
                            dataMap.put(paramName, date);
                        } else {
                            dataMap.put(paramName, paramValue);
                        }

                    } catch (Exception ex) {
                    }
                }
                if (StringUtils.isNotBlank(paramValue)) {
                    dataMap.put(paramName, paramValue.trim());
                } else {
                    dataMap.put(paramName, "");
                }

            }
        }
        return dataMap;
    }

    /**
     * 查询用,从request中获取参数封装成Map,null不处理
     *
     * @param request
     * @return
     */
    /**
     * 从request中获取参数封装成Map
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Enumeration<?> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String paramName = (String) enumeration.nextElement();
            String paramValue = getParameter(request, paramName);
            if (StringUtils.isNotEmpty(paramName) && StringUtils.isNotEmpty(paramValue)) {
                if ("null".equalsIgnoreCase(paramValue)) {
                    continue;
                }
                String tmp = paramName.trim().toLowerCase();
                if (tmp.indexOf("date") > 0) {
                    try {
                        Date date = DateUtils.toDate(paramValue);
                        dataMap.put(paramName, date);
                    } catch (Exception ex) {
                    }
                }
                dataMap.put(paramName, paramValue.trim());
            }
        }
        return dataMap;
    }

    /**
     * 从request中获取参数封装成Map
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParameterStrMap(HttpServletRequest request) {
        Map<String, String> dataMap = new HashMap<String, String>();
        Enumeration<?> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String paramName = (String) enumeration.nextElement();
            String paramValue = getParameter(request, paramName);
            if (StringUtils.isNotEmpty(paramName)) {
                if ("null".equalsIgnoreCase(paramValue)) {
                    continue;
                }

                if (StringUtils.isNotBlank(paramValue)) {
                    dataMap.put(paramName, paramValue.trim());
                } else {
                    dataMap.put(paramName, "");
                }
            }
        }
        return dataMap;
    }

    /**
     * 从request中获取参数封装成Map
     *
     * @param request
     * @param isDateString 日期是否转换成字符串格式
     * @return
     */
    public static Map<String, Object> getParameterMap(HttpServletRequest request, boolean isDateString) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Enumeration<?> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String paramName = (String) enumeration.nextElement();
            String paramValue = getParameter(request, paramName);
            if (StringUtils.isNotEmpty(paramName) && StringUtils.isNotEmpty(paramValue)) {
                if ("null".equalsIgnoreCase(paramValue)) {
                    continue;
                }
                String tmp = paramName.trim().toLowerCase();
                if (tmp.indexOf("date") > 0 && isDateString) {
                    try {
                        Date date = DateUtils.toDate(paramValue);
                        dataMap.put(tmp, date);
                    } catch (Exception ex) {
                    }
                }
                dataMap.put(paramName, paramValue);
            }
        }
        return dataMap;
    }

    /**
     * 从request中获取参数
     *
     * @param request
     * @param name
     * @return
     */
    public static String getParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (StringUtils.isEmpty(value)) {
            String[] values = request.getParameterValues(name);
            if (values != null && values.length > 0) {
                StringBuffer buff = new StringBuffer();
                for (int i = 0; i < values.length; i++) {
                    if (i < values.length - 1) {
                        if (StringUtils.isNotEmpty(values[i])) {
                            buff.append(values[i]).append(',');
                        }
                    } else {
                        if (StringUtils.isNotEmpty(values[i])) {
                            buff.append(values[i]);
                        }
                    }
                }
                if (StringUtils.isNotEmpty(buff.toString())) {
                    value = buff.toString();
                }
            }
        }
        return value;
    }

    /**
     * 从request中获取long参数
     *
     * @param request
     * @param paramName
     * @param defaultValue
     * @return
     */
    public static long getLong(HttpServletRequest request, String paramName, long defaultValue) {
        String paramValue = request.getParameter(paramName);
        if (StringUtils.isNotEmpty(paramValue)) {
            return Long.parseLong(paramValue);
        }
        return defaultValue;
    }

    /**
     * 从request中获取long参数
     *
     * @param request
     * @param paramName
     * @param defaultValue
     * @return
     */
    public static double getDouble(HttpServletRequest request, String paramName, double defaultValue) {
        String paramValue = request.getParameter(paramName);
        if (StringUtils.isNotEmpty(paramValue)) {
            return Double.parseDouble(paramValue);
        }
        return defaultValue;
    }

    /**
     * 从request中获取long参数
     *
     * @param request
     * @param paramName
     * @param defaultValue
     * @return
     */
    public static int getInt(HttpServletRequest request, String paramName, int defaultValue) {
        String paramValue = request.getParameter(paramName);
        if (StringUtils.isNotEmpty(paramValue)) {
            return Integer.parseInt(paramValue);
        }
        return defaultValue;
    }

    /**
     * 获得传递的参数长整形数组
     *
     * @return 值数组
     */
    public static long[] getLongValues(HttpServletRequest request, String paramName) {
        String[] StringValues = (String[]) request.getParameterValues(paramName);
        if (StringValues != null) {
            long[] longValues = new long[StringValues.length];
            for (int i = 0; i < StringValues.length; i++) {
                longValues[i] = Long.parseLong(StringValues[i]);
            }
            return longValues;
        } else {
            return null;
        }
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = "";
        ipAddress = request.getHeader("x-forwarded-for");
        logger.info("x-forwarded-for:" + ipAddress);
        if (StringUtils.isBlank(ipAddress) || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
            logger.info("Proxy-Client-IP:" + ipAddress);
        }
        if (StringUtils.isBlank(ipAddress) || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
            logger.info("WL-Proxy-Client-IP:" + ipAddress);
        }
        if (StringUtils.isNotBlank(ipAddress)) {
            String[] clientIPs = ipAddress.split(",");
            if (clientIPs != null && clientIPs.length > 0) {
                ipAddress = clientIPs[clientIPs.length - 1].trim();
            }
        }
        if (StringUtils.isBlank(ipAddress) || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            }
        }
        logger.info("ipAddress:" + ipAddress);
        return ipAddress;
    }

    public static String getPostValue(HttpServletRequest request) throws Exception {
        String strRet = "";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(request.getInputStream(), "UTF-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            strRet = sb.toString();
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            ex.printStackTrace();
        }
        return strRet;
    }

    public static void main(String[] args) {
        System.out.println("null".equalsIgnoreCase(""));
    }

}