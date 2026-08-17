package com.beautytalk.lannet.beautytalk.adapters;

public class DataObject {
    private String mText1;
    private String mText2;
    private String path;
    private String nameTxt;
    private String statusText;
    private String picname;
    private String shareName;

    public DataObject(String text1, String text2,String imgPath,String nm,String stTxt,String pic,String sName){
        mText1 = text1;
        mText2 = text2;
        path=imgPath;
        nameTxt=nm;
        statusText=stTxt;
        picname=pic;
        shareName=sName;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public String getPicname() {
        return picname;
    }

    public void setPicname(String picname) {
        this.picname = picname;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getNameTxt() {
        return nameTxt;
    }

    public void setNameTxt(String nameTxt) {
        this.nameTxt = nameTxt;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}