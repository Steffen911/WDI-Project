package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.test;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.util.Utils;

public class TestKeepDescriptionWord {

    public static void main(String[] args) {

        String
        s1 = "Volkswagen VW Golf Automatik 1.8 TÜV 05/2017 golf",
        s2 = "Golf 3 1.6 golf",
        s3 = "Volkswagen Polo 1.6 TDI Comfortline polo",
        s4 = "Citroen Berlingo 1.6 HDi 110 FAP Panorama navi   Ahk  Tempomat berlingo",
        s5 = "Volkswagen King Kong Touareg 3.0 V6 TDI DPF Automatik touareg",
        s6 = "2 Trabbis Trabant Projektaufgabe Tausch o. Verkauf 601",
        s7 = "VW T4 Multivan 2  Sehr guter Zustand.. transporter";

        // Assumption: toLowerCase() during pre-processing, before keepDescriptionWords() is called.

        System.out.println(s1);
        System.out.println(Utils.keepDescriptionWords(s1.toLowerCase()));
        System.out.println();
        System.out.println(s2);
        System.out.println(Utils.keepDescriptionWords(s2.toLowerCase()));
        System.out.println();
        System.out.println(s3);
        System.out.println(Utils.keepDescriptionWords(s3.toLowerCase()));
        System.out.println();
        System.out.println(s4);
        System.out.println(Utils.keepDescriptionWords(s4.toLowerCase()));
        System.out.println();
        System.out.println(s5);
        System.out.println(Utils.keepDescriptionWords(s5.toLowerCase()));
        System.out.println();
        System.out.println(s6);
        System.out.println(Utils.keepDescriptionWords(s6.toLowerCase()));
        System.out.println();
        System.out.println(s7);
        System.out.println(Utils.keepDescriptionWords(s7.toLowerCase()));
    }
}
