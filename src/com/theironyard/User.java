package com.theironyard;

import java.util.ArrayList;

/**
 * Created by Caroline on 2/22/16.
 */
public class User {
    String name;
    String password;
    ArrayList<Message> messages = new ArrayList<>();

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
