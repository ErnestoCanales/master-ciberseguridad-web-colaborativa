package controllers;


import helpers.HashUtils;
import models.User;
import play.i18n.Messages;
import play.mvc.Controller;

<<<<<<< HEAD
=======

>>>>>>> 5d47f31 (Creacion tokens de sesion y control de autenticacion)
public class Secure extends Controller {

    public static void login(){
        render();
    }

    public static void logout(){
<<<<<<< HEAD
        session.remove("password");
        login();
    }

    public static void authenticate(String username, String password){
        User u = User.loadUser(username);
        if (u != null && u.getPassword().equals(HashUtils.getMd5(password))){
            session.put("username", username);
            session.put("password", password);
            Application.index();
        }else{
            flash.put("error", Messages.get("Public.login.error.credentials"));
            login();
        }

=======
        session.remove("auth-token");
        login();
    }

    /*
        Se soluciona errores como el login sin credenciales o null. En caso que las credenciales
        sean validas devolvera el token de sesion. EN caso contrario muestrar error en la pantalla. 
    */
    public static void authenticate(String username, String password){
        try{
            if(username == null || username.equals("") || password.equals("")){
                throw new Exception("Contraseña y/o usuario incorrectos");
            }
            User u = User.loadUser(username);
            if (u != null && u.getPassword().equals(password)){ //IMplementar hash+salt
                String token = HashUtils.getToken("1",username);
                
                session.put("auth-token", token); //Mejor en el header de la peticion...
                Application.index();
            }
            else{
                throw new Exception("Contraseña y/o usuario incorrectos");
            }
        }
        catch(Exception e){
            flash.put("error", Messages.get(e.toString().split(":")[1]));
            login();
            return;
        }
>>>>>>> 5d47f31 (Creacion tokens de sesion y control de autenticacion)
    }
}
