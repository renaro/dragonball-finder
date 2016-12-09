package com.jnj.hackthon;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renarosantos on 09/12/16.
 */
public class ControlResponse {

    @SerializedName("status")
    public String status;

    public ControlResponse(final String status) {
        this.status = status;
    }

}
