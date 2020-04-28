# SchoolMaster9000

Sovelluksen avulla voidaan hallinoida koulun toimintaa sekä opettajien, että opilaiden ja kurssien osalta. Järjestelmä helpottaa sekä rehtorin, opettajien että oppilaiden toimintaa poistamalla riippuvuuksia useampaan järjestelmään ja tuo kaikki tarvittavat toiminnallisuudet yhden järjestelmän alle.

## Dokumentaatio
[Tuntikirjanpito](/dokumentaatio/tuntikirjanpito.md)  
[Vaatimusmäärittely](/dokumentaatio/vaatimusmaarittely.md)  
[Arkkitehtuuri](/dokumentaatio/arkkitehtuuri.md)

## Komentorivitoiminnot

### Suoritus

Ohjelman voi suorittaa komentoriviltä komennolla
```
mvn compile exec:java -Dexec.mainClass=ollikkala.schoolmaster9000.Main
```

## Releaset

[v0.1 Viikko5](/releases/tag/v0.1)


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

## Releaset

[v0.1 Viikko5](/releases/tag/v0.1)

### Suoritettava JAR
=======

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

