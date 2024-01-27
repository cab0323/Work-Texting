package com.christian.worktextingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class SendTextActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(myLayout());


    }


    /*
    Testing if the layout will work. If creating the constraintLayout will work the same
    as if i created a new class that made the layout.
     */
    private ConstraintLayout myLayout(){
        ConstraintLayout myConstraint = new ConstraintLayout(this);

        //set the id for the constraintLayout
        myConstraint.setId(View.generateViewId());

        //create layout parameters that take up the whole screen
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //set the layout parameters of the constraint
        myConstraint.setLayoutParams(layoutParams);

        //set the background color of the constraint layout
        myConstraint.setBackgroundColor(ContextCompat.getColor(this, R.color.green_grass));

        //return the layout
        return myConstraint;
    }
}