package kr.sols;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolsApplication.class, args);
	}

}
