package app;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encriptacion de contraseñas
 */
public class MD5Hasher {

    private final String input;

    /**
     * Contructor
     */
    MD5Hasher(String str) {
        this.input = str;

    }


    /**
     * Devolver una String encriptada con MD5
     *
     * @return String encriptada con MD5
     */
    public String getMd5() {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(this.input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
