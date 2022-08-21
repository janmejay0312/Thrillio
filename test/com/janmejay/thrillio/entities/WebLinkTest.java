package com.janmejay.thrillio.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.janmejay.thrillio.manager.BookmarkManager;

class WebLinkTest {

	@Test
	void testIsKidFriendly() {
		//test 1 for porn in url: return false
		WebLink webLink=BookmarkManager.getInstance().createWebLink(2000, "Taming Tiger, Part 2",
				"http://www.javaworld.com/article/2072759/core-java/taming-Porn--part-2.html",
				"http://www.javaworld.com");
		boolean isKidFriendly=webLink.isKidFriendly();
		assertFalse("for porn in Url isKidFriendly() method should return false",isKidFriendly);
		//test2 for porn in title: return false
		webLink=BookmarkManager.getInstance().createWebLink(2000, "Taming Tiger, Porn 2",
				"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",
				"http://www.javaworld.com");
		 isKidFriendly=webLink.isKidFriendly();
		assertFalse("for porn in title, isKidFriendly() method should return false",isKidFriendly);
		//test3 for adult in host: return false
		webLink=BookmarkManager.getInstance().createWebLink(2000, "Taming Tiger, Part 2",
				"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",
				"http://www.adult.com");
		 isKidFriendly=webLink.isKidFriendly();
		assertFalse("for porn in host, isKidFriendly() method should return false",isKidFriendly);
		//test 4, adult in url but not in host:return true
		webLink=BookmarkManager.getInstance().createWebLink(2000, "Taming Tiger, Part 2",
				"http://www.javaworld.com/article/2072759/core-java/taming-adult--part-2.html",
				"http://www.javaworld.com");
		 isKidFriendly=webLink.isKidFriendly();
		assertTrue("for adult in url but not in host, isKidFriendly() method should return true",isKidFriendly);
		//test 5, adult in title: return true
		webLink=BookmarkManager.getInstance().createWebLink(2000, "Taming adult, Part 2",
				"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",
				"http://www.javaworld.com");
		 isKidFriendly=webLink.isKidFriendly();
		assertTrue("for adult in title, isKidFriendly() method should return true",isKidFriendly);
	}

}
