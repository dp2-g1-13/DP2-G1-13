package org.springframework.samples.flatbook.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.repository.DBImageRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.samples.flatbook.util.assertj.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DBImageServiceMockedTests {

    @Mock
    private DBImageRepository mockedImageRepository;

    private DBImageService imageServiceMockito;

    private static DBImage image;
    private static DBImage image2;
    private static DBImage image3;
    private static DBImage image4;
    private static DBImage image5;
    private static DBImage image6;

    @BeforeAll
    static void setupMock() {
        image = new DBImage();
        image.setId(1);
        image.setFilename("image.jpg");
        image.setFileType("image/jpg");
        image.setData("image".getBytes());

        image2 = new DBImage();
        image2.setId(2);
        image2.setFilename("image2.jpg");
        image2.setFileType("image/jpg");
        image2.setData("image2".getBytes());

        image3 = new DBImage();
        image3.setId(3);
        image3.setFilename("image3.jpg");
        image3.setFileType("image/jpg");
        image3.setData("image3".getBytes());

        image4 = new DBImage();
        image4.setId(4);
        image4.setFilename("image4.jpg");
        image4.setFileType("image/jpg");
        image4.setData("image4".getBytes());

        image5 = new DBImage();
        image5.setId(5);
        image5.setFilename("image5.jpg");
        image5.setFileType("image/jpg");
        image5.setData("image5".getBytes());

        image6 = new DBImage();
        image6.setId(6);
        image6.setFilename("image6.jpg");
        image6.setFileType("image/jpg");
        image6.setData("image6".getBytes());
    }

    @BeforeEach
    void instantiateMockService() {
        this.imageServiceMockito = new DBImageService(mockedImageRepository);
    }

    @Test
    void shouldFindImagesByFlatId() {
        when(this.imageServiceMockito.getImagesByFlatId(1)).thenReturn(new HashSet<>(Arrays.asList(image, image2, image3, image4, image5, image6)));
        Set<DBImage> images = this.imageServiceMockito.getImagesByFlatId(1);
        Assertions.assertThat(images).isNotEmpty();
        Assertions.assertThat(images.size()).isEqualTo(6);
        Assertions.assertThat(images).extracting(DBImage::getFilename)
            .containsExactlyInAnyOrder("image.jpg", "image2.jpg", "image3.jpg", "image4.jpg", "image5.jpg", "image6.jpg");
    }

    @Test
    void shouldNotFindImagesByFlatId() {
        when(this.imageServiceMockito.getImagesByFlatId(0)).thenReturn(new HashSet<>());
        Set<DBImage> images = this.imageServiceMockito.getImagesByFlatId(0);
        Assertions.assertThat(images).isEmpty();
    }

    @Test
    void shouldFindById() {
        when(this.mockedImageRepository.findById(1)).thenReturn(image);
        DBImage image = this.imageServiceMockito.getImageById(1);
        assertThat(image).isNotNull();
        Assertions.assertThat(image.getId()).isEqualTo(1);
        assertThat(image).hasId(1);
        assertThat(image).hasFilename("image.jpg");
        assertThat(image).hasFileType("image/jpg");
    }

    @Test
    void shouldNotFindById() {
        DBImage image = this.imageServiceMockito.getImageById(10);
        Assertions.assertThat(image).isNull();
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
        Assertions.assertThat(exception.getMessage()).isEqualTo("Entity must not be null!");
        verify(this.mockedImageRepository).delete(null);
    }
}
