package totolotek;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import totolotek.gracz.GraczMinimalista;
import totolotek.system.Blankiet;
import totolotek.system.BudzetPanstwa;
import totolotek.system.CentralaTotolotka;
import totolotek.system.ChybilTrafil;
import totolotek.system.Zaklad;
import totolotek.wyjatki.BladWyboruLosowania;
import totolotek.wyjatki.NieprawidlowyBlankiet;
import totolotek.wyjatki.NieprawidlowyChybilTrafil;

public class Testy {
    @Test
    void stopienWygranej() {
        Zaklad wygrywajacy = new Zaklad(new int[]{1, 2, 3, 4, 5, 6});

        // Testowane przypadki
        Zaklad trafione6 = new Zaklad(new int[]{1, 2, 3, 4, 5, 6}); // 6 trafien -> stopien 1
        Zaklad trafione5 = new Zaklad(new int[]{1, 2, 3, 4, 5, 10}); // 5 trafien -> stopien 2
        Zaklad trafione4 = new Zaklad(new int[]{1, 2, 3, 4, 11, 12}); // 4 trafienia -> stopien 3
        Zaklad trafione3 = new Zaklad(new int[]{1, 2, 3, 13, 14, 15}); // 3 trafienia -> stopien 4
        Zaklad trafione2 = new Zaklad(new int[]{1, 2, 20, 21, 22, 23}); // 2 trafienia -> stopien 0
        Zaklad trafione0 = new Zaklad(new int[]{10, 11, 12, 13, 14, 15}); // 0 trafien -> stopien 0

        assertEquals(1, wygrywajacy.stopienWygranej(trafione6), "6 trafien powinno dac stopien 1.");
        assertEquals(2, wygrywajacy.stopienWygranej(trafione5), "5 trafien powinno dac stopien 2.");
        assertEquals(3, wygrywajacy.stopienWygranej(trafione4), "4 trafienia powinno dac stopien 3.");
        assertEquals(4, wygrywajacy.stopienWygranej(trafione3), "3 trafienia powinno dac stopien 4.");
        assertEquals(0, wygrywajacy.stopienWygranej(trafione2), "2 trafienia powinny dac stopien 0.");
        assertEquals(0, wygrywajacy.stopienWygranej(trafione0), "0 trafien powinno dac stopien 0.");
    }

    @Test
    void TestGeneratoraKuponow() throws NieprawidlowyChybilTrafil, NieprawidlowyBlankiet {
        ChybilTrafil ch = new ChybilTrafil(1,1);
        assertEquals(300, ch.cenaWygenerowaniaKuponu());

        int[][] zapis= new int[8][];
        boolean[] anuluj = new boolean[8];
        for (int i = 1; i <= 7; i++) {
            zapis[i] = new int[]{1};
            anuluj[i] = true;
        }
        anuluj[0] = false;
        zapis[0]= new int[]{1,2,3,4,5,6};
        Blankiet bk = new Blankiet(zapis,anuluj, new int[]{2,3,4});
        assertEquals(1200, bk.cenaWygenerowaniaKuponu());
    }

    @Test
    void testSystemu() throws BladWyboruLosowania {
        CentralaTotolotka.getInstance().utworzKolekture();
        GraczMinimalista g  = new GraczMinimalista("A","B","C",300*5,1);
        g.dzialajKup();
        g.dzialajKup();
        g.dzialajKup();
        g.dzialajKup();
        g.dzialajKup();
        assertEquals(0,g.środki());
        g.dzialajKup();
        assertEquals(0,g.środki());
        assertEquals(5,g.liczbaKupionychKuponow());

        assertEquals(60*5,BudzetPanstwa.getInstance().dajPodatki());

        CentralaTotolotka.getInstance().przeprowadzLosowanie();
        long pula = 0;
        for(int i = 1; i <= 4; i++) {
            pula += CentralaTotolotka.getInstance().wygranaStopnia(1,i);
        }
        g.dzialajOdbierz();
        assertEquals(pula,g.środki());
    }
}
