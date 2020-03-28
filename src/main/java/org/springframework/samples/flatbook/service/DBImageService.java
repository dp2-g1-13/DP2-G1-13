package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.repository.DBImageRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.samples.flatbook.service.exceptions.FileStorageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
public class DBImageService {

    private DBImageRepository dbImageRepository;
    private FlatRepository flatRepository;

    @Autowired
    public DBImageService(DBImageRepository dbImageRepository) {
        this.dbImageRepository = dbImageRepository;
    }

    @Transactional(readOnly = true)
    public DBImage getImageById(int imageId) {
        return dbImageRepository.findById(imageId);
    }

    @Transactional(readOnly = true)
    public Set<DBImage> getImagesByFlatId(int flatId) {
        return dbImageRepository.findManyByFlatId(flatId);
    }

   @Transactional
    public void deleteImage(DBImage image) throws DataAccessException {
        this.dbImageRepository.delete(image);
   }


}
