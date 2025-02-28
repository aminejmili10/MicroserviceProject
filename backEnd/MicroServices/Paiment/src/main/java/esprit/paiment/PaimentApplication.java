package esprit.paiment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class PaimentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaimentApplication.class, args);
	}
	@GetMapping("/paiement/test")
	public String testEndpoint() {
		System.out.println("Paiment Service: Test endpoint accessed");
		return "Paiment Service is running!";
	}

}
