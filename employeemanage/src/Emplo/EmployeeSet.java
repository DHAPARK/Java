package Emplo;

import java.util.Scanner;

public class EmployeeSet {
	final int MAX_SIZE = 100;
	//배열의 길이 100
	Employee emp[];
	int indexEmp;
	
	public EmployeeSet() {
		emp = new Employee[MAX_SIZE];
		indexEmp = 0;
	}
	
	//직원추가
	void RegisterEmp(Scanner s) {
		System.out.println("");
		System.out.println(">>> 직원 등록 <<<");
		System.out.println(">> name: ");
		String name = s.next();
		System.out.println("== 부서 목록: 총무(11) 인사(12) 기획(13) 생상(14) 영업(15)");
		System.out.println(">> department: ");
		int dept = s.nextInt(); 
		System.out.println("== 직위 목록: 이사(21) 부장(22) 과장(23) 대리(24) 사원(25)");
		System.out.println(">> position: ");
		int position = s.nextInt();
		
		System.out.println(">> grade: ");
		int grade = s.nextInt();
		
		int id = 10101 + indexEmp;
		
		emp[indexEmp] = new Employee(id,name,dept,position,grade);
		indexEmp++;
//		//배열의 길이
//		int arrLen = emp.length;
//		//어차피 다음칸에 삽입이니 그냥 삽입가능.
//		emp[arrLen] = empl;
		s.nextLine();
	}
	
	//직원 정보 출력 기능
	void SearchAll() {
		//배열 길이
		int arrLen = indexEmp;
		for(int i=0;i<arrLen;i++) {
			System.out.println("=======================");
			System.out.println("id: "+emp[i].getId());
			System.out.println("name: "+emp[i].getName());
			
			if(emp[i].getDept() == 11) {
				System.out.println("department: 총무");
			}else if(emp[i].getDept() == 12) {
				System.out.println("department: 인사");
			}else if(emp[i].getDept() == 13) {
				System.out.println("department: 기획");
			}else if(emp[i].getDept() == 14) {
				System.out.println("department: 생산");
			}else if(emp[i].getDept() == 15) {
				System.out.println("department: 영업");
			}
			
			
			if(emp[i].getPosition() == 21) {
				System.out.println("position: 이사");
			}else if(emp[i].getPosition() == 22) {
				System.out.println("position: 부장");
			}else if(emp[i].getPosition() == 23) {
				System.out.println("position: 과장");
			}else if(emp[i].getPosition() == 24) {
				System.out.println("position: 대리");
			}else if(emp[i].getPosition() == 25) {
				System.out.println("position: 사원");
			}
			
			System.out.println("grage: "+emp[i].getGrade());
			System.out.println("\n\n");
		}
	}
}
