package ssa;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


public class JdbcDemonstration {
	
	static String dburl = "jdbc:mysql://localhost:3306/tiy?autoReconnect=true&useSSL=false";
	public static String userName = "root";
	public static String password = "admin";
	static Connection myConn = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	
	public static void main(String[] args) throws SQLException {
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("JdbcProjectLecture.properties"));
			dburl = prop.getProperty("dsiurl");
			userName = prop.getProperty("dsiuser");
			password = prop.getProperty("dsipass");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// fetch data from the database
		insertData();
		fetchData();
		updateData();
		fetchData();
		deleteData();
		fetchData();
	}
	public static void close(Connection con, Statement stmt, ResultSet rs) throws SQLException {
		if(con != null) {
			con.close();
			con = null;
		}
		if(stmt != null) {
			stmt.close();
			stmt = null;
		}
		if(rs != null) {
			rs.close();
			rs = null;
		}
	}
	public static void deleteData() throws SQLException {
		try {
			myConn = DriverManager.getConnection(dburl, userName, password);
			stmt = myConn.createStatement();
			int rowsAffected = stmt.executeUpdate("DELETE from student where id between 196 and 203");
			if(rowsAffected == 0) {
				throw new SQLException("No rows were deleted!");
			}
			System.out.println("Delete Successful!");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(myConn, stmt, rs);
		}	
	}
	public static void insertData() throws SQLException {
		try {
			myConn = DriverManager.getConnection(dburl, userName, password);
			stmt = myConn.createStatement();
			int rowsAffected = stmt.executeUpdate("INSERT student (first_name, last_name, sat, gpa, major_id) VALUES ('George','Washington',1600, 4.0, null)");
			if(rowsAffected != 1) {
				throw new SQLException("No rows were inserted!");
			}
			System.out.println("Insert Successful!");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(myConn, stmt, rs);
		}	
	}
	public static void updateData() throws SQLException {
		try {
			myConn = DriverManager.getConnection(dburl, userName, password);
			stmt = myConn.createStatement();
			int rowsAffected = stmt.executeUpdate("UPDATE student set gpa = 4.0 where id = 170");
			if(rowsAffected != 1) {
				throw new SQLException("No rows were updated");
			}
			System.out.println("Update Successful!");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(myConn, stmt, rs);
		}
		
	}
	public static void fetchData() throws SQLException {
		try {
			myConn = DriverManager.getConnection(dburl, userName, password);
			stmt = myConn.createStatement();
			rs = stmt.executeQuery("SELECT * from student");
			String msg = String.format("%-3s, %-30s, %-4s, %-5s", "ID", "NAME", "SAT", "GPA");
			System.out.println(msg);
			msg = String.format("%-3s, %-30s, %-4s, %-5s", "===", "==============================", "====", "=====");
			System.out.println(msg);
			while(rs.next()) {
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				int sat = rs.getInt("sat");
				double gpa = rs.getDouble("gpa");
				
				msg = String.format("%3d, %-31s %4d %5.1f", id, firstName+" "+lastName, sat, gpa);
				System.out.println(msg);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(myConn, stmt, rs);
		}	
	}
}