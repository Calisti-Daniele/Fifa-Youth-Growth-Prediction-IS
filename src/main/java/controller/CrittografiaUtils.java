package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CrittografiaUtils {
    public static String hashSHA1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b)); // Converte in esadecimale
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore nella generazione dell'hash SHA-1", e);
        }
    }
}
