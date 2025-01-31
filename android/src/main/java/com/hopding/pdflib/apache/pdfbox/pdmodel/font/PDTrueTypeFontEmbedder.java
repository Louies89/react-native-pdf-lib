package com.hopding.pdflib.apache.pdfbox.pdmodel.font;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.hopding.pdflib.apache.fontbox.ttf.HorizontalMetricsTable;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSArrayList;
import com.hopding.pdflib.apache.pdfbox.pdmodel.font.encoding.Encoding;
import com.hopding.pdflib.apache.pdfbox.pdmodel.font.encoding.GlyphList;
import com.hopding.pdflib.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;

/**
 * Embedded PDTrueTypeFont builder. Helper class to populate a PDTrueTypeFont from a TTF.
 *
 * @author John Hewson
 * @author Ben Litchfield
 */
final class PDTrueTypeFontEmbedder extends TrueTypeEmbedder
{
    private final Encoding fontEncoding;

    /**
     * Creates a new TrueType font embedder for the given TTF as a PDTrueTypeFont.
     *
     * @param document parent document
     * @param dict font dictionary
     * @param ttfStream TTF stream
     * @throws IOException if the TTF could not be read
     */
    PDTrueTypeFontEmbedder(PDDocument document, COSDictionary dict, InputStream ttfStream)
            throws IOException
    {
        super(document, dict, ttfStream, false);
        dict.setItem(COSName.SUBTYPE, COSName.TRUE_TYPE);

        // only support WinAnsiEncoding encoding right now
        Encoding encoding = new WinAnsiEncoding();
        GlyphList glyphList = GlyphList.getAdobeGlyphList();
        this.fontEncoding = encoding;
        dict.setItem(COSName.ENCODING, encoding.getCOSObject());
        
        fontDescriptor.setSymbolic(false);
        fontDescriptor.setNonSymbolic(true);

        // add the font descriptor
        dict.setItem(COSName.FONT_DESC, fontDescriptor);

        // set the glyph widths
        setWidths(dict, glyphList);
    }

    /**
     * Sets the glyph widths in the font dictionary.
     */
    private void setWidths(COSDictionary font, GlyphList glyphList) throws IOException
    {
        float scaling = 1000f / ttf.getHeader().getUnitsPerEm();
        HorizontalMetricsTable hmtx = ttf.getHorizontalMetrics();

        Map<Integer, String> codeToName = getFontEncoding().getCodeToNameMap();

        int firstChar = Collections.min(codeToName.keySet());
        int lastChar = Collections.max(codeToName.keySet());

        List<Integer> widths = new ArrayList<Integer>(lastChar - firstChar + 1);
        for (int i = 0; i < lastChar - firstChar + 1; i++)
        {
            widths.add(0);
        }

        // a character code is mapped to a glyph name via the provided font encoding
        // afterwards, the glyph name is translated to a glyph ID.
        for (Map.Entry<Integer, String> entry : codeToName.entrySet())
        {
        	int code = entry.getKey();
        	String name = entry.getValue();
        	
        	if (code >= firstChar && code <= lastChar)
            {
                // todo: we're supposed to use the 'provided font encoding'
            	String uni = glyphList.toUnicode(name);
                int charCode = uni.codePointAt(0);
                int gid = cmap.getGlyphId(charCode);
                widths.set(entry.getKey() - firstChar,
                		Math.round(hmtx.getAdvanceWidth(gid) * scaling));
            }
        }

        font.setInt(COSName.FIRST_CHAR, firstChar);
        font.setInt(COSName.LAST_CHAR, lastChar);
        font.setItem(COSName.WIDTHS, COSArrayList.converterToCOSArray(widths));
    }

    /**
     * Returns the font's encoding.
     */
    public Encoding getFontEncoding()
    {
    	return fontEncoding;
    }

    @Override
    protected void buildSubset(InputStream ttfSubset, String tag,
    		Map<Integer, Integer> gidToCid) throws IOException
    {
    	// use PDType0Font instead
    	throw new UnsupportedOperationException();
    }
}