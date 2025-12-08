package totolotek.gracz;

import java.util.*;

import totolotek.system.Blankiet;
import totolotek.system.CentralaTotolotka;
import totolotek.system.GeneratorKuponow;
import totolotek.system.Kupon;
import totolotek.wyjatki.NieprawidlowyBlankiet;
import totolotek.wyjatki.NieprawidlowyGracz;

public class GraczStaloliczbowy extends GraczStaly{

    public GraczStaloliczbowy(String imie, String nazwisko, String pesel, long środki, int[] ulubioneKolektury, int[] liczby)
            throws NieprawidlowyGracz{
        super(imie,nazwisko,pesel,środki,ulubioneKolektury);
        if (liczby == null || liczby.length != 6) {
            throw new NieprawidlowyGracz("Blad przy tworzeniu staloliczbowego: bledne podane liczby");
        }

        Set<Integer> unikalne = new HashSet<>();

        for (int liczba : liczby) {
            // Sprawdzenie zakresu
            if (liczba < 1 || liczba > 49) {
                throw new NieprawidlowyGracz("Blad przy tworzeniu staloliczbowego: bledne podane liczby");
            }
            // Sprawdzenie unikalności
            if (!unikalne.add(liczba)) {
                throw new NieprawidlowyGracz("Blad przy tworzeniu staloliczbowego: bledne podane liczby");
            }
        }
        try {
            ulubionyGenerator = new Blankiet(new int[][]{liczby,{},{},{},{},{},{},{}},
                    new boolean[] {false,true,true,true,true,true,true,true},
                    new int[]{10});
        }catch (NieprawidlowyBlankiet e){
            System.out.println("Blad systemu przy tworzeniu gracza staloliczbowego" + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public boolean czyMozna(GeneratorKuponow gk) {
        for(Kupon k : zakupioneKupony.keySet()){
            if(k.ostatnieLosowanie() >= CentralaTotolotka.getInstance().numerKolejnegoLosowanie()){
                return false;
            }
        }
        return czyStarczy(gk.cenaWygenerowaniaKuponu());
    }
}
