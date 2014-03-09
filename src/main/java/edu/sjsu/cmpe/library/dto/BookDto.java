package edu.sjsu.cmpe.library.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Authors;

@JsonPropertyOrder(alphabetic = true)
public class BookDto extends LinksDto {
    
	@JsonInclude(Include.NON_NULL)
	private Book book;

    /**
	 * 
	 */
	public BookDto() {
		super();
	}

	/**
	 * @param book
	 */
	public BookDto(Book book) {
		super();
		this.book = book;
	}

	/**
	 * @return the book
	 */
	public Book getBook() {
		return book;
	}

}
