package org.storm.service.oss;

import com.github.pagehelper.PageHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@SpringBootApplication
@EnableEurekaClient
@RestController
@ComponentScan({"org.storm.framework.sys", "org.storm.service"})
@MapperScan({"org.storm.**.mapper"})
public class ServiceClientApplication {

    @Autowired
    private SystemProperties sysProperties;

    public static void main(String[] args) {
        SpringApplication.run(ServiceClientApplication.class, args);
    }

    /**
     * 配置mybatis的分页插件pageHelper
     */
    @Bean
    public PageHelper pageHelper() {

        PageHelper pageHelper = new PageHelper();

        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("dialect", "mysql");
        pageHelper.setProperties(properties);

        return pageHelper;
    }

    @Value("${server.port}")
    String port;

    @RequestMapping("/version.action")
    public String version() {
        return "version: " + sysProperties.getVersion() + " , port: " + port;
    }
}
