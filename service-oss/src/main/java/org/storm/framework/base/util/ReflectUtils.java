package org.storm.framework.base.util;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectUtils {

    public static void setFieldValue(Object target, String fieldName,
                                     Object fieldValue) {
        try {
            Field field = ReflectionUtils.findField(target.getClass(),
                    fieldName);
            if (field != null && !Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            ReflectionUtils.setField(field, target, fieldValue);
        } catch (Exception ex) {
            try {
                BeanUtils.setProperty(target, fieldName, fieldValue);
            } catch (Exception e) {

            }
        }
    }
}
