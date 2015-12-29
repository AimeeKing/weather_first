package com.example.aimee.weather;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aimee.json.Forecast;
import com.example.aimee.tony.ToolClass;
import com.example.aimee.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//import org.apache.http.util.EncodingUtils;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,AdapterView.OnItemSelectedListener,SwipeRefreshLayout.OnRefreshListener{
    private Handler handler;
    private Handler handler1;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView navigationview;
    private SharedPreferences sharedPreferences;
    private CityDB db;
    private SQLiteDatabase dbwrite;
    private final String NAME="City";
    private final int VERSION=1;
    private ContentValues []cv;
    private TextView text;
    private SimpleCursorAdapter scadapter;
    private ListView list;
    private Cursor cr;
    private SQLiteDatabase dbread;
    private YourObjectSpinnerAdapter spinnerAdapter;
    private SearchView searchView;
    private  MenuItem searchItem;
    //private ArrayList<weather_fragment> fragments;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;


    //share viewpage
    private Button btn_weibo;
    private Button btn_weixin;
    private Button btn_msg;
    private Button btn_email;
    private Button btn_blt;
    private ImageButton btn_show;
    private TextView moreinfo;
    private TextView txt_city;
    private TextView txt_temp_now;
    private TextView txt_temp;
    private TextView txt_weather;
    private ImageView img_weather;
    private Spinner spinner;
    private String city_now;
    /*和fragment有关*/
    private int pos;
    private weather_fragment weatherFragment;
    private static HashMap<String,String> name_code = new HashMap<>();

/* 下拉刷新*/
private SwipeRefreshLayout swipeRefreshLayout;
    private Handler mHandler;
    //private Spinner spinner;
  //  private ArrayAdapter<String> adapter;
   // String [] datas;

    //声明一个AlertDialog构造器
    private AlertDialog.Builder builder;

    //声明一个天气arraylist
    //private ArrayList<Forecast> forelist;
    private Gson gson;
    //和第一次有关
    private int onClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //在最开始的时候就要知道是不是第一次了
         gson = new Gson();
        handler=new Handler();
        handler1=new Handler();
        sharedPreferences=getSharedPreferences("Firsttime",MODE_PRIVATE);
        Boolean isfirst=sharedPreferences.getBoolean("isfirst",true);
        db=new CityDB(this,NAME,null,VERSION);
        pos=-1;
        if(isfirst)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Boolean istrue=write(getFromAssert("cityID.txt"));
                            if(istrue)
                                Toast.makeText(getBaseContext(),"DB导入成功",Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getBaseContext(),"DB导入失败",Toast.LENGTH_LONG).show();
                            sharedPreferences=getSharedPreferences("Firsttime",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putBoolean("isfirst",false).commit();
                           // init("杭州");
                            inits("杭州");
                        }
                    });
                }
            }).start();
        }
        else
        {
            //如果不是第一次开启

           // init("杭州");
            pos = 0;
          //  forelist = new ArrayList<Forecast>();
           // fragments = new ArrayList<>();
            spinnerAdapter = new YourObjectSpinnerAdapter();
           // init();
            inits_again();
        }



        //share view page

               mHandler = new Handler();



    /*
    在新的框架和支持包下，我们可以实现以下功能：首先使用Toolbar来代替ActionBar，
    这样我们就能够把ActionBar嵌入到我们的View体系中，然后我们"禁用"系统的status bar，
    由DrawerLayout来处理status bar，最后抽屉部分往上移，或者裁剪掉status bar那一部分。
     */


        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                                               @Override
                                               public boolean onMenuItemClick(MenuItem item) {
                                                   return false;
                                               }
                                           }
        );

        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /*spinner的设置*/
        View SpinnerContainer= LayoutInflater.from(this).inflate(R.layout.toolbar_spinner,toolbar,false);
        /*
        *Parameters
        parser	XML dom node containing the description of the view hierarchy.
        root	Optional view to be the parent of the generated hierarchy (if attachToRoot is true), or else simply an object that provides a set of LayoutParams values for root of the returned hierarchy (if attachToRoot is false.)
        attachToRoot	Whether the inflated hierarchy should be attached to the root parameter? If false, root is only used to create the correct subclass of LayoutParams for the root view in the XML.
         */

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.addView(SpinnerContainer, lp);


        spinner = (Spinner) SpinnerContainer.findViewById(R.id.tool_barspinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        if(spinnerAdapter!=null)
            setSpinnerItemSelectedByValue(spinner, spinnerAdapter.mItems.get(pos));

        /*drawlayout 的设置*/
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mActionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mActionBarDrawerToggle);
        navigationview=(NavigationView)findViewById(R.id.navigation_view);
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {//设置左边导航栏内容
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.city:
                        Toast.makeText(getApplicationContext(), "city Selected", Toast.LENGTH_SHORT).show();
//                        content_fragment fragment = new content_fragment();
//                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.frame,fragment);
//                        fragmentTransaction.commit();
                        Intent intent = new Intent(MainActivity.this,City_modify.class) ;
                        //可以把要传递的参数放到一个bundle里传递过去，bumdle可以看做一个特殊的map。
                        Bundle bundle = new Bundle() ;
                        bundle.putString("result", "第一个activity的内容") ;
                      //  bundle.putString("content",content) ;
                        //bundle_path.putSerializable("DATA", new String[]{Path,Path1,Path2});
                        String[] array=new String[spinnerAdapter.mItems.size()];
                        for(int i=0;i<spinnerAdapter.mItems.size();i++)
                            array[i]=spinnerAdapter.mItems.get(i);
                        bundle.putSerializable("DATA", array) ;
                        intent.putExtras(bundle) ;
                        startActivityForResult(intent, 1) ;
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.weather:
                      //  Toast.makeText(getApplicationContext(), "weather Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.customization:
                       // Toast.makeText(getApplicationContext(), "定制 Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.settings:
                        Intent intent2=new Intent();
                        intent2.setClass(MainActivity.this,setting_activity.class);
                        startActivity(intent2);
                    //    Toast.makeText(getApplicationContext(), "设置 Selected", Toast.LENGTH_SHORT).show();
                        //跳转到setting 活动，前提是要保存里面的内容在onStop里面,onResume里面再取出来

                        return true;
                    case R.id.money:
                        Intent intent1=new Intent();
                        intent1.setClass(MainActivity.this,Money.class);
                        startActivity(intent1);
                     //   Toast.makeText(getApplicationContext(), "赞助 Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.share:

                        showCustomViewDialog();
                       // Toast.makeText(getApplicationContext(), "分享 Selected", Toast.LENGTH_SHORT).show();

                        return true;
                    default:
                      //  Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        init_pulltorefresh();
      //  handleIntent(getIntent());
    }

    /*下拉刷新*/
    public void init_pulltorefresh()
    {
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,//setColorSchemeResources
                android.R.color.holo_blue_dark
                );
        swipeRefreshLayout.setProgressBackgroundColor(android.R.color.white);
        swipeRefreshLayout.setOnRefreshListener(this);
    }
    /*notification*/
    public void init_notification()
    {
        FragmentManager fm=getSupportFragmentManager();
        weather_fragment v=(weather_fragment)fm.findFragmentByTag("frame");
        Forecast forecast=v.forecast;

        int icon=R.drawable.ic_alpha;
        CharSequence tickerText="tips";
        long when=System.currentTimeMillis()+2000;


        RemoteViews contentView=new RemoteViews(getPackageName(),R.layout.notification);
        contentView.setImageViewResource(R.id.img_notify_wea, ToolClass.getimg_weather(forecast.getWeather()[0].getTitle_1()));
        contentView.setTextViewText(R.id.noti_temp_now, String.valueOf(forecast.get_day().getTemp()) + "°");
        contentView.setTextViewText(R.id.noti_temp_max, forecast.get_day().getTempMax()+"°");
        contentView.setTextViewText(R.id.noti_temp_min, forecast.get_day().getTempMin()+"°");
        contentView.setTextViewText(R.id.noti_weather_now,forecast.get_day().getWeather());
        contentView.setTextViewText(R.id.noti_alert, "");
        contentView.setTextViewText(R.id.noti_where, forecast.get_city());
        contentView.setTextViewText(R.id.noti_time, "更新于:" + forecast.get_day().getTime());


        Intent intent=new Intent(MainActivity.this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mbuilder=new NotificationCompat.Builder(this)
                .setSmallIcon(ToolClass.getimg_weather(forecast.getWeather()[0].getTitle_1()))
                .setSubText(tickerText)
                .setContentTitle(tickerText)
                .setContent(contentView)
                .setWhen(when)
                .setContentIntent(pendingIntent);//.setNumber(forecast.get_day().getTemp())
//        Intent resultIntent=new Intent(this,MainActivity.class);
//        TaskStackBuilder
        NotificationManager mNotificationManager=
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mbuilder.build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case 1:
                String recvData[] = data.getStringArrayExtra("DATARETURN");
                if(recvData!=null&&recvData.length>0) {
                    spinnerAdapter.clear();
                    spinnerAdapter.addItems(new ArrayList<String>(Arrays.asList(recvData)));
                    spinnerAdapter.notifyDataSetChanged();
                }
                else {
                    spinnerAdapter.clear();
                    spinnerAdapter.addItem("杭州");
                    setSpinnerItemSelectedByValue(spinner,"杭州");
                    update("杭州");
                }
                break;

        }
    }

    /*
    *分享界面函数
    * 下面多个click是不同的分享按钮
    * showCustViewDialog是设置界面
    *
     */


    public void weibo_click(View view){
        Toast.makeText(MainActivity.this,"weibo",Toast.LENGTH_SHORT).show();
    }

    public void weixin_click(View view){
        Toast.makeText(MainActivity.this,"weixin",Toast.LENGTH_SHORT).show();
    }

    public void msg_click(View view){
        Toast.makeText(MainActivity.this,"message",Toast.LENGTH_SHORT).show();
    }

    public void email_click(View view){
        Toast.makeText(MainActivity.this,"email",Toast.LENGTH_SHORT).show();
    }

    public void blt_click(View view){
        Toast.makeText(MainActivity.this,"bluetooth",Toast.LENGTH_SHORT).show();
    }
    private void showCustomViewDialog(){
        builder=new AlertDialog.Builder(this);

        //builder.setTitle("分享");

        /**
         * 设置内容区域为自定义View
         */
        LinearLayout loginDialog= (LinearLayout) getLayoutInflater().inflate(R.layout.custom_view, null);

        txt_city = (TextView) loginDialog.findViewById(R.id.txt_dialog_city);
        txt_temp_now = (TextView) loginDialog.findViewById(R.id.txt_dialog_tempnow);
        txt_temp = (TextView) loginDialog.findViewById(R.id.txt_dialog_temp);
        txt_weather = (TextView) loginDialog.findViewById(R.id.txt_dialog_weather);
        img_weather = (ImageView) loginDialog.findViewById(R.id.img_dialog_weather);

        Thread mThread = new Thread(backwork);
        mThread.start();



        builder.setView(loginDialog);
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();

        dialog.show();
    }
    private Runnable backwork = new Runnable() {
        @Override
        public void run() {
            updatadialog();
        }
    };
    private void updatadialog(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Forecast f = ToolClass.getSharedForcast(city_now);
                txt_city.setText(f.get_city());
                txt_temp.setText(String.valueOf(f.get_day().getTempMin()) + "℃/" + String.valueOf(f.get_day().getTempMax()) + "℃");
                txt_temp_now.setText(String.valueOf(f.get_day().getTemp()) + "℃");
                txt_weather.setText(f.get_day().getWeather());
                img_weather.setImageResource(ToolClass.getimg_weather(f.get_day().getWeather()));
                System.out.println("Done");
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem=menu.findItem(R.id.add);
//        SearchManager searchManager=(SearchManager)MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        searchView=null;
        if(searchItem!=null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if(searchView!=null)
        {
            //searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(MainActivity.this);
            

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public boolean write(String str) {

        dbwrite = db.getWritableDatabase();
        cv = new ContentValues[2587];
        int n = 0;
        String id = "";
        String city = "";
        char c;
        int a=0;
        cv[a]=new ContentValues();
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (n == 0 && c != '=')
                id += c;
            else if (n == 1 && c != '=' && c != '\n' && c != '\r') {
                city += c;
            } else if (n == 0 && c == '=') {
                n = 1;
                continue;
            } else if (c == '\n') {
                cv[a].put("cityname", city);
                cv[a].put("cityid", id);
                //  dbwrite.insert("CITYID", null, cv[a]);
//                System.out.println(String.format("cityname=%s,cityid=%s\n", city, id));
//                gettext+= String.format("cityname=%s,cityid=%s \n", city, id);
                cv[++a]=new ContentValues();
                id="";
                city="";
                n=0;
            }

        }
        dbwrite.beginTransaction();
        try {
            for (int i = 0; i < a; i++) {
                if (dbwrite.insert("CITYID", null, cv[i]) < 0) {
                    return  false;
                }
            }

            dbwrite.setTransactionSuccessful();
        }finally {
            dbwrite.endTransaction();
            dbwrite.close();
        }
        return true;
    }

    public String getFromAssert(String filename)
    {
        String str="";
        try{
            InputStream in=getResources().getAssets().open(filename);
            int num=in.available();
            byte[] buffer=new byte[num];
            in.read(buffer);
            str= EncodingUtils.getString(buffer, "utf-8");


        }catch(Exception ex)
        {
          Log.i("error",ex.toString());

        }
        return str;
    }
/*searchview 的监听器*/
    @Override
    public boolean onQueryTextSubmit(final String query) {//搜索城市id
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        //addfrag(query);
                        /*_____________________________________________________________________________________________--*/
                        update(query);
                        setSpinnerItemSelectedByValue(spinner,query);
                    }
                });
            }
        }).start();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
    /*spinner 点击的监听器*/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String str=(String)parent.getItemAtPosition(position);
         // addfrag(str);
        /*--------------------------------------------------------------------------------------*/
        update(str);
        //init_notification();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {//postDelayed
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                FragmentManager fm=getSupportFragmentManager();
                weather_fragment v=(weather_fragment)fm.findFragmentByTag("frame");
                TextView txt_temp= (TextView) v.getView().findViewById(R.id.txt_temp_now);
                TextView txt_21=(TextView)findViewById(R.id.textView21);//℃
                ImageView img_today = (ImageView) v.getView().findViewById(R.id.img_weather_today);
                TextView textView9 = (TextView)v.getView().findViewById(R.id.textView9);//PM
                TextView txt_pm25 = (TextView) v.getView().findViewById(R.id.txt_pm2_5);
                TextView txt_wind_dir = (TextView) v.getView().findViewById(R.id.txt_wind_dir);//东风
                TextView txt_wind_grd = (TextView) v.getView().findViewById(R.id.txt_wind_grd);//3级
                TableLayout tablelayout=(TableLayout)v.getView().findViewById(R.id.tablelayout);//tablelayout
                TextView txt_moreimfo=(TextView)findViewById(R.id.moreimfo);
                ImageView img_hide = (ImageView) v.getView().findViewById(R.id.hide_show);
                TextView txt_updatetime=(TextView)v.getView().findViewById(R.id.txt_updatatime);

                AnimationSet an=new AnimationSet(true);
                an.setDuration(1000);
                TranslateAnimation ta=new TranslateAnimation(-100,0,0,0);
                ta.setDuration(1000);
                AlphaAnimation aa=new AlphaAnimation(0,1);
                aa.setDuration(1000);
                an.addAnimation(ta);
                an.addAnimation(aa);
                txt_updatetime.startAnimation(an);
                txt_temp.startAnimation(an);
                txt_21.startAnimation(an);

                an=new AnimationSet(true);
                an.setDuration(1100);
                ta=new TranslateAnimation(-110,0,0,0);
                ta.setDuration(1100);
                aa=new AlphaAnimation(0,1);
                aa.setDuration(1100);
                an.addAnimation(ta);
                an.addAnimation(aa);
                img_today.startAnimation(an);

                an=new AnimationSet(true);
                an.setDuration(1200);
                ta=new TranslateAnimation(-120,0,0,0);
                ta.setDuration(1200);
                aa=new AlphaAnimation(0,1);
                aa.setDuration(1200);
                an.addAnimation(ta);
                an.addAnimation(aa);
                textView9.startAnimation(an);
                txt_pm25.startAnimation(an);

                an=new AnimationSet(true);
                an.setDuration(1300);
                ta=new TranslateAnimation(-130,0,0,0);
                ta.setDuration(1300);
                aa=new AlphaAnimation(0,1);
                aa.setDuration(1300);
                an.addAnimation(ta);
                an.addAnimation(aa);
                txt_wind_dir.startAnimation(an);
                txt_wind_grd.startAnimation(an);
                if(txt_moreimfo.getVisibility()==View.VISIBLE)
                txt_moreimfo.startAnimation(an);
                img_hide.startAnimation(an);

                an=new AnimationSet(true);
                an.setDuration(1400);
                ta=new TranslateAnimation(-140,0,0,0);
                ta.setDuration(1400);
                aa=new AlphaAnimation(0,1);
                aa.setDuration(1400);
                an.addAnimation(ta);
                an.addAnimation(aa);
                tablelayout.startAnimation(an);
            }
        }, 2000);
        init_notification();
    }


    private class YourObjectSpinnerAdapter extends BaseAdapter {
        private List<String> mItems = new ArrayList<>();

        public void clear() {
            mItems.clear();
        }
        public int is_in(String yourObject)
        {
            for(int i=0;i<mItems.size();i++)
            {
                if(mItems.get(i).equals(yourObject))
                    return i;
            }
            return -1;
        }
        public int addItem(String yourObject) {
            int i=is_in(yourObject);
            if(i==-1) {
                mItems.add(yourObject);
                return -1;
            }
            return i;
        }

        public void addItems(List<String> yourObjectList) {
            for(int i=0;i<yourObjectList.size();i++)
             {
                 String str=yourObjectList.get(i);
                 addItem(str);
             }

            //mItems.addAll(yourObjectList);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
                view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
                view.setTag("DROPDOWN");
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));

            return view;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
                view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_action, parent, false);
                view.setTag("NON_DROPDOWN");
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));
            return view;
        }

        private String getTitle(int position) {
            return position >= 0 && position < mItems.size() ? mItems.get(position) : "";
        }
    }

    @Override
    protected void onStop() {
        //if(pos>0)
        save();
        super.onStop();
    }

//    @Override
//    protected void onResume() {
//
//        super.onResume();
//        init_pulltorefresh();
//    }

    /*离开之前存储信息*/
    public void save()
    {
        String jsonStr = gson.toJson(spinnerAdapter.mItems);
        SPUtils.put(MainActivity.this, "citylist", jsonStr);
        SPUtils.put(MainActivity.this, "pos", pos);
    }







    private void inits(String cityname)//初创
    {
        //初始化
        spinnerAdapter = new YourObjectSpinnerAdapter();
        if(spinner!=null)
            if(spinner.getAdapter()==null)
                spinner.setAdapter(spinnerAdapter);
        weatherFragment=new weather_fragment(cityname,addforecast(cityname));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame,weatherFragment,"frame").commit();
        pos=0;
    }

    private void inits_again()//初创
    {
        String str = (String) SPUtils.get(MainActivity.this, "citylist", "");
        ArrayList<String> array = gson.fromJson(str, new TypeToken<List<String>>() {
        }.getType());
        if (array != null) {
            pos=(int)SPUtils.get(MainActivity.this,"pos",0);
            spinnerAdapter=new YourObjectSpinnerAdapter();
            spinnerAdapter.addItems(array);
            weatherFragment=new weather_fragment(array.get(pos),addforecast(array.get(pos)));
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame,weatherFragment,"frame").commit();
        }
        else {

        }
    }

    private void update(String cityname)//当后来点击或者搜索的时候用
    {
        Forecast forecast=addforecast(cityname);
        weatherFragment.setForecast(forecast);
    }


    private Forecast addforecast(String cityname)
    {
        Forecast forecast = null;
        if(!(name_code.containsKey(cityname))) {
            dbread = db.getReadableDatabase();
            cr = dbread.query("CITYID", null, "cityname=?", new String[]{cityname}, null, null, null);
            if (cr.getCount() < 1)
                Toast.makeText(getBaseContext(), "you search" + cityname + " can not find"
                        , Toast.LENGTH_LONG).show();
            else
                while (cr.moveToNext()) {
                    String ct_id = cr.getString(cr.getColumnIndex("cityid"));
//                    Toast.makeText(getBaseContext(),"you search"+cityname+" and it id is "+ct_id
//                            ,Toast.LENGTH_LONG).show();
                    spinnerAdapter.addItem(cityname);
                    spinnerAdapter.notifyDataSetChanged();
                    //仅当这个未被查询时，才会增加界面和相应内容
                    city_now = cityname;
                    forecast = new Forecast(cityname, ct_id);
                    //    return forecast;
                    name_code.put(cityname,ct_id);
                    pos=spinnerAdapter.is_in(cityname);
                }
        }
        else
        {
            pos=spinnerAdapter.addItem(cityname);
            forecast=new Forecast(cityname,name_code.get(cityname));
        }
        return forecast;

    }
}
