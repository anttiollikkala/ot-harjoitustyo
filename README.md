# SchoolMaster9000

Sovelluksen avulla voidaan hallinoida koulun toimintaa sekä opettajien, että opilaiden ja kurssien osalta. Järjestelmä helpottaa sekä rehtorin, opettajien että oppilaiden toimintaa poistamalla riippuvuuksia useampaan järjestelmään ja tuo kaikki tarvittavat toiminnallisuudet yhden järjestelmän alle.

## Tehtävät

### Vk1
[gitlog.txt](/laskarit/viikko1/gitlog.txt)  
[komentorivi.txt](/laskarit/viikko1/komentorivi.txt)  

### Vk2
[Unicafe](/laskarit/viikko2/Unicafe)  
[testikattavuus](/laskarit/viikko2/testikattavuus.png)  

## Dokumentaatio
[Tuntikirjanpito](/dokumentaatio/tuntikirjanpito.md)  
[Vaatimusmäärittely](/dokumentaatio/vaatimusmaarittely.md)  

## Komentorivitoiminnot

### Testaus

Testit suoritetaan komennolla
```
mvn test
```

Testikattavuusraportti luodaan komennolla

```
mvn jacoco:report
```
Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto target/site/jacoco/index.html
