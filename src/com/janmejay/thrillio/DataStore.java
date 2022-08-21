package com.janmejay.thrillio;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.janmejay.thrillio.constants.BookGenre;
import com.janmejay.thrillio.constants.Gender;
import com.janmejay.thrillio.constants.MovieGenre;
import com.janmejay.thrillio.constants.UserType;
import com.janmejay.thrillio.entities.Bookmark;
import com.janmejay.thrillio.entities.User;
import com.janmejay.thrillio.entities.UserBookmark;
import com.janmejay.thrillio.manager.BookmarkManager;
import com.janmejay.thrillio.manager.UserManager;
import com.janmejay.thrillio.util.IOUtil;
import com.mysql.cj.xdevapi.Statement;

public class DataStore {
	private static List<User> user = new ArrayList<>();
	private static List<List<Bookmark>> bookmark = new ArrayList<>();
	// private static UserBookmark[] userBookmark = new
	// UserBookmark[TOTAL_USER_COUNT * USER_BOOKMARK_LIMIT];
	public static List<UserBookmark> userBookmark = new ArrayList<>();

	public static void loadData() throws ClassNotFoundException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		//new com.mysql.jdbc.Driver(); 
		            // OR
		//System.setProperty("jdbc.drivers", "com.mysql.cj.jdbc.Driver");
		
		try(Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root", "Janmejay@312");
				java.sql.Statement stmt = conn.createStatement();){
			loadUser(stmt);
			loadWebLinks(stmt);
			loadBooks(stmt);
			loadMovies(stmt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("loaded data....");
	}

	private static void loadMovies(java.sql.Statement stmt) throws SQLException {
		/*
		 * bookmark[2][0] = BookmarkManager.getInstance().createMovie(3000,
		 * "Citizen Kane", 1941, new String[] { "Orson Welles", "Joseph Cotten" }, new
		 * String[] { "Orson Welles" }, MovieGenre.CLASSICS, 8.5); bookmark[2][1] =
		 * BookmarkManager.getInstance().createMovie(3001, "The Grapes of Wrath", 1940,
		 * new String[] { "Henry Fonda", "Jane Darwell" }, new String[] { "John Ford" },
		 * MovieGenre.CLASSICS, 8.2); bookmark[2][2] =
		 * BookmarkManager.getInstance().createMovie(3002, "A Touch of Greatness", 2004,
		 * new String[] { "Albert Cullum" }, new String[] { "Leslie Sullivan" },
		 * MovieGenre.DOCUMENTARIES, 7.3); bookmark[2][3] =
		 * BookmarkManager.getInstance().createMovie(3003, "The Big Bang Theory", 2007,
		 * new String[] { "Kaley Cuoco", "Jim Parsons" }, new String[] { "Chuck Lorre",
		 * "Bill Prady" }, MovieGenre.TV_SHOWS, 8.7); bookmark[2][4] =
		 * BookmarkManager.getInstance().createMovie(3004, "Ikiru", 1952, new String[] {
		 * "Takashi Shimura", "Minoru Chiaki" }, new String[] { "Akira Kurosawa" },
		 * MovieGenre.FOREIGN_MOVIES, 8.4);
		 */
		String query = "Select m.id, title, release_year, GROUP_CONCAT(DISTINCT a.name SEPARATOR ',') AS cast, GROUP_CONCAT(DISTINCT d.name SEPARATOR ',') AS directors, movie_genre_id, imdb_rating"
				+ " from Movie m, Actor a, Movie_Actor ma, Director d, Movie_Director md "
				+ "where m.id = ma.movie_id and ma.actor_id = a.id and "
				      + "m.id = md.movie_id and md.director_id = d.id group by m.id";
		ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);
		
		List<Bookmark> bookmarkList = new ArrayList<>();
		while (rs.next()) {
			long id = rs.getLong("id");
			String title = rs.getString("title");
			int releaseYear = rs.getInt("release_year");
			String[] cast = rs.getString("cast").split(",");
			String[] directors = rs.getString("directors").split(",");			
			int genre_id = rs.getInt("movie_genre_id");
			MovieGenre genre = MovieGenre.values()[genre_id];
			double imdbRating = rs.getDouble("imdb_rating");
			
			Bookmark bookmark = BookmarkManager.getInstance().createMovie(id, title, releaseYear, cast, directors, genre, imdbRating);
    		bookmarkList.add(bookmark); 
		}
		bookmark.add(bookmarkList);
	}

	private static void loadBooks(java.sql.Statement stmt) throws SQLException {
		/*
		 * bookmark[1][0] = BookmarkManager.getInstance().createBook(4000, "Walden",
		 * 1854, "Wilder Publications", new String[] { "Henry David Thoreau" },
		 * BookGenre.PHILOSOPHY, 4.3); bookmark[1][1] =
		 * BookmarkManager.getInstance().createBook(4001,
		 * "Self-Reliance and Other Essays", 1993, "Dover Publications", new String[] {
		 * "Ralph Waldo Emerson" }, BookGenre.PHILOSOPHY, 4.5); bookmark[1][2] =
		 * BookmarkManager.getInstance().createBook(4002, "Light From Many Lamps", 1988,
		 * "Touchstone", new String[] { "Lillian Eichler Watson" },
		 * BookGenre.PHILOSOPHY, 5.0); bookmark[1][3] =
		 * BookmarkManager.getInstance().createBook(4003, "Head First Design Patterns",
		 * 2004, "O'Reilly Media", new String[] { "Eric Freeman", "Bert Bates",
		 * "Kathy Sierra", "Elisabeth Robson" }, BookGenre.TECHNICAL, 4.5);
		 * bookmark[1][4] = BookmarkManager.getInstance().createBook(4004,
		 * "Effective Java Programming Language Guide",
		 * 
		 * 
		 * 2007, "Prentice Hall", new String[] { "Joshua Bloch" }, BookGenre.TECHNICAL,
		 * 4.9);
		 */
		String query="Select b.id, title, publication_year, p.name, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, amazon_rating, created_date from Book b, Publisher p, Author a, Book_Author ba where b.publisher_id = p.id and b.id = ba.book_id and ba.author_id = a.id group by b.id";
		
ResultSet rs =  stmt.executeQuery(query);
		
    	List<Bookmark> bookmarkList = new ArrayList<>();
    	while (rs.next()) {
    		long id = rs.getLong("id");
			String title = rs.getString("title");
			int publicationYear = rs.getInt("publication_year");
			String publisher = rs.getString("name");		
			String[] authors = rs.getString("authors").split(",");			
			int genre_id = rs.getInt("book_genre_id");
			BookGenre genre = BookGenre.values()[genre_id];
			double amazonRating = rs.getDouble("amazon_rating");
			
			Date createdDate = rs.getDate("created_date");
			System.out.println("createdDate: " + createdDate);
			Timestamp timeStamp = rs.getTimestamp(8);
			System.out.println("timeStamp: " + timeStamp);
			System.out.println("localDateTime: " + timeStamp.toLocalDateTime());
			
			System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear + ", publisher: " + publisher + ", authors: " + String.join(", ", authors) + ", genre: " + genre + ", amazonRating: " + amazonRating);
    		
    		Bookmark bookmark = BookmarkManager.getInstance().createBook(id, title, publicationYear, publisher, authors, genre, amazonRating/*, values[7]*/);
    		bookmarkList.add(bookmark); 
    	}
    	bookmark.add(bookmarkList);
	}

	private static void loadWebLinks(java.sql.Statement stmt) throws SQLException {
		/*
		 * bookmark[0][0] = BookmarkManager.getInstance().createWebLink(2000,
		 * "Taming Tiger, Part 2",
		 * "http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",
		 * "http://www.javaworld.com"); bookmark[0][1] =
		 * BookmarkManager.getInstance().createWebLink(2001,
		 * "How do I import a pre-existing Java project into Eclipse and get up and running?"
		 * ,
		 * "http://stackoverflow.com/questions/142863/how-do-i-import-a-pre-existing-java-project-into-eclipse-and-get-up-and-running",
		 * "http://www.stackoverflow.com"); bookmark[0][2] =
		 * BookmarkManager.getInstance().createWebLink(2002,
		 * "Interface vs Abstract Class",
		 * "http://mindprod.com/jgloss/interfacevsabstract.html",
		 * "http://mindprod.com"); bookmark[0][3] =
		 * BookmarkManager.getInstance().createWebLink(2003,
		 * "NIO tutorial by Greg Travis",
		 * "http://cs.brown.edu/courses/cs161/papers/j-nio-ltr.pdf",
		 * "http://cs.brown.edu"); bookmark[0][4] =
		 * BookmarkManager.getInstance().createWebLink(2004,
		 * "Virtual Hosting and Tomcat",
		 * "http://tomcat.apache.org/tomcat-6.0-doc/virtual-hosting-howto.html",
		 * "http://tomcat.apache.org");
		 */

		List<String> data = new ArrayList<>();
		
		
		List<Bookmark> bookmarkList = new ArrayList<>();
		String query="select * from weblink";
	ResultSet rs	= stmt.executeQuery(query);
	
		// int rowNum = 0;
		while (rs.next()) {
			long id=rs.getLong("id");
     String title=rs.getString("title");
     String host=rs.getString("host");
     String url=rs.getString("url");
			Bookmark bookmarks = BookmarkManager.getInstance().createWebLink(id, title,
					url, host);
			bookmarkList.add(bookmarks);
		}
		bookmark.add(new ArrayList<>(bookmarkList));
	}

	private static void loadUser(java.sql.Statement stmt) throws SQLException {
		/*
		 * user[0] = UserManager.getInstance().createUser(1000,
		 * "user0@semanticsquare.com", "test", "John", "M", UserType.USER, Gender.MALE);
		 * user[1] = UserManager.getInstance().createUser(1001,
		 * "user1@semanticsquare.com", "test", "Sam", "M", UserType.USER, Gender.MALE);
		 * user[2] = UserManager.getInstance().createUser(1002,
		 * "user2@semanticsquare.com", "test", "Anita", "M", UserType.USER,
		 * Gender.MALE); user[3] = UserManager.getInstance().createUser(1003,
		 * "user3@semanticsquare.com", "test", "Sara", "M", UserType.EDITOR,
		 * Gender.FEMALE); user[4] = UserManager.getInstance().createUser(1004,
		 * "user4@semanticsquare.com", "test", "Dheeru", "M", UserType.CHIEF_EDITOR,
		 * Gender.FEMALE);
		 */

		// String[] data = new String[TOTAL_USER_COUNT];
		List<String> data = new ArrayList<>();
		String query="Select * from User";
		ResultSet rs=stmt.executeQuery(query);
		// int rowNum = 0;
		while (rs.next()) {
			long id=rs.getLong("id");
			String email=rs.getString("email");
			String password=rs.getString("password");
			String firstName=rs.getString("first_name");
			String lastName=rs.getString("last_name");
			int userTypeId=rs.getInt("user_type_id");
			UserType userType=UserType.values()[userTypeId];
			int genderId=rs.getInt("gender_id");
			Gender gender=Gender.values()[genderId];
			User users = UserManager.getInstance().createUser(id, email, password,firstName, lastName, userType, gender);
			user.add(users);
		}
	}

	public static List<User> getUser() {
		return user;
	}

	public static List<List<Bookmark>> getBookmark() {
		return bookmark;
	}

	public static void add(UserBookmark userBookmark2) {
		userBookmark.add(userBookmark2);

	}
}
