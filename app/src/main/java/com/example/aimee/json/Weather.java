package com.example.aimee.json;

/**
 * Created by Aimee on 2015/12/3.
 */
public class Weather {
    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTitle_1() {
        return title_1;
    }

    public void setTitle_1(String title_1) {
        this.title_1 = title_1;
    }

    public String getTitle_2() {
        return title_2;
    }

    public void setTitle_2(String title_2) {
        this.title_2 = title_2;
    }

    public int getPrecipitationProbability() {
        return PrecipitationProbability;
    }

    public void setPrecipitationProbability(int precipitationProbability) {
        PrecipitationProbability = precipitationProbability;
    }
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
    String fl;
    String wind;
    String weather;
    String temp;
    String title_1;
    String title_2;
    String day;
    int PrecipitationProbability;//降水概率
}
