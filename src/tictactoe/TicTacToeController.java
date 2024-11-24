package tictactoe;

/**
 * Represents a controller for a game of Tic Tac Toe that handles user input and game flow.
 * This controller manages the interaction between the user and the game model.
 */
public interface TicTacToeController {
  /**
   * Executes a single game of tic-tac-toe using the provided model. When the game is
   * over, the playGame method ends.
   *
   * @param model The game of TicTacToe to be played, must not be null
   * @throws IllegalArgumentException if the model is null
   * @throws IllegalStateException if the controller cannot read input or transmit output
   */
  void playGame(TicTacToe model);
}
