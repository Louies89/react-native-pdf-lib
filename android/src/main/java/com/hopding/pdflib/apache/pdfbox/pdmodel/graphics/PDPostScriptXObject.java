package com.hopding.pdflib.apache.pdfbox.pdmodel.graphics;

import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDStream;

/**
 * A PostScript XObject.
 * Conforming readers may not be able to interpret the PostScript fragments.
 *
 * @author John Hewson
 */
public class PDPostScriptXObject extends PDXObject
{
    /**
     * Creates a PostScript XObject.
     * @param stream The XObject stream
     */
    public PDPostScriptXObject(PDStream stream)
    {
        super(stream, COSName.PS);
    }
}
