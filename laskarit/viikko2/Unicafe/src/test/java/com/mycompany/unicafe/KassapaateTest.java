100000/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anttiollikkala
 */
public class KassapaateTest {
    
    Kassapaate kassapaate;
        
    @Before
    public void setUp() {
        kassapaate = new Kassapaate();
    }
    
    @Test
    public void uudessaKassapaatteessaOikeaMaaraRahaa() {
        assertTrue(kassapaate.kassassaRahaa() == 100000);      
    }

    @Test
    public void uudessaKassapaatteessaOikeaMaaraLounaita() {
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0);   
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 0);  
    }
    
    @Test
    public void kateisOstoRahaRiittaaJaRahaaPalautetaanToimii() {
        assertTrue(kassapaate.syoEdullisesti(300) == 60);
        assertTrue(kassapaate.kassassaRahaa() == 100240);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 1);
    }
    
    @Test
    public void kateisOstoRahaEiRiittaJaRahaPalautetaanKokonaanToimii() {
        assertTrue(kassapaate.syoEdullisesti(200) == 200);
        assertTrue(kassapaate.kassassaRahaa() == 100000);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0);
    }
    
    @Test
    public void kateisOstoRahaRiittaaJaRahaaPalautetaanToimii2() {
        assertTrue(kassapaate.syoMaukkaasti(460) == 60);
        assertTrue(kassapaate.kassassaRahaa() == 100400);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 1);
    }
    
    @Test
    public void kateisOstoRahaEiRiittaJaRahaPalautetaanKokonaanToimii2() {
        assertTrue(kassapaate.syoMaukkaasti(360) == 360);
        assertTrue(kassapaate.kassassaRahaa() == 100000);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty()  == 0);
    }
    
    @Test
    public void korttiostoRahaRiittaaToimiiMaukas() {
        Maksukortti kortti = new Maksukortti(1000);
        assertTrue(kassapaate.syoMaukkaasti(kortti));
        assertTrue(kortti.saldo() == 600);
        assertTrue(kassapaate.kassassaRahaa() == 100000);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 1);
    }
    
    @Test
    public void korttiostoRahaRiittaaToimiiEdullinen() {
        Maksukortti kortti = new Maksukortti(1000);
        assertTrue(kassapaate.syoEdullisesti(kortti));
        assertTrue(kortti.saldo() == 760);
        assertTrue(kassapaate.kassassaRahaa() == 100000);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 1);
    }
    
    @Test
    public void korttiostoRahaEiRiittaToimiiMaukas() {
        Maksukortti kortti = new Maksukortti(300);
        assertTrue(!kassapaate.syoMaukkaasti(kortti));
        assertTrue(kortti.saldo() == 300);
        assertTrue(kassapaate.kassassaRahaa() == 100000);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 0);
    }
  
    @Test
    public void korttiostoRahaEiRiittaToimiiEdullinen() {
        Maksukortti kortti = new Maksukortti(200);
        assertTrue(!kassapaate.syoEdullisesti(kortti));
        assertTrue(kortti.saldo() == 200);
        assertTrue(kassapaate.kassassaRahaa() == 100000);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0);
    }
    
    @Test
    public void kortinLatausToimii() {
        Maksukortti kortti = new Maksukortti(0);
        kassapaate.lataaRahaaKortille(kortti, 500);
        assertTrue(kortti.saldo() == 500);
        assertTrue(kassapaate.kassassaRahaa() == 100500);
    }
    
    @Test
    public void kortilleEiVoiLadataNegatiivisiaSummia() {
        Maksukortti kortti = new Maksukortti(0);
        kassapaate.lataaRahaaKortille(kortti, -500);
        assertTrue(kortti.saldo() == 0);
        assertTrue(kassapaate.kassassaRahaa() == 100000);
    }
}
