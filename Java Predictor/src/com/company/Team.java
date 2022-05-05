package com.company;

import java.util.Arrays;

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
    private int currentPointsTally;
    private int predictedPointsTally;
    private int totalPredictedPointsTally;
    private int[] simulatedLeaguePositions;

    public Team(String name, double hgf, double hga, double hxgf, double hxga, double agf, double aga, double axgf, double axga, int points) {
        this.teamName = name;
        this.homeGoalsFor = hgf;
        this.homeGoalsAgainst = hga;
        this.homeXGFor = hxgf;
        this.homeXGAgainst = hxga;
        this.awayGoalsFor = agf;
        this.awayGoalsAgainst = aga;
        this.awayXGFor = axgf;
        this.awayXGAgainst = axga;
        this.currentPointsTally = points;
        this.predictedPointsTally = 0;
        this.totalPredictedPointsTally = 0;
        this.simulatedLeaguePositions = new int[20];
        Arrays.fill(simulatedLeaguePositions, 0);
    }

    public String getTeamName() { return teamName; }

    public double getHomeGoalsFor() { return homeGoalsFor; }

    public double getHomeGoalsAgainst() { return homeGoalsAgainst; }

    public double getAwayGoalsFor() { return awayGoalsFor; }

    public double getAwayGoalsAgainst() { return awayGoalsAgainst; }

    public double getHomeXGAgainst() { return homeXGAgainst; }

    public double getHomeXGFor() { return homeXGFor; }

    public double getAwayXGAgainst() { return awayXGAgainst; }

    public double getAwayXGFor() { return awayXGFor; }

    public int getCurrentPointsTally() { return currentPointsTally; }

    public int getPredictedPointsTally() { return predictedPointsTally; }

    public void setPredictedPointsTally(int points) { this.predictedPointsTally = points; }

    public int getTotalPredictedPointsTally() { return totalPredictedPointsTally; }

    public void setTotalPredictedPointsTally(int points) { this.totalPredictedPointsTally = points; }

    public int[] getSimulatedLeaguePositions() { return simulatedLeaguePositions; }

    public void setSimulatedLeaguePositions(int position) { this.simulatedLeaguePositions[position - 1] = this.simulatedLeaguePositions[position - 1] + 1; }
}
