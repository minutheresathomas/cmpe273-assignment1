package edu.sjsu.cmpe.library.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.sjsu.cmpe.library.domain.Reviews;

@JsonPropertyOrder({"reviews", "review", "links"})
public class ReviewsDto extends LinksDto{
	
	@JsonInclude(Include.NON_NULL)
	private List<Reviews> reviews;
	@JsonInclude(Include.NON_NULL)
	private Reviews review;
	
	/**
	 * 
	 */
	public ReviewsDto() {
		super();
	}

	/**
     * @param reviews
     */
	public ReviewsDto(List<Reviews> reviews) {
		super();
		this.reviews = reviews;
	}
	
	/**
	 * @param review
	 */
	public ReviewsDto(Reviews review) {
		super();
		this.review = review;
	}

	/**
	 * @return the review
	 */
	public Reviews getReview() {
		return review;
	}

	/**
	 * @return the reviews
	 */
	public List<Reviews> getReviews() {
		return reviews;
	}

	/**
	 * @param reviews the reviews to set
	 */
	public void setReviews(List<Reviews> reviews) {
		this.reviews = reviews;
	}

}
