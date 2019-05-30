package com.hopding.pdflib.apache.pdfbox.pdmodel;

import java.io.IOException;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSStream;
import com.hopding.pdflib.apache.pdfbox.cos.COSString;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDNameTreeNode;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDTextStream;

/**
 * This class holds all of the name trees that are available at the document level.
 *
 * @author Ben Litchfield
 */
public class PDJavascriptNameTreeNode extends PDNameTreeNode
{
    /**
     * Constructor.
     */
    public PDJavascriptNameTreeNode()
    {
        super( PDTextStream.class );
    }

    /**
     * Constructor.
     *
     * @param dic The COS dictionary.
     */
    public PDJavascriptNameTreeNode( COSDictionary dic )
    {
        super( dic, PDTextStream.class );
    }

    /**
     * {@inheritDoc}
     */
    protected COSObjectable convertCOSToPD( COSBase base ) throws IOException
    {
        PDTextStream stream = null;
        if( base instanceof COSString )
        {
            stream = new PDTextStream((COSString)base);
        }
        else if( base instanceof COSStream )
        {
            stream = new PDTextStream((COSStream)base);
        }
        else
        {
            throw new IOException( "Error creating Javascript object, expected either COSString or COSStream and not " 
                    + base );
        }
        return stream;
    }

    /**
     * {@inheritDoc}
     */
    protected PDNameTreeNode createChildNode( COSDictionary dic )
    {
        return new PDJavascriptNameTreeNode(dic);
    }
}
