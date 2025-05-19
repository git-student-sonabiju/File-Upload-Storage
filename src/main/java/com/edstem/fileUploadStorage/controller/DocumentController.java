package com.edstem.fileUploadStorage.controller;

import com.edstem.fileUploadStorage.dto.DocumentDTO;
import com.edstem.fileUploadStorage.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

	private final DocumentService documentService;

	public DocumentController(DocumentService documentService) {
		this.documentService = documentService;
	}

	@PostMapping("/upload")
	public ResponseEntity<DocumentDTO> uploadDocument(
			@RequestParam("name") String name,
			@RequestParam("description") String description,
			@RequestParam("file") MultipartFile file) throws IOException {
		DocumentDTO uploaded = documentService.uploadDocument(name, description, file);
		return new ResponseEntity<>(uploaded, HttpStatus.CREATED);
	}

	@GetMapping("/{id}/download")
	public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) throws IOException {
		Resource file = documentService.downloadDocument(id);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(Files.probeContentType(file.getFile().toPath())))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@GetMapping
	public List<DocumentDTO> listAllDocuments() {
		return documentService.listAllDocuments();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDocument(@PathVariable Long id) throws IOException {
		documentService.deleteDocument(id);
		return ResponseEntity.noContent().build();
	}
}
