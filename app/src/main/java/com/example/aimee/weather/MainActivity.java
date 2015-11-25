package com.example.aimee.weather;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import org.apache.http.util.EncodingUtils;


import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private Handler handler;
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
    private SharedPreferences sh;
    private Cursor cr;
    private SQLiteDatabase dbread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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
                        Toast.makeText(getApplicationContext(),"分享 Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        handler=new Handler();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem=menu.findItem(R.id.add);
        SearchManager searchManager=(SearchManager)MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=null;
        if(searchItem!=null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if(searchView!=null)
        {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
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
            str= EncodingUtils.getString(buffer,"utf-8");


        }catch(Exception ex)
        {

        }
        return str;
    }
}
