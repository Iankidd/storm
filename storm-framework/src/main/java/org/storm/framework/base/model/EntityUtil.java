package org.storm.framework.base.model;

import java.io.Serializable;

public class EntityUtil implements Serializable {

    // 0：明文件传输，1：加密传输，
    public static int encryptFlag = 0;
    public static String encryptSecretKey = "!@#$%&*()RWERTUIOHGFDSJK<MNBVCDE#$%^&*(OLKMN BVCDE$%^&IKMNBVCDER$%#$%YUI";

    public EntityUtil() {

    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}
