package com.hopding.pdflib.apache.pdfbox.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;

import android.util.Log;

/**
 * A filter for stream data.
 *
 * @author Ben Litchfield
 * @author John Hewson
 */
public abstract class Filter
{
    protected Filter()
    {
    }

    /**
     * Decodes data, producing the original non-encoded data.
     * @param encoded the encoded byte stream
     * @param decoded the stream where decoded data will be written
     * @param parameters the parameters used for decoding
     * @param index the index to the filter being decoded
     * @return repaired parameters dictionary, or the original parameters dictionary
     * @throws IOException if the stream cannot be decoded
     */
    public abstract DecodeResult decode(InputStream encoded, OutputStream decoded, COSDictionary parameters,
                            int index) throws IOException;

    /**
     * Encodes data.
     * @param input the byte stream to encode
     * @param encoded the stream where encoded data will be written
     * @param parameters the parameters used for encoding
     * @param index the index to the filter being encoded
     * @throws IOException if the stream cannot be encoded
     */
    public final void encode(InputStream input, OutputStream encoded, COSDictionary parameters,
                            int index) throws IOException
    {
        encode(input, encoded, parameters.asUnmodifiableDictionary());
    }

    // implemented in subclasses
    protected abstract void encode(InputStream input, OutputStream encoded,
                                   COSDictionary parameters) throws IOException;

    // gets the decode params for a specific filter index, this is used to
    // normalise the DecodeParams entry so that it is always a dictionary
    protected static COSDictionary getDecodeParams(COSDictionary dictionary, int index)
    {
        COSBase obj = dictionary.getDictionaryObject(COSName.DECODE_PARMS, COSName.DP);
        if (obj instanceof COSDictionary)
        {
            return (COSDictionary)obj;
        }
        else if (obj instanceof COSArray)
        {
            COSArray array = (COSArray)obj;
            if (index < array.size())
            {
                return (COSDictionary)array.getObject(index);
            }
        }
        else if (obj != null)
        {
        	Log.e("PdfBoxAndroid", "Expected DecodeParams to be an Array or Dictionary but found " +
                      obj.getClass().getName());
        }
        return new COSDictionary();
    }

    /**
     * Finds a suitable image reader for a format.
     *
     * @param formatName The format to search for.
     * @param errorCause The probably cause if something goes wrong.
     * @return The image reader for the format.
     * @throws MissingImageReaderException if no image reader is found.
     */
//    protected static ImageReader findImageReader(String formatName, String errorCause) throws MissingImageReaderException
//    {
//        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(formatName);
//        ImageReader reader = null;
//        while (readers.hasNext())
//        {
//            reader = readers.next();
//            if (reader.canReadRaster())
//            {
//                break;
//            }
//        }
//        if (reader == null)
//        {
//            throw new MissingImageReaderException("Cannot read " + formatName + " image: " + errorCause);
//        }
//        return reader;
//    }TODO

}
