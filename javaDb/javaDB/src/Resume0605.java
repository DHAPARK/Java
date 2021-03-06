
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Student {
	private int rank;

	private String SID;
	private String name;
	private int kor;
	private int math;
	private int eng;
	private int social;
	private int sience;
	private int tot;
	private int avg;

	public Student(String SID, String name, int kor, int math, int eng, int social, int sience) {
		this.SID = SID;
		this.name = name;
		this.kor = kor;
		this.math = math;
		this.eng = eng;
		this.social = social;
		this.sience = sience;

		this.tot = kor + math + eng + social + sience;
		this.avg = this.tot / 5;
	}

	public String getSID() {
		return SID;
	}

	public void setSID(String sID) {
		SID = sID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getKor() {
		return kor;
	}

	public void setKor(int kor) {
		this.kor = kor;
	}

	public int getMath() {
		return math;
	}

	public void setMath(int math) {
		this.math = math;
	}

	public int getEng() {
		return eng;
	}

	public void setEng(int eng) {
		this.eng = eng;
	}

	public int getSocial() {
		return social;
	}

	public void setSocial(int social) {
		this.social = social;
	}

	public int getSience() {
		return sience;
	}

	public void setSience(int sience) {
		this.sience = sience;
	}

	public int getTot() {
		return tot;
	}

	public void setTot(int tot) {
		this.tot = tot;
	}

	public int getAvg() {
		return avg;
	}

	public void setAvg(int avg) {
		this.avg = avg;
	}

}

public class Resume0605 {

	List<Student> stuList = new ArrayList<Student>();
	int stuNrList[] = null;

	Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb?serverTimezone=UTC",
					"root", "1234");
			System.out.println("DB ?????? ??????");
			return conn;
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC ???????????? ?????? ??????");
			return null;
		} catch (SQLException e) {
			System.out.println("DB ?????? ??????");
			return null;
		}
	}

	void getRankEachOther() {
		this.stuNrList = new int[this.stuList.size()];
		int cnt = 0;

		for (Student st : this.stuList) {
			stuNrList[cnt] = st.getAvg();
			cnt++;
		}
		Arrays.sort(stuNrList);

		// ???????????????
		for (int i = 0; i < stuNrList.length; i++) {
			for (Student stu : this.stuList) {
				if (stuNrList[i] == stu.getAvg()) {
					stu.setRank((this.stuList.size()-i));
				}
			}
		}
		
	}

	public static void main(String[] args) {
		Resume0605 dbconn = new Resume0605();
		Connection conn = dbconn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String query = "select * from gradetbl";

		try {
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery(query);

			while (rs.next()) {
				Student stu = new Student(rs.getString("SID"), rs.getString("name"), rs.getInt("sub1"),
						rs.getInt("sub2"), rs.getInt("sub3"), rs.getInt("sub4"), rs.getInt("sub5"));
				dbconn.stuList.add(stu);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbconn.getRankEachOther();

		// ??? ???????????? ????????? ????????? ???????????????.
		// ??????, ?????? ?????????, ??????, ??????, ?????? , ??????, ??????,??????, ??????, ?????? ??? ???????????? ???????????? ??????.
		for (Student stu : dbconn.stuList) {
			System.out.println(String.format("?????? : %s", stu.getRank()));
			System.out.println(String.format("????????? : %s", stu.getSID()));
			System.out.println(String.format("?????? : %s", stu.getName()));
			System.out.println(String.format("?????? : %d", stu.getKor()));
			System.out.println(String.format("?????? : %d", stu.getMath()));
			System.out.println(String.format("?????? : %s", stu.getEng()));
			System.out.println(String.format("?????? : %s", stu.getSocial()));
			System.out.println(String.format("?????? : %s", stu.getSience()));
			System.out.println(String.format("?????? : %s", stu.getTot()));
			System.out.println(String.format("?????? : %s", stu.getAvg()));
			System.out.println("\n\n\n");
		}
	}
}
