package fr.flux;

public class Format {

    private Format() {};

    static String octets(long n) {
        String[] unites = {"o", "Ko", "Mo", "Go", "To"};
        double valeur = n;
        int i = 0;
        while (valeur >= 1024 && i < unites.length - 1) {
            valeur /= 1024;
            i++;
        }
        return i == 0 ? n + " o" : String.format("%.1f %s", valeur, unites[i]);
    }

}
