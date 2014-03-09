package edu.sjsu.cmpe.library.domain;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@JsonPropertyOrder({"isbn", "title", "publication-date", "language", "num_pages", "status", "reviews", "authors"})
public class Book {
	
	@JsonProperty
    private long isbn;
	
	@NotNull
	@JsonProperty
    private String title;
	
	@NotNull
	@JsonProperty("publication-date")
    private String publication_date;
	
	@JsonProperty
    private String language;
	
	@JsonProperty("num-pages")
    private long num_pages;
	
	private enum Status
	{
		available,checked_out,in_queue,lost;
	}
	@JsonProperty
    @DefaultValue("available")
    private Status status;
	
	@Valid
	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("authors")
    private List<Authors> authors;
	
	@Valid
	@JsonProperty("reviews")
	//@JsonInclude(Include.NON_EMPTY)
    private ArrayList<Reviews> reviews = new ArrayList<Reviews>();	
    
	public Book()
	{
		setStatus("available");
	}
    /**
	 * @return the authors
	 */
	public List<Authors> getAuthors() {
		return authors;
	}
	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<Authors> authors) {
		this.authors = authors;
	}
	/**
	 * @return the reviews
	 */
	public ArrayList<Reviews> getReviews() {
		return reviews;
	}
	/**
	 * @param reviews the reviews to set
	 */
	public void setReviews(ArrayList<Reviews> reviews) {
		this.reviews = reviews;
	}
	/**
	 * @return the isbn
	 */
	public long getIsbn() {
		return isbn;
	}
	/**
	 * @param isbn the isbn to set
	 */
	public void setIsbn(long isbn) {
		this.isbn = isbn;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the publication_date
	 */
	public String getPublication_date() {
		return publication_date;
	}
	/**
	 * @param publication_date the publication_date to set
	 */
	public void setPublication_date(String publication_date) {
		this.publication_date = publication_date;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * @return the num_pages
	 */
	public long getNum_pages() {
		return num_pages;
	}
	/**
	 * @param num_pages the num_pages to set
	 */
	public void setNum_pages(long num_pages) {
		this.num_pages = num_pages;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status.toString();
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = Status.valueOf(status);
	}
	
    
}
