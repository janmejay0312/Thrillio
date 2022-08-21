package com.janmejay.thrillio.manager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.janmejay.thrillio.constants.BookGenre;
import com.janmejay.thrillio.constants.KidFriendlyStatus;
import com.janmejay.thrillio.constants.MovieGenre;
import com.janmejay.thrillio.dao.BookmarkDao;
import com.janmejay.thrillio.entities.Book;
import com.janmejay.thrillio.entities.Bookmark;
import com.janmejay.thrillio.entities.Movie;
import com.janmejay.thrillio.entities.User;
import com.janmejay.thrillio.entities.UserBookmark;
import com.janmejay.thrillio.entities.WebLink;
import com.janmejay.thrillio.util.HttpConnect;
import com.janmejay.thrillio.util.IOUtil;

public class BookmarkManager {
	private static BookmarkManager instance = new BookmarkManager();
	private static BookmarkDao dao = new BookmarkDao();

	private BookmarkManager() {
	}

	public static BookmarkManager getInstance() {
		return instance;
	}

	public Movie createMovie(long id, String title, int releaseYear, String[] cast, String[] directors, MovieGenre genre,
			double imdbRatings) {
		Movie movie = new Movie();
		movie.setId(id);
		movie.setTitle(title);
		// movie.setProfileUrl(profileUrl);
		movie.setReleaseYear(releaseYear);
		movie.setCast(cast);
		movie.setDirectors(directors);
		movie.setGenre(genre);
		movie.setImdbRatings(imdbRatings);
		return movie;
	}

	public Book createBook(long id, String title, int publicationYear, String publisher, String[] authors, BookGenre genre,
			double amazonRating) {
		Book book = new Book();
		book.setId(id);
		book.setTitle(title);
		// book.setProfileUrl(profileUrl);
		book.setPublicationYear(publicationYear);
		book.setPublisher(publisher);
		book.setAuthors(authors);
		book.setGenre(genre);
		book.setAmazonRating(amazonRating);
		return book;
	}

	public WebLink createWebLink(long id, String title, String url, String host) {
		WebLink webLink = new WebLink();
		webLink.setId(id);
		webLink.setTitle(title);
		// webLink.setProfileUrl(profileUrl);
		webLink.setUrl(url);
		webLink.setHost(host);

		return webLink;
	}

	public List<List<Bookmark>> getBookmark() {
		return dao.getBookmark();
	}

	public void saveUserBookmark(User user, Bookmark bookmark)  {
		UserBookmark userBookmark = new UserBookmark();
		userBookmark.setUser(user);
		userBookmark.setBookmark(bookmark);
		
		/*if(bookmark instanceof WebLink) {
			try {
				String url=((WebLink)bookmark).getUrl();
				if(!url.endsWith(".pdf")) {
					String webPage=HttpConnect.download(((WebLink)bookmark).getUrl());
					if(webPage!=null) {
						IOUtil.write(webPage,bookmark.getId());
					}
				}	
		}catch(MalformedURLException e) {
			e.printStackTrace();
		}catch(URISyntaxException e) {
			e.printStackTrace();
		}
		}	*/
		dao.saveUserBookmark(userBookmark);
	}

	public void setKidFriendlyStatus(User user, KidFriendlyStatus getKidFriendlyStatus, Bookmark bookmark) {
		bookmark.setKidFriendlyStatus(getKidFriendlyStatus);
		bookmark.setKidFriendlyMarkedBy(user);
		
		updateKidFriendlyStatus(bookmark);
		
		System.out.println("Kid Friendly Status" + ": " + getKidFriendlyStatus.getName() + ": Marked by" + " " + user.getEmail()
				+ " :" + bookmark);

	}

	private void updateKidFriendlyStatus(Bookmark bookmark) {
		int kidFriendlyStatus=bookmark.getKidFriendlyStatus().ordinal();
		long userId=bookmark.getKidFriendlyMarkedBy().getId();
		
		
		String tableToUpdate="Book";
		if(bookmark instanceof Movie)
			tableToUpdate="Movie";
		else
			tableToUpdate="WebLink";
		
		
		try(Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root", "Janmejay@312");
				java.sql.Statement stmt = conn.createStatement();){
		String query="Update "+tableToUpdate+" set kid_friendly_status ="+kidFriendlyStatus+", kid_friendly_marked_by ="+userId +" where id= "+bookmark.getId();
		stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void share(User user, Bookmark bookmark) {
		bookmark.setSharedBy(user);
		System.out.println("Data to be shared:");
		if (bookmark instanceof Book) {
			System.out.println(((Book) bookmark).getItemData());
		} else {
			System.out.println(((WebLink) bookmark).getItemData());
		}
  dao.sharedByInfo(bookmark);
	}
}
