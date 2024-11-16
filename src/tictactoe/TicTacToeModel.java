package tictactoe;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A model implementation of the game Tic Tac Toe. This class manages a 3x3 game board and
 * enforces standard Tic Tac Toe rules. The game supports two players (X and O) who take
 * turns placing their marks on the board. X always plays first. A game ends when either
 * player achieves three of their marks in a row (horizontally, vertically, or diagonally)
 * or when the board becomes full with no winner (a draw). This implementation prevents
 * illegal moves and maintains game state, including tracking the current player's turn
 * and determining when the game is over.
 */
public class TicTacToeModel implements TicTacToe {
  /**
   * A 3x3 grid that represents the Tic Tac Toe board. Each cell can contain either an X
   * player, an O player, or null if the cell is empty.
   */
  private final Player[][] board;

  /**
   * Tracks which player's turn it is to make a move. This value alternates between X and O
   * throughout the game, starting with X.
   */
  private Player currentTurn;

  /**
   * Stores the player who has won the game. This value remains null until a player wins by
   * getting three marks in a row, column, or diagonal.
   */
  private Player winner;

  /**
   * Indicates whether the game has ended. This becomes true when either a player wins or
   * the board becomes full with no winner (a draw).
   */
  private boolean gameOver;

  /**
   * Constructs a new game of Tic Tac Toe with an empty 3x3 board. The game begins with
   * player X's turn, no winner, and the game in progress.
   */
  public TicTacToeModel() {
    this.board = new Player[3][3];
    this.currentTurn = Player.X; // X goes first
    this.winner = null;
    this.gameOver = false;
  }

  @Override
  public void move(int r, int c) {
    if (r < 0 || r >= 3 || c < 0 || c >= 3) {
      throw new IllegalArgumentException("Position out of bounds");
    }
    if (isGameOver()) {
      throw new IllegalStateException("Game is already over");
    }
    if (board[r][c] != null) {
      throw new IllegalArgumentException("Position already occupied");
    }

    board[r][c] = currentTurn;

    // Check for win conditions
    if (checkWin()) {
      winner = currentTurn;
      gameOver = true;
    }
    // Check for tie
    else if (isBoardFull()) {
      gameOver = true;
    }
    // Switch turns
    else {
      currentTurn = (currentTurn == Player.X) ? Player.O : Player.X;
    }
  }

  private boolean checkWin() {
    // Check rows
    for (int i = 0; i < 3; i++) {
      if (board[i][0] != null && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
        return true;
      }
    }

    // Check columns
    for (int j = 0; j < 3; j++) {
      if (board[0][j] != null && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
        return true;
      }
    }

    // Check diagonals
    if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
      return true;
    }
    if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
      return true;
    }

    return false;
  }

  private boolean isBoardFull() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (board[i][j] == null) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public Player getTurn() {
    return currentTurn;
  }

  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  @Override
  public Player getWinner() {
    return winner;
  }

  @Override
  public Player[][] getBoard() {
    Player[][] copy = new Player[3][3];
    for (int i = 0; i < 3; i++) {
      copy[i] = board[i].clone();
    }
    return copy;
  }

  @Override
  public Player getMarkAt(int r, int c) {
    if (r < 0 || r >= 3 || c < 0 || c >= 3) {
      throw new IllegalArgumentException("Invalid position");
    }
    return board[r][c];
  }

  @Override
  public String toString() {
    // Using Java stream API as provided in the starter code
    return Arrays.stream(getBoard()).map(
                    row -> " " + Arrays.stream(row).map(
                            p -> p == null ? " " : p.toString()).collect(Collectors.joining(" | ")))
            .collect(Collectors.joining("\n-----------\n"));
  }
}