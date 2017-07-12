package beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdbc.Database;

public class AuthorList {

	private ArrayList<Author> authorList = new ArrayList<Author>();

	private ArrayList<Author> getAuthors() {
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = Database.getConnection();

			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from library.author order by fio");
			while (rs.next()) {
				Author author = new Author();
				author.setId(rs.getLong("id"));
				author.setName(rs.getString("fio"));
				authorList.add(author);
			}

		} catch (SQLException ex) {
			Logger.getLogger(AuthorList.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException ex) {
				Logger.getLogger(AuthorList.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return authorList;
	}

	public ArrayList<Author> getAuthorList() {
		if (!authorList.isEmpty()) {
			return authorList;
		} else {
			return getAuthors();
		}
	}
}
