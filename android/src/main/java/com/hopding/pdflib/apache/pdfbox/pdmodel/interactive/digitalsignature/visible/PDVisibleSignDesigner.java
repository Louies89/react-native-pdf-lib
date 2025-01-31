package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.digitalsignature.visible;

import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDPage;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * Builder for visible signature design.
 * Uses use param() instead of setParam()
 *
 * @author Vakhtang Koroghlishvili
 */
public class PDVisibleSignDesigner
{
    private Float imageWidth;
    private Float imageHeight;
    private float xAxis;
    private float yAxis;
    private float pageHeight;
    private float pageWidth;
//    private BufferedImage image;TODO
    private String signatureFieldName = "sig"; // default
    private byte[] formaterRectangleParams = { 0, 0, 100, 50 }; // default
    private byte[] AffineTransformParams =   { 1, 0, 0, 1, 0, 0 }; // default
    private float imageSizeInPercents;

    /**
     * Constructor.
     *
     * @param filename Path of the PDF file
     * @param jpegStream JPEG image as a stream
     * @param page The page are you going to add visible signature
     * @throws IOException
     */
//    public PDVisibleSignDesigner(String filename, InputStream jpegStream, int page)
//            throws IOException
//    {
//        this(new FileInputStream(filename), jpegStream, page);
//    }TODO

    /**
     * Constructor.
     *
     * @param documentStream Original PDF document as stream
     * @param jpegStream JPEG image as a stream
     * @param page The page are you going to add visible signature
     * @throws IOException
     */
//    public PDVisibleSignDesigner(InputStream documentStream, InputStream jpegStream, int page)
//            throws IOException
//    {
//        // set visible singature image Input stream
//        signatureImageStream(jpegStream);
//
//        // create PD document
//        PDDocument document = PDDocument.load(documentStream);
//
//        // calculate height an width of document
//        calculatePageSize(document, page);
//
//        document.close();
//    }TODO

    /**
     * Constructor.
     *
     * @param doc - Already created PDDocument of your PDF document
     * @param jpegStream
     * @param page
     * @throws IOException - If we can't read, flush, or can't close stream
     */
//    public PDVisibleSignDesigner(PDDocument doc, InputStream jpegStream, int page) throws IOException
//    {
//        signatureImageStream(jpegStream);
//        calculatePageSize(doc, page);
//    }TODO

    /**
     * Each page of document can be different sizes.
     * 
     * @param document
     * @param page
     */
    private void calculatePageSize(PDDocument document, int page)
    {
        if (page < 1)
        {
            throw new IllegalArgumentException("First page of pdf is 1, not " + page);
        }

        PDPage firstPage = document.getPage(page - 1);
        PDRectangle mediaBox = firstPage.getMediaBox();
        pageHeight(mediaBox.getHeight());
        pageWidth = mediaBox.getWidth();

        float x = this.pageWidth;
        float y = 0;
        pageWidth = this.pageWidth + y;
        float tPercent = (100 * y / (x + y));
        imageSizeInPercents = 100 - tPercent;
    }

    /**
     *
     * @param path  of image location
     * @return image Stream
     * @throws IOException
     */
//    public PDVisibleSignDesigner signatureImage(String path) throws IOException
//    {
//        InputStream fin = new FileInputStream(path);
//        return signatureImageStream(fin);
//    }TODO

    /**
     * zoom signature image with some percent.
     * 
     * @param percent increase image with x percent.
     * @return Visible Signature Configuration Object
     */
    public PDVisibleSignDesigner zoom(float percent)
    {
        imageHeight = imageHeight + (imageHeight * percent) / 100;
        imageWidth = imageWidth + (imageWidth * percent) / 100;
        return this;
    }

    /**
     *
     * @param x - x coordinate
     * @param y - y coordinate
     * @return Visible Signature Configuration Object
     */
    public PDVisibleSignDesigner coordinates(float x, float y)
    {
        xAxis(x);
        yAxis(y);
        return this;
    }

    /**
     *
     * @return xAxis - gets x coordinates
     */
    public float getxAxis()
    {
        return xAxis;
    }

    /**
     *
     * @param xAxis  - x coordinate 
     * @return Visible Signature Configuration Object
     */
    public PDVisibleSignDesigner xAxis(float xAxis)
    {
        this.xAxis = xAxis;
        return this;
    }

    /**
     *
     * @return yAxis
     */
    public float getyAxis()
    {
        return yAxis;
    }

    /**
     *
     * @param yAxis
     * @return Visible Signature Configuration Object
     */
    public PDVisibleSignDesigner yAxis(float yAxis)
    {
        this.yAxis = yAxis;
        return this;
    }

    /**
     * 
     * @return signature image width
     */
    public float getWidth()
    {
        return imageWidth;
    }

    /**
     * 
     * @param width signature image width
     * @return Visible Signature Configuration Object
     */
    public PDVisibleSignDesigner width(float width)
    {
        this.imageWidth = width;
        return this;
    }

    /**
     * 
     * @return signature image height
     */
    public float getHeight()
    {
        return imageHeight;
    }

    /**
     * 
     * @param height signature image Height
     * @return Visible Signature Configuration Object
     */
    public PDVisibleSignDesigner height(float height)
    {
        this.imageHeight = height;
        return this;
    }

    /**
     * 
     * @return template height
     */
    protected float getTemplateHeight()
    {
        return getPageHeight();
    }

    /**
     * 
     * @param templateHeight
     * @return Visible Signature Configuration Object
     */
    private PDVisibleSignDesigner pageHeight(float templateHeight)
    {
        this.pageHeight = templateHeight;
        return this;
    }

    /**
     * 
     * @return signature field name
     */
    public String getSignatureFieldName()
    {
        return signatureFieldName;
    }

    /**
     * 
     * @param signatureFieldName
     * @return Visible Signature Configuration Object
     */
    public PDVisibleSignDesigner signatureFieldName(String signatureFieldName)
    {
        this.signatureFieldName = signatureFieldName;
        return this;
    }

    /**
     * 
     * @return image Image
     */
//    public BufferedImage getImage()
//    {
//        return image;
//    }TODO

    /**
     * 
     * @param stream stream of your visible signature image
     * @return Visible Signature Configuration Object
     * @throws IOException If we can't read, flush, or close stream of image
     */
//    private PDVisibleSignDesigner signatureImageStream(InputStream stream) throws IOException
//    {
//        ImageIO.setUseCache(false);
//        image = ImageIO.read(stream);
//        imageHeight = (float)image.getHeight();
//        imageWidth = (float)image.getWidth();
//        return this;
//    }TODO

    /**
     * 
     * @return Affine Transform parameters of for PDF Matrix
     */
    public byte[] getAffineTransformParams()
    {
        return AffineTransformParams;
    }

    /**
     * 
     * @param affineTransformParams
     * @return Visible Signature Configuration Object
     */
    public PDVisibleSignDesigner affineTransformParams(byte[] affineTransformParams)
    {
        AffineTransformParams = affineTransformParams;
        return this;
    }

    /**
     * 
     * @return formatter PDRectanle parameters
     */
    public byte[] getFormaterRectangleParams()
    {
        return formaterRectangleParams;
    }

    /**
     * sets formatter PDRectangle;
     * 
     * @param formaterRectangleParams
     * @return Visible Signature Configuration Object
     */
    public PDVisibleSignDesigner formaterRectangleParams(byte[] formaterRectangleParams)
    {
        this.formaterRectangleParams = formaterRectangleParams;
        return this;
    }

    /**
     * 
     * @return page width
     */
    public float getPageWidth()
    {
        return pageWidth;
    }

    /**
     * 
     * @param pageWidth pageWidth
     * @return Visible Signature Configuration Object
     */
    public PDVisibleSignDesigner pageWidth(float pageWidth)
    {
        this.pageWidth = pageWidth;
        return this;
    }

    /**
     * 
     * @return page height
     */
    public float getPageHeight()
    {
        return pageHeight;
    }

    /**
     * get image size in percents
     * @return the image size in percent
     */
    public float getImageSizeInPercents()
    {
        return imageSizeInPercents;
    }

   /**
    * 
    * @param imageSizeInPercents
    */
    public void imageSizeInPercents(float imageSizeInPercents)
    {
        this.imageSizeInPercents = imageSizeInPercents;
    }

    /**
     * returns visible signature text
     * @return the visible signature's text
     */
    public String getSignatureText()
    {
        throw new UnsupportedOperationException("That method is not yet implemented");
    }

    /**
     * 
     * @param signatureText - adds the text on visible signature
     * @return the signature design
     */
    public PDVisibleSignDesigner signatureText(String signatureText)
    {
        throw new UnsupportedOperationException("That method is not yet implemented");
    }
}
