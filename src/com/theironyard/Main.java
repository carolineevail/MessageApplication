package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;

public class Main {

    static HashMap<String, User> allUsers = new HashMap<>();

    public static void main(String[] args) {
        Spark.init();



        Spark.get(
                "/",
                ((request, response) -> {
                    User user = getUserFromSession(request.session());


                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    }
                    else {
                        m.put("name", user.getName());
                        m.put("password", user.getPassword());
                        m.put("messages", user.getMessages());
                        return new ModelAndView(m, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    User user = null;
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    if (allUsers.containsKey(name)) {
                        if (password.equalsIgnoreCase(allUsers.get(name).getPassword())){
                            user = allUsers.get(name);
                            response.redirect("/");
                        } else {
                            response.redirect("/");
                        }
                    }
                    else {
                        user = new User(name, password);
                        allUsers.put(user.getName(), user);
                        response.redirect("/");
                    }
                    Session session = request.session();
                    session.attribute("userName", name);
                    allUsers.put(user.getName(), user);

                    return "";
                })
        );

        Spark.post(
                "/create-message",
                ((request, response) -> {
                    String text = request.queryParams("newMessage");
                    Message message = new Message(text);
                    getUserFromSession(request.session()).getMessages().add(message);
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/delete-message",
                ((request, response) -> {
                    int messageNumber = Integer.valueOf(request.queryParams("deleteMessageId")) - 1;
                    getUserFromSession(request.session()).getMessages().remove(messageNumber);
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/edit-message",
                ((request, response) -> {
                    int messageNumber = Integer.valueOf(request.queryParams("editMessageId")) - 1;
                    String newMessage = request.queryParams("editMessageText");
                    getUserFromSession(request.session()).getMessages().get(messageNumber).setMessage(newMessage);
                    //getUserFromSession(request.session()).getMessages().add(new Message(newMessage, messageNumber + 1));
                    response.redirect("/");
                    return "";
                })
        );



        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
    }

    static User getUserFromSession(Session session) {
        String name = session.attribute("userName");
        return allUsers.get(name);
    }
}
