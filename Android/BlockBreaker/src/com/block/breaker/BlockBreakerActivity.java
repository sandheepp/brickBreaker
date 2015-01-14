package com.block.breaker;

import android.app.Activity;
import android.os.Bundle;

public class BlockBreakerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    @Override
    public void onBackPressed()
    {
    	super.onBackPressed();
    	finish();
    }
}