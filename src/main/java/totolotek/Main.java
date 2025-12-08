package totolotek;

import java.util.ArrayList;

import totolotek.gracz.Gracz;
import totolotek.pomocniczeklasy.Losowe;
import totolotek.system.BudzetPanstwa;
import totolotek.system.CentralaTotolotka;
import totolotek.system.Kolektura;
import totolotek.wyjatki.BladWyboruLosowania;

public class Main {
    public static void main(String[] args) throws BladWyboruLosowania {
        ArrayList<Kolektura> kolektury = new ArrayList<>();
        ArrayList<Gracz> gracze = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            kolektury.add(CentralaTotolotka.getInstance().utworzKolekture());
        }
        for(int i = 0; i < 200; i++){
            gracze.add(Losowe.losowyPoprawnyGraczLosowy());
            gracze.add(Losowe.losowyPoprawnyGraczStaloliczbowy());
            gracze.add(Losowe.losowyPoprawnyGraczStaloblankietowy());
            gracze.add(Losowe.losowyPoprawnyGraczMinimalista());
        }
        for(int i = 0; i < 20; i++){
            for(Gracz g : gracze){
                g.dzialajKup();
            }
            CentralaTotolotka.getInstance().przeprowadzLosowanie();
            for(Gracz g : gracze){
                g.dzialajOdbierz();
            }
        }

        for(int i = 0; i < 20; i++){
            CentralaTotolotka.getInstance().sprawozdanie(i+1);
        }
        BudzetPanstwa.getInstance().wypiszPodatek();
        BudzetPanstwa.getInstance().wypiszSubsydia();
    }
}
