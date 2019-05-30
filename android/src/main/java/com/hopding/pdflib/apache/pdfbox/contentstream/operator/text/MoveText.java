package com.hopding.pdflib.apache.pdfbox.contentstream.operator.text;

import java.util.List;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.OperatorProcessor;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;
import com.hopding.pdflib.apache.pdfbox.util.Matrix;

/**
 * Td: Move text position.
 *
 * @author Laurent Huault
 */
public class MoveText extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments)
    {
    	COSNumber x = (COSNumber)arguments.get( 0 );
    	COSNumber y = (COSNumber)arguments.get( 1 );
    	Matrix matrix = new Matrix(1, 0, 0, 1, x.floatValue(), y.floatValue());
    	context.getTextLineMatrix().concatenate(matrix);
    	context.setTextMatrix(context.getTextLineMatrix().clone());
    }

    @Override
    public String getName()
    {
        return "Td";
    }
}
