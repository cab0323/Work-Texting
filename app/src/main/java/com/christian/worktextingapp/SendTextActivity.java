package com.christian.worktextingapp;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SendTextActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private TextView chosenText;
    private Button firstButton;
    private Button secondButton;
    private Button thirdButton;
    private Button fourthButton;

    //testing the "crate your own prompt" button
    private Button createYourOwnButton;
    private boolean promptSelected = false;
    private boolean checkBoxClicked = false; //will only change if the user clicks the box
    private ArrayList<Client> selectedClients;
    private static final int SEND_SMS_PERMISSION = 100;
    private int promptNumberSelected = 0; //used to keep track of which prompt was selected
    private final int NONE = 0;
    private final int  FIRST_BUTTON = 1;
    private final int SECOND_BUTTON = 2;
    private final int THIRD_BUTTON = 3;
    private final int FOURTH_BUTTON = 4;
    private int currentButtonClicked = NONE; //will tell me which button is currently clicked


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(myLayout());

        selectedClients = (ArrayList<Client>) getIntent().getSerializableExtra("selectedPeople");
/*        for(int i = 0; i < selectedClients.size(); i++){
            Log.d("TESTING", "onCreate: selectedClients: " + selectedClients.get(i).getClientName());
        }*/
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
        //add the back button at the top of the screen, in the middle
        Button backButton = new Button(this);
        backButton.setText(R.string.goBackButton);
        backButton.setId(View.generateViewId());
        backButton.setOnClickListener(this::goPreviousPage);
        constraintSet.constrainWidth(backButton.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(backButton.getId(), ConstraintSet.WRAP_CONTENT);
        myConstraint.addView(backButton);
        //add a home button beside the back button eventually

        //add the heading
        TextView heading = new TextView(this);
        heading.setId(View.generateViewId());
        heading.setText(R.string.Heading);
        heading.setTextColor(ContextCompat.getColor(this, R.color.black));
        heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        myConstraint.addView(heading);

        constraintSet.constrainHeight(heading.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(heading.getId(), ConstraintSet.MATCH_CONSTRAINT);

        //set the drawable that will be the default background for the buttons
        Drawable defaultbutton = ContextCompat.getDrawable(getApplicationContext(), R.drawable.prompt_button_default);

        //create the buttons that will have the prompts
        firstButton = new Button(this);
        secondButton = new Button(this);
        thirdButton = new Button(this);
        fourthButton = new Button(this);

        //this button will allow user to create their own prompt
        createYourOwnButton = new Button(this);

        //setting the drawable background of the buttons
        firstButton.setBackground(defaultbutton);
        secondButton.setBackground(defaultbutton);
        thirdButton.setBackground(defaultbutton);
        fourthButton.setBackground(defaultbutton);

        //testing
        createYourOwnButton.setBackground(defaultbutton);

        //set the ID's of the buttons
        firstButton.setId(View.generateViewId());
        secondButton.setId(View.generateViewId());
        thirdButton.setId(View.generateViewId());
        fourthButton.setId(View.generateViewId());

        //testing
        createYourOwnButton.setId(View.generateViewId());

        //set the button texts
        firstButton.setText(String.format(getResources().getString(R.string.firstButtonPrompt), getResources().getString(R.string.buttonPromptsPlaceholder)));
        secondButton.setText(String.format(getResources().getString(R.string.secondButtonPrompt), getResources().getString(R.string.buttonPromptsPlaceholder)));
        thirdButton.setText(String.format(getResources().getString(R.string.thirdButtonPrompt), getResources().getString(R.string.buttonPromptsPlaceholder)));
        fourthButton.setText(String.format(getResources().getString(R.string.fourthButtonPrompt), getResources().getString(R.string.buttonPromptsPlaceholder)));

        //testing
        createYourOwnButton.setText("Make your own prompt");

        //add the onclick listeners
        firstButton.setOnClickListener(this::textPromptClick);
        secondButton.setOnClickListener(this::textPromptClick);
        thirdButton.setOnClickListener(this::textPromptClick);
        fourthButton.setOnClickListener(this::textPromptClick);

        //testing
        createYourOwnButton.setOnClickListener(this::textPromptClick);

        //add the buttons to the layout
        myConstraint.addView(firstButton);
        myConstraint.addView(secondButton);
        myConstraint.addView(thirdButton);
        myConstraint.addView(fourthButton);

        //testing
        myConstraint.addView(createYourOwnButton);

        //the button that will send the text
        Button sendText = new Button(this);
        sendText.setId(View.generateViewId());
        sendText.setText(R.string.sendText);
        sendText.setOnClickListener(this::sendText);
        myConstraint.addView(sendText);

        constraintSet.constrainHeight(sendText.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(sendText.getId(), ConstraintSet.WRAP_CONTENT);

        //add the checkbox that needs to be clicked before the text will be sent
        CheckBox verifyCheckbox = new CheckBox(this);
        verifyCheckbox.setId(View.generateViewId());
        verifyCheckbox.setText(R.string.checkboxText);
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

        //constraint the back button
        constraintSet.connect(backButton.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(backButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, constraintSet.START);
        constraintSet.connect(backButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, constraintSet.END);
        constraintSet.connect(backButton.getId(), ConstraintSet.BOTTOM, heading.getId(), ConstraintSet.TOP);

        //constraint the heading
        constraintSet.connect(heading.getId(), ConstraintSet.TOP, backButton.getId(), ConstraintSet.BOTTOM, 20);
        constraintSet.connect(heading.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(heading.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(heading.getId(), ConstraintSet.BOTTOM, firstButton.getId(), ConstraintSet.TOP);

        //constraint the firstButton button
        constraintSet.connect(firstButton.getId(), ConstraintSet.TOP, heading.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(firstButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 30);
        constraintSet.connect(firstButton.getId(), ConstraintSet.END, secondButton.getId(), ConstraintSet.START, 30);
        constraintSet.connect(firstButton.getId(), ConstraintSet.BOTTOM, thirdButton.getId(), ConstraintSet.TOP);

        //constraint the secondButton button
        constraintSet.connect(secondButton.getId(), ConstraintSet.TOP, heading.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(secondButton.getId(), ConstraintSet.START, firstButton.getId(), ConstraintSet.END, 30);
        constraintSet.connect(secondButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(secondButton.getId(), ConstraintSet.BOTTOM, fourthButton.getId(), ConstraintSet.TOP);

        //constraint the third button
        constraintSet.connect(thirdButton.getId(), ConstraintSet.TOP, firstButton.getId(), ConstraintSet.BOTTOM, 30);
        constraintSet.connect(thirdButton.getId(), ConstraintSet.START, firstButton.getId(), ConstraintSet.START, 30);
        constraintSet.connect(thirdButton.getId(), ConstraintSet.END, fourthButton.getId(), ConstraintSet.START, 30);
        constraintSet.connect(thirdButton.getId(), ConstraintSet.BOTTOM, createYourOwnButton.getId(), ConstraintSet.TOP);

        //constraint the fourth button
        constraintSet.connect(fourthButton.getId(), ConstraintSet.TOP, secondButton.getId(), ConstraintSet.BOTTOM, 30);
        constraintSet.connect(fourthButton.getId(), ConstraintSet.START, thirdButton.getId(), ConstraintSet.END);
        constraintSet.connect(fourthButton.getId(), ConstraintSet.END, secondButton.getId(), ConstraintSet.END, 30);
        constraintSet.connect(fourthButton.getId(), ConstraintSet.BOTTOM, thirdButton.getId(), ConstraintSet.BOTTOM);

        //constraint the choose your own text prompt button
        constraintSet.connect(createYourOwnButton.getId(), ConstraintSet.TOP, thirdButton.getId(), ConstraintSet.BOTTOM, 30);
        constraintSet.connect(createYourOwnButton.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 30);
        constraintSet.connect(createYourOwnButton.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 30);
        constraintSet.connect(createYourOwnButton.getId(), constraintSet.BOTTOM, sendText.getId(), ConstraintSet.TOP);

        //constraint the chosen text
/*        constraintSet.connect(chosenText.getId(), ConstraintSet.TOP, thirdButton.getId(), ConstraintSet.BOTTOM, 50);
        constraintSet.connect(chosenText.getId(), ConstraintSet.START, thirdButton.getId(), ConstraintSet.START, 50);
        constraintSet.connect(chosenText.getId(), ConstraintSet.END, fourthButton.getId(), ConstraintSet.END);
        constraintSet.connect(chosenText.getId(), ConstraintSet.BOTTOM, sendText.getId(), ConstraintSet.TOP);*/

        //the send text button
        constraintSet.connect(sendText.getId(), ConstraintSet.TOP, createYourOwnButton.getId(), ConstraintSet.BOTTOM, 30);
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

        //once a selection is made the selections cannot be unmade, one prompt has to be selected. Last
        //selected prompt is the one sent. Once send button is clicked.
        promptSelected = true;

        /*
        Testing new idea. Instead of changing the chosenText just highlight the button the user clicked. Try to do this
        like i did it in the welcomeActivity of the Deuce 1 game. There i did it by using drawables.
         */
        Drawable selectedStatus = ContextCompat.getDrawable(getApplicationContext(), R.drawable.prompt_button_selected);
        Drawable unselectedStatus = ContextCompat.getDrawable(getApplicationContext(), R.drawable.prompt_button_default);

        //check which one was selected
        if(view.getId() == firstButton.getId()){
            chosenText.setText(firstButton.getText());

            /*
            currentButtonClicked keeps track of which button was last selected. This if checks if the last selected
            button was firstButton, and if the user wishes to unselect it they have to click it again. This is
            where we check if the user is clicking the button that is currently selected again therefore wanting
            to unselect it. We then set the currentButtonClicked to None therefore unselecting it or to Firstbutton.
             */
            if(currentButtonClicked == FIRST_BUTTON){
                firstButton.setBackground(unselectedStatus);
                currentButtonClicked = NONE;
                Log.d("TESTING", "textPromptClick: first if chosen");
            }
            else {
                firstButton.setBackground(selectedStatus);
                resetOtherButton(currentButtonClicked, unselectedStatus);
                currentButtonClicked = FIRST_BUTTON;
            }
            promptNumberSelected = 1;

        } else if (view.getId() == secondButton.getId()) {
            //second button was clicked

            chosenText.setText(secondButton.getText());
            if(currentButtonClicked == SECOND_BUTTON){
                secondButton.setBackground(unselectedStatus);
                currentButtonClicked = NONE;
            }
            else {
                secondButton.setBackground(selectedStatus);
                resetOtherButton(currentButtonClicked, unselectedStatus);
                currentButtonClicked = SECOND_BUTTON;
            }
            promptNumberSelected = 2;
        } else if (view.getId() == thirdButton.getId()) {
            //third button was clicked

            chosenText.setText(thirdButton.getText());
            if(currentButtonClicked == THIRD_BUTTON){
                thirdButton.setBackground(unselectedStatus);
                currentButtonClicked = NONE;
            }
            else {
                thirdButton.setBackground(selectedStatus);
                resetOtherButton(currentButtonClicked, unselectedStatus);
                currentButtonClicked = THIRD_BUTTON;
            }
            promptNumberSelected = 3;
        } else if (view.getId() == fourthButton.getId()) {
            //fourth button was clicked

            chosenText.setText(fourthButton.getText());
            if(currentButtonClicked == FOURTH_BUTTON){
                fourthButton.setBackground(unselectedStatus);
                currentButtonClicked = NONE;
            }
            else {
                fourthButton.setBackground(selectedStatus);
                resetOtherButton(currentButtonClicked, unselectedStatus);
                currentButtonClicked = FOURTH_BUTTON;
            }
            promptNumberSelected = 4;
        }
    }

    private void resetOtherButton (int thisButton, Drawable toThisDrawable){
        Log.d("TESTING", "resetOtherButton: " + thisButton);
        switch (thisButton){
            case FIRST_BUTTON:
                firstButton.setBackground(toThisDrawable);
                break;

            case SECOND_BUTTON:
                secondButton.setBackground(toThisDrawable);
                break;

            case THIRD_BUTTON:
                thirdButton.setBackground(toThisDrawable);
                break;

            case FOURTH_BUTTON:
                fourthButton.setBackground(toThisDrawable);
                break;

            default:
                Log.d("TESTING", "resetOtherButton: NONE");
                break;
        }
    }

    /*
    This is the onClickListener for the send text button. When clicked it will send the text based on the prompt that was clicked.
    If no prompt is clicked it will let the user know to make a selection.
     */
    private void sendText(View view){
        //first check if the user did make a selection and checked the checkbox
        if(promptSelected && checkBoxClicked){
            //going to send the text, check for the permissions first
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION);
            }
            else {
                sendTextMessage();
            }
        }
        else {
            //check what is missing,
            if (!promptSelected) {
                //no prompt was selected
                chosenText.setText(R.string.makeSelecionB4Sending);
            }
            else if (!checkBoxClicked) //ide thinks this is always true but that is not the case, ignore red underlined text
            {
                /* snackbar won't work because of my theme, it cannot inflate button unless app is
                extending appcombat, im extending activity so it won't work. I also do not want to change
                theme. So will try to use dialog instead. I will not add buttons to it, it will just
                show a message and when the user clicks anywhere on the screen away from the dialog
                it will disappear.
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Click Checkbox before sending message");
                builder.setTitle("Checkbox");

                //create it
                AlertDialog alert = builder.create();
                alert.show();

                /*
                another idea: when this is if is called it means the user has not yet clicked the checkbox. Have it
                change background color or add a border to it to make it easier to see.
                 */
            }
            else if (!checkBoxClicked && !promptSelected){
                //neither were selected, tell user to make a selection
                //will alert user using a dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please select a prompt and click the checkbox before moving on");
                builder.setTitle("Make selections");

                //create and show it
                AlertDialog bothAlert = builder.create();
                bothAlert.show();
            }

        }

    }

    private void sendTextMessage(){
        SmsManager sManager = SmsManager.getDefault();
        String message;
        Toast confirmation = Toast.makeText(this, getResources().getString(R.string.sendTextToast), Toast.LENGTH_SHORT);

        /*
        For now the best way I could come up with is to use a switch statement. That will check which button was clicked and format the text
        from that button. Format it to be specific to each clients name(the clients selected to get the text message. Trying to figure out a
        way to not have to do the for loop in each switch statement.
         */
        switch (promptNumberSelected) {
            case 1:
                for(int i = 0; i < selectedClients.size(); i++){
                    message = String.format(getResources().getString(R.string.firstButtonPrompt), selectedClients.get(i).getClientName());
                    sManager.sendTextMessage(selectedClients.get(i).getClientPhoneNumber(), null, message, null, null);
                    confirmation.show();
                    Log.d("TESTING", "sendTextMessage: " + message + promptNumberSelected);
                }
                break;

            case 2:
                for(int i = 0; i < selectedClients.size(); i++){
                    message = String.format(getResources().getString(R.string.secondButtonPrompt), selectedClients.get(i).getClientName());
                    sManager.sendTextMessage(selectedClients.get(i).getClientPhoneNumber(), null, message, null, null);
                    confirmation.show();
                    Log.d("TESTING", "sendTextMessage: " + message + promptNumberSelected);
                }
                break;

            case 3:
                for(int i = 0; i < selectedClients.size(); i++){
                    message = String.format(getResources().getString(R.string.thirdButtonPrompt), selectedClients.get(i).getClientName());
                    sManager.sendTextMessage(selectedClients.get(i).getClientPhoneNumber(), null, message, null, null);
                    confirmation.show();
                    Log.d("TESTING", "sendTextMessage: " + message + promptNumberSelected);
                }
                break;

            case 4:
                for(int i = 0; i < selectedClients.size(); i++){
                    message = String.format(getResources().getString(R.string.fourthButtonPrompt), selectedClients.get(i).getClientName());
                    sManager.sendTextMessage(selectedClients.get(i).getClientPhoneNumber(), null, message, null, null);
                    confirmation.show();
                    Log.d("TESTING", "sendTextMessage: " + message + promptNumberSelected);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode) {
            case SEND_SMS_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("TESTING", "onRequestPermissionResult: permissions granted");
                    sendTextMessage();
                }
                else {
                    AlertDialog.Builder messageFailBuilder = new AlertDialog.Builder(this);
                    messageFailBuilder.setMessage(R.string.alertDialogMessageFailed);
                    messageFailBuilder.setTitle(R.string.alertDialogMessageFailedTitle);

                    //create the dialog alert
                    AlertDialog messageFailedAlert = messageFailBuilder.create();
                    messageFailedAlert.show();
                }
        }
    }

    //the click listener for the checkbox
    private void checkBoxListener(View view){
        //flip to the opposite, to make sure if the user clicks and then un clicks it does not stay true
        checkBoxClicked = !checkBoxClicked;
        Log.d("TESTING", "checkBoxListener: checkBoxClicked is now " + checkBoxClicked);

    }

    //listener to go back to the previous page
    private void goPreviousPage(View view){
        Log.d("TESTING", "goPreviousPage: Going back ");
        Intent goBack = new Intent(SendTextActivity.this, MainActivity.class);
        startActivity(goBack);
    }
}
