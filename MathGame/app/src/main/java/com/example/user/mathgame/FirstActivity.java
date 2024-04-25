package com.example.user.mathgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;

public class FirstActivity extends AppCompatActivity {

    private static final String TAG = FirstActivity.class.getSimpleName();

    HashMap<String,String> m1 = new HashMap<String,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m1.put("key1", "10086value1");
        m1.put("key2", "value2");
        m1.put("key3", "value3");
        String a = m1.get("key1");
        Log.e(TAG, "onGetServerMessage..."+a);
    }
}
