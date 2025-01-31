package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.action;

import java.io.IOException;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;

import com.hopding.pdflib.apache.pdfbox.pdmodel.common.filespecification.PDFileSpecification;

/**
 * This represents a remote go-to action that can be executed in a PDF document.
 *
 * @author Ben Litchfield
 * @author Panagiotis Toumasis
 */
public class PDActionRemoteGoTo extends PDAction
{
    /**
     * This type of action this object represents.
     */
    public static final String SUB_TYPE = "GoToR";

    /**
     * Default constructor.
     */
    public PDActionRemoteGoTo()
    {
        action = new COSDictionary();
        setSubType( SUB_TYPE );
    }

    /**
     * Constructor.
     *
     * @param a The action dictionary.
     */
    public PDActionRemoteGoTo( COSDictionary a )
    {
        super( a );
    }

    /**
     * This will get the type of action that the actions dictionary describes.
     * It must be GoToR for a remote go-to action.
     *
     * @return The S entry of the specific remote go-to action dictionary.
     */
    public String getS()
    {
       return action.getNameAsString( "S" );
    }

    /**
     * This will set the type of action that the actions dictionary describes.
     * It must be GoToR for a remote go-to action.
     *
     * @param s The remote go-to action.
     */
    public void setS( String s )
    {
       action.setName( "S", s );
    }

    /**
     * This will get the file in which the destination is located.
     *
     * @return The F entry of the specific remote go-to action dictionary.
     *
     * @throws IOException If there is an error creating the file spec.
     */
    public PDFileSpecification getFile() throws IOException
    {
        return PDFileSpecification.createFS( action.getDictionaryObject( "F" ) );
    }

    /**
     * This will set the file in which the destination is located.
     *
     * @param fs The file specification.
     */
    public void setFile( PDFileSpecification fs )
    {
        action.setItem( "F", fs );
    }

    /**
     * This will get the destination to jump to.
     * If the value is an array defining an explicit destination,
     * its first element must be a page number within the remote
     * document rather than an indirect reference to a page object
     * in the current document. The first page is numbered 0.
     *
     * @return The D entry of the specific remote go-to action dictionary.
     */

    // Array or String.
    public COSBase getD()
    {
        return action.getDictionaryObject( "D" );
    }

    /**
     * This will set the destination to jump to.
     * If the value is an array defining an explicit destination,
     * its first element must be a page number within the remote
     * document rather than an indirect reference to a page object
     * in the current document. The first page is numbered 0.
     *
     * @param d The destination.
     */

    // In case the value is an array.
    public void setD( COSBase d )
    {
        action.setItem( "D", d );
    }

    /**
     * This will specify whether to open the destination document in a new window.
     * If this flag is false, the destination document will replace the current
     * document in the same window. If this entry is absent, the viewer application
     * should behave in accordance with the current user preference.
     *
     * @return A flag specifying whether to open the destination document in a new window.
     */
    public boolean shouldOpenInNewWindow()
    {
        return action.getBoolean( "NewWindow", true );
    }

    /**
     * This will specify the destination document to open in a new window.
     *
     * @param value The flag value.
     */
    public void setOpenInNewWindow( boolean value )
    {
        action.setBoolean( "NewWindow", value );
    }
}
