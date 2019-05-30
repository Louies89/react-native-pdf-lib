package com.hopding.pdflib.apache.pdfbox.contentstream.operator.text;

import java.io.IOException;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.MissingOperandException;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.OperatorProcessor;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;
import com.hopding.pdflib.apache.pdfbox.pdmodel.font.PDFont;

/**
 * Tf: Set text font and size.
 *
 * @author Laurent Huault
 */
public class SetFontAndSize extends OperatorProcessor
{
	@Override
	public void process(Operator operator, List<COSBase> arguments) throws IOException
	{
		if (arguments.size() < 2)
		{
			throw new MissingOperandException(operator, arguments);
		}
		// set font and size
		COSName fontName = (COSName)arguments.get(0);
		float fontSize = ((COSNumber)arguments.get(1)).floatValue();
		context.getGraphicsState().getTextState().setFontSize(fontSize);
		PDFont font = context.getResources().getFont(fontName);
		context.getGraphicsState().getTextState().setFont(font);
	}

	@Override
	public String getName()
	{
		return "Tf";
	}
}