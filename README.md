![Cover Image](https://repository-images.githubusercontent.com/604114394/e46212af-96d7-4ffe-a10b-da10ab3c5c12)

# GrooveGlide

### ➜ ManiaConverter

*"Groove Glide" ist ein Rhythmus-Spiel, bei dem du eine Plattform bewegst, um fallende Noten im Takt der Musik zu treffen.*

Der ManiaConverter ist ein Tool, um Beatmaps im `.osz`-Format für das Spiel GrooveGlide zu Konvertieren.

## Vorraussetzungen

- Das Spiel GrooveGlide (Converter befindet sich in Projektverzeichnis)

- Das Spiel Osu (Installationsanweisung folgt)

- ZIP (zum Entpacken von `.zip`-Dateien)

- FFMPEG (neuste Version, muss in `$PATH` registriert sein)

## Einrichtung und Nutzung

### Installation Osu!

```shell
flatpak install sh.ppy.osu
```

Osu! kann auch ohne Flatpak installiert werden. Informationen hierzu finden Sie auf der [offiziellen Website](https://sh.ppy.osu/home) 

### Exportieren einer Beatmap

Um eine Beatmap zu exportieren muss sie im Bearbeitungsmodus geöffnet werden.

![Map im Bearbeitungsmodus öffnen](/home/henri/Dokumente/OOSE/Projekte/GrooveGlide/doc/res/map_bearbeiten.png)

> **ACHTUNG:** Folgende Bedingungen müssen für das erfolgreiche Konvertieren erfüllt sein:
> 
> - Es muss sich um eine osu!Mania-Map handeln (Filter kann oben links angewendet werden - Das 4. Pinkfarbene Icon anklicken - siehe Bild).
> 
> - Keiner der Chartnamen (Afternoon, Darkness, etc.) darf Sonderzeichen wie " ' oder \ enthalten.
> 
> - Keine der Charts darf eine SV-Map sein (mehr dazu später).

Im Editor kann nun unter  `Datei ➜ Export package` die gesamte Beatmap Exportiert werden.

![Export Package](/home/henri/Dokumente/OOSE/Projekte/GrooveGlide/doc/res/export_map.png)

> Nach dem Exportieren sollte sich ein Fenster mit der Exportieren `.osz`-Datei öffnen. Wenn nicht kann die Datei im Installationsverzeichnis unter `export` gefunden werden.

### Konvertieren der Datei

Zuerst verschieben wir die exportierte Datei in das `convert`-Verzeichnis unter `ManiaConverter`

![osz in convert](/home/henri/Dokumente/OOSE/Projekte/GrooveGlide/doc/res/osz_in_convert.png)

Anschließend bennen wir die `.osz`-Datei in `.zip` um und Extrahieren den Ordner

```shell
mv "Zutomayo - Darken (Henri Henr)".osz "Zutomayo - Darken (Henri Henr)".zip
unzip "Zutomayo - Darken (Henri Henr)".zip
rm "Zutomayo - Darken (Henri Henr)".zip
```

> **ACHTUNG!** Am besten prüfen Sie jetzt, ob es sich bei einer der Charts um eine SV-Chart handelt. Eine SV-Chart (Slider Velocity) besitzt viele Timing-Points, die beim Konvertieren Probleme Bereiten können. 
> 
> Prüfen Sie daher in dem entpackten Verzeichnis mit einem Texteditor die einzelnen Charts (wieder `.osz`-Dateien) dahingehend, dass unter `[TimingPoints]` höchstens 10 Punkte definiert sind. Wenn mehr vorhanden sind, einfach alle bis auf den ersten löschen.

Nun können wir die Datei endlich konvertieren.

```shell
java -jar ManiaConverter.jar "Zutomayo - Darken (Henri Henr)"/
```

 ![convert_log](/home/henri/Dokumente/OOSE/Projekte/GrooveGlide/doc/res/convert_log.png)

Der neu generierte Ordner endet auf `(m2g)` und kann wie eine gewöhnliche GrooveGlide-BeatMap in den Ordner `GrooveGlide/maps/` importiert werden.