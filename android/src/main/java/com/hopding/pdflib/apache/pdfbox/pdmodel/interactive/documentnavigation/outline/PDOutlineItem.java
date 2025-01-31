package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.outline;

import java.io.IOException;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSFloat;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDestinationNameTreeNode;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDPage;
import com.hopding.pdflib.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color.PDColor;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.action.PDAction;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.action.PDActionFactory;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import com.hopding.pdflib.apache.pdfbox.util.awt.AWTColor;

/**
 * This represents an outline item in a pdf document. The items at each level of the hierarchy form an iterable linked
 * list, chained together through their Prev and Next entries.
 *
 * @author Ben Litchfield
 */
public final class PDOutlineItem extends PDOutlineNode
{
    private static final int ITALIC_FLAG = 1;
    private static final int BOLD_FLAG = 2;

    /**
     * Default Constructor.
     */
    public PDOutlineItem()
    {
        super();
    }

    /**
     * Constructor for an existing outline item.
     *
     * @param dic The storage dictionary.
     */
    public PDOutlineItem( COSDictionary dic )
    {
        super( dic );
    }

    /**
     * Insert a single sibling after this node.
     *
     * @param newSibling The item to insert
     * @throws IllegalArgumentException if the given sibling node is part of a list
     * (i.e. if it has a previous or a next sibling)
     */
    public void insertSiblingAfter(PDOutlineItem newSibling)
    {
    	requireSingleNode(newSibling);
    	PDOutlineNode parent = getParent();
    	newSibling.setParent(parent);
        PDOutlineItem next = getNextSibling();
        setNextSibling(newSibling);
        newSibling.setPreviousSibling(this);
        if (next != null)
        {
            newSibling.setNextSibling(next);
            next.setPreviousSibling(newSibling);
        }
        else if (parent != null)
        {
        	getParent().setLastChild(newSibling);
        }
        updateParentOpenCountForAddedChild(newSibling);
    }

    /**
     * Insert a single sibling before this node.
     *
     * @param newSibling The item to insert.
     * @throws IllegalArgumentException if the given sibling node is part of a list
     * (i.e. if it has a previous or a next sibling)
     */
    public void insertSiblingBefore(PDOutlineItem newSibling)
    {
    	requireSingleNode(newSibling);
    	PDOutlineNode parent = getParent();
    	newSibling.setParent(parent);
    	PDOutlineItem previous = getPreviousSibling();
    	setPreviousSibling(newSibling);
    	newSibling.setNextSibling(this);
    	if (previous != null)
    	{
    		previous.setNextSibling(newSibling);
    		newSibling.setPreviousSibling(previous);
    	}
    	else if (parent != null)
    	{
    		getParent().setFirstChild(newSibling);
    	}
    	updateParentOpenCountForAddedChild(newSibling);
    }

    /**
     * Return the previous sibling or null if there is no sibling.
     *
     * @return The previous sibling.
     */
    public PDOutlineItem getPreviousSibling()
    {
    	return getOutlineItem(COSName.PREV);
    }

    /**
     * Set the previous sibling, this will be maintained by this class.
     *
     * @param outlineNode The new previous sibling.
     */
    void setPreviousSibling(PDOutlineNode outlineNode)
    {
    	getCOSDictionary().setItem(COSName.PREV, outlineNode);
    }

    /**
     * @return The next sibling or null if there is no next sibling.
     */
    public PDOutlineItem getNextSibling()
    {
    	return getOutlineItem(COSName.NEXT);
    }

    /**
     * Set the next sibling, this will be maintained by this class.
     *
     * @param outlineNode The new next sibling.
     */
    void setNextSibling(PDOutlineNode outlineNode)
    {
    	getCOSDictionary().setItem(COSName.NEXT, outlineNode);
    }

    /**
     * Get the title of this node.
     *
     * @return The title of this node.
     */
    public String getTitle()
    {
    	return getCOSDictionary().getString(COSName.TITLE);
    }

    /**
     * Set the title for this node.
     *
     * @param title The new title for this node.
     */
    public void setTitle(String title)
    {
    	getCOSDictionary().setString(COSName.TITLE, title);
    }

    /**
     * Get the page destination of this node.
     *
     * @return The page destination of this node.
     * @throws IOException If there is an error creating the destination.
     */
    public PDDestination getDestination() throws IOException
    {
    	return PDDestination.create(getCOSDictionary().getDictionaryObject(COSName.DEST));
    }

    /**
     * Set the page destination for this node.
     *
     * @param dest The new page destination for this node.
     */
    public void setDestination(PDDestination dest)
    {
    	getCOSDictionary().setItem(COSName.DEST, dest);
    }

    /**
     * A convenience method that will create an XYZ destination using only the defaults.
     *
     * @param page The page to refer to.
     */
    public void setDestination(PDPage page)
    {
        PDPageXYZDestination dest = null;
        if( page != null )
        {
            dest = new PDPageXYZDestination();
            dest.setPage( page );
        }
        setDestination( dest );
    }

    /**
     * This method will attempt to find the page in this PDF document that this outline points to.
     * If the outline does not point to anything then this method will return null.  If the outline
     * is an action that is not a GoTo action then this methods will throw the OutlineNotLocationException
     *
     * @param doc The document to get the page from.
     *
     * @return The page that this outline will go to when activated or null if it does not point to anything.
     * @throws IOException If there is an error when trying to find the page.
     */
    public PDPage findDestinationPage( PDDocument doc ) throws IOException
    {
        PDDestination dest = getDestination();
        if( dest == null )
        {
            PDAction outlineAction = getAction();
            if( outlineAction instanceof PDActionGoTo )
            {
                dest = ((PDActionGoTo)outlineAction).getDestination();
            }
            else
            {
                return null;
            }
        }

        PDPageDestination pageDestination;
        if( dest instanceof PDNamedDestination )
        {
            //if we have a named destination we need to lookup the PDPageDestination
            PDNamedDestination namedDest = (PDNamedDestination)dest;
            PDDocumentNameDictionary namesDict = doc.getDocumentCatalog().getNames();
            if( namesDict != null )
            {
                PDDestinationNameTreeNode destsTree = namesDict.getDests();
                if( destsTree != null )
                {
                    pageDestination = (PDPageDestination)destsTree.getValue( namedDest.getNamedDestination() );
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        else if( dest instanceof PDPageDestination)
        {
            pageDestination = (PDPageDestination) dest;
        }
        else if( dest == null )
        {
            return null;
        }
        else
        {
            throw new IOException( "Error: Unknown destination type " + dest );
        }

        PDPage page = pageDestination.getPage();
        if( page == null )
        {
            int pageNumber = pageDestination.getPageNumber();
            if( pageNumber != -1 )
            {
                page = doc.getPage( pageNumber - 1 );
            }
        }

        return page;
    }

    /**
     * Get the action of this node.
     *
     * @return The action of this node.
     */
    public PDAction getAction()
    {
    	return PDActionFactory.createAction((COSDictionary) getCOSDictionary().getDictionaryObject(COSName.A));
    }

    /**
     * Set the action for this node.
     *
     * @param action The new action for this node.
     */
    public void setAction( PDAction action )
    {
    	getCOSDictionary().setItem(COSName.A, action);
    }

    /**
     * Get the structure element of this node.
     *
     * @return The structure element of this node.
     */
    public PDStructureElement getStructureElement()
    {
        PDStructureElement se = null;
        COSDictionary dic = (COSDictionary) getCOSDictionary().getDictionaryObject(COSName.SE);
        if( dic != null )
        {
            se = new PDStructureElement( dic );
        }
        return se;
    }

    /**
     * Set the structure element for this node.
     *
     * @param structureElement The new structure element for this node.
     */
    public void setStructuredElement( PDStructureElement structureElement )
    {
    	getCOSDictionary().setItem(COSName.SE, structureElement);
    }

    /**
     * Get the RGB text color of this node.  Default is black and this method
     * will never return null.
     *
     * @return The structure element of this node.
     */
    public PDColor getTextColor()
    {
        COSArray csValues = (COSArray) getCOSDictionary().getDictionaryObject(COSName.C);
        if( csValues == null )
        {
            csValues = new COSArray();
            csValues.growToSize( 3, new COSFloat( 0 ) );
            getCOSDictionary().setItem( COSName.C, csValues );
        }
        return new PDColor(csValues, PDDeviceRGB.INSTANCE);
    }

    /**
     * Set the RGB text color for this node.
     *
     * @param textColor The text color for this node.
     */
    public void setTextColor( PDColor textColor )
    {
    	getCOSDictionary().setItem( COSName.C, textColor.toCOSArray() );
    }

    /**
     * Set the RGB text color for this node.
     *
     * @param textColor The text color for this node.
     */
    public void setTextColor( AWTColor textColor )
    {
        COSArray array = new COSArray();
        array.add( new COSFloat( textColor.getRed()/255f));
        array.add( new COSFloat( textColor.getGreen()/255f));
        array.add( new COSFloat( textColor.getBlue()/255f));
        getCOSDictionary().setItem( COSName.C, array );
    }

    /**
     * A flag telling if the text should be italic.
     *
     * @return The italic flag.
     */
    public boolean isItalic()
    {
    	return getCOSDictionary().getFlag( COSName.F, ITALIC_FLAG );
    }

    /**
     * Set the italic property of the text.
     *
     * @param italic The new italic flag.
     */
    public void setItalic( boolean italic )
    {
    	getCOSDictionary().setFlag( COSName.F, ITALIC_FLAG, italic );
    }

    /**
     * A flag telling if the text should be bold.
     *
     * @return The bold flag.
     */
    public boolean isBold()
    {
    	return getCOSDictionary().getFlag( COSName.F, BOLD_FLAG );
    }

    /**
     * Set the bold property of the text.
     *
     * @param bold The new bold flag.
     */
    public void setBold( boolean bold )
    {
    	getCOSDictionary().setFlag( COSName.F, BOLD_FLAG, bold );
    }
}
