package com.hopding.pdflib.apache.pdfbox.contentstream.operator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;

/**
 * An Operator in a PDF content stream.
 *
 * @author Ben Litchfield
 */
public final class Operator
{
    private final String theOperator;
    private byte[] imageData;
    private COSDictionary imageParameters;

    /** map for singleton operator objects; use {@link ConcurrentHashMap} for better scalability with multiple threads */
    private static final ConcurrentMap<String,Operator> operators = new ConcurrentHashMap<String, Operator>();

    /**
     * Constructor.
     *
     * @param aOperator The operator that this object will represent.
     */
    private Operator(String aOperator)
    {
        theOperator = aOperator;
        if( aOperator.startsWith( "/" ) )
        {
            throw new RuntimeException( "Operators are not allowed to start with / '" + aOperator + "'" );
        }
    }

    /**
     * This is used to create/cache operators in the system.
     *
     * @param operator The operator for the system.
     *
     * @return The operator that matches the operator keyword.
     */
    public static Operator getOperator( String operator )
    {
        Operator operation;
        if( operator.equals( "ID" ) || operator.equals( "BI" ) )
        {
            //we can't cache the ID operators.
            operation = new Operator( operator );
        }
        else
        {
            operation = operators.get( operator );
            if( operation == null )
            {
                // another thread may has already added an operator of this kind
                // make sure that we get the same operator
                operation = operators.putIfAbsent( operator, new Operator( operator ) );
                if ( operation == null )
                {
                    operation = operators.get( operator );
                }
            }
        }

        return operation;
    }

    /**
     * This will get the name of the operator.
     *
     * @return The string representation of the operation.
     */
    public String getName()
    {
        return theOperator;
    }

    /**
     * This will print a string rep of this class.
     *
     * @return A string rep of this class.
     */
    @Override
    public String toString()
    {
        return "PDFOperator{" + theOperator + "}";
    }

    /**
     * This is the special case for the ID operator where there are just random
     * bytes inlined the stream.
     *
     * @return Value of property imageData.
     */
    public byte[] getImageData()
    {
        return this.imageData;
    }

    /**
     * This will set the image data, this is only used for the ID operator.
     *
     * @param imageDataArray New value of property imageData.
     */
    public void setImageData(byte[] imageDataArray)
    {
        imageData = imageDataArray;
    }

    /**
     * This will get the image parameters, this is only valid for BI operators.
     *
     * @return The image parameters.
     */
    public COSDictionary getImageParameters()
    {
        return imageParameters;
    }

    /**
     * This will set the image parameters, this is only valid for BI operators.
     *
     * @param params The image parameters.
     */
    public void setImageParameters( COSDictionary params)
    {
        imageParameters = params;
    }
}
