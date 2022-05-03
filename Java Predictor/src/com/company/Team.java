package com.company;

public class Team {
    private String teamName;
    private double homeGoalsFor;
    private double homeGoalsAgainst;
    private double homeXGFor;
    private double homeXGAgainst;
    private double awayGoalsFor;
    private double awayGoalsAgainst;
    private double awayXGFor;
    private double awayXGAgainst;

    public Team(String name, double hgf, double hga, double hxgf, double hxga, double agf, double aga, double axgf, double axga) {
        this.teamName = name;
        this.homeGoalsFor = hgf;
        this.homeGoalsAgainst = hga;
        this.homeXGFor = hxgf;
        this.homeXGAgainst = hxga;
        this.awayGoalsFor = agf;
        this.awayGoalsAgainst = aga;
        this.awayXGFor = axgf;
        this.awayXGAgainst = axga;
    }

    public String getTeamName() { return teamName; }

    public double getHomeGoalsFor() { return homeGoalsFor; }

    public double getHomeGoalsAgainst() { return homeGoalsAgainst; }

    public double getAwayGoalsFor() { return awayGoalsFor; }

    public double getAwayGoalsAgainst() { return awayGoalsAgainst; }

    public double getAwayXGAgainst() { return awayXGAgainst; }

    public double getAwayXGFor() { return awayXGFor; }

    public double getHomeXGAgainst() { return homeXGAgainst; }

    public double getHomeXGFor() { return homeXGFor; }
}
