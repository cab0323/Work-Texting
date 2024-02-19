package com.christian.worktextingapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LayoutClass extends ConstraintLayout implements ActivityCompat.OnRequestPermissionsResultCallback {


    protected List<String> selectedPeopleName;
    private List<Integer> selectedPeopleNumber = new ArrayList<>();

    //testing the contactsid list and the phone number list
    private List<String> contactID = new ArrayList<>();
    private List<String> phoneNumber = new ArrayList<>();

    private List<String> contactsList;
    private ArrayAdapter<String> selectedAdapter;
    private Context myContext;

    //testing the clientClass list
    private List<Client> clients;
    private List<Client> selectedClients;

    public LayoutClass(Context context, Activity act){
        super(context);
        myContext = context;

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
        selectedPeopleName = new ArrayList<>();
        contactsList = new ArrayList<>();
        clients = new ArrayList<>();
        selectedClients = new ArrayList<>();

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

        //check if permission has already been granted to access the contacts "file"
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //permission has not yet been granted, ask for it
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        else {
            //the permission has already been granted, go ahead and read the contacts
            loadContacts(context);
        }

        //create the adapter for the selected list
        selectedAdapter = new ArrayAdapter<>(context, R.layout.list_row, selectedPeopleName);
        selectedList.setAdapter(selectedAdapter);

        //add a button to send the text
        Button sendText = new Button(context);
        sendText.setId(View.generateViewId());
        sendText.setText("Send Text");
        sendText.setOnClickListener(this::sendTextButton); //set the button onClickListener
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

    /*
    This method will read the contacts from the users phone and add them to a list which will then be used in
    a listview to write the contacts to the screen.
     */
    private void loadContacts(Context context){

        /*
        get the contactID, contactName, and if contact has number. Only these 3 things will be returned. This will be accomplished by creating
        the String for the projection, that string will only have these 3 things in it. The projection only cares about the column, null means every
        column should be returned, a specific string array will return what you specify. Selection is for the rows, null here as well means
        every row should be returned.
         */
        String projection[] = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null,null, null);

        //go through the returned data getting each name and id of the users in contacts
        if((cursor != null ? cursor.getCount() : 0) > 0){
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                contactsList.add(name);
                contactID.add(id);

                //now check if they have a phone number
                if(cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0){
                    //they do so then get the number, to do that create a new query that only gets the number of the current person
                    Cursor phoneCursor =
                            resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (phoneCursor.moveToNext()){
                        String number = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneNumber.add(number);

                        Client client = new Client(Integer.parseInt(id), name, number);
                        clients.add(client);
                    }
                    //always close the cursor to be safe
                    phoneCursor.close();
                }

            }
        }

        //close the cursor
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
        if(selectedPeopleNumber.contains(i)){
            view.setBackgroundColor(getResources().getColor(R.color.white));

            /*
            Remove the user from the list. Always call notifyDataSetChanged to make sure the arrayAdapter is
            up to date and constantly updates in real time. The selectedPeopleNumber will make sure the right
            person is added and deleted. Each user is added to the clients list as a copy from the phones
            contacts list. They are added in order they are in, the index they are in the listview corresponds
            to the ID they have in the contacts list.
             */
            selectedPeopleNumber.removeAll(Arrays.asList(i));
            selectedPeopleName.remove(clients.get(i).getClientName());
            selectedAdapter.notifyDataSetChanged();

        }
        else {
            //let the user know this person has been added to list already
            view.setBackgroundColor(getResources().getColor(R.color.yellow_selected));

            //add the person and update the adapter
            selectedPeopleNumber.add(i);
            String n = clients.get(i).getClientName();
            selectedPeopleName.add(n);
            selectedAdapter.notifyDataSetChanged();

        }
    }

    public void sendTextButton(View view){
        Log.d("TESTING", "sendTextButton: BUTTON WAS CLICKED");
        Log.d("TESTING", "sendTextButton: " + contactsList);
        Log.d("TESTING", "sendTextButton: number: " + phoneNumber);
        Log.d("TESTING", "sendTextButton: id: " + contactID);
        Log.d("TESTING", "sendTextButton: selectedNumber" + selectedPeopleNumber);
        Log.d("TESTING", "buttonClick: clients: " + clients);

        /*
        This will add the client class to the list of selected clients. This way it will be easier to pass the information as an array of client type
        to the next activity. This is possible since the list holding the user selected has the users in order, starting at index zero. The clients
        are also in that exact same order so it works.
         */
        for( int i = 0; i < selectedPeopleNumber.size(); i++){
            //add the corresponding client to the list
            selectedClients.add(clients.get(selectedPeopleNumber.get(i)));
        }

        for(int i = 0; i < selectedClients.size(); i++){
            Log.d("TESTING", "sendTextButton: selectedClients: " + selectedClients.get(i).getClientName());
        }

        Bundle arrayBundle = new Bundle();

        Intent goToSendText = new Intent(myContext, SendTextActivity.class);
        ArrayList<Client> selectedClientsArray = new ArrayList<>(selectedClients);
        goToSendText.putExtra("selectedPeople", selectedClientsArray);
        myContext.startActivity(goToSendText);
    }

}
