package br.lukelaw.mvp_luke_law;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(scanBasePackages = "br.lukelaw.mvp_luke_law.webscraping")
public class MvpLukeLawApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvpLukeLawApplication.class, args);
	}

}
