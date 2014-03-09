package edu.sjsu.cmpe.library.repository;

import java.util.ArrayList;
import java.util.List;

import com.yammer.dropwizard.jersey.params.LongParam;

import edu.sjsu.cmpe.library.domain.Authors;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Reviews;

/**
 * Book repository interface.
 * 
 * What is repository pattern?
 * 
 * @see http://martinfowler.com/eaaCatalog/repository.html
 */
public interface BookRepositoryInterface {
	/**
	 * Save a new book in the repository
	 * 
	 * @param newBook
	 *            a book instance to be create in the repository
	 * @return a newly created book instance with auto-generated ISBN
	 */
	Book saveBook(Book newBook);

	/**
	 * Retrieve an existing book by ISBN
	 * 
	 * @param isbn
	 *            a valid ISBN
	 * @return a book instance
	 */
	Book getBookByISBN(Long isbn);

	/**
	 * remove an existing book in the repository
	 * 
	 * @param isbn
	 *            a valid ISBN
	 */
	void removeBook(Long isbn);

	/**
	 * Create a new review for a book in the repository
	 * 
	 * @param isbn
	 *            a valid ISBN
	 * @param review
	 * 		 	  a review instance to be created in the repository
	 * @return a newly created review instance with auto-generated ID
	 */
	ArrayList<Reviews> addReview(Long isbn, Reviews review);
	
	/**
	 * Retrieve an existing review for a book in the repository
	 * 
	 * @param isbn
	 *            a valid ISBN
	 * @param reviewId
	 * 		 	  a reviewId for the existing review in the repository
	 * @return the existing review instance
	 */
	Reviews getReviewById(Long isbn, Long reviewId);
	
	/**
	 * Retrieve all existing reviews for a book in the repository
	 * 
	 * @param isbn
	 *            a valid ISBN
	 * @return the existing review instance
	 */
	
	ArrayList<Reviews> getAllReviewByIsbn(Long isbn);
	
	/**
	 * Retrieve an existing author for a book in the repository
	 * 
	 * @param isbn
	 * 			  a valid ISBN
	 * @param authorId
	 * 			  a authorId for the existing author in the repository
	 * @return the existing author instance
	 */
	Authors getAuthorById(Long isbn, Long authorId);
	
	/**
	 * Retrieve all existing authors for a book in the repository
	 * 
	 * @param isbn
	 * 			  a valid ISBN
	 * @return the existing author instance
	 */
	List<Authors> getAllAuthorsByIsbn(Long isbn);
}
