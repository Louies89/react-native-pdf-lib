package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSString;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSArrayList;

/**
 * A button field represents an interactive control on the screen
 * that the user can manipulate with the mouse.
 *
 * @author sug
 */
public abstract class PDButton extends PDField
{
	/**
	 * The value for the Off state for PDCheckbox and PDRadioButton.
	 * 
	 * This shall not be confused with the OFF state as it is used within
	 * other parts of a PDF. 
	 */
	static final COSName OFF = COSName.getPDFName("Off");
	
    /**
     * A Ff flag. If set, the field is a set of radio buttons
     */
    public static final int FLAG_RADIO = 1 << 15;
    /**
     * A Ff flag. If set, the field is a pushbutton.
     */
    public static final int FLAG_PUSHBUTTON = 1 << 16;
    /**
     * A Ff flag. If set, radio buttons individual fields, using the same
     * value for the on state will turn on and off in unison.
     */
    public static final int FLAG_RADIOS_IN_UNISON = 1 << 25;
    
    /**
     * @see PDFieldTreeNode#PDFieldTreeNode(PDAcroForm)
     *
     * @param theAcroForm The acroform.
     */
    public PDButton(PDAcroForm theAcroForm)
    {
    	super( theAcroForm );
    	getDictionary().setItem(COSName.FT, COSName.BTN);
    }

    /**
     * Constructor.
     * 
     * @param acroForm The form that this field is part of.
     * @param field the PDF object to represent as a field.
     * @param parentNode the parent node of the node to be created
     */
    public PDButton(PDAcroForm acroForm, COSDictionary field, PDFieldTreeNode parentNode)
    {
        super(acroForm, field, parentNode);
    }
    
    /**
     * Determines if push button bit is set.
     * 
     * @return true if type of button field is a push button.
     */
    public boolean isPushButton()
    {
    	return getDictionary().getFlag( COSName.FF, FLAG_PUSHBUTTON );
    }

    /**
     * Set the push button bit.
     *
     * @param pushbutton if true the button field is treated as a push button field.
     */
    public void setPushButton( boolean pushbutton )
    {
    	getDictionary().setFlag( COSName.FF, FLAG_PUSHBUTTON, pushbutton );
    }

    /**
     * Determines if radio button bit is set.
     * 
     * @return true if type of button field is a push button.
     */
    public boolean isRadioButton()
    {
    	return getDictionary().getFlag( COSName.FF, FLAG_RADIO );
    }

    /**
     * Set the radio button bit.
     *
     * @param radiobutton if true the button field is treated as a radio button field.
     */
    public void setRadioButton( boolean radiobutton )
    {
    	getDictionary().setFlag( COSName.FF, FLAG_RADIO, radiobutton );
    }
    
    @Override
    public String getDefaultValue() throws IOException
    {
    	COSBase attribute = getInheritableAttribute(COSName.DV);

    	if (attribute == null)
    	{
    		return "";
    	}
    	else if (attribute instanceof COSName)
    	{
    		return ((COSName) attribute).getName();
    	}
    	else
    	{
    		throw new IOException("Expected a COSName entry but got " + attribute.getClass().getName());
    	}
    }

    /**
     * Set the fields default value.
     *
     * The field value holds a name object which is corresponding to the
     * appearance state representing the corresponding appearance
     * from the appearance directory.
     *
     * The default value is used to represent the initial state of the
     * field or to revert when resetting the form.
     *
     * @param defaultValue the new field value.
     */
    @Override
    public void setDefaultValue(String defaultValue)
    {
    	if (defaultValue == null)
    	{
    		getDictionary().removeItem(COSName.DV);
    	}
    	else
    	{
    		getDictionary().setItem(COSName.DV, COSName.getPDFName(defaultValue));
    	}
    }

    /**
     * This will get the option values - the "Opt" entry.
     *
     * <p>The option values are used to define the export values
     * for the field to
     * <ul>
     * <li>hold values in non-Latin writing systems as name objects, which represent the field value, are limited
     * to PDFDocEncoding
     * </li>
     * <li>allow radio buttons having the same export value to be handled independently
     * </li>
     * </ul>
     * </p>
     *
     * @return List containing all possible options. If there is no Opt entry an empty list will be returned.
     *
     * @return A list of java.lang.String values.
     */
    public List<String> getOptions()
    {
    	COSBase value = getInheritableAttribute(COSName.OPT);
    	if (value instanceof COSString)
    	{
    		List<String> array = new ArrayList<String>();
    		array.add(((COSString) value).getString());
    		return array;
    	}
    	else if (value instanceof COSArray)
    	{
    		return COSArrayList.convertCOSStringCOSArrayToList((COSArray)value);
        }
    	return Collections.<String>emptyList();
    }

    /**
     * This will set the options.
     *
     * @see #getOptions()
     * @param values List containing all possible options. Supplying null or an empty list will remove the Opt entry.
     */
    public void setOptions(List<String> values)
    {
    	if (values == null || values.isEmpty())
    	{
    		removeInheritableAttribute(COSName.OPT);
    	}
    	else
    	{
    		setInheritableAttribute(COSName.OPT, COSArrayList.convertStringListToCOSStringCOSArray(values));
    	}
    }
}