package dev.henrihenr.game2d.maniaconverter.game;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.management.InvalidAttributeValueException;
import javax.naming.directory.InvalidAttributesException;

import dev.henrihenr.game2d.maniaconverter.mania.ManiaBeatMap;

public class GameBeatMapFile
{
    public static String mapConfigOf(GameBeatMap beatMap)
    {
        String data = "[CONFIG]\n";
        data += parameterOf("SONG_NAME", beatMap.songName());
        data += parameterOf("SONG_ARTIST", beatMap.songArtist());
        data += parameterOf("SONG_BPM", beatMap.songBPM());
        data += parameterOf("SONG_FILE", beatMap.songAudioFile());
        data += parameterOf("BEATMAP_ARTIST", beatMap.beatMapArtist());
        data += "\n[CHARTS]\n";
        for (String version : beatMap.versions()) 
        {
            data += parameterOf(version.toUpperCase(), chartFileOf(version));
        }
        return data;
    }

    public static String chartConfigOf(GameBeatChart beatChart)
    {
        String data = "[CONFIG]\n";
        data += parameterOf("MAP_NAME", beatChart.chartName());
        data += parameterOf("MAP_LANES", beatChart.chartLanes());
        data += parameterOf("MAP_ARTST", beatChart.chartArtist());
        data += parameterOf("MAP_OFFSET", beatChart.chartOffset());
        data += "\n[CHART]\n";
        data += chartOf(beatChart.hitObjects());
        return data;
    }

    private static String chartOf(List<GameHitObject> hitObjs)
    {
        String data = "";
        for (GameHitObject hitObj : hitObjs) 
        {
            data += hitObj.beat() + "=";
            for (int i = 1; i < hitObj.row(); i++) data += "0";
            switch (hitObj.type())
            {
                case MOVE: data += "2"; break;
                default: data += "1";
            }
            data += "\n";
        }
        return data;
    }

    private static String chartFileOf(String version)
    {
        return version.toLowerCase() + ".chart";
    }

    private static String parameterOf(String key, String value)
    { 
        return key + "=" + value + "\n";
    }

    private static String parameterOf(String key, int value)
    {
        return parameterOf(key, String.valueOf(value));
    }

    private static String parameterOf(String key, long value)
    {
        return parameterOf(key, String.valueOf(value));
    }

    public static void main(String[] args) throws InvalidAttributeValueException, InvalidAttributesException, IOException 
    {
        System.out.println(
            GameBeatMapFile.mapConfigOf(new GameBeatMap(new ManiaBeatMap(
                Path.of(
                    "res/Camellia - Clouds in the Blue (Asherz007)"
                )
            )))
        );
        System.out.println("------------------------------------------");
        System.out.println(
            GameBeatMapFile.chartConfigOf(new GameBeatMap(new ManiaBeatMap(
                Path.of(
                    "res/Camellia - Clouds in the Blue (Asherz007)"
                )
            )).beatCharts().get("Skyward"))
        );
    }
}