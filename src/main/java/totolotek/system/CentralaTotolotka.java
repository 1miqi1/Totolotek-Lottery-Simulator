package totolotek.system;

import java.util.ArrayList;

import totolotek.pomocniczeklasy.Stale;
import totolotek.wyjatki.BladWyboruLosowania;
import totolotek.wyjatki.ZlaKolektura;

public class CentralaTotolotka {
    private static CentralaTotolotka instance;
    private long budzetCentrali;
    private final ArrayList<Losowanie> losowania;
    private final ArrayList<Kolektura> kolektury;
    public int liczbaSprzedanychKuponow;
    public static final long WYSOKANAGRODA = 228000;
    public static final long NANAGRODY = 49;

    public static CentralaTotolotka getInstance() {
        if (instance == null) {
            instance = new CentralaTotolotka();
        }
        return instance;
    }

    private CentralaTotolotka() {
        this.budzetCentrali = 0;
        this.liczbaSprzedanychKuponow = 0;
        this.kolektury = new ArrayList<>();
        this.losowania = new ArrayList<>();
        for (int i = 0; i < Stale.MAXLICZBALOSOWAN; i++) {
            losowania.add(new Losowanie(i + 1));
        }
    }

    public Kolektura utworzKolekture(){
        Kolektura k = new Kolektura(kolektury.size()+1);
        kolektury.add(k);
        return k;
    }

    public Kolektura dajKolekture(int numer) throws ZlaKolektura {
        if( numer > kolektury.size() || numer < 0){
            throw new ZlaKolektura("Nie istnieje kolekutra o danym numerze");
        }
        return kolektury.get(numer - 1);
    }

    public int liczbaKolektur(){
        return kolektury.size();
    }

    public int liczbaSprzedanychKuponow() {
        return liczbaSprzedanychKuponow;
    }

    public int numerKolejnegoLosowanie() {
        return losowania.size() - Stale.MAXLICZBALOSOWAN + 1;
    }

    void zwiekszPuleNastepnegoLosowania(long wartosc) throws BladWyboruLosowania {
        losowania.get(numerKolejnegoLosowanie()-1).zwiekszPule(wartosc);
    }

    long wyplac(Kupon kupon) throws BladWyboruLosowania {
        long wygrana = 0;
        long podatek = 0;

        ArrayList<Zaklad> z = kupon.zaklady();

        int pierwsze = kupon.pierwszeLosowanie();
        int ostatnie = kupon.ostatnieLosowanie();
        for (int i = pierwsze; i <= Math.min(numerKolejnegoLosowanie(),ostatnie); i++) {
            Losowanie l = losowania.get(i - 1);
            for (Zaklad zaklad : z) {
                long w = l.wygrana(zaklad);
                wygrana += w;
                if (w > WYSOKANAGRODA) {
                    podatek += w * 10 / 100;
                }
            }
        }

        BudzetPanstwa.getInstance().pobierzPodatek(podatek);
        if (wygrana > budzetCentrali) {
            budzetCentrali += BudzetPanstwa.getInstance().przekazSubsydia(wygrana - budzetCentrali);
        }
        budzetCentrali -= wygrana;
        return wygrana - podatek;
    }

    void wprowadzKupon(Kupon kupon) throws BladWyboruLosowania {
        ArrayList<Zaklad> z = kupon.zaklady();
        int n = kupon.liczbaLosowan();

        long cena = kupon.cena() - kupon.podatek();
        BudzetPanstwa.getInstance().pobierzPodatek(kupon.podatek());
        long wartoścNagrod = cena * NANAGRODY / 100;
        budzetCentrali += cena - wartoścNagrod;

        int pierwsze = kupon.pierwszeLosowanie();
        int ostatnie = kupon.ostatnieLosowanie();
        for (int i = pierwsze; i <= ostatnie; i++) {
            Losowanie losowanie = losowania.get(i - 1);
            losowanie.dodajZaklady(z);
            losowanie.zwiekszPule(wartoścNagrod / n);
        }
        liczbaSprzedanychKuponow++;
    }

    public void przeprowadzLosowanie() throws BladWyboruLosowania {
        Losowanie ak = losowania.get(this.numerKolejnegoLosowanie()-1);
        losowania.add(new Losowanie(this.losowania.size() + 1));
        ak.losuj();
    }

    void kumulacja(long a) {
        losowania.get(this.numerKolejnegoLosowanie()-1).kumulacja(a);
    }

    // Informacje publiczne:
    public void sprawozdanie(int numerLosowania) throws BladWyboruLosowania {
        if(numerLosowania >= numerKolejnegoLosowanie()  || numerLosowania <= 0) {
            throw new BladWyboruLosowania(numerLosowania);
        }
        losowania.get(numerLosowania - 1).sprawozdanie();
    }

    public void wypiszStanŚrodkow() {
        System.out.println("Budzet Centrali wynosi: " + budzetCentrali);
    }

    public long wygranaStopnia(int numerLosowania, int stopien) throws BladWyboruLosowania {
        if(numerLosowania >= numerKolejnegoLosowanie()  || numerLosowania <= 0) {
            throw new BladWyboruLosowania(numerLosowania);
        }
        return losowania.get(numerLosowania - 1).wygrana(stopien);
    }

    public long pulaStopniaPierwszego(int numerLosowania) throws BladWyboruLosowania {
        if(numerLosowania >= numerKolejnegoLosowanie()  || numerLosowania <= 0) {
            throw new BladWyboruLosowania(numerLosowania);
        }
        return losowania.get(numerLosowania - 1).pulaStopnia()[1];
    }

    public int[] zwycieskaSzostka(int numerLosowania) throws BladWyboruLosowania {
        if(numerLosowania >= numerKolejnegoLosowanie()  || numerLosowania <= 0) {
            throw new BladWyboruLosowania(numerLosowania);
        }
        return losowania.get(numerLosowania - 1).zwycieskaSzostka().liczby();
    }
}
