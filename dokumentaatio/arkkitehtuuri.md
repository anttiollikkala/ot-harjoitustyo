# Arkkitehtuurikuvaus

## Rakenne

Ohjelman toteutuksessa hyödynnetään kolmikerroksista suunnittelumallia, jossa kerrokset ovat ui, domain ja DAO.
Ui kerros vastaa käyttöliittymästä, domain sovelluslogiikasta ja DAO tietojen pysyväistallennukse hoitamisesta.
DAO:jen tehtävä on hakea data mahdollisimman yksinkertaisessa muodossa.
Domain kerros olisi mahdollista jakaa vielä kahteen, sillä se sisältää sekä luokkia jotka kuvaavat vain entiteettejä, joissa on lähtökohtaisesti vain settereitä ja gettereitä, sekä service tyyppisiä luokkia jotka hakevat, tallentavat ja yhdistävät -dataa muiden service-luokkien ja DAO:jen avulla.

Pakkausten hierarkia on seuraava  
[ui]-->[domain]-->[dao]  

## Käyttöliittymä
Käyttöliittymässä on monta eri näkymää ja ne ovat seuraavanlaiset
#### Ennen kirjautumista
 - Koulun luomiskaavake, jos koulua ei ole vielä luotu
 - Kirjautumislomake, jos koulu on jo luotu
#### Kirjautumisen jälkeen
Kirjautumisen jälkeen sovelluksen vasemmassa laidassa on aina päävalikko. Oikeanpuoleisessa näkymässä vaihtelee alinäkymät seuraavien mahdollisuuskien mukaan:
- Omien tietojen muokkaussivu
- Kurssien listaussivu
  - Uuden kurssin luomissivu
  - Kurssisivu jonka saa auki, kun klikkaa kurssia listaussivulla
- Opiskelijoiden listaussivu
  - Uuden opiskelijan luomiskaavake
  - Opiskelijasivu jonka saa auki, kun klikkaa opiskelijaa listaussivulla
- Opettajien listaussivu
  - Uuden opettajan luomiskaavake
  - Opettajasivu, jonka saa auki, kun klikkaa opettajaa listaussivulla
Näkymien saatavuus on rajoitettu kirjautuneen käyttäjän oikeuksien perusteella
## Sovelluslogiikka
Sovellus noudattaa ratkaisevilta osin seuraavan luokkaakaavion kuvaavaa mallia:
<img alt="luokkakaavio" src="https://github.com/anttiollikkala/ot-harjoitustyo/blob/master/dokumentaatio/img/sekvenssikaavio2.png?raw=true" width="500">  
Luokkakaaviossa on "luokkia" joita sovelluksen toteutuksessa ei todellisuudessa ole (school & principal), mutta ne on sisällytetty kaavioon helpottamaan käsitteiden rakenteen hierarkiaa
## Tietojen pysyväistallennus

Sovellukseen luodaan Data Access Object -mallilla rajapinta SQLite tietokantaan. 
Kaikki sovelluksen tiedot tallennetaan yhteen tietokantatiedostoon siihen kansioon, mistä sovellus ajetaan.  

### Taulut
Tietokannassa käyttäjät ovat taulussa users, kurssit courses, koulun tiedot taulussa config ja kurssi-ilmoittautumisten ja suoritusten liitostaulu on taulussa participations. Käyttäjien sähköpostien, kurssien nimien ja tunnunnusten uniikkius on varmistettu tietokantatasolla unique constrainteilla. Käyttäjien kurssi-ilmoittautumisten ja suoritusten yhdistelmien uniikkius on myös varmistettu tietokantatasolla course_id ja user_id yhdistelmän unique constraintilla. Tauluissa on myös foreign key referenssejä, mutta niiden merkitys ei ole kovin suuri, sillä virheellisten syötteiden antaminen on UI tasolla mahdotonta ja sellaisia rivejä ei voida tietokannasta poistaa mikä voisi aiheuttaa konfliktin referensseissä.


## Päätoiminnallisuudet
Tilanteen alussa käyttäjä on luomassa koulua ja täyttänyt jo vaadittavat kentät koulun luomista varten.
<img alt="Sekvenssikaavio" src="https://github.com/anttiollikkala/ot-harjoitustyo/blob/master/dokumentaatio/img/Sekvenssikaavio.png?raw=true">
