package net_p_blackjk_client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;

public class black_jk_client extends JFrame {
//	static JPanel panel = new JPanel() {
//		Image background = new ImageIcon(getClass().getClassLoader().getResource("image/background.png")).getImage();
//		public void paint(Graphics g) {// 그리는 함수
//			g.drawImage(background, 0, 0, null);// background를 그려줌
//		}
//	};
//	
	
	public black_jk_client(String host) {
		initialize(host);
	}
	
	
	URL resource_standbtn = getClass().getClassLoader().getResource("image/standbtn.PNG");
	ImageIcon stand_btn = new ImageIcon(resource_standbtn);
	URL resource_hitbtn = getClass().getClassLoader().getResource("image/hitbtn.PNG");
	ImageIcon hit_btn = new ImageIcon(resource_hitbtn);
	// URL background_img =
	// getClass().getClassLoader().getResource("image/background.png");
	// Image background = new ImageIcon(background_img).getImage();
	// ImageIcon background = new ImageIcon(background_img);
	private JPanel panel;
	private JPanel dealerPanel;
	private JPanel client1_panel;
	private JPanel client2_panel;
	private JPanel client3_panel;
	private JPanel client4_panel;
	private JPanel client5_panel;
	private String client_one_message;
	private String client_two_message;
	private String client_three_message;
	private String client_id[]= {"호랑이","나비","다람쥐","사자","오뚜기"};
	private JLabel userid;	
	private JSpinner spinner;
	private JPanel dealerstatusPanel;
	private JPanel dealerPanel_result;
	
	private JButton Hit;
	private JButton Stand;
	private JButton Send;
	private JButton Bet;
	private JPanel buttonPanel, titlePanel;
	private JLabel title;
	private JLabel title2;
	private JTextArea ChatArea;
	private JTextField ChatInput, BetInput;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String Game_Server;
	private Socket client;
	private int money = 1_000_000;
	private int betting;
	private JPanel bustpanel1;
	private JPanel bustpanel2;
	private JPanel bustpanel3;
	private JPanel bustpanel4;
	private JPanel bustpanel5;
	private JPanel standpanel1;
	private JPanel standpanel2;
	private JPanel standpanel3;
	private JPanel standpanel4;
	private JPanel standpanel5;

	public static void main(String[] args) {
		black_jk_client window = new black_jk_client("127.0.0.1");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			window.runClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	private void initialize(String host) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Game_Server = host;
		//JPanel panel = new JPanel();
		panel = new JPanel();
		panel.setBounds(0, 35, 1030, 500);
		getContentPane().add(panel);

		panel.setBackground(new Color(50, 205, 50));

		buttonPanel = new JPanel();
		buttonPanel.setBounds(0, 560, 484, 66);
		buttonPanel.setLayout(new GridLayout(1, 2));

		JPanel HitStandButton = new JPanel();
		JPanel TextSendButton = new JPanel();
		Hit = new JButton(hit_btn);

		HitStandButton.setLayout(new GridLayout(1, 2));
		HitStandButton.add(Hit);

		TextSendButton.setLayout(new GridLayout(2, 0));
		
		buttonPanel.add(HitStandButton);
		buttonPanel.add(TextSendButton);

		buttonPanel.setVisible(true);
		getContentPane().setLayout(null);
		getContentPane().add(buttonPanel);

		titlePanel = new JPanel();
		titlePanel.setBounds(0, 0, 1110, 15);

		title = new JLabel("플레이어");
		title.setBounds(0, 0, 700, 15);
		title2 = new JLabel("채팅");
		title2.setBounds(1040, 0, 249, 15);
		titlePanel.setLayout(null);

		titlePanel.add(title);
		titlePanel.add(title2);
		getContentPane().add(titlePanel);
		
		userid = new JLabel("");
		userid.setBounds(53, 0, 88, 15);
		titlePanel.add(userid);

		setSize(1301, 670);
		setVisible(true);

		Hit.setEnabled(false);
		Stand = new JButton(stand_btn);
		HitStandButton.add(Stand);
		Stand.setEnabled(false);
		Stand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendData(userid.getText()+" Stand");
			}
		});
		
		ChatInput = new JTextField(12);
		Send = new JButton("채팅");

		JPanel chatpanel = new JPanel();
		chatpanel.setBounds(1040, 500, 242, 33);
		getContentPane().add(chatpanel);

		chatpanel.setLayout(new FlowLayout());

		chatpanel.add(ChatInput);
		chatpanel.add(Send);
		Bet = new JButton("배팅");
		Bet.setBounds(115, 5, 71, 23);
		//BetInput = new JTextField(12);
		JPanel betpanel = new JPanel();
		betpanel.setBounds(1040, 460, 242, 33);
		getContentPane().add(betpanel);
		
		String betAmount_list[] = new String[] {"100","1000","10000"};
		SpinnerListModel spinnerListModel = new SpinnerListModel( betAmount_list );
		betpanel.setLayout(null);
		spinner = new JSpinner(spinnerListModel);
		spinner.setBounds(46, 5, 64, 22);
		
		betpanel.add(spinner);

		//betpanel.add(BetInput);
		betpanel.add(Bet);
		
		ChatArea = new JTextArea();
		ChatArea.setBounds(1040, 17, 240, 430);
		getContentPane().add(ChatArea);
		ChatArea.setBackground(Color.BLACK);
		ChatArea.setForeground(Color.LIGHT_GRAY);
		ChatArea.setLineWrap(true);
		ChatArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(ChatArea);
		scrollPane.setBounds(1040, 15, 242, 432);
		getContentPane().add(scrollPane);
		
		//JPanel dealerPanel = new JPanel();
		dealerPanel = new JPanel();
		dealerPanel.setBackground(Color.BLACK);
		dealerPanel.setBounds(0, 15, 500, 123);
		getContentPane().add(dealerPanel);
		
		client1_panel = new JPanel();
		client1_panel.setBackground(Color.BLACK);
		client1_panel.setBounds(0, 350, 210, 210);
		getContentPane().add(client1_panel);
		
		client2_panel = new JPanel();
		client2_panel.setBackground(Color.BLACK);
		client2_panel.setBounds(260, 350, 210, 210);
		getContentPane().add(client2_panel);
		
		client3_panel = new JPanel();
		client3_panel.setBackground(Color.BLACK);
		client3_panel.setBounds(520, 350, 210, 210);
		getContentPane().add(client3_panel);
		
		client4_panel = new JPanel();
		client4_panel.setBackground(Color.BLACK);
		client4_panel.setBounds(780, 350, 210, 210);
		getContentPane().add(client4_panel);
		
		client5_panel = new JPanel();
		client5_panel.setBackground(Color.BLACK);
		client5_panel.setBounds(780, 90, 210, 210);
		getContentPane().add(client5_panel);
		
		bustpanel1 = new JPanel();
		bustpanel1.setBackground(Color.BLACK);
		bustpanel1.setBounds(290, 200, 290, 130);
		getContentPane().add(bustpanel1);
		
		bustpanel2 = new JPanel();
		bustpanel2.setBackground(Color.BLACK);
		bustpanel2.setBounds(290, 200, 290, 130);
		getContentPane().add(bustpanel2);
		
		bustpanel3 = new JPanel();
		bustpanel3.setBackground(Color.BLACK);
		bustpanel3.setBounds(290, 200, 290, 130);
		getContentPane().add(bustpanel3);
		
		bustpanel4 = new JPanel();
		bustpanel4.setBackground(Color.BLACK);
		bustpanel4.setBounds(290, 200, 290, 130);
		getContentPane().add(bustpanel4);
		
		bustpanel5 = new JPanel();
		bustpanel5.setBackground(Color.BLACK);
		bustpanel5.setBounds(290, 200, 290, 130);
		getContentPane().add(bustpanel5);
		
		standpanel1 = new JPanel();
		standpanel1.setBackground(Color.BLACK);
		standpanel1.setBounds(0, 350, 210, 40);
		getContentPane().add(standpanel1);
		
		standpanel2 = new JPanel();
		standpanel2.setBackground(Color.BLACK);
		standpanel2.setBounds(260, 350, 210, 40);
		getContentPane().add(standpanel2);
		
		standpanel3 = new JPanel();
		standpanel3.setBackground(Color.BLACK);
		standpanel3.setBounds(520, 350, 210, 40);
		getContentPane().add(standpanel3);
		
		standpanel4 = new JPanel();
		standpanel4.setBackground(Color.BLACK);
		standpanel4.setBounds(780, 350, 210, 40);
		getContentPane().add(standpanel4);
		
		standpanel5 = new JPanel();
		standpanel5.setBackground(Color.BLACK);
		standpanel5.setBounds(780, 90, 210, 40);
		getContentPane().add(standpanel5);
		
		dealerstatusPanel = new JPanel();
		dealerstatusPanel.setBounds(140, 0, 210, 40);
		getContentPane().add(dealerstatusPanel);
		dealerstatusPanel.setBackground(Color.BLACK);
		
		dealerPanel_result = new JPanel();
		dealerPanel_result.setBounds(0, 15, 500, 123);
		//getContentPane().add(dealerPanel_result);
		dealerPanel_result.setBackground(Color.BLACK);

		// static JPanel page1=new JPanel()
//		{
//			Image background=new ImageIcon(Main.class.getResource("../image/background1.png")).getImage();
//			public void paint(Graphics g) {//그리는 함수
//				g.drawImage(background, 0, 0, null);//background를 그려줌		
//			}
//		};

		Bet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String bet;
				
				//2번할것
				bet = (String)spinner.getValue();
				//BetInput.getText();
				betting = Integer.parseInt(bet); // 정수로 변환
				if (betting < money || betting == money) {
					// displayArea.append("배팅 금액 : " + betting + "\n" + "게임을 시작합니다.\n");
					ChatArea.append("배팅 금액 : " + betting + "\n" + "게임을 시작합니다.\n");
					Bet.setEnabled(false);
					
					//여기수정할것임 그..앞차례가 stand해야 뒷차례가 버튼을 좀 활성화시킬수있게 할것임
					//Hit.setEnabled(true);
					//Stand.setEnabled(true);
					//BetInput.setText("");
					money = money - betting;
					sendData("Bet");
				} else {
					// displayArea.append("\n 배팅 금액은 " + money + "보다 작아야 합니다.");
					ChatArea.append("\n 배팅 금액은 " + money + "보다 작아야 합니다.");
					//BetInput.setText("");
					//BetInput.requestFocus();
				}
			}
		});
		Send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String ChatMessage;
				ChatMessage = ChatInput.getText();
				sendData(ChatMessage);
				ChatInput.setText("");
				// ChatInput.requestFocus();
			}
		});
		/*
		 * 버튼 이벤트 설정 HIT를 누른 경우 'hit' 메세지가 서버로 전송 STAND를 누른 경우 'Stand' 메세지가 서버로 전송 Send를
		 * 누른 경우 ChatInput 텍스트 필드에 적은 내용을 서버로 전송 Bet 을 누른 경우 'Bet' 메세지가 서버로 전송 후
		 * Hit,Stand 버튼 활성화
		 */
		Hit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendData(userid.getText()+" hit");
				//System.out.println("현재 보내는 아이디도 확인 : "+userid.getText());
				//sendData(client_id+" hit");
			}
		});
	}

	/*
	 * 서버로 부터 받아 올 입력 및 출력 스트림 및 통신 정보를 초기화 서버로 부터 전송 받은 메시지 정보에 따른 작업
	 * 수행(processConnection)
	 */
	public void runClient() throws IOException {

		try { // 연결 정보 설정
			client = new Socket(InetAddress.getByName(Game_Server), 23001);

			output = new ObjectOutputStream(client.getOutputStream());
			output.flush();
			input = new ObjectInputStream(client.getInputStream());

			// displayArea.append("\n연결 성공");
			ChatArea.append("\n연결 성공");
			// displayArea.append("\n잔액 : " + money + "\n");
			ChatArea.append("\n잔액 : " + money + "\n");
			processConnection(); // 초기화 된 스트림을 통해 작업 수행

		} catch (EOFException eofException) {
			System.out.println(eofException);
		} catch (IOException ioException) {
			System.out.println(ioException);
		} finally {
			// displayArea.append("\n 서버와 연결 종료");
			ChatArea.append("\n 서버와 연결 종료");
			output.close();
			input.close();
			client.close();
		}

	}

	/*
	 * 서버로 부터 전송 받은 메세지를 통해 작업 수행 게임 결과로서 Win, Lose, Draw가 온 경우 각각 배팅 금액을 갱신 후 콘솔 창에
	 * 출력 Bust 또는 Stand를 한 경우 더 이상 게임에 참여하지 못하도록 입력패널 삭제 별칭 즉, Client가 포함 된 메세지는 채팅
	 * 메세지로 분류하여 출력
	 */
	private void processConnection() throws IOException {
		do {
			try {
				message = (String) input.readObject(); // 메세지 한 줄씩 받아오기
				/* 게임 결과 처리 */
				if (message.contains("승리했습니다")) {
					money += 2 * betting;
					// displayArea.append("\n잔액 : " + money + "\n");
					ChatArea.append("\n잔액 : " + money + "\n");
				} else if (message.contains("패배했습니다")) {
					money -= betting;
					// displayArea.append("\n잔액 : " + money + "\n");
					ChatArea.append("\n잔액 : " + money + "\n");
				} else if (message.contains("비겼습니다")) {
					money += betting;
					// displayArea.append("\n잔액 : " + money + "\n");
					ChatArea.append("\n잔액 : " + money + "\n");
				}

				/* Bust, Stand 입력 처리 */
				if (message.contains("버스트!") || message.contains("현재 포지션 [stand] 다른 유저의 차례 마무리 대기")) {
					//buttonPanel.setVisible(false); // 게임 참여 불가하도록 입력 패널 삭제
				}

				/* 채팅 메세지 분류 */
				if (message.contains("Client")) {
					/*
					client_num1 = "";
					client_num2 = "";
					client_num3 = "";
					*/
					//여기서 클라이언트 번호를 집어넣어줌
					if(message.contains("유저0") && this.userid.getText().equals("")) {
						this.userid.setText(client_id[0]);
						//client_id = "0";
					}else if(message.contains("유저1")&& this.userid.getText().equals("")) {
						//client_id = "1";
						this.userid.setText(client_id[1]);
						//client_num2 = "1";
					}else if(message.contains("유저2")&& this.userid.getText().equals("")) {
						this.userid.setText(client_id[2]);
						//client_id = "2";
						//client_num3 = "2";
					}else if(message.contains("유저3")&& this.userid.getText().equals("")) {
						this.userid.setText(client_id[3]);
						//client_id = "2";
						//client_num3 = "2";
					}else if(message.contains("유저4")&& this.userid.getText().equals("")) {
						this.userid.setText(client_id[4]);
						//client_id = "2";
						//client_num3 = "2";
					}
					
					
					//여기서 클라이언트 번호를 집어넣어줌
					ChatArea.append("\n" + message);
				} else { // Client가 포함되지 않은 내용은 게임 정보 출력
					// displayArea.append("\n" + message);
					//★★★★★★★★★★★★★★★★★★1번 clientpanel에 카드채우기
					
					
					//승패
					if(message.contains("0 딜러 버스트 승리했습니다") && this.userid.getText().equals("호랑이")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("youwin.png").toString())));
						bustpanel1.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					if(message.contains("0 딜러 버스트 패배했습니다") && this.userid.getText().equals("호랑이")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("youlose.png").toString())));
						bustpanel1.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					if(message.contains("0 딜러 버스트 비겼습니다") && this.userid.getText().equals("호랑이")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("draw.png").toString())));
						bustpanel1.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					//승패끝
					
					
					
					//if(message.contains("0 Stand상태 들어감") && this.userid.getText().equals("호랑이")) {
					//승패
					if(message.contains("0 승리했습니다") && this.userid.getText().equals("호랑이")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("youwin.png").toString())));
						bustpanel1.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						//bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						//bustpanel1.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					if(message.contains("0 패배했습니다") && this.userid.getText().equals("호랑이")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("youlose.png").toString())));
						bustpanel1.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel1.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					if(message.contains("0 비겼습니다") && this.userid.getText().equals("호랑이")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("draw.png").toString())));
						bustpanel1.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel1.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					//승패끝
					
					if(message.contains("0 Stand상태 들어감")) {
						standpanel1.add(new JLabel(new ImageIcon("cards/" + ("stand.png").toString())));
						standpanel1.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						standpanel1.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						standpanel1.updateUI();
					}
					
					//여기는 그..버튼활성화 즉 앞의 친구가 차례를 끝내야 인제..그 버튼을 누를수 있게하는부분 ★★★★★★★★★★★★여기좀중요 교수님이 말씀하신부분
					if(message.contains("0 Stand상태 들어감") && this.userid.getText().equals("나비")) {
						Hit.setEnabled(true);
						Stand.setEnabled(true);	
					}
					
					if(message.contains("0 bust입니다") && this.userid.getText().equals("나비")) {
						Hit.setEnabled(true);
						Stand.setEnabled(true);	
					}
					//여기는 그..버튼활성화 즉 앞의 친구가 차례를 끝내야 인제..그 버튼을 누를수 있게하는부분 끝
					
					
					if(message.contains("0 bust입니다") && this.userid.getText().equals("호랑이")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						bustpanel1.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel1.updateUI();
						//bustpanel1.
					}
					
					if(message.contains("0 bust입니다")) {
						//stand꺼 그대로 복사해온것
						standpanel1.add(new JLabel(new ImageIcon("cards/" + ("bust2.png").toString())));
						standpanel1.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						standpanel1.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						standpanel1.updateUI();
						//stand꺼 그대로 복사해온것 끝
					}
					
					
					//hit으로 카드 추가할때
					if(message.contains("0 hit추가카드")) {
						//sendData("hit추가카드"+Label[index]+"_"+nextcard);
						client_one_message = message.replace("0 hit추가카드","");
						String []cardLabel = client_one_message.split("_");
						String f_cardname="";
						if(cardLabel[0].equals("클로버")) {
							f_cardname += "0";
							f_cardname = f_card(f_cardname,cardLabel[1]);
						}
						if (cardLabel[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						
						if (cardLabel[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						if (cardLabel[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						client1_panel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						client1_panel.updateUI();
					}
					//여긴 처음에 0번유저는 그..hit , stand 버튼이 처음부터 활성화 되어있어야하기때문에 이렇게 해둠
					if(message.contains("0 \nDealer Card:") && this.userid.getText().equals("호랑이") ) {
						Hit.setEnabled(true);
						Stand.setEnabled(true);
					}
					
					//서버카드 띄우기위한 처리
					if(message.contains("0 \nDealer Card:")) {
						client_one_message = message.replace("0 \nDealer Card:","");
						//System.out.println("s.1. message:"+client_one_message);
						// 여기에서 카드 이미지 출력하면 될듯한데?
						String[] cardLabel = client_one_message.split(",");
						//System.out.println("s.2. cardLabel[0],[1]"+cardLabel[0]+" "+cardLabel[1]);
						String[] firstCard = cardLabel[0].split("_");
						//System.out.println("s.3. firstCard[0],[1]"+firstCard[0]+" "+firstCard[1]);
						String[] secondCard = cardLabel[1].split("_");
						//System.out.println("s.3. secondCard[0],[1]"+secondCard[0]+" "+secondCard[1]);
						String f_cardname = "";
						String s_cardname = "";
						if (firstCard[0].equals("클로버")) {
							f_cardname += "0";
							// 이건 모든카드 공용
							f_cardname = f_card(f_cardname, firstCard[1]);
							// 이건 모든카드 공용
						}
						if (secondCard[0].equals("클로버")) {
							s_cardname += "0";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("스페이드")) {
							s_cardname += "1";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("하트")) {
							s_cardname += "2";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("다이아")) {
							s_cardname += "3";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						// 여기에서 카드 이미지 출력하면 될듯한데? 끝
						//System.out.println("f_cardname : " + f_cardname);
						//System.out.println("s_cardname : " + s_cardname);
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						//후에 결과 보여줄 녀석 저장
						dealerPanel_result.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						dealerPanel_result.updateUI();
						dealerPanel_result.add(new JLabel(new ImageIcon("cards/" + (s_cardname+".png").toString())));
						dealerPanel_result.updateUI();
						
						
						dealerPanel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						dealerPanel.updateUI();
						//dealerPanel.add(new JLabel(new ImageIcon("cards/" + (s_cardname+".png").toString())));
						dealerPanel.add(new JLabel(new ImageIcon("cards/" + ("b2fv.png").toString())));
						dealerPanel.updateUI();
					}
					//서버카드 띄우기위한 처리
					
					
					// ★이건 원래 그..카드뭐가졌는지 표기하라고 놓은거입
					ChatArea.append("\n"+message);
					if (message.contains("0 \n소유한카드")) {
						client_one_message = message.replace("0 \n소유한카드:","");
						//System.out.println("1. message:"+client_one_message);
						// 여기에서 카드 이미지 출력하면 될듯한데?
						String[] cardLabel = client_one_message.split(",");
						//System.out.println("2. cardLabel[0],[1]"+cardLabel[0]+" "+cardLabel[1]);
						String[] firstCard = cardLabel[0].split("_");
						//System.out.println("3. firstCard[0],[1]"+firstCard[0]+" "+firstCard[1]);
						String[] secondCard = cardLabel[1].split("_");
						//System.out.println("3. secondCard[0],[1]"+secondCard[0]+" "+secondCard[1]);
						String f_cardname = "";
						String s_cardname = "";
						if (firstCard[0].equals("클로버")) {
							f_cardname += "0";
							// 이건 모든카드 공용
							f_cardname = f_card(f_cardname, firstCard[1]);
							// 이건 모든카드 공용
						}
						if (secondCard[0].equals("클로버")) {
							s_cardname += "0";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("스페이드")) {
							s_cardname += "1";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("하트")) {
							s_cardname += "2";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("다이아")) {
							s_cardname += "3";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						// 여기에서 카드 이미지 출력하면 될듯한데? 끝
						//System.out.println("1 f_cardname : " + f_cardname);
						//System.out.println("1 s_cardname : " + s_cardname);
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						client1_panel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						client1_panel.updateUI();
						client1_panel.add(new JLabel(new ImageIcon("cards/" + (s_cardname+".png").toString())));
						client1_panel.updateUI();
					}
					
					//실험 나인것 표시할려고 하는것 white로 할것임
					if (message.contains("0 \n소유한카드") && this.userid.getText().equals("호랑이")) {
						client1_panel.setBackground(Color.white);
					}
					//실험 나인것 표시할려고 하는것 white로 할것임 끝
					
					
					//★★★★★★★★★★★★★★★★★★1번 clientpanel에 카드채우기 끝
					
					//★★★★★★★★★★★★★★★★★★2번 clientpanel에 카드채우기
					
					//승패
					if(message.contains("1 딜러 버스트 승리했습니다") && this.userid.getText().equals("나비")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel2.add(new JLabel(new ImageIcon("cards/" + ("youwin.png").toString())));
						bustpanel2.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					if(message.contains("1 딜러 버스트 패배했습니다") && this.userid.getText().equals("나비")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel2.add(new JLabel(new ImageIcon("cards/" + ("youlose.png").toString())));
						bustpanel2.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
						
					}
					if(message.contains("1 딜러 버스트 비겼습니다") && this.userid.getText().equals("나비")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel2.add(new JLabel(new ImageIcon("cards/" + ("draw.png").toString())));
						bustpanel2.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					//승패끝
					
					
					
					//승패
					if(message.contains("1 승리했습니다") && this.userid.getText().equals("나비")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel2.add(new JLabel(new ImageIcon("cards/" + ("youwin.png").toString())));
						bustpanel2.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						//bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						//bustpanel1.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					if(message.contains("1 패배했습니다") && this.userid.getText().equals("나비")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel2.add(new JLabel(new ImageIcon("cards/" + ("youlose.png").toString())));
						bustpanel2.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel2.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel2.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					if(message.contains("1 비겼습니다") && this.userid.getText().equals("나비")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel2.add(new JLabel(new ImageIcon("cards/" + ("draw.png").toString())));
						bustpanel2.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel2.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel2.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					//승패끝
					
					
					
					
					if(message.contains("1 Stand상태 들어감")) {
						standpanel2.add(new JLabel(new ImageIcon("cards/" + ("stand.png").toString())));
						standpanel2.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						standpanel2.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						standpanel2.updateUI();
					}
					
					//여기는 그..버튼활성화 즉 앞의 친구가 차례를 끝내야 인제..그 버튼을 누를수 있게하는부분 ★★★★★★★★★★★★여기좀중요 교수님이 말씀하신부분
					if(message.contains("1 Stand상태 들어감") && this.userid.getText().equals("다람쥐")) {
						Hit.setEnabled(true);
						Stand.setEnabled(true);	
					}
					if(message.contains("1 bust입니다") && this.userid.getText().equals("다람쥐")) {
						Hit.setEnabled(true);
						Stand.setEnabled(true);	
					}
					//여기는 그..버튼활성화 즉 앞의 친구가 차례를 끝내야 인제..그 버튼을 누를수 있게하는부분 끝
					
					
					// displayArea.append("\n" + message);
					if(message.contains("1 bust입니다") && this.userid.getText().equals("나비")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel2.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						bustpanel2.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel2.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel2.updateUI();
						//bustpanel1.
					}
					
					if(message.contains("1 bust입니다")) {
						//stand꺼 그대로 복사해온것
						standpanel2.add(new JLabel(new ImageIcon("cards/" + ("bust2.png").toString())));
						standpanel2.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						standpanel2.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						standpanel2.updateUI();
						//stand꺼 그대로 복사해온것 끝
					}
					
					
					//hit으로 카드 추가할때
					if(message.contains("1 hit추가카드")) {
						//sendData("hit추가카드"+Label[index]+"_"+nextcard);
						client_two_message = message.replace("1 hit추가카드","");
						String []cardLabel = client_two_message.split("_");
						String f_cardname="";
						if(cardLabel[0].equals("클로버")) {
							f_cardname += "0";
							f_cardname = f_card(f_cardname,cardLabel[1]);
						}
						if (cardLabel[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						
						if (cardLabel[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						if (cardLabel[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						client2_panel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						client2_panel.updateUI();
					}
					//hit으로 카드 추가할때임
					
					//서버카드 띄우기위한 처리
					if(message.contains("1 \nDealer Card:")) {
						client_two_message = message.replace("1 \nDealer Card:","");
						//System.out.println("s.1. message:"+client_two_message);
						// 여기에서 카드 이미지 출력하면 될듯한데?
						String[] cardLabel = client_two_message.split(",");
						//System.out.println("s.2. cardLabel[0],[1]"+cardLabel[0]+" "+cardLabel[1]);
						String[] firstCard = cardLabel[0].split("_");
						//System.out.println("s.3. firstCard[0],[1]"+firstCard[0]+" "+firstCard[1]);
						String[] secondCard = cardLabel[1].split("_");
						//System.out.println("s.3. secondCard[0],[1]"+secondCard[0]+" "+secondCard[1]);
						String f_cardname = "";
						String s_cardname = "";
						if (firstCard[0].equals("클로버")) {
							f_cardname += "0";
							// 이건 모든카드 공용
							f_cardname = f_card(f_cardname, firstCard[1]);
							// 이건 모든카드 공용
						}
						if (secondCard[0].equals("클로버")) {
							s_cardname += "0";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("스페이드")) {
							s_cardname += "1";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("하트")) {
							s_cardname += "2";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("다이아")) {
							s_cardname += "3";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						// 여기에서 카드 이미지 출력하면 될듯한데? 끝
						//System.out.println("f_cardname : " + f_cardname);
						//System.out.println("s_cardname : " + s_cardname);
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						dealerPanel_result.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						dealerPanel_result.updateUI();
						dealerPanel_result.add(new JLabel(new ImageIcon("cards/" + (s_cardname+".png").toString())));
						dealerPanel_result.updateUI();
						
						
						
						
						dealerPanel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						dealerPanel.updateUI();
						dealerPanel.add(new JLabel(new ImageIcon("cards/" + ("b2fv.png").toString())));
						dealerPanel.updateUI();
					}
					//서버카드 띄우기위한 처리
					
					
					// ★이건 원래 그..카드뭐가졌는지 표기하라고 놓은거입
					//ChatArea.append("\n"+message);
					if (message.contains("1 \n소유한카드")) {
						client_two_message = message.replace("1 \n소유한카드:","");
						//System.out.println("1. message:"+client_two_message);
						// 여기에서 카드 이미지 출력하면 될듯한데?
						String[] cardLabel = client_two_message.split(",");
						//System.out.println("2. cardLabel[0],[1]"+cardLabel[0]+" "+cardLabel[1]);
						String[] firstCard = cardLabel[0].split("_");
						//System.out.println("3. firstCard[0],[1]"+firstCard[0]+" "+firstCard[1]);
						String[] secondCard = cardLabel[1].split("_");
						//System.out.println("3. secondCard[0],[1]"+secondCard[0]+" "+secondCard[1]);
						String f_cardname = "";
						String s_cardname = "";
						if (firstCard[0].equals("클로버")) {
							f_cardname += "0";
							// 이건 모든카드 공용
							f_cardname = f_card(f_cardname, firstCard[1]);
							// 이건 모든카드 공용
						}
						if (secondCard[0].equals("클로버")) {
							s_cardname += "0";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("스페이드")) {
							s_cardname += "1";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("하트")) {
							s_cardname += "2";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("다이아")) {
							s_cardname += "3";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						// 여기에서 카드 이미지 출력하면 될듯한데? 끝
						//ystem.out.println("2 f_cardname : " + f_cardname);
						//System.out.println("2 s_cardname : " + s_cardname);
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						client2_panel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						client2_panel.updateUI();
						client2_panel.add(new JLabel(new ImageIcon("cards/" + (s_cardname+".png").toString())));
						client2_panel.updateUI();
					}
					
					if (message.contains("1 \n소유한카드") && this.userid.getText().equals("나비")) {
						client2_panel.setBackground(Color.white);
					}
					
					//★★★★★★★★★★★★★★★★★★2번 clientpanel에 카드채우기끝
					
					//★★★★★★★★★★★★★★★★★★3번 clientpanel에 카드채우기
					
					//승패
					if(message.contains("2 딜러 버스트 승리했습니다") && this.userid.getText().equals("다람쥐")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel3.add(new JLabel(new ImageIcon("cards/" + ("youwin.png").toString())));
						bustpanel3.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					if(message.contains("2 딜러 버스트 패배했습니다") && this.userid.getText().equals("다람쥐")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel3.add(new JLabel(new ImageIcon("cards/" + ("youlose.png").toString())));
						bustpanel3.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					if(message.contains("2 딜러 버스트 비겼습니다") && this.userid.getText().equals("다람쥐")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel3.add(new JLabel(new ImageIcon("cards/" + ("draw.png").toString())));
						bustpanel3.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					//승패끝
					
					
					
					
					
					//승패
					if(message.contains("2 승리했습니다") && this.userid.getText().equals("다람쥐")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel3.add(new JLabel(new ImageIcon("cards/" + ("youwin.png").toString())));
						bustpanel3.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						//bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						//bustpanel1.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					if(message.contains("2 패배했습니다") && this.userid.getText().equals("다람쥐")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel3.add(new JLabel(new ImageIcon("cards/" + ("youlose.png").toString())));
						bustpanel3.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel3.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel3.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					if(message.contains("2 비겼습니다") && this.userid.getText().equals("다람쥐")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel3.add(new JLabel(new ImageIcon("cards/" + ("draw.png").toString())));
						bustpanel3.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel3.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel3.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					//승패끝
					
					
					
					
					if(message.contains("2 Stand상태 들어감")) {
						standpanel3.add(new JLabel(new ImageIcon("cards/" + ("stand.png").toString())));
						standpanel3.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						standpanel3.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						standpanel3.updateUI();
					}
					
					//여기는 그..버튼활성화 즉 앞의 친구가 차례를 끝내야 인제..그 버튼을 누를수 있게하는부분 ★★★★★★★★★★★★여기좀중요 교수님이 말씀하신부분
					if(message.contains("2 Stand상태 들어감") && this.userid.getText().equals("사자")) {
						Hit.setEnabled(true);
						Stand.setEnabled(true);	
					}
					
					if(message.contains("2 bust입니다") && this.userid.getText().equals("사자")) {
						Hit.setEnabled(true);
						Stand.setEnabled(true);	
					}
					//여기는 그..버튼활성화 즉 앞의 친구가 차례를 끝내야 인제..그 버튼을 누를수 있게하는부분 끝
					
					if(message.contains("2 bust입니다") && this.userid.getText().equals("다람쥐")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel3.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						bustpanel3.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel3.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel3.updateUI();
						//bustpanel1.
					}
					
					if(message.contains("2 bust입니다")) {
						//stand꺼 그대로 복사해온것
						standpanel3.add(new JLabel(new ImageIcon("cards/" + ("bust2.png").toString())));
						standpanel3.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						standpanel3.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						standpanel3.updateUI();
						//stand꺼 그대로 복사해온것 끝
					}
					
					
					if(message.contains("2 hit추가카드")) {
						//sendData("hit추가카드"+Label[index]+"_"+nextcard);
						client_three_message = message.replace("2 hit추가카드","");
						String []cardLabel = client_three_message.split("_");
						String f_cardname="";
						if(cardLabel[0].equals("클로버")) {
							f_cardname += "0";
							f_cardname = f_card(f_cardname,cardLabel[1]);
						}
						if (cardLabel[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						
						if (cardLabel[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						if (cardLabel[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						client3_panel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						client3_panel.updateUI();
					}
					//hit으로 카드 추가할때임
					
					//서버카드 띄우기위한 처리
					if(message.contains("2 \nDealer Card:")) {
						client_three_message = message.replace("2 \nDealer Card:","");
						//System.out.println("s.1. message:"+client_three_message);
						// 여기에서 카드 이미지 출력하면 될듯한데?
						String[] cardLabel = client_three_message.split(",");
						//System.out.println("s.2. cardLabel[0],[1]"+cardLabel[0]+" "+cardLabel[1]);
						String[] firstCard = cardLabel[0].split("_");
						//System.out.println("s.3. firstCard[0],[1]"+firstCard[0]+" "+firstCard[1]);
						String[] secondCard = cardLabel[1].split("_");
						//System.out.println("s.3. secondCard[0],[1]"+secondCard[0]+" "+secondCard[1]);
						String f_cardname = "";
						String s_cardname = "";
						if (firstCard[0].equals("클로버")) {
							f_cardname += "0";
							// 이건 모든카드 공용
							f_cardname = f_card(f_cardname, firstCard[1]);
							// 이건 모든카드 공용
						}
						if (secondCard[0].equals("클로버")) {
							s_cardname += "0";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("스페이드")) {
							s_cardname += "1";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("하트")) {
							s_cardname += "2";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("다이아")) {
							s_cardname += "3";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						// 여기에서 카드 이미지 출력하면 될듯한데? 끝
						//System.out.println("f_cardname : " + f_cardname);
						//System.out.println("s_cardname : " + s_cardname);
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						dealerPanel_result.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						dealerPanel_result.updateUI();
						dealerPanel_result.add(new JLabel(new ImageIcon("cards/" + (s_cardname+".png").toString())));
						dealerPanel_result.updateUI();
						
						
						
						dealerPanel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						dealerPanel.updateUI();
						dealerPanel.add(new JLabel(new ImageIcon("cards/" + ("b2fv.png").toString())));
						dealerPanel.updateUI();
					}
					//서버카드 띄우기위한 처리
					
					
					// ★이건 원래 그..카드뭐가졌는지 표기하라고 놓은거입
					//ChatArea.append("\n"+message);
					if (message.contains("2 \n소유한카드")) {
						client_three_message = message.replace("2 \n소유한카드:","");
						//System.out.println("1. message:"+client_three_message);
						// 여기에서 카드 이미지 출력하면 될듯한데?
						String[] cardLabel = client_three_message.split(",");
						//System.out.println("2. cardLabel[0],[1]"+cardLabel[0]+" "+cardLabel[1]);
						String[] firstCard = cardLabel[0].split("_");
						//System.out.println("3. firstCard[0],[1]"+firstCard[0]+" "+firstCard[1]);
						String[] secondCard = cardLabel[1].split("_");
						//System.out.println("3. secondCard[0],[1]"+secondCard[0]+" "+secondCard[1]);
						String f_cardname = "";
						String s_cardname = "";
						if (firstCard[0].equals("클로버")) {
							f_cardname += "0";
							// 이건 모든카드 공용
							f_cardname = f_card(f_cardname, firstCard[1]);
							// 이건 모든카드 공용
						}
						if (secondCard[0].equals("클로버")) {
							s_cardname += "0";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("스페이드")) {
							s_cardname += "1";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("하트")) {
							s_cardname += "2";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("다이아")) {
							s_cardname += "3";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						// 여기에서 카드 이미지 출력하면 될듯한데? 끝
						//System.out.println("f_cardname : " + f_cardname);
						//System.out.println("s_cardname : " + s_cardname);
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						client3_panel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						client3_panel.updateUI();
						client3_panel.add(new JLabel(new ImageIcon("cards/" + (s_cardname+".png").toString())));
						client3_panel.updateUI();
					}
					
					if (message.contains("2 \n소유한카드") && this.userid.getText().equals("다람쥐")) {
						client3_panel.setBackground(Color.white);
					}
					
					//★★★★★★★★★★★★★★★★★★3번 clientpanel에 카드채우기 끝
					
					//★★★★★★★★★★★★★★★★★★4번 clientpanel에 카드채우기
					
					//승패
					if(message.contains("3 딜러 버스트 승리했습니다") && this.userid.getText().equals("사자")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel4.add(new JLabel(new ImageIcon("cards/" + ("youwin.png").toString())));
						bustpanel4.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					if(message.contains("3 딜러 버스트 패배했습니다") && this.userid.getText().equals("사자")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel4.add(new JLabel(new ImageIcon("cards/" + ("youlose.png").toString())));
						bustpanel4.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
						
					}
					if(message.contains("3 딜러 버스트 비겼습니다") && this.userid.getText().equals("사자")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel4.add(new JLabel(new ImageIcon("cards/" + ("draw.png").toString())));
						bustpanel4.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					//승패끝
					
					
					
					//승패
					if(message.contains("3 승리했습니다") && this.userid.getText().equals("사자")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel4.add(new JLabel(new ImageIcon("cards/" + ("youwin.png").toString())));
						bustpanel4.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						//bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						//bustpanel1.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					if(message.contains("3 패배했습니다") && this.userid.getText().equals("사자")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel4.add(new JLabel(new ImageIcon("cards/" + ("youlose.png").toString())));
						bustpanel4.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel4.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel4.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					if(message.contains("3 비겼습니다") && this.userid.getText().equals("사자")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel4.add(new JLabel(new ImageIcon("cards/" + ("draw.png").toString())));
						bustpanel4.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel4.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel4.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					//승패끝
					
					
					
					
					if(message.contains("3 Stand상태 들어감")) {
						standpanel4.add(new JLabel(new ImageIcon("cards/" + ("stand.png").toString())));
						standpanel4.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						standpanel4.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						standpanel4.updateUI();
					}
					
					//여기는 그..버튼활성화 즉 앞의 친구가 차례를 끝내야 인제..그 버튼을 누를수 있게하는부분 ★★★★★★★★★★★★여기좀중요 교수님이 말씀하신부분
					if(message.contains("3 Stand상태 들어감") && this.userid.getText().equals("오뚜기")) {
						Hit.setEnabled(true);
						Stand.setEnabled(true);	
					}
					if(message.contains("3 bust입니다") && this.userid.getText().equals("오뚜기")) {
						Hit.setEnabled(true);
						Stand.setEnabled(true);	
					}
					//여기는 그..버튼활성화 즉 앞의 친구가 차례를 끝내야 인제..그 버튼을 누를수 있게하는부분 끝
					
					
					if(message.contains("3 bust입니다") && this.userid.getText().equals("사자")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel4.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						bustpanel4.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel4.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel4.updateUI();
						//bustpanel1.
					}
					if(message.contains("3 bust입니다")) {
						//stand꺼 그대로 복사해온것
						standpanel4.add(new JLabel(new ImageIcon("cards/" + ("bust2.png").toString())));
						standpanel4.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						standpanel4.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						standpanel4.updateUI();
						//stand꺼 그대로 복사해온것 끝
					}
					
					if(message.contains("3 hit추가카드")) {
						//sendData("hit추가카드"+Label[index]+"_"+nextcard);
						client_three_message = message.replace("3 hit추가카드","");
						String []cardLabel = client_three_message.split("_");
						String f_cardname="";
						if(cardLabel[0].equals("클로버")) {
							f_cardname += "0";
							f_cardname = f_card(f_cardname,cardLabel[1]);
						}
						if (cardLabel[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						
						if (cardLabel[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						if (cardLabel[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						client4_panel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						client4_panel.updateUI();
					}
					//hit으로 카드 추가할때임
					
					//서버카드 띄우기위한 처리
					if(message.contains("3 \nDealer Card:")) {
						client_three_message = message.replace("3 \nDealer Card:","");
						//System.out.println("s.1. message:"+client_three_message);
						// 여기에서 카드 이미지 출력하면 될듯한데?
						String[] cardLabel = client_three_message.split(",");
						//System.out.println("s.2. cardLabel[0],[1]"+cardLabel[0]+" "+cardLabel[1]);
						String[] firstCard = cardLabel[0].split("_");
						//System.out.println("s.3. firstCard[0],[1]"+firstCard[0]+" "+firstCard[1]);
						String[] secondCard = cardLabel[1].split("_");
						//System.out.println("s.3. secondCard[0],[1]"+secondCard[0]+" "+secondCard[1]);
						String f_cardname = "";
						String s_cardname = "";
						if (firstCard[0].equals("클로버")) {
							f_cardname += "0";
							// 이건 모든카드 공용
							f_cardname = f_card(f_cardname, firstCard[1]);
							// 이건 모든카드 공용
						}
						if (secondCard[0].equals("클로버")) {
							s_cardname += "0";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("스페이드")) {
							s_cardname += "1";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("하트")) {
							s_cardname += "2";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("다이아")) {
							s_cardname += "3";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						// 여기에서 카드 이미지 출력하면 될듯한데? 끝
						//System.out.println("f_cardname : " + f_cardname);
						//System.out.println("s_cardname : " + s_cardname);
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						dealerPanel_result.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						dealerPanel_result.updateUI();
						dealerPanel_result.add(new JLabel(new ImageIcon("cards/" + (s_cardname+".png").toString())));
						dealerPanel_result.updateUI();
						
						
						dealerPanel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						dealerPanel.updateUI();
						dealerPanel.add(new JLabel(new ImageIcon("cards/" + ("b2fv.png").toString())));
						dealerPanel.updateUI();
					}
					//서버카드 띄우기위한 처리
					
					
					// ★이건 원래 그..카드뭐가졌는지 표기하라고 놓은거입
					//ChatArea.append("\n"+message);
					if (message.contains("3 \n소유한카드")) {
						client_three_message = message.replace("3 \n소유한카드:","");
						//System.out.println("1. message:"+client_three_message);
						// 여기에서 카드 이미지 출력하면 될듯한데?
						String[] cardLabel = client_three_message.split(",");
						//System.out.println("2. cardLabel[0],[1]"+cardLabel[0]+" "+cardLabel[1]);
						String[] firstCard = cardLabel[0].split("_");
						//System.out.println("3. firstCard[0],[1]"+firstCard[0]+" "+firstCard[1]);
						String[] secondCard = cardLabel[1].split("_");
						//System.out.println("3. secondCard[0],[1]"+secondCard[0]+" "+secondCard[1]);
						String f_cardname = "";
						String s_cardname = "";
						if (firstCard[0].equals("클로버")) {
							f_cardname += "0";
							// 이건 모든카드 공용
							f_cardname = f_card(f_cardname, firstCard[1]);
							// 이건 모든카드 공용
						}
						if (secondCard[0].equals("클로버")) {
							s_cardname += "0";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("스페이드")) {
							s_cardname += "1";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("하트")) {
							s_cardname += "2";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("다이아")) {
							s_cardname += "3";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						// 여기에서 카드 이미지 출력하면 될듯한데? 끝
						//System.out.println("f_cardname : " + f_cardname);
						//System.out.println("s_cardname : " + s_cardname);
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						client4_panel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						client4_panel.updateUI();
						client4_panel.add(new JLabel(new ImageIcon("cards/" + (s_cardname+".png").toString())));
						client4_panel.updateUI();
					}
					if (message.contains("3 \n소유한카드") && this.userid.getText().equals("사자")) {
						client4_panel.setBackground(Color.white);
					}
					
					//★★★★★★★★★★★★★★★★★★4번 clientpanel에 카드채우기 끝
					
					//★★★★★★★★★★★★★★★★★★5번 clientpanel에 카드채우기
					
					//승패
					if(message.contains("4 딜러 버스트 승리했습니다") && this.userid.getText().equals("오뚜기")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel5.add(new JLabel(new ImageIcon("cards/" + ("youwin.png").toString())));
						bustpanel5.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					if(message.contains("4 딜러 버스트 패배했습니다") && this.userid.getText().equals("오뚜기")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel5.add(new JLabel(new ImageIcon("cards/" + ("youlose.png").toString())));
						bustpanel5.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					if(message.contains("4 딜러 버스트 비겼습니다") && this.userid.getText().equals("오뚜기")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel5.add(new JLabel(new ImageIcon("cards/" + ("draw.png").toString())));
						bustpanel5.updateUI();
						
						dealerstatusPanel.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						dealerstatusPanel.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					//승패끝
					
					
					
					//승패
					if(message.contains("4 승리했습니다") && this.userid.getText().equals("오뚜기")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel5.add(new JLabel(new ImageIcon("cards/" + ("youwin.png").toString())));
						bustpanel5.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						//bustpanel1.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						//bustpanel1.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					if(message.contains("4 패배했습니다") && this.userid.getText().equals("오뚜기")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel5.add(new JLabel(new ImageIcon("cards/" + ("youlose.png").toString())));
						bustpanel5.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel5.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel5.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					if(message.contains("4 비겼습니다") && this.userid.getText().equals("오뚜기")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel5.add(new JLabel(new ImageIcon("cards/" + ("draw.png").toString())));
						bustpanel5.updateUI();
						
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel5.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel5.updateUI();
						//딜러 result 마지막 나오게 실험
						getContentPane().add(dealerPanel_result);
					}
					
					//승패끝
					
					
					
					
					if(message.contains("4 Stand상태 들어감")) {
						standpanel5.add(new JLabel(new ImageIcon("cards/" + ("stand.png").toString())));
						standpanel5.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						standpanel5.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						standpanel5.updateUI();
					}
					
					if(message.contains("4 bust입니다") && this.userid.getText().equals("오뚜기")) {
						//client_one_message = message.replace("0 bust입니다", newChar)
						bustpanel5.add(new JLabel(new ImageIcon("cards/" + ("bust.png").toString())));
						bustpanel5.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						bustpanel5.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						bustpanel5.updateUI();
						//bustpanel1.
					}
					
					if(message.contains("4 bust입니다")) {
						//stand꺼 그대로 복사해온것
						standpanel5.add(new JLabel(new ImageIcon("cards/" + ("bust2.png").toString())));
						standpanel5.updateUI();
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						standpanel5.add(new JLabel(new ImageIcon("cards/" + ("black.png").toString())));
						standpanel5.updateUI();
						//stand꺼 그대로 복사해온것 끝
					}
					
					if(message.contains("4 hit추가카드")) {
						//sendData("hit추가카드"+Label[index]+"_"+nextcard);
						client_three_message = message.replace("4 hit추가카드","");
						String []cardLabel = client_three_message.split("_");
						String f_cardname="";
						if(cardLabel[0].equals("클로버")) {
							f_cardname += "0";
							f_cardname = f_card(f_cardname,cardLabel[1]);
						}
						if (cardLabel[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						
						if (cardLabel[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						if (cardLabel[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, cardLabel[1]);
						}
						
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						client5_panel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						client5_panel.updateUI();
					}
					//hit으로 카드 추가할때임
					
					//서버카드 띄우기위한 처리
					if(message.contains("4 \nDealer Card:")) {
						client_three_message = message.replace("4 \nDealer Card:","");
						//System.out.println("s.1. message:"+client_three_message);
						// 여기에서 카드 이미지 출력하면 될듯한데?
						String[] cardLabel = client_three_message.split(",");
						//System.out.println("s.2. cardLabel[0],[1]"+cardLabel[0]+" "+cardLabel[1]);
						String[] firstCard = cardLabel[0].split("_");
						//System.out.println("s.3. firstCard[0],[1]"+firstCard[0]+" "+firstCard[1]);
						String[] secondCard = cardLabel[1].split("_");
						//System.out.println("s.3. secondCard[0],[1]"+secondCard[0]+" "+secondCard[1]);
						String f_cardname = "";
						String s_cardname = "";
						if (firstCard[0].equals("클로버")) {
							f_cardname += "0";
							// 이건 모든카드 공용
							f_cardname = f_card(f_cardname, firstCard[1]);
							// 이건 모든카드 공용
						}
						if (secondCard[0].equals("클로버")) {
							s_cardname += "0";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("스페이드")) {
							s_cardname += "1";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("하트")) {
							s_cardname += "2";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("다이아")) {
							s_cardname += "3";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						// 여기에서 카드 이미지 출력하면 될듯한데? 끝
						//System.out.println("f_cardname : " + f_cardname);
						//System.out.println("s_cardname : " + s_cardname);
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						dealerPanel_result.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						dealerPanel_result.updateUI();
						dealerPanel_result.add(new JLabel(new ImageIcon("cards/" + (s_cardname+".png").toString())));
						dealerPanel_result.updateUI();
						
						
						dealerPanel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						dealerPanel.updateUI();
						dealerPanel.add(new JLabel(new ImageIcon("cards/" + ("b2fv.png").toString())));
						dealerPanel.updateUI();
					}
					//서버카드 띄우기위한 처리
					
					
					// ★이건 원래 그..카드뭐가졌는지 표기하라고 놓은거입
					//ChatArea.append("\n"+message);
					if (message.contains("4 \n소유한카드")) {
						client_three_message = message.replace("4 \n소유한카드:","");
						//System.out.println("1. message:"+client_three_message);
						// 여기에서 카드 이미지 출력하면 될듯한데?
						String[] cardLabel = client_three_message.split(",");
						//System.out.println("2. cardLabel[0],[1]"+cardLabel[0]+" "+cardLabel[1]);
						String[] firstCard = cardLabel[0].split("_");
						//System.out.println("3. firstCard[0],[1]"+firstCard[0]+" "+firstCard[1]);
						String[] secondCard = cardLabel[1].split("_");
						//System.out.println("3. secondCard[0],[1]"+secondCard[0]+" "+secondCard[1]);
						String f_cardname = "";
						String s_cardname = "";
						if (firstCard[0].equals("클로버")) {
							f_cardname += "0";
							// 이건 모든카드 공용
							f_cardname = f_card(f_cardname, firstCard[1]);
							// 이건 모든카드 공용
						}
						if (secondCard[0].equals("클로버")) {
							s_cardname += "0";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("스페이드")) {
							f_cardname += "1";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("스페이드")) {
							s_cardname += "1";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("하트")) {
							f_cardname += "2";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("하트")) {
							s_cardname += "2";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						if (firstCard[0].equals("다이아")) {
							f_cardname += "3";
							f_cardname = f_card(f_cardname, firstCard[1]);
						}
						if (secondCard[0].equals("다이아")) {
							s_cardname += "3";
							s_cardname = s_card(s_cardname, secondCard[1]);
						}
						// 여기에서 카드 이미지 출력하면 될듯한데? 끝
						//System.out.println("f_cardname : " + f_cardname);
						//System.out.println("s_cardname : " + s_cardname);
						//여기 이제 f_cardname와 s_cardname 잘나오니까 card이름 불러와서 이미지 불러오면될듯
						client5_panel.add(new JLabel(new ImageIcon("cards/" + (f_cardname+".png").toString())));
						client5_panel.updateUI();
						client5_panel.add(new JLabel(new ImageIcon("cards/" + (s_cardname+".png").toString())));
						client5_panel.updateUI();
					}
					
					if (message.contains("4 \n소유한카드") && this.userid.getText().equals("오뚜기")) {
						client5_panel.setBackground(Color.white);
					}
					
					//★★★★★★★★★★★★★★★★★★5번 clientpanel에 카드채우기 끝
					
				}
			} catch (ClassNotFoundException classNotFoundException) {
				// displayArea.append("\n.");
				ChatArea.append("\n.");
			}
		} while (!message.equals("SERVER>>> TERMINATE"));
	}

	String f_card(String f_cardname, String firstCard) {
		firstCard = firstCard.toLowerCase();
		// 이건 모든카드 공용
		if (firstCard.toLowerCase().equals("ace")) {
			f_cardname += "0";
		}
		if (firstCard.equals("2")) {
			f_cardname += "1";
		}
		if (firstCard.equals("3")) {
			f_cardname += "2";
		}
		if (firstCard.equals("4")) {
			f_cardname += "3";
		}
		if (firstCard.equals("5")) {
			f_cardname += "4";
		}
		if (firstCard.equals("6")) {
			f_cardname += "5";
		}
		if (firstCard.equals("7")) {
			f_cardname += "6";
		}
		if (firstCard.equals("8")) {
			f_cardname += "7";
		}
		if (firstCard.equals("9")) {
			f_cardname += "8";
		}
		if (firstCard.equals("10")) {
			f_cardname += "9";
		}
		if (firstCard.toLowerCase().equals("king")) {
			f_cardname += "12";
		}
		if (firstCard.toLowerCase().equals("queen")) {
			f_cardname += "11";
		}
		if (firstCard.toLowerCase().equals("jack")) {
			f_cardname += "10";
		}
		// 이건 모든카드 공용
		return f_cardname;
	}

	String s_card(String s_cardname, String secondCard) {
		if (secondCard.toLowerCase().equals("ace")) {
			s_cardname += "0";
		}
		if (secondCard.equals("2")) {
			s_cardname += "1";
		}
		if (secondCard.equals("3")) {
			s_cardname += "2";
		}
		if (secondCard.equals("4")) {
			s_cardname += "3";
		}
		if (secondCard.equals("5")) {
			s_cardname += "4";
		}
		if (secondCard.equals("6")) {
			s_cardname += "5";
		}
		if (secondCard.equals("7")) {
			s_cardname += "6";
		}
		if (secondCard.equals("8")) {
			s_cardname += "7";
		}
		if (secondCard.equals("9")) {
			s_cardname += "8";
		}
		if (secondCard.equals("10")) {
			s_cardname += "9";
		}
		if (secondCard.toLowerCase().equals("king")) {
			s_cardname += "12";
		}
		if (secondCard.toLowerCase().equals("queen")) {
			s_cardname += "11";
		}
		if (secondCard.toLowerCase().equals("jack")) {
			s_cardname += "10";
		}
		return s_cardname;
	}

	/* 클라이언트에서 서버로 메세지 전송 메소드 */
	private void sendData(String message) {
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException ioException) {
			// displayArea.append("\nclient의 senddata에서 오류발생");
			ChatArea.append("\nclient의 senddata에서 오류발생");
		}
	}
}