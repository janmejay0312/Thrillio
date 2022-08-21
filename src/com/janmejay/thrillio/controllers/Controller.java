package com.janmejay.thrillio.controllers;

import com.janmejay.thrillio.constants.KidFriendlyStatus;
import com.janmejay.thrillio.entities.Bookmark;
import com.janmejay.thrillio.entities.User;
import com.janmejay.thrillio.manager.BookmarkManager;

public class Controller {
	private static Controller instance = new Controller();

	private Controller() {
	}

	public static Controller getInstance() {
		return instance;
	}

	public void saveUserBookmark(User user, Bookmark bookmark) {
		BookmarkManager.getInstance().saveUserBookmark(user, bookmark);
	}

	public void setKidFriendlyStatus(User user, KidFriendlyStatus getKidFriendlyStatus, Bookmark bookmark) {
		BookmarkManager.getInstance().setKidFriendlyStatus(user, getKidFriendlyStatus, bookmark);

	}

	public void share(User user, Bookmark bookmark) {
		BookmarkManager.getInstance().share(user, bookmark);

	}
}
