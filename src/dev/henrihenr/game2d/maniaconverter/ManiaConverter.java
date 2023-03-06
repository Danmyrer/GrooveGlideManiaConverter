package dev.henrihenr.game2d.maniaconverter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import javax.management.InvalidAttributeValueException;
import javax.naming.directory.InvalidAttributesException;

import dev.henrihenr.game2d.maniaconverter.game.GameBeatChart;
import dev.henrihenr.game2d.maniaconverter.game.GameBeatMap;
import dev.henrihenr.game2d.maniaconverter.game.GameBeatMapFile;
import dev.henrihenr.game2d.maniaconverter.mania.ManiaBeatMap;

public class ManiaConverter 
{
    public static void main(String[] args) throws InvalidArgumentException, InvalidAttributeValueException, InvalidAttributesException, IOException
    {
        if (args.length < 1) throw new InvalidArgumentException("java -jar ManiaConverter pathIn [pathOut]");
        Path pathIn = Path.of(args[0]);

        ManiaBeatMap maniaBeatMap = loadManiaBeatMap(pathIn);
        GameBeatMap gameBeatMap = generateGameBeatMap(maniaBeatMap);

        Path pathOut;
        if (args.length > 1)
        {
            pathOut = Path.of(args[2]);
            if (!Files.exists(pathOut)) throw new InvalidArgumentException(pathOut.toString() + " existiert nicht!");
        }
        else
        {
            try(Scanner in = new Scanner(System.in))
            {
                confirm : while(true)
                {
                    System.out.println("\npathOut nicht gegeben, soll ein Ordner erstellt werden? [Y,n]");
                    String c = in.nextLine();
                    switch (c.toLowerCase())
                    {
                        case "y":
                        case "yes":
                        case "": break confirm;
                        case "n":
                        case "no": System.exit(0);
                        default: continue;
                    }
                }
            }

            pathOut = Path.of(maniaBeatMap.dirPath().toString() + "(m2g)");
            Files.createDirectories(pathOut); // TODO auf überschreibung prüfen
        }

        encodeSongFile(pathIn, pathOut, maniaBeatMap, gameBeatMap);
        generateMapConf(pathOut, gameBeatMap);
        generateBeatCharts(pathOut, gameBeatMap);
    }

    private static ManiaBeatMap loadManiaBeatMap(Path path) throws InvalidAttributeValueException, InvalidAttributesException, IOException, InvalidArgumentException
    {
        System.out.print("Mania-BeatMap wird geladen... ");
        if (!Files.exists(path)) throw new InvalidArgumentException(path.toString() + " existiert nicht!");
        ManiaBeatMap maniaBeatMap = new ManiaBeatMap(path);
        System.out.println("done!");
        return maniaBeatMap;
    }

    private static GameBeatMap generateGameBeatMap(ManiaBeatMap maniaBeatMap) throws InvalidAttributeValueException
    {
        System.out.print("Game-BeatMap wird generiert... ");
        GameBeatMap gameBeatMap = new GameBeatMap(maniaBeatMap);
        System.out.println("done!");
        return gameBeatMap;
    }

    private static void encodeSongFile(Path pathIn, Path pathOut, ManiaBeatMap maniaBeatMap, GameBeatMap gameBeatMap) throws IOException
    {
        System.out.print("Song-Datei wird encodiert... ");
        String ffmpeg = "ffmpeg -i \"" + pathIn + "/" + maniaBeatMap.firstAudioFile() + "\" ";
        ffmpeg += "\"" + pathOut + "/" + gameBeatMap.songAudioFile() + "\"";
        String[] command = {"/bin/bash", "-c", ffmpeg};
        new ProcessBuilder(command).start();
        System.out.println("done!");
    }

    private static void generateMapConf(Path pathOut, GameBeatMap gameBeatMap) throws IOException
    {
        System.out.print("map.conf wird generiert... ");
        File gameConfig = Path.of(pathOut.toString(), "map.conf").toFile();
        gameConfig.createNewFile(); // FIXME schauen ob eine alte datei überschrieben wird
        try (FileWriter writer = new FileWriter(gameConfig))
        {
            writer.write(GameBeatMapFile.mapConfigOf(gameBeatMap)); 
        }
        System.out.println("done!");
    }

    private static void generateBeatCharts(Path pathOut, GameBeatMap gameBeatMap) throws IOException
    {
        System.out.print("Game-Charts werden generiert... ");
        for (String version : gameBeatMap.versions()) 
        {
            GameBeatChart chart = gameBeatMap.beatCharts().get(version);
            File beatChart = Path.of(pathOut.toString(), version.toLowerCase() + ".chart").toFile();
            beatChart.createNewFile(); // FIXME schauen ob eine alte datei überschrieben wird
            try (FileWriter writer = new FileWriter(beatChart))
            {
                writer.write(GameBeatMapFile.chartConfigOf(chart)); 
            }
        }
        System.out.println("done!");
    }
}

class InvalidArgumentException extends Exception
{ 
    public InvalidArgumentException(String arg0) { super(arg0); }
}