package com.hopding.pdflib.apache.pdfbox.pdmodel.fdf;

import java.io.IOException;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.filespecification.PDFileSpecification;

/**
 * This represents an FDF named page reference that is part of the FDF field.
 *
 * @author Ben Litchfield
 */
public class FDFNamedPageReference implements COSObjectable
{
    private COSDictionary ref;

    /**
     * Default constructor.
     */
    public FDFNamedPageReference()
    {
        ref = new COSDictionary();
    }

    /**
     * Constructor.
     *
     * @param r The FDF named page reference dictionary.
     */
    public FDFNamedPageReference( COSDictionary r )
    {
        ref = r;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSBase getCOSObject()
    {
        return ref;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSDictionary getCOSDictionary()
    {
        return ref;
    }

    /**
     * This will get the name of the referenced page.  A required parameter.
     *
     * @return The name of the referenced page.
     */
    public String getName()
    {
        return ref.getString( COSName.NAME );
    }

    /**
     * This will set the name of the referenced page.
     *
     * @param name The referenced page name.
     */
    public void setName( String name )
    {
        ref.setString( COSName.NAME, name );
    }

    /**
     * This will get the file specification of this reference.  An optional parameter.
     *
     * @return The F entry for this dictionary.
     *
     * @throws IOException If there is an error creating the file spec.
     */
    public PDFileSpecification getFileSpecification() throws IOException
    {
        return PDFileSpecification.createFS( ref.getDictionaryObject( COSName.F ) );
    }

    /**
     * This will set the file specification for this named page reference.
     *
     * @param fs The file specification to set.
     */
    public void setFileSpecification( PDFileSpecification fs )
    {
        ref.setItem( COSName.F, fs );
    }
}
