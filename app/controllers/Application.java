package controllers;

import models.Constants;
import models.User;
import play.mvc.*;
<<<<<<< HEAD
=======
import play.i18n.Messages;
>>>>>>> 5d47f31 (Creacion tokens de sesion y control de autenticacion)

import java.util.List;

public class Application extends Controller {

<<<<<<< HEAD
    private static void checkTeacher(){
=======
    /*
        Modificacion para que devuelva si es un profesor
    */
    protected static Boolean checkTeacher(){
>>>>>>> 5d47f31 (Creacion tokens de sesion y control de autenticacion)
        checkUser();

        User u = (User) renderArgs.get("user");
        if (!u.getType().equals(Constants.User.TEACHER)){
<<<<<<< HEAD
            return;
        }
    }

    private static void checkUser(){
        if (session.contains("username")){
            User u = User.loadUser(session.get("username"));
=======
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
>>>>>>> 5d47f31 (Creacion tokens de sesion y control de autenticacion)
            if (u != null){
                renderArgs.put("user", u);
                return;
            }
        }
        Secure.login();
    }

<<<<<<< HEAD
    public static void index() {
=======
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
>>>>>>> 5d47f31 (Creacion tokens de sesion y control de autenticacion)
        checkUser();

        User u = (User) renderArgs.get("user");

        if (u.getType().equals(Constants.User.TEACHER)){
            List<User> students = User.loadStudents();
            render("Application/teacher.html", u, students);
        }else{
            render("Application/student.html", u);
        }
    }


<<<<<<< HEAD
    public static void removeStudent(String student) {
        checkTeacher();

        User.remove(student);
        index();
    }


    public static void setMark(String student) {
        User u = User.loadUser(student);
        render(u);
    }

    public static void doSetMark(String student, Integer mark) {
        User u = User.loadUser(student);
        u.setMark(mark);
        u.save();
        index();
=======
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
>>>>>>> 5d47f31 (Creacion tokens de sesion y control de autenticacion)
    }
}