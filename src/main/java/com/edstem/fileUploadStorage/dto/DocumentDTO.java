package com.edstem.fileUploadStorage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
	private Long id;
	private String name;
	private String description;
	private String filePath;
	private String contentType;
	private Long size;
	private LocalDateTime uploadDate;
}
