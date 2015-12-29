package com.example.aimee.json;

/**
 * Created by Aimee on 2015/12/3.
 */
public class AQI {
    public int getCidyID() {
        return cidyID;
    }

    public void setCidyID(int cidyID) {
        this.cidyID = cidyID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPub_time() {
        return pub_time;
    }

    public void setPub_time(String pub_time) {
        this.pub_time = pub_time;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    public int getSo2() {
        return so2;
    }

    public void setSo2(int so2) {
        this.so2 = so2;
    }

    public int getNo2() {
        return no2;
    }

    public void setNo2(int no2) {
        this.no2 = no2;
    }

    int cidyID;
    String city;
    String pub_time;
    int aqi;
    int pm25;
    int pm10;
    int so2;
    int no2;
}
