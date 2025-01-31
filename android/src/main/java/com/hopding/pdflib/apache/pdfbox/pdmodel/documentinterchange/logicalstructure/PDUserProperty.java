package com.hopding.pdflib.apache.pdfbox.pdmodel.documentinterchange.logicalstructure;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDDictionaryWrapper;

/**
 * A user property.
 * 
 * @author Johannes Koch
 */
public class PDUserProperty extends PDDictionaryWrapper
{

    private final PDUserAttributeObject userAttributeObject;

    /**
     * Creates a new user property.
     * 
     * @param userAttributeObject the user attribute object
     */
    public PDUserProperty(PDUserAttributeObject userAttributeObject)
    {
        this.userAttributeObject = userAttributeObject;
    }

    /**
     * Creates a user property with a given dictionary.
     * 
     * @param dictionary the dictionary
     * @param userAttributeObject the user attribute object
     */
    public PDUserProperty(COSDictionary dictionary,
        PDUserAttributeObject userAttributeObject)
    {
        super(dictionary);
        this.userAttributeObject = userAttributeObject;
    }


    /**
     * Returns the property name.
     * 
     * @return the property name
     */
    public String getName()
    {
        return this.getCOSDictionary().getNameAsString(COSName.N);
    }

    /**
     * Sets the property name.
     * 
     * @param name the property name
     */
    public void setName(String name)
    {
        this.potentiallyNotifyChanged(this.getName(), name);
        this.getCOSDictionary().setName(COSName.N, name);
    }

    /**
     * Returns the property value.
     * 
     * @return the property value
     */
    public COSBase getValue()
    {
        return this.getCOSDictionary().getDictionaryObject(COSName.V);
    }

    /**
     * Sets the property value.
     * 
     * @param value the property value
     */
    public void setValue(COSBase value)
    {
        this.potentiallyNotifyChanged(this.getValue(), value);
        this.getCOSDictionary().setItem(COSName.V, value);
    }

    /**
     * Returns the string for the property value.
     * 
     * @return the string for the property value
     */
    public String getFormattedValue()
    {
        return this.getCOSDictionary().getString(COSName.F);
    }

    /**
     * Sets the string for the property value.
     * 
     * @param formattedValue the string for the property value
     */
    public void setFormattedValue(String formattedValue)
    {
        this.potentiallyNotifyChanged(this.getFormattedValue(), formattedValue);
        this.getCOSDictionary().setString(COSName.F, formattedValue);
    }

    /**
     * Shall the property be hidden?
     * 
     * @return <code>true</code> if the property shall be hidden,
     * <code>false</code> otherwise
     */
    public boolean isHidden()
    {
        return this.getCOSDictionary().getBoolean(COSName.H, false);
    }

    /**
     * Specifies whether the property shall be hidden.
     * 
     * @param hidden <code>true</code> if the property shall be hidden,
     * <code>false</code> otherwise
     */
    public void setHidden(boolean hidden)
    {
        this.potentiallyNotifyChanged(this.isHidden(), hidden);
        this.getCOSDictionary().setBoolean(COSName.H, hidden);
    }


    @Override
    public String toString()
    {
        return new StringBuilder("Name=").append(this.getName())
            .append(", Value=").append(this.getValue())
            .append(", FormattedValue=").append(this.getFormattedValue())
            .append(", Hidden=").append(this.isHidden()).toString();
    }


    /**
     * Notifies the user attribute object if the user property is changed.
     * 
     * @param oldEntry old entry
     * @param newEntry new entry
     */
    private void potentiallyNotifyChanged(Object oldEntry, Object newEntry)
    {
        if (PDUserProperty.isEntryChanged(oldEntry, newEntry))
        {
            this.userAttributeObject.userPropertyChanged(this);
        }
    }

    /**
     * Is the value changed?
     * 
     * @param oldEntry old entry
     * @param newEntry new entry
     * @return <code>true</code> if the entry is changed, <code>false</code>
     * otherwise
     */
    private static boolean isEntryChanged(Object oldEntry, Object newEntry)
    {
        if (oldEntry == null)
        {
            return newEntry != null;
        }
        return !oldEntry.equals(newEntry);
    }

    @Override
    public int hashCode()
    {
    	final int prime = 31;
    	int result = super.hashCode();
    	result = prime * result
    			+ ((userAttributeObject == null) ? 0 : userAttributeObject.hashCode());
    	return result;
    }

    @Override
    public boolean equals(Object obj)
    {
    	if (this == obj)
    	{
    		return true;
    	}
    	if (!super.equals(obj))
    	{
    		return false;
    	}
    	if (getClass() != obj.getClass())
    	{
    		return false;
    	}
    	PDUserProperty other = (PDUserProperty) obj;
    	if (userAttributeObject == null)
    	{
    		if (other.userAttributeObject != null)
    			return false;
    	}
    	else if (!userAttributeObject.equals(other.userAttributeObject))
    	{
    		return false;
    	}
    	return true;
    }
}
