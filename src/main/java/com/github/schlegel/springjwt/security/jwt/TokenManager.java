package com.github.schlegel.springjwt.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.github.schlegel.springjwt.security.PrincipalUser;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class TokenManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManager.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expires.days}")
    private int expiresInDays;

    @Autowired
    private UserDetailsService userDetailsService;

    public SignedJWT createNewToken(final PrincipalUser principalUser) {
        try {
            Date now = new Date();
            Date expires = DateUtils.addDays(now, expiresInDays);

            // set time to midnight
            expires = DateUtils.setHours(expires, 0);
            expires = DateUtils.setMinutes(expires, 0);
            expires = DateUtils.setMinutes(expires, 0);
            expires = DateUtils.setMilliseconds(expires, 0);

            JWTClaimsSet claimsSet = new JWTClaimsSet();
            claimsSet.setSubject(principalUser.getUsername());
            claimsSet.setIssueTime(now);
            claimsSet.setNotBeforeTime(now);
            claimsSet.setExpirationTime(expires);

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            JWSSigner signer = new MACSigner(secret);
            signedJWT.sign(signer);

            return signedJWT;
        } catch (JOSEException e) {
            LOGGER.error("Couldn't generate token for user " + principalUser.getUsername(), e);
        }

        return null;
    }

    public PrincipalUser getUserDetails(final String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret);

            if (signedJWT.verify(verifier)) {
                Date now = new Date();
                boolean isNotExpired = now.before(signedJWT.getJWTClaimsSet().getExpirationTime());

                if (isNotExpired) {
                    String email = signedJWT.getJWTClaimsSet().getSubject();
                    try {
                        return (PrincipalUser) userDetailsService.loadUserByUsername(email);
                    } catch (UsernameNotFoundException e) {
                        // user not available in database
                        return null;
                    }
                }
            }
        } catch (IllegalStateException e) {
            LOGGER.debug("Illegal state for token \"" + token + "\"");
        } catch (JOSEException e) {
            LOGGER.debug("Can't verify token \"" + token + "\"");
        } catch (ParseException e) {
            LOGGER.debug("Can't parse token \"" + token + "\"");
        }

        return null;
    }
}
