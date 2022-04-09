package computerVSPerson;

import java.util.Random;
import java.util.Scanner;

class Player {
	int posX = 0;
	String dist = "";
	int compCnt = 0;

	public Player(String dist) {
		this.dist = dist;
	}
}

class Engine {
	String board[] = new String[36];

	public Engine() {
		;
	}

	void InitBoard() {
		for (int i = 0; i < 36; i++) {
			this.board[i] = "[  ]";
		}
	}
}

class Constant {
	int left = 35;
	int right = 10;

	public Constant() {
		;
	}

}

class Painter {
	Engine engine;
	Constant constant;

	public Painter(Engine engine) {
		this.engine = engine;
		this.constant = new Constant();
	}

	void paint(String dist, int posX) {
		if (dist.equals("사람")) {
			this.engine.board[posX] = "[p ]";
		} else {
			if (this.engine.board[posX].equals("[p ]")) {
				this.engine.board[posX] = "[pc]";
			} else {
				this.engine.board[posX] = "[ c]";
			}
			for (int i = 0; i < 10; i++) {
				System.out.print(this.engine.board[i]);
			}
			int left = this.constant.left;
			int right = this.constant.right;

			for (int k = 0; k < 8; k++) {
				System.out.println("");

				System.out.print(this.engine.board[left - k]);
				for (int i = 0; i < 8; i++) {
					System.out.print("    ");
				}
				System.out.print(this.engine.board[right + k]);

				System.out.println("");
			}

			for (int i = 27; i >= 18; i--) {
				System.out.print(this.engine.board[i]);
			}
			System.out.println("");

		}

	}
}

class Updater {
	Painter painter;

	public Updater(Painter painter) {
		this.painter = painter;
	}

	void updateBoard(String dist, int posX) {
		this.painter.paint(dist, posX);
	}
}

class InputGetter {
	Scanner sc = new Scanner(System.in);

	int getInput() {
		return sc.nextInt();
	}
}

class Dice {
	Player player;
	Engine engine;
	int num;
	int cnt;
	int sum;
	int avg;
	int diceCnt = 0;
	public Dice(Player player,Engine engine) {
		this.player = player;
		this.engine = engine;
	}

	int rollDice() {
		if (this.player.dist.equals("사람")) {
			System.out.println("how many times will you roll the dice (1~3)? ");
			this.num = new InputGetter().getInput();
			for (int i = 0; i < this.num; i++) {
				this.diceCnt = new Random().nextInt(6) + 1;
				System.out.println((i+1)+". dice: "+ this.diceCnt);
				this.sum += this.diceCnt;
			}
			this.avg = (this.sum / this.num);
			System.out.println("average number: "+this.avg);
			this.player.posX += this.avg;
			if (this.player.posX >= 35) {
				this.player.posX = 0;
				this.player.compCnt += 1;
			}
			if (this.player.compCnt >= 3) {
				System.out.println("Player win!");
				System.exit(0);
			}

			engine.InitBoard();
			
			return avg;
		} else {
			this.num = new Random().nextInt(3) + 1;
			for (int i = 0; i < this.num; i++) {
				this.sum += new Random().nextInt(6) + 1;
			}
			this.avg = (this.sum / this.num);
			this.player.posX += this.avg;
			if (this.player.posX >= 35) {
				this.player.posX = 0;
				this.player.compCnt += 1;
			}
			if (this.player.compCnt >= 3) {
				System.out.println("Computer win!");
				System.exit(0);
			}
			
			
			engine.InitBoard();
			return avg;
		}

	}
}

public class Game {
	public static void main(String[] args) {
		Engine engine = new Engine();
		engine.InitBoard();
		Painter painter = new Painter(engine);
		Updater updater = new Updater(painter);

		Player player1 = new Player("사람");
		Player player2 = new Player("컴퓨터");

		while (true) {
			Dice dice1 = new Dice(player1,engine);
			dice1.rollDice();
			Dice dice2 = new Dice(player2,engine);
			dice2.rollDice();
			updater.updateBoard(player1.dist, player1.posX);
			updater.updateBoard(player2.dist, player2.posX);
		}
	}

}
