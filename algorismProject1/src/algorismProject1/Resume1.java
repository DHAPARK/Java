package algorismProject1;

import java.util.Scanner;

public class Resume1 {
	public Resume1() {}
	// 퀵 정렬 알고리즘을 사용하여 정렬.
	public static void main(String[] args) {
		// 입력한 학생 수만큼 점수를 입력하고 성적순으로 정렬하여 출력.
		Scanner sc = new Scanner(System.in);
		System.out.println("학생 수 입력");
		// 학생 수 입력
		int[] stArr = new int[sc.nextInt()];
		
		for (int i = 0; i < stArr.length; i++) {
			System.out.println(String.format("학생 %d 점수 입력:", i+1));
			stArr[i] = sc.nextInt();
		}
		//이부분까지 학생들의 성적 입력 완료.
		
		//성적 순으로 정렬 시작.
		System.out.println("성적 순으로 정렬");
		
		//0번인덱스부터 n(stArr의 크기)번인덱스까지
		new Resume1().qSort(stArr, 0, stArr.length-1);
		
		//등수 출력
		int cnt = stArr.length-1;
		for (int i=0;i<=stArr.length-1;i++) {
			System.out.println(String.format("%d등 : %d",i+1,stArr[cnt]));
			cnt--;
		}
	}
	
	void qSort(int[] stArr, int front, int end) {
		int pivot = front;
		int temp;
		int i;
		int j;
		
		if (front >= end) {return;} // 배열의 크기가 1일땐 정렬할 필요가 없다.즉시 return
		do {
			i = front + 1; 
			j = end;
			// 오른쪽 으로 피벗값보다 큰값 탐색
			while (stArr[i] < stArr[pivot]) {
				i++;
				if (i >= end) { 
					i = pivot;
				}				
			}
			// 왼쪽 으로 피벗값보다 작은 값 탐색
			while (stArr[j] > stArr[pivot]) {
				j--;
				if (j <= front) {
					j = pivot;
				}
			}
			if (i < j) {
				//if 와 else문 
				//left와 rgiht위치가 조건에 맞고 안맞을때 = 자리를 교체/교체하지 않는다.
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
		
		//자리교체가 일어난 경우 새로운 피벗을 사용하여 재정렬한다.
		qSort(stArr, front, j - 1);
		qSort(stArr, j + 1, end);
	}
	
}
