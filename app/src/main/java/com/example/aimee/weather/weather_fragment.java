package com.example.aimee.weather;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aimee.json.Forecast;
import com.example.aimee.tony.ToolClass;
import com.example.aimee.util.HttpUtils;


/**
 * Created by Aimee on 2015/12/3.
 */
public class weather_fragment extends Fragment {
    public String getCity() {
        return city;
    }

    public String getcityid() {
        return forecast.getCityId();
    }

    public String city;
    public Forecast forecast;
    private View v;
    private long updatetime;

    private TextView txt_temp_now;
    private TextView txt_pm25;
    private TextView txt_wind_dir;
    private TextView txt_wind_grd;
    private TextView weather1;
    private TextView weather2;
    private TextView weather3;
    private TextView weather4;
    private TextView temp1;
    private TextView temp2;
    private TextView temp3;
    private TextView temp4;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img_today;
    private Handler UIhandler;
    private TextView txt_city;
    private TextView txt_updatetime;
    private TextView txt_day_1;
    private TextView txt_day_2;
    private TextView txt_day_3;
    private TextView txt_day_4;
    private ImageButton btn_show;
    private TextView moreinfo;
    private SwipeRefreshLayout swipeRefreshLayout;


    int[] weather = {R.id.txt_weahter_1, R.id.txt_weather_2, R.id.txt_weahter_3, R.id.txt_weahter_4};
    int[] temp = {R.id.txt_temp_1, R.id.txt_temp_2, R.id.txt_temp_3, R.id.txt_temp_4};
    int[] img = {R.id.img_weather_1, R.id.img_weather_2, R.id.img_weather_3, R.id.img_weather_4};

    public weather_fragment(String city, Forecast forecast) {
        this.city = city;
        this.forecast = forecast;
    }

    public weather_fragment() {

    }

    public View get_v()
    {
        return v;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.weather_frag, container, false);
        TextView textView = (TextView) v.findViewById(R.id.txt_city);
        textView.setText(city);
        btn_show = (ImageButton) v.findViewById(R.id.hide_show);
        UIhandler = new Handler();
        setfore();
        return v;
    }
    /**
     * 初始化界面内容的ID
     * 因为在线程里面不能更改主线程的界面
     * 必须用handler来修改
     * */
    private void initView(View v){
        moreinfo=(TextView)findById(R.id.moreimfo);
        txt_day_1=(TextView)findById(R.id.txt_day_1th);
        txt_day_2=(TextView)findById(R.id.txt_day_2th);
        txt_day_3=(TextView)findById(R.id.txt_day_3th);
        txt_day_4=(TextView)findById(R.id.txt_day_4th);
        txt_updatetime=(TextView)v.findViewById(R.id.txt_updatatime);
        txt_city = (TextView) v.findViewById(R.id.txt_city);
        txt_temp_now = (TextView) v.findViewById(R.id.txt_temp_now);
        txt_pm25 = (TextView) v.findViewById(R.id.txt_pm2_5);
        txt_wind_dir = (TextView) v.findViewById(R.id.txt_wind_dir);
        txt_wind_grd = (TextView) v.findViewById(R.id.txt_wind_grd);
        weather1 = (TextView) v.findViewById(R.id.txt_weahter_1);
        weather2 = (TextView) v.findViewById(R.id.txt_weather_2);
        weather3 = (TextView) v.findViewById(R.id.txt_weahter_3);
        weather4 = (TextView) v.findViewById(R.id.txt_weahter_4);
        temp1 = (TextView) v.findViewById(R.id.txt_temp_1);
        temp2 = (TextView) v.findViewById(R.id.txt_temp_2);
        temp3 = (TextView) v.findViewById(R.id.txt_temp_3);
        temp4 = (TextView) v.findViewById(R.id.txt_temp_4);
        img1 = (ImageView) v.findViewById(R.id.img_weather_1);
        img2 = (ImageView) v.findViewById(R.id.img_weather_2);
        img3 = (ImageView) v.findViewById(R.id.img_weather_3);
        img4 = (ImageView) v.findViewById(R.id.img_weather_4);
        img_today = (ImageView) v.findViewById(R.id.img_weather_today);
    }


    private void initforecast()
    {
        HttpUtils httputils=new HttpUtils();
        String url="http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId="+getcityid()+"&imei=529e2dd3d767bdd3595eec30dd481050&device=pisces&miuiVersion=JXCCNBD20.0&modDevice=&source=miuiWeatherApp";
        String result=httputils.doGet(url);
        forecast.getJsonObeject(result);
    }
    /**
     * 这个是我的界面更改函数
     * 用handler来修改界面
     * 因为handler是在oncreate里面声明的
     * 所以它能够更改界面
     * */
    private void TupdateUI(){
        UIhandler.post(new Runnable() {
            @Override
            public void run() {

                txt_city.setText(forecast.get_city());
                txt_temp_now.setText(String.valueOf(forecast.get_day().getTemp()));
                txt_pm25.setText(String.valueOf(forecast.getAqi().getPm25()));
                txt_wind_dir.setText(forecast.get_day().getWD());
                txt_wind_grd.setText(forecast.get_day().getWS());
                txt_day_1.setText(forecast.getWeather()[1].getDay());
                txt_day_2.setText(forecast.getWeather()[2].getDay());
                txt_day_3.setText(forecast.getWeather()[3].getDay());
                txt_day_4.setText(forecast.getWeather()[4].getDay());
                weather1.setText(forecast.getWeather()[1].getWeather());
                weather2.setText(forecast.getWeather()[2].getWeather());
                weather3.setText(forecast.getWeather()[3].getWeather());
                weather4.setText(forecast.getWeather()[4].getWeather());
                temp1.setText(forecast.getWeather()[1].getTemp());
                temp2.setText(forecast.getWeather()[2].getTemp());
                temp3.setText(forecast.getWeather()[3].getTemp());
                temp4.setText(forecast.getWeather()[4].getTemp());
                img1.setImageResource(ToolClass.getimg_weather(forecast.getWeather()[1].getTitle_1()));
                img2.setImageResource(ToolClass.getimg_weather(forecast.getWeather()[2].getTitle_1()));
                img3.setImageResource(ToolClass.getimg_weather(forecast.getWeather()[3].getTitle_1()));
                img4.setImageResource(ToolClass.getimg_weather(forecast.getWeather()[4].getTitle_1()));
                img_today.setImageResource(ToolClass.getimg_weather(forecast.get_day().getWeather()));

                txt_updatetime.setText("更新时间 " + forecast.get_day().getTime());
                moreinfo.setText("Aqi:"+forecast.getAqi().getAqi()+ " 可见度:"+forecast.get_day().getVisibility()
                        +"紫外线:"+forecast.get_day().getUVIndex()+"\n气压:"+forecast.get_day().getPressure()+
                        " 日出:"+forecast.getAccu_f().getSun_EpochRise()+" 日落:"+forecast.getAccu_f().getSun_EpochSet()+
                        " 体感湿度:"+forecast.get_day().getRelativeHumidity()+"体感温度:"+forecast.get_day().getRealFeelTemperature()+"℃"
                        +" 降雨概率："+forecast.getAccu_f().getPrecipitationProbability()+"%");

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moreinfo.getVisibility()==View.VISIBLE)//可见状态改为不可见
                {
                   // Animation animation = new TranslateAnimation(0,0,0,1000);
                   // animation.setDuration(1000);
                   // moreinfo.startAnimation(animation);
                    moreinfo.setVisibility(View.GONE);
                }
                else
                {
                   // Animation animation = new TranslateAnimation(0,0,0,1000);
                   // animation.setDuration(1000);
                   // moreinfo.startAnimation(animation);
                    moreinfo.setVisibility(View.VISIBLE);
                }
            }
        });
            }
        });

    }

    private void updateUI()
    {
        txt_city.setText(forecast.get_city());
        txt_temp_now.setText(String.valueOf(forecast.get_day().getTemp()));
        txt_pm25.setText(String.valueOf(forecast.getAqi().getPm25()));
        txt_wind_dir.setText(forecast.get_day().getWD());
        txt_wind_grd.setText(forecast.get_day().getWS());
        weather1.setText(forecast.getWeather()[1].getWeather());
        weather2.setText(forecast.getWeather()[2].getWeather());
        weather3.setText(forecast.getWeather()[3].getWeather());
        weather4.setText(forecast.getWeather()[4].getWeather());
        temp1.setText(forecast.getWeather()[1].getTemp());
        temp2.setText(forecast.getWeather()[2].getTemp());
        temp3.setText(forecast.getWeather()[3].getTemp());
        temp4.setText(forecast.getWeather()[4].getTemp());
        img1.setImageResource(ToolClass.getimg_weather(forecast.getWeather()[1].getTitle_1()));
        img2.setImageResource(ToolClass.getimg_weather(forecast.getWeather()[2].getTitle_1()));
        img3.setImageResource(ToolClass.getimg_weather(forecast.getWeather()[3].getTitle_1()));
        img4.setImageResource(ToolClass.getimg_weather(forecast.getWeather()[4].getTitle_1()));
        img_today.setImageResource(ToolClass.getimg_weather(forecast.get_day().getWeather()));
    }

    private void setfore() {
        this.updatetime=System.currentTimeMillis();
//        HttpUtils httputils=new HttpUtils();
//        String url="http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId="+getcityid()+"&imei=529e2dd3d767bdd3595eec30dd481050&device=pisces&miuiVersion=JXCCNBD20.0&modDevice=&source=miuiWeatherApp";
//        httputils.doGetAsyn(url, new HttpUtils.CallBack() {
//            @Override
//            public void onRequestComplete(String result) {
//                //getJsonObeject(result);
//                forecast.getJsonObeject(result);
//                updateUI(v);
//            }
//        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result  = ToolClass.getJSONString(getcityid());
                if(result!=null) {
                    initView(v);
                    forecast.getJsonObeject(result);
                    ToolClass.setSharedForcast(forecast);
                    TupdateUI();
                  //  updateUI();
                }else{
                    System.out.println("get city "+forecast.get_city()+" info fail ,result is null,please check!");
                }
            }
        });
        thread.start();

    }


    public void setForecast(Forecast forecast)
        {

            if(this.updatetime-System.currentTimeMillis()>10800000||forecast.get_city()!=this.forecast.get_city()) {
                this.forecast = forecast;
                setfore();
            }
        }

    public void updateUI(View v)
    {
        TextView textView=(TextView)v.findViewById(R.id.txt_temp_now);//现在天气
        textView.setText(String.valueOf(forecast.get_day().getTemp()));
        textView= (TextView) v.findViewById(R.id.txt_pm2_5);//PM2.5
        textView.setText(String.valueOf(forecast.getAqi().getPm25()));
        textView= (TextView) v.findViewById(R.id.txt_wind_dir);//风向
        textView.setText(forecast.get_day().getWD());
        textView= (TextView) v.findViewById(R.id.txt_wind_grd);//风力
        textView.setText(forecast.get_day().getWS());

        ImageView image= (ImageView) findById(R.id.img_weather_today);
        image.setImageResource(ToolClass.getimg_weather(forecast.get_day().getWeather()));
        for(int i=0;i<4;i++) {
            textView = (TextView) v.findViewById(weather[i]);//天气
            textView.setText(forecast.getWeather()[i].getWeather());
            textView = (TextView) v.findViewById(temp[i]);//温度
            textView.setText(forecast.getWeather()[i].getTemp());
           /* textView = (TextView) v.findViewById(R.id.txt_temp_now);
            textView.setText(forecast.get_day().getTemp());
            textView = (TextView) v.findViewById(R.id.txt_temp_now);
            textView.setText(forecast.get_day().getTemp());
            textView = (TextView) v.findViewById(R.id.txt_temp_now);
            textView.setText(forecast.get_day().getTemp());
            */
            image= (ImageView) findById(img[i]);
            image.setImageResource(ToolClass.getimg_weather(forecast.getWeather()[i].getTitle_1()));
        }


    }


    public Object findById(int id)
    {

        return v.findViewById(id);
    }
}
