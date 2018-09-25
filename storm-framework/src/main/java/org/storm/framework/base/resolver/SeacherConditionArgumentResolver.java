package org.storm.framework.base.resolver;

import com.github.pagehelper.StringUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.storm.framework.base.annotation.IsSearchCondition;
import org.storm.framework.base.util.datatables.Column;
import org.storm.framework.base.util.datatables.Order;
import org.storm.framework.base.util.datatables.SearchCondition;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeacherConditionArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public SearchCondition resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Set<String> parameterNames = webRequest.getParameterMap().keySet();
        SearchCondition condition = new SearchCondition();
        for (String name : parameterNames) {

            /*
             * 设置 SearchCondition 的columns属性;
             */
            if (name.startsWith("columns")) {
                int index = Integer.parseInt(name.substring(8, 9));
                String secendName = name.substring(getCharPosition(name, "\\[", 2) + 1, getCharPosition(name, "\\]", 2));
                try {
                    if (condition.getColumns().get(index) == null) {
                        condition.getColumns().add(new Column());
                    }
                } catch (IndexOutOfBoundsException e) {
                    condition.getColumns().add(new Column());
                }
                switch (secendName) {
                    case "data":
                        condition.getColumns().get(index).setData(webRequest.getParameter(name));
                        break;
                    case "name":
                        condition.getColumns().get(index).setName(webRequest.getParameter(name));
                        break;
                    case "searchable":
                        condition.getColumns().get(index).setSearchable(Boolean.valueOf(webRequest.getParameter(name)));
                        break;
                    case "orderable":
                        condition.getColumns().get(index).setOrderable(Boolean.valueOf(webRequest.getParameter(name)));
                        break;
                    case "search":
                        String thirdName = name.substring(getCharPosition(name, "\\[", 3) + 1, getCharPosition(name, "\\]", 3));
                        switch (thirdName) {
                            case "regex":
                                condition.getColumns().get(index).getSearch().setRegex(Boolean.valueOf(webRequest.getParameter(name)));
                                break;
                            case "value":
                                String c = webRequest.getParameter(name);
                                condition.getColumns().get(index).getSearch().setValue(c);
                                break;
                        }
                        break;
                }
            }

            /*
             * 设置 SearchCondition 的columns属性;
             */
            else if (name.startsWith("order")) {
                int index = Integer.parseInt(name.substring(6, 7));
                String secendName = name.substring(getCharPosition(name, "\\[", 2) + 1, getCharPosition(name, "\\]", 2));
                try {
                    if (condition.getOrders().get(index) == null) {
                        condition.getOrders().add(new Order());
                    }
                } catch (IndexOutOfBoundsException e) {
                    condition.getOrders().add(new Order());
                }
                switch (secendName) {
                    case "column":
                        condition.getOrders().get(index).setColumn(Integer.parseInt(webRequest.getParameter(name)));
                        break;
                    case "dir":
                        condition.getOrders().get(index).setDir(webRequest.getParameter(name));
                        break;
                }
            }

            /*
             * 设置 SearchCondition 的draw属性;
             */
            else if ("draw".equalsIgnoreCase(name)) {
                if (StringUtil.isNotEmpty(webRequest.getParameter("draw"))) {
                    condition.setDraw(Integer.parseInt(webRequest.getParameter("draw")));
                } else {
                    return null;
                }
            }

            /*
             * 设置SearchCondition 的length属性;
             */
            else if ("length".equalsIgnoreCase(name)) {
                if (StringUtil.isNotEmpty(webRequest.getParameter("length"))) {
                    condition.setLength(Integer.parseInt(webRequest.getParameter("length")));
                }
            }

            /*
             * 设置SearchCondition 的start属性;
             */
            else if ("start".equalsIgnoreCase(name)) {
                if (StringUtil.isNotEmpty(webRequest.getParameter("start"))) {
                    condition.setStart(Integer.parseInt(webRequest.getParameter("start")));
                }
            }

            /*
             * 设置SearchCondition 的start属性;
             */
            else if ("search".equalsIgnoreCase(name)) {
                String secendName = name.substring(getCharPosition(name, "\\[", 2) + 1, getCharPosition(name, "\\]", 2));
                switch (secendName) {
                    case "regex":
                        condition.getSearch().setRegex(Boolean.valueOf(webRequest.getParameter(name)));
                        break;
                    case "value":
                        condition.getSearch().setValue(webRequest.getParameter(name));
                        break;
                }
            }
        }
        return condition;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(IsSearchCondition.class);
    }

    /**
     * 获取某段字符串在某个字符串中某次出现的位置
     *
     * @param string 被查找的字符串
     * @param regex  需要查找的正则表达式
     * @param index  出现的次数
     * @return 需要查找的字符串/字符出现的位置
     */
    private int getCharPosition(String string, String regex, int index) {
        Matcher slashMatcher = Pattern.compile(regex).matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            if (mIdx == index) {
                break;
            }
        }
        return slashMatcher.start();
    }
}
