package com.hopding.pdflib.apache.pdfbox.multipdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDPage;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.action.PDAction;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;

/**
 * Split a document into several other documents.
 *
 * @author Mario Ivankovits
 * @author Ben Litchfield
 */
public class Splitter
{
    private PDDocument sourceDocument;
    private PDDocument currentDestinationDocument;

    private int splitLength = 1;
    private int startPage = Integer.MIN_VALUE;
    private int endPage = Integer.MAX_VALUE;
    private List<PDDocument> destinationDocuments;

    private int currentPageNumber = 0;

    /**
     * This will take a document and split into several other documents.
     *
     * @param document The document to split.
     *
     * @return A list of all the split documents.
     *
     * @throws IOException If there is an IOError
     */
    public List<PDDocument> split(PDDocument document) throws IOException
    {
        destinationDocuments = new ArrayList<PDDocument>();
        sourceDocument = document;
        processPages();
        return destinationDocuments;
    }

    /**
     * This will tell the splitting algorithm where to split the pages.  The default
     * is 1, so every page will become a new document.  If it was two then each document would
     * contain 2 pages.  If the source document had 5 pages it would split into
     * 3 new documents, 2 documents containing 2 pages and 1 document containing one
     * page.
     *
     * @param split The number of pages each split document should contain.
     */
    public void setSplitAtPage(int split)
    {
        if(split <= 0)
        {
            throw new RuntimeException("Error split must be at least one page.");
        }
        splitLength = split;
    }

    /**
     * This will set the start page.
     *
     * @param start the start page
     */
    public void setStartPage(int start)
    {
        if(start <= 0)
        {
            throw new RuntimeException("Error split must be at least one page.");
        }
        startPage = start;
    }

    /**
     * This will set the end page.
     *
     * @param end the end page
     */
    public void setEndPage(int end)
    {
        if(end <= 0)
        {
            throw new RuntimeException("Error split must be at least one page.");
        }
        endPage = end;
    }

    /**
     * Interface method to handle the start of the page processing.
     *
     * @throws IOException If an IO error occurs.
     */
    private void processPages() throws IOException
    {
        for (int i = 0; i < sourceDocument.getNumberOfPages(); i++)
        {
            PDPage page = sourceDocument.getPage(i);
            if (currentPageNumber + 1 >= startPage && currentPageNumber + 1 <= endPage)
            {
                processPage(page);
                currentPageNumber++;
            }
            else
            {
                if (currentPageNumber > endPage)
                {
                    break;
                }
                else
                {
                    currentPageNumber++;
                }
            }
        }
    }

    /**
     * Helper method for creating new documents at the appropriate pages.
     *
     * @throws IOException If there is an error creating the new document.
     */
    private void createNewDocumentIfNecessary() throws IOException
    {
        if (splitAtPage(currentPageNumber) || currentDestinationDocument == null)
        {
        	currentDestinationDocument = createNewDocument();
        	destinationDocuments.add(currentDestinationDocument);
        }
    }

    /**
     * Check if it is necessary to create a new document.
     * By default a split occurs at every page. If you wanted to split
     * based on some complex logic then you could override this method. For example.
     * <code>
     * protected void splitAtPage()
     * {
     * 		// will split at pages with prime numbers only
     * 		return isPrime(pageNumber);
     * }
     * </code>
     * 
     * @param pageNumber the page number to be checked as splitting page
     *
     * @return true If a new document should be created.
     */
    protected boolean splitAtPage(int pageNumber)
    {
    	return pageNumber % splitLength == 0;
    }

    /**
     * Create a new document to write the split contents to.
     *
     * @return the newly created PDDocument. 
     * @throws IOException If there is an problem creating the new document.
     */
    protected PDDocument createNewDocument() throws IOException
    {
        PDDocument document = new PDDocument();
        document.getDocument().setVersion(getSourceDocument().getVersion());
        document.setDocumentInformation(getSourceDocument().getDocumentInformation());
        document.getDocumentCatalog().setViewerPreferences(
                getSourceDocument().getDocumentCatalog().getViewerPreferences());
        return document;
    }

    /**
     * Interface to start processing a new page.
     *
     * @param page The page that is about to get processed.
     *
     * @throws IOException If there is an error creating the new document.
     */
    protected void processPage(PDPage page) throws IOException
    {
        createNewDocumentIfNecessary();
        
        PDPage imported = getDestinationDocument().importPage(page);
        imported.setCropBox(page.getCropBox());
        imported.setMediaBox(page.getMediaBox());
        // only the resources of the page will be copied
        imported.setResources(page.getResources());
        imported.setRotation(page.getRotation());
        // remove page links to avoid copying not needed resources 
        processAnnotations(imported);
    }
    
    private void processAnnotations(PDPage imported) throws IOException
    {
    	List<PDAnnotation> annotations = imported.getAnnotations();
    	for (PDAnnotation annotation : annotations)
    	{
    		if (annotation instanceof PDAnnotationLink)
    		{
    			PDAnnotationLink link = (PDAnnotationLink)annotation;
    			PDDestination destination = link.getDestination();
    			if (destination == null && link.getAction() != null)
    			{
    				PDAction action = link.getAction();
    				if (action instanceof PDActionGoTo)
    				{
    					destination = ((PDActionGoTo)action).getDestination();
    				}
    			}
    			if (destination instanceof PDPageDestination)
    			{
    				// TODO preserve links to pages within the splitted result
    				((PDPageDestination) destination).setPage(null);
    			}
    		}
    		else
    		{
    			// TODO preserve links to pages within the splitted result
    			annotation.setPage(null);
    		}
    	}
    }

    /**
     * The source PDF document.
     * 
     * @return the pdf to be splitted
     */
    protected final PDDocument getSourceDocument()
    {
        return sourceDocument;
    }

    /**
     * The source PDF document.
     * 
     * @return current destination pdf
     */
    protected final PDDocument getDestinationDocument()
    {
        return currentDestinationDocument;
    }
}
