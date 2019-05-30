package com.hopding.pdflib.apache.pdfbox.contentstream.operator.state;

import java.io.IOException;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.OperatorProcessor;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.state.RenderingIntent;

/**
 * ri: Set the rendering intent.
 *
 * @author John Hewson
 */
public class SetRenderingIntent extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> operands) throws IOException
    {
        COSName value = (COSName)operands.get(0);
        context.getGraphicsState().setRenderingIntent(RenderingIntent.fromString(value.getName()));
    }

    @Override
    public String getName()
    {
        return "ri";
    }
}
