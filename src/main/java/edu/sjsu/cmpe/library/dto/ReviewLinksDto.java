package edu.sjsu.cmpe.library.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.sjsu.cmpe.library.domain.Reviews;

@JsonInclude(Include.NON_EMPTY)
public class ReviewLinksDto extends Reviews{
	
	private List<LinkDto> reviews = new ArrayList<LinkDto>();
	
	public void addLink(LinkDto link) {
		reviews.add(link);
    }

	/**
	 * @return the reviews
	 */
	public List<LinkDto> getReviews() {
		return reviews;
	}

//	/**
//	 * @param reviews the reviews to set
//	 */
//	public void setReviews(List<LinkDto> reviews) {
//		this.reviews = reviews;
//	}

}
