package totolotek.gracz;

import totolotek.pomocniczeklasy.Losowe;
import totolotek.system.CentralaTotolotka;
import totolotek.system.ChybilTrafil;
import totolotek.wyjatki.NieprawidlowyChybilTrafil;
import totolotek.wyjatki.ZlaKolektura;

public class GraczLosowy extends Gracz {
    public GraczLosowy(String imie, String nazwisko, String pesel) {
        super(imie, nazwisko, pesel, 0);
        środki = Losowe.losowyNumer((long)1e8-1);
    }

    @Override
    public void dzialajKup(){
        int numerKolektury = Math.toIntExact(Losowe.losowyNumer(CentralaTotolotka.getInstance().liczbaKolektur()));
        int liczbaKuponow = Math.toIntExact(Losowe.losowyNumer(100));
        try {
            for(int i = 0; i < liczbaKuponow; i++){
                ChybilTrafil c = new ChybilTrafil(Math.toIntExact(Losowe.losowyNumer(10)),
                        Math.toIntExact(Losowe.losowyNumer(8)));
                if(!czyStarczy(c.cenaWygenerowaniaKuponu())){
                    break;
                }else{
                    kupKupon(c, numerKolektury);
                }
            }
        } catch (ZlaKolektura | NieprawidlowyChybilTrafil e) {
            System.out.println("Blad systemu przy kupowaniu przez gracza losowego: " + e.getMessage());
            System.exit(0);
        }
    }
}
