package tictactoe;

/**
 * Represents a player in a game of Tic Tac Toe. A player can be either X or O. X always
 * moves first according to standard Tic Tac Toe rules. The toString method returns "X" or
 * "O" respectively for display purposes.
 */
public enum Player {
  X, O;

  @Override
  public String toString() {
    return this.name();
  }
}