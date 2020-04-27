
package org.springframework.samples.flatbook.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics extends BaseEntity {

	private Integer			numberOfRequests;

	private Double			ratioOfAcceptedRequests;

	private Double			ratioOfRejectedRequests;

	private Double			ratioOfFlatsWithAdvertisement;

	private Integer			numberOfFlats;

	private Integer			numberOfAdvertisements;

	private Integer			numberOfUsers;

	private List<Host>		topThreeBestReviewedHosts;

	private List<Host>		topThreeWorstReviewedHosts;

	private List<Flat>		topThreeBestReviewedFlats;

	private List<Flat>		topThreeWorstReviewedFlats;

	private List<Tenant>	topThreeBestReviewedTenants;

	private List<Tenant>	topThreeWorstReviewedTenants;

	private List<Person>	topThreeMostReportedUsers;
}
