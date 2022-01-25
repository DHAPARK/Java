package algorismProject1;

import java.util.Scanner;

public class Resume1 {
	public Resume1() {}
	// �� ���� �˰����� ����Ͽ� ����.
	public static void main(String[] args) {
		// �Է��� �л� ����ŭ ������ �Է��ϰ� ���������� �����Ͽ� ���.
		Scanner sc = new Scanner(System.in);
		System.out.println("�л� �� �Է�");
		// �л� �� �Է�
		int[] stArr = new int[sc.nextInt()];
		
		for (int i = 0; i < stArr.length; i++) {
			System.out.println(String.format("�л� %d ���� �Է�:", i+1));
			stArr[i] = sc.nextInt();
		}
		//�̺κб��� �л����� ���� �Է� �Ϸ�.
		
		//���� ������ ���� ����.
		System.out.println("���� ������ ����");
		
		//0���ε������� n(stArr�� ũ��)���ε�������
		new Resume1().qSort(stArr, 0, stArr.length-1);
		
		//��� ���
		int cnt = stArr.length-1;
		for (int i=0;i<=stArr.length-1;i++) {
			System.out.println(String.format("%d�� : %d",i+1,stArr[cnt]));
			cnt--;
		}
	}
	
	void qSort(int[] stArr, int front, int end) {
		int pivot = front;
		int temp;
		int i;
		int j;
		
		if (front >= end) {return;} // �迭�� ũ�Ⱑ 1�϶� ������ �ʿ䰡 ����.��� return
		do {
			i = front + 1; 
			j = end;
			// ������ ���� �ǹ������� ū�� Ž��
			while (stArr[i] < stArr[pivot]) {
				i++;
				if (i >= end) { 
					i = pivot;
				}				
			}
			// ���� ���� �ǹ������� ���� �� Ž��
			while (stArr[j] > stArr[pivot]) {
				j--;
				if (j <= front) {
					j = pivot;
				}
			}
			if (i < j) {
				//if �� else�� 
				//left�� rgiht��ġ�� ���ǿ� �°� �ȸ����� = �ڸ��� ��ü/��ü���� �ʴ´�.
				temp = stArr[j];
				stArr[j] = stArr[i];
				stArr[i] = temp;
			} else {
				if (j != pivot) {
					temp = stArr[j];
					stArr[j] = stArr[pivot];
					stArr[pivot] = temp;
				}
			}
		} while (i < j);
		
		//�ڸ���ü�� �Ͼ ��� ���ο� �ǹ��� ����Ͽ� �������Ѵ�.
		qSort(stArr, front, j - 1);
		qSort(stArr, j + 1, end);
	}
	
}
