package com.msg.msg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.msg.msg.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class MsgApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsgApplication.class, args);
	}

}
