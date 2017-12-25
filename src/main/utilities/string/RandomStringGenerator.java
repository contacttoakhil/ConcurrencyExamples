package main.utilities.string;

/**
 * Created by mittaa3 on 2/19/2015.
 */
public class RandomStringGenerator {
    public static enum Mode {
        ALPHA, ALPHANUMERIC, NUMERIC
    }

    public static String generateRandomString(int length, Mode mode) throws Exception {

        StringBuffer buffer = new StringBuffer();
        String characters = "";

        switch(mode){

            case ALPHA:
                characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                break;

            case ALPHANUMERIC:
                characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                break;

            case NUMERIC:
                characters = "1234567890";
                break;
        }

        int charactersLength = characters.length();

        for (int i = 0; i < length; i++) {
            double index = Math.random() * charactersLength;
            buffer.append(characters.charAt((int) index));
        }
        return buffer.toString();
    }
}
