package com.hopding.pdflib.apache.pdfbox.pdmodel.documentinterchange.taggedpdf;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;
/**
 * The standard structure types.
 * 
 * @author Johannes Koch
 */
public class StandardStructureTypes
{   
    private StandardStructureTypes()
    {
    }


    // Grouping Elements
    /**
     * Document
     */
    public static final String DOCUMENT = "Document";

    /**
     * Part
     */
    public static final String PART = "Part";

    /**
     * Art
     */
    public static final String ART = "Art";

    /**
     * Sect
     */
    public static final String SECT = "Sect";

    /**
     * Div
     */
    public static final String DIV = "Div";

    /**
     * BlockQuote
     */
    public static final String BLOCK_QUOTE = "BlockQuote";

    /**
     * Caption
     */
    public static final String CAPTION = "Caption";

    /**
     * TOC
     */
    public static final String TOC = "TOC";

    /**
     * TOCI
     */
    public static final String TOCI = "TOCI";

    /**
     * Index
     */
    public static final String INDEX = "Index";

    /**
     * NonStruct
     */
    public static final String NON_STRUCT = "NonStruct";

    /**
     * Private
     */
    public static final String PRIVATE = "Private";


    // Block-Level Structure Elements
    /**
     * P
     */
    public static final String P = "P";

    /**
     * H
     */
    public static final String H = "H";

    /**
     * H1
     */
    public static final String H1 = "H1";

    /**
     * H2
     */
    public static final String H2 = "H2";

    /**
     * H3
     */
    public static final String H3 = "H3";

    /**
     * H4
     */
    public static final String H4 = "H4";

    /**
     * H5
     */
    public static final String H5 = "H5";

    /**
     * H6
     */
    public static final String H6 = "H6";

    /**
     * L
     */
    public static final String L = "L";

    /**
     * LI
     */
    public static final String LI = "LI";

    /**
     * Lbl
     */
    public static final String LBL = "Lbl";

    /**
     * LBody
     */
    public static final String L_BODY = "LBody";

    /**
     * Table
     */
    public static final String TABLE = "Table";

    /**
     * TR
     */
    public static final String TR = "TR";

    /**
     * TH
     */
    public static final String TH = "TH";

    /**
     * TD
     */
    public static final String TD = "TD";

    /**
     * THead
     */
    public static final String T_HEAD = "THead";

    /**
     * TBody
     */
    public static final String T_BODY = "TBody";

    /**
     * TFoot
     */
    public static final String T_FOOT = "TFoot";


    // Inline-Level Structure Elements
    /**
     * Span
     */
    public static final String SPAN = "Span";

    /**
     * Quote
     */
    public static final String QUOTE = "Quote";

    /**
     * Note
     */
    public static final String NOTE = "Note";

    /**
     * Reference
     */
    public static final String REFERENCE = "Reference";

    /**
     * BibEntry
     */
    public static final String BIB_ENTRY = "BibEntry";

    /**
     * Code
     */
    public static final String CODE = "Code";

    /**
     * Link
     */
    public static final String LINK = "Link";

    /**
     * Annot
     */
    public static final String ANNOT = "Annot";

    /**
     * Ruby
     */
    public static final String RUBY = "Ruby";

    /**
     * RB
     */
    public static final String RB = "RB";

    /**
     * RT
     */
    public static final String RT = "RT";

    /**
     * RP
     */
    public static final String RP = "RP";

    /**
     * Warichu
     */
    public static final String WARICHU = "Warichu";

    /**
     * WT
     */
    public static final String WT = "WT";

    /**
     * WP
     */
    public static final String WP = "WP";


    // Illustration Elements
    /**
     * Figure
     */
    public static final String Figure = "Figure";

    /**
     * Formula
     */
    public static final String FORMULA = "Formula";

    /**
     * Form
     */
    public static final String FORM = "Form";

    /**
     * All standard structure types.
     */
    public static List<String> types = new ArrayList<String>();

    static
    {
        Field[] fields = StandardStructureTypes.class.getFields();
        for (Field field : fields)
        {
            if (Modifier.isFinal(field.getModifiers()))
            {
                try
                {
                    types.add(field.get(null).toString());
                }
                catch (IllegalArgumentException e)
                {
                	Log.e("PdfBoxAndroid", e.getMessage(),e);
                }
                catch (IllegalAccessException e)
                {
                	Log.e("PdfBoxAndroid", e.getMessage(),e);
                }
            }
        }
        Collections.sort(types);
    }

}
