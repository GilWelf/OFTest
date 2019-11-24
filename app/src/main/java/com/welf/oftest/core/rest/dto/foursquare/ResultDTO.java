package com.welf.oftest.core.rest.dto.foursquare;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

/**
 * DTO representing the json response of the Foursquare api call
 */
public class ResultDTO {

    @Expose
    private JsonObject meta;

    @Expose
    private JsonObject response;


    public JsonObject getMeta() {
        return meta;
    }

    public void setMeta(JsonObject meta) {
        this.meta = meta;
    }

    public JsonObject getResponse() {
        return response;
    }

    public void setResponse(JsonObject response) {
        this.response = response;
    }
}
