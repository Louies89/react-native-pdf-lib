package com.hopding.pdflib.apache.pdfbox.pdmodel;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;

/**
 * This class holds all of the name trees that are available at the document level.
 *
 * @author Ben Litchfield
 */
public class PDDocumentNameDictionary implements COSObjectable
{
    private COSDictionary nameDictionary;
    private PDDocumentCatalog catalog;

    /**
     * Constructor.
     *
     * @param cat The document catalog that this dictionary is part of.
     */
    public PDDocumentNameDictionary( PDDocumentCatalog cat )
    {
        COSBase names = cat.getCOSObject().getDictionaryObject(COSName.NAMES);
        if (names != null)
        {
            nameDictionary = (COSDictionary)names;
        }
        else
        {
            nameDictionary = new COSDictionary();
            cat.getCOSObject().setItem(COSName.NAMES, nameDictionary);
        }
        catalog = cat;
    }

    /**
     * Constructor.
     *
     * @param cat The document that this dictionary is part of.
     * @param names The names dictionary.
     */
    public PDDocumentNameDictionary( PDDocumentCatalog cat, COSDictionary names )
    {
        catalog = cat;
        nameDictionary = names;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSBase getCOSObject()
    {
        return nameDictionary;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos dictionary for this object.
     */
    public COSDictionary getCOSDictionary()
    {
        return nameDictionary;
    }

    /**
     * Get the destination named tree node.  The value in this name tree will be PDDestination
     * objects.
     *
     * @return The destination name tree node.
     */
    public PDDestinationNameTreeNode getDests()
    {
        PDDestinationNameTreeNode dests = null;

        COSDictionary dic = (COSDictionary)nameDictionary.getDictionaryObject( COSName.DESTS );

        //The document catalog also contains the Dests entry sometimes
        //so check there as well.
        if( dic == null )
        {
            dic = (COSDictionary)catalog.getCOSObject().getDictionaryObject( COSName.DESTS );
        }

        if( dic != null )
        {
            dests = new PDDestinationNameTreeNode( dic );
        }


        return dests;
    }

    /**
     * Set the named destinations that are associated with this document.
     *
     * @param dests The destination names.
     */
    public void setDests( PDDestinationNameTreeNode dests )
    {
        nameDictionary.setItem( COSName.DESTS, dests );
        //The dests can either be in the document catalog or in the
        //names dictionary, PDFBox will just maintain the one in the
        //names dictionary for now unless there is a reason to do
        //something else.
        //clear the potentially out of date Dests reference.
        catalog.getCOSObject().setItem( COSName.DESTS, (COSObjectable)null);
    }

    /**
     * Get the embedded files named tree node.  The value in this name tree will be PDComplexFileSpecification
     * objects.
     *
     * @return The embedded files name tree node.
     */
    public PDEmbeddedFilesNameTreeNode getEmbeddedFiles()
    {
        PDEmbeddedFilesNameTreeNode retval = null;

        COSDictionary dic = (COSDictionary)nameDictionary.getDictionaryObject( COSName.EMBEDDED_FILES );

        if( dic != null )
        {
            retval = new PDEmbeddedFilesNameTreeNode( dic );
        }

        return retval;
    }

    /**
     * Set the named embedded files that are associated with this document.
     *
     * @param ef The new embedded files
     */
    public void setEmbeddedFiles( PDEmbeddedFilesNameTreeNode ef )
    {
        nameDictionary.setItem( COSName.EMBEDDED_FILES, ef );
    }

    /**
     * Get the document level javascript entries.  The value in this name tree will be PDTextStream.
     *
     * @return The document level named javascript.
     */
    public PDJavascriptNameTreeNode getJavaScript()
    {
        PDJavascriptNameTreeNode retval = null;

        COSDictionary dic = (COSDictionary)nameDictionary.getDictionaryObject( COSName.JAVA_SCRIPT );

        if( dic != null )
        {
            retval = new PDJavascriptNameTreeNode( dic );
        }

        return retval;
    }

    /**
     * Set the named javascript entries that are associated with this document.
     *
     * @param js The new Javascript entries.
     */
    public void setJavascript( PDJavascriptNameTreeNode js )
    {
        nameDictionary.setItem( COSName.JAVA_SCRIPT, js );
    }
}
