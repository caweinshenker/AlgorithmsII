import java.util.*;
import edu.princeton.cs.algs4.*;


public class BaseballElimination {

	private HashMap<String, Integer> teamNumbers;
	private int[] wins;
	private int[] losses;
	private int[] remaining;
	private int[][] against;
	private String[] names;
	private FordFulkerson ff;

	public BaseballElimination(String filename) {

		teamNumbers = new HashMap<String, Integer>();

		In in = new In(filename);
		int teams = in.readInt();

		wins = new int[teams];
		losses = new int[teams];
		against = new int [teams][teams];
		remaining = new int[teams];
		names = new String[teams];
		ff = null;

		for (int team= 0; team < teams ; team++) {
			String teamName = in.readString();
			teamNumbers.put(teamName, team);
			names[team] = teamName;
			wins[team] = in.readInt();
			losses[team] = in.readInt();
			remaining[team] = in.readInt();
			for (int opponent = 0; opponent < teams; opponent++) {
				against[team][opponent] = in.readInt();
			}
		}
	}

	public int numberOfTeams() {
		return teamNumbers.size();
	}

	public Iterable<String> teams() {
		return teamNumbers.keySet();
	}

	public int wins(String team) {
		if (!teamNumbers.containsKey(team))
			throw new java.lang.IllegalArgumentException("Invalid team");

		return wins[teamNumbers.get(team)];
	}

	public int losses(String team) {
		if (!teamNumbers.containsKey(team))
			throw new java.lang.IllegalArgumentException("Invalid team");

		return wins[teamNumbers.get(team)];
	}

	public int remaining(String team) {
		if (!teamNumbers.containsKey(team))
			throw new java.lang.IllegalArgumentException("Invalid team");

		return remaining[teamNumbers.get(team)];
	}

	public int against(String team1, String team2) {
		if (!teamNumbers.containsKey(team1) || !(teamNumbers.containsKey(team2)))
			throw new java.lang.IllegalArgumentException("Invalid team");

		return against[teamNumbers.get(team1)][teamNumbers.get(team2)];
	}

	private String teamName(int id) {
		return names[id];
	}

	public boolean isEliminated(String team) {
		if (!teamNumbers.containsKey(team))
			throw new java.lang.IllegalArgumentException("Invalid team");

		return isTriviallyEliminated(team) || isNonTriviallyEliminated(team);
	}

	private ArrayList<String> trivialEliminators(String team) {
		ArrayList<String> eliminators = new ArrayList<String>();
		for (String opponent : teams()){
			if (wins(opponent) > remaining(team) + wins(team))
				eliminators.add(opponent);
		}
		return eliminators;
	}

	private ArrayList<String> nonTrivialEliminators(String team) {
		ArrayList<String> eliminators = new ArrayList<String>();
		for (String opponent : teams()) {
			int teamVertexIdx = teamNumbers.get(opponent) + 1 + (numberOfTeams() * numberOfTeams()) / 2;
			if (ff.inCut(teamVertexIdx)) {
				eliminators.add(opponent);
			}
		}
		return eliminators;
	}

	private boolean isTriviallyEliminated(String team) {
		return trivialEliminators(team).size() > 0;
	}

	private boolean isNonTriviallyEliminated(String team) {
		int teamNumber = teamNumbers.get(team);
		int noTeams = numberOfTeams();
		int noGames = (noTeams * noTeams) / 2;
		FlowNetwork flowNetwork = new FlowNetwork(noGames + noTeams + 2);
		int s = 0;
		int t = flowNetwork.V() - 1;
		int gamesLeft;
		int expected = 0;
		int maxWins;
		int gameStart = 1;
		int teamStart = 1 + noGames;
		int teamNo;
		int opponentNo;

		//s to games and games to teams
		for (int game = 0; game < noGames; game++) {
			teamNo = game / noTeams;
			opponentNo = game % noTeams;
			
			if (teamNo == teamNumber || opponentNo == teamNumber) continue;

			gamesLeft = against(teamName(teamNo), teamName(opponentNo));

			flowNetwork.addEdge(new FlowEdge(s, 1 + game, gamesLeft));
			flowNetwork.addEdge(new FlowEdge(1 + game, teamStart + teamNo, Double.POSITIVE_INFINITY));
			flowNetwork.addEdge(new FlowEdge(1 + game, teamStart + opponentNo, Double.POSITIVE_INFINITY));
		}

		//teams to t
		for (teamNo = 0; teamNo < noTeams; teamNo++) {
			if (teamNo == teamNumber) continue;
			maxWins = Math.max(wins(team) + remaining(team) - wins[teamNo], 0);
			expected += maxWins;
			flowNetwork.addEdge(new FlowEdge(teamStart + teamNo, t, maxWins));
		}

		ff = new FordFulkerson(flowNetwork, s, t);
		if (ff.value() == expected)  return true;
		return false;
	}

	public Iterable<String> certificateOfElimination(String team) {
		if (!teamNumbers.containsKey(team))
			throw new java.lang.IllegalArgumentException("Invalid team");

		ArrayList<String> eliminators = trivialEliminators(team);
		eliminators.addAll(nonTrivialEliminators(team));
		return new HashSet<String>(eliminators);
	}

	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}
