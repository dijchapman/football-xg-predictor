package com.company;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Main mainApp = new Main();
        PredictorCalculations predictorCalculations = new PredictorCalculations();
        FBRefScraper webScraper = new FBRefScraper();

        ArrayList<Team> teamData = webScraper.getTeamData();

        String homeTeam = "Manchester City";
        String awayTeam = "Liverpool";
        //mainApp.predictFixture(predictorCalculations, teamData, homeTeam, awayTeam);

        webScraper.getRemainingFixtures();
    }

    public void predictFixture(PredictorCalculations predictorCalculations, ArrayList<Team> teamData, String homeTeam, String awayTeam) {
        double averageHomeTeamGoals = teamData.stream().mapToDouble(Team::getHomeGoalsFor).average().orElse(0.0);
        double averageHomeTeamXG = teamData.stream().mapToDouble(Team::getHomeXGFor).average().orElse(0.0);
        double averageHomeTeamXGAgainst = teamData.stream().mapToDouble(Team::getHomeXGAgainst).average().orElse(0.0);
        double averageAwayTeamGoals = teamData.stream().mapToDouble(Team::getAwayGoalsFor).average().orElse(0.0);
        double averageAwayTeamXG = teamData.stream().mapToDouble(Team::getAwayXGFor).average().orElse(0.0);
        double averageAwayTeamXGAgainst = teamData.stream().mapToDouble(Team::getAwayXGAgainst).average().orElse(0.0);

        Team homeTeamData = teamData.stream().filter(a -> a.getTeamName().equals(homeTeam)).collect(Collectors.toList()).get(0);
        Team awayTeamData = teamData.stream().filter(a -> a.getTeamName().equals(awayTeam)).collect(Collectors.toList()).get(0);

        double homeAttackStrength = predictorCalculations.calculateStrengthRating(homeTeamData.getHomeXGFor(), averageHomeTeamXG);
        double homeDefenceStrength = predictorCalculations.calculateStrengthRating(homeTeamData.getHomeXGAgainst(), averageHomeTeamXGAgainst);
        double awayAttackStrength = predictorCalculations.calculateStrengthRating(awayTeamData.getAwayXGFor(), averageAwayTeamXG);
        double awayDefenceStrength = predictorCalculations.calculateStrengthRating(awayTeamData.getAwayXGAgainst(), averageAwayTeamXGAgainst);

        double homeExpGoals = predictorCalculations.calculatePredictedXG(homeAttackStrength, awayDefenceStrength, averageHomeTeamGoals);
        double awayExpGoals = predictorCalculations.calculatePredictedXG(awayAttackStrength, homeDefenceStrength, averageAwayTeamGoals);

        predictorCalculations.predictGamePoisson(homeTeam, awayTeam, homeExpGoals, awayExpGoals);
    }

    public void predictRemainingFixtures(PredictorCalculations predictorCalculations, FBRefScraper webScraper, ) {

    }
}
