package com.example.zric7.bigcafe3.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
Tangkap & inisiasikan respon Json dari web service
-- Getter setter
*/

public class MenuValue {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("result")
    @Expose
    private List<MenuModel> result = null;



    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<MenuModel> getResult() {
        return result;
    }

    public void setResult(List<MenuModel> result) {
        this.result = result;
    }

//    @SerializedName("status")
//    String status;
//    @SerializedName("message")
//    String message;
//    List<MenuModel> menuModelList;

}
