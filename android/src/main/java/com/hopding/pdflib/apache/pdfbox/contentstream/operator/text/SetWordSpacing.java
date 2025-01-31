package com.hopding.pdflib.apache.pdfbox.contentstream.operator.text;

import java.util.List;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.OperatorProcessor;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;

/**
 * Tw: Set word spacing.
 *
 * @author Laurent Huault
 */
public class SetWordSpacing extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments)
    {
        COSNumber wordSpacing = (COSNumber)arguments.get( 0 );
        context.getGraphicsState().getTextState().setWordSpacing( wordSpacing.floatValue() );
    }

    @Override
    public String getName()
    {
        return "Tw";
    }
}
