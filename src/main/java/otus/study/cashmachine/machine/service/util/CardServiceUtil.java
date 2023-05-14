package otus.study.cashmachine.machine.service.util;

import otus.study.cashmachine.bank.data.Card;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class CardServiceUtil {
    public static void checkPin(Card card, String pin) {
        if (!getHash(pin).equals(card.getPinCode())) {
            throw new IllegalArgumentException("Pincode is incorrect");
        }
    }

    public static String getHash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            digest.update(value.getBytes());
            String result = HexFormat.of().formatHex(digest.digest());
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
