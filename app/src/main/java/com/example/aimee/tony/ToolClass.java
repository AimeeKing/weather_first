package com.example.aimee.tony;

import android.content.Context;
import android.widget.Toast;

import com.example.aimee.json.Forecast;
import com.example.aimee.weather.MainActivity;
import com.example.aimee.weather.R;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Tony- on 2015/12/22.
 */
public class ToolClass {
    private static HashMap<String,Forecast> forecast = new HashMap<>();
    public static Forecast getSharedForcast(String city){
        return forecast.get(city);
    }
    public static void setSharedForcast(Forecast f){
        forecast.put(f.get_city(),f);
    }

    public static String getJSONString(String cityID){
        try{
            URL url = new URL("http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId="+cityID+".html");
            HttpURLConnection hturlC = (HttpURLConnection)url.openConnection();
            if(hturlC.getResponseCode()==HttpURLConnection.HTTP_OK){
                //Toast.makeText(this,"连接天气API成功",Toast.LENGTH_SHORT).show();

                InputStreamReader isr = new InputStreamReader(hturlC.getInputStream(),"utf-8");
                int i;
                String content = "";
                while((i=isr.read())!=-1){
                    content+=(char)i;
                }
                isr.close();
                return content;
            }
            hturlC.disconnect();
        }catch (Exception e){
            System.out.println("get api failure!");
        }
        return null;
    }

    public static int getimg_weather(String weather)
    {
        if( weather.equals("多云"))
            return R.drawable.biz_plugin_weather_duoyun;
        else
        if(weather.equals("阵雨"))
            return R.drawable.biz_plugin_weather_zhenyu;
        else
        if(weather.equals("晴"))
            return R.drawable.biz_plugin_weather_qing;
        else
        if(weather.equals("中雨"))
            return R.drawable.biz_plugin_weather_zhongyu;
        else
        if(weather.equals("小雨"))
            return R.drawable.biz_plugin_weather_xiaoyu;
        else
        if(weather.equals("阴"))
            return R.drawable.biz_plugin_weather_yin;
        else
        if(weather.equals("暴雪"))
            return R.drawable.biz_plugin_weather_baoxue;
        else
        if(weather.equals("暴雨"))
            return R.drawable.biz_plugin_weather_baoyu;
        else
        if(weather.equals("大暴雨"))
            return R.drawable.biz_plugin_weather_dabaoyu;
        else
        if(weather.equals( "大雪"))
            return R.drawable.biz_plugin_weather_daxue;
        else
        if(weather.equals("大雨"))
            return R.drawable.biz_plugin_weather_dayu;
        else
        if(weather.equals("雷阵雨"))
            return R.drawable.biz_plugin_weather_leizhenyu;
        else
        if(weather.equals("雷阵雨冰雹")||weather.equals("冰雹"))
            return R.drawable.biz_plugin_weather_leizhenyubingbao;
        else
        if(weather.equals("沙尘暴"))
            return R.drawable.biz_plugin_weather_shachenbao;
        else
        if(weather.equals("特大暴雨"))
            return R.drawable.biz_plugin_weather_tedabaoyu;
        else
        if(weather.equals("雾")||weather.equals("霾"))
            return R.drawable.biz_plugin_weather_wu;
        else
        if(weather.equals("小雪"))
            return R.drawable.biz_plugin_weather_xiaoxue;
        else
        if(weather.equals("雨夹雪"))
            return R.drawable.biz_plugin_weather_yujiaxue;
        else
        if(weather.equals("阵雪"))
            return R.drawable.biz_plugin_weather_zhenxue;
        else
        if(weather.equals("中雪"))
            return R.drawable.biz_plugin_weather_zhongxue;
        else
            return R.drawable.biz_plugin_weather_qing;
    }
}
