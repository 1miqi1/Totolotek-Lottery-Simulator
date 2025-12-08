package totolotek.system;

import java.util.ArrayList;

import totolotek.pomocniczeklasy.Stale;
import totolotek.wyjatki.NieprawidlowyChybilTrafil;

public class ChybilTrafil extends GeneratorKuponow{
    private int liczbaZakladow;
    private int liczbaLosowan;

    public ChybilTrafil(int liczbaLosowan, int liczbaZakladow) throws NieprawidlowyChybilTrafil{
        if(liczbaZakladow <= 0 || liczbaZakladow > Stale.MAXLICZBAZAKLADOW ){
            throw new NieprawidlowyChybilTrafil("niepoprawna liczba zakladow");
        }
        if(liczbaLosowan <= 0 || liczbaLosowan > Stale.MAXLICZBALOSOWAN){
            throw new NieprawidlowyChybilTrafil("niepoprawan liczba losowan");
        }
        this.liczbaLosowan = liczbaLosowan;
        this.liczbaZakladow = liczbaZakladow;
    }

    @Override
    Kupon wygenerujKupon(int numerLosowania, int numerKolektury, String identyfikator) {
        ArrayList<Zaklad> zaklady = new ArrayList<>();
        for(int i = 0; i < liczbaZakladow; i++){
            zaklady.add(new Zaklad());
        }
        return new Kupon(zaklady, numerLosowania, liczbaLosowan, numerKolektury, identyfikator);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ChybilTrafil:\n");
        sb.append("Liczba zakladow: ")
        .append(liczbaZakladow)
        .append(", Liczba losowan: ")
        .append(liczbaLosowan);

        return sb.toString();
    }
}
