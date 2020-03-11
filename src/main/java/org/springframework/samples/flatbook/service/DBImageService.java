package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.repository.DBImageRepository;
import org.springframework.samples.flatbook.service.exceptions.FileStorageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@Service
public class DBImageService {

    private DBImageRepository dbImageRepository;

    @Autowired
    public DBImageService(DBImageRepository dbImageRepository) {
        this.dbImageRepository = dbImageRepository;
    }

    @Transactional(readOnly = true)
    public DBImage getImageById(int imageId) {
        return dbImageRepository.findById(imageId);
    }

    @Transactional(readOnly = true)
    public Collection<DBImage> getImagesByFlatId(int flatId) {
        return dbImageRepository.findManyByFlatId(flatId);
    }

    @Transactional
    public void storeImage(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("The filename contains invalid path sequence " + fileName);
            }

            DBImage image = new DBImage();
            image.setFilename(fileName);
            image.setFileType(file.getContentType());
            image.setData(file.getBytes());

            dbImageRepository.save(image);
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName, e);
        }
    }
}
