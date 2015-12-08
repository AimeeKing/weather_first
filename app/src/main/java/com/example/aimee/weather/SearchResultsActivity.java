package com.example.aimee.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Aimee on 2015/11/25.
 */
public class SearchResultsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

   private void handleIntent(Intent intent)
   {
       if(Intent.ACTION_SEARCH.equals((intent.getAction())))
       {
           String query=intent.getStringExtra()
       }
   }
}
