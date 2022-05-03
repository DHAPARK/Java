package Emplo;

import java.util.Scanner;

public class EmployeeTest {
	//직원 3명을 등록한 후 출력하여 동작 여부를 확인
	public static void main(String[] args) {
		EmployeeSet set = new EmployeeSet();
		Scanner scanner = new Scanner(System.in);
		set.RegisterEmp(scanner);
		set.RegisterEmp(scanner);
		set.RegisterEmp(scanner);
		set.SearchAll();
		
		scanner.close();
		
	}
}
