package totolotek.gracz;

import totolotek.system.Blankiet;
import totolotek.system.CentralaTotolotka;
import totolotek.system.GeneratorKuponow;
import totolotek.system.Kupon;
import totolotek.wyjatki.NieprawidlowyGracz;

public class GraczStaloblankietowy extends GraczStaly {
    private final int coIle;
    public GraczStaloblankietowy(String imie, String nazwisko, String pesel, long środki,  int[] ulubioneKolektury,
                                 Blankiet b, int coIle) throws NieprawidlowyGracz {
        super(imie,nazwisko,pesel,środki,ulubioneKolektury);
        this.ulubionyGenerator = b;
        if(coIle <= 0){
            throw new NieprawidlowyGracz("Bład przy tworzeniu stałoblankietowego: " +
                    "zle podana liczba losowan, co ktora SB kupuje kupon");
        }
        this.coIle = coIle;
    }

    @Override
    public boolean czyMozna(GeneratorKuponow gk){
        int ostatni = 0;
        for(Kupon k : zakupioneKupony.keySet()){
            ostatni = Math.max(k.pierwszeLosowanie(),ostatni);
        }
        if(CentralaTotolotka.getInstance().numerKolejnegoLosowanie() - ostatni != coIle){
            return false;
        }
        return czyStarczy(gk.cenaWygenerowaniaKuponu());
    }
}
