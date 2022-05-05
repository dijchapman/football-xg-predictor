package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Main mainApp = new Main();
        PredictorCalculations predictorCalculations = new PredictorCalculations();
        FBRefScraper webScraper = new FBRefScraper();

        ArrayList<Team> teamData = webScraper.getTeamData();
        ArrayList<Fixture> remainingFixtures = webScraper.getRemainingFixtures();

        String homeTeam = "Manchester City";
        String awayTeam = "Liverpool";
        //mainApp.predictFixture(predictorCalculations, teamData, homeTeam, awayTeam);

        int numberOfSimulations = 100;
        mainApp.runMultipleSimulations(predictorCalculations, teamData, remainingFixtures, numberOfSimulations);
    }

    private static int leaguePosition = 1;
    public void showLeagueTableSingleSimulation(ArrayList<Team> teamData, boolean display) {
        String format = "%2s %-25s %5s%n";
        leaguePosition = 1;

        teamData.stream().sorted((t1, t2) -> Long.compare(
                t2.getCurrentPointsTally() + t2.getPredictedPointsTally(),
                t1.getCurrentPointsTally() + t1.getPredictedPointsTally()
                )).forEach(t -> {
            float finalPointsTally = t.getCurrentPointsTally() + t.getPredictedPointsTally();
            if (display)
                System.out.printf(format, leaguePosition, t.getTeamName(), finalPointsTally, t.getSimulatedLeaguePositions()[0]);
            else
                teamData.stream().filter(team -> team.getTeamName().equals(t.getTeamName())).forEach((team) -> {
                    team.setSimulatedLeaguePositions(leaguePosition);
                });
            leaguePosition++;
        });
    }

    public void showLeagueTableMultipleSimulations(ArrayList<Team> teamData, int numberOfSimulations) {
        String format = "%2s %-25s %4s";
        leaguePosition = 1;

        teamData.stream().sorted((t1, t2) -> Long.compare(
                t2.getCurrentPointsTally() + (t2.getTotalPredictedPointsTally() / numberOfSimulations),
                t1.getCurrentPointsTally() + (t1.getTotalPredictedPointsTally() / numberOfSimulations)
        )).forEach(t -> {
            double finalPointsTally = (double) t.getCurrentPointsTally() + ((double) t.getTotalPredictedPointsTally() / (double) numberOfSimulations);
            System.out.printf(format, leaguePosition, t.getTeamName(), (double) Math.round(finalPointsTally * 10) / 10);
            for (int f:t.getSimulatedLeaguePositions())
                System.out.printf("%5d ", f);
            System.out.println();
            if (leaguePosition == 4 || leaguePosition == 6 || leaguePosition == 17)
                System.out.println("-----------------------------------------");
            leaguePosition++;
        });
    }

    public String predictFixture(PredictorCalculations predictorCalculations, ArrayList<Team> teamData, String homeTeam, String awayTeam) {
        double averageHomeTeamGoals = teamData.stream().mapToDouble(Team::getHomeGoalsFor).average().orElse(0.0);
        double averageHomeTeamXG = teamData.stream().mapToDouble(Team::getHomeXGFor).average().orElse(0.0);
        double averageHomeTeamXGAgainst = teamData.stream().mapToDouble(Team::getHomeXGAgainst).average().orElse(0.0);
        double averageAwayTeamGoals = teamData.stream().mapToDouble(Team::getAwayGoalsFor).average().orElse(0.0);
        double averageAwayTeamXG = teamData.stream().mapToDouble(Team::getAwayXGFor).average().orElse(0.0);
        double averageAwayTeamXGAgainst = teamData.stream().mapToDouble(Team::getAwayXGAgainst).average().orElse(0.0);

        Team homeTeamData = teamData.stream().filter(t -> t.getTeamName().equals(homeTeam)).collect(Collectors.toList()).get(0);
        Team awayTeamData = teamData.stream().filter(t -> t.getTeamName().equals(awayTeam)).collect(Collectors.toList()).get(0);

        double homeAttackStrength = predictorCalculations.calculateStrengthRating(homeTeamData.getHomeXGFor(), averageHomeTeamXG);
        double homeDefenceStrength = predictorCalculations.calculateStrengthRating(homeTeamData.getHomeXGAgainst(), averageHomeTeamXGAgainst);
        double awayAttackStrength = predictorCalculations.calculateStrengthRating(awayTeamData.getAwayXGFor(), averageAwayTeamXG);
        double awayDefenceStrength = predictorCalculations.calculateStrengthRating(awayTeamData.getAwayXGAgainst(), averageAwayTeamXGAgainst);

        double homeExpGoals = predictorCalculations.calculatePredictedXG(homeAttackStrength, awayDefenceStrength, averageHomeTeamGoals);
        double awayExpGoals = predictorCalculations.calculatePredictedXG(awayAttackStrength, homeDefenceStrength, averageAwayTeamGoals);

        return predictorCalculations.predictGamePoisson(homeTeam, awayTeam, homeExpGoals, awayExpGoals);
    }

    public void runMultipleSimulations(PredictorCalculations predictorCalculations, ArrayList<Team> teamData, ArrayList<Fixture> remainingFixtures, int numberOfSimulations) {
        // reset total predicted points tally for each team
        teamData.forEach((team -> team.setTotalPredictedPointsTally(0)));

        // run simulations
        for (int simulation = 0; simulation < numberOfSimulations; simulation++) {
            predictRemainingFixtures(predictorCalculations, teamData, remainingFixtures);

            // add simulated points tally to total simulated points tally
            teamData.forEach((team -> {
                team.setTotalPredictedPointsTally(team.getTotalPredictedPointsTally() + team.getPredictedPointsTally());
            }));

            // update simulated league position team array
            showLeagueTableSingleSimulation(teamData, false);
        }

        System.out.println("After running " + numberOfSimulations + " simulations, the league table is:");
        showLeagueTableMultipleSimulations(teamData, numberOfSimulations);
    }

    public void predictRemainingFixtures(PredictorCalculations predictorCalculations, ArrayList<Team> teamData, ArrayList<Fixture> remainingFixtures) {
        // reset predicted points tally for each team
        teamData.forEach((team -> team.setPredictedPointsTally(0)));

        remainingFixtures.forEach((fixture -> {
            String resultSimulation = predictFixture(predictorCalculations, teamData, fixture.getHomeTeam(), fixture.getAwayTeam());
            switch (resultSimulation) {
                case "home win":
                    updateTeamPointsTally(teamData, fixture.getHomeTeam(), 3);
                    break;
                case "away win":
                    updateTeamPointsTally(teamData, fixture.getAwayTeam(), 3);
                    break;
                case "draw":
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
