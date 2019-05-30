package com.hopding.pdflib.apache.pdfbox.pdmodel.common;

import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;

/**
 * A wrapper for a COS dictionary including Type information.
 *
 * @author Johannes Koch
 *
 */
public class PDTypedDictionaryWrapper extends PDDictionaryWrapper
{

    /**
     * Creates a new instance with a given type.
     * 
     * @param type the type (Type)
     */
    public PDTypedDictionaryWrapper(String type)
    {
        super();
        this.getCOSDictionary().setName(COSName.TYPE, type);
    }

    /**
     * Creates a new instance with a given COS dictionary.
     * 
     * @param dictionary the dictionary
     */
    public PDTypedDictionaryWrapper(COSDictionary dictionary)
    {
        super(dictionary);
    }


    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType()
    {
        return this.getCOSDictionary().getNameAsString(COSName.TYPE);
    }

    // There is no setType(String) method because changing the Type would most
    // probably also change the type of PD object.
}
