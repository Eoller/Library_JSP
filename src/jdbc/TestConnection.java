package jdbc;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class TestConnection {

	public void check(){
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("jdbc/Library");
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from library.author");
			while (rs.next()) {
				System.out.println(rs.getString("fio"));
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException ex){
			ex.printStackTrace();
		}
	}
}
