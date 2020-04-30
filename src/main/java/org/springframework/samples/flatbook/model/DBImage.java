
package org.springframework.samples.flatbook.model;

import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "images")
public class DBImage extends BaseEntity {

	@Column(name = "filename")
	private String	filename;

	@Column(name = "file_type")
	private String	fileType;

	@Lob
	@Column(name = "data")
	private byte[]	data;


	public DBImage() {
	}

	public DBImage(final MultipartFile file) throws IOException {
		this.filename = file.getOriginalFilename();
		this.fileType = file.getContentType();
		this.data = file.getBytes();
	}
}
