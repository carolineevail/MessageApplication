package com.theironyard;

/**
 * Created by Caroline on 2/22/16.
 */
public class Message {
    String message;
//    int id;

    public Message(String message) {
        this.message = message;
//        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
