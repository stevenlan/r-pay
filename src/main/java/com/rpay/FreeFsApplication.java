package com.rpay;

import com.rpay.common.properties.FsServerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * free-fs
 *
 * @author dinghao
 * @date 2021/3/16
 */
@SpringBootApplication
@EnableConfigurationProperties(FsServerProperties.class)
@ComponentScan(basePackages = {"com.rpay"})
@EnableTransactionManagement
public class FreeFsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeFsApplication.class, args);
    }

}
