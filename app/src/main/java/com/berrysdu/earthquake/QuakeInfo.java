package com.berrysdu.earthquake;

import java.text.DecimalFormat;

public class QuakeInfo {

    public QuakeInfo(float level_,String city_,String dateLine1_,String dateLine2_,String url_){
        level=level_;city=city_;dateLine1=dateLine1_;url=url_;dateLine2=dateLine2_;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getLevel() {
        DecimalFormat decimalFormat=new DecimalFormat("0.0");
        return Math.abs(Float.parseFloat(decimalFormat.format(level)));
    }


    public void setDateLong(long dateLong) {
        this.dateLong = dateLong;
    }

    public long getDateLong() {
        return dateLong;
    }

    public String getCity() {
        return city;
    }

    public String getDateLine1() {
        return dateLine1;
    }

    public String getDateLine2() {
        return dateLine2;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public void setLevel(float level) {
        this.level = level;
    }


    private long dateLong;
    private float level;
    private String city;
    private String dateLine1;
    private String dateLine2;
    private String url;
}
