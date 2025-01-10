package it.unisa.hotelcampus.utils;

import java.nio.charset.StandardCharsets;

public class PasswordHash {

  public static String toHash(String password) {
    String hashString = null;

    try {
      java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
      byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

      StringBuilder sb = new StringBuilder();
      for (byte b : hash) {
        sb.append(String.format("%02x", b));
      }
      hashString = sb.toString();

    } catch (java.security.NoSuchAlgorithmException e) {
      System.out.println(e);
    }
    return hashString;
  }
}
