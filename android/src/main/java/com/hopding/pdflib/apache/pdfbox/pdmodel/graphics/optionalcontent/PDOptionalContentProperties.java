package com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.optionalcontent;

import java.util.Collection;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSObject;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;

/**
 * This class represents the optional content properties dictionary.
 *
 * @since PDF 1.5
 */
public class PDOptionalContentProperties implements COSObjectable
{

    /**
     * Enumeration for the BaseState dictionary entry on the "D" dictionary.
     */
    public static enum BaseState
    {

        /** The "ON" value. */
        ON(COSName.ON),
        /** The "OFF" value. */
        OFF(COSName.OFF),
        /** The "Unchanged" value. */
        UNCHANGED(COSName.UNCHANGED);

        private COSName name;

        private BaseState(COSName value)
        {
            this.name = value;
        }

        /**
         * Returns the PDF name for the state.
         * @return the name of the state
         */
        public COSName getName()
        {
            return this.name;
        }

        /**
         * Returns the base state represented by the given {@link COSName}.
         * @param state the state name
         * @return the state enum value
         */
        public static BaseState valueOf(COSName state)
        {
            if (state == null)
            {
                return BaseState.ON;
            }
            return BaseState.valueOf(state.getName().toUpperCase());
        }

    }

    private COSDictionary dict;

    /**
     * Creates a new optional content properties dictionary.
     */
    public PDOptionalContentProperties()
    {
        this.dict = new COSDictionary();
        this.dict.setItem(COSName.OCGS, new COSArray());
        this.dict.setItem(COSName.D, new COSDictionary());
    }

    /**
     * Creates a new instance based on a given {@link COSDictionary}.
     * @param props the dictionary
     */
    public PDOptionalContentProperties(COSDictionary props)
    {
        this.dict = props;
    }

    /** {@inheritDoc} */
    public COSBase getCOSObject()
    {
        return this.dict;
    }

    private COSArray getOCGs()
    {
        COSArray ocgs = (COSArray)this.dict.getItem(COSName.OCGS);
        if (ocgs == null)
        {
            ocgs = new COSArray();
            this.dict.setItem(COSName.OCGS, ocgs); //OCGs is required
        }
        return ocgs;
    }

    private COSDictionary getD()
    {
        COSDictionary d = (COSDictionary)this.dict.getDictionaryObject(COSName.D);
        if (d == null)
        {
            d = new COSDictionary();
            this.dict.setItem(COSName.D, d); //D is required
        }
        return d;
    }

    /**
     * Returns the optional content group of the given name.
     * @param name the group name
     * @return the optional content group or null, if there is no such group
     */
    public PDOptionalContentGroup getGroup(String name)
    {
        COSArray ocgs = getOCGs();
        for (COSBase o : ocgs)
        {
            COSDictionary ocg = toDictionary(o);
            String groupName = ocg.getString(COSName.NAME);
            if (groupName.equals(name))
            {
                return new PDOptionalContentGroup(ocg);
            }
        }
        return null;
    }

    /**
     * Adds an optional content group (OCG).
     * @param ocg the optional content group
     */
    public void addGroup(PDOptionalContentGroup ocg)
    {
        COSArray ocgs = getOCGs();
        ocgs.add(ocg.getCOSObject());

        //By default, add new group to the "Order" entry so it appears in the user interface
        COSArray order = (COSArray)getD().getDictionaryObject(COSName.ORDER);
        if (order == null)
        {
            order = new COSArray();
            getD().setItem(COSName.ORDER, order);
        }
        order.add(ocg);
    }

    /**
     * Returns the collection of all optional content groups.
     * @return the optional content groups
     */
    public Collection<PDOptionalContentGroup> getOptionalContentGroups()
    {
        Collection<PDOptionalContentGroup> coll = new java.util.ArrayList<PDOptionalContentGroup>();
        COSArray ocgs = getOCGs();
        for (COSBase base : ocgs)
        {
            COSObject obj = (COSObject)base; //Children must be indirect references
            coll.add(new PDOptionalContentGroup((COSDictionary)obj.getObject()));
        }
        return coll;
    }

    /**
     * Returns the base state for optional content groups.
     * @return the base state
     */
    public BaseState getBaseState()
    {
        COSDictionary d = getD();
        COSName name = (COSName)d.getItem(COSName.BASE_STATE);
        return BaseState.valueOf(name);
    }

    /**
     * Sets the base state for optional content groups.
     * @param state the base state
     */
    public void setBaseState(BaseState state)
    {
        COSDictionary d = getD();
        d.setItem(COSName.BASE_STATE, state.getName());
    }

    /**
     * Lists all optional content group names.
     * @return an array of all names
     */
    public String[] getGroupNames()
    {
        COSArray ocgs = (COSArray)dict.getDictionaryObject(COSName.OCGS);
        int size = ocgs.size();
        String[] groups = new String[size];
        for (int i = 0; i < size; i++)
        {
            COSBase obj = (COSBase)ocgs.get(i);
            COSDictionary ocg = toDictionary(obj);
            groups[i] = ocg.getString(COSName.NAME);
        }
        return groups;
    }

    /**
     * Indicates whether a particular optional content group is found in the PDF file.
     * @param groupName the group name
     * @return true if the group exists, false otherwise
     */
    public boolean hasGroup(String groupName)
    {
        String[] layers = getGroupNames();
        for (String layer : layers)
        {
            if (layer.equals(groupName))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Indicates whether an optional content group is enabled.
     * @param groupName the group name
     * @return true if the group is enabled
     */
    public boolean isGroupEnabled(String groupName)
    {
        //TODO handle Optional Content Configuration Dictionaries,
        //i.e. OCProperties/Configs

        COSDictionary d = getD();
        COSArray on = (COSArray)d.getDictionaryObject(COSName.ON);
        if (on != null)
        {
            for (COSBase o : on)
            {
                COSDictionary group = toDictionary(o);
                String name = group.getString(COSName.NAME);
                if (name.equals(groupName))
                {
                    return true;
                }
            }
        }

        COSArray off = (COSArray)d.getDictionaryObject(COSName.OFF);
        if (off != null)
        {
            for (COSBase o : off)
            {
                COSDictionary group = toDictionary(o);
                String name = group.getString(COSName.NAME);
                if (name.equals(groupName))
                {
                    return false;
                }
            }
        }

        BaseState baseState = getBaseState();
        boolean enabled = !baseState.equals(BaseState.OFF);
        //TODO What to do with BaseState.Unchanged?
        return enabled;
    }

    private COSDictionary toDictionary(COSBase o)
    {
        if (o instanceof COSObject)
        {
            return (COSDictionary)((COSObject)o).getObject();
        }
        else
        {
            return (COSDictionary)o;
        }
    }

    /**
     * Enables or disables an optional content group.
     * @param groupName the group name
     * @param enable true to enable, false to disable
     * @return true if the group already had an on or off setting, false otherwise
     */
    public boolean setGroupEnabled(String groupName, boolean enable)
    {
        COSDictionary d = getD();
        COSArray on = (COSArray)d.getDictionaryObject(COSName.ON);
        if (on == null)
        {
            on = new COSArray();
            d.setItem(COSName.ON, on);
        }
        COSArray off = (COSArray)d.getDictionaryObject(COSName.OFF);
        if (off == null)
        {
            off = new COSArray();
            d.setItem(COSName.OFF, off);
        }

        boolean found = false;
        if (enable)
        {
            for (COSBase o : off)
            {
                COSDictionary group = toDictionary(o);
                String name = group.getString(COSName.NAME);
                if (name.equals(groupName))
                {
                    //enable group
                    off.remove(group);
                    on.add(group);
                    found = true;
                    break;
                }
            }
        }
        else
        {
            for (COSBase o : on)
            {
                COSDictionary group = toDictionary(o);
                String name = group.getString(COSName.NAME);
                if (name.equals(groupName))
                {
                    //disable group
                    on.remove(group);
                    off.add(group);
                    found = true;
                    break;
                }
            }
        }
        if (!found)
        {
            PDOptionalContentGroup ocg = getGroup(groupName);
            if (enable)
            {
                on.add(ocg.getCOSObject());
            }
            else
            {
                off.add(ocg.getCOSObject());
            }
        }
        return found;
    }


}
