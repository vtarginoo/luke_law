package br.lukelaw.mvp_luke_law;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

//http://localhost:8080/swagger-ui/index.html
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "MVP Luke Law API",
				version = "1.0.0",
				description = "API para consulta e análise de processos via PJE"
		)
)
public class MvpLukeLawApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvpLukeLawApplication.class, args);
	}

}
