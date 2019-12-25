package Server;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class Crypto {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static String salt = "MSgdllepa4msYtI0w7o+aeJxjLnZlXa70m0e9n69jt7PlD5rA4GRw+ZuEOnsk2YysrBOhpPXTmzNldwLv23Vf4ldb4jluXWhItOi+JycjVAwwXnFwiKl8BUlT84QFMhR4B0L//jcnw3UtTI5sMdo5q/rujIyQfG4EVyVFnYmI1B6IQnCv4j5zrClJxFp9Mpp2rkFeDlVQ1W6vCRG+4g31i4Xy7+Cnc1LIhoWUqUWvhlkx8oSrfhsDXI6RUFlesKvRASzqUgXSxJyxEF2BRlsuf/LCBRBgLeLB6WN2RlksMWse/B65QJptGwJkvZ86Uv2Cp4zn4VAZGKyBYAgq1d5ueFIb0YB4ZPW/Fdk/9O1BJtK7RMrZQmTT7sffPSVOGEy1cgXpuCwKdIkoH6OHY1YCWJoUiHO4OxDHV1WNYuR0g6DyHqVCjcoG9yUQj5qe4Q6oIuNvErRjkhlYcFVXPZiWANolsmYyJfHt/Ih4+kPxMPJUnHtikLLvjCtIhSB62SfNn5avr8V5DQHdBG4DBxB0uzHSPNKXM0ZH6DL//POZbmJZ2SpP56Cz6onhLbx5wzsmWH3qc0JUDR8OxaZtP/c5WCkj++MX+/r+t516Lpx4KhZLl4Mp1dlNZFxzVL7mOlkv7VGv+aPv+/GSlZFprDXGOIBgBZrDonyzFs/nZdkzhI=";

    public static String generateSalt(int length){
        if(length<1){
            System.err.println("Salt length must be more than 1");
            return null;
        }
        byte[] salt = new byte[length];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    public static String hashPassword(String password){
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = salt.getBytes();
        PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, ITERATIONS, KEY_LENGTH );

        Arrays.fill(passwordChars, Character.MIN_VALUE);

        try{
            SecretKeyFactory skFactory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = skFactory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(securePassword);
        }catch (NoSuchAlgorithmException | InvalidKeySpecException ex){
            ex.printStackTrace();
            return null;
        }finally {
            spec.clearPassword();
        }
    }
}
