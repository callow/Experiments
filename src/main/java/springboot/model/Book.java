package springboot.model;

import java.util.List;

public class Book {
	
	private String isbn;
	private String title;
	private List<Author> authors;
	private int year;
	private double price;
	private String genre;
	
	
	
	public Book(String isbn, String title, List<Author> authors, int year, double price, String genre) {
		this.isbn = isbn;
		this.title = title;
		this.authors = authors;
		this.year = year;
		this.price = price;
		this.genre = genre;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	
	

}
