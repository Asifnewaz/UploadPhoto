package com.securepenny.camerapermissionandsave;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by R041708040 on 10/22/2017.
 */

public class UploadObject implements Serializable {

        @SerializedName("Success")
        @Expose
        private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
