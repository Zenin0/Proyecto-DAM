package app;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Objecto de encriptacion de contraseñas
public class MD5Hasher {

    private final String input;

    MD5Hasher(String str) {
        this.input = str;

    }

    public String getMd5() {
        try {

            // El método estático getInstance se llama con hash MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Se llama al método digest() para calcular el resumen del mensaje
            // de un resumen de entrada () devuelve una matriz de bytes
            byte[] messageDigest = md.digest(this.input.getBytes());

            // Convierte una matriz de bytes en una representación signum
            BigInteger no = new BigInteger(1, messageDigest);

            // Convierte el resumen del mensaje en valor hexadecimal
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();
        }

        // Para especificar algoritmos de resumen de mensajes incorrectos
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
