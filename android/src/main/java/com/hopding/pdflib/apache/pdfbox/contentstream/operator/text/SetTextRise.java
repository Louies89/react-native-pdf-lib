package com.hopding.pdflib.apache.pdfbox.contentstream.operator.text;

import java.util.List;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.OperatorProcessor;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;

import java.io.IOException;

/**
 * Ts: Set text rise.
 *
 * @author Ben Litchfield
 */
public class SetTextRise extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
        COSNumber rise = (COSNumber)arguments.get(0);
        context.getGraphicsState().getTextState().setRise( rise.floatValue() );
    }

    @Override
    public String getName()
    {
        return "Ts";
    }
}
