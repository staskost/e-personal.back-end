package com.msg.msg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.msg.msg.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class MsgApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsgApplication.class, args);
	}

}
