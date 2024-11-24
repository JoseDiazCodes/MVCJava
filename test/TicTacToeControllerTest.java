import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;
import tictactoe.FailingAppendable;
import tictactoe.TicTacToe;
import tictactoe.TicTacToeConsoleController;
import tictactoe.TicTacToeController;
import tictactoe.TicTacToeModel;

/**
 * Tests for the TicTacToe controller implementation. Verifies game flow, input handling,
 * error conditions, and proper game completion scenarios using mock I/O objects.
 */

public class TicTacToeControllerTest {

  /**
   * Tests that a complete game can be played where one player (X) wins.
   */
  @Test
  public void testGameWithWinner() {
    TicTacToe m = new TicTacToeModel();
    // X wins with top row
    StringReader input = new StringReader("1 1 2 1 1 2 2 2 1 3");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
    assertTrue(gameLog.toString().contains("Game is over! X wins."));
  }

  /**
   * Tests that the game can be quit by entering 'q' when prompted for row input.
   */
  @Test
  public void testQuitInsteadOfRow() {
    TicTacToe m = new TicTacToeModel();
    StringReader input = new StringReader("q 2");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
    assertTrue(gameLog.toString().contains("Game quit! Ending game state:"));
  }

  /**
   * Tests that the game can be quit by entering 'q' when prompted for column input.
   */
  @Test
  public void testQuitInsteadOfColumn() {
    TicTacToe m = new TicTacToeModel();
    StringReader input = new StringReader("1 q");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
    assertTrue(gameLog.toString().contains("Game quit! Ending game state:"));
  }

  /**
   * Tests that non-numeric input for row coordinate is properly handled with error message.
   */
  @Test
  public void testInvalidRowInput() {
    TicTacToe m = new TicTacToeModel();
    StringReader input = new StringReader("abc 2 q");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
    assertTrue(gameLog.toString().contains("Please enter numbers for position."));
  }

  /**
   * Tests that non-numeric input for column coordinate is properly handled with error message.
   */
  @Test
  public void testInvalidColumnInput() {
    TicTacToe m = new TicTacToeModel();
    StringReader input = new StringReader("1 abc q");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
    assertTrue(gameLog.toString().contains("Please enter numbers for position."));
  }

  /**
   * Tests that moves outside the game board boundaries are rejected with error message.
   */

  @Test
  public void testOutOfBoundsMove() {
    TicTacToe m = new TicTacToeModel();
    StringReader input = new StringReader("4 4 q");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
    assertTrue(gameLog.toString().contains("Invalid move. Try again."));
  }

  /**
   * Tests that attempts to move in an already occupied cell are rejected with error message.
   */
  @Test
  public void testOccupiedCell() {
    TicTacToe m = new TicTacToeModel();
    // Try to place in same spot twice
    StringReader input = new StringReader("2 2 2 2 q");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
    assertTrue(gameLog.toString().contains("Invalid move. Try again."));
  }

  /**
   * Tests handling of multiple invalid moves in sequence (out of bounds, occupied, non-numeric).
   */
  @Test
  public void testMultipleInvalidMoves() {
    TicTacToe m = new TicTacToeModel();
    // Multiple invalid moves: out of bounds, occupied cell, invalid input
    StringReader input = new StringReader("4 4 2 2 2 2 abc 2 q");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
    String output = gameLog.toString();
    assertTrue(output.contains("Invalid move. Try again."));
    assertTrue(output.contains("Please enter numbers for position."));
  }


  /**
   * Tests a game with mixture of valid and invalid moves to ensure proper game flow.
   */
  @Test
  public void testValidAndInvalidMoves() {
    TicTacToe m = new TicTacToeModel();
    // Mix of valid and invalid moves leading to X win
    // Make sure we end input with a q to avoid NoSuchElementException
    StringReader input = new StringReader("2 2 abc 2 1 1 4 4 3 3 1 2 1 3 q");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
    String output = gameLog.toString();
    assertTrue(output.contains("Please enter numbers for position."));
    assertTrue(output.contains("Invalid move. Try again."));
  }

  /**
   * Tests that proper exception is thrown when input ends unexpectedly during game.
   */
  @Test(expected = IllegalStateException.class)
  public void testAbruptInputEnd() {
    TicTacToe m = new TicTacToeModel();
    // Input ends without quit and before game is over
    StringReader input = new StringReader("2 2");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
  }

  /**
   * Tests that a single valid move can be made and verified, followed by game quit.
   */
  @Test
  public void testSingleValidMove() {
    TicTacToe m = new TicTacToeModel();
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(new StringReader("2 2 q"), gameLog);
    c.playGame(m);
    assertEquals("   |   |  \n"
        + "-----------\n"
        + "   |   |  \n"
        + "-----------\n"
        + "   |   |  \n"
        + "Enter a move for X:\n"
        + "   |   |  \n"
        + "-----------\n"
        + "   | X |  \n"
        + "-----------\n"
        + "   |   |  \n"
        + "Enter a move for O:\n"
        + "Game quit! Ending game state:\n"
        + "   |   |  \n"
        + "-----------\n"
        + "   | X |  \n"
        + "-----------\n"
        + "   |   |  \n", gameLog.toString());
  }

  /**
   * Tests handling of invalid input for row coordinate with specific error formatting.
   */
  @Test
  public void testBogusInputAsRow() {
    TicTacToe m = new TicTacToeModel();
    StringReader input = new StringReader("!#$ 2 q");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);

    System.out.println("=== Actual Output ===");
    System.out.println(gameLog.toString());
    System.out.println("=== Line Count: " + gameLog.toString().split("\n").length + " ===");

    String[] lines = gameLog.toString().split("\n");
    String lastMsg = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 6, lines.length));
    assertEquals("Game quit! Ending game state:\n"
            + "   |   |  \n"
            + "-----------\n"
            + "   |   |  \n"
            + "-----------\n"
            + "   |   |  ", lastMsg);
  }

  /**
   * Tests that a complete game can reach a tie state with proper output.
   */
  @Test
  public void testTieGame() {
    TicTacToe m = new TicTacToeModel();
    // note the entire sequence of user inputs for the entire game is in this one string:
    StringReader input = new StringReader("2 2 1 1 3 3 1 2 1 3 2 3 2 1 3 1 3 2");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
    String[] lines = gameLog.toString().split("\n");
    assertEquals(60, lines.length);
    assertEquals("Game is over! Tie game.", lines[lines.length - 1]);
  }

  /**
   * Tests that IOException from Appendable is properly converted to IllegalStateException.
   */
  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    TicTacToe m = new TicTacToeModel();
    StringReader input = new StringReader("2 2 1 1 3 3 1 2 1 3 2 3 2 1 3 1 3 2");
    Appendable gameLog = new FailingAppendable();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
  }

}
