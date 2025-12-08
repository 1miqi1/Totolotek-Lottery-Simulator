package totolotek.system;

public abstract class GeneratorKuponow {
    abstract Kupon wygenerujKupon(int numerLosowania, int numerKolektury, String identyfikator);
    public long cenaWygenerowaniaKuponu() {
        return wygenerujKupon(0,0,"").cena();
    }
}
