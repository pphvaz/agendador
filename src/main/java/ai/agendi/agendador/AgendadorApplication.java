package ai.agendi.agendador;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AgendadorApplication {

	public static void main(String[] args) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("admin"));

		SpringApplication.run(AgendadorApplication.class, args);
	}

	@Value("${spring.datasource.url}")
	private String datasourceUrl;

	@PostConstruct
	public void logDatasourceUrl() {
		System.out.println("Datasource URL: " + datasourceUrl);
	}
}
