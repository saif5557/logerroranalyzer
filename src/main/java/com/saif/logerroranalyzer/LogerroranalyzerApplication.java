package com.saif.logerroranalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class LogerroranalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogerroranalyzerApplication.class, args);
	}

}
