package com.example.aimee.weather;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.aimee.UI.SwipeDismissListViewTouchListener;

import java.util.ArrayList;
import java.util.Arrays;

public class City_modify extends ListActivity {
    ArrayAdapter<String> mAdapter;
    private ImageButton imgbut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_modify);
        imgbut= (ImageButton) findViewById(R.id.backbutton);
        imgbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(City_modify.this, MainActivity.class);
                Bundle bundle = new Bundle();
                String[] array = new String[mAdapter.getCount()];
                for (int i = 0; i < mAdapter.getCount(); i++)
                    array[i] = mAdapter.getItem(i);
                // 设置结果，并进行传送
                bundle.putSerializable("DATARETURN", array);
                intent.putExtras(bundle);
                City_modify.this.setResult(10, intent);
                City_modify.this.finish();
            }
        });

        Intent intent=getIntent();
        String recvData[] = intent.getStringArrayExtra("DATA");
        //(List<YourObject>) getIntent().getSerializable(key)
//        ArrayList<String> array=(ArrayList<String>) savedInstanceState.getSerializable("DATA");

        // Set up ListView example
        String[] items = new String[20];
        for (int i = 0; i < items.length; i++) {
            items[i] = "Item " + (i + 1);
        }

        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new ArrayList<String>(Arrays.asList(recvData)));
        setListAdapter(mAdapter);

        ListView listView = getListView();
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mAdapter.remove(mAdapter.getItem(position));
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());

        // Set up normal ViewGroup example





    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //按下返回按钮的处理
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(City_modify.this, MainActivity.class);
            Bundle bundle = new Bundle();
            String[] array = new String[mAdapter.getCount()];
            for (int i = 0; i < mAdapter.getCount(); i++)
                array[i] = mAdapter.getItem(i);
            // 设置结果，并进行传送
            bundle.putSerializable("DATARETURN", array);
            intent.putExtras(bundle);
            this.setResult(10, intent);
            this.finish();
        }
            return false;


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_city_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
