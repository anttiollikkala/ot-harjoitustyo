package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void saldoAlussaOikein() {
        assertTrue(kortti.saldo() == 10);
    }
    
    @Test
    public void lataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(10);
        assertTrue(kortti.saldo() == 20);
    }
    
    @Test
    public void saldoVaheneeOikeinJosRahaaOnTarpeeksi() {
        kortti.otaRahaa(5);
        assertTrue(kortti.saldo() == 5);
    }
    
    @Test
    public void saldoEiMuutuJosRahaaEiOleTarpeeksi() {
        kortti.otaRahaa(15);
        assertTrue(kortti.saldo() == 10);
    }
    
    @Test
    public void metodiPalauttaaTrueJosRahatRiittivat() {
        assertTrue(kortti.otaRahaa(5));
    }
    
    @Test
    public void metodiPalauttaaFalseJosRahatEivatRiittaneet() {
        assertTrue(!kortti.otaRahaa(15));
    }
    
    @Test
    public void korttiTulostaaRahamaaranOikein() {
        assertEquals(kortti.toString(), "saldo: 0.10");
    }
}
