package edu.northeastern.process.reqres;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jim Z on 12/27/20 04:07
 */
public class Pairs {
    @JsonProperty(value = "key", required = true)
    private String k;
    @JsonProperty(value = "value", required = true)
    private String v;

    public String getK() {
        return k;
    }
    public void setK(String k) {
        this.k = k;
    }
    public String getV() {
        return v;
    }
    public void setV(String v) {
        this.v = v;
    }
}
