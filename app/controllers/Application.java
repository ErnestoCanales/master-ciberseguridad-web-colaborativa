package controllers;

import models.Constants;
import models.User;
import play.mvc.*;
import play.i18n.Messages;

import java.util.List;

public class Application extends Controller {

    /*
        Modificacion para que devuelva si es un profesor
    */
    protected static Boolean checkTeacher(){
        checkUser();

        User u = (User) renderArgs.get("user");
        if (!u.getType().equals(Constants.User.TEACHER)){
            return false;
        }
        return true;
    }

    /*
        Modificacion para leer del token el usuario 
    */
    private static void checkUser(){ //Modificado
        if (session.contains("auth-token")){
            String token = session.get("auth-token");
            String username=Auth.currentUser(token);
            User u = User.loadUser(username);
            if (u != null){
                renderArgs.put("user", u);
                return;
            }
        }
        Secure.login();
    }

    /*
        Modificacion para verificar que el token es valido y pueda continuar al panel incial
        En caso contrario se le redirige a login. Habria que aplicar esto para cada
        endpoint/controlador que se necesite autenticacion.
    */
    public static void index() {
        if(!Auth.verifySessionToken()){ 
            flash.put("error", Messages.get("Sesion caducada o no valida"));
            Secure.login();
            return;
        }
        checkUser();

        User u = (User) renderArgs.get("user");

        if (u.getType().equals(Constants.User.TEACHER)){
            List<User> students = User.loadStudents();
            render("Application/teacher.html", u, students);
        }else{
            render("Application/student.html", u);
        }
    }


    /*
        Modificacion para verificar token valido y que se trata de un profesor
    */
    public static void removeStudent(String student) {
        if(!Auth.verifySessionToken()){ 
            flash.put("error", Messages.get("Sesion caducada o no valida"));
            Secure.login();
            return;
        }
        if(checkTeacher()){
            User.remove(student);
            index();
        }
        else{
            index();
        }


    }

    /*
        Modificacion para verificar token valido
    */
    public static void setMark(String student) {
        if(!Auth.verifySessionToken()){ 
            flash.put("error", Messages.get("Sesion caducada o no valida"));
            Secure.login();
            return;
        }
        if(checkTeacher()){
            User u = User.loadUser(student);
            render(u);
        }
        else{
            index();
        }
    }

    /*
        Modificacion para verificar token valido
    */
    public static void doSetMark(String student, Integer mark) {
        if(!Auth.verifySessionToken()){ 
            flash.put("error", Messages.get("Sesion caducada o no valida"));
            Secure.login();
            return;
        }
        if(checkTeacher()){
            User u = User.loadUser(student);
            u.setMark(mark);
            u.save();
            index();
        }
        else{
            index();
        }
    }
}