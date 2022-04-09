import java.util.Random;
import java.util.Scanner;

class Player {
	int pos = 0;
	int passCnt = 0;

	public Player() {
	}

	void ifPassed19(int avg) {

		int JumpCnt = this.pos + avg;
		if (JumpCnt > 19) {
			this.pos = (JumpCnt - 19) - 1;
			passCnt += 1;
			System.out.printf("축하합니다. 결승선 통과! 현재 %d번째 통과! \n\n",this.passCnt);
		} else {
			this.pos = JumpCnt;
		}
		System.out.printf("%d 만큼 플레이어가 이동합니다. 현재 위치 : %d\n\n", avg, this.pos);
	}
}

class Dice {

	Player player;

	int time = 0;

	public Dice(Player player) {
		this.player = player;
	}

	int getTime() {
		while (true) {
			time = new Scanner(System.in).nextInt();
			if (time > 0 && time < 4) {
				return time;
			} else {
				System.err.println("1~3사이의 숫자만 입력하세요");
				continue;
			}
		}

	}

	int diceing() {
		if (player.passCnt == 3) {
			System.out.println("완주 3회 완료로 게임이 종료됩니다.");
			System.exit(0);
		}
		System.out.println("주사위를 몇번 굴리시겠습니까?");
		// 주사위 횟수 1~3회
		int time = getTime();

		int sum = 0, num = 0, avg = 0;

		for (int i = 0; i < time; i++) {
			num = new Random().nextInt(6) + 1;
			sum += num;
			System.out.printf("%d 번째 주사위 결과 : %d\n", i + 1, num);
		}
		avg = sum / time;
		System.out.printf("%d개의 주사위의 평균값은 %d입니다!\n", time, avg);
		return avg;
	}

}

public class DiceGame {

	public static void main(String[] args) {
		Player player = new Player();
		Dice dice = new Dice(player);

		System.out.println("게임이 시작됩니다.");
		while (true) {
			
			player.ifPassed19(dice.diceing());
		}

	}

}
