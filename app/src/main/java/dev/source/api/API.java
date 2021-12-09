package dev.source.api;

//Interface implemented by other API
//used so that the method can be interchangeable
//ex. login with WebAPI, Firebase,...

import dev.source.model.User;


public interface API {


//    all other API class implemented this API must have this method;
    void login(String username, String password, WebAPIListener webAPIListener);

    void getGrade(String token, WebAPIListener webAPIListener);
}
