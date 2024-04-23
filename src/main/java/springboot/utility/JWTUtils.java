package springboot.utility;

import java.util.Calendar;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtils {
    // key
    private static final String SING = "123qwaszx";

    /**
     * generate token, expire in 7 days
     */
    public static String getToken(Map<String,String> map){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,7); 
        JWTCreator.Builder builder = JWT.create();
        for (Map.Entry<String, String> entry : map.entrySet()) {
        	builder.withClaim(entry.getKey(), entry.getValue());
        }

        return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SING));
    }

    /**
     * verfiy token
     */
    public static DecodedJWT verify(String token){
        DecodedJWT verify = null;
        try {
            verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        }catch (SignatureVerificationException e){
            e.printStackTrace();
        }catch (AlgorithmMismatchException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return verify;
    }
}
