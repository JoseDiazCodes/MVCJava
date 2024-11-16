import org.junit.Test;
import tictactoe.Player;
import tictactoe.TicTacToe;
import tictactoe.TicTacToeModel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test cases for the tic-tac-toe model. This test class verifies that the game state is
 * properly managed and that all game actions are properly validated according to standard
 * Tic Tac Toe rules.
 */
public class TicTacToeModelTest {
  /**
   * An instance of the Tic Tac Toe game that is reset before each test method runs.
   */
  private TicTacToe ttt1 = new TicTacToeModel();

  /**
   * Tests that after a single move, the turn switches to the other player. This verifies
   * the basic turn-taking mechanism of the game.
   */
  @Test
  public void testMove() {
    ttt1.move(0, 0);
    assertEquals(Player.O, ttt1.getTurn());
  }

  /**
   * Tests that a horizontal win is properly detected. This test verifies that player X can
   * win by placing three X's in the top row while the game correctly tracks game state.
   */
  @Test
  public void testHorizontalWin() {
    ttt1.move(0, 0); // X takes upper left
    assertFalse(ttt1.isGameOver());
    ttt1.move(1, 0); // O takes middle left
    ttt1.move(0, 1); // X takes upper middle
    assertNull(ttt1.getWinner());
    ttt1.move(2, 0); // O takes lower left
    ttt1.move(0, 2); // X takes upper right
    assertTrue(ttt1.isGameOver());
    assertEquals(Player.X, ttt1.getWinner());
    assertEquals(" X | X | X\n"
            + "-----------\n"
            + " O |   |  \n"
            + "-----------\n"
            + " O |   |  ", ttt1.toString());
  }

  /**
   * Tests that a diagonal win is properly detected. This test uses a helper method to set
   * up a scenario where O wins on a diagonal before the board is completely filled.
   */
  @Test
  public void testDiagonalWin() {
    diagonalWinHelper();
    assertTrue(ttt1.isGameOver());
    assertEquals(Player.O, ttt1.getWinner());
    assertEquals(" X | X | O\n"
            + "-----------\n"
            + " X | O |  \n"
            + "-----------\n"
            + " O |   |  ", ttt1.toString());
  }

  /**
   * A helper method that creates a game state where O wins on a diagonal. This method
   * places pieces to create a specific board configuration used by multiple tests.
   */
  private void diagonalWinHelper() {
    ttt1.move(0, 0); // X takes upper left
    assertFalse(ttt1.isGameOver());
    ttt1.move(2, 0); // O takes lower left
    ttt1.move(1, 0); // X takes middle left
    assertNull(ttt1.getWinner());
    ttt1.move(1, 1); // O takes center
    ttt1.move(0, 1); // X takes upper middle
    ttt1.move(0, 2); // O takes upper right
  }

  /**
   * Tests that invalid moves are properly rejected. This verifies that attempts to play in
   * occupied positions or out-of-bounds locations throw appropriate exceptions.
   */
  @Test
  public void testInvalidMove() {
    ttt1.move(0, 0);
    assertEquals(Player.O, ttt1.getTurn());
    assertEquals(Player.X, ttt1.getMarkAt(0, 0));
    try {
      ttt1.move(0, 0);
      fail("Invalid move should have thrown exception");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }
    try {
      ttt1.move(-1, 0);
      fail("Invalid move should have thrown exception");
    } catch (IllegalArgumentException iae) {
      assertTrue(iae.getMessage().length() > 0);
    }
  }

  /**
   * Tests that moves are not allowed after the game is over. This verifies that attempts
   * to play after a win condition throw an IllegalStateException.
   */
  @Test(expected = IllegalStateException.class)
  public void testMoveAttemptAfterGameOver() {
    diagonalWinHelper();
    ttt1.move(2, 2); // 2,2 is an empty position
  }

  /**
   * Tests that a cat's game (draw) is properly detected. This verifies that when the
   * board is filled with no winner, the game ends correctly with no winner declared.
   */
  @Test
  public void testCatsGame() {
    ttt1.move(0, 0);
    assertEquals(Player.O, ttt1.getTurn());
    ttt1.move(1, 1);
    assertEquals(Player.X, ttt1.getTurn());
    ttt1.move(0, 2);
    ttt1.move(0, 1);
    ttt1.move(2, 1);
    ttt1.move(1, 0);
    ttt1.move(1, 2);
    ttt1.move(2, 2);
    ttt1.move(2, 0);
    assertTrue(ttt1.isGameOver());
    assertNull(ttt1.getWinner());
    assertEquals(" X | O | X\n"
            + "-----------\n"
            + " O | O | X\n"
            + "-----------\n"
            + " X | X | O", ttt1.toString());
  }

  /**
   * Tests that attempting to get a mark at an invalid row throws an exception. This
   * verifies that the model properly validates row indices.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetMarkAtRow() {
    ttt1.getMarkAt(-12, 0);
  }

  /**
   * Tests that attempting to get a mark at an invalid column throws an exception. This
   * verifies that the model properly validates column indices.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetMarkAtCol() {
    ttt1.getMarkAt(0, -30);
  }

  /**
   * Tests that the getBoard method returns a defensive copy of the board. This verifies
   * that attempts to modify the returned board do not affect the actual game state.
   */
  @Test
  public void testGetBoard() {
    diagonalWinHelper();
    Player[][] bd = ttt1.getBoard();
    assertEquals(Player.X, bd[0][0]);
    assertEquals(Player.O, bd[1][1]);
    assertEquals(Player.X, bd[0][1]);
    // attempt to cheat by mutating board returned by getBoard()
    // check correct preconditions
    assertEquals(Player.O, bd[2][0]);
    assertEquals(Player.O, ttt1.getMarkAt(2, 0));
    bd[2][0] = Player.X; // mutate
    // check correct post conditions
    assertEquals(Player.O, ttt1.getMarkAt(2, 0));
    Player[][] bd2 = ttt1.getBoard();
    assertEquals(Player.O, bd2[2][0]);
  }

  /**
   * Tests that a win is properly detected even when it occurs on a full board. This
   * verifies that the game correctly identifies a winner in edge cases where the winning
   * move is the final move of the game.
   */
  @Test
  public void testWinOnFullBoard() {
    // Play a game where X wins with a diagonal on the last move
    ttt1.move(0, 0); // X
    ttt1.move(0, 1); // O
    ttt1.move(1, 1); // X
    ttt1.move(0, 2); // O
    ttt1.move(2, 0); // X
    ttt1.move(1, 0); // O
    ttt1.move(1, 2); // X
    ttt1.move(2, 1); // O
    ttt1.move(2, 2); // X wins on diagonal

    assertTrue(ttt1.isGameOver());
    assertEquals(Player.X, ttt1.getWinner());
    // Verify board is full
    Player[][] board = ttt1.getBoard();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        assertTrue(board[i][j] != null);
      }
    }
  }
}