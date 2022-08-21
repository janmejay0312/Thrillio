package com.janmejay.thrillio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.janmejay.thrillio.constants.KidFriendlyStatus;
import com.janmejay.thrillio.constants.UserType;
import com.janmejay.thrillio.controllers.Controller;
import com.janmejay.thrillio.entities.Bookmark;
import com.janmejay.thrillio.entities.User;
import com.janmejay.thrillio.partner.Sharable;

public class View {
	public static void browse(User user, List<List<Bookmark>> bookmarks) {
		// List<String>list=new ArrayList<>();
		System.out.println("...." + user.getEmail() + "  is Browsing...");
		int bookmarkCount = 0;

		for (List<Bookmark> bookmarkList : bookmarks) {
			for (Bookmark bookmark : bookmarkList) {
				//if (bookmarkCount < DataStore.USER_BOOKMARK_LIMIT) {
					boolean isBookmarked = getBookmarkedDecision(bookmark);
					if (isBookmarked) {
						bookmarkCount++;
						Controller.getInstance().saveUserBookmark(user, bookmark);
						System.out.println("New Item Bookmarked" + "---- " + bookmark);
					}
				//s}

				// User currentUser = new User();
				if (user.getUserType().equals(UserType.EDITOR) || user.getUserType().equals(UserType.CHIEF_EDITOR)) {
					// marking a bookmark KidFriendly
					if (bookmark.isKidFriendly() && bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.UNKNOWN)) {
						KidFriendlyStatus getKidFriendlyStatus = getKidFriendlyStatusDecision();
						if (!getKidFriendlyStatus.equals(KidFriendlyStatus.UNKNOWN)) {
							Controller.getInstance().setKidFriendlyStatus(user, getKidFriendlyStatus, bookmark);
						}
					}
					// Sharing
					if (bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.APPROVED)
							&& bookmark instanceof Sharable) {
						boolean isShared = getBookmarkDecision();
						if (isShared) {
							Controller.getInstance().share(user, bookmark);
						}
					}
				}
			}
		}

	}

	private static boolean getBookmarkDecision() {
		return Math.random() > 0.5 ? true : false;
	}

	private static KidFriendlyStatus getKidFriendlyStatusDecision() {
		double randomVal = Math.random();
		// TODO Auto-generated method stub
		return randomVal < 0.4 ? KidFriendlyStatus.APPROVED
				: (randomVal >= 0.4 && randomVal < 0.8) ? KidFriendlyStatus.REJECTED : KidFriendlyStatus.UNKNOWN;
	}

	private static boolean getBookmarkedDecision(Bookmark bookmark) {
		return Math.random() < 0.5 ? true : false;
	}
}
