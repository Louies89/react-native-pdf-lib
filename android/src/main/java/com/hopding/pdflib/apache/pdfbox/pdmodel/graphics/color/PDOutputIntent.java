package com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color;

import java.io.IOException;
import java.io.InputStream;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDStream;

/**
 * An Output Intent describes the colour reproduction characteristics of a possible output
 * device or production condition.
 * Output intents provide a means for matching the colour characteristics of a PDF document with
 * those of a target output device or production environment in which the document will be printed.
 *
 * @author Guillaume Bailleul
 */
public final class PDOutputIntent implements COSObjectable
{
    private COSDictionary dictionary;

    public PDOutputIntent(PDDocument doc, InputStream colorProfile) throws IOException
    {
        dictionary = new COSDictionary();
        dictionary.setItem(COSName.TYPE, COSName.OUTPUT_INTENT);
        dictionary.setItem(COSName.S, COSName.GTS_PDFA1);
        PDStream destOutputIntent = configureOutputProfile(doc, colorProfile);
        dictionary.setItem(COSName.DEST_OUTPUT_PROFILE, destOutputIntent);
    }

    public PDOutputIntent(COSDictionary dictionary)
    {
        this.dictionary = dictionary;
    }

    public COSBase getCOSObject()
    {
        return dictionary;
    }

    public COSStream getDestOutputIntent()
    {
        return (COSStream) dictionary.getItem(COSName.DEST_OUTPUT_PROFILE);
    }

    public String getInfo()
    {
        return dictionary.getString(COSName.INFO);
    }

    public void setInfo(String value)
    {
        dictionary.setString(COSName.INFO, value);
    }

    public String getOutputCondition()
    {
        return dictionary.getString(COSName.OUTPUT_CONDITION);
    }

    public void setOutputCondition(String value)
    {
        dictionary.setString(COSName.OUTPUT_CONDITION, value);
    }

    public String getOutputConditionIdentifier()
    {
        return dictionary.getString(COSName.OUTPUT_CONDITION_IDENTIFIER);
    }

    public void setOutputConditionIdentifier(String value)
    {
        dictionary.setString(COSName.OUTPUT_CONDITION_IDENTIFIER, value);
    }

    public String getRegistryName()
    {
        return dictionary.getString(COSName.REGISTRY_NAME);
    }

    public void setRegistryName(String value)
    {
        dictionary.setString(COSName.REGISTRY_NAME, value);
    }

    private PDStream configureOutputProfile(PDDocument doc, InputStream colorProfile)
            throws IOException
    {
        PDStream stream = new PDStream(doc, colorProfile, false);
        stream.getStream().setFilters(COSName.FLATE_DECODE);
        stream.getStream().setInt(COSName.LENGTH, stream.getByteArray().length);
        stream.getStream().setInt(COSName.N, 3);
        stream.addCompression();
        return stream;
    }
}  
