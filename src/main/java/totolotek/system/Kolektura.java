package totolotek.system;

import java.util.HashMap;

import totolotek.gracz.Gracz;
import totolotek.pomocniczeklasy.Losowe;
import totolotek.pomocniczeklasy.PomoczniczeFunkcje;
import totolotek.wyjatki.BladWyboruLosowania;
import totolotek.wyjatki.OszustwoGracza;

public class Kolektura {
    private final int numer;
    private final HashMap<Kupon, Boolean> sprzedaneKupony;

    Kolektura(int numer) {
        this.numer = numer;
        this.sprzedaneKupony = new HashMap<>();
    }

    public long wyplac(Kupon k) throws OszustwoGracza, BladWyboruLosowania {
        if (!sprzedaneKupony.containsKey(k)) {
            throw new OszustwoGracza(" kupon nie zostal sprzedany w tej kolekturze");
        } else if (sprzedaneKupony.get(k)) {
            throw new OszustwoGracza("Ten kupon jest wykorzystany");
        }
        sprzedaneKupony.put(k, true);
        return CentralaTotolotka.getInstance().wyplac(k);
    }

    public void sprzedajKupon(GeneratorKuponow gk, Gracz g) throws OszustwoGracza, BladWyboruLosowania {
        if(!g.czyAktywny()){
            throw new OszustwoGracza("Wykryto wlamanie na konto innego gracza");
        }
        long cena = gk.cenaWygenerowaniaKuponu();
        if(!g.czyStarczy(cena)){
            throw new OszustwoGracza("Nie wsytarczajaca liczba środkow na zakup kuponu");
        }

        Kupon k = gk.wygenerujKupon(
                CentralaTotolotka.getInstance().numerKolejnegoLosowanie(),
                numer,
                this.wygenerujId()
        );
        CentralaTotolotka.getInstance().wprowadzKupon(k);
        sprzedaneKupony.put(k, false);
        g.wezKupon(k);
        g.zaplac(cena);
    }

    private String wygenerujId() {
        CentralaTotolotka centrala = CentralaTotolotka.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(centrala.liczbaSprzedanychKuponow() + 1);
        sb.append("-");
        sb.append(numer);
        sb.append("-");
        long losowyZnacznik = Losowe.losowyNumer((long) 1e8);
        sb.append(losowyZnacznik);
        sb.append("-");

        int sumaKontrolna = 0;
        sumaKontrolna += PomoczniczeFunkcje.sumaCyfr(numer);
        sumaKontrolna += PomoczniczeFunkcje.sumaCyfr(losowyZnacznik);
        sumaKontrolna += PomoczniczeFunkcje.sumaCyfr(centrala.liczbaSprzedanychKuponow() + 1);
        sumaKontrolna %= 100;
        sb.append(sumaKontrolna);
        return sb.toString();
    }
}
