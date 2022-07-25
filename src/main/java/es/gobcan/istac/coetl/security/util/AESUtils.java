package es.gobcan.istac.coetl.security.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AESUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AESUtils.class);

    public static final String CRYPT_ALGORITHM = "AES";
    public static final String CRYPT_INSTANCE= "AES/ECB/PKCS5PADDING";
    public static final String SECRET_KEY_INSTANCE= "PBKDF2WithHmacSHA256";
    public static final String SECRET_KEY_SALT= "474553544f525f434f4e534f4c415f45544c"; //GESTOR_CONSOLA_ETL
    public static final String SECRET_KEY_PASSWORD= "GESTOR_CONSOLA_ETL";
    public static final Integer ITERATIONS = 65536;
    public static final Integer LENGTH = 128;

    public static String encrypt(String password) throws NoSuchPaddingException, NoSuchAlgorithmException,
        InvalidAlgorithmParameterException {
        byte[] cipherText = null;
        Cipher cipher = Cipher.getInstance(CRYPT_INSTANCE);

        try {
            cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword());
            cipherText = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e ){
            LOG.error("Error encrypt :  ", e);
        }
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
        InvalidAlgorithmParameterException {
        byte[] plainText = null;
        Cipher cipher = Cipher.getInstance(CRYPT_INSTANCE);
        try {
            cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword());
            plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException  e){
            LOG.error("Error decrypt :  ", e);
        }
        return new String(plainText);
    }

    private static SecretKey getKeyFromPassword() {
        SecretKey secret = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE);
            KeySpec spec = new PBEKeySpec(SECRET_KEY_PASSWORD.toCharArray(), SECRET_KEY_SALT.getBytes(), ITERATIONS, LENGTH);
            secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), CRYPT_ALGORITHM);
        }catch (InvalidKeySpecException  | NoSuchAlgorithmException e) {
            LOG.error("Error invalid key ", e);
        }
        return secret;
    }
}
