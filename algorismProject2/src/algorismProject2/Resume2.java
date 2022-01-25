package algorismProject2;

import java.util.Scanner;

public class Resume2 {
	// �����˻�Ʈ���� ����ؼ� �˻�
	// ���� ��� (�׸� ����)
	// - ��ǰ ���/ ��ǰ ����/ ��ǰ �˻�/ ��ü ��ǰ ��ȸ / ����
	// - ��ϵ� ��ǰ�� ���� ���¿��� 4 �Է� �� '��ϵ� ��ǰ�� �����ϴ�.' ���

	public static void main(String[] args) {
		BinarySearchTree bt = new BinarySearchTree();
		
		int chNum;
		String pdName = "";
		int pdNum=0;
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("(1) ��ǰ ��� (2) ��ǰ ���� (3) ��ǰ �˻� (4) ��ü ��ǰ ��ȸ (5) ����");
			switch(chNum = sc.nextInt()) {
			case 1:
				pdNum=0;
				pdName="";
				//��ǰ ���
				System.out.println("��ǰ ���");
				System.out.println("��ǰ ��ȣ �Է�");
				pdNum = sc.nextInt();
				System.out.println("��ǰ�� �Է�");
				pdName = sc.next();
				
				if(bt.Search(bt.root, pdNum) == null) {
					bt.root = bt.insert(bt.root, pdNum,pdName);
				}
				
				break;
			case 2:
				//��ǰ ����
				pdNum=0;
				System.out.println("��ǰ ����");
				System.out.println("��ǰ ��ȣ �Է� : ");
				pdNum = sc.nextInt();
				//��������
				bt.delete(bt.root, pdNum);
				
				System.out.println("��ǰ ���� �Ϸ�");
				break;
			case 3:
				//��ǰ �˻�
				pdNum=0;
				System.out.println("��ǰ �˻�");
				System.out.println("��ǰ ��ȣ �Է�:");
				pdNum = sc.nextInt();
				System.out.println("��ǰ�� : ");
				
				if(bt.rSearch(bt.root, pdNum) != null) {
					String pdname = bt.rSearch(bt.root, pdNum).pdName;
					System.out.println(pdname);
				} else {
					System.out.println("Ž�� ����");
				}
				
				break;
			case 4:
				
				BinarySearchTree.Node nd = bt.new Node(0, null);
				for(int i=0;i<=BinarySearchTree.cnt;i++) {
					nd = bt.Search(bt.root, i+1);
					if(nd == null) {
						break;
					}else {
						System.out.println(String.format("%d %s", nd.data,nd.pdName));
					}
				}
				//��ü ��ǰ ��ȸ
				
				break;
			default:
				return;
			}
			
		}
	}
}
