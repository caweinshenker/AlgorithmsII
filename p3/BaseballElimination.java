import java.util.*;
import edu.princeton.cs.algs4.*;


public class BaseballElimination {

	private HashMap<String, Integer> teamNumbers;
	private int[] wins;
	private int[] losses;
	private int[] remaining;
	private int[][] against;
	private FordFulkerson ff;
	private String lastTeam;

	public BaseballElimination(String filename) {

		teamNumbers = new HashMap<String, Integer>();

		In in = new In(filename);
		int teams = in.readInt();

		wins = new int[teams];
		losses = new int[teams];
		against = new int [teams][teams];
		remaining = new int[teams];


		ArrayList<String> teamNames = new ArrayList<String>();
		ArrayList<ArrayList<Integer>> gamesToPlay = new ArrayList<ArrayList<Integer>>();

		lastTeam = null;

		for (int team= 0; team < teams ; team++) {
			String teamName = in.readString();
			teamNumbers.put(teamName, team);
			wins[team] = in.readInt();
			wins[team] = in.readInt();
			for (int opponent = 0; opponent < teams; opponent++) {
				against[team][opponent] = in.readInt();
			}
		}

		fillRemaining();
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

	private void fillRemaining() {
		for (int team = 0; team < numberOfTeams(); team++){
			int remainingGames = 0;
			for (int opponent = 0; opponent < numberOfTeams(); opponent++)
				remainingGames += against[team][opponent];
			remaining[team] = remainingGames;
		}
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

	public boolean isEliminated(String team) {
		if (!teamNumbers.containsKey(team))
			throw new java.lang.IllegalArgumentException("Invalid team");

		return isTriviallyEliminated(team) || isNonTriviallyEliminated(team);
	}

	private boolean isTriviallyEliminated(String team) {
		for (String opponent : teams()){
			if (wins(opponent) > remaining(team) + wins(team))
				return true;
		}
		return false;
	}

	private boolean isNonTriviallyEliminated(String team) {
		int teamNumber = teamNumbers.get(team);
		int noGames = numberOfTeams() * numberOfTeams();
		FlowNetwork flowNetwork = new FlowNetwork(noGames + numberOfTeams() + 2);
		int s = 0;
		int t = flowNetwork.V() - 1;
		int nodeIdx = 0;
		int capacity;
		int expected = 0;
		int maxwins;

		for (int teamNo = 0; teamNo < numberOfTeams(); teamNo++){
			if (teamNo == teamNumber) continue;
			for (int opponentNo = 0; opponentNo < numberOfTeams(); opponentNo++){
				if (opponentNo == teamNumber) continue;
				capacity = against[teamNo][opponentNo];
				nodeIdx = 1 + (teamNo * opponentNo) + opponentNo;
				if (teamNo != opponentNo){
					//s to games
					flowNetwork.addEdge(new FlowEdge(s, nodeIdx, capacity));
					//games to teams
					flowNetwork.addEdge(new FlowEdge(nodeIdx, nodeIdx + teamNo, Integer.MAX_VALUE));
					flowNetwork.addEdge(new FlowEdge(nodeIdx, nodeIdx + opponentNo, Integer.MAX_VALUE));
					//teams to t
				}
			}
			maxwins = wins[teamNo] + remaining[teamNo] - wins(team);
			expected += maxwins;
			flowNetwork.addEdge(new FlowEdge(nodeIdx + teamNo, t, maxwins));
		}

		FordFulkerson ff = new FordFulkerson(flowNetwork, s, t);
		if (ff.value() == expected)  return true;
		return false;
	}

	public Iterable<String> certificateOfElimination(String team) {
		if (!teamNumbers.containsKey(team))
			throw new java.lang.IllegalArgumentException("Invalid team");

		ArrayList<String> eliminatingTeams = new ArrayList<String>();

		if (isEliminated(team)) {
			for (String candidate : teams()) {
				int teamVertexIdx = teamNumbers.get(candidate) + numberOfTeams() * (numberOfTeams() + 1) + 1;
				if (ff.inCut(teamVertexIdx)) {
					eliminatingTeams.add(candidate);
				}
			}
		}

		return eliminatingTeams;
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
