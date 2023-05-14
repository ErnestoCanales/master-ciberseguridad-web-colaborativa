package controllers;


import com.google.gson.JsonObject;
import models.User;
import play.mvc.Controller;
<<<<<<< HEAD
=======
import play.i18n.Messages;

>>>>>>> 5d47f31 (Creacion tokens de sesion y control de autenticacion)

public class Api extends Controller {

    public static void removeAllUsers(){
<<<<<<< HEAD
        User.removeAll();
        renderJSON(new JsonObject());
=======
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
        
>>>>>>> 5d47f31 (Creacion tokens de sesion y control de autenticacion)
    }
}
