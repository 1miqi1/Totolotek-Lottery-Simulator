package totolotek.wyjatki;

public class NieprawidlowyChybilTrafil extends Exception {
    public NieprawidlowyChybilTrafil(String message) {
        super("zle wypelniony ChybilTrafil: " + message);
    }
}
