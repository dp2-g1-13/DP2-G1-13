
package org.springframework.samples.flatbook.model;

import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "images")
public class DBImage extends BaseEntity {

	@NotEmpty
	@Column(name = "filename")
	private String	filename;
	@NotEmpty
	@Column(name = "file_type")
	private String	fileType;
	@NotEmpty
	@Lob
	@Column(name = "data")
	private byte[]	data;

	@ManyToOne
	@JoinColumn(name = "flat_id")
	private Flat	flat;


	public DBImage() {
	}

	public DBImage(final MultipartFile file) throws IOException {
		this.filename = file.getOriginalFilename();
		this.fileType = file.getContentType();
		this.data = file.getBytes();
	}
}
