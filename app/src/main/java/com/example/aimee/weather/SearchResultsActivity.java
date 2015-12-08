package com.example.aimee.weather;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Aimee on 2015/11/25.
 */
public class SearchResultsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(getIntent());
    }

   private void handleIntent(Intent intent)
   {
       if(Intent.ACTION_SEARCH.equals((intent.getAction())))
       {
           String query=intent.getStringExtra(SearchManager.QUERY);
           Toast.makeText(getBaseContext(),"you search"+query
               ,Toast.LENGTH_LONG).show();
       }
   }
}
