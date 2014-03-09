package edu.sjsu.cmpe.library.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.jersey.core.impl.provider.entity.StreamingOutputProvider;
import com.yammer.dropwizard.jersey.params.LongParam;

import edu.sjsu.cmpe.library.domain.Authors;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Reviews;

public class BookRepository implements BookRepositoryInterface {
    /** In-memory map to store books. (Key, Value) -> (ISBN, Book) */
    private final ConcurrentHashMap<Long, Book> bookInMemoryMap;

    /** Never access this key directly; instead use generateISBNKey() */
    private long isbnKey;
    
    private boolean flag;
    
    public BookRepository(ConcurrentHashMap<Long, Book> bookMap) {
	checkNotNull(bookMap, "bookMap must not be null for BookRepository");
	bookInMemoryMap = bookMap;
	isbnKey = 0;
	flag = false;
	}

    /**
     * This should be called if and only if you are adding new books to the
     * repository.
     * 
     * @return a new incremental ISBN number
     */
    private final Long generateISBNKey() {
	// increment existing isbnKey and return the new value
	return Long.valueOf(++isbnKey);
    }
    
    /**
     * This will auto-generate unique ISBN for new books.
     */
    @Override
    public Book saveBook(Book newBook) {
	checkNotNull(newBook, "newBook instance must not be null");
	// Generate new ISBN
	Long isbn = generateISBNKey();
	newBook.setIsbn(isbn);
	for(int i=0 ;i < newBook.getAuthors().size() ; i++)
		newBook.getAuthors().get(i).setId();
	// Finally, save the new book into the map
	bookInMemoryMap.putIfAbsent(isbn, newBook);
	return newBook;
    }
    
    /* (non-Javadoc)
	 * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#removeBook(java.lang.Long)
	 */
	@Override
	public void removeBook(Long isbn) {
		if(!bookInMemoryMap.isEmpty())
			bookInMemoryMap.remove(isbn);
		System.out.println("book with ISBN "+ isbn + " has been removed");
	}

	/**
     * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#getBookByISBN(java.lang.Long)
     */
    @Override
    public Book getBookByISBN(Long isbn) {
	checkArgument(isbn > 0,
		"ISBN was %s but expected greater than zero value", isbn);
	return bookInMemoryMap.get(isbn);
    }

    
	@Override
	public ArrayList<Reviews> addReview(Long isbn, Reviews newReview) {
		checkNotNull(newReview, "newReview instance must not be null");
		Book book = bookInMemoryMap.get(isbn);
		newReview.setId();
		book.getReviews().add(newReview);
		ArrayList<Reviews> savedReview = book.getReviews();
		return savedReview;
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#getReviewById(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Reviews getReviewById(Long isbn, Long reviewId) {
		Book book = bookInMemoryMap.get(isbn);
		ArrayList<Reviews> review = book.getReviews();
		Reviews bookReview = new Reviews();
		for(int i=0 ; i<review.size() ; i++)
		{
			if(review.get(i).getId()==reviewId)
			{
				bookReview = book.getReviews().get(i);
			}
		}
		return bookReview;
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#getAllReviewByIsbn(java.lang.Long)
	 */
	@Override
	public ArrayList<Reviews> getAllReviewByIsbn(Long isbn) {
		Book book = bookInMemoryMap.get(isbn);
		ArrayList<Reviews> reviews = book.getReviews();
		return reviews;
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#getAuthorById(java.lang.Long, java.lang.String)
	 */
	@Override
	public Authors getAuthorById(Long isbn, Long authorId) {
		Book book = bookInMemoryMap.get(isbn);
		Authors author = new Authors();
		for(int i=0 ; i<book.getAuthors().size() ; i++)
		{
			if((authorId).equals(book.getAuthors().get(i).getId()))
			{
				author = book.getAuthors().get(i);
			}
		}
		return author;
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#getAllAuthorsByIsbn(java.lang.Long)
	 */
	@Override
	public List<Authors> getAllAuthorsByIsbn(Long isbn) {
		Book book = bookInMemoryMap.get(isbn);
		List<Authors> authorsList = book.getAuthors();
		return authorsList;
	}
	
}
