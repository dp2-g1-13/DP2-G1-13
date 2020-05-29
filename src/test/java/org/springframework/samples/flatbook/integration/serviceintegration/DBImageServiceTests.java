package org.springframework.samples.flatbook.integration.serviceintegration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.service.DBImageService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest(includeFilters= @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(locations = "classpath:application-mysql.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class DBImageServiceTests {

    @Autowired
    private DBImageService imageService;

    private static final int ID = 1;
    private static final int ID_TO_BE_DELETED = 270;

    @Test
    void shouldFindImagesByFlatId() {
        Set<DBImage> images = this.imageService.getImagesByFlatId(ID);
        Assertions.assertThat(images).isNotEmpty();
        Assertions.assertThat(images.size()).isEqualTo(6);
    }

    @Test
    void shouldNotFindImagesByFlatId() {
        Set<DBImage> images = this.imageService.getImagesByFlatId(0);
        Assertions.assertThat(images).isEmpty();
    }

    @Test
    void shouldFindById() {
        DBImage image = this.imageService.getImageById(ID);
        assertThat(image).isNotNull();
        assertThat(image).hasId(1);
        assertThat(image).hasFilename("755093144.jpg");
        assertThat(image).hasFileType("image/jpeg");
    }

    @Test
    void shouldNotFindById() {
        DBImage image = this.imageService.getImageById(0);
        Assertions.assertThat(image).isNull();
    }

    @Test
    void shouldDeleteImage() {
        DBImage image = this.imageService.getImageById(ID_TO_BE_DELETED);
        this.imageService.deleteImage(image);
        image =  this.imageService.getImageById(ID_TO_BE_DELETED);
        assertThat(image).isNull();
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTryingToDeleteNullImage() {
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> this.imageService.deleteImage(null));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Entity must not be null!; nested exception is java.lang.IllegalArgumentException: Entity must not be null!");
    }
}
