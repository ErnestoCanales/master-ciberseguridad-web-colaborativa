package controllers;
import models.User;
import play.mvc.Controller;
import helpers.HashUtils;


/*
    NUeva clase que actua como middleware
*/
public class Auth extends Controller {

    public static Boolean verifySessionToken(){
        String token = session.get("auth-token");
        if(HashUtils.checkToken(token)){
            return true;
        }
        return false;
    }
    public static String currentUser(String token){
        String result = HashUtils.returnUserToken(token);
        return result;
    }
}

