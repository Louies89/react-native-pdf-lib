package com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics;

import java.io.IOException;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.MissingResourceException;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.PDXObject;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * Do: Draws an XObject.
 *
 * @author Ben Litchfield
 * @author John Hewson
 */
public final class DrawObject extends GraphicsOperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> operands) throws IOException
    {
        COSName objectName = (COSName)operands.get(0);
        PDXObject xobject = context.getResources().getXObject(objectName);

        if (xobject == null)
        {
            throw new MissingResourceException("Missing XObject: " + objectName.getName());
        }
        else if (xobject instanceof PDImageXObject)
        {
            PDImageXObject image = (PDImageXObject)xobject;
            context.drawImage(image);
        }
        else if (xobject instanceof PDFormXObject)
        {
            PDFormXObject form = (PDFormXObject) xobject;
            if (form.getGroup() != null &&
                COSName.TRANSPARENCY.equals(form.getGroup().getSubType()))
            {
                getContext().showTransparencyGroup(form);
            }
            else
            {
                getContext().showForm(form);
            }
        }
    }

    @Override
    public String getName()
    {
        return "Do";
    }
}
