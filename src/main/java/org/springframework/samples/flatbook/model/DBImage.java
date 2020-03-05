package org.springframework.samples.flatbook.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

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
}
