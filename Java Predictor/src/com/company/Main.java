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

        mainApp.predictRemainingFixtures(predictorCalculations, webScraper, teamData);
    }

    public String predictFixture(PredictorCalculations predictorCalculations, ArrayList<Team> teamData, String homeTeam, String awayTeam) {
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

        return predictorCalculations.predictGamePoisson(homeTeam, awayTeam, homeExpGoals, awayExpGoals);
    }

    public void predictRemainingFixtures(PredictorCalculations predictorCalculations, FBRefScraper webScraper, ArrayList<Team> teamData) {
        ArrayList<Fixture> remainingFixtures = webScraper.getRemainingFixtures();

        remainingFixtures.forEach((fixture -> {
            String resultSimulation = predictFixture(predictorCalculations, teamData, fixture.getHomeTeam(), fixture.getAwayTeam());
            System.out.print(fixture.getHomeTeam() + " vs " + fixture.getAwayTeam() + " = ");
            switch (resultSimulation) {
                case "home win":
                    System.out.println(fixture.getHomeTeam() + " win");
                    updateTeamPointsTally(teamData, fixture.getHomeTeam(), 3);
                    break;
                case "away win":
                    System.out.println(fixture.getAwayTeam() + " win");
                    updateTeamPointsTally(teamData, fixture.getAwayTeam(), 3);
                    break;
                case "draw":
                    System.out.println("draw");
                    updateTeamPointsTally(teamData, fixture.getHomeTeam(), 1);
                    updateTeamPointsTally(teamData, fixture.getAwayTeam(), 1);
                    break;
            }
        }));
    }

    public ArrayList<Team> updateTeamPointsTally(ArrayList<Team> teamData, String teamName, int points) {
        teamData.stream().filter(team -> team.getTeamName().equals(teamName)).forEach(team -> {
            team.setPredictedPointsTally(team.getPredictedPointsTally() + points);
        });

        return teamData;
    }
}
