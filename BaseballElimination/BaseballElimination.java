import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Given the standings in a sports division at some point during the season, determine which teams have been mathematically eliminated from winning their division.
public class BaseballElimination {
    private final HashMap<String, Integer> teams = new HashMap<>();
    private final HashMap<Integer, String> invTeams = new HashMap<>();
    private final HashMap<Integer, List<String>> eliminated = new HashMap<>();
    private int[][] games;
    private int[] wins;
    private int[] loss;
    private int[] left;

    // create a baseball division from given filename in the given format
    public BaseballElimination(String filename) {
        parseTeams(filename);
    }

    // number of teams
    public int numberOfTeams() {
        return teams.size();
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        return wins[validateTeam(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        return loss[validateTeam(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return left[validateTeam(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return games[validateTeam(team1)][validateTeam(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        int index = validateTeam(team);

        return eliminated.get(index) != null || trivialElimination(index) || maxFlowElimination(index);
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        int index = validateTeam(team);

        List<String> eliminatedList = eliminated.get(index);
        if (eliminatedList != null) {
            return eliminatedList;
        }

        if (isEliminated(team)) {
            return eliminated.get(index);
        }

        return null;
    }

    private boolean maxFlowElimination(int index) {
        FordFulkerson network = createNetwork(index);
        List<String> result = new ArrayList<>();

        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == index) {
                continue;
            }

            if (network.inCut(i)) {
                result.add(invTeams.get(i));
            }
        }

        if (!result.isEmpty()) {
            eliminated.put(index, result);

            return true;
        }

        return false;
    }

    private FordFulkerson createNetwork(int index) {
        int points = wins[index] + left[index];
        int teamNo = numberOfTeams();
        //games between teams, except for 'index', are ((k-1)*(k-1) - (k-1))/2, plus s and t nodes
        int totalNodes = teamNo + ((teamNo - 1) * (teamNo - 1) - (teamNo - 1)) / 2 + 2;
        int s = totalNodes - 2;
        int t = totalNodes - 1;

        FlowNetwork net = new FlowNetwork(totalNodes);

        //add teams
        for (int i = 0; i < teamNo; i++) {
            if (i == index) {
                continue;
            }

            net.addEdge(new FlowEdge(i, t, points - wins[i]));
        }

        //add games
        int vertInd = teamNo;
        for (int i = 0; i < teamNo; i++) {
            for (int j = i + 1; j < teamNo; j++) {
                if (i == index || j == index) {
                    continue;
                }

                net.addEdge(new FlowEdge(s, vertInd, games[i][j]));
                net.addEdge(new FlowEdge(vertInd, i, Double.POSITIVE_INFINITY));
                net.addEdge(new FlowEdge(vertInd, j, Double.POSITIVE_INFINITY));

                vertInd++;
            }
        }

        return new FordFulkerson(net, s, t);
    }

    private boolean trivialElimination(int index) {
        List<String> result = new ArrayList<>();
        int points = wins[index] + left[index];

        for (int i = 0; i < numberOfTeams(); i++) {
            if (points < wins[i]) {
                result.add(invTeams.get(i));
            }
        }

        if (!result.isEmpty()) {
            eliminated.put(index, result);

            return true;
        }

        return false;
    }

    private void parseTeams(String filename) {
        In in = new In(filename);

        int count = in.readInt();

        wins = new int[count];
        loss = new int[count];
        left = new int[count];
        games = new int[count][count];

        for (int i = 0; i < count; i++) {
            String team = in.readString();
            teams.put(team, i);
            invTeams.put(i, team);
            wins[i] = in.readInt();
            loss[i] = in.readInt();
            left[i] = in.readInt();

            for (int j = 0; j < count; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    private int validateTeam(String team) {
        Integer index = teams.get(team);

        if (index == null) {
            throw new IllegalArgumentException("Illegal team");
        }

        return index;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("baseball/teams5.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
