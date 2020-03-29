
package org.springframework.samples.flatbook.model;

import java.util.List;

public class Indicators {

	Integer			totalAcceptedRequests;
	Integer			totalOfAcceptedRequests;
	Integer			totalOfRejectededRequests;
	Double			ratioOfAcceptedRequests;
	Double			ratioOfRejectedRequests;

	List<Person>	mostReportedUsers;
	List<Person>	mostHighRatedUsers;
	List<Person>	mostLowRatedUsers;
}
