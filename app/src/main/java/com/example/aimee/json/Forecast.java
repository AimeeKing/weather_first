package com.example.aimee.json;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aimee on 2015/12/3.
 */
public class Forecast {
    String city;
    int cityId;
    Weather we[];
    More day;
    AQI Aqi;
    Forecast()
    {
        we=new Weather[5];
        day=new More();
        Aqi=new AQI();
    }
    /*
    json解析的过程就像是宅包裹，拆{}用getJSONObject,拆[]用getJSONArry
     */
    void getJsonObeject(String message)
    {
        try {
            JSONObject JSONinfo=new JSONObject(message);
            JSONObject forecast=JSONinfo.getJSONObject("forecast");
            JSONObject realtime=JSONinfo.getJSONObject("realtime");
            JSONObject today=JSONinfo.getJSONObject("today");
            JSONObject aqi=JSONinfo.getJSONObject("aqi");
            JSONObject accu_cc=JSONinfo.getJSONObject("accu_cc");
            getjsonfor(forecast);
            getrealtime(realtime);
            gettoday(today);
            getaccu(accu_cc);
            getaqi(aqi);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    void getjsonfor(JSONObject forecast)
    {
        try {
            city=forecast.getString("city");
            cityId=forecast.getInt("cityid");
            for (int i = 0; i < 5; i++) {
                we[i] = new Weather();
                we[i].fl = forecast.getString("fl" + (i+1));
                we[i].title_1=forecast.getString("img_title"+(2*i+1));
                we[i].title_2=forecast.getString("img_title"+(2*i+2));
                we[i].wind=forecast.getString("wind"+(i+1));
                we[i].temp=forecast.getString("temp"+(i+1));
                we[i].weather=forecast.getString("weather"+(i+1));
            }
        }catch(Exception e)
        {
            System.out.print(e.toString());
        }
    }
    void getrealtime(JSONObject realtime)
    {
        try {
            day.SD=realtime.getString("SD");
            day.WD=realtime.getString("WD");
            day.WS=realtime.getString("WS");
            day.temp=realtime.getInt("temp");
            day.time=realtime.getString("time");
            day.weather=realtime.getString("weather");
        }catch(Exception e)
        {
            System.out.print(e.toString());
        }
    }

    void gettoday(JSONObject today)
    {
        try {
            day.cityCode=today.getInt("cityCode");
            day.humidityMin=today.getInt("humidityMin");
            day.humidityMax=today.getInt("humidityMax");
            day.precipitationMax=today.getInt("precipitationMax");
            day.precipitationMin=today.getInt("precipitationMin");
            day.tempMax=today.getInt("tempMax");
            day.tempMin=today.getInt("tempMin");
            day.windMax=today.getInt("windMax");
            day.windMin=today.getInt("windMin");
        }catch(Exception e)
        {
            System.out.print(e.toString());
        }
    }

    void getaqi(JSONObject aqi)
    {
        try {
            Aqi.aqi=aqi.getInt("aqi");
            Aqi.pub_time=aqi.getString("pub_time");
            Aqi.pm25=aqi.getInt("pm25");
        }catch(Exception e)
        {
            System.out.print(e.toString());
        }

    }
    void getaccu(JSONObject accu_cc)
    {
        try {
            day.Visibility=accu_cc.getString("Visibility");
            day.UVIndex=accu_cc.getInt("UVIndex");
            day.RealFeelTemperature=accu_cc.getString("RealFeelTemperature");
            day.RelativeHumidity=accu_cc.getInt("RelativeHumidity");
            day.Pressure=accu_cc.getString("Pressure");
            day.WindSpeed=accu_cc.getString("WindSpeed");
        }catch(Exception e)
        {
            System.out.print(e.toString());
        }
    }
}
