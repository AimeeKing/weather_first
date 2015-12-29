package com.example.aimee.json;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aimee on 2015/12/3.
 */
public class Forecast  {
    String city;
    String cityId;
    Weather we[];
    More day;
    AQI Aqi;
    Alert alert;
    Accu_f accu_f;
    public Accu_f getAccu_f() {
        return accu_f;
    }

    public Alert getAlert() {
        return alert;
    }
    public String get_city()
    {
        return  city;
    }

    public String getCityId()
    {
        return  cityId;
    }

    public Weather[] getWeather()
    {
        return we;
    }

    public More get_day()
    {
        return day;
    }

    public AQI getAqi()
    {
        return Aqi;
    }

    private String result_t;
    public Forecast(String city,String cityId)
    {
        we=new Weather[5];
        day=new More();
        Aqi=new AQI();
        alert=new Alert();
        this.city=city;
        this.cityId=cityId;
        accu_f=new Accu_f();

    }
    /*
    json解析的过程就像是宅包裹，拆{}用getJSONObject,拆[]用getJSONArry
     */
    public void getJsonObeject(String message)
    {
        try {
            JSONObject JSONinfo=new JSONObject(message);
            JSONObject forecast=JSONinfo.getJSONObject("forecast");
            JSONObject realtime=JSONinfo.getJSONObject("realtime");
            JSONObject today=JSONinfo.getJSONObject("today");
            JSONObject aqi=JSONinfo.getJSONObject("aqi");
            JSONObject accu_cc=JSONinfo.getJSONObject("accu_cc");
            JSONArray alert_json=JSONinfo.getJSONArray("alert");
            JSONObject alert_json_obj;
            JSONObject acc_obj_1=JSONinfo.getJSONObject("accu_f5");
            JSONObject acc_obj_2=acc_obj_1.getJSONArray("DailyForecasts").getJSONObject(0);
            getjsonfor(forecast);
            getrealtime(realtime);
            gettoday(today);
            getaccu(accu_cc);
            getaccu_u5(acc_obj_2);
            getaqi(aqi);
            if(!alert_json.equals(new JSONArray())) {
                alert_json_obj = alert_json.getJSONObject(0);
                if(alert_json!=null)
                getalert(alert_json_obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getaccu_u5(JSONObject acc_obj_2) {
        try {
            //EpochDate,Sun_EpochRise,Sun_EpochSet
        /*
        *                "Date": "2015-12-26T07:00:00+08:00",
                "EpochDate": "1451084400",
                "Sun_Rise": "2015-12-26T06:54:00+08:00",
                "Sun_EpochRise": "1451084040",
                "Sun_Set": "2015-12-26T17:06:00+08:00",
                "Sun_EpochSet": "1451120760",
                "PrecipitationProbability": "15"
        * */

            String EpochDate=acc_obj_2.getString("EpochDate");
            String Sun_Rise=acc_obj_2.getString("Sun_Rise");
            String Sun_Set=acc_obj_2.getString("Sun_Set");
            accu_f.EpochDate=EpochDate;

            accu_f.Sun_EpochRise=getdate(Sun_Rise);

            accu_f.Sun_EpochSet=getdate(Sun_Set);
            accu_f.PrecipitationProbability=String.valueOf(acc_obj_2.getInt("PrecipitationProbability"));

        }catch(Exception e)
        {
            System.out.print(e.toString());
        }
    }

    private String getdate(String strDate)
    {
        String pat1 = "yyyy-MM-dd'T'HH:mm:ss'+08:00'" ;
        String pat2 = "HH:mm" ;
        SimpleDateFormat sdf1 = new SimpleDateFormat(pat1) ;
        SimpleDateFormat sdf2 = new SimpleDateFormat(pat2) ;        // 实例化模板对象
        Date d = null ;
        try{
            d = sdf1.parse(strDate) ;   // 将给定的字符串中的日期提取出来
        }catch(Exception e){            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace() ;       // 打印异常信息
        }
        return sdf2.format(d) ;    // 将日期变为新的格式
    }

    void getjsonfor(JSONObject forecast)
    {
        try {
            city=forecast.getString("city");
            cityId=String.valueOf(forecast.getInt("cityid"));
            for (int i = 0; i < 5; i++) {
                we[i] = new Weather();
                we[i].fl = forecast.getString("fl" + (i+1));
                we[i].title_1=forecast.getString("img_title"+(2*i+1));
                we[i].title_2=forecast.getString("img_title"+(2*i+2));
                we[i].wind=forecast.getString("wind"+(i+1));
                we[i].temp=forecast.getString("temp"+(i+1));
                we[i].weather=forecast.getString("weather"+(i+1));
                we[i].day=acu_date(i);
            }
        }catch(Exception e)
        {
            System.out.print(e.toString());
        }
    }
    void getalert(JSONObject alert_json)
    {
        try {
            alert.abnormal=alert_json.getString("abnormal");
            alert.city_code=alert_json.getString("city_code");
            alert.detail=alert_json.getString("detail");
            alert.holiday=alert_json.getString("holiday");
            alert.level=alert_json.getString("time");//21:15
            alert.pub_time=alert_json.getString("pub_time");
            alert.title=alert_json.getString("title");//21:15
            alert.type=alert_json.getString("type");

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
            day.time=realtime.getString("time");//21:15
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
            Aqi.pm10=aqi.getInt("pm10");
            Aqi.so2=aqi.getInt("so2");
            Aqi.no2=aqi.getInt("no2");
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
            day.WindDirectionDegrees=accu_cc.getInt("WindDirectionDegrees");
        }catch(Exception e)
        {
            System.out.print(e.toString());
        }
    }

    private String acu_date(int i)//i表示距离今天有多少天
    {
        Date date=new Date();
        DateFormat dateFormat=new SimpleDateFormat("MM/dd");
        long data=(date.getTime()/1000)+i*60*60*24;
        date.setTime(data*1000);
        return dateFormat.format(date);
    }

}
