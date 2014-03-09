package edu.sjsu.cmpe.library.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.sjsu.cmpe.library.domain.Authors;

@JsonPropertyOrder(alphabetic = true)
public class AuthorLinksDto extends Authors{
	
	private List<LinkDto> authors = new ArrayList<LinkDto>();

    public void addLink(LinkDto link) {
    	authors.add(link);
    }

	/**
	 * @return the authors
	 */
	public List<LinkDto> getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<LinkDto> authors) {
		this.authors = authors;
	}

}
