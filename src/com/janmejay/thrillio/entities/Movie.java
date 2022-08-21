package com.janmejay.thrillio.entities;

import java.util.Arrays;

import com.janmejay.thrillio.constants.MovieGenre;

public class Movie extends Bookmark {
	private int releaseYear;
	private String[] cast;
	private String[] directors;
	private MovieGenre genre;
	private double imdbRatings;

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String[] getCast() {
		return cast;
	}

	public void setCast(String[] cast) {
		this.cast = cast;
	}

	public String[] getDirectors() {
		return directors;
	}

	public void setDirectors(String[] directors) {
		this.directors = directors;
	}

	public MovieGenre getGenre() {
		return genre;
	}

	public void setGenre(MovieGenre genre) {
		this.genre = genre;
	}

	public double getImdbRatings() {
		return imdbRatings;
	}

	public void setImdbRatings(double imdbRatings) {
		this.imdbRatings = imdbRatings;
	}

	@Override
	public String toString() {
		return "Movie [releaseYear=" + releaseYear + ", cast=" + Arrays.toString(cast) + ", directors="
				+ Arrays.toString(directors) + ", genre=" + genre + ", imdbRatings=" + imdbRatings + "]";
	}

	public boolean isKidFriendly() {
		if (genre.equals(MovieGenre.HORROR) || genre.equals(MovieGenre.THRILLERS))
			return false;
		return true;
	}
}
