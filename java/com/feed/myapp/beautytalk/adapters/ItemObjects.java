package com.beautytalk.lannet.beautytalk.adapters;

/**
 * Created by Lannet1 on 4/17/2018.
 */

public class ItemObjects {

    String imgagePath;
    String datestring;
    String idStr;
    public ItemObjects(String img, String dt,String id)
    {
        imgagePath=img;
        datestring=dt;
        idStr=id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getImgagePath() {
        return imgagePath;
    }

    public void setImgagePath(String imgagePath) {
        this.imgagePath = imgagePath;
    }

    public String getDatestring() {
        return datestring;
    }

    public void setDatestring(String datestring) {
        this.datestring = datestring;
    }
}
