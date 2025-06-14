package com.vbatecan.patient_management_system.controller;

import com.vbatecan.patient_management_system.model.responses.MessageResponse;
import com.vbatecan.patient_management_system.service.IndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IndexingController {

	private final IndexingService indexingService;

	@PostMapping("/reindex")
	public ResponseEntity<MessageResponse> index() {
		this.indexingService.reindexAll();
		return ResponseEntity.ok().build();
	}
}
