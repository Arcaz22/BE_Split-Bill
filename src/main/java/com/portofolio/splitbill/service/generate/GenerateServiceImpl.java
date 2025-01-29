package com.portofolio.splitbill.service.generate;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GenerateServiceImpl implements GenerateService {
    private static final String ALGORITHM = "HmacSHA256";

    @Override
    public boolean verifySignature(String secret, String code, String signature) {
        try {
            byte[] hashWithSalt = Base64.getDecoder().decode(signature);

            byte[] hash = new byte[32];
            byte[] salt = new byte[16];
            System.arraycopy(hashWithSalt, 0, hash, 0, 32);
            System.arraycopy(hashWithSalt, 32, salt, 0, 16);

            byte[] codeBytes = code.getBytes(StandardCharsets.UTF_8);
            byte[] codeWithSaltBytes = new byte[codeBytes.length + salt.length];
            System.arraycopy(codeBytes, 0, codeWithSaltBytes, 0, codeBytes.length);
            System.arraycopy(salt, 0, codeWithSaltBytes, codeBytes.length, salt.length);

            Mac sha256Hmac = Mac.getInstance(ALGORITHM);
            sha256Hmac.init(new SecretKeySpec(secret.getBytes(), ALGORITHM));
            byte[] expectedHash = sha256Hmac.doFinal(codeWithSaltBytes);

            for (int i = 0; i < 32; i++) {
                if (hash[i] != expectedHash[i]) {
                    return false;
                }
            }
            return true;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error verifying signature", e);
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Invalid signature", e);
            throw new IllegalArgumentException("Invalid signature");
        }
    }

}
