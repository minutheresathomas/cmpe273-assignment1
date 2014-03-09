package edu.sjsu.cmpe.library.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.sjsu.cmpe.library.domain.Authors;
import edu.sjsu.cmpe.library.domain.Reviews;

@JsonPropertyOrder(alphabetic = true)
public class AuthorsDto extends LinksDto {
	
	@JsonInclude(Include.NON_NULL)
	private List<Authors> authors;
	@JsonInclude(Include.NON_NULL)
	private Authors author;
	
	
	/**
	 * 
	 */
	public AuthorsDto() {
		super();
	}


	/**
	 * @param authors
	 */
	public AuthorsDto(List<Authors> authors) {
		super();
		this.authors = authors;
	}


	/**
	 * @param author
	 */
	public AuthorsDto(Authors author) {
		super();
		this.author = author;
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
	 * @return the author
	 */
	public Authors getAuthor() {
		return author;
	}


	/**
	 * @param author the author to set
	 */
	public void setAuthor(Authors author) {
		this.author = author;
	}
	
	

}
