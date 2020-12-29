package edu.northeastern.process.reqres;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.northeastern.base.enums.AlertLevel;

import java.util.Date;
import java.util.List;

/**
 * Created by Jim Z on 12/27/20 01:31
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventRequest {

    @JsonProperty
    private String type;
    @JsonProperty
    private String key;
    @JsonProperty
    private AlertLevel level;
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date expireTime;
    @JsonProperty
    private String toWhom;
    @JsonProperty
    private String message;
    @JsonProperty
    private List<Pairs> conditions;
    @JsonProperty
    private String query;
    @JsonProperty
    private String url;
    @JsonProperty
    private String cronExpression;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public AlertLevel getLevel() {
        return level;
    }

    public void setLevel(AlertLevel level) {
        this.level = level;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getToWhom() {
        return toWhom;
    }

    public void setToWhom(String toWhom) {
        this.toWhom = toWhom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Pairs> getConditions() {
        return conditions;
    }

    public void setConditions(List<Pairs> conditions) {
        this.conditions = conditions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
