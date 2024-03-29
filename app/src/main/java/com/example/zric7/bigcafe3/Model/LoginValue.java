package com.example.zric7.bigcafe3.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginValue {
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<LoginModel> getResult() {
        return result;
    }

    public void setResult(List<LoginModel> result) {
        this.result = result;
    }

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("result")
    @Expose
    private List<LoginModel> result = null;
}
