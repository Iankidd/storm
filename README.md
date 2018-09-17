## Spring Boot 通用基础框架
#### 涉及技术及工具：
 - 核心框架：spring boot 2.0.3 RELEASE
 - MVC 框架：spring MVC
 - ORM 框架：mybatis
 - AOP 框架：spring AOP
 - MyBatis 工具：mybatis pagehelper
 - 数据库：Mysql
 - 数据连接池：durid
 - 事务管理：spring transaction
 - 线程池管理：spring executor
 - 日志框架：logback
 - 定时任务：quartz
 - 缓存管理：redis
 - 安全权限管理：shiro
 - WEB 容器：tomcat
 - 模板引擎：thymeleaf
 - 后台模板框架：inspinia
 
#### 1、目录结构
![](https://raw.githubusercontent.com/Iankidd/storm/master/img-folder/main_poject.png)
 - 通用底层框架：org.storm.framework
	* 基础封装工具类：org.storm.framework.base
	* 系统管理业务类：org.storm.framework.sys
		- 控制层：org.storm.framework.sys.controller
		- 数据层：org.storm.framework.sys.model
		- 服务层：org.storm.framework.sys.service
		- 数据映射：org.storm.framework.sys.mapper
 - 服务包类：org.storm.service
	* 后台管理包类：org.storm.service.oss
		- 控制层：org.storm.service.oss.controller
	* 服务扩展包类：org.storm.service.xxx
 - 业务层包类：org.storm.apps
	* 数据层：org.storm.apps.model
	* 服务层：org.storm.apps.service
	* 数据映射：org.storm.apps.mapper
 - 视图静态资源目录：static
 - 视图页面目录：templates
	* 公共模块：common
	* 引用脚部模块：init
	* 视图模块：view
