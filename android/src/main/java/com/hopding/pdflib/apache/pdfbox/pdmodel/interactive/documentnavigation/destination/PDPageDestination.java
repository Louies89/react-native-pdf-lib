package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.destination;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDPage;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDPageTree;

/**
 * This represents a destination to a page, see subclasses for specific parameters.
 *
 * @author Ben Litchfield
 */
public abstract class PDPageDestination extends PDDestination
{
    /**
     * Storage for the page destination.
     */
    protected COSArray array;

    /**
     * Constructor to create empty page destination.
     *
     */
    protected PDPageDestination()
    {
        array = new COSArray();
    }

    /**
     * Constructor to create empty page destination.
     *
     * @param arr A page destination array.
     */
    protected PDPageDestination( COSArray arr )
    {
        array = arr;
    }

    /**
     * This will get the page for this destination.  A page destination
     * can either reference a page or a page number(when doing a remote destination to
     * another PDF).  If this object is referencing by page number then this method will
     * return null and getPageNumber should be used.
     *
     * @return The page for this destination.
     */
    public PDPage getPage()
    {
        PDPage retval = null;
        if( array.size() > 0 )
        {
            COSBase page = array.getObject( 0 );
            if( page instanceof COSDictionary )
            {
                retval = new PDPage( (COSDictionary)page );
            }
        }
        return retval;
    }

    /**
     * Set the page for this destination.
     *
     * @param page The page for the destination.
     */
    public void setPage( PDPage page )
    {
        array.set( 0, page );
    }

    /**
     * This will get the page number for this destination.  A page destination
     * can either reference a page or a page number(when doing a remote destination to
     * another PDF).  If this object is referencing by page number then this method will
     * return that number, otherwise -1 will be returned.
     *
     * @return The page number for this destination.
     */
    public int getPageNumber()
    {
        int retval = -1;
        if( array.size() > 0 )
        {
            COSBase page = array.getObject( 0 );
            if( page instanceof COSNumber )
            {
                retval = ((COSNumber)page).intValue();
            }
        }
        return retval;
    }

    /**
     * Returns the page number for this destination, regardless of whether
     * this is a page number or a reference to a page.
     *
     * @since Apache PDFBox 1.0.0
     * @see com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem
     * @return page number, or -1 if the destination type is unknown
     */
    public int findPageNumber()
    {
        int retval = -1;
        if( array.size() > 0 )
        {
            COSBase page = array.getObject( 0 );
            if( page instanceof COSNumber )
            {
                retval = ((COSNumber)page).intValue();
            }
            else if (page instanceof COSDictionary)
            {
                COSBase parent = page;
                while (((COSDictionary) parent).getDictionaryObject(COSName.PARENT, COSName.P) != null)
                {
                    parent = ((COSDictionary) parent).getDictionaryObject(COSName.PARENT, COSName.P);
                }
                // now parent is the pages node
                PDPageTree pages = new PDPageTree((COSDictionary) parent);
                retval = pages.indexOf(new PDPage((COSDictionary) page)) + 1;
            }
        }
        return retval;
    }

    /**
     * Set the page number for this destination.
     *
     * @param pageNumber The page for the destination.
     */
    public void setPageNumber( int pageNumber )
    {
        array.set( 0, pageNumber );
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSBase getCOSObject()
    {
        return array;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSArray getCOSArray()
    {
        return array;
    }
}
