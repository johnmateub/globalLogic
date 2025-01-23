package com.globallogic.usuarios.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AESUtil {
  @Qualifier("passwordKey")
  private final SecretKey secretKey;

  public String encryptPasswordBased(String plainText, IvParameterSpec iv) {
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
      return Base64.getEncoder()
          .encodeToString(cipher.doFinal(plainText.getBytes()));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
             InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
      System.out.println(e.getMessage());
      return plainText;
    }

  }

  public String decryptPasswordBased(String cipherText, IvParameterSpec iv) {
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
      return new String(cipher.doFinal(Base64.getDecoder()
          .decode(cipherText)));
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
             InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
      return cipherText;
    }
  }

}
