package com.janmejay.thrillio.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.janmejay.thrillio.DataStore;
import com.janmejay.thrillio.entities.Book;
import com.janmejay.thrillio.entities.Bookmark;
import com.janmejay.thrillio.entities.Movie;
import com.janmejay.thrillio.entities.UserBookmark;
import com.janmejay.thrillio.entities.WebLink;

public class BookmarkDao {
	public List<List<Bookmark>> getBookmark() {
		return DataStore.getBookmark();
	}

	public void saveUserBookmark(UserBookmark userBookmark) {
		//DataStore.add(userBookmark);
		
		try(Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root", "Janmejay@312");
				java.sql.Statement stmt = conn.createStatement();){
		if(userBookmark.getBookmark() instanceof Book) {
			saveUserBook(userBookmark,stmt);
		}else if(userBookmark.getBookmark() instanceof Movie) {
			saveUserMovie(userBookmark,stmt);
		}else
			saveUserWeblink(userBookmark,stmt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void saveUserWeblink(UserBookmark userBookmark, Statement stmt) throws SQLException {
		String query="insert into User_WebLink(user_id, weblink_id)" +
                "values("+ userBookmark.getUser().getId()+" , "+userBookmark.getBookmark().getId()+");";
	stmt.executeUpdate(query);
		
	}

	private void saveUserMovie(UserBookmark userBookmark, Statement stmt) throws SQLException {
		String query="insert into User_Movie(user_id, movie_id)" +
                "values("+ userBookmark.getUser().getId()+" , "+userBookmark.getBookmark().getId()+");";
	stmt.executeUpdate(query);
		
	}

	private void saveUserBook(UserBookmark userBookmark, Statement stmt) throws SQLException {
		String query="insert into User_Book(user_id, book_id)" +
	                  "values("+ userBookmark.getUser().getId()+" , "+userBookmark.getBookmark().getId()+");";
		stmt.executeUpdate(query);
		
	}

	public List<WebLink> getAllWebLinks() {
		List<WebLink> result = new ArrayList<>();
		List<List<Bookmark>> bookmarks = DataStore.getBookmark();
		List<Bookmark> allWebLinks = bookmarks.get(0);
		for (Bookmark bookmark : allWebLinks) {
			result.add((WebLink) bookmark);
		}
		return result;
	}

	public List<WebLink> getWebLinks(WebLink.DownloadStatus downloadStatus) {
		List<WebLink> result = new ArrayList<>();
		List<WebLink> allWebLinks = getAllWebLinks();
		for (WebLink webLink : allWebLinks) {
			if (webLink.getDownloadStatus().equals(downloadStatus))
				result.add(webLink);

		}
		return result;
	}

	public void sharedByInfo(Bookmark bookmark) {
		long userId=bookmark.getSharedBy().getId();
		
		String tableToUpdate="Book";
		if(bookmark instanceof WebLink)
			tableToUpdate="WebLink";
		
		try(Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root", "Janmejay@312");
				java.sql.Statement stmt = conn.createStatement();){
		String query="Update "+tableToUpdate+" set shared_by ="+userId+ " where id= "+bookmark.getId();
		stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
