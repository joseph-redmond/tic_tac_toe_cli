package com.josephredmond.services;

import com.josephredmond.enums.GamePiece;
import com.josephredmond.models.Board;
import com.josephredmond.models.Piece;
import java.security.SecureRandom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Scanner;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Service
public class GameService {
	private static final int BOARD_SIZE = 3;
	private boolean playerTurn;
	private Board board = new Board();
	private Piece piece = new Piece();
	private Random random = new SecureRandom();

	public void init() {
		initializeBoard();
		selectPlayerPiece();
		displayBoard();
		determineFirstPlayer();
	}

	public void selectPlayerPiece() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Select your piece (X/O):");
			String input = scanner.nextLine().toUpperCase();
			System.out.println(input);
			System.out.println(GamePiece.valueOf(input).equals(GamePiece.X));
			if (GamePiece.valueOf(input).equals(GamePiece.X)) {
				piece.setPlayer(GamePiece.X);
				piece.setAi(GamePiece.O);
				break;
			} else if (GamePiece.valueOf(input).equals(GamePiece.O)) {
				piece.setPlayer(GamePiece.O);
				piece.setAi(GamePiece.X);
				break;
			} else {
				System.out.println("Invalid input. Please try again.");
			}
		}
	}

	public void determineFirstPlayer() {
		playerTurn = random.nextBoolean();
		if (playerTurn) {
			System.out.println("You go first.");
		} else {
			System.out.println("AI goes first.");
		}
	}

	public void initializeBoard() {
		board.setBoard(new GamePiece[BOARD_SIZE][BOARD_SIZE]);

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board.getBoard()[i][j] = GamePiece.EMPTY;
			}
		}
	}

	public void displayBoard() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				String value = board.getBoard()[i][j].name();
				if(board.getBoard()[i][j].equals(GamePiece.EMPTY)) {
					value = "-";
				}
				System.out.print(" " + value + " ");
			}
			System.out.println();
		}
	}

	public void playerMove() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Enter row and column numbers (1-3):");
			int row = scanner.nextInt() - 1;
			int col = scanner.nextInt() - 1;

			if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && board.getBoard()[row][col] == GamePiece.EMPTY) {

				board.getBoard()[row][col] = piece.getPlayer();
				break;
			} else {
				System.out.println("Invalid move. Try again.");
			}
		}
	}

	public void aiMove() {
		int[] move = getBestMove();
		board.getBoard()[move[0]][move[1]] = piece.getAi();
		System.out.println("AI moves at " + (move[0] + 1) + ", " + (move[1] + 1));
	}

	private int[] getBestMove() {
		int[] bestMove = new int[2];
		int bestScore = Integer.MIN_VALUE;

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (board.getBoard()[i][j] == GamePiece.EMPTY) {
					board.getBoard()[i][j] = piece.getAi();
					int score = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
					board.getBoard()[i][j] = GamePiece.EMPTY;

					if (score > bestScore) {
						bestScore = score;
						bestMove[0] = i;
						bestMove[1] = j;
					}
				}
			}
		}

		return bestMove;
	}

	private int minimax(int depth, boolean maximizingPlayer, int alpha, int beta) {
		if (checkWinner(piece.getPlayer())) {
			return -10 + depth;
		}
		if (checkWinner(piece.getAi())) {
			return 10 - depth;
		}
		if (checkDraw()) {
			return 0;
		}

		if (maximizingPlayer) {
			int maxScore = Integer.MIN_VALUE;
			for (int i = 0; i < BOARD_SIZE; i++) {
				for (int j = 0; j < BOARD_SIZE; j++) {
					if (board.getBoard()[i][j] == GamePiece.EMPTY) {
						board.getBoard()[i][j] = piece.getAi();
						int score = minimax(depth + 1, false, alpha, beta);
						board.getBoard()[i][j] = GamePiece.EMPTY;
						maxScore = Math.max(maxScore, score);
						alpha = Math.max(alpha, score);
						board.getBoard()[i][j] = GamePiece.EMPTY;
						if (beta <= alpha) {
							break;
						}
					}
				}
			}
			return maxScore;
		} else {
			int minScore = Integer.MAX_VALUE;
			for (int i = 0; i < BOARD_SIZE; i++) {
				for (int j = 0; j < BOARD_SIZE; j++) {
					if (board.getBoard()[i][j] == GamePiece.EMPTY) {
						board.getBoard()[i][j] = piece.getPlayer();
						int score = minimax(depth + 1, true, alpha, beta);
						board.getBoard()[i][j] = GamePiece.EMPTY;
						minScore = Math.min(minScore, score);
						beta = Math.min(beta, score);
						if (beta <= alpha) {
							break;
						}
					}
				}
			}
			return minScore;
		}
	}

	public boolean checkWinner(GamePiece piece) {
		for (int i = 0; i < BOARD_SIZE; i++) {
			if (board.getBoard()[i][0] == piece && board.getBoard()[i][1] == piece && board.getBoard()[i][2] == piece) {
				return true;
			}
			if (board.getBoard()[0][i] == piece && board.getBoard()[1][i] == piece && board.getBoard()[2][i] == piece) {
				return true;
			}
		}
		if (board.getBoard()[0][0] == piece && board.getBoard()[1][1] == piece && board.getBoard()[2][2] == piece) {
			return true;
		}
		return board.getBoard()[0][2] == piece && board.getBoard()[1][1] == piece && board.getBoard()[2][0] == piece;
	}

	public boolean checkDraw() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (board.getBoard()[i][j] == GamePiece.EMPTY) {
					return false;
				}
			}
		}
		return true;
	}

	public void start() {
		init();
		while (!checkWinner(piece.getPlayer()) && !checkWinner(piece.getAi()) && !checkDraw()) {
			if (isPlayerTurn()) {
				playerMove();
			} else {
				aiMove();
			}
			displayBoard();
			setPlayerTurn(!isPlayerTurn());
		}
		if (checkWinner(getPiece().getPlayer())) {
			System.out.println("You win!");
		} else if (checkWinner(getPiece().getAi())) {
			System.out.println("AI wins!");
		} else {
			System.out.println("It's a draw!");
		}
	}
}
