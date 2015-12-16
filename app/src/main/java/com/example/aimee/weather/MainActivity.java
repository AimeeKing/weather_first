package com.example.aimee.weather;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
//import org.apache.http.util.EncodingUtils;


import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,AdapterView.OnItemSelectedListener {
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
    private ArrayList<weather_fragment> fragments;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;


    //share viewpage
    private Button btn_weibo;
    private Button btn_weixin;
    private Button btn_msg;
    private Button btn_email;
    private Button btn_blt;
    private TextView txt_city;
    private TextView txt_temp_now;
    private TextView txt_temp;
    private TextView txt_weather;

    private Handler mHandler;
    //  private Spinner spinner;
  //  private ArrayAdapter<String> adapter;
   // String [] datas;

    //声明一个AlertDialog构造器
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragments= new ArrayList<>();
        weather_fragment fragment = new weather_fragment("杭州");
        fragments.add(fragment);
         fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
        /*DB的设置*/
        db=new CityDB(this,NAME,null,VERSION);

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
        spinnerAdapter = new YourObjectSpinnerAdapter();
        spinnerAdapter.addItem("杭州");

        Spinner spinner = (Spinner) SpinnerContainer.findViewById(R.id.tool_barspinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

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
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.city:
                        Toast.makeText(getApplicationContext(),"city Selected",Toast.LENGTH_SHORT).show();
                        content_fragment fragment = new content_fragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,fragment);
                        fragmentTransaction.commit();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.weather:
                        Toast.makeText(getApplicationContext(),"weather Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.customization:
                        Toast.makeText(getApplicationContext(),"定制 Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(),"设置 Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.money:
                        Toast.makeText(getApplicationContext(),"赞助 Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.share:

                        showCustomViewDialog();
                        Toast.makeText(getApplicationContext(), "分享 Selected", Toast.LENGTH_SHORT).show();

                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        handler=new Handler();
        handler1=new Handler();
        sharedPreferences=getSharedPreferences("Firsttime",MODE_PRIVATE);
        Boolean isfirst=sharedPreferences.getBoolean("isfirst",true);
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
                        }
                    });
                }
            }).start();
        }
      //  handleIntent(getIntent());
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
                txt_city.setText("江干区");
                txt_temp.setText("4℃/10℃");
                txt_temp_now.setText("5℃");
                txt_weather.setText("阴天");
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

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals((intent.getAction())))
        {
            String query=intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getBaseContext(),"you search"+query
                    ,Toast.LENGTH_LONG).show();
        }
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
    public boolean onQueryTextSubmit(final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        String ct_id;
                        if(dbread==null)
                            dbread=db.getReadableDatabase();
                        cr=dbread.query("CITYID",null,"cityname=?",new String[]{query},null,null,null);
                        if(cr.getCount()<1)
                            Toast.makeText(getBaseContext(),"you search"+query+" can not find"
                                    ,Toast.LENGTH_LONG).show();
                        else
                        while (cr.moveToNext()) {
                            ct_id=cr.getString(cr.getColumnIndex("cityid"));
                            Toast.makeText(getBaseContext(),"you search"+query+" and it id is "+ct_id
                                    ,Toast.LENGTH_LONG).show();
                            spinnerAdapter.addItem(query);
                            spinnerAdapter.notifyDataSetChanged();
                        /*
                        *   是不是这里新建一个layout，然后在要用的时候再放进去
                        *
                        */
                            weather_fragment w=new weather_fragment(query);
                            fragments.add(w);
                        }

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
        Toast.makeText(getBaseContext(),"have Click "+str,Toast.LENGTH_LONG).show();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragments.get(position));
        fragmentTransaction.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    private class YourObjectSpinnerAdapter extends BaseAdapter {
        private List<String> mItems = new ArrayList<>();

        public void clear() {
            mItems.clear();
        }

        public void addItem(String yourObject) {
            mItems.add(yourObject);
        }

        public void addItems(List<String> yourObjectList) {
            mItems.addAll(yourObjectList);
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

}
