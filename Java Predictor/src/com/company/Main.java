package com.company;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        PredictorCalculations predictorCalculations = new PredictorCalculations();
        FBRefScraper webScraper = new FBRefScraper();

        String homeTeamName = "Manchester City";
        String awayTeamName = "Liverpool";

        ArrayList<Team> teamData = webScraper.getTeamData();

        double averageHomeTeamGoals = teamData.stream().mapToDouble(Team::getHomeGoalsFor).average().orElse(0.0);
        double averageHomeTeamXG = teamData.stream().mapToDouble(Team::getHomeXGFor).average().orElse(0.0);
        double averageHomeTeamXGAgainst = teamData.stream().mapToDouble(Team::getHomeXGAgainst).average().orElse(0.0);
        double averageAwayTeamGoals = teamData.stream().mapToDouble(Team::getAwayGoalsFor).average().orElse(0.0);
        double averageAwayTeamXG = teamData.stream().mapToDouble(Team::getAwayXGFor).average().orElse(0.0);
        double averageAwayTeamXGAgainst = teamData.stream().mapToDouble(Team::getAwayXGAgainst).average().orElse(0.0);

        Team homeTeam = teamData.stream().filter(a -> a.getTeamName().equals(homeTeamName)).collect(Collectors.toList()).get(0);
        Team awayTeam = teamData.stream().filter(a -> a.getTeamName().equals(awayTeamName)).collect(Collectors.toList()).get(0);

        double homeAttackStrength = predictorCalculations.calculateStrengthRating(homeTeam.getHomeXGFor(), averageHomeTeamXG);
        double homeDefenceStrength = predictorCalculations.calculateStrengthRating(homeTeam.getHomeXGAgainst(), averageHomeTeamXGAgainst);
        double awayAttackStrength = predictorCalculations.calculateStrengthRating(awayTeam.getAwayXGFor(), averageAwayTeamXG);
        double awayDefenceStrength = predictorCalculations.calculateStrengthRating(awayTeam.getAwayXGAgainst(), averageAwayTeamXGAgainst);

        double homeExpGoals = predictorCalculations.calculatePredictedXG(homeAttackStrength, awayDefenceStrength, averageHomeTeamGoals);
        double awayExpGoals = predictorCalculations.calculatePredictedXG(awayAttackStrength, homeDefenceStrength, averageAwayTeamGoals);

        predictorCalculations.predictGamePoisson(homeTeamName, awayTeamName, homeExpGoals, awayExpGoals);
    }
}
