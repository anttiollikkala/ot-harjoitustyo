# Arkkitehtuurikuvaus

## Rakenne

Ohjelman toteutuksessa hyÃ¶dynnetÃ¤Ã¤n kolmikerroksista suunnittelumallia, jossa kerrokset ovat ui, domain ja DAO.
Ui kerros vastaa kÃ¤yttÃ¶liittymÃ¤stÃ¤m, domain sovelluslogiikasta ja DAO tietojen pysyÃvÃ¤istallennuksen hoitamisesta.

## Sovelluslogiikka
<img alt="luokkakaavio" src="https://github.com/anttiollikkala/ot-harjoitustyo/blob/master/dokumentaatio/img/luokkakaavio.png?raw=true">
## Tietojen pysyvÃ¤istallennus

Sovellukseen luodaan Data Access Object -mallilla rajapinta SQLite tietokantaan. 
Kaikki sovelluksen tiedot tallennetaan yhteen tietokantatiedostoon siihen kansioonm , mistÃ¤ sovellus ajetaan.
