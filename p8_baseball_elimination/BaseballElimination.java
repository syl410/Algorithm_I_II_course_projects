import java.util.HashMap;
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {
	private final int num; // team number
	private final HashMap<String, int[]> teamMap;
	private final String[] teamArr;
	private final int[][] gameLeft;
	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		In in = new In(filename);
		num = in.readInt();		
		teamMap = new HashMap<>();
		teamArr = new String[num];
		gameLeft = new int[num][num];

		for (int i = 0; !in.isEmpty(); i++) {
			// read: New_York    75 59 28 (teamName, win, lose, rest)
			int[] record = new int[4];
			String teamName = in.readString();
			teamArr[i] = teamName;
			record[0] = i; // team position
			record[1] = in.readInt(); // win
			record[2] = in.readInt(); // lose
			record[3] = in.readInt(); // rest
			teamMap.put(teamName, record);

			for (int j = 0; j < num; j++) {
				gameLeft[i][j] = in.readInt();
			}
		}
	}
	// verify if input team name is valid or not
	private void verifyTeamName(String teamName) {
		if (!teamMap.containsKey(teamName)) throw new IllegalArgumentException();
	}
	// number of teams
	public int numberOfTeams() {
		return num;
	}
	// all teams
	public Iterable<String> teams() {
		return teamMap.keySet();	
	}
	// number of wins for given team
	public int wins(String team) {
		verifyTeamName(team);
		return teamMap.get(team)[1];		
	}
	// number of losses for given team
	public int losses(String team) {
		verifyTeamName(team);
		return teamMap.get(team)[2];
	}
	// number of remaining games for given team
	public int remaining(String team) {
		verifyTeamName(team);
		return teamMap.get(team)[3];	
	}
	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		verifyTeamName(team1);
		verifyTeamName(team2);
		int team1Pos = teamMap.get(team1)[0];
		int team2Pos = teamMap.get(team2)[0];
		return gameLeft[team1Pos][team2Pos];	
	}
	// is given team eliminated?
	public boolean isEliminated(String team) {
		verifyTeamName(team);
		// max wins the "team" can win
		int maxWins = wins(team) + remaining(team);
		// 1, check if team max wins is less than other team wins
		for (String teamName : teams()) {
			int teamWins = wins(teamName);
			if (teamWins > maxWins) return true;
		}

		// 2.1, create flow network
		FlowNetwork flowNW = createFlowNW(team);

		// 2.1, find FordFulkerson max flow
		FordFulkerson ff = new FordFulkerson(flowNW, 0, flowNW.V() - 1);
		for (FlowEdge e : flowNW.adj(0)) {
			if (e.flow() != e.capacity()) {
				return true;
			}
		}
		return false;
	}
	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		verifyTeamName(team);
		int restNum = num - 1; // team number except teamName
		int matchPair = restNum * (restNum - 1) / 2;
		int pos = teamMap.get(team)[0];// position of team
		int maxWins = wins(team) + remaining(team);

		ArrayList<String> R = new ArrayList<>();

		// 1, check if team max wins is less than other team wins
		for (String teamName : teams()) {
			int teamWins = wins(teamName);
			if (teamWins > maxWins) R.add(teamName);
		}

		// R is non-zero. It is trivial case.
		if (R.size() > 0) return R;

		// 2, non-trival case
		// create flow network
		FlowNetwork flowNW = createFlowNW(team);

		// find FordFulkerson max flow
		int t = flowNW.V() - 1; // last vertex, destination
		int firstTeam = 1 + matchPair;
		FordFulkerson ff = new FordFulkerson(flowNW, 0, t);

		// check if edges from 0 are all full
		// if yes, it is eliminated
		// if no, it is eliminated
		boolean isEliminated = false;
		for (FlowEdge e : flowNW.adj(0)) {
			if (e.flow() != e.capacity()) {
				isEliminated = true;
				break;
			}
		}

		// it is not eliminated
		if (!isEliminated) return R;
		// find all teams can eliminate the team
		for (FlowEdge e : flowNW.adj(t)) {
			if (e.flow() == e.capacity()) {
				int v = e.other(t);
				int row = v - firstTeam;
				if (row >= pos) row++;
				R.add(teamArr[row]);
			}
		}
		return R;
	}

	private FlowNetwork createFlowNW(String teamName) {
		int restNum = num - 1; // team number except teamName
		int matchPair = restNum * (restNum - 1) / 2;
		int flowSize = 1 + matchPair + restNum + 1;

		// build flow network
		FlowNetwork flowNW = new FlowNetwork(flowSize);

		int teamPos = teamMap.get(teamName)[0];
		int totalWins = wins(teamName) + remaining(teamName);

		int gameV = 1; // game vertices
		int team1 = 0;
		int restGame = 0; // calculate total games except games of teamName
		for (int row = 0; row < num; row++) {
			if (row == teamPos) continue;
			else {
				int team2 = team1 + 1; // match vertices
				for (int col = row + 1; col < num; col++) {
					if (col != teamPos) {
						restGame += gameLeft[row][col];
						// add edges from s to game vertices
						flowNW.addEdge(new FlowEdge(0, gameV, gameLeft[row][col]));
						// add edges from game vertices to team vertices
						flowNW.addEdge(new FlowEdge(gameV, 1 + matchPair + team1, Double.POSITIVE_INFINITY));
						flowNW.addEdge(new FlowEdge(gameV, 1 + matchPair + team2, Double.POSITIVE_INFINITY));
						team2++;
						gameV++;
					}
				}
				
				int cap = totalWins - wins(teamArr[row]);
				flowNW.addEdge(new FlowEdge(1 + matchPair + team1, flowSize - 1, cap));
				team1++;
			}
		}

		return flowNW;
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
