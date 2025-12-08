package totolotek.pomocniczeklasy;

public class PomoczniczeFunkcje {
    public static String zlote(long a){
        long zlote = a / 100;
        long grosze = a % 100;
        return zlote + " zl " + String.format("%02d", grosze) + " gr";
    }

    public static int sumaCyfr(long a){
        int suma = 0;
        while( a > 1){
            suma += a % 10;
            a /= 10;
        }
        return suma;
    }
}
