package com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics;

import com.hopding.pdflib.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import com.hopding.pdflib.apache.pdfbox.contentstream.PDFStreamEngine;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.OperatorProcessor;

/**
 * Base class for graphics operators.
 *
 * @author John Hewson
 */
public abstract class GraphicsOperatorProcessor extends OperatorProcessor
{
    /** The processing context. */
    protected PDFGraphicsStreamEngine context;

    @Override
    public void setContext(PDFStreamEngine context)
    {
        super.setContext(context);
        this.context = (PDFGraphicsStreamEngine)context;
    }
}
