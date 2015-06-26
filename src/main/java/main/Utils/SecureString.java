package main.Utils;

/**
 * Created by xkcd2 on 4/11/2015.
 */
import java.math.BigInteger;
import java.security.SecureRandom;

public final class SecureString {
    private static SecureRandom random = new SecureRandom();

    public static String nextUserAuthString() {
        return new BigInteger(130, random).toString(32);
    }
}