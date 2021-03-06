package org.storm.framework.base.util;

public class SysConstants {

    public static final String MD5_SALT = "md5salt";

    public static final String SYS_LOGIN_KEY = "oss.session.user";
    public static final String SYS_ROLE_KEY = "sys_user_role";
    public static final String SYS_PERMISSION_KEY = "sys_user_permission";
    public static final String SYS_USER_MENU = "sys_user_menu";
    public static final String SYS_IGNORE_KEY = "sys_ignore_access";
    public static final String SYS_OPERATE_KEY = "sys_operate_menu";
    public final static String LOGIN_USER_MAP = "login_user_map";

    /**
     * 状态，0：有效，1：无效
     */
    public enum EStatus {
        Valid {
            @Override
            public String getName() {
                return "有效";
            }
        },
        InValid {
            @Override
            public String getName() {
                return "无效";
            }
        };

        public abstract String getName();
    }

    public static String getNameOfEStatus(int value) {
        return EStatus.values()[value].getName();
    }

    public enum EYesNo {
        Yes {
            @Override
            public String getName() {
                return "是";
            }
        },
        No {
            @Override
            public String getName() {
                return "否";
            }
        };

        public abstract String getName();
    }

    public static String getNameOfEYesNo(int value) {
        return EYesNo.values()[value].getName();
    }

    public enum DeleteState {
        NO {
            @Override
            public String getName() {
                return "否";
            }
        },
        Yes {
            @Override
            public String getName() {
                return "是";
            }
        };

        public abstract String getName();
    }

    /**
     * 资源类型，分别有：菜单、按钮
     *
     * @author cgy
     */
    public enum EResourceType {
        Menu, Button
    }

    /**
     * 错误级别
     */
    public enum ELogLevel {
        DEBUG {
            @Override
            public String getName() {
                return "DEBUG";
            }
        },
        INFO {
            @Override
            public String getName() {
                return "INFO";
            }
        },
        WARN {
            @Override
            public String getName() {
                return "WARN";
            }
        },
        ERROR {
            @Override
            public String getName() {
                return "ERROR";
            }
        },
        FATAL {
            @Override
            public String getName() {
                return "FATAL";
            }
        };

        public abstract String getName();
    }

    public static String getNameOfLogLevel(int value) {
        return ELogLevel.values()[value].getName();
    }

    public enum EmailTaskStatus {
        Standby {
            @Override
            public String getName() {
                return "待发送";
            }
        },
        Fail {
            @Override
            public String getName() {
                return "发送失败";
            }
        },
        Success {
            @Override
            public String getName() {
                return "发送成功";
            }
        };

        public abstract String getName();
    }

}
