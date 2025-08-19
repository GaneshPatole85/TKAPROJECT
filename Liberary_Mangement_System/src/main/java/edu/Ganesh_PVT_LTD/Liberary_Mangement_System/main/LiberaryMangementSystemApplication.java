package edu.Ganesh_PVT_LTD.Liberary_Mangement_System.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("edu")
@EntityScan("edu.Ganesh_PVT_LTD.Liberary_Mangement_System")
public class LiberaryMangementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiberaryMangementSystemApplication.class, args);
	}

}
