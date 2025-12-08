package totolotek.gracz;

import java.util.HashMap;

import totolotek.system.CentralaTotolotka;
import totolotek.system.GeneratorKuponow;
import totolotek.system.Kolektura;
import totolotek.system.Kupon;
import totolotek.wyjatki.BladWyboruLosowania;
import totolotek.wyjatki.OszustwoGracza;
import totolotek.wyjatki.ZlaKolektura;

public abstract class Gracz {
    protected final String imie;
    protected final String nazwisko;
    protected final String pesel;
    protected boolean czyAktywny;
    protected long środki;
    protected HashMap<Kupon,Boolean> zakupioneKupony;

    protected Gracz(String imie, String nazwisko, String pesel, long środki) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
        this.czyAktywny = false;
        this.zakupioneKupony = new HashMap<>();
        this.środki = środki;
    }

    public boolean czyAktywny(){
        return czyAktywny;
    }

    public boolean czyStarczy(long wartośc){
        return środki >= wartośc;
    }

    public boolean czyMozna(GeneratorKuponow gk){
        return czyStarczy(gk.cenaWygenerowaniaKuponu());
    }

    public long zaplac(long cena) throws OszustwoGracza {
        if(cena > środki){
            throw new OszustwoGracza("Brak Środkow");
        }
        środki -= cena;
        return cena;
    }

    public void wezKupon(Kupon kupon){
        zakupioneKupony.put(kupon,true);
    }

    public void wykorzystajKupon(Kupon k){
        if(zakupioneKupony.containsKey(k) && zakupioneKupony.get(k)){
            try {
                środki += k.gdzieWydany().wyplac(k);
                zakupioneKupony.put(k,false);
            } catch (OszustwoGracza | BladWyboruLosowania e) {
                System.out.println("Blad systemu przy wykorzystwaniu kuponu" + e.getMessage());
            }
        }
    }

    public void kupKupon(GeneratorKuponow gk, int numerKolektury) throws ZlaKolektura {
        this.czyAktywny = true;
        try{
            if(czyStarczy(gk.cenaWygenerowaniaKuponu())){
                Kolektura k = CentralaTotolotka.getInstance().dajKolekture(numerKolektury);
                k.sprzedajKupon(gk,this);
            }
        }catch (OszustwoGracza | BladWyboruLosowania e){
            System.out.println("Blad system" + e.getMessage());
            System.out.println(e.getMessage());
        }
        this.czyAktywny = false;
    }

    public void dzialajOdbierz(){
        for(Kupon k : zakupioneKupony.keySet()){
            if(k.ostatnieLosowanie() < CentralaTotolotka.getInstance().numerKolejnegoLosowanie()){
                wykorzystajKupon(k);
            }
        }
    }

    public abstract void dzialajKup();

    public void napiszOSobie(){
        System.out.println("Imie i Nazwisko: " + imie + " " + nazwisko + " Pesel: " + pesel + "Środki: " + środki);
    }

    public long środki(){
        return środki;
    }

    public long liczbaKupionychKuponow(){
        return zakupioneKupony.size();
    }

    public void wypiszIdentyfikatory(){
        if(!zakupioneKupony.keySet().isEmpty()){
            System.out.println("Identyfikatory zakupionych kuponow");
            for(Kupon k : zakupioneKupony.keySet()){
                System.out.println(k.id());
            }
        }
        else{
            System.out.println("Brak zakupionych kuponow");
        }
    }
}
