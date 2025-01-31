package com.hopding.pdflib.apache.pdfbox.pdmodel.documentinterchange.logicalstructure;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;

/**
 * The MarkInfo provides additional information relevant to specialized
 * uses of structured documents.
 *
 * @author Ben Litchfield
 */
public class PDMarkInfo implements COSObjectable
{
    private COSDictionary dictionary;

    /**
     * Default Constructor.
     *
     */
    public PDMarkInfo()
    {
        dictionary = new COSDictionary();
    }

    /**
     * Constructor for an existing MarkInfo element.
     *
     * @param dic The existing dictionary.
     */
    public PDMarkInfo( COSDictionary dic )
    {
        dictionary = dic;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    @Override
    public COSBase getCOSObject()
    {
        return dictionary;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSDictionary getDictionary()
    {
        return dictionary;
    }

    /**
     * Tells if this is a tagged PDF.
     *
     * @return true If this is a tagged PDF.
     */
    public boolean isMarked()
    {
        return dictionary.getBoolean( "Marked", false );
    }

    /**
     * Set if this is a tagged PDF.
     *
     * @param value The new marked value.
     */
    public void setMarked( boolean value )
    {
        dictionary.setBoolean( "Marked", value );
    }

    /**
     * Tells if structure elements use user properties.
     *
     * @return A boolean telling if the structure elements use user properties.
     */
    public boolean usesUserProperties()
    {
        return dictionary.getBoolean( "UserProperties", false );
    }

    /**
     * Set if the structure elements contain user properties.
     *
     * @param userProps The new value for this property.
     */
    public void setUserProperties( boolean userProps )
    {
        dictionary.setBoolean( "UserProperties", userProps );
    }

    /**
     * Tells if this PDF contain 'suspect' tags.  See PDF Reference 1.6
     * section 10.6 "Logical Structure" for more information about this property.
     *
     * @return true if the suspect flag has been set.
     */
    public boolean isSuspect()
    {
        return dictionary.getBoolean( "Suspects", false );
    }

    /**
     * Set the value of the suspects property.  See PDF Reference 1.6
     * section 10.6 "Logical Structure" for more information about this
     * property.
     *
     * @param suspect The new "Suspects" value.
     */
    public void setSuspect( boolean suspect )
    {
        dictionary.setBoolean( "Suspects", false );
    }
}
