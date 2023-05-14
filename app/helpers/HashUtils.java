package helpers;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.*;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.*;



public class HashUtils {

    public static String getMd5(String s){
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(s.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = bigInt.toString(16);
            while(hashtext.length() < 32){
                hashtext = "0" + hashtext;
            }
        } catch (Exception e) {}
        return s;
    }

    /*
       Funcion que devuelve la firma un HMAC
    */

    public static String hmacWithJava(String algorithm, String data, String key)
    throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);

        return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
    }

    /*
        Funcion que construye un payload: Header + Body
    */

    public static String getPayload(String algorithm, String type, String id, String name, Long iat){
        JsonObject header = new JsonObject();
        JsonObject body = new JsonObject();

        header.addProperty("alg", algorithm);
        header.addProperty("typ", type);

        body.addProperty("sub", id);
        body.addProperty("name", name);
        body.addProperty("iat", iat);

        String encodedHeader = Base64.getEncoder().encodeToString(header.toString().getBytes());
        String encodedBody = Base64.getEncoder().encodeToString(body.toString().getBytes());

        String result = encodedHeader + "." + encodedBody;

        return result;
    }

    /*
        FUncion que devuelve "montado" el token: HMAC256(Base64(header)+Base64(body), key)
        Por defecto se deja como predeterminado SHA256 pero se puede parametrizar para 
        emplear otros tipos de HMAC. 
    */

    public static String getToken(String id, String name){
        String token="";
        try {
            String hmacSHA256Algorithm = "HmacSHA256"; //Parametrizar
            long unixTime = (System.currentTimeMillis() / 1000L) + 300; //Parametrizar +5minutos
            String type = "JWT";
            String payload = getPayload("HS256",type,id,name,unixTime);
            String key = System.getenv("SECRET_KEY");

            String signature = hmacWithJava(hmacSHA256Algorithm, payload, key);
            token = payload + "." + signature.toString();
            return token;
        }catch (Exception e) {
            return token;
        }
    }

    /*
        Verifica que el token enviado por el cliente es valido, primero obteniento
        el payload(Header+Body), firmando dicho payload con la clave secreta y comparando 
        el resultado con la firma dada. Tambien se verifica que el token no haya caducado
    */
    public static Boolean checkToken(String token){
        try{
            String header = token.split("\\.")[0];
            String body = token.split("\\.")[1];
            String signature = token.split("\\.")[2];
            String hmacSHA256Algorithm = "HmacSHA256"; //Parametrizar
            String key = System.getenv("SECRET_KEY");
            long iatNow = (System.currentTimeMillis() / 1000L);

            //Decode body
            byte[] decodeBody = Base64.getDecoder().decode(body);
            String decodedStringBody = new String(decodeBody);
            JsonObject JsonBody = JsonParser.parseString(decodedStringBody).getAsJsonObject();
            Long iat = JsonBody.get("iat").getAsLong(); // get property "message"

            String payload = header + "." + body;
            String signatureReal = hmacWithJava(hmacSHA256Algorithm, payload, key);
            if(iatNow<iat && signatureReal.equals(signature)){
                return true;
            }
            else{
                throw new Exception("Autorizacion no valida");
            }
        }catch (Exception e) {
            return false;
        }
    }

    /*
        Se soluciona errores como el login sin credenciales. En caso que las credenciales
        sean validas devolvera el token de sesion. EN caso contrario printeara un error. 
    */
    public static String returnUserToken(String token){
        try{
            String body = token.split("\\.")[1];

            //Decode body
            byte[] decodeBody = Base64.getDecoder().decode(body);
            String decodedStringBody = new String(decodeBody);
            JsonObject JsonBody = JsonParser.parseString(decodedStringBody).getAsJsonObject();
            String nameUser = JsonBody.get("name").getAsString(); // get property "message"
            return nameUser;
        }catch (Exception e) {
            return "";
        }
    }
    
}
