package dev.henrihenr.game2d.maniaconverter.mania;

public record ManiaHitObject(
    int x,
    long timing
) 
{
    @Override
    public String toString()
    {
        return "(" + x + ", " + timing + ")";
    }

    public static void main(String[] args) 
    {
        ManiaHitObject ho = new ManiaHitObject(10, 300);
        System.out.println(ho.toString());
    }
}
