package dev.henrihenr.game2d.maniaconverter.mania;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.management.InvalidAttributeValueException;
import javax.naming.directory.InvalidAttributesException;

public class ManiaBeatMap
{
    private final Path dirPath;
    private final List<String> versions;
    private final Map<String, ManiaChart> beatMap;
    private final String firstAudioFile;

    public ManiaBeatMap(Path dirPath) throws IOException, InvalidAttributesException, InvalidAttributeValueException
    {
        if (!Files.exists(dirPath)) throw new FileNotFoundException(dirPath + " - Das Verzeichnis konnte nicht gefunden werden.");
        this.dirPath = dirPath;

        // Holen aller dateien, die auf ".osu" enden
        Stream<Path> filterStream = Files.list(dirPath).filter(file -> file.toString().toLowerCase().endsWith(".osu"));
        if (filterStream.count() < 1) throw new FileNotFoundException("Es muss mindestens eine Beatmap-Datei Existieren.");
        Set<Path> beatMapPaths = new HashSet<>();
        Files.list(dirPath).filter(file -> file.toString().toLowerCase().endsWith(".osu")).forEach(path -> beatMapPaths.add(path));
        // Füllen der Map beatMaps, um später weitere parameter zu erhalten
        List<String> beatMapData = new ArrayList<>();
        for (Path path : beatMapPaths) {
            String data = Files.readString(path);
            beatMapData.add(data);
        }

        this.beatMap = new HashMap<>();
        versions = ManiaChart.checkAndSetStringValues(beatMapData, "Version");
        for (int i = 0; i < versions.size(); i++) 
        {
            this.beatMap.put(versions.get(i), new ManiaChart(beatMapData.get(i)));
        }

        this.firstAudioFile = this.beatMap.get(versions.get(0)).audioFileName();
    }

    // region getter
    public Path dirPath() { return this.dirPath; }
    public List<String> versions() { return this.versions; }
    public Map<String,ManiaChart> beatMap() { return this.beatMap; }
    public String firstAudioFile() { return this.firstAudioFile; }
    // endregion

    /**
     * Erstellt eine Map aus zwei verschiedenen Listen
     * 
     * @param <A> Typ der ersten Liste
     * @param <B> Typ der zweiten Liste
     * @param keys  Liste mit Schlüsseln
     * @param values    Liste mit Werten
     * @return gezippte Map
     */
    public static <A,B> Map<A,B> zipToMap(List<A> keys, List<B> values)
    {
        // Wir nehmen uns einen IntStream mit der größe der Keys, konvertieren das in einen Stream<Int> (mit boxed())
        // und reichen diesen in einen Collector, der zwei Objekte nimmt und diese Mappt
        return IntStream.range(0, keys.size()).boxed().collect(
            Collectors.toMap(keys::get, values::get)
        );
    }

    @Override
    public String toString() 
    {
        return this.dirPath.toString() + ", " + this.beatMap.toString();
    }

    public static void main(String[] args) throws InvalidAttributeValueException, InvalidAttributesException, IOException 
    {
        ManiaBeatMap beatMap = new ManiaBeatMap(Path.of("res/Camellia - Clouds in the Blue (Asherz007)"));
        System.out.println(beatMap.toString());
    }
}
