package com.hopding.pdflib.apache.pdfbox.pdmodel.fdf;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSStream;
import com.hopding.pdflib.apache.pdfbox.cos.COSString;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSArrayList;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.filespecification.PDFileSpecification;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.filespecification.PDSimpleFileSpecification;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This represents an FDF dictionary that is part of the FDF document.
 *
 * @author Ben Litchfield
 */
public class FDFDictionary implements COSObjectable
{
    private COSDictionary fdf;

    /**
     * Default constructor.
     */
    public FDFDictionary()
    {
        fdf = new COSDictionary();
    }

    /**
     * Constructor.
     *
     * @param fdfDictionary The FDF documents catalog.
     */
    public FDFDictionary( COSDictionary fdfDictionary )
    {
        fdf = fdfDictionary;
    }

    /**
     * This will create an FDF dictionary from an XFDF XML document.
     *
     * @param fdfXML The XML document that contains the XFDF data.
     * @throws IOException If there is an error reading from the dom.
     */
    public FDFDictionary( Element fdfXML ) throws IOException
    {
        this();
        NodeList nodeList = fdfXML.getChildNodes();
        for( int i=0; i<nodeList.getLength(); i++ )
        {
            Node node = nodeList.item( i );
            if( node instanceof Element )
            {
                Element child = (Element)node;
                if( child.getTagName().equals( "f" ) )
                {
                    PDSimpleFileSpecification fs = new PDSimpleFileSpecification();
                    fs.setFile( child.getAttribute( "href" ) );
                    setFile(fs);
                }
                else if( child.getTagName().equals( "ids" ) )
                {
                    COSArray ids = new COSArray();
                    String original = child.getAttribute( "original" );
                    String modified = child.getAttribute( "modified" );
                    ids.add( COSString.parseHex( original ) );
                    ids.add( COSString.parseHex( modified ) );
                    setID( ids );
                }
                else if( child.getTagName().equals( "fields" ) )
                {
                    NodeList fields = child.getChildNodes();
                    List<FDFField> fieldList = new ArrayList<FDFField>();
                    for( int f=0; f<fields.getLength(); f++ )
                    {
                        Node currentNode = fields.item( f );
                        if (currentNode instanceof Element && ((Element)currentNode).getTagName().equals("field")) 
                        {
                        	fieldList.add( new FDFField( (Element)fields.item( f ) ) );
                        }
                    }
                    setFields( fieldList );
                }
                else if( child.getTagName().equals( "annots" ) )
                {
                    NodeList annots = child.getChildNodes();
                    List<FDFAnnotationText> annotList = new ArrayList<FDFAnnotationText>();
                    for( int j=0; j<annots.getLength(); j++ )
                    {
                        Node annotNode = annots.item( i );
                        if( annotNode instanceof Element )
                        {
                            Element annot = (Element)annotNode;
                            if( annot.getNodeName().equals( "text" ) )
                            {
                                annotList.add( new FDFAnnotationText( annot ) );
                            }
                            else
                            {
                                throw new IOException( "Error: Unknown annotation type '" + annot.getNodeName() );
                            }
                        }
                    }
                    setAnnotations(annotList);
                }
            }
        }
    }

    /**
     * This will write this element as an XML document.
     *
     * @param output The stream to write the xml to.
     *
     * @throws IOException If there is an error writing the XML.
     */
    public void writeXML( Writer output ) throws IOException
    {
        PDFileSpecification fs = this.getFile();
        if( fs != null )
        {
            output.write( "<f href=\"" + fs.getFile() + "\" />\n" );
        }
        COSArray ids = this.getID();
        if( ids != null )
        {
            COSString original = (COSString)ids.getObject( 0 );
            COSString modified = (COSString)ids.getObject( 1 );
            output.write( "<ids original=\"" + original.toHexString() + "\" " );
            output.write( "modified=\"" + modified.toHexString() + "\" />\n");
        }
        List<FDFField> fields = getFields();
        if( fields != null && fields.size() > 0 )
        {
            output.write( "<fields>\n" );
            for (FDFField field : fields)
            {
                field.writeXML(output);
            }
            output.write( "</fields>\n" );
        }
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    @Override
    public COSBase getCOSObject()
    {
        return fdf;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSDictionary getCOSDictionary()
    {
        return fdf;
    }

    /**
     * The source file or target file: the PDF document file that
     * this FDF file was exported from or is intended to be imported into.
     *
     * @return The F entry of the FDF dictionary.
     *
     * @throws IOException If there is an error creating the file spec.
     */
    public PDFileSpecification getFile() throws IOException
    {
        return PDFileSpecification.createFS( fdf.getDictionaryObject( COSName.F ) );
    }

    /**
     * This will set the file specification.
     *
     * @param fs The file specification.
     */
    public void setFile( PDFileSpecification fs )
    {
        fdf.setItem( COSName.F, fs );
    }

    /**
     * This is the FDF id.
     *
     * @return The FDF ID.
     */
    public COSArray getID()
    {
        return (COSArray)fdf.getDictionaryObject( COSName.ID );
    }

    /**
     * This will set the FDF id.
     *
     * @param id The new id for the FDF.
     */
    public void setID( COSArray id )
    {
        fdf.setItem( COSName.ID, id );
    }

    /**
     * This will get the list of FDF Fields.  This will return a list of FDFField
     * objects.
     *
     * @return A list of FDF fields.
     */
    public List<FDFField> getFields()
    {
        List<FDFField> retval = null;
        COSArray fieldArray = (COSArray)fdf.getDictionaryObject( COSName.FIELDS );
        if( fieldArray != null )
        {
            List<FDFField> fields = new ArrayList<FDFField>();
            for( int i=0; i<fieldArray.size(); i++ )
            {
                fields.add( new FDFField( (COSDictionary)fieldArray.getObject( i ) ) );
            }
            retval = new COSArrayList<FDFField>( fields, fieldArray );
        }
        return retval;
    }

    /**
     * This will set the list of fields.  This should be a list of FDFField objects.
     *
     * @param fields The list of fields.
     */
    public void setFields( List<FDFField> fields )
    {
        fdf.setItem( COSName.FIELDS, COSArrayList.converterToCOSArray( fields ) );
    }

    /**
     * This will get the status string to be displayed as the result of an
     * action.
     *
     * @return The status.
     */
    public String getStatus()
    {
        return fdf.getString( COSName.STATUS );
    }

    /**
     * This will set the status string.
     *
     * @param status The new status string.
     */
    public void setStatus( String status )
    {
        fdf.setString( COSName.STATUS, status );
    }

    /**
     * This will get the list of FDF Pages.  This will return a list of FDFPage objects.
     *
     * @return A list of FDF pages.
     */
    public List<FDFPage> getPages()
    {
        List<FDFPage> retval = null;
        COSArray pageArray = (COSArray)fdf.getDictionaryObject( COSName.PAGES );
        if( pageArray != null )
        {
            List<FDFPage> pages = new ArrayList<FDFPage>();
            for( int i=0; i<pageArray.size(); i++ )
            {
                pages.add( new FDFPage( (COSDictionary)pageArray.get( i ) ) );
            }
            retval = new COSArrayList<FDFPage>( pages, pageArray );
        }
        return retval;
    }

    /**
     * This will set the list of pages.  This should be a list of FDFPage objects.
     *
     *
     * @param pages The list of pages.
     */
    public void setPages( List pages )
    {
        fdf.setItem( COSName.PAGES, COSArrayList.converterToCOSArray( pages ) );
    }

    /**
     * The encoding to be used for a FDF field.  The default is PDFDocEncoding
     * and this method will never return null.
     *
     * @return The encoding value.
     */
    public String getEncoding()
    {
        String encoding = fdf.getNameAsString( COSName.ENCODING );
        if( encoding == null )
        {
            encoding = "PDFDocEncoding";
        }
        return encoding;

    }

    /**
     * This will set the encoding.
     *
     * @param encoding The new encoding.
     */
    public void setEncoding( String encoding )
    {
        fdf.setName( COSName.ENCODING, encoding );
    }

    /**
     * This will get the list of FDF Annotations.  This will return a list of FDFAnnotation objects
     * or null if the entry is not set.
     *
     * @return A list of FDF annotations.
     *
     * @throws IOException If there is an error creating the annotation list.
     */
    public List<FDFAnnotation> getAnnotations() throws IOException
    {
        List<FDFAnnotation> retval = null;
        COSArray annotArray = (COSArray)fdf.getDictionaryObject( COSName.ANNOTS );
        if( annotArray != null )
        {
            List<FDFAnnotation> annots = new ArrayList<FDFAnnotation>();
            for( int i=0; i<annotArray.size(); i++ )
            {
                annots.add( FDFAnnotation.create( (COSDictionary)annotArray.getObject( i ) ) );
            }
            retval = new COSArrayList<FDFAnnotation>( annots, annotArray );
        }
        return retval;
    }

    /**
     * This will set the list of annotations.  This should be a list of FDFAnnotation objects.
     *
     *
     * @param annots The list of annotations.
     */
    public void setAnnotations( List<FDFAnnotationText> annots )
    {
        fdf.setItem( COSName.ANNOTS, COSArrayList.converterToCOSArray( annots ) );
    }

    /**
     * This will get the incremental updates since the PDF was last opened.
     *
     * @return The differences entry of the FDF dictionary.
     */
    public COSStream getDifferences()
    {
        return (COSStream)fdf.getDictionaryObject( COSName.DIFFERENCES );
    }

    /**
     * This will set the differences stream.
     *
     * @param diff The new differences stream.
     */
    public void setDifferences( COSStream diff )
    {
        fdf.setItem( COSName.DIFFERENCES, diff );
    }

    /**
     * This will get the target frame in the browser to open this document.
     *
     * @return The target frame.
     */
    public String getTarget()
    {
        return fdf.getString( COSName.TARGET );
    }

    /**
     * This will set the target frame in the browser to open this document.
     *
     * @param target The new target frame.
     */
    public void setTarget( String target )
    {
        fdf.setString( COSName.TARGET, target );
    }

    /**
     * This will get the list of embedded FDF entries, or null if the entry is null.
     * This will return a list of PDFileSpecification objects.
     *
     * @return A list of embedded FDF files.
     *
     * @throws IOException If there is an error creating the file spec.
     */
    public List<PDFileSpecification> getEmbeddedFDFs() throws IOException
    {
        List<PDFileSpecification> retval = null;
        COSArray embeddedArray = (COSArray)fdf.getDictionaryObject( COSName.EMBEDDED_FDFS );
        if( embeddedArray != null )
        {
            List<PDFileSpecification> embedded = new ArrayList<PDFileSpecification>();
            for( int i=0; i<embeddedArray.size(); i++ )
            {
                embedded.add( PDFileSpecification.createFS( embeddedArray.get( i ) ) );
            }
            retval = new COSArrayList<PDFileSpecification>( embedded, embeddedArray );
        }
        return retval;
    }

    /**
     * This will set the list of embedded FDFs.  This should be a list of
     * PDFileSpecification objects.
     *
     *
     * @param embedded The list of embedded FDFs.
     */
    public void setEmbeddedFDFs( List embedded )
    {
        fdf.setItem( COSName.EMBEDDED_FDFS, COSArrayList.converterToCOSArray( embedded ) );
    }

    /**
     * This will get the java script entry.
     *
     * @return The java script entry describing javascript commands.
     */
    public FDFJavaScript getJavaScript()
    {
        FDFJavaScript fs = null;
        COSDictionary dic = (COSDictionary)fdf.getDictionaryObject( COSName.JAVA_SCRIPT );
        if( dic != null )
        {
            fs = new FDFJavaScript( dic );
        }
        return fs;
    }

    /**
     * This will set the JavaScript entry.
     *
     * @param js The javascript entries.
     */
    public void setJavaScript( FDFJavaScript js )
    {
        fdf.setItem( COSName.JAVA_SCRIPT, js );
    }

}
