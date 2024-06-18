# 3D Grafikus rendszerek - 4. Házi feladat
## Kiinduló projekt
A kiinduló projekt egy Gradle segítségével buildelhető WebGL alapú 3D grafikus alkalmazás, amely GLSL shadereket és Kotlin nyelvű logikát használ a rendereléshez.
Némely matematikai alapművelet és geomtria előre meg van írva. Ezeket kell a feladatnak megfelelően kiegészíteni vagy befejezni. A program fordítása során a Kotlin/JS transzpiláció a Kotlin kódot JavaScript kódra fordítja, amit a böngésző képes futtatni.

Kamera irányítása az A,W,S,D,Q,E billentyűk segítségével történik.

## Megvalósított feladatok
Jelenítsen meg egy színteret összetett árnyalási megoldásokkal. A színtérben mindenképpen legyen egy lebegő 3D test (a továbbiakban: OBJEKTUMOK).

- Spot: Az OBJEKTUMOK árnyalásakor használja a Max-Blinn modellt, és legalább egy, pontszerű, de irányított (nem izotrópikus) spotlight-jellegű fényforrást kezeljen. Ez a fényforrás pozíciójával, főirányával, a főirányban a fény RGB hullámhosszokon vett intenzitásával adott. Az intenzitás a főiránytól eltérve az eltérési szög koszinuszának valamilyen szabadon választott hatványával csökken.

- Normal mapping (textúrával): Az OBJEKTUMOK árnyalásakor használjon normal map textúrát a normálvektorok meghatározásához, és valamiféle nem-diffúz (Phong, Phong-Blinn, max-Blinn, environment map) árnyalást. A normálvektorokat tangenstérben értelmezze. Ehhez a vertexek tangens- és binormálvektor-attribútumait olvassa modellfileból.

- Visszapillantó: Rajzolja textúrába a színteret egy hátrafordított kamerával. Jelenítse meg a textúrát egy, a képernyőn fent középen elhelyezett téglalapra feszítve, de úgy, hogy az visszapillantó tükörként működjön.