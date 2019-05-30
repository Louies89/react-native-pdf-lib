package com.hopding.pdflib.apache.pdfbox.contentstream.operator;

import java.io.IOException;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.PDXObject;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import com.hopding.pdflib.apache.pdfbox.text.PDFMarkedContentExtractor;

/**
 * Do: Draws an XObject.
 *
 * @author Ben Litchfield
 * @author Mario Ivankovits
 */
public class DrawObject extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
        COSName name = (COSName) arguments.get(0);

        PDXObject xobject = context.getResources().getXObject(name);
        if (context instanceof PDFMarkedContentExtractor)
        {
            ((PDFMarkedContentExtractor) context).xobject(xobject);
        }

        if(xobject instanceof PDFormXObject)
        {
            PDFormXObject form = (PDFormXObject)xobject;

            context.showForm(form);
        }
    }

    @Override
    public String getName()
    {
        return "Do";
    }
}
