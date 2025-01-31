package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.annotation;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;

/**
 * This class represents a PDF /BE entry the border effect dictionary.
 *
 * @author Paul King
 */
public class PDBorderEffectDictionary implements COSObjectable
{

    /*
     * The various values of the effect applied to the border as defined in the
     * PDF 1.6 reference Table 8.14
     */

    /**
     * Constant for the name for no effect.
     */
    public static final String STYLE_SOLID = "S";

    /**
     * Constant for the name of a cloudy effect.
     */
    public static final String STYLE_CLOUDY = "C";

    private COSDictionary dictionary;

    /**
     * Constructor.
     */
    public PDBorderEffectDictionary()
    {
        dictionary = new COSDictionary();
    }

    /**
     * Constructor.
     *
     * @param dict
     *            a border style dictionary.
     */
    public PDBorderEffectDictionary( COSDictionary dict )
    {
        dictionary = dict;
    }

    /**
     * returns the dictionary.
     *
     * @return the dictionary
     */
    public COSDictionary getDictionary()
    {
        return dictionary;
    }

    /**
     * returns the dictionary.
     *
     * @return the dictionary
     */
    public COSBase getCOSObject()
    {
        return dictionary;
    }

    /**
     * This will set the intensity of the applied effect.
     *
     * @param i
     *            the intensity of the effect values 0 to 2
     */
    public void setIntensity( float i )
    {
        getDictionary().setFloat( "I", i );
    }

    /**
     * This will retrieve the intensity of the applied effect.
     *
     * @return the intensity value 0 to 2
     */
    public float getIntensity()
    {
        return getDictionary().getFloat( "I", 0 );
    }

    /**
     * This will set the border effect, see the STYLE_* constants for valid values.
     *
     * @param s
     *            the border effect to use
     */
    public void setStyle( String s )
    {
        getDictionary().setName( "S", s );
    }

    /**
     * This will retrieve the border effect, see the STYLE_* constants for valid
     * values.
     *
     * @return the effect of the border
     */
    public String getStyle()
    {
        return getDictionary().getNameAsString( "S", STYLE_SOLID );
    }

}
