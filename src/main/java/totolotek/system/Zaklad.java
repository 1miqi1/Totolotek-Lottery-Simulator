package totolotek.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import totolotek.pomocniczeklasy.Stale;
import totolotek.wyjatki.BlednyZaklad;

public class Zaklad {
    private final TreeSet<Integer> liczby;

    public Zaklad(int[] t){
        liczby = new TreeSet<>();
        for (int i = 0; i < t.length; i++) {
            if(t[i] <= 0 || t[i] > Stale.LICZBANUMEROW){
                throw new BlednyZaklad("Bledne liczby przy tworzeniu zakladu");
            }
            liczby.add(t[i]);
        }
        if(liczby.size() != 6){
            throw new BlednyZaklad("Bledne liczby przy tworzeniu zakladu");
        }
    }

    Zaklad() {
        List<Integer> wszystkie = new ArrayList<>();
        for (int i = 1; i <= Stale.LICZBANUMEROW; i++) {
            wszystkie.add(i);
        }
        Collections.shuffle(wszystkie);
        liczby = new TreeSet<>(wszystkie.subList(0, Stale.DLUGOSCZAKLADUU));
    }

    int[] liczby() {
        int[] r = new int[6];
        int j = 0;
        for(int i : liczby){
            r[j] = i;
            j++;
        }
        return r;
    }

    public int stopienWygranej(Zaklad z) {
        TreeSet<Integer> przeciecie = new TreeSet<>(liczby);
        przeciecie.retainAll(z.liczby);
        int stopien = 7 - przeciecie.size();
        return stopien <= 4 ? stopien : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : liczby) {
            sb.append(String.format("%2d ", i));
        }
        return sb.toString();
    }
}
