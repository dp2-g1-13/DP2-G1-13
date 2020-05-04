package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.repository.FlatReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlatReviewService {

    private FlatReviewRepository flatReviewRepository;

    @Autowired
    public FlatReviewService(FlatReviewRepository flatReviewRepository) {
        this.flatReviewRepository = flatReviewRepository;
    }

    @Transactional(readOnly = true)
    public FlatReview findFlatReviewById(int flatReviewId) {
        return this.flatReviewRepository.findById(flatReviewId);
    }

    @Transactional
    public void saveFlatReview(FlatReview flatReview) {
        this.flatReviewRepository.save(flatReview);
    }

    @Transactional
	public void deleteFlatReviewById(final int flatReviewId) {
		this.flatReviewRepository.deleteById(flatReviewId);
	}
}
