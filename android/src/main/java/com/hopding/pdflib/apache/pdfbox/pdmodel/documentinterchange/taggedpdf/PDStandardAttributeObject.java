package com.hopding.pdflib.apache.pdfbox.pdmodel.documentinterchange.taggedpdf;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSFloat;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;
import com.hopding.pdflib.apache.pdfbox.cos.COSString;
import com.hopding.pdflib.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDAttributeObject;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color.PDGamma;

/**
 * A standard attribute object.
 * 
 * @author Johannes Koch
 */
public abstract class PDStandardAttributeObject extends PDAttributeObject
{

    /**
     * Default constructor.
     */
    public PDStandardAttributeObject()
    {
    }

    /**
     * Creates a new standard attribute object with a given dictionary.
     * 
     * @param dictionary the dictionary
     */
    public PDStandardAttributeObject(COSDictionary dictionary)
    {
        super(dictionary);
    }


    /**
     * Is the attribute with the given name specified in this attribute object?
     * 
     * @param name the attribute name
     * @return <code>true</code> if the attribute is specified,
     * <code>false</code> otherwise
     */
    public boolean isSpecified(String name)
    {
        return this.getCOSDictionary().getDictionaryObject(name) != null;
    }


    /**
     * Gets a string attribute value.
     * 
     * @param name the attribute name
     * @return the string attribute value
     */
    protected String getString(String name)
    {
        return this.getCOSDictionary().getString(name);
    }

    /**
     * Sets a string attribute value.
     * 
     * @param name the attribute name
     * @param value the string attribute value
     */
    protected void setString(String name, String value)
    {
        COSBase oldBase = this.getCOSDictionary().getDictionaryObject(name);
        this.getCOSDictionary().setString(name, value);
        COSBase newBase = this.getCOSDictionary().getDictionaryObject(name);
        this.potentiallyNotifyChanged(oldBase, newBase);
    }

    /**
     * Gets an array of strings.
     * 
     * @param name the attribute name
     * @return the array of strings
     */
    protected String[] getArrayOfString(String name)
    {
        COSBase v = this.getCOSDictionary().getDictionaryObject(name);
        if (v instanceof COSArray)
        {
            COSArray array = (COSArray) v;
            String[] strings = new String[array.size()];
            for (int i = 0; i < array.size(); i++)
            {
                strings[i] = ((COSName) array.getObject(i)).getName();
            }
            return strings;
        }
        return null;
    }

    /**
     * Sets an array of strings.
     * 
     * @param name the attribute name
     * @param values the array of strings
     */
    protected void setArrayOfString(String name, String[] values)
    {
        COSBase oldBase = this.getCOSDictionary().getDictionaryObject(name);
        COSArray array = new COSArray();
        for (String value : values)
        {
            array.add(new COSString(value));
        }
        this.getCOSDictionary().setItem(name, array);
        COSBase newBase = this.getCOSDictionary().getDictionaryObject(name);
        this.potentiallyNotifyChanged(oldBase, newBase);
    }

    /**
     * Gets a name value.
     * 
     * @param name the attribute name
     * @return the name value
     */
    protected String getName(String name)
    {
        return this.getCOSDictionary().getNameAsString(name);
    }

    /**
     * Gets a name value.
     * 
     * @param name the attribute name
     * @param defaultValue the default value
     * @return the name value
     */
    protected String getName(String name, String defaultValue)
    {
        return this.getCOSDictionary().getNameAsString(name, defaultValue);
    }

    /**
     * Gets a name value or array of name values.
     * 
     * @param name the attribute name
     * @param defaultValue the default value
     * @return a String or array of Strings
     */
    protected Object getNameOrArrayOfName(String name, String defaultValue)
    {
        COSBase v = this.getCOSDictionary().getDictionaryObject(name);
        if (v instanceof COSArray)
        {
            COSArray array = (COSArray) v;
            String[] names = new String[array.size()];
            for (int i = 0; i < array.size(); i++)
            {
                COSBase item = array.getObject(i);
                if (item instanceof COSName)
                {
                    names[i] = ((COSName) item).getName();
                }
            }
            return names;
        }
        if (v instanceof COSName)
        {
            return ((COSName) v).getName();
        }
        return defaultValue;
    }

    /**
     * Sets a name value.
     * 
     * @param name the attribute name
     * @param value the name value
     */
    protected void setName(String name, String value)
    {
        COSBase oldBase = this.getCOSDictionary().getDictionaryObject(name);
        this.getCOSDictionary().setName(name, value);
        COSBase newBase = this.getCOSDictionary().getDictionaryObject(name);
        this.potentiallyNotifyChanged(oldBase, newBase);
    }

    /**
     * Sets an array of name values.
     * 
     * @param name the attribute name
     * @param values the array of name values
     */
    protected void setArrayOfName(String name, String[] values)
    {
        COSBase oldBase = this.getCOSDictionary().getDictionaryObject(name);
        COSArray array = new COSArray();
        for (String value : values)
        {
            array.add(COSName.getPDFName(value));
        }
        this.getCOSDictionary().setItem(name, array);
        COSBase newBase = this.getCOSDictionary().getDictionaryObject(name);
        this.potentiallyNotifyChanged(oldBase, newBase);
    }

    /**
     * Gets a number or a name value.
     * 
     * @param name the attribute name
     * @param defaultValue the default name
     * @return a Float or a String
     */
    protected Object getNumberOrName(String name, String defaultValue)
    {
        COSBase value = this.getCOSDictionary().getDictionaryObject(name);
        if (value instanceof COSNumber)
        {
            return ((COSNumber) value).floatValue();
        }
        if (value instanceof COSName)
        {
            return ((COSName) value).getName();
        }
        return defaultValue;
    }

    /**
     * Gets an integer.
     * 
     * @param name the attribute name
     * @param defaultValue the default value
     * @return the integer
     */
    protected int getInteger(String name, int defaultValue)
    {
        return this.getCOSDictionary().getInt(name, defaultValue);
    }

    /**
     * Sets an integer.
     * 
     * @param name the attribute name
     * @param value the integer
     */
    protected void setInteger(String name, int value)
    {
        COSBase oldBase = this.getCOSDictionary().getDictionaryObject(name);
        this.getCOSDictionary().setInt(name, value);
        COSBase newBase = this.getCOSDictionary().getDictionaryObject(name);
        this.potentiallyNotifyChanged(oldBase, newBase);
    }

    /**
     * Gets a number value.
     * 
     * @param name the attribute name
     * @param defaultValue the default value
     * @return the number value
     */
    protected float getNumber(String name, float defaultValue)
    {
        return this.getCOSDictionary().getFloat(name, defaultValue);
    }

    /**
     * Gets a number value.
     * 
     * @param name the attribute name
     * @return the number value
     */
    protected float getNumber(String name)
    {
        return this.getCOSDictionary().getFloat(name);
    }

    /**
     * An "unspecified" default float value.
     */
    protected static final float UNSPECIFIED = -1.f;

    /**
     * Gets a number or an array of numbers.
     * 
     * @param name the attribute name
     * @param defaultValue the default value
     * @return a Float or an array of floats
     */
    protected Object getNumberOrArrayOfNumber(String name, float defaultValue)
    {
        COSBase v = this.getCOSDictionary().getDictionaryObject(name);
        if (v instanceof COSArray)
        {
            COSArray array = (COSArray) v;
            float[] values = new float[array.size()];
            for (int i = 0; i < array.size(); i++)
            {
                COSBase item = array.getObject(i);
                if (item instanceof COSNumber)
                {
                    values[i] = ((COSNumber) item).floatValue();
                }
            }
            return values;
        }
        if (v instanceof COSNumber)
        {
            return ((COSNumber) v).floatValue();
        }
        if (defaultValue == UNSPECIFIED)
        {
            return null;
        }
        return defaultValue;
    }

    /**
     * Sets a float number.
     * 
     * @param name the attribute name
     * @param value the float number
     */
    protected void setNumber(String name, float value)
    {
        COSBase oldBase = this.getCOSDictionary().getDictionaryObject(name);
        this.getCOSDictionary().setFloat(name, value);
        COSBase newBase = this.getCOSDictionary().getDictionaryObject(name);
        this.potentiallyNotifyChanged(oldBase, newBase);
    }

    /**
     * Sets an integer number.
     * 
     * @param name the attribute name
     * @param value the integer number
     */
    protected void setNumber(String name, int value)
    {
        COSBase oldBase = this.getCOSDictionary().getDictionaryObject(name);
        this.getCOSDictionary().setInt(name, value);
        COSBase newBase = this.getCOSDictionary().getDictionaryObject(name);
        this.potentiallyNotifyChanged(oldBase, newBase);
    }

    /**
     * Sets an array of float numbers.
     * 
     * @param name the attribute name
     * @param values the float numbers
     */
    protected void setArrayOfNumber(String name, float[] values)
    {
        COSArray array = new COSArray();
        for (int i = 0; i < values.length; i++)
        {
            array.add(new COSFloat(values[i]));
        }
        COSBase oldBase = this.getCOSDictionary().getDictionaryObject(name);
        this.getCOSDictionary().setItem(name, array);
        COSBase newBase = this.getCOSDictionary().getDictionaryObject(name);
        this.potentiallyNotifyChanged(oldBase, newBase);
    }

    /**
     * Gets a colour.
     * 
     * @param name the attribute name
     * @return the colour
     */
    protected PDGamma getColor(String name)
    {
        COSArray c = (COSArray) this.getCOSDictionary().getDictionaryObject(name);
        if (c != null)
        {
            return new PDGamma(c);
        }
        return null;
    }

    /**
     * Gets a single colour or four colours.
     * 
     * @param name the attribute name
     * @return the single ({@link PDGamma}) or a ({@link PDFourColours})
     */
    protected Object getColorOrFourColors(String name)
    {
        COSArray array =
            (COSArray) this.getCOSDictionary().getDictionaryObject(name);
        if (array == null)
        {
            return null;
        }
        if (array.size() == 3)
        {
            // only one colour
            return new PDGamma(array);
        }
        else if (array.size() == 4)
        {
            return new PDFourColours(array);
        }
        return null;
    }

    /**
     * Sets a colour.
     * 
     * @param name the attribute name
     * @param value the colour
     */
    protected void setColor(String name, PDGamma value)
    {
        COSBase oldValue = this.getCOSDictionary().getDictionaryObject(name);
        this.getCOSDictionary().setItem(name, value);
        COSBase newValue = value == null ? null : value.getCOSObject();
        this.potentiallyNotifyChanged(oldValue, newValue);
    }

    /**
     * Sets four colours.
     * 
     * @param name the attribute name
     * @param value the four colours
     */
    protected void setFourColors(String name, PDFourColours value)
    {
        COSBase oldValue = this.getCOSDictionary().getDictionaryObject(name);
        this.getCOSDictionary().setItem(name, value);
        COSBase newValue = value == null ? null : value.getCOSObject();
        this.potentiallyNotifyChanged(oldValue, newValue);
    }

}
