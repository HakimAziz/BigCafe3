package com.example.zric7.bigcafe3.Model;

import java.util.List;

/*
Tangkap & inisiasikan respon Json dari web service
-- Getter setter
*/

public class MenuValue {
    String status;
    String message;
    List<MenuModel> menuModelList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MenuModel> getMenuModelList() {
        return menuModelList;
    }

    public void setMenuModelList(List<MenuModel> menuModelList) {
        this.menuModelList = menuModelList;
    }
}
