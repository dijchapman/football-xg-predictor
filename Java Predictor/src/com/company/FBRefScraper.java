package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class FBRefScraper {

    public ArrayList<Team> getTeamData() {
        try {
            ArrayList<Team> teamData = new ArrayList<>();
            String urlLeagueSeason = "https://fbref.com/en/comps/9/Premier-League-Stats";
            final Document document = Jsoup.connect(urlLeagueSeason).get();
            for (Element row : document.select("table#results111601_home_away tr")) {
                if (row.select("td.left").toString().contains("<a href")) {
                    String teamName = row.select("td.left").text();
                    int homeGames = Integer.parseInt(row.select("td.group_start.right:nth-of-type(2)").text());
                    double homeGoalsFor = Double.parseDouble(row.select("td.right:nth-of-type(6)").text()) / homeGames;
                    double homeGoalsAgainst = Double.parseDouble(row.select("td.right:nth-of-type(7)").text()) / homeGames;
                    double homeXGoalsFor = Double.parseDouble(row.select("td.right:nth-of-type(11)").text()) / homeGames;
                    double homeXGoalsAgainst = Double.parseDouble(row.select("td.right:nth-of-type(12)").text()) / homeGames;

                    int awayGames = Integer.parseInt(row.select("td.group_start.right:nth-of-type(15)").text());
                    double awayGoalsFor = Double.parseDouble(row.select("td.right:nth-of-type(19)").text()) / awayGames;
                    double awayGoalsAgainst = Double.parseDouble(row.select("td.right:nth-of-type(20)").text()) / awayGames;
                    double awayXGoalsFor = Double.parseDouble(row.select("td.right:nth-of-type(24)").text()) / awayGames;
                    double awayXGoalsAgainst = Double.parseDouble(row.select("td.right:nth-of-type(25)").text()) / awayGames;

                    int pointsTally = Integer.parseInt(row.select("td.right:nth-of-type(9)").text()) + Integer.parseInt(row.select("td.right:nth-of-type(22)").text());

                    Team newTeam = new Team(teamName, homeGoalsFor, homeGoalsAgainst, homeXGoalsFor, homeXGoalsAgainst, awayGoalsFor, awayGoalsAgainst, awayXGoalsFor, awayXGoalsAgainst, pointsTally);
                    teamData.add(newTeam);
                }
            }

            return teamData;
        } catch (Exception e) {
            System.out.println("getLeagueData: " + e.getMessage());
        }

        return null;
    }

    public void getRemainingFixtures() {
        try {
            ArrayList<Team> teamData = new ArrayList<>();
            String urlLeagueSeason = "https://fbref.com/en/comps/9/schedule/Premier-League-Scores-and-Fixtures";
            final Document document = Jsoup.connect(urlLeagueSeason).get();
            for (Element row : document.select("table#sched_11160_1 tr")) {
                if (row.select("td.right:nth-of-type(4)").toString().contains("<a href") && row.select("td.right:nth-of-type(5)").text().isEmpty()) {
                    String homeTeam = row.select("td.right:nth-of-type(4)").text();
                    String awayTeam = row.select("td.left:nth-of-type(8)").text();

                    System.out.println(homeTeam + " vs " + awayTeam);
                }
            }
        } catch (Exception e) {
            System.out.println("getRemainingFixtures: " + e.getMessage());
        }
    }
}
