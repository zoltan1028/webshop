package com.e_commerce.webshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class WebshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebshopApplication.class, args);
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
