package com.jnj.hackthon;

import com.google.gson.annotations.SerializedName;

/**
 * Created by renarosantos on 09/12/16.
 */
public class RadarResponse {

    @SerializedName("radar")
    public long radar;

    @SerializedName("cordX")
    public long cordX;

    @SerializedName("cordY")
    public long cordY;


    public long radar() {
        return radar;
    }

    public long cordX() {
        return cordX;
    }

    public long cordY() {
        return cordY;
    }
}
