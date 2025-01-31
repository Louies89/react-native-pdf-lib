package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.destination;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;

/**
 * This represents a destination to a page at a x location and the height is magnified
 * to just fit on the screen.
 *
 * @author Ben Litchfield
 */
public class PDPageFitHeightDestination extends PDPageDestination
{
    /**
     * The type of this destination.
     */
    protected static final String TYPE = "FitV";
    /**
     * The type of this destination.
     */
    protected static final String TYPE_BOUNDED = "FitBV";

    /**
     * Default constructor.
     *
     */
    public PDPageFitHeightDestination()
    {
        super();
        array.growToSize(3);
        array.setName( 1, TYPE );

    }

    /**
     * Constructor from an existing destination array.
     *
     * @param arr The destination array.
     */
    public PDPageFitHeightDestination( COSArray arr )
    {
        super( arr );
    }

    /**
     * Get the left x coordinate.  A return value of -1 implies that the current x-coordinate
     * will be used.
     *
     * @return The left x coordinate.
     */
    public int getLeft()
    {
        return array.getInt( 2 );
    }

    /**
     * Set the left x-coordinate, a value of -1 implies that the current x-coordinate
     * will be used.
     * @param x The left x coordinate.
     */
    public void setLeft( int x )
    {
        array.growToSize( 3 );
        if( x == -1 )
        {
            array.set( 2, (COSBase)null );
        }
        else
        {
            array.setInt( 2, x );
        }
    }

    /**
     * A flag indicating if this page destination should just fit bounding box of the PDF.
     *
     * @return true If the destination should fit just the bounding box.
     */
    public boolean fitBoundingBox()
    {
        return TYPE_BOUNDED.equals( array.getName( 1 ) );
    }

    /**
     * Set if this page destination should just fit the bounding box.  The default is false.
     *
     * @param fitBoundingBox A flag indicating if this should fit the bounding box.
     */
    public void setFitBoundingBox( boolean fitBoundingBox )
    {
        array.growToSize( 2 );
        if( fitBoundingBox )
        {
            array.setName( 1, TYPE_BOUNDED );
        }
        else
        {
            array.setName( 1, TYPE );
        }
    }
}
