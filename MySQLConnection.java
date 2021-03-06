import java.io.File;
import java.sql.*;

public class MySQLConnection {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://koko.c8cejdutv9zr.us-east-2.rds.amazonaws.com/Books";
	static final String USER = "team";
	static final String PASS = "cs157a";

	public static void main(String[] args) throws SQLException {
		Connection conn = connect(DB_URL, USER, PASS);
		Statement stmt = conn.createStatement();
		ResultSet rs = null;
		
		 //RUN THESE 3 below lines ONCE ONCE ONCE to import data into tables
		 //BooksTables books = new BooksTables(stmt, conn);
		 //books.createTable(stmt, conn); 
		 //autoFill(stmt);
		 
//		/**
//		 * Select all authors from the authors table. Order the information
//		 * alphabetically by the author’s last name and first name.
//		 */
//		 String q1 = "select * from authors ORDER BY firstName ASC, lastName
//		 ASC";
//		 rs = stmt.executeQuery(q1);
//		 System.out.println("Author ID " +" " + "Name");
//		 while (rs.next())
//		 {
//		 int authorID = rs.getInt(1);
//		 String lastname = rs.getString(2);
//		 String firstname = rs.getString(3);
//		 System.out.printf("%-10s %-15s \n",authorID, lastname +","+
//		 firstname);
//		 }
//		/**
//		 * Select all publishers from the publishers table.
//		 */
//		 String q2 = " select * from publishers ";
//		 System.out.println("PublisherID" +" "+ "Publisher Name");
//		 rs = stmt.executeQuery(q2);
//		 while(rs.next()){
//		 System.out.printf("%-10s %-20s \n", rs.getInt(1), rs.getString(2));
//		
//		 }
//		/**
//		 * Select a specific publisher and list all books published by that
//		 * publisher. Include the title, year and ISBN number. Order the
//		 * information alphabetically by title.
//		 */
//		 int id = 4;
//		 String q3 = "select * from titles where publisherID = "+ id + " ORDER
//		 BY title ASC";
//		 rs = stmt.executeQuery(q3);
//		 System.out.println("Select a specific publisherID :"+ id);
//		 while (rs.next()){
//		 if(rs.getInt(5) == id){
//		 System.out.println(rs.getString(2)+", "+ rs.getString(4)+",
//		 "+rs.getString(1));
//		 }
//		 }
//		/**
//		 * Add new author
//		 */
//		String q4 = "insert into authors values (18, 'Mone', 'Roe') ";
//		stmt.execute(q4);
//		String q44 = "Select * from authors where authorID = 18";
//		rs = stmt.executeQuery(q44);
//		System.out.println("AuthorID " + "  " + "Name");
//		while (rs.next()) {
//			int authorID = rs.getInt(1);
//			String lastname = rs.getString(2);
//			String firstname = rs.getString(3);
//			System.out.printf("%-10s %-15s \n", authorID, lastname + "," + firstname);
//		}
//
//		/**
//		 * Edit/Update the existing information about an author
//		 */
//		String q5 = "update authors set authorID = 13, firstname = 'Angel', lastname = 'Pho' where authorID = 25";
//		stmt.execute(q5);
//		String q55 = "Select * from authors where authorID = 13";
//		rs = stmt.executeQuery(q55);
//		System.out.println("AuthorID " + "  " + "Name");
//		while (rs.next()) {
//			int authorID = rs.getInt(1);
//			String lastname = rs.getString(2);
//			String firstname = rs.getString(3);
//			System.out.printf("%-10s %-15s \n", authorID, lastname + "," + firstname);
//		}
//		
		/**
		 * Add a new title for an author 
		 */
		String trigger = " DELIMITER $$ "
						 + " CREATE TRIGGER trigger_helpers "
						 + " AFTER INSERT ON titles "
						 + " FOR EACH ROW BEGIN "
						 + " INSERT INTO authors (authorID, firstname, lastname) "
						 + " VALUES (last_insert_id(), 'MIN','HO'); "
						 + " INSERT INTO authorISBN (isbn, authorID) "
						 + " VALUES ('ISBN99909',(SELECT authorID FROM authors WHERE authorID = last_insert_id())); "
						 + " END$$ "
						 + " DELIMITER ; " ;
		String q6 = "insert into titles values ('ISBN99909','The Conjuring',1,2008,5,8.99) ";
		stmt.execute(q6);
		String q66 = " select * from titles ";
		rs = stmt.executeQuery(q66);
		while (rs.next()) {
		System.out.printf("%-10s %-15s \n", rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getFloat(6));
		}
		
//		/**
//		 * Add new publisher 
//		 */
//		String q7 = "insert into publishers values (17, 'April Karin') ";//or(null,'hhhh')
//		stmt.execute(q7);
//		String q77 = " select * from publishers ";
//		rs = stmt.executeQuery(q77);
//		while(rs.next()){
//			System.out.printf("%-10s %-15s \n", rs.getInt(1),rs.getString(2));
//		}
//		
//		/**
//		 * Edit/Update the existing information about a publisher 
//		 */
//		String q8 = "update publishers set publisherID = 20, publisherName = 'Angel' where publisherID = 32 ";
//		stmt.execute(q8);
//		String q88 = " select * from publishers ";
//		rs = stmt.executeQuery(q88);
//		while(rs.next()){
//			System.out.printf("%-10s %-15s \n", rs.getInt(1),rs.getString(2));
//		}
	}//end main


	/**
	 * connect to database by DriverManager
	 * 
	 * @param DB_URL
	 * @param USER
	 * @param PASS
	 * @return
	 */
	public static Connection connect(String DB_URL, String USER, String PASS) {
		Connection con = null;
		try {
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			if (con != null) {
				System.out.println("Established");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}// end connect

	/**
	 * auto fill data into file from external file MUST PLACE Files into dir:
	 * "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/filename.csv"
	 * 
	 * @param stmt
	 */
	public static void autoFill(Statement stmt) {
		String author = "LOAD DATA LOCAL INFILE '" + "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/author.csv"
				+ "' INTO TABLE Books.authors FIELDS TERMINATED BY ','"
				+ " LINES TERMINATED BY '\r\n' ignore 1 rows(authorID, firstname, lastname) ";
		String publishers = "LOAD DATA LOCAL INFILE '" + "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/publisher.csv"
				+ "' INTO TABLE Books.publishers FIELDS TERMINATED BY ','"
				+ " LINES TERMINATED BY '\r\n' ignore 1 rows (publisherID, publisherName) ";
		String authorISBN = "LOAD DATA LOCAL INFILE '" + "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/authorISBN.csv"
				+ "' INTO TABLE Books.authorISBN FIELDS TERMINATED BY ','"
				+ " LINES TERMINATED BY '\r\n' ignore 1 rows (isbn, authorID) ";
		String titles = "LOAD DATA LOCAL INFILE '" + "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/title.csv"
				+ "' INTO TABLE Books.titles FIELDS TERMINATED BY ','"
				+ " LINES TERMINATED BY '\r\n' ignore 1 rows (isbn,title, editionNumber, year, publisherID, price) ";
		try {
			stmt.executeUpdate(author);
			stmt.executeUpdate(publishers);
			stmt.executeUpdate(authorISBN);
			stmt.executeUpdate(titles);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
