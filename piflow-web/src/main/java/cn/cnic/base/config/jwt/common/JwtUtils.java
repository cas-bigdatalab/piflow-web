package cn.cnic.base.config.jwt.common;

import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.component.system.entity.SysRole;
import io.jsonwebtoken.*;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class JwtUtils {
    public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";

    private static final String CLAIM_KEY_USER_ID = "user_id";
    private static final String CLAIM_KEY_AUTHORITIES = "scope";

    private Map<String, String> tokenMap = new ConcurrentHashMap<>(32);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long access_token_expiration;

    @Value("${jwt.expiration}")
    private Long refresh_token_expiration;

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    public UserVo getUserFromToken(String token) {
        UserVo userVo;
        try {

            final Claims claims = getClaimsFromToken(token);
            String userId = getUserIdFromToken(token);
            String username = claims.getSubject();
            String roleName = claims.get(CLAIM_KEY_AUTHORITIES).toString();
            SysRoleType role = SysRoleType.selectGender(roleName);
            userVo = new UserVo(userId, username, role, "");
            ArrayList<SysRole> sysRoles = new ArrayList<>();
            SysRole sysRole = new SysRole();
            sysRole.setRole(role);
            sysRoles.add(sysRole);
            userVo.setRoles(sysRoles);
        } catch (Exception e) {
            userVo = null;
        }
        return userVo;
    }

    public String getUserIdFromToken(String token) {
        String userId;
        try {
            final Claims claims = getClaimsFromToken(token);
            userId = String.valueOf(claims.get(CLAIM_KEY_USER_ID));
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getTokenBody(token);
            username = claims.getSubject();
        } catch (ExpiredJwtException e) {
            username = e.getClaims().getSubject();
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = claims.getIssuedAt();
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public String generateAccessToken(UserVo userDetail) {
        Map<String, Object> claims = generateClaims(userDetail);
        claims.put(CLAIM_KEY_AUTHORITIES, authoritiesToArray(userDetail.getAuthorities()).get(0));
        return generateAccessToken(userDetail.getUsername(), claims);
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getTokenBody(token);
            refreshedToken = generateAccessToken(claims.getSubject(), claims);
        } catch (ExpiredJwtException e) {
            final Claims claims = e.getClaims();
            refreshedToken = generateAccessToken(claims.getSubject(), claims);
        }
        return refreshedToken;
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        UserVo jwtUser = (UserVo) userDetails;
        final String userId = getUserIdFromToken(token);
        final String username = getUsernameFromToken(token);
//        final Date created = getCreatedDateFromToken(token);
        return (userId == jwtUser.getId()
                && username.equals(jwtUser.getUsername())
                && !isTokenExpired(token)
//                && !isCreatedBeforeLastPasswordReset(created, userDetail.getLastPasswordResetDate())
        );
    }

    public String generateRefreshToken(UserVo userDetail) {
        Map<String, Object> claims = generateClaims(userDetail);
        // 只授于更新 token 的权限
        String roles[] = new String[]{JwtUtils.ROLE_REFRESH_TOKEN};
        claims.put(CLAIM_KEY_AUTHORITIES, JSONArray.fromObject(roles).toString());
        return generateRefreshToken(userDetail.getUsername(), claims);
    }

    public void putToken(String userName, String token) {
        tokenMap.put(userName, token);
    }

    public void deleteToken(String userName) {
        if (ObjectUtils.isNotEmpty(tokenMap.get(userName))) {
            tokenMap.remove(userName);
        }
    }

    public boolean containToken(String userName, String token) {
        if (userName != null && tokenMap.containsKey(userName) && tokenMap.get(userName).equals(token)) {
            return true;
        }
        return false;
    }

    public boolean containTokenByUsername(String userName) {
        if (userName != null && tokenMap.containsKey(userName)) {
            return true;
        }
        return false;
    }

    public String getTokenFromUsername(String username) {
        return tokenMap.get(username);
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }
        return claims;
    }

    private Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Date generateExpirationDate(long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        if (ObjectUtils.isNotEmpty(expiration)) {
            return expiration.before(new Date());
        } else {
            return true;
        }
    }

    public Boolean isTwoTimesTokenExpired(String token) {
        try {
            Claims tokenBody = getTokenBody(token);
            Date expiration = tokenBody.getExpiration();
            return expiration.getTime() + access_token_expiration * 1000 * 2 < System.currentTimeMillis();
        } catch (ExpiredJwtException e) {
            long expiredTime = e.getClaims().getExpiration().getTime() + access_token_expiration * 1000 * 2;
            return expiredTime < System.currentTimeMillis();
        }
    }


    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Map<String, Object> generateClaims(UserVo userDetail) {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(CLAIM_KEY_USER_ID, userDetail.getId());
        return claims;
    }

    private String generateAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, access_token_expiration);
    }

    private List<String> authoritiesToArray(Collection<? extends GrantedAuthority> authorities) {
        List<String> list = new ArrayList<>();
        for (GrantedAuthority ga : authorities) {
            list.add(ga.getAuthority());
        }
        return list;
    }


    private String generateRefreshToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, refresh_token_expiration);
    }


    private String generateToken(String subject, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(expiration))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }
}
