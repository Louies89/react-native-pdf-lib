package com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.shading;

import java.io.IOException;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDRectangle;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.function.PDFunction;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

/**
 * A Shading Resource.
 */
public abstract class PDShading implements COSObjectable
{
    private COSDictionary dictionary;
    private COSArray background = null;
    private PDRectangle bBox = null;
    private PDColorSpace colorSpace = null;
    private PDFunction function = null;
    private PDFunction[] functionArray = null;

    /**
     * shading type 1 = function based shading.
     */
    public static final int SHADING_TYPE1 = 1;

    /**
     * shading type 2 = axial shading.
     */
    public static final int SHADING_TYPE2 = 2;

    /**
     * shading type 3 = radial shading.
     */
    public static final int SHADING_TYPE3 = 3;

    /**
     * shading type 4 = Free-Form Gouraud-Shaded Triangle Meshes.
     */
    public static final int SHADING_TYPE4 = 4;

    /**
     * shading type 5 = Lattice-Form Gouraud-Shaded Triangle Meshes.
     */
    public static final int SHADING_TYPE5 = 5;

    /**
     * shading type 6 = Coons Patch Meshes.
     */
    public static final int SHADING_TYPE6 = 6;

    /**
     * shading type 7 = Tensor-Product Patch Meshes.
     */
    public static final int SHADING_TYPE7 = 7;

    /**
     * Default constructor.
     */
    public PDShading()
    {
        dictionary = new COSDictionary();
    }

    /**
     * Constructor using the given shading dictionary.
     *
     * @param shadingDictionary the dictionary for this shading
     */
    public PDShading(COSDictionary shadingDictionary)
    {
        dictionary = shadingDictionary;
    }

    /**
     * This will get the underlying dictionary.
     *
     * @return the dictionary for this shading
     */
    public COSDictionary getCOSDictionary()
    {
        return dictionary;
    }

    /**
     * Convert this standard java object to a COS object.
     *
     * @return the cos object that matches this Java object
     */
    public COSBase getCOSObject()
    {
        return dictionary;
    }

    /**
     * This will return the type.
     *
     * @return the type of object that this is
     */
    public String getType()
    {
        return COSName.SHADING.getName();
    }

    /**
     * This will set the shading type.
     *
     * @param shadingType the new shading type
     */
    public void setShadingType(int shadingType)
    {
        dictionary.setInt(COSName.SHADING_TYPE, shadingType);
    }

    /**
     * This will return the shading type.
     *
     * @return the shading typ
     */
    public abstract int getShadingType();

    /**
     * This will set the background.
     *
     * @param newBackground the new background
     */
    public void setBackground(COSArray newBackground)
    {
        background = newBackground;
        dictionary.setItem(COSName.BACKGROUND, newBackground);
    }

    /**
     * This will return the background.
     *
     * @return the background
     */
    public COSArray getBackground()
    {
        if (background == null)
        {
            background = (COSArray) dictionary.getDictionaryObject(COSName.BACKGROUND);
        }
        return background;
    }

    /**
     * An array of four numbers in the form coordinate system (see below),
     * giving the coordinates of the left, bottom, right, and top edges,
     * respectively, of the shading's bounding box.
     *
     * @return the BBox of the form
     */
    public PDRectangle getBBox()
    {
        if (bBox == null)
        {
            COSArray array = (COSArray) dictionary.getDictionaryObject(COSName.BBOX);
            if (array != null)
            {
                bBox = new PDRectangle(array);
            }
        }
        return bBox;
    }

    /**
     * This will set the BBox (bounding box) for this Shading.
     *
     * @param newBBox the new BBox
     */
    public void setBBox(PDRectangle newBBox)
    {
        bBox = newBBox;
        if (bBox == null)
        {
            dictionary.removeItem(COSName.BBOX);
        }
        else
        {
            dictionary.setItem(COSName.BBOX, bBox.getCOSArray());
        }
    }

    /**
     * This will set the AntiAlias value.
     *
     * @param antiAlias the new AntiAlias value
     */
    public void setAntiAlias(boolean antiAlias)
    {
        dictionary.setBoolean(COSName.ANTI_ALIAS, antiAlias);
    }

    /**
     * This will return the AntiAlias value.
     *
     * @return the AntiAlias value
     */
    public boolean getAntiAlias()
    {
        return dictionary.getBoolean(COSName.ANTI_ALIAS, false);
    }

    /**
     * This will get the color space or null if none exists.
     *
     * @return the color space for the shading
     * @throws IOException if there is an error getting the color space
     */
//    public PDColorSpace getColorSpace() throws IOException
//    {
//        if (colorSpace == null)
//        {
//            COSBase colorSpaceDictionary = dictionary.getDictionaryObject(COSName.CS, COSName.COLORSPACE);
//            colorSpace = PDColorSpace.create(colorSpaceDictionary);
//        }
//        return colorSpace;
//    }TODO

    /**
     * This will set the color space for the shading.
     *
     * @param colorSpace the color space
     */
    public void setColorSpace(PDColorSpace colorSpace)
    {
    	this.colorSpace = colorSpace;
    	if (colorSpace != null)
    	{
    		dictionary.setItem(COSName.COLORSPACE, colorSpace.getCOSObject());
    	}
    	else
    	{
    		dictionary.removeItem(COSName.COLORSPACE);
    	}
    }

    /**
     * Create the correct PD Model shading based on the COS base shading.
     *
     * @param resourceDictionary the COS shading dictionary
     * @return the newly created shading resources object
     * @throws IOException if we are unable to create the PDShading object
     */
    public static PDShading create(COSDictionary resourceDictionary) throws IOException
    {
        PDShading shading = null;
        int shadingType = resourceDictionary.getInt(COSName.SHADING_TYPE, 0);
        switch (shadingType)
        {
            case SHADING_TYPE1:
                shading = new PDShadingType1(resourceDictionary);
                break;
            case SHADING_TYPE2:
                shading = new PDShadingType2(resourceDictionary);
                break;
            case SHADING_TYPE3:
                shading = new PDShadingType3(resourceDictionary);
                break;
            case SHADING_TYPE4:
                shading = new PDShadingType4(resourceDictionary);
                break;
            case SHADING_TYPE5:
                shading = new PDShadingType5(resourceDictionary);
                break;
            case SHADING_TYPE6:
                shading = new PDShadingType6(resourceDictionary);
                break;
            case SHADING_TYPE7:
                shading = new PDShadingType7(resourceDictionary);
                break;
            default:
                throw new IOException("Error: Unknown shading type " + shadingType);
        }
        return shading;
    }

    /**
     * This will set the function for the color conversion.
     *
     * @param newFunction the new function
     */
    public void setFunction(PDFunction newFunction)
    {
        functionArray = null;
        function = newFunction;
        if (newFunction == null)
        {
            getCOSDictionary().removeItem(COSName.FUNCTION);
        }
        else
        {
            getCOSDictionary().setItem(COSName.FUNCTION, newFunction);
        }
    }

    /**
     * This will set the functions COSArray for the color conversion.
     *
     * @param newFunctions the new COSArray containing all functions
     */
    public void setFunction(COSArray newFunctions)
    {
        functionArray = null;
        function = null;
        if (newFunctions == null)
        {
            getCOSDictionary().removeItem(COSName.FUNCTION);
        }
        else
        {
            getCOSDictionary().setItem(COSName.FUNCTION, newFunctions);
        }
    }

    /**
     * This will return the function used to convert the color values.
     *
     * @return the function
     * @exception IOException if we are unable to create the PDFunction object
     */
    public PDFunction getFunction() throws IOException
    {
        if (function == null)
        {
            COSBase dictionaryFunctionObject = getCOSDictionary().getDictionaryObject(COSName.FUNCTION);
            if (dictionaryFunctionObject != null)
            {
                function = PDFunction.create(dictionaryFunctionObject);
            }
        }
        return function;
    }

    /**
     * Provide the function(s) of the shading dictionary as array.
     *
     * @return an array containing the function(s)
     * @throws IOException if something went wrong
     */
    private PDFunction[] getFunctionsArray() throws IOException
    {
        if (functionArray == null)
        {
            COSBase functionObject = getCOSDictionary().getDictionaryObject(COSName.FUNCTION);
            if (functionObject instanceof COSDictionary)
            {
                functionArray = new PDFunction[1];
                functionArray[0] = PDFunction.create(functionObject);
            }
            else
            {
                COSArray functionCOSArray = (COSArray) functionObject;
                int numberOfFunctions = functionCOSArray.size();
                functionArray = new PDFunction[numberOfFunctions];
                for (int i = 0; i < numberOfFunctions; i++)
                {
                    functionArray[i] = PDFunction.create(functionCOSArray.get(i));
                }
            }
        }
        return functionArray;
    }

    /**
     * Convert the input value using the functions of the shading dictionary.
     *
     * @param inputValue the input value
     * @return the output values
     * @throws IOException thrown if something went wrong
     */
    public float[] evalFunction(float inputValue) throws IOException
    {
    	return evalFunction(new float[] { inputValue });
    }

    /**
     * Convert the input values using the functions of the shading dictionary.
     *
     * @param input the input values
     * @return the output values
     * @throws IOException thrown if something went wrong
     */
    public float[] evalFunction(float[] input) throws IOException
    {
        PDFunction[] functions = getFunctionsArray();
        int numberOfFunctions = functions.length;
        float[] returnValues = null;
        if (numberOfFunctions == 1)
        {
            returnValues = functions[0].eval(input);
        }
        else
        {
            returnValues = new float[numberOfFunctions];
            for (int i = 0; i < numberOfFunctions; i++)
            {
                float[] newValue = functions[i].eval(input);
                returnValues[i] = newValue[0];
            }
        }
        // From the PDF spec:
        // "If the value returned by the function for a given colour component 
        // is out of range, it shall be adjusted to the nearest valid value."
        for (int i = 0; i < returnValues.length; ++i)
        {
            if (returnValues[i] < 0)
            {
                returnValues[i] = 0;
            }
            else if (returnValues[i] > 1)
            {
                returnValues[i] = 1;
            }
        }
        return returnValues;
    }

    /**
     * Returns an AWT paint which corresponds to this shading
     *
     * @param matrix the pattern matrix concatenated with that of the parent content stream,
     * this matrix which maps the pattern's internal coordinate system to user space
     * @return an AWT Paint instance
     */
//    public abstract Paint toPaint(Matrix matrix);TODO
}
