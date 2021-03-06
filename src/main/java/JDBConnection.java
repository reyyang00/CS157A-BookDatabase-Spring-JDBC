import java.sql.*;
import java.sql.ResultSet;
import java.util.Scanner;


public class JDBConnection {


    //JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://koko.c8cejdutv9zr.us-east-2.rds.amazonaws.com/codeman";

    static final String USER = "ruiyang00";
    static final String PASS = "yyx71618";


    public static void main(String[] args) throws SQLException {
        Connection con = null;
        Statement stmt = null;

        try {

            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = con.createStatement();

            BooksTables books = new BooksTables(stmt, con);
            books.populateTables(stmt, con);
            //  autoFill(stmt);


           //  selectQueryOnAuthor(stmt);

           //  selectQueryOnPublihers(stmt);

              selectBooksFromSpecificPublisher(stmt);

           // addNewAuthorQuery(stmt);

           // addNewPublisherQuery(stmt);

          //   addNewTitleForAuthor(stmt, con);

           // selectAllTitles(stmt);

           //  updatePublisherInfo(stmt);

             // updateAuthorInfo(stmt);


//            stmt.executeUpdate("CREATE TRIGGER trg_test \n " +
//
//                    "  AFTER UPDATE ON authors \n" +
//
//                    "  FOR EACH ROW \n" +
//
//                    "  WHEN (new.avalue IS NULL)\n" +
//
//                    "  BEGIN\n" +
//
//                    "    SELECT seq_test.nextval INTO :new.avalue FROM dual;\n" +
//
//                    "  END");


            if (con != null) {
                System.out.println("Established");

            }
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {

            if (con != null) {
                try {
                    con.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    stmt.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void autoFill(Statement stmt) {
        String author = "LOAD DATA LOCAL INFILE '" + "/Users/ruiyang/Documents/SJSU/Fall2018/CS 157A/projectJDBC/CS157A-BookDatabase-Spring-JDBC/src/main/resources/author.csv"
                + "' INTO TABLE codeman.authors FIELDS TERMINATED BY ','"
                + " LINES TERMINATED BY '\r\n' ignore 1 rows(authorID, firstname, lastname) ";

        String publishers = "LOAD DATA LOCAL INFILE '" + "/Users/ruiyang/Documents/SJSU/Fall2018/CS 157A/projectJDBC/CS157A-BookDatabase-Spring-JDBC/src/main/resources/publisher.csv"
                + "' INTO TABLE codeman.publishers FIELDS TERMINATED BY ','"
                + " LINES TERMINATED BY '\r\n' ignore 1 rows (publisherID, publisherName) ";

        String authorISBN = "LOAD DATA LOCAL INFILE '" + "/Users/ruiyang/Documents/SJSU/Fall2018/CS 157A/projectJDBC/CS157A-BookDatabase-Spring-JDBC/src/main/resources/authorISBN.csv"
                + "' INTO TABLE codeman.authorISBN FIELDS TERMINATED BY ','"
                + " LINES TERMINATED BY '\r\n' ignore 1 rows (isbn, authorID) ";
        String titles = "LOAD DATA LOCAL INFILE '" + "/Users/ruiyang/Documents/SJSU/Fall2018/CS 157A/projectJDBC/CS157A-BookDatabase-Spring-JDBC/src/main/resources/title.csv"
                + "' INTO TABLE codeman.titles FIELDS TERMINATED BY ','"
                + " LINES TERMINATED BY '\r\n' ignore 1 rows (isbn,title, editionNumber, year, publisherID, price) ";
        try {
            //stmt.executeUpdate(author);
            //stmt.executeUpdate(publishers);
            stmt.executeUpdate(authorISBN);
            // stmt.executeUpdate(titles);
            System.out.println("Entires populated");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //1) Select all authors from the authors table. Order the information alphabetically by the author’s last name and first name.
    //   works perfect, bug free
    public static void selectQueryOnAuthor(Statement stmt) {

        try {
            String sql = "SELECT * FROM authors ORDER BY lastname ASC, firstname ASC";
            ResultSet rs = stmt.executeQuery(sql);

            // this means that we find all the authors rs.next()
            while (rs.next()) {
                int authorID = rs.getInt("authorID");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");

                System.out.print("AuthorID: " + authorID);
                System.out.print(", Lastname: " + lastname);
                System.out.print(", Firstname: " + firstname);

                System.out.println();
            }
            rs.close();
        } catch (SQLException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------
    //2) Select all publishers from the publishers table.
    //   works perfect, bug free
    public static void selectQueryOnPublihers(Statement stmt) {

        try {
            String sql = "SELECT * FROM publishers";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int publisherID = rs.getInt("publisherID");
                String publisherName = rs.getString("publisherName");

                System.out.print("PublisherID: " + publisherID);
                System.out.print(", Publiher Name: " + publisherName);

                System.out.println();
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //-------------------------------------------------------------------------------------------------------------------------
    //3) Select a specific publisher and list all books published by that publisher.
    //   Include the title, year and ISBN number. Order the information alphabetically by title.
    //   works perfect, bug free
    public static void selectBooksFromSpecificPublisher(Statement stmt) {


        try {

            //select all the books that matches the publisher = Pan Macmillan
            int id = 0;

            
            String get = "SELECT * FROM publishers WHERE publisherName = 'Pan Macmillan' ";
            ResultSet rs1 = stmt.executeQuery(get);
            while (rs1.next()) {
                id = rs1.getInt("publisherID");
            }

            String sql = "SELECT * FROM titles WHERE publisherID = " + id + " ORDER BY title ASC ";
            ResultSet rs = stmt.executeQuery(sql);
            // if book exits then print them all
            while (rs.next()) {
                int publisherID = rs.getInt("publisherID");
                int editionNum = rs.getInt("editionNumber");
                String year = rs.getString("year");
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                long price = rs.getLong("price");


                System.out.print("ISBN: " + isbn);
                System.out.print(", Title: " + title);
             //   System.out.print(", Edition Number: " + editionNum);
                System.out.print(", Year: " + year);
             //   System.out.print(", PublisherID: " + publisherID);
            //    System.out.print(", Price: " + price);
                System.out.println();
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //-------------------------------------------------------------------------------------------------------------------------
    //4) Add new author
    //   works perfect, bug free
    public static void addNewAuthorQuery(Statement stmt) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter First Name: ");
        String firstName = sc.nextLine();
        System.out.println("Enter Last Name: ");
        String lastName = sc.nextLine();
        try {
            String sql = "SELECT * FROM authors WHERE firstname = '" + firstName + "'" + " AND lastname =  '" + lastName + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                System.out.println("Failure! Author already existed");
            } else {
                sql = "INSERT INTO authors(firstname, lastname) VALUE ('" + firstName + "'" + ", '" + lastName + "'" + ")";
                stmt.executeUpdate(sql);
            }
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------
    //5) Add new publisher
    //   works perfect, bug free
    public static void addNewPublisherQuery(Statement stmt) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Publisher's  Name: ");
        String pbname = sc.nextLine();
        try {

            String sql = "SELECT * FROM publishers WHERE publisherName = '" + pbname + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                System.out.println("Failure! publisher already existed");
            } else {
                sql = "INSERT INTO publishers(publisherName) VALUE ('" + pbname + "'" + ")";
                stmt.executeUpdate(sql);
            }

        } catch (SQLException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------
    // 6)Edit/Update the existing information about a publisher
    //   works perfect, bug free
    public static void updatePublisherInfo(Statement stmt) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Publisher's  Name You want to update: ");
        String pbname = sc.nextLine();
        try {

            String sql = "SELECT * FROM publishers WHERE publisherName = '" + pbname + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                System.out.println("Enter the New publisher's Name You want to update: ");
                String newPBname = sc.nextLine();
                sql = " UPDATE publishers " + " SET publisherName = '" + newPBname + "'" + " WHERE publisherName = '" + pbname + "'";
                stmt.executeUpdate(sql);
            } else {
                System.out.println("Failure! publisher '" + pbname + "' does not existed");
            }

        } catch (SQLException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //-------------------------------------------------------------------------------------------------------------------------
    // 7)Edit/Update the existing information about an author

    public static void updateAuthorInfo(Statement stmt) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Author's First name you want to update: ");
        String oldfirstName = sc.next();
        System.out.println("Enter Author's Last name you want to update: ");
        String oldlastName = sc.next();
        try {

            String sql = "SELECT * FROM authors WHERE firstname = '" + oldfirstName + "'" + " AND lastname =  '" + oldlastName + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                System.out.println("Enter the New author's first name You want to update: ");
                String newFN = sc.next();
                System.out.println("Enter the New author's last name You want to update: ");
                String newLN = sc.next();
                sql = " UPDATE authors " + " SET firstname = '" + newFN + "' WHERE firstname = '" + oldfirstName + "' AND " + " lastname ='" + oldlastName + "'";

                String sql2 = " UPDATE authors " + " SET lastname = '" + newLN + "' WHERE firstname = '" + oldfirstName + "' AND " + " lastname ='" + oldlastName + "'";

                stmt.executeUpdate(sql);
                stmt.executeUpdate(sql2);
            } else {
                System.out.println("Failure! author does not  exist");
            }

        } catch (SQLException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------
    // 8) add a new title for an author
    //   works perfect, bug free
    public static void addNewTitleForAuthor(Statement stmt, Connection con) {

        try {

            String sql = "set foreign_key_checks = 0";
            stmt.execute(sql);

            // below is the SQL code for calling  add_new_title_to_ahtuor() procedure
            //"CALL `codeman`.`add_new_title_to_author`('ISBN000113', 'Hello World', 1, '1990', 'HouseTagerreal', 99.99, 'Roy', 'Oh');";
            // CallableStatement is only used for store procedure call.
            CallableStatement cStmt = con.prepareCall("{call add_new_title_to_author(?, ?, ?, ?, ?, ?, ? ,?)}");


            //author info
            cStmt.setString(7, "Roy");
            cStmt.setString(8, "Oh");

            //authorISBN info
            cStmt.setString(1, "ISBN000124");

            //publisher info
            cStmt.setString(5, "Random baba");

            //title info
            cStmt.setString(2, "New Start");
            cStmt.setInt(3, 2);
            cStmt.setString(4, "1990");
            cStmt.setDouble(6, 10.99);


            cStmt.execute();
            cStmt.close();
            stmt.close();
            System.out.println("Stored procedure called successfully!");
//          -----------------------------------------------------------------


            // below are the sql codes that I write for Stored Procedure
//          -----------------------------------------------------------------

//            DELIMITER $$
//            CREATE DEFINER=`ruiyang00`@`%` PROCEDURE `add_new_title_to_author`(in new_isbn char(10),
//                    in new_title varchar(500),
//                    in new_editionNumber int,
//            in new_year  char(4),
//                    in new_publisherName char(100),
//                    in new_price decimal(8,2),
//            in authorFirstName varchar(20),
//                    in authorLastName varchar(20))
//            BEGIN


            //1) check if author is in our author tabl

//            IF NOT EXISTS( SELECT authorID FROM authors WHERE firstname = authorFirstName AND lastname = authorLastName)
//            THEN
//            INSERT INTO authors(firstname , lastname) VALUE(authorFirstName, authorLastName);
//            SET @someVaribale = (SELECT authorID FROM authors WHERE firstname = authorFirstName AND lastname = authorLastName);
//            INSERT INTO authorISB(isbn, authorID) VALUE(new_isbn, @someVaribale);
//

            // we have a existing author in out author table
            // connect the author to the new title through authorISBN
//            ELSE
//            SET @someVaribale = (SELECT authorID FROM authors WHERE firstname = authorFirstName AND lastname = authorLastName);
//            INSERT INTO authorISBN(isbn, authorID) VALUE(new_isbn, @someVaribale);
//            END IF;
//
//
//             2). check publishers table

//            IF NOT EXISTS( SELECT publisherID FROM publishers WHERE publisherName = new_publisherName)
//            THEN
//            INSERT INTO publishers(publisherName) VALUE(new_publisherName);
//            SET @localPublisheID = (SELECT publisherID FROM publishers WHERE publisherName = new_publisherName);
//            INSERT INTO titles (isbn, title, editionNumber, year, publisherID, price) VALUE(new_isbn, new_title, new_editionNumber, new_year,  @localPublisheID,  new_price);
//            ELSE

            //3) insert
//            SET @localPublisheID = (SELECT publisherID FROM publishers WHERE publisherName = new_publisherName);
//            INSERT INTO titles (isbn, title, editionNumber, year, publisherID, price) VALUE(new_isbn, new_title, new_editionNumber, new_year,  @localPublisheID,  new_price);
//            END IF;
//            END$$
//                    DELIMITER ;
//          -----------------------------------------------------------------
            // stored procedure  
        } catch (SQLException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

     //-------------------------------------------------------------------------------------------------------------------------
     // 9) add a new title for an author
     //   works perfect, bug free

    public static void selectAllTitles(Statement stmt){
        try {

            String sql = "SELECT * FROM titles";
            ResultSet rs = stmt.executeQuery(sql);
            // if book exits then print them all
            while (rs.next()) {
                int publisherID = rs.getInt("publisherID");
                int editionNum = rs.getInt("editionNumber");
                String year = rs.getString("year");
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                long price = rs.getLong("price");


                System.out.print("ISBN: " + isbn);
                System.out.print(", Title: " + title);
                System.out.print(", Edition Number: " + editionNum);
                System.out.print(", Year: " + year);
                System.out.print(", PublisherID: " + publisherID);
                System.out.print(", Price: " + price);
                System.out.println();
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

