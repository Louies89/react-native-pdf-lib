package com.hopding.pdflib.apache.pdfbox.pdmodel.interactive.measurement;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;

/**
 * This class represents a rectlinear measure dictionary.
 */
public class PDRectlinearMeasureDictionary extends PDMeasureDictionary
{

    /**
     * The subtype of the rectlinear measure dictionary.
     */
    public static final String SUBTYPE = "RL";

    /**
     * Constructor.
     */
    public PDRectlinearMeasureDictionary()
    {
        this.setSubtype(SUBTYPE);
    }

    /**
     * Constructor.
     * 
     * @param dictionary the corresponding dictionary
     */
    public PDRectlinearMeasureDictionary(COSDictionary dictionary)
    {
        super(dictionary);
    }

    /**
     * This will return the scale ration.
     * 
     * @return the scale ratio.
     */
    public String getScaleRatio()
    {
        return this.getDictionary().getString(COSName.R);
    }

    /**
     * This will set the scale ration.
     * 
     * @param scaleRatio the scale ratio.
     */
    public void setScaleRatio(String scaleRatio)
    {
        this.getDictionary().setString(COSName.R, scaleRatio);
    }

    /**
     * This will return the changes along the x-axis.
     * 
     * @return changes along the x-axis
     */
    public PDNumberFormatDictionary[] getChangeXs()
    {
        COSArray x = (COSArray)this.getDictionary().getDictionaryObject("X");
        if (x != null)
        {
            PDNumberFormatDictionary[] retval =
                new PDNumberFormatDictionary[x.size()];
            for (int i = 0; i < x.size(); i++)
            {
                COSDictionary dic = (COSDictionary) x.get(i);
                retval[i] = new PDNumberFormatDictionary(dic);
            }
            return retval;
        }
        return null;
    }

    /**
     * This will set the changes along the x-axis.
     * 
     * @param changeXs changes along the x-axis
     */
    public void setChangeXs(PDNumberFormatDictionary[] changeXs)
    {
        COSArray array = new COSArray();
        for (PDNumberFormatDictionary changeX : changeXs)
        {
            array.add(changeX);
        }
        this.getDictionary().setItem("X", array);
    }

    /**
     * This will return the changes along the y-axis.
     * 
     * @return changes along the y-axis
     */
    public PDNumberFormatDictionary[] getChangeYs()
    {
        COSArray y = (COSArray)this.getDictionary().getDictionaryObject("Y");
        if (y != null)
        {
            PDNumberFormatDictionary[] retval =
                new PDNumberFormatDictionary[y.size()];
            for (int i = 0; i < y.size(); i++)
            {
                COSDictionary dic = (COSDictionary) y.get(i);
                retval[i] = new PDNumberFormatDictionary(dic);
            }
            return retval;
        }
        return null;
    }

    /**
     * This will set the changes along the y-axis.
     * 
     * @param changeYs changes along the y-axis
     */
    public void setChangeYs(PDNumberFormatDictionary[] changeYs)
    {
        COSArray array = new COSArray();
        for (PDNumberFormatDictionary changeY : changeYs)
        {
            array.add(changeY);
        }
        this.getDictionary().setItem("Y", array);
    }

    /**
     * This will return the distances.
     * 
     * @return distances
     */
    public PDNumberFormatDictionary[] getDistances()
    {
        COSArray d = (COSArray)this.getDictionary().getDictionaryObject("D");
        if (d != null)
        {
            PDNumberFormatDictionary[] retval =
                new PDNumberFormatDictionary[d.size()];
            for (int i = 0; i < d.size(); i++)
            {
                COSDictionary dic = (COSDictionary) d.get(i);
                retval[i] = new PDNumberFormatDictionary(dic);
            }
            return retval;
        }
        return null;
    }

    /**
     * This will set the distances.
     * 
     * @param distances distances
     */
    public void setDistances(PDNumberFormatDictionary[] distances)
    {
        COSArray array = new COSArray();
        for (PDNumberFormatDictionary distance : distances)
        {
            array.add(distance);
        }
        this.getDictionary().setItem("D", array);
    }

    /**
     * This will return the areas.
     * 
     * @return areas
     */
    public PDNumberFormatDictionary[] getAreas()
    {
        COSArray a = (COSArray)this.getDictionary().getDictionaryObject(COSName.A);
        if (a != null)
        {
            PDNumberFormatDictionary[] retval =
                new PDNumberFormatDictionary[a.size()];
            for (int i = 0; i < a.size(); i++)
            {
                COSDictionary dic = (COSDictionary) a.get(i);
                retval[i] = new PDNumberFormatDictionary(dic);
            }
            return retval;
        }
        return null;
    }

    /**
     * This will set the areas.
     * 
     * @param areas areas
     */
    public void setAreas(PDNumberFormatDictionary[] areas)
    {
        COSArray array = new COSArray();
        for (PDNumberFormatDictionary area : areas)
        {
            array.add(area);
        }
        this.getDictionary().setItem(COSName.A, array);
    }

    /**
     * This will return the angles.
     * 
     * @return angles
     */
    public PDNumberFormatDictionary[] getAngles()
    {
        COSArray t = (COSArray)this.getDictionary().getDictionaryObject("T");
        if (t != null)
        {
            PDNumberFormatDictionary[] retval =
                new PDNumberFormatDictionary[t.size()];
            for (int i = 0; i < t.size(); i++)
            {
                COSDictionary dic = (COSDictionary) t.get(i);
                retval[i] = new PDNumberFormatDictionary(dic);
            }
            return retval;
        }
        return null;
    }

    /**
     * This will set the angles.
     * 
     * @param angles angles
     */
    public void setAngles(PDNumberFormatDictionary[] angles)
    {
        COSArray array = new COSArray();
        for (PDNumberFormatDictionary angle : angles)
        {
            array.add(angle);
        }
        this.getDictionary().setItem("T", array);
    }

    /**
     * This will return the sloaps of a line.
     * 
     * @return the sloaps of a line
     */
    public PDNumberFormatDictionary[] getLineSloaps()
    {
        COSArray s = (COSArray)this.getDictionary().getDictionaryObject("S");
        if (s != null)
        {
            PDNumberFormatDictionary[] retval =
                new PDNumberFormatDictionary[s.size()];
            for (int i = 0; i < s.size(); i++)
            {
                COSDictionary dic = (COSDictionary) s.get(i);
                retval[i] = new PDNumberFormatDictionary(dic);
            }
            return retval;
        }
        return null;
    }

    /**
     * This will set the sloaps of a line.
     * 
     * @param lineSloaps the sloaps of a line
     */
    public void setLineSloaps(PDNumberFormatDictionary[] lineSloaps)
    {
        COSArray array = new COSArray();
        for (PDNumberFormatDictionary lineSloap : lineSloaps)
        {
            array.add(lineSloap);
        }
        this.getDictionary().setItem("S", array);
    }

    /**
     * This will return the origin of the coordinate system.
     * 
     * @return the origin
     */
    public float[] getCoordSystemOrigin()
    {
        COSArray o = (COSArray)this.getDictionary().getDictionaryObject("O");
        if (o != null)
        {
            return o.toFloatArray();
        }
        return null;
    }

    /**
     * This will set the origin of the coordinate system.
     * 
     * @param coordSystemOrigin the origin
     */
    public void setCoordSystemOrigin(float[] coordSystemOrigin)
    {
        COSArray array = new COSArray();
        array.setFloatArray(coordSystemOrigin);
        this.getDictionary().setItem("O", array);
    }

    /**
     * This will return the CYX factor.
     * 
     * @return CYX factor
     */
    public float getCYX()
    {
        return this.getDictionary().getFloat("CYX");
    }

    /**
     * This will set the CYX factor.
     * 
     * @param cyx CYX factor
     */
    public void setCYX(float cyx)
    {
        this.getDictionary().setFloat("CYX", cyx);
    }

}
