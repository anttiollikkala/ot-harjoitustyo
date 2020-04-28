# SchoolMaster9000

Sovelluksen avulla voidaan hallinoida koulun toimintaa sekä opettajien, että opilaiden ja kurssien osalta. Järjestelmä helpottaa sekä rehtorin, opettajien että oppilaiden toimintaa poistamalla riippuvuuksia useampaan järjestelmään ja tuo kaikki tarvittavat toiminnallisuudet yhden järjestelmän alle.

## Dokumentaatio
[Tuntikirjanpito](/dokumentaatio/tuntikirjanpito.md)  
[Vaatimusmäärittely](/dokumentaatio/vaatimusmaarittely.md)  
[Arkkitehtuuri](/dokumentaatio/arkkitehtuuri.md)  
[Käyttöohjeet](/dokumentaatio/instructions.md)

## Komentorivitoiminnot

### Suoritus

## Releaset

[Viikko5](https://github.com/anttiollikkala/ot-harjoitustyo/releases/tag/v0.1)  
[Viikko6](https://github.com/anttiollikkala/ot-harjoitustyo/releases/tag/v0.2)


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

### Suoritettava JAR

komennolla
```
mvn package
```

### JavaDoc
Luodaan komennolla
```
mvn javadoc:javadoc
```
Löytyy tiedostosta target/site/apidocs/index.html

### CheckStyle
Komennolla
```
mvn jxr:jxr checkstyle:checkstyle
```
Raportti löytyy tiedostosta:target/site/checkstyle.html

