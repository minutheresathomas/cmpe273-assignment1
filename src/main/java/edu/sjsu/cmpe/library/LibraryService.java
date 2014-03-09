package edu.sjsu.cmpe.library;

import java.util.concurrent.ConcurrentHashMap;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.core.ResourceConfig;
import edu.sjsu.cmpe.library.api.resources.BookResource;
import edu.sjsu.cmpe.library.api.resources.RootResource;
import edu.sjsu.cmpe.library.config.LibraryServiceConfiguration;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.repository.BookRepository;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;

public class LibraryService extends Service<LibraryServiceConfiguration> {

    public static void main(String[] args) throws Exception {
	new LibraryService().run(args);
    }

    @Override
    public void initialize(Bootstrap<LibraryServiceConfiguration> bootstrap) {
	bootstrap.setName("library-service");
    }

    @Override
    public void run(LibraryServiceConfiguration configuration,
	    Environment environment) throws Exception {
        environment.setJerseyProperty(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, LoggingFilter.class.getName());
    	environment.setJerseyProperty(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, LoggingFilter.class.getName());

	/** Root API */
	environment.addResource(RootResource.class);
	/** Books APIs */
	BookRepositoryInterface bookRepository = new BookRepository(
		new ConcurrentHashMap<Long, Book>());
	environment.addResource(new BookResource(bookRepository));
	/** Review APIs */
	/** Add new resources here */
    }
}
