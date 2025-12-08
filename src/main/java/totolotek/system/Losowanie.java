package totolotek.system;

import java.util.ArrayList;

import totolotek.pomocniczeklasy.PomoczniczeFunkcje;
import totolotek.wyjatki.BladWyboruLosowania;


class Losowanie {
    private final int numerLosowania;
    private long pulaNagrod;
    private final long[] pulaStopnia;
    private final long[] liczbaWygranych;
    private Zaklad zwycieskaSzostka;
    private final ArrayList<Zaklad> zaklady;
    private boolean czyLosowanieOdbyte = false;

    private static final long[] MINIMALNE = new long[]{0, (long) 2e8, 0, 3600, 2400 };

    Losowanie(int numerLosowania) {
        this.numerLosowania = numerLosowania;
        this.pulaNagrod = 0;
        this.pulaStopnia = new long[]{0,0,0,0,0};
        this.liczbaWygranych = new long[]{0,0,0,0,0};
        this.zaklady = new ArrayList<>();
    }

    void dodajZaklady(ArrayList<Zaklad> z) throws BladWyboruLosowania {
        if(czyLosowanieOdbyte){
            throw new BladWyboruLosowania("nie mozna dodac zakladow");
        }
        zaklady.addAll(z);
    }

    void zwiekszPule(long wartośc) throws BladWyboruLosowania {
        if(czyLosowanieOdbyte){
            throw new BladWyboruLosowania("nie mozna zwiekszyś puli");
        }
        pulaNagrod += wartośc;
    }

    long[] pulaStopnia() throws BladWyboruLosowania {
        if(!czyLosowanieOdbyte){
            throw new BladWyboruLosowania(numerLosowania);
        }
        return pulaStopnia;
    }

    Zaklad zwycieskaSzostka() throws BladWyboruLosowania {
        if(!czyLosowanieOdbyte){
            throw new BladWyboruLosowania(numerLosowania);
        }
        return zwycieskaSzostka;
    }

    void kumulacja(long a){
        pulaStopnia[1] += a;
    }

    long wygrana(int stopien) throws BladWyboruLosowania {
        if(!czyLosowanieOdbyte){
            throw new BladWyboruLosowania(numerLosowania);
        }
        return liczbaWygranych[stopien] == 0 ? 0 : pulaStopnia[stopien]/liczbaWygranych[stopien];
    }

    long wygrana(Zaklad z) throws BladWyboruLosowania {
        return wygrana(z.stopienWygranej(zwycieskaSzostka));
    }

    void losuj() throws BladWyboruLosowania {
        if(czyLosowanieOdbyte){
            throw new BladWyboruLosowania("nie mozna drugi raz losowac");
        }
        zwycieskaSzostka = new Zaklad();
        wyznaczNagrody();
        czyLosowanieOdbyte = true;
        for(int i = 2; i <= 3; i++){
            if(liczbaWygranych[i] == 0){
                CentralaTotolotka.getInstance().zwiekszPuleNastepnegoLosowania(pulaStopnia[i]);
            }
        }
        System.out.println(this);
    }

    private void wyznaczNagrody() throws BladWyboruLosowania {
        if(czyLosowanieOdbyte){
            throw new BladWyboruLosowania("nie mozna drugi wyznaczac nagrod");
        }
        for(Zaklad z : zaklady){
            liczbaWygranych[z.stopienWygranej(zwycieskaSzostka)]++;
        }
        long pozostaleNagrody = pulaNagrod;
        pulaStopnia[1] += (pulaNagrod * 44) / 100;
        if(liczbaWygranych[1] == 0){
            CentralaTotolotka.getInstance().kumulacja(pulaStopnia[1]);
        }
        pozostaleNagrody -= pulaStopnia[1];
        pulaStopnia[1] = Long.max(pulaStopnia[1], MINIMALNE[1] );

        pulaStopnia[2] = (pulaNagrod * 8) / 100;
        pozostaleNagrody -= pulaStopnia[2];

        pulaStopnia[3] = Long.max(pozostaleNagrody, MINIMALNE[3] * liczbaWygranych[3] );

        pulaStopnia[4] = MINIMALNE[4] * liczbaWygranych[4];
    }

    void sprawozdanie() throws BladWyboruLosowania {
        if (!czyLosowanieOdbyte) {
            throw new BladWyboruLosowania(numerLosowania);
        }

        System.out.println("Losowanie nr " + numerLosowania);

        for (int i = 1; i <= 4; i++) {
            StringBuilder sb = new StringBuilder();

            sb.append("Pula ").append(i)
            .append(" Liczba trafien: ").append(liczbaWygranych[i])
            .append(" Pula: ").append(PomoczniczeFunkcje.zlote(pulaStopnia[i]));

            if (liczbaWygranych[i] != 0) {
                sb.append(" Kwota Wygrana: ")
                .append(PomoczniczeFunkcje.zlote(wygrana(i)));
            }

            System.out.println(sb);
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Losowanie nr ").append(numerLosowania).append("\n");

        if (czyLosowanieOdbyte) {
            sb.append("Wynik: ").append(zwycieskaSzostka);
        }

        return sb.toString();
    }

}
