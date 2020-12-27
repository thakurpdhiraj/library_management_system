package com.dhitha.lms.auth.service;

import com.dhitha.lms.auth.dto.UserDTO;
import com.dhitha.lms.auth.error.GenericException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.IOUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link TokenService}
 * @author Dhiraj */
@Service
@RequiredArgsConstructor
@Log4j2
public class TokenServiceImpl implements TokenService {
  // Should be read in another way ??
  private static final String ISSUER = "http://localhost:8081";
  private static final int TOKEN_EXPIRY_MIN = 30;
  private static final JWSAlgorithm SIGN_ALGORITHM = JWSAlgorithm.RS256;
  private static final String DELIMITER = ",";

  private final ResourceLoader resourceLoader;

  @Override
  public String generateIdToken(UserDTO userDTO) throws GenericException {
   return generateIdToken(userDTO,null);
  }

  @Override
  public String generateIdToken(UserDTO userDTO, Date issueTime) throws GenericException {
    if(issueTime == null){
      issueTime = Date.from(Instant.now());
    }
    Instant expiryTime = Instant.now().plus(TOKEN_EXPIRY_MIN, ChronoUnit.MINUTES);
    JWTClaimsSet idToken =
        new Builder()
            .issuer(ISSUER)
            .expirationTime(Date.from(expiryTime))
            .issueTime(issueTime)
            .notBeforeTime(issueTime)
            .subject(String.valueOf(userDTO.getId()))
            .claim("name", userDTO.getName())
            .claim("username", userDTO.getUsername())
            .claim("createdAt", userDTO.getCreatedAt())
            .claim("updatedAt", userDTO.getUpdatedAt())
            .claim("accountNonExpired", userDTO.getAccountNonExpired())
            .claim("accountNonLocked", userDTO.getAccountNonLocked())
            .claim("credentialsNonExpired", userDTO.getCredentialsNonExpired())
            .claim("enabled", userDTO.getEnabled())
            .claim("roles", String.join(DELIMITER, userDTO.getUserRoles()))
            .build();
    return this.signAndSerializeToken(idToken);
  }

  @Override
  public JWTClaimsSet verifyToken(String token) throws GenericException {
    try {
      DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
      JWKSet jwkSet = new JWKSet(getPublicKey());
      JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);
      JWSKeySelector<SecurityContext> verificationKeySelector =
          new JWSVerificationKeySelector<>(SIGN_ALGORITHM, jwkSource);
      jwtProcessor.setJWSKeySelector(verificationKeySelector);
      jwtProcessor.setJWTClaimsSetVerifier(
          new DefaultJWTClaimsVerifier<>(
              new JWTClaimsSet.Builder().issuer(ISSUER).build(),
              new HashSet<>(Collections.singletonList("exp"))));
      return jwtProcessor.process(token, null);

    } catch (NumberFormatException | IllegalStateException | ParseException | JOSEException | BadJOSEException e) {
      log.error("Error verifying jwt using public key ", e);
      throw new GenericException("invalid token", HttpStatus.UNAUTHORIZED.value());
    }
  }

  /* ************************** PRIVATE UTILITIES ***************************************/

  private RSAKey getPublicKey() throws GenericException {
    Resource resource = resourceLoader.getResource("classpath:/certs/lms-public-key.pem");
    try {

      String keyString = IOUtils.readFileToString(resource.getFile());
      JWK jwk = JWK.parseFromPEMEncodedObjects(keyString);
      return jwk.toRSAKey();
    } catch (IOException | JOSEException e) {
      log.error("Error fetching public key ", e);
      throw new GenericException();
    }
  }

  private RSAKey getPrivateKey() throws GenericException {
    Resource resource = resourceLoader.getResource("classpath:/certs/lms-private-key.pem");
    try {
      String keyString = IOUtils.readFileToString(resource.getFile());
      JWK jwk = JWK.parseFromPEMEncodedObjects(keyString);
      return jwk.toRSAKey();
    } catch (IOException | JOSEException e) {
      log.error("Error fetching public key ", e);
      throw new GenericException();
    }
  }

  private String signAndSerializeToken(JWTClaimsSet claimsSet) throws GenericException {
    try {
      JWSHeader header = new JWSHeader.Builder(SIGN_ALGORITHM).type(JOSEObjectType.JWT).build();
      RSASSASigner rsassaSigner = new RSASSASigner(getPrivateKey());
      SignedJWT signedJWT = new SignedJWT(header, claimsSet);
      signedJWT.sign(rsassaSigner);
      return signedJWT.serialize();
    } catch (IllegalStateException | JOSEException e) {
      log.error("Error signing jwt using private key ", e);
      throw new GenericException();
    }
  }
}
