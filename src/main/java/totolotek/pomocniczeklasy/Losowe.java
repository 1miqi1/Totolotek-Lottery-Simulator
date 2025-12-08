package totolotek.pomocniczeklasy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import totolotek.gracz.GraczLosowy;
import totolotek.gracz.GraczMinimalista;
import totolotek.gracz.GraczStaloblankietowy;
import totolotek.gracz.GraczStaloliczbowy;
import totolotek.system.Blankiet;
import totolotek.system.CentralaTotolotka;
import totolotek.wyjatki.NieprawidlowyBlankiet;
import totolotek.wyjatki.NieprawidlowyGracz;

public class Losowe {
    public static long losowyNumer(long dlugosc) {
        Random random = new Random();
        return 1 + random.nextLong(dlugosc);
    }

    public static String losowyNapis(int length) {
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(letters.length());
            sb.append(letters.charAt(index));
        }

        return sb.toString();
    }

    public static Blankiet prawidlowyLosowyBlankiet(){
        try {
            Random random = new Random();

            int liczbaZakladow = Stale.MAXLICZBAZAKLADOW;
            int[][] liczby = new int[liczbaZakladow][];
            boolean[] anuluj = new boolean[liczbaZakladow];

            for (int i = 0; i < liczbaZakladow; i++) {
                // Losuj unikalne liczby w zakladzie
                Set<Integer> zestaw = new TreeSet<>();
                while (zestaw.size() < Stale.DLUGOSCZAKLADUU) {
                    int liczba = 1 + random.nextInt(Stale.LICZBANUMEROW);
                    zestaw.add(liczba);
                }

                int[] zaklad = zestaw.stream().mapToInt(Integer::intValue).toArray();
                liczby[i] = zaklad;

                anuluj[i] = random.nextBoolean();
            }

            int[] liczbaLosowan = new int[]{1+ random.nextInt(Stale.MAXLICZBALOSOWAN)};
            return new Blankiet(liczby, anuluj, liczbaLosowan);
        }catch (NieprawidlowyBlankiet e) {
            System.out.println("Blad systemu przy tworzeniu losowego blankietu" + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    public static int[] losowaTablica(int k, int dlugosc) {
        if (k <= 0 || dlugosc <= 0) {
            throw new IllegalArgumentException("k i dlugosc musza byc wieksze od 0");
        }
        if (dlugosc > k) {
            throw new IllegalArgumentException("Nie mozna wygenerowac " + dlugosc + " roznych liczb z zakresu 1-" + k);
        }

        Random rand = new Random();

        List<Integer> liczby = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            liczby.add(i);
        }

        Collections.shuffle(liczby, rand);

        int[] tablica = new int[dlugosc];
        for (int i = 0; i < dlugosc; i++) {
            tablica[i] = liczby.get(i);
        }

        return tablica;
    }

    public static GraczLosowy losowyPoprawnyGraczLosowy(){
        String imie = losowyNapis(5);
        String nazwisko = losowyNapis(5);
        String pesel = losowyNapis(5);
        return new GraczLosowy(imie,nazwisko,pesel);
    }

    public static GraczMinimalista losowyPoprawnyGraczMinimalista(){
        int k = CentralaTotolotka.getInstance().liczbaKolektur();
        int kolektura = Math.toIntExact(losowyNumer(k));
        String imie = losowyNapis(5);
        String nazwisko = losowyNapis(5);
        String pesel = losowyNapis(5);
        long środki = losowyNumer((long)2e6);
        return new GraczMinimalista(imie, nazwisko, pesel, środki ,kolektura);
    }

    public static GraczStaloliczbowy losowyPoprawnyGraczStaloliczbowy(){
        try{
            int k = CentralaTotolotka.getInstance().liczbaKolektur();
            String imie = losowyNapis(5);
            String nazwisko = losowyNapis(5);
            String pesel = losowyNapis(5);
            long środki = losowyNumer((long)2e6);
            int[] liczby = losowaTablica(49,6);
            return new GraczStaloliczbowy(imie, nazwisko, pesel, środki, losowaTablica(k,k/4), liczby);
        } catch (NieprawidlowyGracz e) {
            System.out.println("Blad systemu " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    public static GraczStaloblankietowy losowyPoprawnyGraczStaloblankietowy() {
        try {
            int k = CentralaTotolotka.getInstance().liczbaKolektur();
            String imie = losowyNapis(5);
            String nazwisko = losowyNapis(5);
            String pesel = losowyNapis(5);
            long środki = losowyNumer((long) 2e6);
            int coIle = Math.toIntExact(1 + losowyNumer(5));
            return new GraczStaloblankietowy(imie, nazwisko, pesel, środki, losowaTablica(k, k / 4), prawidlowyLosowyBlankiet(), coIle);
        } catch (NieprawidlowyGracz e) {
            System.out.println("Blad systemu przy tworzeniu GraczaSB " + e.getMessage());
            System.exit(0);
        }
        return null;
    }
}

