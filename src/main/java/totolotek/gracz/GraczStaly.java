package totolotek.gracz;

import totolotek.system.CentralaTotolotka;
import totolotek.system.GeneratorKuponow;
import totolotek.wyjatki.NieprawidlowyGracz;
import totolotek.wyjatki.ZlaKolektura;

public abstract class GraczStaly extends Gracz {
    private int akkKolektura;
    private final int[] ulubioneKolektury;
    protected GeneratorKuponow ulubionyGenerator;

    public GraczStaly(String imie, String nazwisko, String pesel, long środki,  int[] ulubioneKolektury) throws NieprawidlowyGracz {
        super(imie, nazwisko, pesel, środki);
        akkKolektura = 0;
        this.ulubioneKolektury = ulubioneKolektury;
        for(int i = 0; i < ulubioneKolektury.length; i++) {
            if(ulubioneKolektury[i] < 1 || ulubioneKolektury[i] > CentralaTotolotka.getInstance().liczbaKolektur()){
                throw new NieprawidlowyGracz("Nieprawidlowy zbior ulubionych kolektur");
            }
        }
    }

    @Override
    public void dzialajKup(){
        try {
            if(this.czyMozna(ulubionyGenerator)){
                this.kupKupon(ulubionyGenerator,ulubioneKolektury[akkKolektura]);
                akkKolektura++;
                akkKolektura %= ulubioneKolektury.length;
            }
        }catch (ZlaKolektura e){
            System.out.println("Blad systemu przy dzialajKup" + e.getMessage());
            System.exit(0);
        }
    }
}
