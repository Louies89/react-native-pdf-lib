package com.hopding.pdflib.apache.pdfbox.pdmodel.common.function;

import java.io.IOException;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;

/**
 * The identity function.
 *
 * @author Tilman Hausherr
 */
public class PDFunctionTypeIdentity extends PDFunction
{

    public PDFunctionTypeIdentity(COSBase function)
    {
        super(null);
    }

    @Override
    public int getFunctionType()
    {
        // shouldn't be called
        throw new UnsupportedOperationException();
    }

    @Override
    public float[] eval(float[] input) throws IOException
    {
        return input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "FunctionTypeIdentity";
    }

}
