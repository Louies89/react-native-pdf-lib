package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.destination;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;

/**
 * This represents a destination to a page at a y location and the width is magnified
 * to just fit on the screen.
 *
 * @author Ben Litchfield
 */
public class PDPageFitRectangleDestination extends PDPageDestination
{
    /**
     * The type of this destination.
     */
    protected static final String TYPE = "FitR";

    /**
     * Default constructor.
     *
     */
    public PDPageFitRectangleDestination()
    {
        super();
        array.growToSize(6);
        array.setName( 1, TYPE );

    }

    /**
     * Constructor from an existing destination array.
     *
     * @param arr The destination array.
     */
    public PDPageFitRectangleDestination( COSArray arr )
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
     * Get the bottom y coordinate.  A return value of -1 implies that the current y-coordinate
     * will be used.
     *
     * @return The bottom y coordinate.
     */
    public int getBottom()
    {
        return array.getInt( 3 );
    }

    /**
     * Set the bottom y-coordinate, a value of -1 implies that the current y-coordinate
     * will be used.
     * @param y The bottom y coordinate.
     */
    public void setBottom( int y )
    {
        array.growToSize( 6 );
        if( y == -1 )
        {
            array.set( 3, (COSBase)null );
        }
        else
        {
            array.setInt( 3, y );
        }
    }

    /**
     * Get the right x coordinate.  A return value of -1 implies that the current x-coordinate
     * will be used.
     *
     * @return The right x coordinate.
     */
    public int getRight()
    {
        return array.getInt( 4 );
    }

    /**
     * Set the right x-coordinate, a value of -1 implies that the current x-coordinate
     * will be used.
     * @param x The right x coordinate.
     */
    public void setRight( int x )
    {
        array.growToSize( 6 );
        if( x == -1 )
        {
            array.set( 4, (COSBase)null );
        }
        else
        {
            array.setInt( 4, x );
        }
    }


    /**
     * Get the top y coordinate.  A return value of -1 implies that the current y-coordinate
     * will be used.
     *
     * @return The top y coordinate.
     */
    public int getTop()
    {
        return array.getInt( 5 );
    }

    /**
     * Set the top y-coordinate, a value of -1 implies that the current y-coordinate
     * will be used.
     * @param y The top ycoordinate.
     */
    public void setTop( int y )
    {
        array.growToSize( 6 );
        if( y == -1 )
        {
            array.set( 5, (COSBase)null );
        }
        else
        {
            array.setInt( 5, y );
        }
    }
}
