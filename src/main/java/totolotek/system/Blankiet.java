package totolotek.system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import totolotek.pomocniczeklasy.Stale;
import totolotek.wyjatki.NieprawidlowyBlankiet;

public class Blankiet extends GeneratorKuponow {
    private final int[][] liczby;
    private final boolean[] anuluj;
    private final int[] liczbaLosowan;

    public Blankiet(int[][] liczby, boolean[] anuluj, int[] liczbaLosowan) throws NieprawidlowyBlankiet{
        if(liczby.length !=  Stale.MAXLICZBAZAKLADOW) {
            throw new NieprawidlowyBlankiet("nieprawidlowa liczba zakladow");
        }
        if(anuluj.length != Stale.MAXLICZBAZAKLADOW) {
            throw new NieprawidlowyBlankiet("nieprawidlowa liczba anuluj");
        }
        for(int i : liczbaLosowan){
            if( i > Stale.MAXLICZBALOSOWAN || i <= 0){
                throw new NieprawidlowyBlankiet("nieprawidlowa liczba losowan");
            }
        }
        for(int i = 0 ; i < liczby.length ; i++){
            for(int j = 0; i < liczby[i].length ; i++ ){
                if(liczby[i][j] <=0 || liczby[i][j] > Stale.LICZBANUMEROW){
                    throw new NieprawidlowyBlankiet("nieprawidlowy numer");
                }
            }
        }
        this.liczby = liczby;
        this.anuluj = anuluj;
        this.liczbaLosowan = liczbaLosowan;
    }

    @Override
    Kupon wygenerujKupon(int numerLosowania, int numerKolektury, String identyfikator) {
        ArrayList<Zaklad> zaklady = new ArrayList<>();
        for(int i = 0; i < Stale.MAXLICZBAZAKLADOW; i++){
            Set<Integer> unikalne = new HashSet<>();
            for (int j = 0; j < liczby[i].length ; j++) {
                unikalne.add(liczby[i][j]);
            }
            if(unikalne.size() == Stale.DLUGOSCZAKLADUU || !anuluj[i]){
                zaklady.add(new Zaklad(liczby[i]));
            }
        }
        int n = 1;
        for(int i : liczbaLosowan){
            n = Math.max(n,i);
        }
        return new Kupon(zaklady,numerLosowania, n, numerKolektury, identyfikator);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Blankiet:\n");
        for (int z = 0; z < liczby.length; z++) {
            sb.append(z + 1).append("\n");
            boolean[] zaznaczone = new boolean[Stale.LICZBANUMEROW + 1];
            for (int n : liczby[z]) {
                if (n >= 1 && n <= Stale.LICZBANUMEROW) {
                    zaznaczone[n] = true;
                }
            }

            for (int wiersz = 0; wiersz < 5; wiersz++) {
                for (int kol = 1; kol <= 10; kol++) {
                    int num = wiersz * 10 + kol;
                    if (num > Stale.LICZBANUMEROW) break;
                    if (!zaznaczone[num]) {
                        sb.append(String.format("[ %2d ] ", num));
                    } else {
                        sb.append("[ -- ] ");
                    }
                }
                sb.append("\n");
            }

            if (anuluj[z]) {
                sb.append("[ -- ] anuluj\n");
            } else {
                sb.append("[    ] anuluj\n");
            }
        }

        sb.append("Liczba losowan: ");
        for (int i = 1; i <= Stale.MAXLICZBALOSOWAN; i++) {
            for (int k : liczbaLosowan) {
                if (i == k) {
                    sb.append("[ -- ] ");
                } else {
                    sb.append(String.format("[ %2d ] ", i));
                }
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
