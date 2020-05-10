# Testausdokumentti

Sovellukseen on tehty automaattiset yksikkö-, sekä integraatiotestit JUnit kirjastoa hyväksikäyttäen. 
Järjestelmätason testit on suoritettu manuaalisesti katsoen että kaikki sovelluksen ominaisuuden toimivat tarkoituksenmukaisella tavalla.

## Yksikkötestaukset

Sovelluksen yksikkötestaukset kohdistuu kaikkiin DAO luokkiin, sekä muutamassa tapauksessa myös entiteettejä kuvaaviin luokkiin, mutta entiteettien testaus laajasti ei ole tarkoituksenmukaista, sillä ne eivät sisällä kuin hyvin yksinkertaista logiikkaa, kuten settereitä ja gettereitä.
