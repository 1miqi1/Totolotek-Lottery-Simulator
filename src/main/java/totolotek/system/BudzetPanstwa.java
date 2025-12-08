package totolotek.system;

public class BudzetPanstwa {
    private static BudzetPanstwa instance;
    private long subsydia;
    private long podatki;

    private BudzetPanstwa() {
        subsydia = 0;
        podatki = 0;
    }

    public static synchronized BudzetPanstwa getInstance() {
        if (instance == null) {
            instance = new BudzetPanstwa();
        }
        return instance;
    }

    long przekazSubsydia(long wartośc) {
        subsydia += wartośc;
        return wartośc;
    }

    void pobierzPodatek(long wartośc){
        podatki += wartośc;
    }

    public long dajSubsydia() {
        return subsydia;
    }

    public long dajPodatki() {
        return podatki;
    }

    public void wypiszSubsydia() {
        System.out.println("Wielkośc pobranych subsydiow: " + subsydia);
    }

    public void wypiszPodatek() {
        System.out.println("Wielkośc wplywow do budzetu: " + podatki);
    }
}
