package com.lmzfm.jwt;
import com.lmzfm.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
public class JwtUtil {

    public static final long EXPIRE = 1000 * 60 * 60 ;
    public static final String APP_SECRET = "lmzfm";
    public static String getJwtToken(UserInfo user){
        String JwtToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim("username", user.getUsername())
                .claim("type", user.getType())
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
        return JwtToken;
    }
    /**
     * 判断token是否存在与有效
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 判断token是否存在与有效
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if(StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取用户id
     */
    public static Integer getUserId(String token){
        Jws<Claims> claimsJws =
                Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (Integer) claims.get("id");
    }

    /**
     * 获取用户type
     */
    public static Integer getUserType(String token){
        Jws<Claims> claimsJws =
                Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (Integer) claims.get("type");
    }

    /**
     * 根据token获取xm
     * @return
     */
    public static String getMemberIdByJwtToken(String token) {
//        String jwtToken = request.getHeader("token");
        Jws<Claims> claimsJws =
                Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("username");
    }




    public static void main(String[] args) {
        //生成token
//        UserInfo info=new UserInfo();
//        info.setId(1);
//        info.setUsername("admin");
//        info.setType(1);//管理员
//        String token=getJwtToken(info);
//        System.out.println("token："+token);
        String token="eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2ODA5MzU0MjEsImV4cCI6MTY4MDkzNzIyMSwidXNlcm5hbWUiOiJhZG1pbiIsInR5cGUiOjEsImlkIjoxfQ.lMgK6w0Wu1C_Qi_G4xg9NGUA-nN6ZTBn0JVu3hAwBcE";
        //验证是否有效
        boolean b = checkToken(token);
        System.out.println("boolean："+b);

        //获取用户名信息
        System.out.println("username:"+getMemberIdByJwtToken(token));

    }
}

