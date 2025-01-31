package com.hopding.pdflib.apache.pdfbox.pdmodel.font;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hopding.pdflib.apache.fontbox.ttf.Type1Equivalent;
import com.hopding.pdflib.apache.fontbox.type1.DamagedFontException;
import com.hopding.pdflib.apache.fontbox.type1.Type1Font;
import com.hopding.pdflib.apache.fontbox.util.BoundingBox;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.font.encoding.Encoding;
import com.hopding.pdflib.apache.pdfbox.pdmodel.font.encoding.StandardEncoding;
import com.hopding.pdflib.apache.pdfbox.pdmodel.font.encoding.Type1Encoding;
import com.hopding.pdflib.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;
import com.hopding.pdflib.apache.pdfbox.util.Matrix;

import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

/**
 * A PostScript Type 1 Font.
 *
 * @author Ben Litchfield
 */
public class PDType1Font extends PDSimpleFont implements PDType1Equivalent
{
	// alternative names for glyphs which are commonly encountered
	private static final Map<String, String> ALT_NAMES = new HashMap<String, String>();
	static
	{
		ALT_NAMES.put("ff", "f_f");
		ALT_NAMES.put("ffi", "f_f_i");
		ALT_NAMES.put("ffl", "f_f_l");
		ALT_NAMES.put("fi", "f_i");
		ALT_NAMES.put("fl", "f_l");
		ALT_NAMES.put("st", "s_t");
		ALT_NAMES.put("IJ", "I_J");
		ALT_NAMES.put("ij", "i_j");
		ALT_NAMES.put("ellipsis", "elipsis"); // misspelled in ArialMT
	}
	private static final int PFB_START_MARKER = 0x80;

	// todo: replace with enum? or getters?
	public static final PDType1Font TIMES_ROMAN = new PDType1Font("Times-Roman");
	public static final PDType1Font TIMES_BOLD = new PDType1Font("Times-Bold");
	public static final PDType1Font TIMES_ITALIC = new PDType1Font("Times-Italic");
	public static final PDType1Font TIMES_BOLD_ITALIC = new PDType1Font("Times-BoldItalic");
	public static final PDType1Font HELVETICA = new PDType1Font("Helvetica");
	public static final PDType1Font HELVETICA_BOLD = new PDType1Font("Helvetica-Bold");
	public static final PDType1Font HELVETICA_OBLIQUE = new PDType1Font("Helvetica-Oblique");
	public static final PDType1Font HELVETICA_BOLD_OBLIQUE = new PDType1Font("Helvetica-BoldOblique");
	public static final PDType1Font COURIER = new PDType1Font("Courier");
	public static final PDType1Font COURIER_BOLD = new PDType1Font("Courier-Bold");
	public static final PDType1Font COURIER_OBLIQUE = new PDType1Font("Courier-Oblique");
	public static final PDType1Font COURIER_BOLD_OBLIQUE = new PDType1Font("Courier-BoldOblique");
	public static final PDType1Font SYMBOL = new PDType1Font("Symbol");
	public static final PDType1Font ZAPF_DINGBATS = new PDType1Font("ZapfDingbats");

	private final Type1Font type1font; // embedded font
	private final Type1Equivalent type1Equivalent; // embedded or system font for rendering
	private final boolean isEmbedded;
	private final boolean isDamaged;
	private Matrix fontMatrix;

	private Map<String, Integer> invertedEncoding; // for writing

	/**
	 * Creates a Type 1 standard 14 font for embedding.
	 *
	 * @param baseFont One of the standard 14 PostScript names
	 */
	private PDType1Font(String baseFont)
	{
		super(baseFont);

		dict.setItem(COSName.SUBTYPE, COSName.TYPE1);
		dict.setName(COSName.BASE_FONT, baseFont);
		encoding = new WinAnsiEncoding();
		dict.setItem(COSName.ENCODING, COSName.WIN_ANSI_ENCODING);

		// todo: could load the PFB font here if we wanted to support Standard 14 embedding
		type1font = null;
		type1Equivalent = ExternalFonts.getType1EquivalentFont(getBaseFont());
		isEmbedded = false;
		isDamaged = false;
	}

	/**
	 * Creates a new Type 1 font for embedding.
	 *
	 * @param doc PDF document to write to
	 * @param afmIn AFM file stream
	 * @param pfbIn PFB file stream
	 * @throws IOException
	 */
	public PDType1Font(PDDocument doc, InputStream afmIn, InputStream pfbIn) throws IOException
	{
		PDType1FontEmbedder embedder = new PDType1FontEmbedder(doc, dict, afmIn, pfbIn);
		encoding = embedder.getFontEncoding();
		type1font = embedder.getType1Font();
		type1Equivalent = embedder.getType1Font();
		isEmbedded = true;
		isDamaged = false;
	}

	/**
	 * Creates a Type 1 font from a Font dictionary in a PDF.
	 * 
	 * @param fontDictionary font dictionary
	 */
	public PDType1Font(COSDictionary fontDictionary) throws IOException
	{
		super(fontDictionary);

		PDFontDescriptor fd = getFontDescriptor();
		Type1Font t1 = null;
		boolean fontIsDamaged = false;
		if (fd != null)
		{
			// a Type1 font may contain a Type1C font
			PDStream fontFile3 = fd.getFontFile3();
			if (fontFile3 != null)
			{
				throw new IllegalArgumentException("Use PDType1CFont for FontFile3");
			}

			// or it may contain a PFB
			PDStream fontFile = fd.getFontFile();
			if (fontFile != null)
			{
				try
				{
					COSStream stream = fontFile.getStream();
					int length1 = stream.getInt(COSName.LENGTH1);
					int length2 = stream.getInt(COSName.LENGTH2);

					// repair Length1 if necessary
					byte[] bytes = fontFile.getByteArray();
					length1 = repairLength1(bytes, length1);

					if (bytes.length > 0 && (bytes[0] & 0xff) == PFB_START_MARKER)
					{
						// some bad files embed the entire PFB, see PDFBOX-2607
						t1 = Type1Font.createWithPFB(bytes);
					}
					else
					{
						// the PFB embedded as two segments back-to-back
						byte[] segment1 = Arrays.copyOfRange(bytes, 0, length1);
						byte[] segment2 = Arrays.copyOfRange(bytes, length1, length1 + length2);
						
						// empty streams are simply ignored
						if (length1 > 0 && length2 > 0)
						{
							t1 = Type1Font.createWithSegments(segment1, segment2);
						}
					}
				}
				catch (DamagedFontException e)
				{
					Log.w("PdfBoxAndroid", "Can't read damaged embedded Type1 font " + fd.getFontName());
					fontIsDamaged = true;
				}
				catch (IOException e)
				{
					Log.e("PdfBoxAndroid", "Can't read the embedded Type1 font " + fd.getFontName(), e);
					fontIsDamaged = true;
				}
			}
		}
		isEmbedded = t1 != null;
		isDamaged = fontIsDamaged;

		// try to find a suitable .pfb font to substitute
		if (t1 == null)
		{
			t1 = ExternalFonts.getType1Font(getBaseFont());
		}

		type1font = t1;

		// find a type 1-equivalent font to use for rendering, could even be a .ttf
		if (type1font != null)
		{
			type1Equivalent = type1font;
		}
		else
		{
			Type1Equivalent t1Equiv = ExternalFonts.getType1EquivalentFont(getBaseFont());
			if (t1Equiv != null)
			{
				type1Equivalent = t1Equiv;
			}
			else
			{
				type1Equivalent = ExternalFonts.getType1FallbackFont(getFontDescriptor());
				Log.w("PdfBoxAndroid", "Using fallback font " + type1Equivalent.getName() + " for " + getBaseFont());
			}
		}
		readEncoding();
	}

	/**
	 * Some Type 1 fonts have an invalid Length1, which causes the binary segment of the font
	 * to be truncated, see PDFBOX-2350.
	 *
	 * @param bytes Type 1 stream bytes
	 * @param length1 Length1 from the Type 1 stream
	 * @return repaired Length1 value
	 */
	private int repairLength1(byte[] bytes, int length1)
	{
		// scan backwards from the end of the first segment to find 'exec'
		int offset = Math.max(0, length1 - 4);
		while (offset > 0)
		{
			if (bytes[offset + 0] == 'e' &&
					bytes[offset + 1] == 'x' &&
					bytes[offset + 2] == 'e' &&
					bytes[offset + 3] == 'c')
			{
				offset += 4;
				// skip additional CR LF space characters
				while (offset < length1 && (bytes[offset] == '\r' || bytes[offset] == '\n' || bytes[offset] == ' '))
				{
					offset++;
				}
				break;
			}
			offset--;
		}

		if (length1 - offset != 0 && offset > 0)
		{
			Log.w("PdfBoxAndroid", "Ignored invalid Length1 for Type 1 font " + getName());
			return offset;
		}

		return length1;
	}

	/**
	 * Returns the PostScript name of the font.
	 */
	public String getBaseFont()
	{
		return dict.getNameAsString(COSName.BASE_FONT);
	}

	//    @Override TODO
	public float getHeight(int code) throws IOException
	{
		String name = codeToName(code);
		if (getStandard14AFM() != null)
		{
			String afmName = getEncoding().getName(code);
			return getStandard14AFM().getCharacterHeight(afmName); // todo: isn't this the y-advance, not the height?
		}
		else
		{
			RectF bounds = new RectF();
			type1Equivalent.getPath(name).computeBounds(bounds, true);
			return (float)bounds.height();
		}
	}

	@Override
	protected byte[] encode(int unicode) throws IOException
	{
		if (unicode > 0xff)
		{
			throw new IllegalArgumentException("This font type only supports 8-bit code points");
		}

		String name = getGlyphList().codePointToName(unicode);
		String nameInFont = getNameInFont(name);
		Map<String, Integer> inverted = getInvertedEncoding();

		if (nameInFont.equals(".notdef") || !type1Equivalent.hasGlyph(nameInFont))
		{
			throw new IllegalArgumentException(
					String.format("No glyph for U+%04X in font %s", unicode, getName()));
		}

		int code = inverted.get(name);
		return new byte[] { (byte)code };
	}

	/**
	 * Inverts the font's Encoding. Any duplicate (Name -> Code) mappings will be lost.
	 */
	private Map<String, Integer> getInvertedEncoding()
	{
		if (invertedEncoding != null)
		{
			return invertedEncoding;
		}

		invertedEncoding = new HashMap<String, Integer>();
		//Map<Integer, String> codeToName = MacOSRomanEncoding.INSTANCE.getCodeToNameMap();
		Map<Integer, String> codeToName = encoding.getCodeToNameMap();
		for (Map.Entry<Integer, String> entry : codeToName.entrySet())
		{
			if (!invertedEncoding.containsKey(entry.getValue()))
			{
				invertedEncoding.put(entry.getValue(), entry.getKey());
			}
		}
		return invertedEncoding;
	}

	@Override
	public float getWidthFromFont(int code) throws IOException
	{
		String name = codeToName(code);
		if (getStandard14AFM() != null)
		{
			return getStandard14Width(code);
		}
		else
		{
			return type1Equivalent.getWidth(name);
		}
	}

	@Override
	public boolean isEmbedded()
	{
		return isEmbedded;
	}

	@Override
	public float getAverageFontWidth()
	{
		if (getStandard14AFM() != null)
		{
			return getStandard14AFM().getAverageCharacterWidth();
		}
		else
		{
			return super.getAverageFontWidth();
		}
	}

	@Override
	public int readCode(InputStream in) throws IOException
	{
		return in.read();
	}

	@Override
	protected Encoding readEncodingFromFont() throws IOException
	{
		if (getStandard14AFM() != null)
		{
			// read from AFM
			return new Type1Encoding(getStandard14AFM());
		}
		else
		{
			// extract from Type1 font/substitute
			if (type1Equivalent.getEncoding() != null)
			{
				return Type1Encoding.fromFontBox(type1Equivalent.getEncoding());
			}
			else
			{
				// default (only happens with TTFs)
				return StandardEncoding.INSTANCE;
			}
		}
	}

	/**
	 * Returns the embedded or substituted Type 1 font, or null if there is none.
	 */
	public Type1Font getType1Font()
	{
		return type1font;
	}

	@Override
	public Type1Equivalent getType1Equivalent()
	{
		return type1Equivalent;
	}

	@Override
	public String getName()
	{
		return getBaseFont();
	}

	@Override
	public BoundingBox getBoundingBox() throws IOException
	{
		return type1Equivalent.getFontBBox();
	}

	@Override
	public String codeToName(int code) throws IOException
	{
		String name = getEncoding().getName(code);
		return getNameInFont(name);
	}

	/**
	 * Maps a PostScript glyph name to the name in the underlying font, for example when
	 * using a TTF font we might map "W" to "uni0057".
	 */
	private String getNameInFont(String name) throws IOException
	{
		if (isEmbedded() || type1Equivalent.hasGlyph(name))
		{
			return name;
		}
		else
		{
			// try alternative name
			String altName = ALT_NAMES.get(name);
			if (altName != null && !name.equals(".notdef") && type1Equivalent.hasGlyph(altName))
			{
				return altName;
			}
			else
			{
				// try unicode name
				String unicodes = getGlyphList().toUnicode(name);
				if (unicodes != null && unicodes.length() == 1)
				{
					String uniName = String.format("uni%04X", unicodes.codePointAt(0));
					if (type1Equivalent.hasGlyph(uniName))
					{
						return uniName;
					}
				}
			}
		}
		return ".notdef";
	}

	@Override
	public Path getPath(String name) throws IOException
	{
		// Acrobat does not draw .notdef for Type 1 fonts, see PDFBOX-2421
		// I suspect that it does do this for embedded fonts though, but this is untested
		if (name.equals(".notdef") && !isEmbedded)
		{
			return new Path();
		}
		else
		{
			return type1Equivalent.getPath(name);
		}
	}

	@Override
	public Matrix getFontMatrix()
	{
		if (fontMatrix == null)
		{
			// PDF specified that Type 1 fonts use a 1000upem matrix, but some fonts specify
			// their own custom matrix anyway, for example PDFBOX-2298
			if (type1font != null)
			{
				List<Number> numbers = type1font.getFontMatrix();
				if (numbers != null && numbers.size() == 6)
				{
					fontMatrix = new Matrix(
							numbers.get(0).floatValue(), numbers.get(1).floatValue(),
							numbers.get(2).floatValue(), numbers.get(3).floatValue(),
							numbers.get(4).floatValue(), numbers.get(5).floatValue());
				}
				else
				{
					return super.getFontMatrix();
				}
			}
			else
			{
				fontMatrix = DEFAULT_FONT_MATRIX;
			}
		}
		return fontMatrix;
	}

	@Override
	public boolean isDamaged()
	{
		return isDamaged;
	}
}
