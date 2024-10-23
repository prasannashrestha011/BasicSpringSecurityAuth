package application.source.backend.Jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import application.source.backend.Entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String secretKey = "200b6d04141310e0747b61f164b7c88265d4e19f144e639f7087eb7394291e554db127da81088a4cb333632b1cb523037648354740b6a07682eb19435fa8f17934701755fd24d07ffe7721712a493f851b92185e40c07f95f01f51944b8e2d1c846574ae27008ae980a3eb181413b97cad74b19c33443ac2a06350867b83335e25f36e3603f821a3c50798892c91442b3f9b712af728548222a0972cc912663cc9040f60830f226a39a47b17658b19bb2dffd1002972464c63f6a6ff1d6ad8a2b21ac80574d3ae1321803fc84acc01091d2e469ceb270fdccdb899a32d7f11c82c76feb4226551aa867220b9e66b55040ced5d2b9deb078891720eefbb396b5f95dead58b5b4a1c924aca3ea6e26534eec0445b1eff7a9f0786d8244bd693ec55502bcfa1d59cfef0f0d35d382bdbb1b40c31386c45df085034ef14848b9b90e52e1036ef88f00a5c5d0d18cb7ae785cf20ca98fd7d3ca5922e33dd4c5b2e7626839e0a3b107b232bf52f3eb7b13cb0b7c1c89c87b17bf24fe28a04e9cb959318f0f70fc20a4033e1785dd05ff7b20889e3cb7d7d25077ed24c4adc184d38628fe20bec2788746e44455a01359f057adf240046066e5566b35db71a42fb4056ed1fc0cb099f6897d129c25b512c0007bf2dc90050c1d2077adf89f7d4a176e359c11d296822e30db92a44e54060f1ae993e2c6513ce0da01182cf030f4051bff";

    private static final Long jwtExpiration = 86400000L;

    public String generateToken(UserEntity user) {
        return generateTokenWithClaims(new HashMap<>(), user);
    }

    public String generateTokenWithClaims(Map<String, Object> extraClaims, UserEntity user) {

        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSiginKey())
                .compact();
    }

    public SecretKey getSiginKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSiginKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // token validation
    public boolean isTokenValid(String token, UserDetails user) {
        var extractedUsername = extractUsername(token);
        return (extractedUsername.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}