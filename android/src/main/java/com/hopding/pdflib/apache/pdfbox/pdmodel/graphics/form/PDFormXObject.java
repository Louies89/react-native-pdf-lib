package com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.form;

import com.hopding.pdflib.apache.pdfbox.contentstream.PDContentStream;
import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSFloat;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDResources;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDRectangle;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.PDXObject;
import com.hopding.pdflib.apache.pdfbox.util.Matrix;
import com.hopding.pdflib.apache.pdfbox.util.awt.AffineTransform;

/*
TODO There are further Form XObjects to implement:

+ PDFormXObject
|- PDReferenceXObject
|- PDGroupXObject
   |- PDTransparencyXObject

See PDF 32000 p111

When doing this all methods on PDFormXObject should probably be made
final and all fields private.
 */

/**
 * A Form XObject.
 * 
 * @author Ben Litchfield
 */
public class PDFormXObject extends PDXObject implements PDContentStream
{
	// name of XObject in resources, to prevent recursion
	private String name;

	private PDGroup group;

	/**
	 * Creates a Form XObject for reading.
	 * @param stream The XObject stream
	 */
	public PDFormXObject(PDStream stream)
	{
		super(stream, COSName.FORM);
	}

	/**
	 * Creates a Form XObject for reading.
	 * @param stream The XObject stream
	 * @param name The name of the form XObject, to prevent recursion.
	 */
	public PDFormXObject(PDStream stream, String name)
	{
		super(stream, COSName.FORM);
		this.name = name;
	}

	/**
	 * Creates a Form Image XObject for writing, in the given document.
	 * @param document The current document
	 */
	public PDFormXObject(PDDocument document)
	{
		super(document, COSName.FORM);
	}

	/**
	 * This will get the form type, currently 1 is the only form type.
	 * @return The form type.
	 */
	public int getFormType()
	{
		return getCOSStream().getInt(COSName.FORMTYPE, 1);
	}

	/**
	 * Set the form type.
	 * @param formType The new form type.
	 */
	public void setFormType(int formType)
	{
		getCOSStream().setInt(COSName.FORMTYPE, formType);
	}

	/**
	 * Returns the group attributes dictionary (Group XObject).
	 *
	 * @return the group attributes dictionary
	 */
	public PDGroup getGroup()
	{
		if( group == null ) 
		{
			COSDictionary dic = (COSDictionary) getCOSStream().getDictionaryObject(COSName.GROUP);
			if( dic != null ) 
			{
				group = new PDGroup(dic);
			}
		}
		return group;
	}

	@Override
	public COSStream getContentStream()
	{
		return getCOSStream();
	}

	/**
	 * This will get the resources at this page and not look up the hierarchy.
	 * This attribute is inheritable, and findResources() should probably used.
	 * This will return null if no resources are available at this level.
	 * @return The resources at this level in the hierarchy.
	 */
	@Override
	public PDResources getResources()
	{
		COSDictionary resources = (COSDictionary) getCOSStream().getDictionaryObject(COSName.RESOURCES);
		if (resources != null)
		{
			return new PDResources(resources);
		}
		return null;
	}

	/**
	 * This will set the resources for this page.
	 * @param resources The new resources for this page.
	 */
	public void setResources(PDResources resources)
	{
		getCOSStream().setItem(COSName.RESOURCES, resources);
	}

	/**
	 * An array of four numbers in the form coordinate system (see below),
	 * giving the coordinates of the left, bottom, right, and top edges, respectively,
	 * of the form XObject's bounding box.
	 * These boundaries are used to clip the form XObject and to determine its size for caching.
	 * @return The BBox of the form.
	 */
	@Override
	public PDRectangle getBBox()
	{
		PDRectangle retval = null;
		COSArray array = (COSArray) getCOSStream().getDictionaryObject(COSName.BBOX);
		if (array != null)
		{
			retval = new PDRectangle(array);
		}
		return retval;
	}

	/**
	 * This will set the BBox (bounding box) for this form.
	 * @param bbox The new BBox for this form.
	 */
	public void setBBox(PDRectangle bbox)
	{
		if (bbox == null)
		{
			getCOSStream().removeItem(COSName.BBOX);
		}
		else
		{
			getCOSStream().setItem(COSName.BBOX, bbox.getCOSArray());
		}
	}

	/**
	 * This will get the optional Matrix of an XObjectForm. It maps the form space to user space.
	 * @return the form matrix if available, or the identity matrix.
	 */
	@Override
	public Matrix getMatrix()
	{
		COSArray array = (COSArray) getCOSStream().getDictionaryObject(COSName.MATRIX);
		if (array != null)
		{
			return new Matrix(array);
		} else {
			// default value is the identity matrix
			return new Matrix();
		}
	}

	/**
	 * Sets the optional Matrix entry for the form XObject.
	 * @param transform the transformation matrix
	 */
	public void setMatrix(AffineTransform transform)
	{
		COSArray matrix = new COSArray();
		double[] values = new double[6];
		transform.getMatrix(values);
		for (double v : values)
		{
			matrix.add(new COSFloat((float) v));
		}
		getCOSStream().setItem(COSName.MATRIX, matrix);
	}

	/**
	 * This will get the key of this XObjectForm in the structural parent tree.
	 * Required if the form XObject contains marked-content sequences that are
	 * structural content items.
	 * @return the integer key of the XObjectForm's entry in the structural parent tree
	 */
	public int getStructParents()
	{
		return getCOSStream().getInt(COSName.STRUCT_PARENTS, 0);
	}

	/**
	 * This will set the key for this XObjectForm in the structural parent tree.
	 * @param structParent The new key for this XObjectForm.
	 */
	public void setStructParents(int structParent)
	{
		getCOSStream().setInt(COSName.STRUCT_PARENTS, structParent);
	}
}
