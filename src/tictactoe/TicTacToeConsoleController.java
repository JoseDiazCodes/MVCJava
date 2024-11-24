package tictactoe;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A console-based controller for Tic Tac Toe that handles user input and output through
 * text commands. Supports move entry and game quitting.
 */
public class TicTacToeConsoleController implements TicTacToeController {
  /**
   * The source of input for the game, providing player moves and commands.
   */
  private final Readable in;

  /**
   * The destination for output, displaying game state and messages to players.
   */
  private final Appendable out;

  /**
   * Scanner used to parse input from the Readable source into game moves.
   */
  private final Scanner scan;

  /**
   * Creates a controller that uses the specified sources for input and output.
   *
   * @param in Source of input, typically System.in
   * @param out Destination for output, typically System.out
   * @throws IllegalArgumentException if either parameter is null
   */
  public TicTacToeConsoleController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Input/Output sources cannot be null");
    }
    this.in = in;
    this.out = out;
    this.scan = new Scanner(in);
  }

  @Override
  public void playGame(TicTacToe model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }

    try {
      while (!model.isGameOver()) {
        // Initial board state + prompt
        out.append(model.toString()).append("\n");
        out.append("Enter a move for ").append(model.getTurn().toString()).append(":\n");

        if (!scan.hasNext()) {
          throw new IllegalStateException("No more input available");
        }

        String input = scan.next();
        if (input.equalsIgnoreCase("q")) {
          out.append("Game quit! Ending game state:\n")
                  .append(model.toString()).append("\n");
          return;
        }

        try {
          int row = Integer.parseInt(input);
          if (!scan.hasNext()) {
            throw new IllegalStateException("No more input available");
          }
          String colInput = scan.next();
          if (colInput.equalsIgnoreCase("q")) {
            out.append("Game quit! Ending game state:\n")
                    .append(model.toString()).append("\n");
            return;
          }

          try {
            int col = Integer.parseInt(colInput);
            try {
              model.move(row - 1, col - 1);
            } catch (IllegalArgumentException e) {
              out.append("Invalid move. Try again.\n");
            }
          } catch (NumberFormatException e) {
            out.append("Please enter numbers for position.\n");
          }
        } catch (NumberFormatException e) {
          out.append("Please enter numbers for position.\n");
          continue; // Skip reprinting board, just continue to next iteration
        } catch (NoSuchElementException e) {
          throw new IllegalStateException("No more input available");
        }
      }

      // Game over states
      out.append(model.toString()).append("\n");
      out.append("Game is over! ");
      Player winner = model.getWinner();
      if (winner != null) {
        out.append(winner.toString()).append(" wins.\n");
      } else {
        out.append("Tie game.\n");
      }

    } catch (IOException e) {
      throw new IllegalStateException("Failed to transmit output", e);
    }
  }
}