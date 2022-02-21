package xresume;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class JavaChatClientMain extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaChatClientMain frame = new JavaChatClientMain();
					frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("오류가 나는위치 탐색10");
					e.printStackTrace();
				}
			}
		});
	}

	public JavaChatClientMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 100, 220, 80);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(0, 0, 205, 38);
		contentPane.add(btnConnect);
		Myaction action = new Myaction();
		btnConnect.addActionListener(action);

	}

	class Myaction implements ActionListener// 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e){
			Scanner sc = new Scanner(System.in);
			Boolean Flag = false;
			String username = "";
			String userpw = "";
			int cnt=0;
			// 아이디.비밀번호 체크단
			String path = JavaChatClientMain.class.getResource("").getPath();
			File file = new File(path + "id&pw.txt");
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));
				// 라인을 하나씩 읽어온뒤 ArrayList에 저장
				ArrayList<String> arr = new ArrayList<String>();
				try {
					while(true) {
						String line = br.readLine();
						if(line==null)break;
						String id = line.split(",")[0];
						arr.add(id);
						String pw = line.split(",")[1];
						arr.add(pw);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				for (int i = 0; i < 3; i++) {
					System.out.println("id를 입력하세요");
					username = sc.next();
					for(int k=0; k<arr.size(); k++) {
						if(arr.get(k).equals(username)) {
							Flag = true;
							cnt = k;
							break;
						}else {
							Flag = false;
							continue;
						}
					}
					if(Flag == true) {
						System.out.println("pw를 입력하세요");
						userpw = sc.next();
						if(arr.get(cnt+1).equals(userpw)){
							Flag = false;
							break;
						}else {
							Flag = false;
							continue;
						}
					}
					
					// 여기까지 검색단?
					if (i == 2) {
						System.out.println("채팅방 입장이 불가합니다.");
						System.exit(0);
					}
				}
				// System.out.println(br.readLine());
			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
			}
			//

			String ip_addr = "127.0.0.1";
			String port_no = "30000";
			JavaChatClientView view = new JavaChatClientView(username, ip_addr, port_no);
			setVisible(false);
		}
	}
}
