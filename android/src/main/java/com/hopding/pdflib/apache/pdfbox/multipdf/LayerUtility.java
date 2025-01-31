package com.hopding.pdflib.apache.pdfbox.multipdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.hopding.pdflib.apache.fontbox.util.BoundingBox;
import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocumentCatalog;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDPage;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDPageContentStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDResources;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDRectangle;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.optionalcontent.PDOptionalContentGroup;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.optionalcontent.PDOptionalContentProperties;
import com.hopding.pdflib.apache.pdfbox.util.Matrix;
import com.hopding.pdflib.apache.pdfbox.util.awt.AffineTransform;

/**
 * This class allows to import pages as Form XObjects into a PDF file and use them to create
 * layers (optional content groups).
 */
public class LayerUtility
{
	private static final boolean DEBUG = true;

	private PDDocument targetDoc;
	private PDFCloneUtility cloner;

	/**
	 * Creates a new instance.
	 * @param document the PDF document to modify
	 */
	public LayerUtility(PDDocument document)
	{
		this.targetDoc = document;
		this.cloner = new PDFCloneUtility(document);
	}

	/**
	 * Returns the PDF document we work on.
	 * @return the PDF document
	 */
	public PDDocument getDocument()
	{
		return this.targetDoc;
	}

	/**
	 * Some applications may not wrap their page content in a save/restore (q/Q) pair which can
	 * lead to problems with coordinate system transformations when content is appended. This
	 * method lets you add a q/Q pair around the existing page's content.
	 * @param page the page
	 * @throws IOException if an I/O error occurs
	 */
	public void wrapInSaveRestore(PDPage page) throws IOException
	{
		COSDictionary saveGraphicsStateDic = new COSDictionary();
		COSStream saveGraphicsStateStream = getDocument().getDocument().createCOSStream(saveGraphicsStateDic);
		OutputStream saveStream = saveGraphicsStateStream.createUnfilteredStream();
		saveStream.write("q\n".getBytes("ISO-8859-1"));
		saveStream.flush();

		COSStream restoreGraphicsStateStream = getDocument().getDocument().createCOSStream(saveGraphicsStateDic);
		OutputStream restoreStream = restoreGraphicsStateStream.createUnfilteredStream();
		restoreStream.write("Q\n".getBytes("ISO-8859-1"));
		restoreStream.flush();

		//Wrap the existing page's content in a save/restore pair (q/Q) to have a controlled
		//environment to add additional content.
		COSDictionary pageDictionary = page.getCOSObject();
		COSBase contents = pageDictionary.getDictionaryObject(COSName.CONTENTS);
		if (contents instanceof COSStream)
		{
			COSStream contentsStream = (COSStream)contents;

			COSArray array = new COSArray();
			array.add(saveGraphicsStateStream);
			array.add(contentsStream);
			array.add(restoreGraphicsStateStream);

			pageDictionary.setItem(COSName.CONTENTS, array);
		}
		else if( contents instanceof COSArray )
		{
			COSArray contentsArray = (COSArray)contents;

			contentsArray.add(0, saveGraphicsStateStream);
			contentsArray.add(restoreGraphicsStateStream);
		}
		else
		{
			throw new IOException("Contents are unknown type: " + contents.getClass().getName());
		}
	}

	/**
	 * Imports a page from some PDF file as a Form XObject so it can be placed on another page
	 * in the target document.
	 * @param sourceDoc the source PDF document that contains the page to be copied
	 * @param pageNumber the page number of the page to be copied
	 * @return a Form XObject containing the original page's content
	 * @throws IOException if an I/O error occurs
	 */
	public PDFormXObject importPageAsForm(PDDocument sourceDoc, int pageNumber) throws IOException
	{
		PDPage page = sourceDoc.getPage(pageNumber);
		return importPageAsForm(sourceDoc, page);
	}

	private static final Set<String> PAGE_TO_FORM_FILTER = new java.util.HashSet<String>(
			Arrays.asList(new String[] {"Group", "LastModified", "Metadata"}));

	/**
	 * Imports a page from some PDF file as a Form XObject so it can be placed on another page
	 * in the target document.
	 * @param sourceDoc the source PDF document that contains the page to be copied
	 * @param page the page in the source PDF document to be copied
	 * @return a Form XObject containing the original page's content
	 * @throws IOException if an I/O error occurs
	 */
	public PDFormXObject importPageAsForm(PDDocument sourceDoc, PDPage page) throws IOException
	{
		COSStream pageStream = (COSStream)page.getStream().getCOSObject();
		PDStream newStream = new PDStream(targetDoc,
				pageStream.getUnfilteredStream(), false);
		PDFormXObject form = new PDFormXObject(newStream);

		//Copy resources
		PDResources pageRes = page.getResources();
		PDResources formRes = new PDResources();
		cloner.cloneMerge(pageRes, formRes);
		form.setResources(formRes);

		//Transfer some values from page to form
		transferDict(page.getCOSObject(), form.getCOSStream(), PAGE_TO_FORM_FILTER, true);

		Matrix matrix = form.getMatrix();
		AffineTransform at = matrix.createAffineTransform();
		PDRectangle mediaBox = page.getMediaBox();
		PDRectangle cropBox = page.getCropBox();
		PDRectangle viewBox = (cropBox != null ? cropBox : mediaBox);

		//Handle the /Rotation entry on the page dict
		int rotation = page.getRotation();

		//Transform to FOP's user space
		//at.scale(1 / viewBox.getWidth(), 1 / viewBox.getHeight());
		at.translate(mediaBox.getLowerLeftX() - viewBox.getLowerLeftX(),
				mediaBox.getLowerLeftY() - viewBox.getLowerLeftY());
		switch (rotation)
		{
		case 90:
			at.scale(viewBox.getWidth() / viewBox.getHeight(), viewBox.getHeight() / viewBox.getWidth());
			at.translate(0, viewBox.getWidth());
			at.rotate((float)(-Math.PI / 2.0));
			break;
		case 180:
			at.translate(viewBox.getWidth(), viewBox.getHeight());
			at.rotate((float)-Math.PI);
			break;
		case 270:
			at.scale(viewBox.getWidth() / viewBox.getHeight(), viewBox.getHeight() / viewBox.getWidth());
			at.translate(viewBox.getHeight(), 0);
			at.rotate((float)(-Math.PI * 1.5));
		default:
			//no additional transformations necessary
		}
		//Compensate for Crop Boxes not starting at 0,0
		at.translate(-viewBox.getLowerLeftX(), -viewBox.getLowerLeftY());
		if (!at.isIdentity())
		{
			form.setMatrix(at);
		}

		BoundingBox bbox = new BoundingBox();
		bbox.setLowerLeftX(viewBox.getLowerLeftX());
		bbox.setLowerLeftY(viewBox.getLowerLeftY());
		bbox.setUpperRightX(viewBox.getUpperRightX());
		bbox.setUpperRightY(viewBox.getUpperRightY());
		form.setBBox(new PDRectangle(bbox));

		return form;
	}

	/**
	 * Places the given form over the existing content of the indicated page (like an overlay).
	 * The form is enveloped in a marked content section to indicate that it's part of an
	 * optional content group (OCG), here used as a layer. This optional group is returned and
	 * can be enabled and disabled through methods on {@link PDOptionalContentProperties}.
	 * @param targetPage the target page
	 * @param form the form to place
	 * @param transform the transformation matrix that controls the placement
	 * @param layerName the name for the layer/OCG to produce
	 * @return the optional content group that was generated for the form usage
	 * @throws IOException if an I/O error occurs
	 */
	public PDOptionalContentGroup appendFormAsLayer(PDPage targetPage,
			PDFormXObject form, AffineTransform transform,
			String layerName) throws IOException
	{
		PDDocumentCatalog catalog = targetDoc.getDocumentCatalog();
		PDOptionalContentProperties ocprops = catalog.getOCProperties();
		if (ocprops == null)
		{
			ocprops = new PDOptionalContentProperties();
			catalog.setOCProperties(ocprops);
		}
		if (ocprops.hasGroup(layerName))
		{
			throw new IllegalArgumentException("Optional group (layer) already exists: " + layerName);
		}

		PDOptionalContentGroup layer = new PDOptionalContentGroup(layerName);
		ocprops.addGroup(layer);

		PDPageContentStream contentStream = new PDPageContentStream(
				targetDoc, targetPage, true, !DEBUG);
		contentStream.beginMarkedContent(COSName.OC, layer);
		contentStream.saveGraphicsState();
		contentStream.transform(new Matrix(transform));
		contentStream.drawForm(form);
		contentStream.restoreGraphicsState();
		contentStream.endMarkedContent();
		contentStream.close();

		return layer;
	}

	private void transferDict(COSDictionary orgDict, COSDictionary targetDict,
			Set<String> filter, boolean inclusive) throws IOException
	{
		for (Map.Entry<COSName, COSBase> entry : orgDict.entrySet())
		{
			COSName key = entry.getKey();
			if (inclusive && !filter.contains(key.getName()))
			{
				continue;
			}
			else if (!inclusive && filter.contains(key.getName()))
			{
				continue;
			}
			targetDict.setItem(key,
					cloner.cloneForNewDocument(entry.getValue()));
		}
	}
}