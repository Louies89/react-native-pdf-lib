package com.hopding.pdflib.apache.pdfbox.contentstream.operator.markedcontent;

import java.io.IOException;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.OperatorProcessor;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.text.PDFMarkedContentExtractor;

/**
 * EMC : Ends a marked-content sequence begun by BMC or BDC.
 *
 * @author Johannes Koch
 */
public class EndMarkedContentSequence extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
        if (this.context instanceof PDFMarkedContentExtractor)
        {
            ((PDFMarkedContentExtractor) this.context).endMarkedContentSequence();
        }
    }

    @Override
    public String getName()
    {
        return "EMC";
    }
}
