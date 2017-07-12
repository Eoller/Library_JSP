package beans;

import java.awt.Image;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import jdbc.Database;
import enums.SearchType;

public class BookList {

	private ArrayList<Book> bookList = new ArrayList<Book>();

	private ArrayList<Book> getBooks(String sql) {

		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;

		try {
			conn = Database.getConnection();
			stmt = conn.createStatement();

			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Book book = new Book();
				book.setId(rs.getLong("id"));
				book.setName(rs.getString("name"));
				book.setGenre(rs.getString("genre"));
				book.setIsbn(rs.getString("isbn"));
				book.setAuthor(rs.getString("author"));
				book.setPageCount(rs.getInt("page_count"));
				book.setPublishDate(rs.getInt("publish_year"));
				book.setPublisher(rs.getString("publisher"));
				book.setImage(rs.getBytes("image"));
				bookList.add(book);
			}

		} catch (SQLException ex) {
			Logger.getLogger(BookList.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {

				if (rs != null) {
					rs.close();
				}

			} catch (SQLException ex) {
				Logger.getLogger(BookList.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return bookList;
	}

	public ArrayList<Book> getAllBooks() {
		return getBooks("select b.id,b.name,b.isbn,b.page_count,b.publish_year, p.name as publisher, "
				+ "a.fio as author, g.name as genre, b.image from library.book b inner join library.author a on b.author_id=a.id "
				+ "inner join library.genre g on b.genre_id=g.id inner join library.publisher p on b.publisher_id=p.id order by b.name");
	}

	public ArrayList<Book> getBooksByGenre(long id) {
		if (id == 0) {
			return getAllBooks();
		} else {
			return getBooks(
					"select b.id,b.name,b.isbn,b.page_count,b.publish_year, p.name as publisher, a.fio as author, g.name as genre, b.image from library.book b "
							+ "inner join library.author a on b.author_id=a.id " + "inner join library.genre g on b.genre_id=g.id "
							+ "inner join library.publisher p on b.publisher_id=p.id " + "where genre_id=" + id
							+ " order by b.name " + "limit 0,5");
		}
	}

	public ArrayList<Book> getBooksByLetter(String letter) {
		;
		return getBooks(
				"select b.id,b.name,b.isbn,b.page_count,b.publish_year, p.name as publisher, a.fio as author, g.name as genre, b.image from library.book b "
						+ "inner join library.author a on b.author_id=a.id " + "inner join library.genre g on b.genre_id=g.id "
						+ "inner join library.publisher p on b.publisher_id=p.id " + "where substr(b.name,1,1)='" + letter
						+ "' order by b.name " + "limit 0,5");

	}

	public ArrayList<Book> getBooksBySearch(String searchStr, SearchType type) {
		StringBuilder sql = new StringBuilder(
				"select b.id,b.name,b.isbn,b.page_count,b.publish_year, p.name as publisher, a.fio as author, g.name as genre, b.image from library.book b "
						+ "inner join library.author a on b.author_id=a.id " + "inner join library.genre g on b.genre_id=g.id "
						+ "inner join library.publisher p on b.publisher_id=p.id ");

		if (type == SearchType.AUTHOR) {
			sql.append("where lower(a.fio) like '%" + searchStr.toLowerCase() + "%' order by b.name ");

		} else if (type == SearchType.TITLE) {
			sql.append("where lower(b.name) like '%" + searchStr.toLowerCase() + "%' order by b.name ");
		}
		sql.append("limit 0,5");

		return getBooks(sql.toString());

	}
}
