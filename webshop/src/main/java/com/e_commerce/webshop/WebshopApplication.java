package com.e_commerce.webshop;

import com.e_commerce.webshop.service.InitDataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.SQLException;

@SpringBootApplication
@EnableScheduling
public class WebshopApplication {
	@Autowired
	private static InitDataBaseService initDataBaseService;

	public static void main(String[] args) {
		SpringApplication.run(WebshopApplication.class, args);
		initDataBaseService.initH2();

	}
	@Autowired
	public void setMyService(InitDataBaseService service) {
		WebshopApplication.initDataBaseService = service;
	}
	org.h2.tools.Server server;
	{
		try {
			server = org.h2.tools.Server.createTcpServer().start();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
