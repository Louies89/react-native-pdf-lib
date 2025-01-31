package com.hopding.pdflib.apache.pdfbox.pdmodel.font.encoding;

import com.hopding.pdflib.apache.fontbox.afm.CharMetric;
import com.hopding.pdflib.apache.fontbox.afm.FontMetrics;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;

import java.util.Map;

/**
 * An encoding for a Type 1 font.
 */
public class Type1Encoding extends Encoding
{
    /**
     * Creates an encoding from the given FontBox encoding.
     *
     * @param encoding FontBox encoding
     */
    public static Type1Encoding fromFontBox(com.hopding.pdflib.apache.fontbox.encoding.Encoding encoding)
    {
        // todo: could optimise this by looking for specific subclasses
        Map<Integer,String> codeToName = encoding.getCodeToNameMap();
        Type1Encoding enc = new Type1Encoding();

        for (Map.Entry<Integer, String> entry : codeToName.entrySet())
        {
        	enc.add(entry.getKey(), entry.getValue());
        }

        return enc;
    }

    /**
     * Creates an empty encoding.
     */
    public Type1Encoding()
    {
    }

    /**
     * Creates an encoding from the given AFM font metrics.
     *
     * @param fontMetrics AFM font metrics.
     */
    public Type1Encoding(FontMetrics fontMetrics)
    {
        for (CharMetric nextMetric : fontMetrics.getCharMetrics())
        {
            add(nextMetric.getCharacterCode(), nextMetric.getName());
        }
    }

    @Override
    public COSBase getCOSObject()
    {
        return null;
    }
}
