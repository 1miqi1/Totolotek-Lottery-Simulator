package totolotek.wyjatki;

public class BladWyboruLosowania extends Exception {
    public BladWyboruLosowania(int numer) {
        super("Losowanie o numerze: " + numer + " sie jeszcze nie odbylo");
    }
    public BladWyboruLosowania(String message) {
        super("Losowanie sie juz odbylo " + message);
    }
}
