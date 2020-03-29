package org.springframework.samples.flatbook.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.repository.DBImageRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@DataJpaTest(includeFilters= @ComponentScan.Filter(Service.class))
@ExtendWith(MockitoExtension.class)
public class DBImageServiceTests {

    @Autowired
    private DBImageService imageService;

    @Mock
    private DBImageRepository mockedImageRepository;

    private DBImageService imageServiceMockito;

    private static DBImage image;

    @BeforeAll
    static void setupMock() {
        image = new DBImage();
        image.setId(1);
        image.setFilename("image.jpg");
        image.setFileType("image/jpg");
        image.setData("image".getBytes());
    }

    @BeforeEach
    void instantiateMockService() {
        this.imageServiceMockito = new DBImageService(mockedImageRepository);
    }

    @Test
    void shouldFindImagesByFlatId() {
        Set<DBImage> images = this.imageService.getImagesByFlatId(1);
        assertThat(images).isNotEmpty();
        assertThat(images.size()).isEqualTo(6);
    }

    @Test
    void shouldNotFindImagesByFlatId() {
        Set<DBImage> images = this.imageService.getImagesByFlatId(10);
        assertThat(images).isEmpty();
    }

    @Test
    void shouldFindById() {
        when(this.mockedImageRepository.findById(1)).thenReturn(image);
        DBImage image = this.imageServiceMockito.getImageById(1);
        assertThat(image).isNotNull();
        assertThat(image.getId()).isEqualTo(1);
        assertThat(image.getFilename()).isEqualTo("image.jpg");
        assertThat(image.getFileType()).isEqualTo("image/jpg");
    }

    @Test
    void shouldNotFindById() {
        DBImage image = this.imageServiceMockito.getImageById(10);
        assertThat(image).isNull();
    }

    @Test
    void shouldDeleteImage() {
        doNothing().when(mockedImageRepository).delete(isA(DBImage.class));
        this.imageServiceMockito.deleteImage(image);

        verify(mockedImageRepository).delete(image);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTryingToDeleteNullImage() {
        doThrow(new IllegalArgumentException("Entity must not be null!")).when(this.mockedImageRepository).delete(isNull());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.imageServiceMockito.deleteImage(null));
        assertThat(exception.getMessage()).isEqualTo("Entity must not be null!");
        verify(this.mockedImageRepository).delete(null);
    }
}
