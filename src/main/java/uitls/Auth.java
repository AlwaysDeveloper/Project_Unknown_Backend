package uitls;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class Auth {
    protected static String SECRET_KEY = "This is the secret key";
    public String JsonWebToken_create(String id, String username) throws UnsupportedEncodingException {
        JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(null);
        JsonObjectBuilder jsonObjectBuilder = jsonBuilderFactory.createObjectBuilder();
        jsonObjectBuilder.add("alg","HS256");
        String header = jsonObjectBuilder.build().toString();
        header = Base64.getUrlEncoder().encodeToString(header.getBytes("UTF-8"));
        jsonObjectBuilder.add("user", username);
        String claims = jsonObjectBuilder.build().toString();
        String concatenated = header + "." + claims;
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key sign  = new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder().setId(id).setSubject(claims).setExpiration(new Date(System.currentTimeMillis()+(90*60*1000)))
                .setIssuedAt(new Date(System.currentTimeMillis())).signWith(signatureAlgorithm, sign);
        return builder.compact();
    }

    public Claims decodeJWT(String token) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token).getBody();
        return claims;
    }

    public String BcryptHashPassword(String password){
        return new String(
                BCrypt.withDefaults().hashToChar(12, password.toCharArray())
        );
    }

    public Boolean isPasswordCorrect(String password, String toCheck){
        BCrypt.Result result = BCrypt.verifyer().verify(toCheck.toCharArray(), password.toCharArray());
        return result.verified;
    }

    //get the jsonwebtoken from cookie string
    public String _getToken(List cookies) throws Exception {
        ListIterator iterator = cookies.listIterator();
        String toProcess = "";
        while (iterator.hasNext()){
            String[] index = iterator.next().toString().split("; ");
            for (String s :index){
                toProcess = s.indexOf("Bearer") == 0 || s.indexOf("jwt") == 0 ? s : "";
                break;
            }
        }

        if(toProcess.equals("")) throw new Exception("You are not logged in");

        if(toProcess.indexOf(",Http") < 0){
            toProcess = toProcess.split("=")[0];
        }else {
            toProcess= toProcess.split(",Http")[0].split("=")[1];
        }

        return toProcess;
    }


}
