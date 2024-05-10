package kt.tripsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class TripsyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripsyncApplication.class, args);
	}

}
