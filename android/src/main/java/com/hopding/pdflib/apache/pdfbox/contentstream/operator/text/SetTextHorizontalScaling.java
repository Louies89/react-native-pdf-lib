package com.hopding.pdflib.apache.pdfbox.contentstream.operator.text;

import java.util.List;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.OperatorProcessor;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;

import java.io.IOException;

/**
 * Tz: Set horizontal text scaling.
 *
 * @author Ben Litchfield
 */
public class SetTextHorizontalScaling extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
        COSNumber scaling = (COSNumber)arguments.get(0);
        context.getGraphicsState().getTextState().setHorizontalScaling(scaling.floatValue());
    }

    @Override
    public String getName()
    {
        return "Tz";
    }
}
