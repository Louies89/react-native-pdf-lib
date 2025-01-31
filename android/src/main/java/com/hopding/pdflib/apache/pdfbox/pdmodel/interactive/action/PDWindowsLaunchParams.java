package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.action;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;

import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;

/**
 * Launch paramaters for the windows OS.
 *
 * @author Ben Litchfield
 */
public class PDWindowsLaunchParams implements COSObjectable
{
    /**
     * The open operation for the launch.
     */
    public static final String OPERATION_OPEN = "open";
    /**
     * The print operation for the lanuch.
     */
    public static final String OPERATION_PRINT = "print";

    /**
     * The params dictionary.
     */
    protected COSDictionary params;

    /**
     * Default constructor.
     */
    public PDWindowsLaunchParams()
    {
        params = new COSDictionary();
    }

    /**
     * Constructor.
     *
     * @param p The params dictionary.
     */
    public PDWindowsLaunchParams( COSDictionary p )
    {
        params = p;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSBase getCOSObject()
    {
        return params;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return The cos object that matches this Java object.
     */
    public COSDictionary getCOSDictionary()
    {
        return params;
    }

    /**
     * The file to launch.
     *
     * @return The executable/document to launch.
     */
    public String getFilename()
    {
        return params.getString( "F" );
    }

    /**
     * Set the file to launch.
     *
     * @param file The executable/document to launch.
     */
    public void setFilename( String file )
    {
        params.setString( "F", file );
    }

    /**
     * The dir to launch from.
     *
     * @return The dir of the executable/document to launch.
     */
    public String getDirectory()
    {
        return params.getString( "D" );
    }

    /**
     * Set the dir to launch from.
     *
     * @param dir The dir of the executable/document to launch.
     */
    public void setDirectory( String dir )
    {
        params.setString( "D", dir );
    }

    /**
     * Get the operation to perform on the file.  This method will not return null,
     * OPERATION_OPEN is the default.
     *
     * @return The operation to perform for the file.
     * @see PDWindowsLaunchParams#OPERATION_OPEN
     * @see PDWindowsLaunchParams#OPERATION_PRINT
     */
    public String getOperation()
    {
        return params.getString( "O", OPERATION_OPEN );
    }

    /**
     * Set the operation to perform..
     *
     * @param op The operation to perform on the file.
     */
    public void setOperation( String op )
    {
        params.setString( "D", op );
    }

    /**
     * A parameter to pass the executable.
     *
     * @return The parameter to pass the executable.
     */
    public String getExecuteParam()
    {
        return params.getString( "P" );
    }

    /**
     * Set the parameter to pass the executable.
     *
     * @param param The parameter for the executable.
     */
    public void setExecuteParam( String param )
    {
        params.setString( "P", param );
    }
}
