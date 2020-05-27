
package org.springframework.samples.flatbook.model.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"flatId", "hostId", "tenantId"
})
public class PerformanceTestInfo {

	@JsonProperty("flatId")
	private Integer	flatId;
	@JsonProperty("hostId")
	private String	hostId;
	@JsonProperty("tenantId")
	private String	tenantId;
	@JsonProperty("requestId")
	private Integer	requestId;
	@JsonProperty("reviewId")
	private Integer	reviewId;
	@JsonProperty("advertisementId")
	private Integer	advertisementId;


	@JsonProperty("advertisementId")
	public Integer getAdvertisementId() {
		return this.advertisementId;
	}

	@JsonProperty("advertisementId")
	public void setAdvertisementId(final Integer advertisementId) {
		this.advertisementId = advertisementId;
	}

	@JsonProperty("reviewId")
	public Integer getReviewId() {
		return this.reviewId;
	}

	@JsonProperty("reviewId")
	public void setReviewId(final Integer reviewId) {
		this.reviewId = reviewId;
	}

	@JsonProperty("requestId")
	public Integer getRequestId() {
		return this.requestId;
	}

	@JsonProperty("requestId")
	public void setRequestId(final Integer requestId) {
		this.requestId = requestId;
	}

	@JsonProperty("flatId")
	public Integer getFlatId() {
		return this.flatId;
	}

	@JsonProperty("flatId")
	public void setFlatId(final Integer flatId) {
		this.flatId = flatId;
	}

	@JsonProperty("hostId")
	public String getHostId() {
		return this.hostId;
	}

	@JsonProperty("hostId")
	public void setHostId(final String hostId) {
		this.hostId = hostId;
	}

	@JsonProperty("tenantId")
	public String getTenantId() {
		return this.tenantId;
	}

	@JsonProperty("tenantId")
	public void setTenantId(final String tenantId) {
		this.tenantId = tenantId;
	}

}
