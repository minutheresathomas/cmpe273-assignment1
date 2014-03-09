package edu.sjsu.cmpe.library.domain;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.sjsu.cmpe.library.utils.LibraryUtils;

@JsonInclude(Include.NON_NULL)
public class Authors {
	
	@JsonProperty
	@JsonInclude(Include.NON_DEFAULT)
	private long id;
	
	@NotNull(message = "Name of author must not be null")
	@JsonProperty
	private String name;

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
		this.id = LibraryUtils.generateAuthorId();
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		System.out.println("Author name :" + name);
	}
}
