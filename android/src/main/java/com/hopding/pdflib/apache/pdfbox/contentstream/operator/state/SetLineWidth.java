package com.hopding.pdflib.apache.pdfbox.contentstream.operator.state;

import java.io.IOException;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.MissingOperandException;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.OperatorProcessor;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;

/**
 * w: Set line width.
 *
 * @author Ben Litchfield
 */
public class SetLineWidth extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
    	if (arguments.size() < 1)
    	{
    		throw new MissingOperandException(operator, arguments);
    	}
        COSNumber width = (COSNumber) arguments.get(0);
        context.getGraphicsState().setLineWidth(width.floatValue());
    }

    @Override
    public String getName()
    {
        return "w";
    }
}
