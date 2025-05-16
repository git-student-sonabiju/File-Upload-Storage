package com.edstem.fileUploadStorage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUploadDTO {
	private String name;
	private String description;
	private MultipartFile file;
}
