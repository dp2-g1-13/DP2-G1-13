package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.model.TennantReview;
import org.springframework.samples.flatbook.repository.TennantReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TennantReviewService {

    private TennantReviewRepository tennantReviewRepository;

    @Autowired
    public TennantReviewService(TennantReviewRepository tennantReviewRepository) {
        this.tennantReviewRepository = tennantReviewRepository;
    }

    @Transactional(readOnly = true)
    public TennantReview findTennantReviewById(int tennantReviewId) throws DataAccessException {
        return this.tennantReviewRepository.findById(tennantReviewId);
    }
    
    @Transactional(readOnly = true)
    public Tennant findTennantOfTennantReviewById(int tennantReviewId) throws DataAccessException {
        return this.tennantReviewRepository.findTennantOfTennantReviewById(tennantReviewId);
    }
    
    @Transactional
    public void saveTennantReview(TennantReview tennantReview) throws DataAccessException {
        this.tennantReviewRepository.save(tennantReview);
    }
    
    @Transactional
	public void deleteTennantReviewById(final int tennantReviewId) {
    	this.tennantReviewRepository.deleteById(tennantReviewId);
	}
}
