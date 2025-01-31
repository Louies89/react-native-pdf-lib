package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.outline;

import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;

/**
 * This represents an outline in a pdf document.
 *
 * @author Ben Litchfield
 */
public final class PDDocumentOutline extends PDOutlineNode
{

    /**
     * Default Constructor.
     */
    public PDDocumentOutline()
    {
    	getCOSDictionary().setName(COSName.TYPE, COSName.OUTLINES.getName());
    }

    /**
     * Constructor for an existing document outline.
     *
     * @param dic The storage dictionary.
     */
    public PDDocumentOutline( COSDictionary dic )
    {
    	super( dic );
    	getCOSDictionary().setName(COSName.TYPE, COSName.OUTLINES.getName());
    }

    @Override
    public boolean isNodeOpen()
    {
    	return true;
    }

    @Override
    public void openNode()
    {
    	// The root of the outline hierarchy is not an OutlineItem and cannot be opened or closed
    }

    @Override
    public void closeNode()
    {
    	// The root of the outline hierarchy is not an OutlineItem and cannot be opened or closed
    }
}
