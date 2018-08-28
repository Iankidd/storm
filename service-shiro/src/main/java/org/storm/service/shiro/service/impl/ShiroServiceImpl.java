package org.storm.service.shiro.service.impl;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.storm.framework.sys.service.SysMenuService;
import org.storm.service.shiro.filter.AuthorizeFilter;
import org.storm.service.shiro.service.ShiroService;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShiroServiceImpl implements ShiroService {

    private static final Logger logger = LoggerFactory.getLogger(ShiroServiceImpl.class);
    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 初始化权限
     */
    private Map<String, String> loadFilterChainDefinitions() {
        //权限控制map.从数据库获取
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/index.action", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/font-awesome/**", "anon");
        filterChainDefinitionMap.put("/**", "AuthorizeFilter");
        return filterChainDefinitionMap;
    }

    /**
     * 初始化过滤器
     */
    private Map<String, Filter> loadFilters() {
        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        filtersMap.put("AuthorizeFilter", new AuthorizeFilter());
        return filtersMap;
    }

    @Override
    public void updatePermission() {
        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                logger.error("重新加载权限失败: " + ExceptionUtils.getFullStackTrace(e));
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空旧权限控制
            manager.getFilterChains().clear();

            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
            shiroFilterFactoryBean.setFilters(loadFilters());
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                manager.createChain(url, chainDefinition);
            }

            logger.info("更新权限成功!");
        }
    }
}
