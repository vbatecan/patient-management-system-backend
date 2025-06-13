package com.vbatecan.patient_management_system;

import com.vbatecan.patient_management_system.service.IndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class PatientManagementSystemApplication {

	private final IndexingService indexingService;

	public static void main(String[] args) {
		SpringApplication.run(PatientManagementSystemApplication.class, args);
	}

}
