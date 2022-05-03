package Emplo;

public class Employee {
	private int id;
	private String name;
	// 부서
	private int dept;
	// 직급
	private int position;
	// 호봉
	private int grade;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDept() {
		return dept;
	}
	public void setDept(int dept) {
		this.dept = dept;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public Employee(int id, String name, int dept, int position, int grade) {
		super();
		this.id = id;
		this.name = name;
		this.dept = dept;
		this.position = position;
		this.grade = grade;
	}
}