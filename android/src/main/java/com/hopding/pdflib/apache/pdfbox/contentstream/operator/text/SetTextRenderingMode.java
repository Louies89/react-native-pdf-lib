package com.hopding.pdflib.apache.pdfbox.contentstream.operator.text;

import java.util.List;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.OperatorProcessor;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import java.io.IOException;

/**
 * Tr: Set text rendering mode.
 *
 * @author Ben Litchfield
 */
public class SetTextRenderingMode extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
        COSNumber mode = (COSNumber)arguments.get(0);
        RenderingMode renderingMode = RenderingMode.fromInt(mode.intValue());
        context.getGraphicsState().getTextState().setRenderingMode(renderingMode);
    }

    @Override
    public String getName()
    {
        return "Tr";
    }
}
