package org.springframework.samples.flatbook.web.apis.pojos;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "results",
    "status"
})
public class GeocodeResponse {

    @JsonProperty("results")
    private List<GeocodeResult> results = null;
    @JsonProperty("status")
    private String status;

    @JsonProperty("results")
    public List<GeocodeResult> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<GeocodeResult> results) {
        this.results = results;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

}
