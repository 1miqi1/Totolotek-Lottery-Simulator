package totolotek.gracz;

import totolotek.system.ChybilTrafil;
import totolotek.wyjatki.NieprawidlowyChybilTrafil;
import totolotek.wyjatki.NieprawidlowyGracz;

public class GraczMinimalista extends GraczStaly{
    public GraczMinimalista(String imie, String nazwisko, String pesel,long środki, int numKolektury) throws NieprawidlowyGracz{
        super(imie,nazwisko, pesel, środki, new int[]{numKolektury});
        try {
            this.ulubionyGenerator = new ChybilTrafil(1,1);
        }catch (NieprawidlowyChybilTrafil e){
            System.out.println("Blad systemu przy tworzeniu gracza Minimalisty" + e.getMessage());
            System.exit(0);
        }
    }
}
