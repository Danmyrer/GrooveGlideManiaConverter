package dev.henrihenr.game2d.maniaconverter.game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;
import javax.naming.directory.InvalidAttributesException;

import dev.henrihenr.game2d.maniaconverter.mania.ManiaBeatMap;
import dev.henrihenr.game2d.maniaconverter.mania.ManiaChart;
import dev.henrihenr.game2d.maniaconverter.util.BeatMapConverter;

public class GameBeatMap
{
    private final String songName;
    private final String songArtist;
    private final int songBPM;
    private final String songAudioFile;
    private final String beatMapArtist;
    private final List<String> versions;
    private final Map<String,GameBeatChart> beatCharts;

    public GameBeatMap(String songName, String songArtist, int songBPM, String songAudioFile, Map<String,Long> songAudioOffset,
    String beatMapArtist, List<Path> beatChartPaths, List<String> versions, Map<String,GameBeatChart> beatCharts)
    {
        this.songName = songName;
        this.songArtist = songArtist;
        this.songBPM = songBPM;
        this.songAudioFile = songAudioFile;
        this.beatMapArtist = beatMapArtist;
        this.versions = versions;
        this.beatCharts = beatCharts;
    }

    public GameBeatMap(ManiaBeatMap maniaBeatMap) throws InvalidAttributeValueException
    {
        Optional<Map.Entry<String, ManiaChart>> firstEntrySet = maniaBeatMap.beatMap().entrySet().stream().findAny();
        if (!firstEntrySet.isPresent()) throw new InvalidAttributeValueException();

        this.songName = firstEntrySet.get().getValue().title();
        this.songArtist = firstEntrySet.get().getValue().artist();
        this.songBPM = BeatMapConverter.bpmOf(firstEntrySet.get().getValue());
        this.songAudioFile = firstEntrySet.get().getValue().audioFileName().split("\\.")[0] + ".wav";
        this.beatMapArtist = firstEntrySet.get().getValue().creator();
        this.versions = maniaBeatMap.versions();
        this.beatCharts = getBeatChartMapOf(maniaBeatMap);
    }

    private Map<String, GameBeatChart> getBeatChartMapOf(ManiaBeatMap maniaBeatMap)
    {
        Map<String, GameBeatChart> beatCharts = new HashMap<>();
        for (String version : maniaBeatMap.versions()) 
        {
            beatCharts.put(version, new GameBeatChart(maniaBeatMap.beatMap().get(version)));
        }
        return beatCharts;
    }

    public String songName() { return this.songName; }
    public String songArtist() { return this.songArtist; }
    public int songBPM() { return this.songBPM; }
    public String songAudioFile() { return this.songAudioFile; }
    public String beatMapArtist() { return this.beatMapArtist; }
    public List<String> versions() { return this.versions; }
    public Map<String,GameBeatChart> beatCharts() { return this.beatCharts; }

    @Override
    public String toString() 
    {
        return List.of(songName, songArtist, songBPM, songAudioFile, beatMapArtist).toString();
    }

    public static void main(String[] args) throws InvalidAttributeValueException, FileNotFoundException, InvalidAttributesException, IOException 
    {
        GameBeatMap beatMap = new GameBeatMap(new ManiaBeatMap(Path.of("res/Camellia - Clouds in the Blue (Asherz007)")));
        System.out.println(beatMap.toString());
    }
}
