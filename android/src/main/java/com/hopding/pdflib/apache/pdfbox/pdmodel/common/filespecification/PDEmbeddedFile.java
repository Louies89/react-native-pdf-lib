package com.hopding.pdflib.apache.pdfbox.pdmodel.common.filespecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDStream;

/**
 * This represents an embedded file in a file specification.
 *
 * @author Ben Litchfield
 */
public class PDEmbeddedFile extends PDStream
{

    /**
     * Constructor.
     *
     * @param document {@inheritDoc}
     */
    public PDEmbeddedFile( PDDocument document )
    {
        super( document );
        getStream().setName(COSName.TYPE, "EmbeddedFile" );

    }

    /**
     * Constructor.
     *
     * @param str The stream parameter.
     */
    public PDEmbeddedFile( COSStream str )
    {
        super( str );
    }

    /**
     * Constructor.
     *
     * @param doc {@inheritDoc}
     * @param str {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     */
    public PDEmbeddedFile( PDDocument doc, InputStream str  ) throws IOException
    {
        super( doc, str );
        getStream().setName(COSName.TYPE, "EmbeddedFile" );
    }

    /**
     * Constructor.
     *
     * @param doc {@inheritDoc}
     * @param str {@inheritDoc}
     * @param filtered {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     */
    public PDEmbeddedFile( PDDocument doc, InputStream str, boolean filtered ) throws IOException
    {
        super( doc, str, filtered );
        getStream().setName(COSName.TYPE, "EmbeddedFile" );
    }

    /**
     * Set the subtype for this embedded file.  This should be a mime type value.  Optional.
     *
     * @param mimeType The mimeType for the file.
     */
    public void setSubtype( String mimeType )
    {
        getStream().setName(COSName.SUBTYPE, mimeType );
    }

    /**
     * Get the subtype(mimetype) for the embedded file.
     *
     * @return The type of embedded file.
     */
    public String getSubtype()
    {
        return getStream().getNameAsString(COSName.SUBTYPE );
    }

    /**
     * Get the size of the embedded file.
     *
     * @return The size of the embedded file.
     */
    public int getSize()
    {
        return getStream().getEmbeddedInt( "Params", "Size" );
    }

    /**
     * Set the size of the embedded file.
     *
     * @param size The size of the embedded file.
     */
    public void setSize( int size )
    {
        getStream().setEmbeddedInt( "Params", "Size", size );
    }

    /**
     * Get the creation date of the embedded file.
     *
     * @return The Creation date.
     * @throws IOException If there is an error while constructing the date.
     */
    public Calendar getCreationDate() throws IOException
    {
        return getStream().getEmbeddedDate( "Params", "CreationDate" );
    }

    /**
     * Set the creation date.
     *
     * @param creation The new creation date.
     */
    public void setCreationDate( Calendar creation )
    {
        getStream().setEmbeddedDate( "Params", "CreationDate", creation );
    }

    /**
     * Get the mod date of the embedded file.
     *
     * @return The mod date.
     * @throws IOException If there is an error while constructing the date.
     */
    public Calendar getModDate() throws IOException
    {
        return getStream().getEmbeddedDate( "Params", "ModDate" );
    }

    /**
     * Set the mod date.
     *
     * @param mod The new creation mod.
     */
    public void setModDate( Calendar mod )
    {
        getStream().setEmbeddedDate( "Params", "ModDate", mod );
    }

    /**
     * Get the check sum of the embedded file.
     *
     * @return The check sum of the file.
     */
    public String getCheckSum()
    {
        return getStream().getEmbeddedString( "Params", "CheckSum" );
    }

    /**
     * Set the check sum.
     *
     * @param checksum The checksum of the file.
     */
    public void setCheckSum( String checksum )
    {
        getStream().setEmbeddedString( "Params", "CheckSum", checksum );
    }

    /**
     * Get the mac subtype.
     *
     * @return The mac subtype.
     */
    public String getMacSubtype()
    {
        String retval = null;
        COSDictionary params = (COSDictionary)getStream().getDictionaryObject( COSName.PARAMS );
        if( params != null )
        {
            retval = params.getEmbeddedString( "Mac", "Subtype" );
        }
        return retval;
    }

    /**
     * Set the mac subtype.
     *
     * @param macSubtype The mac subtype.
     */
    public void setMacSubtype( String macSubtype )
    {
        COSDictionary params = (COSDictionary)getStream().getDictionaryObject( COSName.PARAMS );
        if( params == null && macSubtype != null )
        {
            params = new COSDictionary();
            getStream().setItem( COSName.PARAMS, params );
        }
        if( params != null )
        {
            params.setEmbeddedString( "Mac", "Subtype", macSubtype );
        }
    }

    /**
     * Get the mac Creator.
     *
     * @return The mac Creator.
     */
    public String getMacCreator()
    {
        String retval = null;
        COSDictionary params = (COSDictionary)getStream().getDictionaryObject( COSName.PARAMS );
        if( params != null )
        {
            retval = params.getEmbeddedString( "Mac", "Creator" );
        }
        return retval;
    }

    /**
     * Set the mac Creator.
     *
     * @param macCreator The mac Creator.
     */
    public void setMacCreator( String macCreator )
    {
        COSDictionary params = (COSDictionary)getStream().getDictionaryObject( COSName.PARAMS );
        if( params == null && macCreator != null )
        {
            params = new COSDictionary();
            getStream().setItem( COSName.PARAMS, params );
        }
        if( params != null )
        {
            params.setEmbeddedString( "Mac", "Creator", macCreator );
        }
    }

    /**
     * Get the mac ResFork.
     *
     * @return The mac ResFork.
     */
    public String getMacResFork()
    {
        String retval = null;
        COSDictionary params = (COSDictionary)getStream().getDictionaryObject( COSName.PARAMS );
        if( params != null )
        {
            retval = params.getEmbeddedString( "Mac", "ResFork" );
        }
        return retval;
    }

    /**
     * Set the mac ResFork.
     *
     * @param macResFork The mac ResFork.
     */
    public void setMacResFork( String macResFork )
    {
        COSDictionary params = (COSDictionary)getStream().getDictionaryObject( COSName.PARAMS );
        if( params == null && macResFork != null )
        {
            params = new COSDictionary();
            getStream().setItem( COSName.PARAMS, params );
        }
        if( params != null )
        {
            params.setEmbeddedString( "Mac", "ResFork", macResFork);
        }
    }
}