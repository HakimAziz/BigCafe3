package com.example.zric7.bigcafe3.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderValue {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("result")
    @Expose
    private List<OrderModel> result = null;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<OrderModel> getResult() {
        return result;
    }

    public void setResult(List<OrderModel> result) {
        this.result = result;
    }
}
