
// import org.junit.Test;
// import static org.junit.Assert.*;
//
// public class MyUnitTest {
//
//     @Test
//     public void testConcatenate() {
//         MyUnit myUnit = new MyUnit();
//
//         String result = myUnit.concatenate("one", "two");
//
//         assertEquals("onetwo", result);
//
//     }
// }



import org.junit.Test;
import static org.junit.Assert.*;


public class BaseballEliminationShould {

	BaseballElimination baseball = new BaseballElimination("baseball-testing/baseball/teams4.txt");

	@Test
	public void returnNumberOfTeams() {
		assertEquals(4, baseball.numberOfTeams());
	}

	@Test
	public void returnCorrectWinCount() {
		assertEquals(83, baseball.wins("Atlanta"));
		assertEquals(80, baseball.wins("Philadelphia"));
	}
}
