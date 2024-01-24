package com.christian.worktextingapp;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LayoutClass extends ConstraintLayout {

    protected boolean clickedAlready = false;
    protected List<Integer> selectedPeople;

    public LayoutClass(Context context){
        super(context);

        //first set the attributes for the constraint class
        setId(View.generateViewId());

        //make the layoutParams match the parent so they take up the whole available screen
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //set the layout parameters of the constraint layout
        setLayoutParams(layoutParams);
        setBackgroundColor(getResources().getColor(R.color.green_grass));

        //set the constraint set
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        //initialize anything that needs to be initialized
        selectedPeople = new ArrayList<>();

        //create the layout
        createTheLayout(context, constraintSet);

        //apply the constraint set to the ConstraintLayout
        constraintSet.applyTo(this);
    }

    private void createTheLayout(Context context, ConstraintSet constraintSet){
        //add the heading
        TextView welcomeSign = new TextView(context);
        welcomeSign.setId(View.generateViewId());
        welcomeSign.setText("Welcome!");
        welcomeSign.setTextColor(getResources().getColor(R.color.black));
        welcomeSign.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        addView(welcomeSign);

        //testing making a listview without XML
        ListView myList = new ListView(context);
        myList.setId(View.generateViewId());
        myList.setClickable(true);
        addView(myList);

        String[] items = {"one", "two", "three", "four"};
        ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.list_row,items);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(this::listClick);  //change the background to white of the ones selected

        //add a button to send the text
        Button sendText = new Button(context);
        sendText.setId(View.generateViewId());
        sendText.setText("Send Text");
        sendText.setOnClickListener(this::buttonClick); //set the button onClickListener
        addView(sendText);

        //set the constraints of the welcome sign
        constraintSet.constrainHeight(welcomeSign.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(welcomeSign.getId(), ConstraintSet.WRAP_CONTENT);

        constraintSet.connect(welcomeSign.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(welcomeSign.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(welcomeSign.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(welcomeSign.getId(), ConstraintSet.BOTTOM, myList.getId(),  ConstraintSet.TOP);

        //set the constraints of the list
        constraintSet.constrainWidth(myList.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainWidth(myList.getId(), ConstraintSet.MATCH_CONSTRAINT);

        constraintSet.connect(myList.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(myList.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(myList.getId(), ConstraintSet.TOP, welcomeSign.getId(), ConstraintSet.TOP);
        constraintSet.connect(myList.getId(), ConstraintSet.BOTTOM, sendText.getId(), ConstraintSet.TOP);

        //set the constraints of the button
        constraintSet.constrainWidth(sendText.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(sendText.getId(), ConstraintSet.WRAP_CONTENT);

        constraintSet.connect(sendText.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(sendText.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(sendText.getId(), ConstraintSet.TOP, myList.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(sendText.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
    }

    /*
    This method will be called when the items in the ListView are clicked. Each item in the ListView is it's
    own View. The method gives you the view that was clicked and using this we can change the background
    of the clicked ones to let the user know if their selection has been registered. We can also add the
    selected views to a list that will be used to send the text when the button is clicked.
     */
    public void listClick(AdapterView<?> adapterView, View view, int i, long l) {

        //toggle off and on depending on if it is already in the list of text to send or not
        if(selectedPeople.contains(i)){
            view.setBackgroundColor(getResources().getColor(R.color.green_grass));

            /*
            remove the selected user, removeAll works as the users are added not by name but by position in the list
            so there should be no worry of deleting incorrect user. EX. if two users named Mary are selected one top
            of the list and the other in the middle of list they would not both be deleted if the following line of
            code is run since the code seems them as two different users since they are referred to by their position
            in the list not by their name.
             */
            selectedPeople.removeAll(Arrays.asList(i));

            //this prints out the name and the i is the spot the name is in the list
            Log.d("TESTING", "listClick: " + ((TextView)view).getText().toString() + i + " Removed");
        }
        else {
            //let the user know this person has been added to list already
            view.setBackgroundColor(getResources().getColor(R.color.white));

            selectedPeople.add(i);
            Log.d("TESTING", "listClick: " + ((TextView)view).getText().toString() + i + " Added");
        }
    }

    public void buttonClick(View view){
        Log.d("TESTING", "buttonClick: BUTTON WAS CLICKED");
        Log.d("TESTING", "buttonClick: " + selectedPeople);
    }
}
