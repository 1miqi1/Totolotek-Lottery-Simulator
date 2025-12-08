package totolotek.system;

import java.util.ArrayList;
import java.util.Objects;

import totolotek.pomocniczeklasy.PomoczniczeFunkcje;
import totolotek.pomocniczeklasy.Stale;
import totolotek.wyjatki.ZlaKolektura;

public class Kupon {
    private final ArrayList<Zaklad> zaklady;
    private final int pierwszeLosowanie;
    private final int liczbaLosowan;
    private final int numerKolektury;
    private final String id;

    Kupon (ArrayList<Zaklad> zaklady,int pierwszeLosowanie, int liczbaLosowan, int numerKolektury, String id) {
        this.zaklady = zaklady;
        this.pierwszeLosowanie = pierwszeLosowanie;
        this.liczbaLosowan = liczbaLosowan;
        this.numerKolektury = numerKolektury;
        this.id = id;
    }

    ArrayList<Zaklad> zaklady(){
        return zaklady;
    }

    public int pierwszeLosowanie(){
        return pierwszeLosowanie;
    }

    public int ostatnieLosowanie(){
        return pierwszeLosowanie + liczbaLosowan - 1;
    }

    public int numerKolektury() {
        return numerKolektury;
    }

    public Kolektura gdzieWydany(){
        try{
            return CentralaTotolotka.getInstance().dajKolekture(numerKolektury());
        }catch (ZlaKolektura e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return null;
    }

    public int liczbaLosowan(){
        return liczbaLosowan;
    }

    public String id(){
        return id;
    }

    public long podatek(){
        return zaklady.size() * liczbaLosowan * Stale.PODATEKODZAKLDAU;
    }

    public long cena(){
        return zaklady.size() * liczbaLosowan * Stale.CENAZAKLADU;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Kupon other = (Kupon) obj;

        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        String[] cześci = id.split("-");
        return Integer.parseInt(cześci[1]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("KUPON NR ").append(id).append("\n");

        int i = 1;
        for (Zaklad zaklad : zaklady) {
            sb.append(String.format("%d: %s\n", i++, zaklad));
        }

        sb.append(String.format("LICZBA LOSOWAn: %d\n", liczbaLosowan));
        sb.append("NUMERY LOSOWAn:\n");
        for (int j = 0; j < liczbaLosowan; j++) {
            sb.append(String.format("%d ", pierwszeLosowanie + j));
        }
        sb.append("\n");
        sb.append("CENA: ").append(PomoczniczeFunkcje.zlote(this.cena()));

        return sb.toString();
    }
}
