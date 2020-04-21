# Arkkitehtuurikuvaus

## Rakenne

Ohjelman toteutuksessa hyödynnetään kolmikerroksista suunnittelumallia, jossa kerrokset ovat ui, domain ja DAO.
Ui kerros vastaa käyttöliittymästä, domain sovelluslogiikasta ja DAO tietojen pysyväistallennukse hoitamisesta.

## Sovelluslogiikka
Sovellus noudattaa ratkaisevilta osin seuraavan luokkaakaavion kuvaavaa mallia:
<img alt="luokkakaavio" src="https://github.com/anttiollikkala/ot-harjoitustyo/blob/master/dokumentaatio/img/luokkakaavio.png?raw=true" width="500">
## Tietojen pysyväistallennus

Sovellukseen luodaan Data Access Object -mallilla rajapinta SQLite tietokantaan. 
Kaikki sovelluksen tiedot tallennetaan yhteen tietokantatiedostoon siihen kansioonm , mistä sovellus ajetaan.

## Päätoiminnallisuudet
<img alt="Sekvenssikaavio" src="https://github.com/anttiollikkala/ot-harjoitustyo/blob/master/dokumentaatio/img/Sekvenssikaavio.png?raw=true">
