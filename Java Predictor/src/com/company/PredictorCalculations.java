package com.company;

public class PredictorCalculations {
    public double calculateStrengthRating(double teamAverage, double leagueAverage) {
        return teamAverage / leagueAverage;
    }

    public double calculatePredictedXG(double attackStrength, double defenceStrength, double leagueGoalsAverage) {
        return attackStrength * defenceStrength * leagueGoalsAverage;
    }

    public String predictGamePoisson(String homeTeam, String awayTeam, double homeExpGoals, double awayExpGoals) {
        double[] homeGoalChances = goalChances(homeExpGoals);
        double[] awayGoalChances = goalChances(awayExpGoals);

        double homeWinChance = 0.0;
        double awayWinChance = 0.0;
        double drawChance = 0;
        String format = "%20s %5s %5s %5s %5s %5s %6s %6s%n";
        // System.out.println("                      " + awayTeam);
        // System.out.printf(format, homeTeam, "0", "1", "2", "3", "4", "5+", "Total");
        double[] awayGoalChanceTotals = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        for (int homeGoals = 0; homeGoals < 6; homeGoals++) {
            double[] chances = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
            double total = 0;
            for (int awayGoals = 0; awayGoals < 6; awayGoals++) {
                double resultChance = (double) Math.round(homeGoalChances[homeGoals] * awayGoalChances[awayGoals] * 1000) / 10;
                chances[awayGoals] = resultChance;
                total += resultChance;

                awayGoalChanceTotals[awayGoals] += resultChance;

                if (homeGoals > awayGoals)
                    homeWinChance += resultChance;
                else if (homeGoals < awayGoals)
                    awayWinChance += resultChance;
                else
                    drawChance += resultChance;
            }
            // System.out.printf(format, homeGoals, chances[0], chances[1], chances[2], chances[3], chances[4], chances[5], (double) Math.round(total * 10) / 10);
        }

        for (int awayGoals = 0; awayGoals < 6; awayGoals++) { awayGoalChanceTotals[awayGoals] = (double) Math.round(awayGoalChanceTotals[awayGoals] * 10) / 10; }

        homeWinChance = (double) Math.round(homeWinChance * 10) / 10;
        awayWinChance = (double) Math.round(awayWinChance * 10) / 10;
        drawChance = (double) Math.round(drawChance * 10) / 10;

        // System.out.printf(format, "Total", awayGoalChanceTotals[0], awayGoalChanceTotals[1], awayGoalChanceTotals[2], awayGoalChanceTotals[3], awayGoalChanceTotals[4], awayGoalChanceTotals[5], "100.0");
        // System.out.println(homeTeam + " Win Chance: " + homeWinChance + "%");
        // System.out.println(awayTeam + " Win Chance: " + awayWinChance + "%");
        // System.out.println("Draw Chance: " + drawChance + "%");

        return simulateGame(homeGoalChances, awayGoalChances, homeExpGoals, awayExpGoals);
    }

    public String simulateGame(double[] homeGoalChances, double[] awayGoalChances, double homeExpGoals, double awayExpGoals) {
        int homeGoalsSimulation = goalsScoredSimulation(homeGoalChances);
        int awayGoalsSimulation = goalsScoredSimulation(awayGoalChances);

        if (homeGoalsSimulation > awayGoalsSimulation) {
            return "home win";
        }
        else if (awayGoalsSimulation > homeGoalsSimulation) {
            return "away win";
        }

        return "draw";
    }

    public double[] goalChances(double expGoals) {
        double[] goalChances = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        double chanceTotal = 0.0;
        for (int goals = 1; goals < 6; goals++) {
            double chance = calculateGoalsPoisson(expGoals, goals);
            chanceTotal += chance;
            goalChances[goals] = chance;
        }
        goalChances[0] = 1 - chanceTotal;

        return goalChances;
    }

    public Double calculateGoalsPoisson(double mean, int goals) {
        int factorial = goals;
        for (int fact = factorial - 1; fact > 0; fact--) {
            factorial *= fact;
        }
        return (Math.pow(mean, goals) * Math.exp(-mean)) / factorial;
    }

    public int goalsScoredSimulation(double[] goalChances) {
        double chanceTally = 0.0;
        double goalSimulation = Math.random();
        for (int goals = 0; goals < 6; goals++) {
            chanceTally += goalChances[goals];

            if (goalSimulation < chanceTally)
                return goals;
        }

        return 0;
    }
}
