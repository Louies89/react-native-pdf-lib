package com.hopding.pdflib.apache.pdfbox.contentstream.operator.state;

import java.io.IOException;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.OperatorProcessor;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;

/**
 * Q: Restore the graphics state.
 * 
 * @author Laurent Huault
 */
public class Restore extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
        if (context.getGraphicsStackSize() > 1)
        {
            context.restoreGraphicsState();
        }
        else
        {
            // this shouldn't happen but it does, see PDFBOX-161
        	throw new EmptyGraphicsStackException();
        }
    }

    @Override
    public String getName()
    {
        return "Q";
    }
}
