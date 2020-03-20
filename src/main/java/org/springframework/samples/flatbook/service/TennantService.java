
package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Tennant;
import org.springframework.samples.flatbook.repository.TennantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TennantService {

	private TennantRepository tennantRepository;
	
	 @Autowired
	    public TennantService(TennantRepository tennantRepository) {
	        this.tennantRepository = tennantRepository;
	    }
	 
	 @Transactional(readOnly = true)
	    public Tennant findTennantById(String username) throws DataAccessException {
	        return this.tennantRepository.findById(username).get();
	    }
	 
}
