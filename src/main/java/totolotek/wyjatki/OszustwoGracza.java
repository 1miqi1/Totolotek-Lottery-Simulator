package totolotek.wyjatki;

public class OszustwoGracza extends Exception {
    public OszustwoGracza(String message) {
        super("Gracz przylapany na oszustwie: " + message);
    }
}
