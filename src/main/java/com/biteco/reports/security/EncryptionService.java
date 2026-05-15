package com.biteco.reports.security;

import org.springframework.stereotype.Service;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
public class EncryptionService {
    private static final String ALGORITHM = "AES";
    private final SecretKey key;

    public EncryptionService() {
        // Clave fija de 256 bits para pruebas
        String keyStr = "0123456789abcdef0123456789abcdef";
        this.key = new SecretKeySpec(keyStr.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    public String encrypt(String plaintext) {
        try {
            if (plaintext == null) plaintext = "";
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            System.err.println("Encryption error: " + e.getMessage());
            return plaintext;
        }
    }

    public String decrypt(String encrypted) {
        try {
            if (encrypted == null || encrypted.isEmpty()) return "";
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Decryption error: " + e.getMessage());
            return encrypted;
        }
    }
}
