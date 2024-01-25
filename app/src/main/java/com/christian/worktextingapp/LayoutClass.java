package com.christian.worktextingapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LayoutClass extends ConstraintLayout implements ActivityCompat.OnRequestPermissionsResultCallback {

    protected boolean clickedAlready = false;
    protected List<Integer> selectedPeopleNumber;
    protected List<String> selectedPeopleName;

    private List<String> contactsList;
    private ArrayAdapter<String> selectedAdapter;

    public LayoutClass(Context context, Activity act){
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
        selectedPeopleNumber = new ArrayList<>();
        selectedPeopleName = new ArrayList<>();
        contactsList = new ArrayList<>();

        //create the layout
        createTheLayout(context, constraintSet, act);

        //apply the constraint set to the ConstraintLayout
        constraintSet.applyTo(this);
    }

    private void createTheLayout(Context context, ConstraintSet constraintSet, Activity activity){

        //this guideline will be at 75% of the way down the screen
        Guideline guidelineAtSeventyFive = new Guideline(context);
        ConstraintLayout.LayoutParams lp = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.orientation = LayoutParams.HORIZONTAL;
        guidelineAtSeventyFive.setId(View.generateViewId());
        guidelineAtSeventyFive.setLayoutParams(lp);
        addView(guidelineAtSeventyFive);
        guidelineAtSeventyFive.setGuidelinePercent(0.75f);

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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_row,contactsList);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(this::listClick);  //change the background to white of the ones selected

        //making a new list that will sit beside the contacts list
        ListView selectedList = new ListView(context);
        selectedList.setId(View.generateViewId());
        addView(selectedList);

        //testing getting permission to read the contacts
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //permission has not yet been granted, ask for it
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        else {
            //the permission has already been granted, go ahead and read the contacts
            loadContacts(context);
        }
        //----------------

        //create the adapter for the selected list
        //ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(context, R.layout.list_row, selectedPeopleNumber);
        selectedAdapter = new ArrayAdapter<>(context, R.layout.list_row, selectedPeopleName);
        selectedList.setAdapter(selectedAdapter);

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
        constraintSet.connect(welcomeSign.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 50);
        constraintSet.connect(welcomeSign.getId(), ConstraintSet.BOTTOM, myList.getId(),  ConstraintSet.TOP);

        //set the constraints of the contacts list
        constraintSet.constrainWidth(myList.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainWidth(myList.getId(), ConstraintSet.MATCH_CONSTRAINT);

        constraintSet.connect(myList.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(myList.getId(), ConstraintSet.END, selectedList.getId(), ConstraintSet.START);
        constraintSet.connect(myList.getId(), ConstraintSet.TOP, welcomeSign.getId(), ConstraintSet.BOTTOM, 50);
        constraintSet.connect(myList.getId(), ConstraintSet.BOTTOM, guidelineAtSeventyFive.getId(), ConstraintSet.TOP);

        //set the constraints of the selectedList
        constraintSet.constrainWidth(selectedList.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(selectedList.getId(), ConstraintSet.MATCH_CONSTRAINT);

        constraintSet.connect(selectedList.getId(), ConstraintSet.START, myList.getId(), ConstraintSet.END);
        constraintSet.connect(selectedList.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(selectedList.getId(), ConstraintSet.TOP, myList.getId(), ConstraintSet.TOP);
        constraintSet.connect(selectedList.getId(), ConstraintSet.BOTTOM, myList.getId(), ConstraintSet.BOTTOM);

        //set the background of the listview
        myList.setBackgroundColor(getResources().getColor(R.color.white));

        //set the constraints of the button
        constraintSet.constrainWidth(sendText.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(sendText.getId(), ConstraintSet.WRAP_CONTENT);

        constraintSet.connect(sendText.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(sendText.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(sendText.getId(), ConstraintSet.TOP, guidelineAtSeventyFive.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(sendText.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
    }

    /*
    This will be called when the user is presented with the permission question. If they click agree this will be called. From here i can call
    the loadContacts method. I can just implement the method instead of having to extend from all of activity.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        loadContacts(getContext());
        //ill have to test if getContext gets the correct context
    }

    private void loadContacts(Context context){
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor=resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        if((cursor != null ? cursor.getCount() : 0) > 0){
            while (cursor != null && cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                contactsList.add(name);
            }
        }

        if(cursor != null){
            cursor.close();
        }
    }

    /*
    This method will be called when the items in the ListView are clicked. Each item in the ListView is it's
    own View. The method gives you the view that was clicked and using this we can change the background
    of the clicked ones to let the user know if their selection has been registered. We can also add the
    selected views to a list that will be used to send the text when the button is clicked.
     */
    public void listClick(AdapterView<?> adapterView, View view, int i, long l) {

        //get the name of the person selected
        String personSelected = ((TextView)view).getText().toString();

        //toggle off and on depending on if it is already in the list of text to send or not
        if(selectedPeopleName.contains(personSelected)){
            view.setBackgroundColor(getResources().getColor(R.color.white));

            /*
            remove the selected user, removeAll works as the users are added not by name but by position in the list
            so there should be no worry of deleting incorrect user. EX. if two users named Mary are selected one top
            of the list and the other in the middle of list they would not both be deleted if the following line of
            code is run since the code seems them as two different users since they are referred to by their position
            in the list not by their name.
             */
            //selectedPeopleNumber.removeAll(Arrays.asList(i));

            selectedPeopleName.remove(personSelected);
            selectedAdapter.notifyDataSetChanged();

            //this prints out the name and the i is the spot the name is in the list
            Log.d("TESTING", "listClick: " + ((TextView)view).getText().toString() + i + " Removed");
        }
        else {
            //let the user know this person has been added to list already
            view.setBackgroundColor(getResources().getColor(R.color.yellow_selected));

            selectedPeopleName.add(personSelected);
            //update the adapter
            selectedAdapter.notifyDataSetChanged();
            Log.d("TESTING", "listClick: " + ((TextView)view).getText().toString() + i + " Added");
        }
    }

    public void buttonClick(View view){
        Log.d("TESTING", "buttonClick: BUTTON WAS CLICKED");
        Log.d("TESTING", "buttonClick: " + selectedPeopleName);
    }

}
