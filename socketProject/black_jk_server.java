
package net_p_blackjk_server;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class black_jk_server extends JFrame{
	private static String client_id[]= {"호랑이","나비","다람쥐","사자","오뚜기"};
	//ImageIcon start_btn = new ImageIcon("gamestartbtn.PNG");
	URL resource = getClass().getClassLoader().getResource("image/gamestartbtn.PNG");
	ImageIcon start_btn = new ImageIcon(resource);
	//서버 소켓 만들어야함
	private ServerSocket server;
	//사용자 수
	private int playersleft;
	//난수 생성 // 4개의 카드타입중 하나를 뽑기위해
	private Random rand = new Random();
	//카드종류나열
	private String[] Label = {"스페이드","다이아","하트","클로버"};
	//카드의 합 = 21이 나오면 블랙잭임
	private static final int BLACKJACK = 21;
	//게임 시작 버튼
	private JButton Deal;
	//새로운 카드
	private Deck NewDeck;
	//정보를 출력할 textarea
	private JTextArea displayArea;
	//player를 실행할 executor
	private ExecutorService executor;
	//클라이언트 소켓 배열
	//여기
	private List<SockServer> sockServer;
	//private SockServer[] sockServer;
	//연결 호스트 카운터
	private int counter;
	//딜러의 첫 분배카드 2장
	private String FirstDealerCard,SecondDealerCard;
	//유저의 초기 카드 2장
	private String FirstUserCard, SecondUserCard;
	//Hit시 추가로 받는 카드
	private String nextcard;
	//게임을 진행할 클라이언트(플레이어들)의 리스트
	private ArrayList<PlayGame> players;
	//게임을 진행할 딜러의 PlayGame객체
	private PlayGame DealerCards;
	private int isBetOk;
	private int index,index2;//라벨 인덱스
	
	
	public static void main(String[] args) {
		black_jk_server window = new black_jk_server();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		window.ServerStart();
	}
	public black_jk_server() {
		super("딜러");
		initialize();
	}
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		players = new ArrayList();
		//여기
		sockServer = new ArrayList<SockServer>();
		//sockServer = new SockServer[100];
		executor = Executors.newFixedThreadPool(5); // 최대 클라이언트 3개 스레드 풀 설정.
		Deal = new JButton(start_btn);
		Deal.setBounds(0, 520, 501, 40);
		Deal.setEnabled(false);
		Deal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				displayArea.append("\n게임시작!!\n");
				Deal.setEnabled(false);
				NewDeck = new Deck(); // 초기 카드 셋팅
				DealCards(); // 딜러가 카드를 분배하면서 게임 시작
			}
		});
		getContentPane().setLayout(null);
		getContentPane().add(Deal);
		/* 정보 출력 창 */
		displayArea = new JTextArea();
		displayArea.setForeground(Color.WHITE);
		displayArea.setBackground(Color.BLACK);
		displayArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(displayArea);
		scrollPane.setBounds(0, 0, 501, 520);
		getContentPane().add(scrollPane);
		setSize(517, 600);
		//setVisible(true);
	}
	
	private void sendResult() {
		// 모든 플레이어가 Stand를 했을 경우 결과 정보 출력
		// counter값은 연결 된 클라이언트의 수
		try {
			for (int i = 0; i < counter; i++) {
				// 딜러의 점수 정보 전달
				//여기
				sockServer.get(i).sendData("Dealer has" + DealerCards.getTotalSum());
				//sockServer[i].sendData("Dealer has " + DealerCards.getTotalSum());

				// 딜러의 카드의 총합이 21보다 작고 유저의 카드의 합도 21보다 작으면 승부 출력
				if ((DealerCards.getTotalSum() <= BLACKJACK) && (players.get(i).getTotalSum() <= BLACKJACK)) {

					if (DealerCards.getTotalSum() > players.get(i).getTotalSum()) {
						//여기
						//sockServer.get(i).sendData("패배했습니다");
						sockServer.get(i).sendData((i+" ")+"패배했습니다");
						//sockServer[i].sendData("\n You Lose!!");
					}

					if (DealerCards.getTotalSum() < players.get(i).getTotalSum()) {
						//여기
						sockServer.get(i).sendData((i+" ")+"승리했습니다");
						//sockServer[i].sendData("\n You Win!!");
					}

					if (DealerCards.getTotalSum() == players.get(i).getTotalSum()) {
						//여기
						sockServer.get(i).sendData((i+" ")+"비겼습니다");
						//sockServer[i].sendData("\n Draw!!");
					}

				}
				// 딜러의 카드의 총 합이 21을 넘는다면
				if (DealerCards.getTotalSum() > BLACKJACK) {
					// 유저의 카드의 총합도 21을 넘는다면 무승부
					if (players.get(i).getTotalSum() > BLACKJACK) {
						//여기
						sockServer.get(i).sendData((i+" ")+"딜러 버스트 비겼습니다");
						//sockServer[i].sendData("\n DRAW!");
					}
					if (players.get(i).getTotalSum() <= BLACKJACK) {
						//여기
						sockServer.get(i).sendData((i+" ")+"딜러 버스트 승리했습니다");
						//sockServer[i].sendData("\n You Win!");
					}
				}

				if ((players.get(i).getTotalSum() > BLACKJACK) && (DealerCards.getTotalSum() <= BLACKJACK)) {
					//여기
					sockServer.get(i).sendData((i+" ")+"패배했습니다");
					//sockServer[i].sendData("\n You Lose!");
				}
			}

		} catch (NullPointerException n) {
		}
	}
	/* 딜러와 연결된 유저들의 초기 카드 2장을 셋팅하여 콘솔 창에 출력한다. */
	private void DealCards() {
		try {
			/* 딜러 카드 셋팅 */
			playersleft = counter;
			FirstDealerCard = NewDeck.getFirstCard();
			SecondDealerCard = NewDeck.getSecondCard();

			index = rand.nextInt(4);
			index2 = rand.nextInt(4);
			//딜러의 카드정보를 서버로 클라이언트에 보내기위해 억지로 넣어준것
			String dealerCardInfo_sendToClient = "\nDealer Card:"+Label[index]+"_"+NewDeck.getFirstCard()+","+Label[index2]+"_"+NewDeck.getSecondCard();
			//"뭐라고 갈까?:"+dealerCardInfo_sendToClient
			displayArea.append(dealerCardInfo_sendToClient);
			
			//card나오는것들을 전부 저장하기위한 arraylist
			ArrayList<String> cardindex = new ArrayList<>();
			ArrayList<String> cardindex2 = new ArrayList<>();
			
			
			/* 각각의 클라이언트에게 2장의 카드를 셋팅 */
			for (int i = 0; i < counter; i++) {
				index = rand.nextInt(4);
				index2 = rand.nextInt(4);
				FirstUserCard = NewDeck.dealCard();
				cardindex.add(Label[index]+"_"+FirstUserCard);
				SecondUserCard = NewDeck.dealCard();
				cardindex2.add(Label[index2]+"_"+SecondUserCard);
				PlayGame newGame = new PlayGame(FirstUserCard, SecondUserCard);
				players.add(newGame);
				//딜러의 카드정보들을 클라이언트들에게 전송
				sockServer.get(i).sendData((i+" ")+dealerCardInfo_sendToClient);
				//여기
				sockServer.get(i).sendData((i+" ")+"\n소유한카드:"+Label[index]+"_"+FirstUserCard+","+Label[index2]+"_"+SecondUserCard);
				//여기
				sockServer.get(i).sendData((i+" ")+"카드의 합: " + newGame.getTotalSum());
				//sockServer[i].sendData("카드의 합: " + newGame.getTotalSum());
			}
			
			for(int j=0;j<counter;j++) {
				for(int k=0;k<counter;k++) {
					if(j==k)continue;
					sockServer.get(j).sendData((k+" ")+"\n소유한카드:"+cardindex.get(k)+","+cardindex2.get(k));
				}
			}
			
			
		} catch (NullPointerException n) {
			//
		}
	}
	
	
	/*
	 * 포트 '23001'을 이용하여 클라이언트의 accept 요청을 무한 대기 상태로 기다린다. 요청이 들어 온 경우 서버와 연결하며
	 * 스레드 풀로 생성된 소켓을 실행하여 게임을 진행한다. 연결 될 수 있는 최대 클라이언트의 수는 5
	 */
	public void ServerStart() {
		try {
			server = new ServerSocket(23001); // 포트 23001으로 서버 시작
			displayArea.append("블랙잭 게임 대기중...");
			while (true) {
				try {
					counter = sockServer.size();
					// 서버가 종료될때까지 클라이언트의 접속을 기다리고 소켓 실행.
					//System.out.println("counter:"+counter+"");
					//여기
					sockServer.add(new SockServer(counter));
					sockServer.get(counter).waitForConnection();
					//sockServer[counter].waitForConnection();
					//여기
					executor.execute(sockServer.get(counter));
					//executor.execute(sockServer[counter]);
					//++counter;
					
				} catch (EOFException eofException) {
					displayArea.append("\n서버 종료");
					sockServer.remove(this);
				}
			}
		} catch (IOException e) {
		}
	}
	
	/* 연결된 클라이언트와 서버 간의 메세지를 주고 받는 작업을 수행한다. */
	private class SockServer implements Runnable {
		private ObjectOutputStream output; // output stream
		private ObjectInputStream input; // input stream
		private Socket connection; // client 소켓
		private int ClientNumber;

		public SockServer(int counter) {
			ClientNumber = counter;
			isBetOk = counter;
		}
		public void run() {
			try {
				try {
					output = new ObjectOutputStream(connection.getOutputStream());
					output.flush();
					input = new ObjectInputStream(connection.getInputStream());
					/* 접속 메세지 서버에 출력 */
					displayArea.append("유저" + ClientNumber + "가 입장했습니다.\n");
					String message = "유저" + ClientNumber + "가 입장했습니다.\n";
					sendData(message);
					/*
					 * 클라이언트로부터 받아온 메세지에 따라 동작 수행 클라이언트로 부터 받아온 메세지는 'hit' or
					 * 'stand' or 'Bet' or '채팅 메세지'
					 */
					do {
						try {
							if (message.contains(" hit")) { // 'hit'버튼
								int num=100;
								//displayArea.append("아니대체 뭐라오길래"+message+"\n");
								String client_num="100";
								if(message.contains("호랑이 hit")) {
									//System.out.println("호랑이 hit");
									client_num = "0"; 
								}else if(message.contains("나비 hit")) {
									//System.out.println("나비 hit");
									client_num = "1";
								}else if(message.contains("다람쥐 hit")) {
									//System.out.println("다람쥐 hit");
									client_num = "2";
								}else if(message.contains("사자 hit")) {
									//System.out.println("사자 hit");
									client_num = "3";
								}else if(message.contains("오뚜기 hit")) {
									//System.out.println("오뚜기 hit");
									client_num = "4";
								}
								
								//여기서 clinet쪽에서 온 client_num 변수값을 받아 client를 구분한다.
								num = Integer.parseInt(client_num);
								//실험
								String sendHitDataToAll = PlayerHit(num);
								
								for (int i = 0; i < counter; i++) {
									sockServer.get(i).sendData(sendHitDataToAll);
									//displayArea.append("PlayerHit(num): "+sendHitDataToAll);
									String sendData2 = "내 카드의 총합: " + players.get(this.ClientNumber).getTotalSum();
									//sockServer.get(i).sendData(sendData2);
									//PlayerHit(num);
									//여기
									//sockServer.get(i).sendData("Client " + ClientNumber + " : " + message);
									//sockServer[i].sendData("Client " + ClientNumber + " : " + message);
								}
								//실험
								
								
								//PlayerHit();
							}
							else if (message.contains("Stand")) { // 'stand'버튼
								this.sendData("다른 플레이어 차례가 끝나길 기다려주세요...");
								playersleft--;
								if (playersleft == 0) {
									DealerHit(); // 딜러의 합이 16보다 작은 경우 사용자가
													// stand했을 때 한장 추가로 뽑기.
								}
								//실험
								//int num=100;
								//displayArea.append("아니대체 뭐라오길래"+message+"\n");
								String client_num="100";
								if(message.contains("호랑이 Stand")) {
									//System.out.println("호랑이 Stand");
									client_num = "0"; 
								}else if(message.contains("나비 Stand")) {
									//System.out.println("나비 Stand");
									client_num = "1";
								}else if(message.contains("다람쥐 Stand")) {
									//System.out.println("다람쥐 Stand");
									client_num = "2";
								}else if(message.contains("사자 Stand")) {
									//System.out.println("사자 Stand");
									client_num = "3";
								}else if(message.contains("오뚜기 Stand")) {
									//System.out.println("오뚜기 Stand");
									client_num = "4";
								}
								
								//여기서 clinet쪽에서 온 client_num 변수값을 받아 client를 구분한다.
								//num = Integer.parseInt(client_num);
								//실험
								//String sendHitDataToAll = PlayerHit(num);
								client_num += " Stand상태 들어감";
								for (int i = 0; i < counter; i++) {
									sockServer.get(i).sendData(client_num);
									//displayArea.append("PlayerHit(num): "+client_num);
									//String sendData2 = "내 카드의 총합: " + players.get(this.ClientNumber).getTotalSum();
									//sockServer.get(i).sendData(sendData2);
									//PlayerHit(num);
									//여기
									//sockServer.get(i).sendData("Client " + ClientNumber + " : " + message);
									//sockServer[i].sendData("Client " + ClientNumber + " : " + message);
								}
								//실험
								
								//실험
								
							} else if (message.contains("Bet")) {
								//System.out.println("isBetOk는:"+(isBetOk)+"");
								isBetOk--;
								if (isBetOk == 0) {
									Deal.setEnabled(true);
								}
							}
							else { // 채팅 내용
								try {
									for (int i = 0; i <= counter; i++) {
										//여기
										sockServer.get(i).sendData("Client " + ClientNumber + " : " + message);
										//sockServer[i].sendData("Client " + ClientNumber + " : " + message);
									}
								} catch (NullPointerException e) {
								}
							}
							message = (String) input.readObject(); // 메세지 받아오기
						} catch (ClassNotFoundException c) {
						}
					} while (!message.equals("CLIENT>>> TERMINATE"));
				} catch (EOFException f) {
					displayArea.append("\nClient" + ClientNumber + " 접속 종료");
				} finally {
					closeConnection(); // 연결 종료
				}
			} catch (IOException e) {
			}
		}
		/* 클라이언트로 부터 23001 포트로의 접속 요청이 있을떄까지 대기하며, 있을 경우 접속 정보를 출력하고 다음 단계 실행 */
		private void waitForConnection() throws IOException {
			displayArea.append("\n[Server] Waiting for connection...\n");
			connection = server.accept();
			displayArea.append("\n접속 정보 : Client " + ClientNumber + " " + connection.getInetAddress().getHostName());
		}
		/* 사용자가 Stand했을 경우 Dealer의 게임 셋팅 */
		private void DealerHit() {
			DealerCards = new PlayGame(FirstDealerCard, SecondDealerCard);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 16보다 작은 경우 계속해서 카드를 'hit'한다.
			if (DealerCards.getTotalSum() < 16) {
				while (DealerCards.getTotalSum() < 16) {
					nextcard = NewDeck.dealCard();
					DealerCards.CardHit(nextcard);
					String dealerHitCardInfo_toClient ="\n딜러 히트!"+nextcard+"\n"+"딜러 소유카드 총합:"+DealerCards.getTotalSum()+"\n"; 
					displayArea.append(dealerHitCardInfo_toClient);
				}
			}
			if (DealerCards.CheckBust()) {
				displayArea.append("\n딜러 버스트!");
			} else {
				displayArea.append("\n딜러 소유카드 총합:" + " " + DealerCards.getTotalSum());
			}
			sendResult(); // 결과 출력
			DealerCards.setTotalSumZero(); // 총합 0으로 초기화
		}
		// 클라이언트가 hit를 눌렀을 경우
		private String PlayerHit(int i) {
			nextcard = NewDeck.dealCard();
			index = rand.nextInt(4);
			//여기를 뭔가 그..contain하게다시 고쳐야함
			String sendData1 = (i+" ")+"hit추가카드"+Label[index]+"_"+nextcard;
			//sendData((i+" ")+"hit추가카드"+Label[index]+"_"+nextcard);
			players.get(this.ClientNumber).CardHit(nextcard);
			//String sendData2 = "내 카드의 총합: " + players.get(this.ClientNumber).getTotalSum();
			//sendData("내 카드의 총합: " + players.get(this.ClientNumber).getTotalSum());
			//System.out.println("아니뭐냐고ㅗㅗㅗㅗ"+sendData1);
			if (players.get(this.ClientNumber).CheckBust()) { // if player
															// busted
				String bustmsg = (i+" ")+"bust입니다";
				//sendData("버스트!\n");
				playersleft--;
				if (playersleft == 0) {
					DealerHit();
				}
				return bustmsg;
				//return "버스트!\n";
			}else {
				return sendData1;  
			}
		}
		// 연결된 모든 소켓 종료
		private void closeConnection() {
			displayArea.append("\nClient " + ClientNumber + "연결 종료" + "\n");

			try {
				output.close();
				input.close();
				connection.close();
			} catch (IOException e) {
			}
		}
		/* 클라이언트로 메세지 전달 메소드 */
		private void sendData(String message) {
			try 
			{
				output.writeObject(message);
				output.flush();
			} catch (IOException ioException) {
			}
		}

	} // end ServerStart
	
}

class PlayGame {

	private Stack<Cards> Cards; // 게임에 사용되는 카드
	private static final int BLACKJACK = 21; // 블랙잭 = 21
	private boolean bust = false; // true면 bust
	private int TotalSum = 0; // 카드의 전체 합계

	/* 초기 셋팅된 카드 두장을 기준으로 총합을 계산하는 생성자 */
	public PlayGame(String FirstCard, String SecondCard) {
		Cards = new Stack();
		Cards.push(new Cards(FirstCard));
		Cards.push(new Cards(SecondCard));
		CalcCardSum();
	}

	/* 카드의 모든 총합을 리턴 */
	public int getTotalSum() {
		return TotalSum;
	}

	/* 'Hit'를 한 경우 추가 카드를 받는 메소드, 전체 합계를 구하고 21이 넘는지를 검사한다. */
	public void CardHit(String additionalCard) {
		Cards.push(new Cards(additionalCard));
		CalcCardSum();
		CheckBust();
	}

	/* 카드의 총 합을 계산한다.*/
	public void CalcCardSum() {
		while(!Cards.isEmpty()){
			Cards card = Cards.pop();
			if(card.getValue() == "Ace"){
				if(TotalSum <=10) TotalSum +=11;
				else TotalSum +=1;
			}
			else
				TotalSum += getValue(card.getValue());
		}
	}
	/* 게임 종료 후 서버에서 새로운 게임을 진행하기 위해 전체 합을 0으로 초기화 */
	public void setTotalSumZero(){
		this.TotalSum = 0;
	}

	/* Cards 객체의 Number값인 String을 계산하기 위한 int형으로 1대1 매칭 변환 */
	public int getValue(String cardvalue) {
		int value = 0;

		switch (cardvalue) {
		case "King":
		case "Queen":
		case "Jack":
		case "10":
			value = 10;
			break;
		case "9":
			value = 9;
			break;
		case "8":
			value = 8;
			break;
		case "7":
			value = 7;
			break;
		case "6":
			value = 6;
			break;
		case "5":
			value = 5;
			break;
		case "4":
			value = 4;
			break;
		case "3":
			value = 3;
			break;
		case "2":
			value = 2;
			break;
		default:
			break;
		}
		return value;
	}

	/* 카드의 총 합이 21이 넘는지 검사하는 메소드 
	 * 21이 넘는다면 bust는 true를 반환하고 게임은 종료 */
	public boolean CheckBust() {
		if (TotalSum > BLACKJACK) bust = true;
		else bust = false;
		return bust;
	}

}


class Cards {

	private String Value; // 카드의 유니크 값
	
	public Cards(String cardVal){ // 생성자를 통한 초기화
		Value = cardVal; 
	}

	public String getValue() {
		return Value;
	}
}	


class Deck {
	private List<Cards> deck = new ArrayList<Cards>(); // 게임에 사용될 덱
	private int counter; // Deck의 인덱스 카운터
	private static final int NUMBER_OF_CARDS = 52; // 전체 카드 갯수
	private static final Random rand = new Random(); // 난수 생성
	private String firstcard, secondcard, anothercard; // 초기 2장의 카드와 hit시 생성되는 추가 카드
	private String[] Number = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
	
	/* 생성자, Cards 객체 리스트 deck을 초기화 하고 shuffle하여 초기 2장의 카드를 초기화한다. */
	public Deck() {
		counter = 0;
		//System.out.println(deck.size());
		for (int i = 0; i < 52; i++) {
			// 13개씩 끊어서 저장(Number[0~12] 이므로)
			deck.add(new Cards(Number[i % 13])); 
		}
		Collections.shuffle(deck);
		firstcard = deck.get(counter++).getValue();
		secondcard = deck.get(counter++).getValue();
	}
	/* 첫번째 카드 반환 */
	public synchronized String getFirstCard() {
		return firstcard;
	}
	/* 두 번째 카드 반환 */
	public synchronized String getSecondCard() {
		return secondcard;
	}
	/* 'Hit' 시 새로운 카드 반환 */
	public synchronized String dealCard() {
		anothercard = deck.get(counter++).getValue();
		return anothercard;
	}
}


