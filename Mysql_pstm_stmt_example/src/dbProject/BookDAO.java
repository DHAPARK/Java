package dbProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookDAO {
	static Connection conn = null;
	public static Connection getConnection() throws ClassNotFoundException,SQLException{
		String url = "jdbc:mysql://127.0.0.1:3306/csemall?useUnicode=true&serverTimezone=Asia/Seoul";
		String user = "jspbook";
		String pwd = "12345";
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(url,user,pwd);
		return conn;
	}
	
	
	public void insertBook(BookDTO bookDTO) throws ClassNotFoundException,SQLException{
		conn = getConnection();
		// book 테이블에 데이터 저장
		ResultSet rs = null;
		String sql = "insert into book(bookNo,bookTitle,bookAuthor,bookYear,bookPrice,bookPublisher) values (?,?,?,?,?,?)";
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, bookDTO.getBookNo());
			pstm.setString(2, bookDTO.getBookTitle());
			pstm.setString(3, bookDTO.getBookAuthor());
			pstm.setInt(4, bookDTO.getBookYear());
			pstm.setInt(5,bookDTO.getBookPrice());
			pstm.setString(6, bookDTO.getBookPublisher());
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	public ArrayList<BookDTO> selectBook() throws ClassNotFoundException,SQLException{
		// book 테이블에 있는 모든 데이터 출력
		conn = getConnection();
		ResultSet rs = null;
		
		String sql = "select * from book";
		ArrayList<BookDTO> btlist = new ArrayList<BookDTO>();
		try {
			Statement stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				BookDTO bt = new BookDTO();
				bt.setBookNo(rs.getString(1));
				bt.setBookTitle(rs.getString(2));
				bt.setBookAuthor(rs.getString(3));
				bt.setBookYear(rs.getInt(4));
				bt.setBookPrice(rs.getInt(5));
				bt.setBookPublisher(rs.getString(6));
				btlist.add(bt);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return btlist;
	}
}
