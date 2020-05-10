# Vaatimusmäärittely

## Sovelluksen tarkoitus

Sovelluksen tarkoitus on hallinoida koulun toimintaa sekä opettajien, että opilaiden ja kurssien osalta.
Järjestelmä helpottaa sekä rehtorin, opettajien että oppilaiden toimintaa poistamalla riippuvuuksia useampaan järjestelmään ja tuo kaikki tarvittavat toiminnallisuudet yhden järjestelmän alle.

## Käyttäjät

Sovelluksessa on kolmenlaisia käyttäjiä:
 - Rehtori
 - Opettaja
 - Oppilas

Rehtoreita voi olla vain yksi, mutta opettajia ja oppilaita rajaton määrä. 
Rehtori voi lisätä opettajia ja oppilaita ja opettaja voi lisätä oppilaita.

## Toiminnallisuus

### Ennen kirjautumista

 - Käyttäjä voi kirjautua järjestelmään TEHTY
 - Voidaan luoda koulu, jos ei vielä luotu TEHTY

### Kirjautumisen jälkeen

#### Oppilas
 - Oppilas voi liittyä kursseille TEHTY
 - Voi poistua kurssilta, jos sitä ei ole merkitty opettajan toimesta vielä suoritetuksi TEHTY
 - Tarkastella opintohistoriaa TEHTY
 - Estetty liittyminen samalle kurssille ueampaan kertaan TEHTY
 - Estetty poistuminen kurssilta jos jo suoritettu TEHTY

#### Opettaja
 - Luoda kursseja joihin oppilaat voi osallistua TEHTY
 - Arvostella kursseille osallistujien suorituksia arvosanoin ja kommentoida suoritusta. TEHTY
 - Ei voi luoda samalla nimellä tai tunnuksella toista kurssia TEHTY
 - Ei voi luoda jo järjestelmässä olevallsähköpostiosoitteella oppilasta TEHTY
 - Tarkastella oppilaiden tietoja, osallistumisia ja suorituksia oppilassivulla TEHTY

#### Rehtori
 - Kaikkea mitä opettaja TEHTY
 - Lisätä opettajia järjestelmään TEHTY
 - Ei voi luoda jo järjestelmässä olevalla sähköpostiosoitteella opettajaa TEHTY
 - Voi tarkastella opettajien tietoja ja listata kurssit joille he opettavat TEHTY
 
#### Kaikki käyttäjät
- Voi tarkastella luotuja kursseja TEHTY

## Jatkokehitysideoita

 - Laskaripisteiden kirjaus
