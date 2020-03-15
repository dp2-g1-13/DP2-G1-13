package org.springframework.samples.flatbook.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;

@Entity
@Getter
@Setter
@Table(name = "images")
public class DBImage extends BaseEntity {

    @NotEmpty
    @Column(name = "filename")
    private String filename;
    @NotEmpty
    @Column(name = "file_type")
    private String fileType;
    @NotEmpty
    @Lob
    @Column(name = "data")
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "flat_id")
    private Flat flat;

    public DBImage() {}

    public DBImage(MultipartFile file) throws IOException {
        this.filename = file.getOriginalFilename();
        this.fileType = file.getContentType();
        this.data = file.getBytes();
    }
}
