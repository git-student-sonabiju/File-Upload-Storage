package com.edstem.fileUploadStorage.service;

import com.edstem.fileUploadStorage.dto.DocumentDTO;
import com.edstem.fileUploadStorage.model.Document;
import com.edstem.fileUploadStorage.repository.DocumentRepository;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentService {
	private final DocumentRepository documentRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public DocumentService(DocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}

	public DocumentDTO uploadDocument(String name, String description, MultipartFile file) throws IOException {
		if (file.isEmpty()) throw new IllegalArgumentException("File is empty");
		if (file.getSize() > 10 * 1024 * 1024) throw new IllegalArgumentException("File too large");

		String contentType = file.getContentType();
		if (!List.of("application/pdf", "image/png", "image/jpeg").contains(contentType)) {
			throw new IllegalArgumentException("Unsupported file type: " + contentType);
		}
		String folderPath = uploadDir + "/" + LocalDate.now();
		Files.createDirectories(Paths.get(folderPath));
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(folderPath, fileName);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		Document document = new Document();
		document.setName(name);
		document.setDescription(description);
		document.setFilePath(filePath.toString());
		document.setContentType(contentType);
		document.setSize(file.getSize());
		document.setUploadDate(LocalDateTime.now());

		Document saved = documentRepository.save(document);
		return mapToDTO(saved);
	}

	public Resource downloadDocument(Long id) throws IOException {
		Document document = documentRepository.findById(id)
				.orElseThrow(() -> new FileNotFoundException("Document not found"));

		Path path = Paths.get(document.getFilePath());
		if (!Files.exists(path)) {
			throw new FileNotFoundException("File not found on disk");
		}

		return new UrlResource(path.toUri());
	}

	public List<DocumentDTO> listAllDocuments() {
		return documentRepository.findAll().stream()
				.map(this::mapToDTO)
				.collect(Collectors.toList());
	}

	public void deleteDocument(Long id) throws IOException {
		Document document = documentRepository.findById(id)
				.orElseThrow(() -> new FileNotFoundException("Document not found"));

		Files.deleteIfExists(Paths.get(document.getFilePath()));
		documentRepository.delete(document);
	}

	private DocumentDTO mapToDTO(Document doc) {
		DocumentDTO dto = new DocumentDTO();
		dto.setId(doc.getId());
		dto.setName(doc.getName());
		dto.setDescription(doc.getDescription());
		dto.setFilePath(doc.getFilePath());
		dto.setContentType(doc.getContentType());
		dto.setSize(doc.getSize());
		dto.setUploadDate(doc.getUploadDate());
		return dto;
	}
}
