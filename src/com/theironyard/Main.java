package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static User user;


    public static void main(String[] args) {
        Spark.init();
        HashMap<String, User> allUsers = new HashMap<>();

        User alice = new User("Alice", "1234");
        User bob = new User("Bob", "2345");

        allUsers.put(alice.name, alice);
        allUsers.put(bob.name, bob);

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    }
                    else {
                        m.put("name", user.name);
                        m.put("password", user.password);
                        m.put("messages", user.messages);
                        return new ModelAndView(m, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    if (allUsers.containsKey(name)) {
                        if (password.equalsIgnoreCase(allUsers.get(name).password)){
                            user = new User(name, password);
                            response.redirect("/");
                        } else {
                            response.redirect("/");
                        }
                    }
                    else {
                        user = new User (name, password);
                        allUsers.put(user.name, user);
                        response.redirect("/");
                    }
                    return "";
                })
        );

        Spark.post(
                "/create-message",
                ((request, response) -> {
                    String text = request.queryParams("newMessage");
                    Message message = new Message(text, user.messages.size() + 1);
                    user.messages.add(message);
                    response.redirect("/");
                    return "";
                })
        );
    }
}
