package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.pagenavigation;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDPage;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * This a single bead in a thread in a PDF document.
 *
 * @author Ben Litchfield
 */
public class PDThreadBead implements COSObjectable
{


    private COSDictionary bead;

    /**
     * Constructor that is used for a preexisting dictionary.
     *
     * @param b The underlying dictionary.
     */
    public PDThreadBead( COSDictionary b )
    {
        bead = b;
    }

    /**
     * Default constructor.
     *
     */
    public PDThreadBead()
    {
        bead = new COSDictionary();
        bead.setName( "Type", "Bead" );
        setNextBead( this );
        setPreviousBead( this );
    }

    /**
     * This will get the underlying dictionary that this object wraps.
     *
     * @return The underlying info dictionary.
     */
    public COSDictionary getDictionary()
    {
        return bead;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSBase getCOSObject()
    {
        return bead;
    }

    /**
     * This will get the thread that this bead is part of.  This is only required
     * for the first bead in a thread, so other beads 'may' return null.
     *
     * @return The thread that this bead is part of.
     */
    public PDThread getThread()
    {
        PDThread retval = null;
        COSDictionary dic = (COSDictionary)bead.getDictionaryObject( "T" );
        if( dic != null )
        {
            retval = new PDThread( dic );
        }
        return retval;
    }

    /**
     * Set the thread that this bead is part of.  This is only required for the
     * first bead in a thread.  Note: This property is set for you by the PDThread.setFirstBead() method.
     *
     * @param thread The thread that this bead is part of.
     */
    public void setThread( PDThread thread )
    {
        bead.setItem( "T", thread );
    }

    /**
     * This will get the next bead.  If this bead is the last bead in the list then this
     * will return the first bead.
     *
     * @return The next bead in the list or the first bead if this is the last bead.
     */
    public PDThreadBead getNextBead()
    {
        return new PDThreadBead( (COSDictionary) bead.getDictionaryObject( "N" ) );
    }

    /**
     * Set the next bead in the thread.
     *
     * @param next The next bead.
     */
    protected void setNextBead( PDThreadBead next )
    {
        bead.setItem( "N", next );
    }

    /**
     * This will get the previous bead.  If this bead is the first bead in the list then this
     * will return the last bead.
     *
     * @return The previous bead in the list or the last bead if this is the first bead.
     */
    public PDThreadBead getPreviousBead()
    {
        return new PDThreadBead( (COSDictionary) bead.getDictionaryObject( "V" ) );
    }

    /**
     * Set the previous bead in the thread.
     *
     * @param previous The previous bead.
     */
    protected void setPreviousBead( PDThreadBead previous )
    {
        bead.setItem( "V", previous );
    }

    /**
     * Append a bead after this bead.  This will correctly set the next/previous beads in the
     * linked list.
     *
     * @param append The bead to insert.
     */
    public void appendBead( PDThreadBead append )
    {
        PDThreadBead nextBead = getNextBead();
        nextBead.setPreviousBead( append );
        append.setNextBead( nextBead );
        setNextBead( append );
        append.setPreviousBead( this );
    }

    /**
     * Get the page that this bead is part of.
     *
     * @return The page that this bead is part of.
     */
    public PDPage getPage()
    {
        PDPage page = null;
        COSDictionary dic = (COSDictionary)bead.getDictionaryObject( "P" );
        if( dic != null )
        {
            page = new PDPage( dic );
        }
        return page;
    }

    /**
     * Set the page that this bead is part of.  This is a required property and must be
     * set when creating a new bead.  The PDPage object also has a list of beads in the natural
     * reading order.  It is recommended that you add this object to that list as well.
     *
     * @param page The page that this bead is on.
     */
    public void setPage( PDPage page )
    {
        bead.setItem( "P", page );
    }

    /**
     * The rectangle on the page that this bead is part of.
     *
     * @return The part of the page that this bead covers.
     */
    public PDRectangle getRectangle()
    {
        PDRectangle rect = null;
        COSArray array = (COSArray)bead.getDictionaryObject( COSName.R );
        if( array != null )
        {
            rect = new PDRectangle( array );
        }
        return rect;
    }

    /**
     * Set the rectangle on the page that this bead covers.
     *
     * @param rect The portion of the page that this bead covers.
     */
    public void setRectangle( PDRectangle rect )
    {
        bead.setItem( COSName.R, rect );
    }
}
