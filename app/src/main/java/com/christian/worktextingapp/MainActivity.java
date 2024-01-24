package com.christian.worktextingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

/*
    extend from Activity instead of AppCompatActivity when you don't want an ActionBar. This is because
    AppCompatActivity wants an ActionBar.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutClass myLayout = new LayoutClass(this);
        setContentView(myLayout);
    }
}