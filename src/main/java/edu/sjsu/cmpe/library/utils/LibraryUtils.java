package edu.sjsu.cmpe.library.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.core.EntityTag;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Authors;
import edu.sjsu.cmpe.library.domain.Reviews;
import edu.sjsu.cmpe.library.dto.AuthorLinksDto;
import edu.sjsu.cmpe.library.dto.LinksDto;
import edu.sjsu.cmpe.library.dto.ReviewLinksDto;


public class LibraryUtils {
	
	private static AtomicInteger atomicAuthorId = new AtomicInteger(100);
	private static AtomicInteger atomicReviewId = new AtomicInteger(1000);
	
	public static Book transformBook(Book book, AuthorLinksDto authorLinkList, 
			ReviewLinksDto reviewLinkList)
	{
		Book copyBook = new Book();
		copyBook.setIsbn(book.getIsbn());
		copyBook.setTitle(book.getTitle());
		copyBook.setPublication_date(book.getPublication_date());
		copyBook.setLanguage(book.getLanguage());
		copyBook.setNum_pages(book.getNum_pages());
		copyBook.setStatus(book.getStatus());
		copyBook.setAuthors(getAuthorsFromDto(authorLinkList));
		if(!book.getReviews().isEmpty())
			copyBook.setReviews(getReviewsFromDto(reviewLinkList));
		return copyBook;
	}
	
	public static List<Authors> getAuthorsFromDto(AuthorLinksDto authorLinkList)
	{
		List<Authors> authorLinks = new ArrayList<Authors>();
		authorLinks.add(authorLinkList);
		return authorLinks;
	}
	
	public static ArrayList<Reviews> getReviewsFromDto(ReviewLinksDto reviewLinkList)
	{
		ArrayList<Reviews> reviewLinks = new ArrayList<Reviews>();
		reviewLinks.add(reviewLinkList);
		return reviewLinks;
	}

	public static long generateAuthorId()
	{
		return(atomicAuthorId.getAndIncrement());
	}
	
	public static long generateReviewId()
	{
		return(atomicReviewId.getAndIncrement());
	}
	
	public static String calculateCheckSum(String s)
	{
		String hash = "";
	try {
		   MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		   messageDigest.update(s.getBytes(),0,s.length());
		   hash = new BigInteger(1,messageDigest.digest()).toString(16);
		  // System.out.println("Hash: "+hash);
		} catch (final NoSuchAlgorithmException e) {
		   e.printStackTrace();
		}
	 return hash;
	}
	
	public static EntityTag createETagForBook(Book book)
	{
		
		StringBuilder s = new StringBuilder(4000);
	
		String checkIsbn = Long.toString(book.getIsbn());
		String checkTitle = book.getTitle();
		String checkDate = book.getPublication_date();
		String checkNum_Pages = Long.toString(book.getNum_pages());
		String checkLanguage = book.getLanguage();
		String checkStatus = book.getStatus();
		s.append(nullToEmpty(checkIsbn)).
		append(nullToEmpty(checkTitle)).
		append(nullToEmpty(checkDate)).
		append(nullToEmpty(checkNum_Pages)).
		append(nullToEmpty(checkLanguage)).
		append(nullToEmpty(checkStatus));
		
		List<Authors> checkAuthors = book.getAuthors();
		for(int i=0; i< checkAuthors.size() ; i++)
			s.append(nullToEmpty(checkAuthors.get(i).getName()));
		ArrayList<Reviews> checkReviews = book.getReviews();
		for(int i=0; i< checkReviews.size() ; i++)
		{
			s.append(nullToEmpty(Long.toString(checkReviews.get(i).getRating())));
			s.append(nullToEmpty(checkReviews.get(i).getComment()));
		}
		EntityTag eTag = new EntityTag(calculateCheckSum(s.toString()));
		//System.out.println("Test etag" + s.toString());
		return eTag;
	}
	
	public static String nullToEmpty(String s)
	{
		return s == null ? "" : s;
	}
}
