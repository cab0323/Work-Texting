package com.christian.worktextingapp;

public class Client {

    private int clientID;
    private String clientName;
    private String clientPhoneNumber;

    /*
    this class is where the information of each client is stored. Will add more methods as time comes for example a method
    to check when you last did this clients maintanence, ect. For now just does basic data storage of the clients
    id, name, number
     */
    public Client(int clientID, String clientName, String clientPhoneNumber){
        //initialize the clients information
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientPhoneNumber = clientPhoneNumber;

    }

    //do my getters for now
    public int getClientID(){
        return clientID;
    }

    public String getClientName(){
        return clientName;
    }

    public String getClientPhoneNumber(){
        return clientPhoneNumber;
    }

}
