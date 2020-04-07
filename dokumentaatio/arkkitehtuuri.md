# Arkkitehtuurikuvaus

## Rakenne

Ohjelman toteutuksessa hyödynnetään kolmikerroksista suunnittelumallia, jossa kerrokset ovat ui, domain ja DAO.
Ui kerros vastaa käyttöliittymästäm, domain sovelluslogiikasta ja DAO tietojen pysy�väistallennuksen hoitamisesta.

## Sovelluslogiikka
![Luokkakaavio](/img/luokkakaavio.png)
## Tietojen pysyväistallennus

Sovellukseen luodaan Data Access Object -mallilla rajapinta SQLite tietokantaan. 
Kaikki sovelluksen tiedot tallennetaan yhteen tietokantatiedostoon siihen kansioonm , mistä sovellus ajetaan.
