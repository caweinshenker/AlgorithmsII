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
			//StdOut.printf("%s %d %d %d\n", names[team], wins[team], losses[team], remaining[team]);
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

		return losses[teamNumbers.get(team)];
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

		if (isTriviallyEliminated(team)) return true;
		if (isNonTriviallyEliminated(team)) return true;
		return false;
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
		int noGames = numberOfGames();

		if (ff == null){
			isNonTriviallyEliminated(team);
		}

		for (int teamNo = 0; teamNo < numberOfTeams(); teamNo++){
			if (ff.inCut(1 + noGames + teamNo))
				eliminators.add(names[teamNo]);
		}
		return eliminators.size() == 0 ? null : eliminators;
	}


	private boolean isTriviallyEliminated(String team) {
		return trivialEliminators(team).size() > 0;
	}

	private int numberOfGames() {
		int games = 0;
		for (int team = 0; team < numberOfTeams(); team++) {
			games += numberOfTeams() - team - 1;
		}
		return games;
	}

	private boolean isNonTriviallyEliminated(String team) {
		int teamNumber = teamNumbers.get(team);
		int noTeams = numberOfTeams();
		int noGames = numberOfGames();
		FlowNetwork flowNetwork = new FlowNetwork(noGames + noTeams + 2);
		int s = 0;
		int t = flowNetwork.V() - 1;
		int gamesLeft;
		int expected = 0;
		int gameStart = 1;
		int teamStart = 1 + noGames;
		int game = 0;

		//s to games and games to teams
		for (int teamNo = 0; teamNo < noTeams; teamNo++) {
			for (int opponentNo = teamNo + 1; opponentNo < noTeams; opponentNo++) {
				if (teamNumber == teamNo || teamNumber == opponentNo) {
					continue;
				}
				gamesLeft = against(teamName(teamNo), teamName(opponentNo));
				expected += gamesLeft;

				//StdOut.printf("Game: %d, Team: %d, Opponent: %d\n", game, teamNo, opponentNo);
				flowNetwork.addEdge(new FlowEdge(s, 1 + game, gamesLeft));
				flowNetwork.addEdge(new FlowEdge(1 + game, teamStart + teamNo, Double.POSITIVE_INFINITY));
				flowNetwork.addEdge(new FlowEdge(1 + game, teamStart + opponentNo, Double.POSITIVE_INFINITY));
				game++;
			}
		}

		//teams to t
		for (int teamNo = 0; teamNo < noTeams; teamNo++) {
			if (teamNumber == teamNo) continue;
			int maxWins = Math.max(wins(team) + remaining(team) - wins[teamNo], 0);
			flowNetwork.addEdge(new FlowEdge(teamStart + teamNo, t, maxWins));
		}

		ff = new FordFulkerson(flowNetwork, s, t);

		//StdOut.printf("%s", flowNetwork.toString());

		for (FlowEdge e : flowNetwork.adj(s)) {
			if ((int) e.capacity() != (int) e.flow())
				return true;
		}

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
