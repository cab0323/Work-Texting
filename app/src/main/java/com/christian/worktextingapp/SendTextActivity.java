package com.christian.worktextingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class SendTextActivity extends Activity {

    private TextView chosenText;
    private Button firstButton;
    private Button secondButton;
    private Button thirdButton;
    private Button fourthButton;
    private boolean promptSelected = false;
    private boolean checkBoxClicked = false; //will only change if the user clicks the box
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

        //create the constraint set and clone the constraint layout
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(myConstraint);

        //call the class that will add things to the constraint set
        addToLayout(constraintSet, myConstraint);

        //apply to the constraint layout
        constraintSet.applyTo(myConstraint);
        //return the layout
        return myConstraint;
    }

    private void addToLayout(ConstraintSet constraintSet, ConstraintLayout myConstraint){
        //i may add a guideline here later
        //add the heading
        TextView heading = new TextView(this);
        heading.setId(View.generateViewId());
        heading.setText("Choose the text message prompt");
        heading.setTextColor(ContextCompat.getColor(this, R.color.black));
        heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        myConstraint.addView(heading);

        constraintSet.constrainHeight(heading.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(heading.getId(), ConstraintSet.MATCH_CONSTRAINT);

        //the strings that will go in the buttons
        String firstButtonText = "Hello i am testing the firstButton button with a long text let's see if it works";
        String secondButtonText = "Hello i am testing the secondButton button with a long text too.";
        String thirdButtonText = "This is the third button";
        String fourthButtonText = "This is the fourth button";

        //the buttons that will have the prompts
        //create the buttons
        firstButton = new Button(this);
        secondButton = new Button(this);
        thirdButton = new Button(this);
        fourthButton = new Button(this);

        //set the ID's of the buttons
        firstButton.setId(View.generateViewId());
        secondButton.setId(View.generateViewId());
        thirdButton.setId(View.generateViewId());
        fourthButton.setId(View.generateViewId());

        //set the button texts
        firstButton.setText(firstButtonText);
        secondButton.setText(secondButtonText);
        thirdButton.setText(thirdButtonText);
        fourthButton.setText(fourthButtonText);

        //add the onclick listeners
        firstButton.setOnClickListener(this::textPromptClick);
        secondButton.setOnClickListener(this::textPromptClick);

        //add the buttons to the layout
        myConstraint.addView(firstButton);
        myConstraint.addView(secondButton);
        myConstraint.addView(thirdButton);
        myConstraint.addView(fourthButton);

        //the button that will send the text
        Button sendText = new Button(this);
        sendText.setId(View.generateViewId());
        sendText.setText("Send Text");
        sendText.setOnClickListener(this::sendText);
        myConstraint.addView(sendText);

        constraintSet.constrainHeight(sendText.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(sendText.getId(), ConstraintSet.WRAP_CONTENT);

        //add the checkbox that needs to be clicked before the text will be sent
        CheckBox verifyCheckbox = new CheckBox(this);
        verifyCheckbox.setId(View.generateViewId());
        verifyCheckbox.setText("Check here before clicking button");
        verifyCheckbox.setTextColor(ContextCompat.getColor(this, R.color.black));
        verifyCheckbox.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        verifyCheckbox.setOnClickListener(this::checkBoxListener);
        myConstraint.addView(verifyCheckbox);

        constraintSet.constrainWidth(verifyCheckbox.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(verifyCheckbox.getId(), ConstraintSet.WRAP_CONTENT);


        //here goes the textview that will show the user what they chose
        chosenText = new TextView(this);
        chosenText.setId(View.generateViewId());
        chosenText.setTextColor(ContextCompat.getColor(this, R.color.black));
        myConstraint.addView(chosenText);

        constraintSet.constrainHeight(chosenText.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainWidth(chosenText.getId(), ConstraintSet.WRAP_CONTENT);

        //constraint the heading
        constraintSet.connect(heading.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(heading.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(heading.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(heading.getId(), ConstraintSet.BOTTOM, firstButton.getId(), ConstraintSet.TOP);

        //constraint the firstButton button
        constraintSet.connect(firstButton.getId(), ConstraintSet.TOP, heading.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(firstButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(firstButton.getId(), ConstraintSet.END, secondButton.getId(), ConstraintSet.START);
        constraintSet.connect(firstButton.getId(), ConstraintSet.BOTTOM, thirdButton.getId(), ConstraintSet.TOP);

        //constraint the secondButton button
        constraintSet.connect(secondButton.getId(), ConstraintSet.TOP, heading.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(secondButton.getId(), ConstraintSet.START, firstButton.getId(), ConstraintSet.END);
        constraintSet.connect(secondButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(secondButton.getId(), ConstraintSet.BOTTOM, fourthButton.getId(), ConstraintSet.TOP);

        //constraint the third button
        constraintSet.connect(thirdButton.getId(), ConstraintSet.TOP, firstButton.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(thirdButton.getId(), ConstraintSet.START, firstButton.getId(), ConstraintSet.START);
        constraintSet.connect(thirdButton.getId(), ConstraintSet.END, fourthButton.getId(), ConstraintSet.START);
        constraintSet.connect(thirdButton.getId(), ConstraintSet.BOTTOM, chosenText.getId(), ConstraintSet.TOP);

        //constraint the fourth button
        constraintSet.connect(fourthButton.getId(), ConstraintSet.TOP, secondButton.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(fourthButton.getId(), ConstraintSet.START, thirdButton.getId(), ConstraintSet.END);
        constraintSet.connect(fourthButton.getId(), ConstraintSet.END, secondButton.getId(), ConstraintSet.END);
        constraintSet.connect(fourthButton.getId(), ConstraintSet.BOTTOM, thirdButton.getId(), ConstraintSet.BOTTOM);

        //constraint the chosen text
        constraintSet.connect(chosenText.getId(), ConstraintSet.TOP, thirdButton.getId(), ConstraintSet.BOTTOM, 50);
        constraintSet.connect(chosenText.getId(), ConstraintSet.START, thirdButton.getId(), ConstraintSet.START, 50);
        constraintSet.connect(chosenText.getId(), ConstraintSet.END, fourthButton.getId(), ConstraintSet.END);
        constraintSet.connect(chosenText.getId(), ConstraintSet.BOTTOM, sendText.getId(), ConstraintSet.TOP);

        //the send text button
        constraintSet.connect(sendText.getId(), ConstraintSet.TOP, chosenText.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(sendText.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(sendText.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(sendText.getId(), ConstraintSet.BOTTOM, verifyCheckbox.getId(), ConstraintSet.TOP, 20);

        //the verify checkbox
        constraintSet.connect(verifyCheckbox.getId(), ConstraintSet.TOP, sendText.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(verifyCheckbox.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(verifyCheckbox.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(verifyCheckbox.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 50);


    }

    /*
    This method will check which prompt was clicked and show the user that prompt was clicked by setting the textview to
    that prompt.
     */
    private void textPromptClick(View view){
        //depending on what button was clicked set the textview to that button's text

        //once a selection is made the selections cannot be unmade, one promtp has to be selected. Last
        //selected prompt is the one sent. Once send button is clicked.
        promptSelected = true;

        //check which one was selected
        if(view.getId() == firstButton.getId()){
            chosenText.setText(firstButton.getText());
            Log.d("TESTING", "textPromptClick: FIRST CLICKED");
        } else if (view.getId() == secondButton.getId()) {
            chosenText.setText("SECOND");
            Log.d("TESTING", "textPromptClick: SECOND CLICKED");
        }
    }

    /*
    This is the onClickListener for the send text button. When clicked it will send the text based on the prompt that was clicked.
    If no prompt is clicked it will let the user know to make a selection.
     */
    private void sendText(View view){
        //first check if the user did make a selection and checked the checkbox
        if(promptSelected && checkBoxClicked){
            Log.d("TESTING", "sendText: sending text");
        }
        else {
            //check what is missing,
            if (!promptSelected) {
                //no prompt was selected
                chosenText.setText("MAKE A SELECTION BEFORE SENDING");
            }
        }

    }

    //the click listener for the checkbox
    private void checkBoxListener(View view){
        //flip to the opposite, to make sure if the user clicks and then un clicks it does not stay true
        checkBoxClicked = !checkBoxClicked;
    }

}
