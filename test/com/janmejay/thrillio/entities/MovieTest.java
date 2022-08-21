package com.janmejay.thrillio.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.janmejay.thrillio.constants.MovieGenre;
import com.janmejay.thrillio.manager.BookmarkManager;

class MovieTest {

	@Test
	void testIsKidFriendly() {
		//test1 for Horror , return falseF
		Movie movie = BookmarkManager.getInstance().createMovie(3000, "Citizen Kane", 1941,
				new String[] { "Orson Welles", "Joseph Cotten" }, new String[] { "Orson Welles" }, MovieGenre.HORROR,
				8.5);
		boolean isKidFriendly = movie.isKidFriendly();
		assertFalse("For Horror movies, isKidFriendly() method should return false", isKidFriendly);
		
		// test2 for Thriller,return false
		movie = BookmarkManager.getInstance().createMovie(3000, "Citizen Kane", 1941,
				new String[] { "Orson Welles", "Joseph Cotten" }, new String[] { "Orson Welles" }, MovieGenre.THRILLERS,
				8.5);
		isKidFriendly = movie.isKidFriendly();
		assertFalse("For thriller movies, isKidFriendly() method should return false", isKidFriendly);
	}

}
