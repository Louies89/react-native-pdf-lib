package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.annotation;

import java.io.IOException;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDPage;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDRectangle;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color.PDColor;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import android.util.Log;

/**
 * A PDF annotation.
 * 
 * @author Ben Litchfield
 * 
 */
public abstract class PDAnnotation implements COSObjectable
{
	/**
	 * An annotation flag.
	 */
	public static final int FLAG_INVISIBLE = 1 << 0;
	/**
	 * An annotation flag.
	 */
	public static final int FLAG_HIDDEN = 1 << 1;
	/**
	 * An annotation flag.
	 */
	public static final int FLAG_PRINTED = 1 << 2;
	/**
	 * An annotation flag.
	 */
	public static final int FLAG_NO_ZOOM = 1 << 3;
	/**
	 * An annotation flag.
	 */
	public static final int FLAG_NO_ROTATE = 1 << 4;
	/**
	 * An annotation flag.
	 */
	public static final int FLAG_NO_VIEW = 1 << 5;
	/**
	 * An annotation flag.
	 */
	public static final int FLAG_READ_ONLY = 1 << 6;
	/**
	 * An annotation flag.
	 */
	public static final int FLAG_LOCKED = 1 << 7;
	/**
	 * An annotation flag.
	 */
	public static final int FLAG_TOGGLE_NO_VIEW = 1 << 8;

	private final COSDictionary dictionary;

	/**
	 * Create the correct annotation from the base COS object.
	 * 
	 * @param base The COS object that is the annotation.
	 * @return The correctly typed annotation object.
	 * @throws IOException If there is an error while creating the annotation.
	 */
	public static PDAnnotation createAnnotation(COSBase base) throws IOException
	{
		PDAnnotation annot = null;
		if (base instanceof COSDictionary)
		{
			COSDictionary annotDic = (COSDictionary) base;
			String subtype = annotDic.getNameAsString(COSName.SUBTYPE);
			if (PDAnnotationFileAttachment.SUB_TYPE.equals(subtype))
			{
				annot = new PDAnnotationFileAttachment(annotDic);
			}
			else if (PDAnnotationLine.SUB_TYPE.equals(subtype))
			{
				annot = new PDAnnotationLine(annotDic);
			}
			else if (PDAnnotationLink.SUB_TYPE.equals(subtype))
			{
				annot = new PDAnnotationLink(annotDic);
			}
			else if (PDAnnotationPopup.SUB_TYPE.equals(subtype))
			{
				annot = new PDAnnotationPopup(annotDic);
			}
			else if (PDAnnotationRubberStamp.SUB_TYPE.equals(subtype))
			{
				annot = new PDAnnotationRubberStamp(annotDic);
			}
			else if (PDAnnotationSquareCircle.SUB_TYPE_SQUARE.equals(subtype)
					|| PDAnnotationSquareCircle.SUB_TYPE_CIRCLE.equals(subtype))
			{
				annot = new PDAnnotationSquareCircle(annotDic);
			}
			else if (PDAnnotationText.SUB_TYPE.equals(subtype))
			{
				annot = new PDAnnotationText(annotDic);
			}
			else if (PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT.equals(subtype)
					|| PDAnnotationTextMarkup.SUB_TYPE_UNDERLINE.equals(subtype)
					|| PDAnnotationTextMarkup.SUB_TYPE_SQUIGGLY.equals(subtype)
					|| PDAnnotationTextMarkup.SUB_TYPE_STRIKEOUT.equals(subtype))
			{
				annot = new PDAnnotationTextMarkup(annotDic);
			}
			else if (PDAnnotationLink.SUB_TYPE.equals(subtype))
			{
				annot = new PDAnnotationLink(annotDic);
			}
			else if (PDAnnotationWidget.SUB_TYPE.equals(subtype))
			{
				annot = new PDAnnotationWidget(annotDic);
			}
			else if (PDAnnotationMarkup.SUB_TYPE_FREETEXT.equals(subtype)
					|| PDAnnotationMarkup.SUB_TYPE_POLYGON.equals(subtype)
					|| PDAnnotationMarkup.SUB_TYPE_POLYLINE.equals(subtype)
					|| PDAnnotationMarkup.SUB_TYPE_CARET.equals(subtype)
					|| PDAnnotationMarkup.SUB_TYPE_INK.equals(subtype)
					|| PDAnnotationMarkup.SUB_TYPE_SOUND.equals(subtype))
			{
				annot = new PDAnnotationMarkup(annotDic);
			}
			else
			{
				// TODO not yet implemented:
				// Movie, Screen, PrinterMark, TrapNet, Watermark, 3D, Redact
				annot = new PDAnnotationUnknown(annotDic);
				Log.d("PdfBoxAndroid", "Unknown or unsupported annotation subtype " + subtype);
			}
		}
		else
		{
			throw new IOException("Error: Unknown annotation type " + base);
		}

		return annot;
	}

	/**
	 * Constructor.
	 */
	public PDAnnotation()
	{
		dictionary = new COSDictionary();
		dictionary.setItem(COSName.TYPE, COSName.ANNOT);
	}

	/**
	 * Constructor.
	 * 
	 * @param dict The annotations dictionary.
	 */
	public PDAnnotation(COSDictionary dict)
	{
		dictionary = dict;
		dictionary.setItem(COSName.TYPE, COSName.ANNOT);
	}

	/**
	 * returns the dictionary.
	 * 
	 * @return the dictionary
	 */
	public COSDictionary getDictionary()
	{
		return dictionary;
	}

	/**
	 * The annotation rectangle, defining the location of the annotation on the page in default user space units. This
	 * is usually required and should not return null on valid PDF documents. But where this is a parent form field with
	 * children, such as radio button collections then the rectangle will be null.
	 * 
	 * @return The Rect value of this annotation.
	 */
	public PDRectangle getRectangle()
	{
		COSArray rectArray = (COSArray) dictionary.getDictionaryObject(COSName.RECT);
		PDRectangle rectangle = null;
		if (rectArray != null)
		{
			if (rectArray.size() == 4
					&& rectArray.get(0) instanceof COSNumber
					&& rectArray.get(1) instanceof COSNumber
					&& rectArray.get(2) instanceof COSNumber
					&& rectArray.get(3) instanceof COSNumber)
			{
				rectangle = new PDRectangle(rectArray);
			}
			else
			{
				Log.w("PdfBoxAndroid", rectArray + " is not a rectangle array, returning null");
			}
		}
		return rectangle;
	}

	/**
	 * This will set the rectangle for this annotation.
	 * 
	 * @param rectangle The new rectangle values.
	 */
	public void setRectangle(PDRectangle rectangle)
	{
		dictionary.setItem(COSName.RECT, rectangle.getCOSArray());
	}

	/**
	 * This will get the flags for this field.
	 * 
	 * @return flags The set of flags.
	 */
	public int getAnnotationFlags()
	{
		return getDictionary().getInt(COSName.F, 0);
	}

	/**
	 * This will set the flags for this field.
	 * 
	 * @param flags The new flags.
	 */
	public void setAnnotationFlags(int flags)
	{
		getDictionary().setInt(COSName.F, flags);
	}

	/**
	 * Interface method for COSObjectable.
	 * 
	 * @return This object as a standard COS object.
	 */
	@Override
	public COSBase getCOSObject()
	{
		return getDictionary();
	}

	/**
	 * Returns the annotations appearance state, which selects the applicable appearance stream
	 * from an appearance subdictionary.
	 */
	public COSName getAppearanceState()
	{
		COSName name = (COSName) getDictionary().getDictionaryObject(COSName.AS);
		if (name != null)
		{
			return name;
		}
		return null;
	}

	/**
	 * This will set the annotations appearance state name.
	 * 
	 * @param as The name of the appearance stream.
	 */
	public void setAppearanceState(String as)
	{
		if (as == null)
		{
			getDictionary().removeItem(COSName.AS);
		}
		else
		{
			getDictionary().setItem(COSName.AS, COSName.getPDFName(as));
		}
	}

	/**
	 * This will get the appearance dictionary associated with this annotation. This may return null.
	 * 
	 * @return This annotations appearance.
	 */
	public PDAppearanceDictionary getAppearance()
	{
		PDAppearanceDictionary ap = null;
		COSDictionary apDic = (COSDictionary) dictionary.getDictionaryObject(COSName.AP);
		if (apDic != null)
		{
			ap = new PDAppearanceDictionary(apDic);
		}
		return ap;
	}

	/**
	 * This will set the appearance associated with this annotation.
	 * 
	 * @param appearance The appearance dictionary for this annotation.
	 */
	public void setAppearance(PDAppearanceDictionary appearance)
	{
		COSDictionary ap = null;
		if (appearance != null)
		{
			ap = appearance.getCOSObject();
		}
		dictionary.setItem(COSName.AP, ap);
	}

	/**
	 * Returns the appearance stream for this annotation, if any. The annotation state is taken
	 * into account, if present.
	 */
	public PDAppearanceStream getNormalAppearanceStream()
	{
		PDAppearanceDictionary appearanceDict = getAppearance();
		if (appearanceDict == null)
		{
			return null;
		}

		PDAppearanceEntry normalAppearance = appearanceDict.getNormalAppearance();
		if (normalAppearance == null)
		{
			return null;
		}

		if (normalAppearance.isSubDictionary())
		{
			COSName state = getAppearanceState();
			return normalAppearance.getSubDictionary().get(state);
		}
		else
		{
			return normalAppearance.getAppearanceStream();
		}
	}

	/**
	 * Get the invisible flag.
	 * 
	 * @return The invisible flag.
	 */
	public boolean isInvisible()
	{
		return getDictionary().getFlag( COSName.F, FLAG_INVISIBLE);
	}

	/**
	 * Set the invisible flag.
	 * 
	 * @param invisible The new invisible flag.
	 */
	public void setInvisible(boolean invisible)
	{
		getDictionary().setFlag( COSName.F, FLAG_INVISIBLE, invisible);
	}

	/**
	 * Get the hidden flag.
	 * 
	 * @return The hidden flag.
	 */
	public boolean isHidden()
	{
		return getDictionary().getFlag( COSName.F, FLAG_HIDDEN);
	}

	/**
	 * Set the hidden flag.
	 * 
	 * @param hidden The new hidden flag.
	 */
	public void setHidden(boolean hidden)
	{
		getDictionary().setFlag( COSName.F, FLAG_HIDDEN, hidden);
	}

	/**
	 * Get the printed flag.
	 * 
	 * @return The printed flag.
	 */
	public boolean isPrinted()
	{
		return getDictionary().getFlag( COSName.F, FLAG_PRINTED);
	}

	/**
	 * Set the printed flag.
	 * 
	 * @param printed The new printed flag.
	 */
	public void setPrinted(boolean printed)
	{
		getDictionary().setFlag( COSName.F, FLAG_PRINTED, printed);
	}

	/**
	 * Get the noZoom flag.
	 * 
	 * @return The noZoom flag.
	 */
	public boolean isNoZoom()
	{
		return getDictionary().getFlag( COSName.F, FLAG_NO_ZOOM);
	}

	/**
	 * Set the noZoom flag.
	 * 
	 * @param noZoom The new noZoom flag.
	 */
	public void setNoZoom(boolean noZoom)
	{
		getDictionary().setFlag( COSName.F, FLAG_NO_ZOOM, noZoom);
	}

	/**
	 * Get the noRotate flag.
	 * 
	 * @return The noRotate flag.
	 */
	public boolean isNoRotate()
	{
		return getDictionary().getFlag( COSName.F, FLAG_NO_ROTATE);
	}

	/**
	 * Set the noRotate flag.
	 * 
	 * @param noRotate The new noRotate flag.
	 */
	public void setNoRotate(boolean noRotate)
	{
		getDictionary().setFlag( COSName.F, FLAG_NO_ROTATE, noRotate);
	}

	/**
	 * Get the noView flag.
	 * 
	 * @return The noView flag.
	 */
	public boolean isNoView()
	{
		return getDictionary().getFlag( COSName.F, FLAG_NO_VIEW);
	}

	/**
	 * Set the noView flag.
	 * 
	 * @param noView The new noView flag.
	 */
	public void setNoView(boolean noView)
	{
		getDictionary().setFlag( COSName.F, FLAG_NO_VIEW, noView);
	}

	/**
	 * Get the readOnly flag.
	 * 
	 * @return The readOnly flag.
	 */
	public boolean isReadOnly()
	{
		return getDictionary().getFlag( COSName.F, FLAG_READ_ONLY);
	}

	/**
	 * Set the readOnly flag.
	 * 
	 * @param readOnly The new readOnly flag.
	 */
	public void setReadOnly(boolean readOnly)
	{
		getDictionary().setFlag( COSName.F, FLAG_READ_ONLY, readOnly);
	}

	/**
	 * Get the locked flag.
	 * 
	 * @return The locked flag.
	 */
	public boolean isLocked()
	{
		return getDictionary().getFlag( COSName.F, FLAG_LOCKED);
	}

	/**
	 * Set the locked flag.
	 * 
	 * @param locked The new locked flag.
	 */
	public void setLocked(boolean locked)
	{
		getDictionary().setFlag( COSName.F, FLAG_LOCKED, locked);
	}

	/**
	 * Get the toggleNoView flag.
	 * 
	 * @return The toggleNoView flag.
	 */
	public boolean isToggleNoView()
	{
		return getDictionary().getFlag( COSName.F, FLAG_TOGGLE_NO_VIEW);
	}

	/**
	 * Set the toggleNoView flag.
	 * 
	 * @param toggleNoView The new toggleNoView flag.
	 */
	public void setToggleNoView(boolean toggleNoView)
	{
		getDictionary().setFlag( COSName.F, FLAG_TOGGLE_NO_VIEW, toggleNoView);
	}

	/**
	 * Get the "contents" of the field.
	 * 
	 * @return the value of the contents.
	 */
	public String getContents()
	{
		return dictionary.getString(COSName.CONTENTS);
	}

	/**
	 * Set the "contents" of the field.
	 * 
	 * @param value the value of the contents.
	 */
	public void setContents(String value)
	{
		dictionary.setString(COSName.CONTENTS, value);
	}

	/**
	 * This will retrieve the date and time the annotation was modified.
	 * 
	 * @return the modified date/time (often in date format, but can be an arbitary string).
	 */
	public String getModifiedDate()
	{
		return getDictionary().getString(COSName.M);
	}

	/**
	 * This will set the date and time the annotation was modified.
	 * 
	 * @param m the date and time the annotation was created.
	 */
	public void setModifiedDate(String m)
	{
		getDictionary().setString(COSName.M, m);
	}

	/**
	 * This will get the name, a string intended to uniquely identify each annotation within a page. Not to be confused
	 * with some annotations Name entry which impact the default image drawn for them.
	 * 
	 * @return The identifying name for the Annotation.
	 */
	public String getAnnotationName()
	{
		return getDictionary().getString(COSName.NM);
	}

	/**
	 * This will set the name, a string intended to uniquely identify each annotation within a page. Not to be confused
	 * with some annotations Name entry which impact the default image drawn for them.
	 * 
	 * @param nm The identifying name for the annotation.
	 */
	public void setAnnotationName(String nm)
	{
		getDictionary().setString(COSName.NM, nm);
	}

	/**
	 * This will get the key of this annotation in the structural parent tree.
	 * 
	 * @return the integer key of the annotation's entry in the structural parent tree
	 */
	public int getStructParent()
	{
		return getDictionary().getInt(COSName.STRUCT_PARENT, 0);
	}

	/**
	 * This will set the key for this annotation in the structural parent tree.
	 * 
	 * @param structParent The new key for this annotation.
	 */
	public void setStructParent(int structParent)
	{
		getDictionary().setInt(COSName.STRUCT_PARENT, structParent);
	}

	/**
	 * This will set the color used in drawing various elements. As of PDF 1.6 these are : Background of icon when
	 * closed Title bar of popup window Border of a link annotation
	 * 
	 * Colour is in DeviceRGB colourspace
	 * 
	 * @param c colour in the DeviceRGB colourspace
	 * 
	 */
	public void setColor(PDColor c)
	{
		getDictionary().setItem(COSName.C, c.toCOSArray());
	}

	/**
	 * This will retrieve the color used in drawing various elements. As of PDF
	 * 1.6 these are :
	 * <ul>
	 * <li>Background of icon when closed</li>
	 * <li>Title bar of popup window</li>
	 * <li>Border of a link annotation</li></ul>
	 * @return Color object representing the colour
	 */
	public PDColor getColor()
	{
		return getColor(COSName.C);
	}

	protected PDColor getColor(COSName itemName)
	{
		COSBase c = this.getDictionary().getItem(itemName);
		if (c instanceof COSArray)
		{
			PDColorSpace colorSpace = null;
			switch (((COSArray) c).size())
			{
			case 1:
				colorSpace = PDDeviceGray.INSTANCE;
				break;
			case 3:
				colorSpace = PDDeviceRGB.INSTANCE;
				break;
//			case 4:
//				colorSpace = PDDeviceCMYK.INSTANCE;
//				break; TODO
			default:
				break;
			}
			return new PDColor((COSArray) c, colorSpace);
		}
		return null;
	}

	/**
	 * This will retrieve the subtype of the annotation.
	 * 
	 * @return the subtype
	 */
	public String getSubtype()
	{
		return this.getDictionary().getNameAsString(COSName.SUBTYPE);
	}

	/**
	 * This will set the corresponding page for this annotation.
	 * 
	 * @param page is the corresponding page
	 */
	public void setPage(PDPage page)
	{
		this.getDictionary().setItem(COSName.P, page);
	}

	/**
	 * This will retrieve the corresponding page of this annotation.
	 * 
	 * @return the corresponding page
	 */
	public PDPage getPage()
	{
		COSDictionary p = (COSDictionary) this.getDictionary().getDictionaryObject(COSName.P);
		if (p != null)
		{
			return new PDPage(p);
		}
		return null;
	}

}
