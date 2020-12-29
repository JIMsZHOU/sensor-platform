package edu.northeastern.process.reqres;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Jim Z on 12/28/20 23:45
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateEventRequest {
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private List<Pairs> condition;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pairs> getCondition() {
        return condition;
    }

    public void setCondition(List<Pairs> condition) {
        this.condition = condition;
    }
}
