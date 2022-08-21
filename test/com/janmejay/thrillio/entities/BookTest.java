package com.janmejay.thrillio.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.janmejay.thrillio.constants.BookGenre;
import com.janmejay.thrillio.manager.BookmarkManager;

class BookTest {

	@Test
	void testIsKidFriendly() {
		// test 1 Philosophy, return false
		Book book = BookmarkManager.getInstance().createBook(4000, "Walden", 1854, "Wilder Publications",
				new String[] { "Henry David Thoreau" }, BookGenre.PHILOSOPHY, 4.3);
		boolean isKidFriendly = book.isKidFriendly();
		assertFalse("for Philosophy, isKidFriendly() should return false", isKidFriendly);
		// test2 self-help book, return false
		book = BookmarkManager.getInstance().createBook(4000, "Walden", 1854, "Wilder Publications",
				new String[] { "Henry David Thoreau" }, BookGenre.SELF_HELP, 4.3);
		isKidFriendly = book.isKidFriendly();
		assertFalse("for Self-Help, isKidFriendly() should return false", isKidFriendly);
	}

}
