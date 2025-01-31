package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.annotation;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDRectangle;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color.PDColor;

/**
 * This is the class that represents a rectangular or eliptical annotation
 * Introduced in PDF 1.3 specification .
 *
 * @author Paul King
 */
public class PDAnnotationSquareCircle extends PDAnnotationMarkup
{

    /**
     * Constant for a Rectangular type of annotation.
     */
    public static final String SUB_TYPE_SQUARE = "Square";
    /**
     * Constant for an Eliptical type of annotation.
     */
    public static final String SUB_TYPE_CIRCLE = "Circle";

    /**
     * Creates a Circle or Square annotation of the specified sub type.
     *
     * @param subType the subtype the annotation represents.
         */
    public PDAnnotationSquareCircle( String subType )
    {
        super();
        setSubtype( subType );
    }

    /**
     * Creates a Line annotation from a COSDictionary, expected to be a correct
     * object definition.
     *
     * @param field
     *            the PDF objet to represent as a field.
     */
    public PDAnnotationSquareCircle( COSDictionary field )
    {
        super( field );
    }


    /**
     * This will set interior color of the drawn area
     * Color is in DeviceRGB colorspace.
     *
     * @param ic color in the DeviceRGB color space.
     */
    public void setInteriorColor( PDColor ic )
    {
    	getDictionary().setItem(COSName.IC, ic.toCOSArray());
    }

    /**
     * This will retrieve the interior color of the drawn area
     * color is in DeviceRGB color space.
     *
     * @return object representing the color.
     */
    public PDColor getInteriorColor()
    {
    	return getColor(COSName.IC);
    }


    /**
     * This will set the border effect dictionary, specifying effects to be applied
     * when drawing the line.
     *
     * @param be The border effect dictionary to set.
     *
     */
    public void setBorderEffect( PDBorderEffectDictionary be )
    {
        getDictionary().setItem( "BE", be );
    }

    /**
     * This will retrieve the border effect dictionary, specifying effects to be
     * applied used in drawing the line.
     *
     * @return The border effect dictionary
     */
    public PDBorderEffectDictionary getBorderEffect()
    {
        COSDictionary be = (COSDictionary) getDictionary().getDictionaryObject( "BE" );
        if (be != null)
        {
            return new PDBorderEffectDictionary( be );
        }
        else
        {
            return null;
        }
    }

    /**
     * This will set the rectangle difference rectangle. Giving the difference
     * between the annotations rectangle and where the drawing occurs.
         * (To take account of any effects applied through the BE entry forexample)
     *
     * @param rd the rectangle difference
     *
     */
    public void setRectDifference( PDRectangle rd )
    {
        getDictionary().setItem( "RD", rd );
    }

    /**
     * This will get the rectangle difference rectangle. Giving the difference
     * between the annotations rectangle and where the drawing occurs.
         * (To take account of any effects applied through the BE entry forexample)
     *
     * @return the rectangle difference
     */
    public PDRectangle getRectDifference()
    {
        COSArray rd = (COSArray) getDictionary().getDictionaryObject( "RD" );
        if (rd != null)
        {
            return new PDRectangle( rd );
        }
        else
        {
            return null;
        }
    }

    /**
     * This will set the sub type (and hence appearance, AP taking precedence) For
     * this annotation. See the SUB_TYPE_XXX constants for valid values.
     *
     * @param subType The subtype of the annotation
     */
    public void setSubtype( String subType )
    {
        getDictionary().setName( COSName.SUBTYPE, subType );
    }

    /**
     * This will retrieve the sub type (and hence appearance, AP taking precedence)
     * For this annotation.
     *
     * @return The subtype of this annotation, see the SUB_TYPE_XXX constants.
     */
    @Override
    public String getSubtype()
    {
        return getDictionary().getNameAsString( COSName.SUBTYPE);
    }

    /**
     * This will set the border style dictionary, specifying the width and dash
     * pattern used in drawing the line.
     *
     * @param bs the border style dictionary to set.
     * TODO not all annotations may have a BS entry
     *
     */
    public void setBorderStyle( PDBorderStyleDictionary bs )
    {
        this.getDictionary().setItem( "BS", bs);
    }

    /**
     * This will retrieve the border style dictionary, specifying the width and
     * dash pattern used in drawing the line.
     *
     * @return the border style dictionary.
     * TODO not all annotations may have a BS entry
     */
    public PDBorderStyleDictionary getBorderStyle()
    {
    	COSDictionary bs = (COSDictionary) this.getDictionary().getItem(COSName.BS);
        if (bs != null)
        {
            return new PDBorderStyleDictionary( bs );
        }
        else
        {
            return null;
        }
    }

}
