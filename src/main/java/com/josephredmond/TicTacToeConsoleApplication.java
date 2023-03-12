package com.josephredmond;

import com.josephredmond.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
@CommandLine.Command(name = "tic_tac_toe", mixinStandardHelpOptions = true, version = "1.0",
		description = "Tic Tac Toe game with Spring Boot 3 and Picocli")
@RequiredArgsConstructor
public class TicTacToeConsoleApplication implements CommandLineRunner {
	private final GameService gameService;
	@Override
	public void run(String... args) {
		CommandLine cmd = new CommandLine(this);
		cmd.parseArgs(args);
		gameService.start();
	}


	public static void main(String[] args) {
		SpringApplication.run(TicTacToeConsoleApplication.class, args);
	}
}