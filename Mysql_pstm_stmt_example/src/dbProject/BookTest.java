package dbProject;

import java.sql.SQLException;
import java.util.ArrayList;

public class BookTest {
	public static void main(String[] args) throws ClassNotFoundException,SQLException{
		BookDTO bt = new BookDTO();
		BookDAO bdao = new BookDAO();
		bt.setBookNo("B004");
		bt.setBookTitle("자바스크립트");
		bt.setBookAuthor("강길동");
		bt.setBookYear(2020);
		bt.setBookPrice(28000);
		bt.setBookPublisher("서울출판사");
		bdao.insertBook(bt);
		
		
		ArrayList<BookDTO> abd = new ArrayList<>();
		abd = bdao.selectBook();
		for(BookDTO bd : abd) {
			System.out.print(bd.getBookNo()+" ");
			System.out.print(bd.getBookTitle()+" ");
			System.out.print(bd.getBookAuthor()+" ");
			System.out.print(bd.getBookYear()+" ");
			System.out.print(bd.getBookPrice()+" ");
			System.out.print(bd.getBookPublisher()+" ");
			System.out.println();
		}
	}
}
