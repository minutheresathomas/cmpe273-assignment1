package edu.sjsu.cmpe.library.domain;

import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.sjsu.cmpe.library.utils.LibraryUtils;

//@JsonInclude(Include.NON_EMPTY)
public class Reviews {
	@JsonInclude(Include.NON_DEFAULT)
	private long id;
	
	@Min(1)
	@Max(5)
	@NotNull(message = "Rating '${validatedValue}' must be between 0 and 5")
	@JsonInclude(Include.NON_DEFAULT)
	private long rating;
	
	@NotNull(message = "comment must not be null")
	private String comment;
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId() {
		//this.id = UUID.randomUUID().toString();
		this.id = LibraryUtils.generateReviewId();
	}
	
	/**
	 * @return the rating
	 */
	public long getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(long rating) {
		this.rating = rating;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}
