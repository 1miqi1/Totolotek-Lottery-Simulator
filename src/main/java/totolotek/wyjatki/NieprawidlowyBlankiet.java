package totolotek.wyjatki;

public class NieprawidlowyBlankiet extends Exception {
    public NieprawidlowyBlankiet(String message) {
        super("zle wypelniony Blankiet: " + message);
    }
}
