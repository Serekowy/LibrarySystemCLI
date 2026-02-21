package LibrarySystem.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
    public String hashPassword(String password) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        byte[] hashedPassword = hashWithSalt(password, salt);

        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hashedPasswordBase64 = Base64.getEncoder().encodeToString(hashedPassword);

        return saltBase64 + ":" + hashedPasswordBase64;
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        if (hashedPassword == null || password == null) return false;

        String[] passwordParts = hashedPassword.split(":");
        byte[] salt = Base64.getDecoder().decode(passwordParts[0]);
        String hash = passwordParts[1];

        byte[] hashedInputPassword = hashWithSalt(password, salt);
        String hashedInputPasswordBase64 = Base64.getEncoder().encodeToString(hashedInputPassword);

        return hash.equals(hashedInputPasswordBase64);
    }

    private byte[] hashWithSalt(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            return md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
