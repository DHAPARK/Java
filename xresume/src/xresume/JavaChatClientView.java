package xresume;
// JavaChatClientView.java
// 실질적인 채팅 창
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class JavaChatClientView extends JFrame {
	private JPanel contentPane;
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private JTextArea textArea;
	private static final  int BUF_LEN = 128; //  Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private BufferedReader br;
	private BufferedWriter bw;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private JLabel lblUserName;
	
	/**
	 * Create the frame.
	 */
	public JavaChatClientView(String username, String ip_addr, String port_no) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 100);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		txtInput = new JTextField();
		txtInput.setBounds(91, 20, 185, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setBounds(288, 364, 76, 40);
		contentPane.add(btnSend);
		
		lblUserName = new JLabel("Name");
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(12, 20, 67, 40);
		contentPane.add(lblUserName);
		setVisible(true);
	
		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		UserName = username;
		lblUserName.setText(username + ">");
		
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			dos = new DataOutputStream(os);
			SendMessage("/login " + UserName);
			ListenNetwork net = new ListenNetwork();
			net.start();
			Myaction action = new Myaction();
			btnSend.addActionListener(action); // 내부클래스로 액션 리스너를 상속받은 클래스로
			txtInput.addActionListener(action);
			txtInput.requestFocus();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//AppendText("connect error");
		}

	}
	// Server Message를 수신해서 화면에 표시
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					// String msg = dis.readUTF();
					byte[] b = new byte[BUF_LEN];
					int ret;
					ret = dis.read(b);
					if (ret < 0) {
						//AppendText("dis.read() < 0 error");
						try {
							dos.close();
							dis.close();
							socket.close();
							System.exit(0);
							break;
						} catch (Exception ee) {
							System.exit(0);
							break;
						}// catch문 끝
					}
					String	msg = new String(b, "euc-kr");
					msg = msg.trim(); // 앞뒤 blank NULL, \n 모두 제거
					AppendText(msg); // server 화면에 출력
				} catch (IOException e) {
					try {
						dos.close();
						dis.close();
						System.out.println("오류가 나는위치 탐색7");
						socket.close();
						System.exit(0);
						break;
					} catch (Exception ee) {
						System.out.println("오류가 나는위치 탐색6");
						System.exit(0);
						break;
					} // catch문 끝
				} // 바깥 catch문끝
				
			}
		}
	}
	// keyboard enter key 치면 서버로 전송
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				SendMessage(msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				if (msg.contains("/exit")) { // 종료 처리
					try {
						//종료를 위한 수정
						SendMessage("/exit");
						//종료를 위한 수정
						socket.close();
						System.exit(0);
					} catch (IOException e1) {
						e1.printStackTrace();
						System.out.println("오류가 나는위치 탐색5");
					}
					System.exit(0);
				}
			}
		}
	}
	// 화면에 출력
	public void AppendText(String msg) {
		System.out.println(msg + "\n");
	}

	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			System.out.println("오류가 나는위치 탐색1");
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				System.out.println("오류가 나는위치 탐색2");
				e1.printStackTrace();
			}
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public void SendMessage(String msg) {
		try {
			// dos.writeUTF(msg);
			byte[] bb;
			bb = MakePacket(msg);
			dos.write(bb, 0, bb.length);
		} catch (IOException e) {
			//AppendText("dos.write() error");
			try {
				dos.close();
				dis.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("오류가 나는위치 탐색3");
				e1.printStackTrace();
				try {
					socket.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					System.out.println("오류가 나는위치 탐색4");
					e2.printStackTrace();
				}
				System.exit(0);
			}
		}
	}
}
