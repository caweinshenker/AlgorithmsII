import org.junit.*;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.*;


public class BaseballEliminationShould {

	private BaseballElimination baseball = new BaseballElimination("baseball-testing/baseball/teams4.txt");

	@Test
	public void returnNumberOfTeams() {
		assertEquals(4, baseball.numberOfTeams());
	}

	@Test
	public void returnCorrectWinCount() {
		assertEquals(83, baseball.wins("Atlanta"));
		assertEquals(80, baseball.wins("Philadelphia"));
	}

	@Test
	public void returnCorrectLossCount() {
		assertEquals(8, baseball.remaining("Atlanta"));
		assertEquals(3, baseball.remaining("Montreal"));
	}


	@Test
	public void returnCorrectAgainstCount() {
		assertEquals(0, baseball.against("Atlanta", "Atlanta"));
		assertEquals(1, baseball.against("Atlanta","Philadelphia"));
		assertEquals(2, baseball.against("Philadelphia", "Montreal"));
	}

	// @Test public void calculateNumberOfGames() {
	// 	assertEquals(6, baseball.numberOfGames());
	// }


}
