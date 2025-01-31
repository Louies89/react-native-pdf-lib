package com.hopding.pdflib.apache.pdfbox.pdmodel.font;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hopding.pdflib.apache.fontbox.ttf.CmapSubtable;
import com.hopding.pdflib.apache.fontbox.ttf.HeaderTable;
import com.hopding.pdflib.apache.fontbox.ttf.HorizontalHeaderTable;
import com.hopding.pdflib.apache.fontbox.ttf.OS2WindowsMetricsTable;
import com.hopding.pdflib.apache.fontbox.ttf.PostScriptTable;
import com.hopding.pdflib.apache.fontbox.ttf.TTFParser;
import com.hopding.pdflib.apache.fontbox.ttf.TTFSubsetter;
import com.hopding.pdflib.apache.fontbox.ttf.TrueTypeFont;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.io.IOUtils;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDRectangle;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDStream;

/**
 * Common functionality for embedding TrueType fonts.
 *
 * @author Ben Litchfield
 * @author John Hewson
 */
abstract class TrueTypeEmbedder implements Subsetter
{
	private static final int ITALIC = 1;
	private static final int OBLIQUE = 256;
	private static final String BASE25 = "BCDEFGHIJKLMNOPQRSTUVWXYZ";

	private final PDDocument document;
	protected TrueTypeFont ttf;
	protected PDFontDescriptor fontDescriptor;
	protected final CmapSubtable cmap;
	private final Set<Integer> subsetCodePoints = new HashSet<Integer>();
	private final boolean embedSubset;

	/**
	 * Creates a new TrueType font for embedding.
	 */
	TrueTypeEmbedder(PDDocument document, COSDictionary dict, InputStream ttfStream,
			boolean embedSubset) throws IOException
			{
		this.document = document;
		this.embedSubset = embedSubset;

		buildFontFile2(ttfStream);
		dict.setName(COSName.BASE_FONT, ttf.getName());

		// choose a Unicode "cmap"
		cmap = ttf.getUnicodeCmap();
			}

	public void buildFontFile2(InputStream ttfStream) throws IOException
	{
		PDStream stream = new PDStream(document, ttfStream, false);
		stream.getStream().setInt(COSName.LENGTH1, stream.getByteArray().length);
		stream.addCompression();

		// as the stream was closed within the PDStream constructor, we have to recreate it
		InputStream input = null;
		try
		{
			input = stream.createInputStream();
			ttf = new TTFParser().parseEmbedded(input);
			if (!isEmbeddingPermitted(ttf))
			{
				throw new IOException("This font does not permit embedding");
			}
			if (fontDescriptor == null)
			{
				fontDescriptor = createFontDescriptor(ttf);
			}
		}
		finally
		{
			IOUtils.closeQuietly(input);
		}

		fontDescriptor.setFontFile2(stream);
	}

	/**
	 * Returns true if the fsType in the OS/2 table permits embedding.
     */
    private boolean isEmbeddingPermitted(TrueTypeFont ttf) throws IOException
    {
    	if (ttf.getOS2Windows() != null)
    	{
    		int fsType = ttf.getOS2Windows().getFsType();
    		int exclusive = fsType & 0x8; // bits 0-3 are a set of exclusive bits

    		if ((exclusive & OS2WindowsMetricsTable.FSTYPE_RESTRICTED) ==
    				OS2WindowsMetricsTable.FSTYPE_RESTRICTED)
    		{
    			// restricted License embedding
    			return false;
    		}
    		else if ((exclusive & OS2WindowsMetricsTable.FSTYPE_BITMAP_ONLY) ==
    				OS2WindowsMetricsTable.FSTYPE_BITMAP_ONLY)
    		{
    			// bitmap embedding only
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * Returns true if the fsType in the OS/2 table permits subsetting.
     */
    private boolean isSubsettingPermitted(TrueTypeFont ttf) throws IOException
    {
    	if (ttf.getOS2Windows() != null)
    	{
    		int fsType = ttf.getOS2Windows().getFsType();
    		if ((fsType & OS2WindowsMetricsTable.FSTYPE_NO_SUBSETTING) ==
    				OS2WindowsMetricsTable.FSTYPE_NO_SUBSETTING)
    		{
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * Creates a new font descriptor dictionary for the given TTF.
     */
    private PDFontDescriptor createFontDescriptor(TrueTypeFont ttf) throws IOException
    {
        PDFontDescriptor fd = new PDFontDescriptor();
        fd.setFontName(ttf.getName());

        OS2WindowsMetricsTable os2 = ttf.getOS2Windows();
        PostScriptTable post = ttf.getPostScript();

        // Flags
        fd.setFixedPitch(post.getIsFixedPitch() > 0 ||
                         ttf.getHorizontalHeader().getNumberOfHMetrics() == 1);

        int fsSelection = os2.getFsSelection();
        fd.setItalic((fsSelection & ITALIC) == fsSelection ||
                     (fsSelection & OBLIQUE) == fsSelection);

        switch (os2.getFamilyClass())
        {
            case OS2WindowsMetricsTable.FAMILY_CLASS_CLAREDON_SERIFS:
            case OS2WindowsMetricsTable.FAMILY_CLASS_FREEFORM_SERIFS:
            case OS2WindowsMetricsTable.FAMILY_CLASS_MODERN_SERIFS:
            case OS2WindowsMetricsTable.FAMILY_CLASS_OLDSTYLE_SERIFS:
            case OS2WindowsMetricsTable.FAMILY_CLASS_SLAB_SERIFS:
                fd.setSerif(true);
                break;
            case OS2WindowsMetricsTable.FAMILY_CLASS_SCRIPTS:
                fd.setScript(true);
                break;
        }

        fd.setFontWeight(os2.getWeightClass());

        fd.setSymbolic(true);
        fd.setNonSymbolic(false);

        // ItalicAngle
        fd.setItalicAngle(post.getItalicAngle());

        // FontBBox
        HeaderTable header = ttf.getHeader();
        PDRectangle rect = new PDRectangle();
        float scaling = 1000f / header.getUnitsPerEm();
        rect.setLowerLeftX(header.getXMin() * scaling);
        rect.setLowerLeftY(header.getYMin() * scaling);
        rect.setUpperRightX(header.getXMax() * scaling);
        rect.setUpperRightY(header.getYMax() * scaling);
        fd.setFontBoundingBox(rect);

        // Ascent, Descent
        HorizontalHeaderTable hHeader = ttf.getHorizontalHeader();
        fd.setAscent(hHeader.getAscender() * scaling);
        fd.setDescent(hHeader.getDescender() * scaling);

        // CapHeight, XHeight
        if (os2.getVersion() >= 1.2)
        {
            fd.setCapHeight(os2.getCapHeight() / scaling);
            fd.setXHeight(os2.getHeight() / scaling);
        }
        else
        {
            // estimate by summing the typographical +ve ascender and -ve descender
            fd.setCapHeight((os2.getTypoAscender() + os2.getTypoDescender()) / scaling);

            // estimate by halving the typographical ascender
            fd.setXHeight((os2.getTypoAscender() / 2.0f) / scaling);
        }

        // StemV - there's no true TTF equivalent of this, so we estimate it
        fd.setStemV(fd.getFontBoundingBox().getWidth() * .13f);

        return fd;
    }

    /**
     * Returns the FontBox font.
     */
    public TrueTypeFont getTrueTypeFont()
    {
        return ttf;
    }

    /**
     * Returns the font descriptor.
     */
    public PDFontDescriptor getFontDescriptor()
    {
    	return fontDescriptor;
    }

    @Override
    public void addToSubset(int codePoint)
    {
    	subsetCodePoints.add(codePoint);
    }

    @Override
    public void subset() throws IOException
    {
    	if (!isSubsettingPermitted(ttf))
    	{
    		throw new IOException("This font does not permit subsetting");
    	}
    	
    	if (!embedSubset)
    	{
    		throw new IllegalStateException("Subsetting is disabled");
    	}

    	// PDF spec required tables (if present), all others will be removed
    	List<String> tables = new ArrayList<String>();
    	tables.add("head");
    	tables.add("hhea");
    	tables.add("loca");
    	tables.add("maxp");
    	tables.add("cvt ");
    	tables.add("prep");
    	tables.add("glyf");
    	tables.add("hmtx");
    	tables.add("fpgm");
    	// Windows ClearType
    	tables.add("gasp");

    	// set the GIDs to subset
    	TTFSubsetter subsetter = new TTFSubsetter(getTrueTypeFont(), tables);
    	subsetter.addAll(subsetCodePoints);

    	// calculate deterministic tag based on the chosen subset
    	Map<Integer, Integer> gidToCid = subsetter.getGIDMap();
    	String tag = getTag(gidToCid);
    	subsetter.setPrefix(tag);

    	// save the subset font
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	subsetter.writeToStream(out);

    	// re-build the embedded font
    	buildSubset(new ByteArrayInputStream(out.toByteArray()), tag, gidToCid);
    }
    
    /**
     * Returns true if the font needs to be subset.
     */
    public boolean needsSubset()
    {
    	return embedSubset;
    }

    /**
     * Rebuild a font subset.
     */
    protected abstract void buildSubset(InputStream ttfSubset, String tag,
    		Map<Integer, Integer> gidToCid) throws IOException;

    /**
     * Returns an uppercase 6-character unique tag for the given subset.
     */
    public String getTag(Map<Integer, Integer> gidToCid)
    {
    	// deterministic
    	long num = gidToCid.hashCode();

    	// base25 encode
    	StringBuilder sb = new StringBuilder();
    	do
    	{
    		long div = num / 25;
    		int mod = (int)(num % 25);
    		sb.append(BASE25.charAt(mod));
    		num = div;
    	} while (num != 0 && sb.length() < 6);

    	// pad
    	while (sb.length() < 6)
    	{
    		sb.insert(0, 'A');
    	}

    	sb.append('+');
    	return sb.toString();
    }
}