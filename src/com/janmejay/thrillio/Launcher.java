package com.janmejay.thrillio;

import java.util.List;

import com.janmejay.thrillio.bgjobs.WebpageDownloaderTask;
import com.janmejay.thrillio.entities.Bookmark;
import com.janmejay.thrillio.entities.User;
import com.janmejay.thrillio.manager.BookmarkManager;
import com.janmejay.thrillio.manager.UserManager;

public class Launcher {
	private static List<User> user;
	private static List<List<Bookmark>> bookmark;

	public static void main(String[] args) {
		loadData();
		start();
		//runDownloaderJob();
	}

	private static void runDownloaderJob() {
		WebpageDownloaderTask task = new WebpageDownloaderTask(true);
		(new Thread(task)).start();

	}

	private static void start() {
		// System.out.println("Browsing.....");
		for (User u : user) {
			View.browse(u, bookmark);
		}

	}

	private static void loadData() {
		System.out.println("loading Data....");

		try {
			DataStore.loadData();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user = UserManager.getInstance().getUser();
		bookmark = BookmarkManager.getInstance().getBookmark();
		 System.out.println("printing Data....");
		 printUserData();
		 printBookmarkData();
	}

	
	  private static void printBookmarkData() { 
		  for(List<Bookmark> b1:bookmark) {
	  for(Bookmark b2:b1) { System.out.println(b2); } }
	  
	  }
	  
	  private static void printUserData() { for(User user1:user) {
	 System.out.println(user1); }
	 
	 }
	 

}
