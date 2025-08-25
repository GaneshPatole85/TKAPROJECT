package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.externalapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExternalApi {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
