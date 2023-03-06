package dev.henrihenr.game2d.maniaconverter.game;

import java.util.List;

import dev.henrihenr.game2d.maniaconverter.mania.ManiaChart;
import dev.henrihenr.game2d.maniaconverter.util.BeatMapConverter;

public record GameBeatChart(
    String chartName,
    int chartLanes,
    String chartArtist,
    long chartOffset,
    List<GameHitObject> hitObjects
) {
    public GameBeatChart(ManiaChart maniaChart)
    {
        this(
            maniaChart.version(), 
            maniaChart.circleSize(), 
            maniaChart.creator(), 
            BeatMapConverter.hitObjectOffsetOf(maniaChart),
            BeatMapConverter.gameHitObjOf(maniaChart)
        );
    }
}
