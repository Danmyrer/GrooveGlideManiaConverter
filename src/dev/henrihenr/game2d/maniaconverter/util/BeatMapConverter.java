package dev.henrihenr.game2d.maniaconverter.util;

import java.util.ArrayList;
import java.util.List;

import dev.henrihenr.game2d.maniaconverter.game.GameHitObject;
import dev.henrihenr.game2d.maniaconverter.mania.ManiaChart;
import dev.henrihenr.game2d.maniaconverter.mania.ManiaHitObject;
import dev.henrihenr.game2d.maniaconverter.mania.TimeObject;

public class BeatMapConverter 
{
    public static List<GameHitObject> gameHitObjOf(ManiaChart maniaChart)
    {
        List<GameHitObject> gameHitObjs = new ArrayList<>();
    
        int keys = maniaChart.circleSize();
        int bpm = bpmOf(maniaChart);
        long offset = hitObjectOffsetOf(maniaChart);
        List<ManiaHitObject> maniaHitObjs = maniaChart.hitObjects();

        for (ManiaHitObject maniaHitObj : maniaHitObjs) 
        {
            for (int i = 1; i <= keys; i++) 
            {
                if (maniaHitObj.x() / (512 / keys) < i) 
                {
                    gameHitObjs.add(
                        new GameHitObject(beatOf(maniaHitObj.timing(), bpm, offset), i, GameHitObject.Type.DEFAULT)
                    );
                    break;
                }
            }
        }

        return gameHitObjs;
    }

    public static double beatOf(long offset, int bpm, long hitObjectOffset)
    {
        return (offset - hitObjectOffset) * (bpm / (double) 60000) + 1;
    }

    public static int bpmOf(ManiaChart maniaChart)
    {
        TimeObject timeObj = maniaChart.timeObjects().get(0);
        return (int) Math.round(60000 / timeObj.bpm());
    }

    public static long hitObjectOffsetOf(ManiaChart maniaChart)
    {
        return maniaChart.hitObjects().get(0).timing();
    }
}