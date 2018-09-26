package org.storm.framework.base.util.date;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;

import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateJsonValueProcessor implements JsonValueProcessor {
    public final static String[] EXINCLUDES = {"properties", "class", "resources"};
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private SimpleDateFormat dateFormat;

    public DateJsonValueProcessor(String datePattern) {
        try {
            dateFormat = new SimpleDateFormat(datePattern);
        } catch (Exception ex) {
            dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        }
    }


    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        return process(value);
    }


    @Override
    public Object processObjectValue(String key, Object value,
                                     JsonConfig jsonConfig) {
        return process(value);
    }


    private Object process(Object value) {
        if (value != null) {
            if (value.getClass().getName().equals(Date.class.getName())
                    || value.getClass().getName().equals(Timestamp.class.getName())) {
                return dateFormat.format((Date) value);
            }
        }

        return value;
    }


    public static void write(Object bean, Writer writer,
                             String[] excludes, String datePattern) throws Exception {
        JsonConfig jsonConfig = configJson(excludes, datePattern);
        JSON json = JSONSerializer.toJSON(bean, jsonConfig);

        json.write(writer);
    }


    public static JsonConfig configJson(String[] excludes,
                                        String datePattern) {
        JsonConfig jsonConfig = new JsonConfig();
        if (excludes != null) {
            jsonConfig.setExcludes(excludes);
        }
        jsonConfig.setIgnoreDefaultExcludes(true);
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        jsonConfig.registerJsonValueProcessor(Date.class,
                new DateJsonValueProcessor(datePattern));
        jsonConfig.registerJsonValueProcessor(Timestamp.class,
                new DateJsonValueProcessor(datePattern));

        return jsonConfig;
    }
}
