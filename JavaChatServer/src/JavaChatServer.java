// JavaChatServer.java
// Java Chatting Server
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class JavaChatServer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // ��������
	private Socket client_socket; // accept() ���� ������ client ����
	private Vector UserVec = new Vector(); // ����� ����ڸ� ������ ����
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaChatServer frame = new JavaChatServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JavaChatServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 386);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 244);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(12, 264, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(111, 264, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
				txtPortNumber.setEnabled(false); // ���̻� ��Ʈ��ȣ ������ �ϰ� ���´�
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 300, 300, 35);
		contentPane.add(btnServerStart);
	}

	// ���ο� ������ accept() �ϰ� user thread�� ���� �����Ѵ�.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					AppendText("Waiting clients ...");
					client_socket = socket.accept(); // accept�� �Ͼ�� �������� ���� �����
					AppendText("���ο� ������ from " + client_socket);
					// User �� �ϳ��� Thread ����
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // ���ο� ������ �迭�� �߰�
					AppendText("����� ����. ���� ������ �� " + UserVec.size());
					
					
					
					new_user.start(); // ���� ��ü�� ������ ����
				} catch (IOException e) {
					AppendText("!!!! accept ���� �߻�... !!!!");
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("����ڷκ��� ���� �޼��� : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User �� �����Ǵ� Thread
	// Read One ���� ��� -> Write All
	class UserService extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;
		private Socket client_socket;
		private Vector user_vc;
		private String UserName = "";
		private boolean Status= false;
		
		public boolean isStatus() {
			return Status;
		}
		public void setStatus(boolean status) {
			Status = status;
		}
		public String getUserName() {
			return UserName;
		}

		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// �Ű������� �Ѿ�� �ڷ� ����
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			try {
				is = client_socket.getInputStream();
				dis = new DataInputStream(is);
				os = client_socket.getOutputStream();
				dos = new DataOutputStream(os);
				// line1 = dis.readUTF();
				// /login user1 ==> msg[0] msg[1]
				byte[] b = new byte[BUF_LEN];
				dis.read(b);
				String line1 = new String(b);
				String[] msg = line1.split(" ");
				UserName = msg[1].trim();
				AppendText("���ο� ������ " + UserName + " ����.");
				WriteOne("Welcome to Java chat server\n");
				WriteOne(UserName + "�� ȯ���մϴ�.\n"); // ����� ����ڿ��� ���������� �˸�
				WriteAll(String.format("%s %s",UserName,"�Բ��� �����ϼ̽��ϴ�."));
			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		// ��� User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
		public void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				user.WriteOne(str);
			}
		}

		// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
		public byte[] MakePacket(String msg) {
			byte[] packet = new byte[BUF_LEN];
			byte[] bb = null;
			int i;
			for (i = 0; i < BUF_LEN; i++)
				packet[i] = 0;
			try {
				bb = msg.getBytes("euc-kr");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		// UserService Thread�� ����ϴ� Client ���� 1:1 ����
		public void WriteOne(String msg) {
			try {
				// dos.writeUTF(msg);
				byte[] bb;
				bb = MakePacket(msg);
				dos.write(bb, 0, bb.length);
			} catch (IOException e) {
				AppendText("dos.write() error");
				try {
					WriteAll(String.format("%s %s",UserName,"�Բ��� �����ϼ̽��ϴ�."));
					dos.close();
					dis.close();
					client_socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				UserVec.removeElement(this); // �������� ���� ��ü�� ���Ϳ��� �����
				AppendText("����� ����. ���� ������ �� " + UserVec.size());
			}
		}

		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					// String msg = dis.readUTF();
					byte[] b = new byte[BUF_LEN];
					int ret;
					ret = dis.read(b);
					if (ret < 0) {
						AppendText("dis.read() < 0 error");
						try {
							WriteAll(String.format("%s %s",UserName,"�Բ��� �����ϼ̽��ϴ�.\n"));
							dos.close();
							dis.close();
							client_socket.close();
							UserVec.removeElement(this); // �������� ���� ��ü�� ���Ϳ��� �����
							AppendText("����� ����. ���� ������ �� " + UserVec.size());
							break;
						} catch (Exception ee) {
							break;
						} // catch�� ��
					}
					String msg = new String(b, "euc-kr");
					msg = msg.trim(); // �յ� blank NULL, \n ��� ����
					//�׸ӳ�...list��
					if((msg.split(" ")[1]).toLowerCase().equals("/list")) {
						Iterator<UserService> iter = UserVec.iterator();
						UserService user = null;
						while(iter.hasNext()) {
							user = iter.next();
							WriteOne(String.format("%s %s\n", user.getUserName(),user.isStatus() ? "S":"O"));
						}
						continue;
					}
					//�׸ӳ�...list�ϼ�
					//exit��
					if((msg.split(" ")[1]).toLowerCase().equals("/exit")) {
						WriteAll(String.format("%s %s",UserName,"�Բ��� �����ϼ̽��ϴ�.\n"));
						dos.close();
						dis.close();
						client_socket.close();
						UserVec.removeElement(this); // �������� ���� ��ü�� ���Ϳ��� �����
						AppendText("����� ����. ���� ������ �� " + UserVec.size());
						break;
					}
					//exit�ϼ�
					///to �ӼӸ� ����
					if((msg.split(" ")[1]).toLowerCase().equals("/to")) {
						String targetName = msg.split(" ")[2];
						Iterator<UserService> iter = UserVec.iterator();
						UserService user = null;
						while(iter.hasNext()) {
							user = iter.next();
							if(user.getUserName().equals(targetName) && user.isStatus() == false) {
								user.WriteOne(String.format("[�ӼӸ�] [%s] %s\n",UserName,msg.split(" ")[3]));
								break;
							}
						}
						continue;
					}
					//�ӼӸ� �ϼ�
					//sleep
					if((msg.split(" ")[1]).toLowerCase().equals("/sleep")) {
						String StargetName = msg.substring(msg.indexOf('[')+1,msg.indexOf(']'));
						Iterator<UserService> iter = UserVec.iterator();
						UserService user = null;
						while(iter.hasNext()) {
							user = iter.next();
							if(user.getUserName().equals(StargetName)) {
								user.setStatus(true);
								break;
							}
						}
						continue;
					}
					//sleep�ϼ�
					
					//wakeup
					if((msg.split(" ")[1]).toLowerCase().equals("/wakeup")) {
						String StargetName = msg.substring(msg.indexOf('[')+1,msg.indexOf(']'));
						Iterator<UserService> iter = UserVec.iterator();
						UserService user = null;
						while(iter.hasNext()) {
							user = iter.next();
							if(user.getUserName().equals(StargetName)) {
								user.setStatus(false);
								break;
							}
						}
						continue;
					}
					//wakeup�ϼ�
					
					AppendText(msg); // server ȭ�鿡 ���
					//�������� ���������
					for (int i = 0; i < user_vc.size(); i++) {
						UserService user = (UserService) user_vc.elementAt(i);
						if(user.isStatus() == false) {
							user.WriteOne(msg + "\n");
						}
					}
					//�������� ��������� ��
					
					//WriteAll(msg + "\n"); // Write All
				} catch (IOException e) {
					AppendText("dis.read() error");
					try {
						WriteAll(String.format("%s %s",UserName,"�Բ��� �����ϼ̽��ϴ�.\n"));
						dos.close();
						dis.close();
						client_socket.close();
						UserVec.removeElement(this); // �������� ���� ��ü�� ���Ϳ��� �����
						AppendText("����� ����. ���� ������ �� " + UserVec.size());
						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����
			} // while
		} // run
	}
}