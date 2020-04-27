
package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.flatbook.model.Statistics;
import org.springframework.samples.flatbook.repository.AdvertisementRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.samples.flatbook.repository.HostRepository;
import org.springframework.samples.flatbook.repository.PersonRepository;
import org.springframework.samples.flatbook.repository.RequestRepository;
import org.springframework.samples.flatbook.repository.TenantRepository;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

	private PersonRepository		personRepository;

	private HostRepository			hostRepository;

	private TenantRepository		tenantRepository;

	private RequestRepository		requestRepository;

	private FlatRepository			flatRepository;

	private AdvertisementRepository	advertisementRepository;


	@Autowired
	public StatisticsService(final PersonRepository personRepository, final RequestRepository requestRepository, final HostRepository hostRepository, final FlatRepository flatRepository, final TenantRepository tenantRepository,
		final AdvertisementRepository advertisementRepository) {
		this.personRepository = personRepository;
		this.requestRepository = requestRepository;
		this.hostRepository = hostRepository;
		this.flatRepository = flatRepository;
		this.tenantRepository = tenantRepository;
		this.advertisementRepository = advertisementRepository;
	}

	public Statistics findStatistics() {
		Statistics statistics = new Statistics();
		statistics.setNumberOfRequests(this.requestRepository.numberOfRequests());
		statistics.setRatioOfAcceptedRequests(this.requestRepository.ratioOfAcceptedRequests());
		statistics.setRatioOfRejectedRequests(this.requestRepository.ratioOfRejectedRequests());
		statistics.setNumberOfAdvertisements(this.advertisementRepository.numberOfAdvertisements());
		statistics.setNumberOfFlats(this.flatRepository.numberOfFlats());
		statistics.setNumberOfUsers(this.personRepository.numberOfUsers());
		statistics.setRatioOfFlatsWithAdvertisement(this.flatRepository.ratioOfFlatsWithAdvertisement());
		statistics.setTopThreeMostReportedUsers(this.personRepository.topMostReportedUsers(PageRequest.of(0, 3)).getContent());
		statistics.setTopThreeWorstReviewedHosts(this.hostRepository.topWorstReviewedHosts(PageRequest.of(0, 3)).getContent());
		statistics.setTopThreeBestReviewedHosts(this.hostRepository.topBestReviewedHosts(PageRequest.of(0, 3)).getContent());
		statistics.setTopThreeBestReviewedFlats(this.flatRepository.topBestReviewedFlats(PageRequest.of(0, 3)).getContent());
		statistics.setTopThreeWorstReviewedFlats(this.flatRepository.topWorstReviewedFlats(PageRequest.of(0, 3)).getContent());
		statistics.setTopThreeBestReviewedTenants(this.tenantRepository.topBestReviewedTenants(PageRequest.of(0, 3)).getContent());
		statistics.setTopThreeWorstReviewedTenants(this.tenantRepository.topWorstReviewedTenants(PageRequest.of(0, 3)).getContent());
		return statistics;
	}

}
