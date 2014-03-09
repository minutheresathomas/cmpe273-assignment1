package edu.sjsu.cmpe.library.api.resources;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.yammer.dropwizard.jersey.params.LongParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.library.domain.Authors;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Reviews;
import edu.sjsu.cmpe.library.dto.AuthorLinksDto;
import edu.sjsu.cmpe.library.dto.AuthorsDto;
import edu.sjsu.cmpe.library.dto.ErrorDto;
import edu.sjsu.cmpe.library.dto.ReviewLinksDto;
import edu.sjsu.cmpe.library.dto.ReviewsDto;
import edu.sjsu.cmpe.library.dto.BookDto;
import edu.sjsu.cmpe.library.dto.LinkDto;
import edu.sjsu.cmpe.library.dto.LinksDto;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;
import edu.sjsu.cmpe.library.utils.LibraryUtils;

@Path("/v1/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    /** bookRepository instance */
    private final BookRepositoryInterface bookRepository;
    private boolean flag;
    /**
     * BookResource constructor
     * 
     * @param bookRepository
     *            a BookRepository instance
     */
    public BookResource(BookRepositoryInterface bookRepository) {
	this.bookRepository = bookRepository; 
	flag = false;
    }
    
    /**
     * 1. Create Book API
	 *		Resource: POST - /books
	 *		Description: Add a new book along with the author information to the library
     * 
     * @param request
     * @return Response
     */
    @POST
    @Timed(name = "create-book")
    public Response createBook(@Valid Book request) {
    	
    	List<ErrorDto> errorList = new ArrayList<ErrorDto>();
    	if(request.getTitle().equalsIgnoreCase(null) || request.getTitle().equalsIgnoreCase(""))
    	{
    		errorList.add(new ErrorDto("LIBRARY-ERROR-1", "Missing Title for book."));
    		return Response.status(400).entity(errorList).build();
    	}
    	if(request.getPublication_date().equalsIgnoreCase(null) || 
    			request.getPublication_date().equalsIgnoreCase(""))
    	{
    		errorList.add(new ErrorDto("LIBRARY-ERROR-2", "Missing Publication-Date for book"));
    		return Response.status(400).entity(errorList).build();
    	}

		// Store the new book in the BookRepository so that we can retrieve it.
		Book savedBook = bookRepository.saveBook(request);
		
		String location = "/books/" + savedBook.getIsbn();
		BookDto bookResponse = new BookDto();
		bookResponse.addLink(new LinkDto("view-book", location, "GET"));
		bookResponse.addLink(new LinkDto("update-book", location, "PUT"));
		bookResponse.addLink(new LinkDto("delete-book", location, "DELETE"));
		bookResponse.addLink(new LinkDto("create-review", location + "/reviews", "POST"));
		
		return Response.status(201).entity(bookResponse).build();
	}   
    
    /**
     * 2. View Book API
	 *		Resource: GET - /books/{isbn}
	 *		Description: View an existing book from the library.
	 *
     * @param isbn
     * @return
     */

    @GET
    @Path("/{isbn}")
    @Timed(name = "view-book")
    //public BookDto getBookByIsbn(@PathParam("isbn") LongParam isbn) {
    public Response getBookByIsbn(@PathParam("isbn") LongParam isbn, 
    		@Context Request request) {
    	
    	//create cache control
    	CacheControl cache = new CacheControl();
    	cache.setMaxAge(172800);
    	//get book by isbn
		Book book = bookRepository.getBookByISBN(isbn.get());
		
		Response.ResponseBuilder responseBuilder = null;
		// create the Etag for the request
		EntityTag etag = LibraryUtils.createETagForBook(book);
		
		//verify with eTag in http request
		responseBuilder = request.evaluatePreconditions(etag);
		
		if(responseBuilder != null)
		{
			return responseBuilder.status(304).cacheControl(cache).tag(etag).build();
		}
		
		AuthorLinksDto authorResponse = new AuthorLinksDto();
		for(int i=0 ; i<book.getAuthors().size() ; i++)
		{
			authorResponse.addLink(new LinkDto("view-author", "/books/" + book.getIsbn()
					+ "/authors/" + book.getAuthors().get(i).getId() , "GET"));
		}
		
		ReviewLinksDto reviewResponse = new ReviewLinksDto();
		if(!book.getReviews().isEmpty())
		{
			for(int i=0 ; i<book.getReviews().size() ; i++)
			{
				reviewResponse.addLink(new LinkDto("view-review", "/books/" + book.getIsbn()
						+ "/reviews/" + book.getReviews().get(i).getId() , "GET"));
			}
		}
		
		BookDto bookResponse = new BookDto(LibraryUtils.transformBook
				(book,authorResponse,reviewResponse));
	
		bookResponse.addLink(new LinkDto("view-book", 
				"/books/" + book.getIsbn(), "GET"));
		bookResponse.addLink(new LinkDto("update-book",
				"/books/" + book.getIsbn(), "PUT"));
		bookResponse.addLink(new LinkDto("delete-book", 
				"/books/" + book.getIsbn(), "DELETE"));
		bookResponse.addLink(new LinkDto("create-review", 
				"/books/" + book.getIsbn() + "/reviews", "POST"));
		if(!book.getReviews().isEmpty())
			bookResponse.addLink(new LinkDto("view-all-reviews", 
					"/books/" + book.getIsbn() + "/reviews", "GET"));
		//prepare the response 
		Response response = Response.status(200).entity(bookResponse).
				cacheControl(cache).tag(etag).build();
		return response;
	}
    
    /**
     * 3. Delete Book API
     * 		Resource: DELETE - /books/{isbn}
     * 		Description: Delete an existing book from the library.
     * 
     * @param isbn
     * @return
     */    
    @DELETE
    @Path("/{isbn}")
    @Timed(name = "delete-book")
    public Response deleteBookByIsbn(@PathParam("isbn") LongParam isbn)
    {
    	try {
	    	bookRepository.removeBook(isbn.get());
	    	LinksDto links = new LinksDto();
	    	links.addLink(new LinkDto("create-book", "/books", "POST"));
	    	return Response.ok(links).build();
    	}
    	catch(WebApplicationException e) {
    		e.printStackTrace();
			return Response.noContent().build();
    	}
    }
    
    /**
     * 4. Update Book API
     * 		Resource: PUT - /books/{isbn}?status={new-status}
     * 		Description: Update an existing book meta-data from the library. 
     * 					 For instance, change the status: from “Available” to “Lost”.
     * 
     * 
     * @param isbn
     * @param status
     * @return
     */
    @PUT
    @Path("/{isbn}")
    @Timed(name = "update-book")
    public Response updateBookByIsbn(@PathParam("isbn") LongParam isbn, 
    		@QueryParam("status") @DefaultValue("available") String status)
    {
    	// get the book with the ISBN and change the status of that particular book
    	Book book = bookRepository.getBookByISBN(isbn.get());
    	book.setStatus(status.toLowerCase().trim());
    	
    	String location = "/books/" + book.getIsbn();
    	BookDto bookResponse = new BookDto();
    	bookResponse.addLink(new LinkDto("view-book", location, "GET"));
    	bookResponse.addLink(new LinkDto("update-book", location, "PUT"));
    	bookResponse.addLink(new LinkDto("delete-book", location, "DELETE"));
    	bookResponse.addLink(new LinkDto("create-review", location + "/reviews", "POST"));
    	
    	return Response.status(200).entity(bookResponse).build();
    }
    
    /**
     * 5. Create Book Review API
     * 		Resource: POST - /books/{isbn}/reviews
     * 		Description: Add a new review to the book. 
     * 					 (Neither updating nor deleting reviews is allowed to minimize the scope)
     * 
     * @param isbn
     * @param request
     * @return
     */    
    @POST
    @Path("/{isbn}/reviews")
    @Timed(name = "create-review")
    public Response createReviewByIsbn(@PathParam("isbn") LongParam isbn, 
    		@Valid Reviews request)
    {
    	List<ErrorDto> errorList = new ArrayList<ErrorDto>();
    	if(request.getRating()==0)
    	{
    		errorList.add(new ErrorDto("LIBRARY-ERROR-3", "Missing rating for review"));
    		return Response.status(400).entity(errorList).build();
    	}
    	if(request.getComment().equalsIgnoreCase(null) || request.getComment().equalsIgnoreCase(""))
    	{
    		errorList.add(new ErrorDto("LIBRARY-ERROR-4", "Missing comment for review"));
    		return Response.status(400).entity(errorList).build();
    	}
    	ArrayList<Reviews> bookReview = 
    			bookRepository.addReview(isbn.get(), request);
    	
    	ReviewsDto reviewLinks = new ReviewsDto();
    	for(int i =0 ; i<bookReview.size() ; i++)
    	{
    		reviewLinks.addLink(new LinkDto("view-review", "/books/" + isbn + 
    			"/reviews/" + bookReview.get(i).getId() , "GET"));
    	}
    	return Response.status(201).entity(reviewLinks).build();
    }
    
    /**
     * 6. View Book Review API
     * 		Resource: GET - /books/{isbn}/reviews/{id}
     * 		Description: View a particular review of the book.
     * 
     * @param isbn
     * @param reviewId
     * @return
     */    
    @GET
    @Path("/{isbn}/reviews/{id}")
    @Timed(name = "view-review")
    public Response viewReviewById(@PathParam("isbn") LongParam isbn, 
    		@PathParam("id") LongParam reviewId)
    {
    	Reviews getBookReview = bookRepository.getReviewById(isbn.get(), reviewId.get());
    	ReviewsDto reviewLink = new ReviewsDto(getBookReview);
    	reviewLink.addLink(new LinkDto("view-review", "/books/" + isbn + 
    			"/reviews/" + getBookReview.getId() , "GET"));
    	return Response.status(200).entity(reviewLink).build();
    }
    
    /**
     * 7. View All Reviews API
     * 		Resource: GET - /books/{isbn}/reviews
     * 		Description: View all reviews of the book.
     * 
     * @param isbn
     * @return
     */    
    @GET
    @Path("/{isbn}/reviews")
    @Timed(name = "view-all-reviews")
    public Response viewAllReviewsByIsbn(@PathParam("isbn") LongParam isbn)
    {
    	ArrayList<Reviews> bookReviews = bookRepository.getAllReviewByIsbn(isbn.get());
    	ReviewsDto reviewLinks = new ReviewsDto(bookReviews);
    	return Response.status(200).entity(reviewLinks).build();
    }
    
    /**
     * 8. View Book Author API
     * 		Resource: GET - /books/{isbn}/authors/{id}
     * 		Description: View a particular author of the book.
     * 
     * @param isbn
     * @param authorId
     * @return
     */
    
    @GET
    @Path("/{isbn}/authors/{id}")
    @Timed(name = "view-author")
    public Response viewAuthorById(@PathParam("isbn") LongParam isbn , 
    		@PathParam("id") LongParam authorId)
    {
    	Authors bookAuthor = bookRepository.getAuthorById(isbn.get(), authorId.get());
    	AuthorsDto authorLink = new AuthorsDto(bookAuthor);
    	authorLink.addLink(new LinkDto("view-author", "/books/" + isbn + 
    			"/authors/" + bookAuthor.getId() , "GET"));
    	System.out.println("author name: "+bookAuthor.getName()+"author id " + bookAuthor.getId());
    	return Response.status(200).entity(authorLink).build();
    }
    
    /**
     * View All Authors of the Book API
     * 		Resource: GET - /books/{isbn}/authors
     * 		Description: View all authors of the book.
     * 
     * @param isbn
     * @return
     */
    
    @GET
    @Path("/{isbn}/authors")
    @Timed(name = "view-all-authors")
    public Response viewAllAuthorsByIsbn(@PathParam("isbn") LongParam isbn)
    {
    	List<Authors> bookAuthors = bookRepository.getAllAuthorsByIsbn(isbn.get());
    	AuthorsDto authorLinks = new AuthorsDto(bookAuthors);
    	return Response.status(200).entity(authorLinks).build();
    }
    
}

