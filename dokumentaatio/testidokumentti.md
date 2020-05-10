# Testausdokumentti

Sovellukseen on tehty automaattiset yksikkö-, sekä integraatiotestit JUnit kirjastoa hyväksikäyttäen. 
Järjestelmätason testit on suoritettu manuaalisesti katsoen että kaikki sovelluksen ominaisuuden toimivat tarkoituksenmukaisella tavalla. Käyttöliittymän testaus on jätetty pois kaikista automaattisista testeistä ja testikattavuuden raportista.

## Yksikkötestaukset

Sovelluksen yksikkötestaukset kohdistuu kaikkiin DAO-luokkiin, sekä muutamassa tapauksessa myös entiteettejä kuvaaviin luokkiin, mutta entiteettien testaus laajasti ei ole tarkoituksenmukaista, sillä ne eivät sisällä kuin hyvin yksinkertaista logiikkaa, kuten settereitä ja gettereitä. DAO-luokkia testatessa luodaan uusi tietokanta testien alussa ja tietokantatiedosto poistetaan testien lopuksi.

## Integraatiotestaus

Service luokkien kattavalla testauksella on saavutettu sopivat integraatiotestauksen taso. Service luokkien pääasiallinen tarkoitus järjestelmässä on yhdistellä dataa käyttäen eri tietolähteitä ja luokkia (muut service-luokat & DAO-luokat), niin niiden toiminnan testaaminen on vääjäämättä testannut luokkien yhteistoimintaa. Service luokkien automaattisia testejä varten järjestelmä luo myös testitietokannat, joiden tiedostot se poistaa testien päätyttyä.

## Järjestelmätestit
Sovelluksen kehityksen eri vaiheissa järjstelmästä on varmistettu seuraavien ominaisuuksien toiminta:
- Koulun pystyy luomaan
- Pystyy kirjautumaan minkä tahansa roolin tunnuksilla
- Kurssilistaussivulla tiedot pitävät paikkansa ja päivittyvät
- Kurssin pystyy lisäämään, mutta vajailla tai virheellisillä tiedoilla antaa varoituksen
- Opiskelijalistauksen ja opiskelijasivun tiedot pitävät paikkansa
- Pystyy lisäämään opiskelijan, mutta vajailla tai virheellisillä tiedoilla antaa varoituksen
- Opettajalistauksen ja opettajasivun tiedot pitävät paikkansa
- Pystyy lisäämään opettajan, mutta vajailla tai virheellisillä tiedoilla antaa varoituksen
- Opiskelija pystyy liittymään kursseille ja poistumaan niiltä, mutta suoritetulta kurssilta ei pysty poistumaan
- Opettaja ja rehtori pystyy merkitsemään opiskelijan osallistumisen suorituksesksi, mutta vajailla suorituslomakkeen tiedoilla suoritusta ei synny

## Testikattavuus

Testauksen rivikattavuudeksi saavutettiin 83% ja haaraumakaattavuudeksi 85%

<img alt="luokkakaavio" src="https://github.com/anttiollikkala/ot-harjoitustyo/blob/master/dokumentaatio/img/testikattavuus.png?raw=true">  

## Mahdolliset virhetilanteet
Sovelluksessa ei ole tunnettuja virhetilanteita mitä voi syntyä, mikäli asennus- ja käyttöohjeita noudattaa.
Sovelluksen voi saada rikki, mikäli tietokantatiedoston poistaa kesken sovelluksen toiminnan. Tämän testaaminen eri tilanteissa ei ole kuitenkaan tarkoituksenmukaista ja on käyttäjän vastuulla olla tekemättä niin.

