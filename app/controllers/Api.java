package controllers;


import com.google.gson.JsonObject;
import models.User;
import play.mvc.Controller;
import play.i18n.Messages;


public class Api extends Controller {

    public static void removeAllUsers(){
        if(!Auth.verifySessionToken()){
            flash.put("error", Messages.get("Sesion caducada o no valida"));
            Secure.login();
            return;
        }
        if(Application.checkTeacher()){
            User.removeAll();
            renderJSON(new JsonObject());
        }
        else{
            renderJSON(new JsonObject());
        }
        
    }
}
