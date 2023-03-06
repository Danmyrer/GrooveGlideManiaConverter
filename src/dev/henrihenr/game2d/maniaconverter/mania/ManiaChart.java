package dev.henrihenr.game2d.maniaconverter.mania;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.InvalidAttributeValueException;
import javax.naming.directory.InvalidAttributesException;

public class ManiaChart 
{
    private final String version;
    private final String audioFileName;
    private final String title, artist, creator;
    private final int circleSize;
    private final List<TimeObject> timeObjects;
    private final List<ManiaHitObject> hitObjects;

    public ManiaChart(String data) throws IOException, FileNotFoundException, InvalidAttributesException, InvalidAttributeValueException
    {
        this.version = checkAndSetStringValue(data, "Version");
        this.audioFileName = checkAndSetStringValue(data, "AudioFilename");

        this.title = checkAndSetStringValue(data, "Title");
        this.artist = checkAndSetStringValue(data, "Artist");
        this.creator = checkAndSetStringValue(data, "Creator");

        this.circleSize = checkAndSetIntegerValue(data, "CircleSize");

        this.timeObjects = checkAndSetTimeObjectValues(data, "[TimingPoints]");
        this.hitObjects = checkAndSetHitObjectValues(data, "[HitObjects]");
    }

    /**
     * Prüft auf das Vorhandensein eines Parameters in einer Datei und setzt diesen.
     * 
     * @param data  Konfigurationsdatei die Gelesen werden soll
     * @param parameter
     * @return Gesuchten Wert
     * @throws InvalidAttributesException
     */
    public static String checkAndSetStringValue(String data, String parameter) throws InvalidAttributesException
    {
        int index = data.indexOf(parameter) + parameter.length() + 1;
        if(index == -1) throw new InvalidAttributesException("Der Parameter '" + parameter + "' konnte nicht gefunden werden");
        return data.substring(index, data.indexOf("\n", index)).trim();
    }

    public static Integer checkAndSetIntegerValue(String data, String parameter) throws InvalidAttributesException
    {
        int index = data.indexOf(parameter) + parameter.length() + 1;
        if(index == -1) throw new InvalidAttributesException("Der Parameter '" + parameter + "' konnte nicht gefunden werden");
        return Integer.parseInt(data.substring(index, data.indexOf("\n", index)).trim());
    }

    /**
     * Prüft in mehreren Daten auf Vorhandensein eines Parameters und fügt diese in eine Liste hinzu
     * 
     * @param data  Konfigurationsdatei die Gelesen werden soll
     * @param parameter Parameter der gesucht wird
     * @return  Liste mit den Gesuchten Werten
     * @throws InvalidAttributesException
     */
    public static List<String> checkAndSetStringValues(List<String> data, String parameter) throws InvalidAttributesException
    {
        List<String> values = new ArrayList<>();
        for (String string : data) values.add(checkAndSetStringValue(string, parameter));
        return values;
    }

    /**
     * @param data
     * @param parameter
     * @return
     * @throws InvalidAttributesException
     * @throws InvalidAttributeValueException
     */
    public static List<TimeObject> checkAndSetTimeObjectValues(String data, String parameter) throws InvalidAttributesException, InvalidAttributeValueException
    {
        List<TimeObject> timeObjects = new ArrayList<>();

        int index = data.indexOf(parameter);
        if (index == -1) throw new InvalidAttributesException(parameter + " konnte nicht gefunden werden");
        String[] hitObjStrings = data.substring(data.indexOf("\n", index) + 1).split("\n");
        for (String string : hitObjStrings) 
        {
            String[] hitObjValStrings = string.split(",");
            if(hitObjValStrings.length <= 1) break;
            try
            {
                timeObjects.add(new TimeObject(
                    Long.valueOf(hitObjValStrings[0]),
                    Double.valueOf(hitObjValStrings[1]),
                    Integer.valueOf(hitObjValStrings[2])
                ));
            }
            catch(NumberFormatException e)
            {
                e.printStackTrace();
                throw new InvalidAttributeValueException(parameter + " " + string + " konnte nicht gelesen werden");
            }
        }
        return timeObjects;
    }

    public static List<ManiaHitObject> checkAndSetHitObjectValues(String data, String parameter) throws InvalidAttributesException, InvalidAttributeValueException
    {
        List<ManiaHitObject> hitObjects = new ArrayList<>();

        int index = data.indexOf(parameter);
        if (index == -1) throw new InvalidAttributesException(parameter + " konnte nicht gefunden werden");
        String[] hitObjStrings = data.substring(data.indexOf("\n", index) + 1).split("\n");
        for (String string : hitObjStrings) 
        {
            String[] hitObjValStrings = string.split(",");
            if(hitObjValStrings.length == 0) continue;
            try
            {
                hitObjects.add(new ManiaHitObject(Integer.valueOf(hitObjValStrings[0]), Long.valueOf(hitObjValStrings[2])));
            }
            catch(NumberFormatException e)
            {
                throw new InvalidAttributeValueException(parameter + " " + string + " konnte nicht gelesen werden");
            }
        }
        return hitObjects;
    }

    // region Getter-Methoden
    public String audioFileName() { return this.audioFileName; }
    public String title() { return this.title; }
    public String artist() { return this.artist; }
    public String creator() { return this.creator; }
    public String version() { return this.version; }
    public int circleSize() { return this.circleSize; }
    public List<TimeObject> timeObjects() { return this.timeObjects; }
    public List<ManiaHitObject> hitObjects() { return this.hitObjects; }
    // endregion

    @Override
    public String toString()
    {
        return List.of(audioFileName.toString(), title, artist, creator, version, circleSize, "timeObjects<>, hitObjects<>").toString();
    }
}