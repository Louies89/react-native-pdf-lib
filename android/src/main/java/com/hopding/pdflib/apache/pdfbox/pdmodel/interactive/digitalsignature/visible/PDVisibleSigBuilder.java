package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.digitalsignature.visible;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDPage;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDResources;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDRectangle;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.form.PDFieldTreeNode;
import com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import com.hopding.pdflib.apache.pdfbox.util.awt.AffineTransform;

import android.util.Log;

/**
 * Implementation of PDFTemplateBuilder.
 * @see PDFTemplateBuilder
 * @author Vakhtang Koroghlishvili
 */
public class PDVisibleSigBuilder implements PDFTemplateBuilder
{
    private PDFTemplateStructure pdfStructure;

    @Override
    public void createPage(PDVisibleSignDesigner properties)
    {
    	PDPage page = new PDPage(new PDRectangle(properties.getPageWidth(), properties.getPageHeight()));
        pdfStructure.setPage(page);
        Log.i("PdfBoxAndroid", "PDF page has been created");
    }

    @Override
    public void createTemplate(PDPage page) throws IOException
    {
        PDDocument template = new PDDocument();
        template.addPage(page);
        pdfStructure.setTemplate(template);
    }

    public PDVisibleSigBuilder()
    {
        pdfStructure = new PDFTemplateStructure();
        Log.i("PdfBoxAndroid", "PDF Strucure has been Created");
    }

    @Override
    public void createAcroForm(PDDocument template)
    {
        PDAcroForm theAcroForm = new PDAcroForm(template);
        template.getDocumentCatalog().setAcroForm(theAcroForm);
        pdfStructure.setAcroForm(theAcroForm);
        Log.i("PdfBoxAndroid", "Acro form page has been created");
    }

    @Override
    public PDFTemplateStructure getStructure()
    {
        return pdfStructure;
    }

    @Override
    public void createSignatureField(PDAcroForm acroForm) throws IOException
    {
        PDSignatureField sf = new PDSignatureField(acroForm);
        pdfStructure.setSignatureField(sf);
        Log.i("PdfBoxAndroid", "Signature field has been created");
    }

    @Override
    public void createSignature(PDSignatureField pdSignatureField, PDPage page,
                                String signatureName) throws IOException
    {
        PDSignature pdSignature = new PDSignature();
        pdSignatureField.setSignature(pdSignature);
        pdSignatureField.getWidget().setPage(page);
        page.getAnnotations().add(pdSignatureField.getWidget());
        pdSignature.setName(signatureName);
        pdSignature.setByteRange(new int[] { 0, 0, 0, 0 });
        pdSignature.setContents(new byte[4096]);
        pdfStructure.setPdSignature(pdSignature);
        Log.i("PdfBoxAndroid", "PDSignatur has been created");
    }

    @Override
    public void createAcroFormDictionary(PDAcroForm acroForm, PDSignatureField signatureField)
            throws IOException
    {
        List<PDFieldTreeNode> acroFormFields = acroForm.getFields();
        COSDictionary acroFormDict = acroForm.getDictionary();
        acroForm.setSignaturesExist(true);
        acroForm.setAppendOnly(true);
        acroFormDict.setDirect(true);
        acroFormFields.add(signatureField);
        acroForm.setDefaultAppearance("/sylfaen 0 Tf 0 g");
        pdfStructure.setAcroFormFields(acroFormFields);
        pdfStructure.setAcroFormDictionary(acroFormDict);
        Log.i("PdfBoxAndroid", "AcroForm dictionary has been created");
    }

    @Override
    public void createSignatureRectangle(PDSignatureField signatureField,
                                         PDVisibleSignDesigner properties) throws IOException
    {

        PDRectangle rect = new PDRectangle();
        rect.setUpperRightX(properties.getxAxis() + properties.getWidth());
        rect.setUpperRightY(properties.getTemplateHeight() - properties.getyAxis());
        rect.setLowerLeftY(properties.getTemplateHeight() - properties.getyAxis() -
                           properties.getHeight());
        rect.setLowerLeftX(properties.getxAxis());
        signatureField.getWidget().setRectangle(rect);
        pdfStructure.setSignatureRectangle(rect);
        Log.i("PdfBoxAndroid", "rectangle of signature has been created");
    }

    @Override
    public void createAffineTransform(byte[] params)
    {
    	AffineTransform transform = new AffineTransform(params[0], params[1], params[2],
    			params[3], params[4], params[5]);
        pdfStructure.setAffineTransform(transform);
        Log.i("PdfBoxAndroid", "Matrix has been added");
    }

    @Override
    public void createProcSetArray()
    {
        COSArray procSetArr = new COSArray();
        procSetArr.add(COSName.getPDFName("PDF"));
        procSetArr.add(COSName.getPDFName("Text"));
        procSetArr.add(COSName.getPDFName("ImageB"));
        procSetArr.add(COSName.getPDFName("ImageC"));
        procSetArr.add(COSName.getPDFName("ImageI"));
        pdfStructure.setProcSet(procSetArr);
        Log.i("PdfBoxAndroid", "ProcSet array has been created");
    }

//    @Override
//    public void createSignatureImage(PDDocument template, BufferedImage image) throws IOException
//    {
//        pdfStructure.setImage(JPEGFactory.createFromImage(template, image));
//        Log.i("PdfBoxAndroid", "Visible Signature Image has been created");
//    }TODO

    @Override
    public void createFormaterRectangle(byte[] params)
    {
        PDRectangle formrect = new PDRectangle();
        formrect.setUpperRightX(params[0]);
        formrect.setUpperRightY(params[1]);
        formrect.setLowerLeftX(params[2]);
        formrect.setLowerLeftY(params[3]);

        pdfStructure.setFormaterRectangle(formrect);
        Log.i("PdfBoxAndroid", "Formater rectangle has been created");
    }

    @Override
    public void createHolderFormStream(PDDocument template)
    {
        PDStream holderForm = new PDStream(template);
        pdfStructure.setHolderFormStream(holderForm);
        Log.i("PdfBoxAndroid", "Holder form Stream has been created");
    }

    @Override
    public void createHolderFormResources()
    {
        PDResources holderFormResources = new PDResources();
        pdfStructure.setHolderFormResources(holderFormResources);
        Log.i("PdfBoxAndroid", "Holder form resources have been created");

    }

    @Override
    public void createHolderForm(PDResources holderFormResources, PDStream holderFormStream,
                                 PDRectangle formrect)
    {
        PDFormXObject holderForm = new PDFormXObject(holderFormStream);
        holderForm.setResources(holderFormResources);
        holderForm.setBBox(formrect);
        holderForm.setFormType(1);
        pdfStructure.setHolderForm(holderForm);
        Log.i("PdfBoxAndroid", "Holder form has been created");

    }

    @Override
    public void createAppearanceDictionary(PDFormXObject holderForml,
                                           PDSignatureField signatureField) throws IOException
    {
        PDAppearanceDictionary appearance = new PDAppearanceDictionary();
        appearance.getCOSObject().setDirect(true);

        PDAppearanceStream appearanceStream = new PDAppearanceStream(holderForml.getCOSStream());

        appearance.setNormalAppearance(appearanceStream);
        signatureField.getWidget().setAppearance(appearance);

        pdfStructure.setAppearanceDictionary(appearance);
        Log.i("PdfBoxAndroid", "PDF appereance Dictionary has been created");
    }

    @Override
    public void createInnerFormStream(PDDocument template)
    {
        PDStream innterFormStream = new PDStream(template);
        pdfStructure.setInnterFormStream(innterFormStream);
        Log.i("PdfBoxAndroid", "Stream of another form (inner form - it would be inside holder form) " +
                 "has been created");
    }

    @Override
    public void createInnerFormResource()
    {
        PDResources innerFormResources = new PDResources();
        pdfStructure.setInnerFormResources(innerFormResources);
        Log.i("PdfBoxAndroid", "Resources of another form (inner form - it would be inside holder form)" +
                 "have been created");
    }

    @Override
    public void createInnerForm(PDResources innerFormResources, PDStream innerFormStream,
                                PDRectangle formrect)
    {
        PDFormXObject innerForm = new PDFormXObject(innerFormStream);
        innerForm.setResources(innerFormResources);
        innerForm.setBBox(formrect);
        innerForm.setFormType(1);
        pdfStructure.setInnerForm(innerForm);
        Log.i("PdfBoxAndroid", "Another form (inner form - it would be inside holder form) have been created");
    }

    @Override
    public void insertInnerFormToHolerResources(PDFormXObject innerForm,
                                                PDResources holderFormResources)
    {
        COSName name = holderFormResources.add(innerForm, "FRM");
        pdfStructure.setInnerFormName(name);
        Log.i("PdfBoxAndroid", "Already inserted inner form  inside holder form");
    }

    @Override
    public void createImageFormStream(PDDocument template)
    {
        PDStream imageFormStream = new PDStream(template);
        pdfStructure.setImageFormStream(imageFormStream);
        Log.i("PdfBoxAndroid", "Created image form Stream");
    }

    @Override
    public void createImageFormResources()
    {
        PDResources imageFormResources = new PDResources();
        pdfStructure.setImageFormResources(imageFormResources);
        Log.i("PdfBoxAndroid", "Created image form Resources");
    }

    @Override
    public void createImageForm(PDResources imageFormResources, PDResources innerFormResource,
                                PDStream imageFormStream, PDRectangle formrect, AffineTransform at,
                                PDImageXObject img) throws IOException
    {
        PDFormXObject imageForm = new PDFormXObject(imageFormStream);
        imageForm.setBBox(formrect);
        imageForm.setMatrix(at);
        imageForm.setResources(imageFormResources);
        imageForm.setFormType(1);

        imageFormResources.getCOSObject().setDirect(true);
        COSName imageFormName = innerFormResource.add(imageForm, "n");
        COSName imageName = imageFormResources.add(img, "img");
        pdfStructure.setImageForm(imageForm);
        pdfStructure.setImageFormName(imageFormName);
        pdfStructure.setImageName(imageName);
        Log.i("PdfBoxAndroid", "Created image form");
    }

    @Override
    public void injectProcSetArray(PDFormXObject innerForm, PDPage page,
                                   PDResources innerFormResources,  PDResources imageFormResources,
                                   PDResources holderFormResources, COSArray procSet)
    {
        innerForm.getResources().getCOSObject().setItem(COSName.PROC_SET, procSet);
        page.getCOSObject().setItem(COSName.PROC_SET, procSet);
        innerFormResources.getCOSObject().setItem(COSName.PROC_SET, procSet);
        imageFormResources.getCOSObject().setItem(COSName.PROC_SET, procSet);
        holderFormResources.getCOSObject().setItem(COSName.PROC_SET, procSet);
        Log.i("PdfBoxAndroid", "inserted ProcSet to PDF");
    }

    @Override
    public void injectAppearanceStreams(PDStream holderFormStream, PDStream innterFormStream,
                                        PDStream imageFormStream, COSName imageObjectName,
                                        COSName imageName, COSName innerFormName,
                                        PDVisibleSignDesigner properties) throws IOException
    {
        // 100 means that document width is 100% via the rectangle. if rectangle
        // is 500px, images 100% is 500px.
        // String imgFormComment = "q "+imageWidthSize+ " 0 0 50 0 0 cm /" +
        // imageName + " Do Q\n" + builder.toString();
        String imgFormComment = "q " + 100 + " 0 0 50 0 0 cm /" + imageName.getName() + " Do Q\n";
        String holderFormComment = "q 1 0 0 1 0 0 cm /" + innerFormName.getName() + " Do Q \n";
        String innerFormComment = "q 1 0 0 1 0 0 cm /" + imageObjectName.getName() + " Do Q\n";

        appendRawCommands(pdfStructure.getHolderFormStream().createOutputStream(),
                holderFormComment);
        appendRawCommands(pdfStructure.getInnterFormStream().createOutputStream(),
                innerFormComment);
        appendRawCommands(pdfStructure.getImageFormStream().createOutputStream(),
                imgFormComment);
        Log.i("PdfBoxAndroid", "Injected apereance stream to pdf");
    }

    public void appendRawCommands(OutputStream os, String commands) throws IOException
    {
        os.write(commands.getBytes("UTF-8"));
        os.close();
    }

    @Override
    public void createVisualSignature(PDDocument template)
    {
        pdfStructure.setVisualSignature(template.getDocument());
        Log.i("PdfBoxAndroid", "Visible signature has been created");
    }

    @Override
    public void createWidgetDictionary(PDSignatureField signatureField,
                                       PDResources holderFormResources) throws IOException
    {
        COSDictionary widgetDict = signatureField.getWidget().getDictionary();
        widgetDict.setNeedToBeUpdated(true);
        widgetDict.setItem(COSName.DR, holderFormResources.getCOSObject());

        pdfStructure.setWidgetDictionary(widgetDict);
        Log.i("PdfBoxAndroid", "WidgetDictionary has been crated");
    }

    @Override
    public void closeTemplate(PDDocument template) throws IOException
    {
        template.close();
        pdfStructure.getTemplate().close();
    }
}
