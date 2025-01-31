package com.hopding.pdflib.apache.pdfbox.pdmodel.common;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;

/**
 * Contains information for a page label range.
 * 
 * @author Igor Podolskiy
 * 
 * @see PDPageLabels
 */
public class PDPageLabelRange implements COSObjectable
{

    private COSDictionary root;

    // Page label dictonary (PDF32000-1:2008 Section 12.4.2, Table 159)
    private static final COSName KEY_START = COSName.ST;
    private static final COSName KEY_PREFIX = COSName.P;
    private static final COSName KEY_STYLE = COSName.S;

    // Style entry values (PDF32000-1:2008 Section 12.4.2, Table 159)

    /**
     * Decimal page numbering style (1, 2, 3, ...).
     */
    public static final String STYLE_DECIMAL = "D";

    /**
     * Roman numbers (upper case) numbering style (I, II, III, IV, ...).
     */
    public static final String STYLE_ROMAN_UPPER = "R";

    /**
     * Roman numbers (lower case) numbering style (i, ii, iii, iv, ...).
     */
    public static final String STYLE_ROMAN_LOWER = "r";

    /**
     * Letter (upper case) numbering style (A, B, ..., Z, AA, BB, ..., ZZ, AAA,
     * ...).
     */
    public static final String STYLE_LETTERS_UPPER = "A";

    /**
     * Letter (lower case) numbering style (a, b, ..., z, aa, bb, ..., zz, aaa,
     * ...).
     */
    public static final String STYLE_LETTERS_LOWER = "a";

    /**
     * Creates a new empty page label range object.
     */
    public PDPageLabelRange()
    {
        this(new COSDictionary());
    }

    /**
     * Creates a new page label range object from the given dictionary.
     * 
     * @param dict
     *            the base dictionary for the new object.
     */
    public PDPageLabelRange(COSDictionary dict)
    {
        root = dict;
    }

    /**
     * Returns the underlying dictionary.
     * 
     * @return the underlying dictionary.
     */
    public COSDictionary getCOSDictionary()
    {
        return root;
    }

    @Override
    public COSBase getCOSObject()
    {
        return root;
    }

    /**
     * Returns the numbering style for this page range.
     * 
     * @return one of the STYLE_* constants
     */
    public String getStyle()
    {
        return root.getNameAsString(KEY_STYLE);
    }

    /**
     * Sets the numbering style for this page range.
     * 
     * @param style
     *            one of the STYLE_* constants or {@code null} if no page
     *            numbering is desired.
     */
    public void setStyle(String style)
    {
        if (style != null)
        {
            root.setName(KEY_STYLE, style);
        }
        else
        {
            root.removeItem(KEY_STYLE);
        }
    }

    /**
     * Returns the start value for page numbering in this page range.
     * 
     * @return a positive integer the start value for numbering.
     */
    public int getStart()
    {
        return root.getInt(KEY_START, 1);
    }

    /**
     * Sets the start value for page numbering in this page range.
     * 
     * @param start
     *            a positive integer representing the start value.
     * @throws IllegalArgumentException
     *             if {@code start} is not a positive integer
     */
    public void setStart(int start)
    {
        if (start <= 0)
        {
            throw new IllegalArgumentException(
                    "The page numbering start value must be a positive integer");
        }
        root.setInt(KEY_START, start);
    }

    /**
     * Returns the page label prefix for this page range.
     * 
     * @return the page label prefix for this page range, or {@code null} if no
     *         prefix has been defined.
     */
    public String getPrefix()
    {
        return root.getString(KEY_PREFIX);
    }

    /**
     * Sets the page label prefix for this page range.
     * 
     * @param prefix
     *            the page label prefix for this page range, or {@code null} to
     *            unset the prefix.
     */
    public void setPrefix(String prefix)
    {
        if (prefix != null)
        {
            root.setString(KEY_PREFIX, prefix);
        }
        else
        {
            root.removeItem(KEY_PREFIX);
        }
    }
}
