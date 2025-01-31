package com.hopding.pdflib.apache.pdfbox.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.util.Hex;

import android.util.Log;

/**
 * Decodes data encoded in an ASCII hexadecimal form, reproducing the original binary data.
 * 
 * @author Ben Litchfield
 */
final class ASCIIHexFilter extends Filter
{
    private static final int[] REVERSE_HEX = {
      /*   0 */  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      /*  10 */  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      /*  20 */  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      /*  30 */  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      /*  40 */  -1, -1, -1, -1, -1, -1, -1, -1,  0,  1,
      /*  50 */   2,  3,  4,  5,  6,  7,  8,  9, -1, -1,
      /*  60 */  -1, -1, -1, -1, -1, 10, 11, 12, 13, 14,
      /*  70 */  15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      /*  80 */  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      /*  90 */  -1, -1, -1, -1, -1, -1, -1, 10, 11, 12,
      /* 100 */  13, 14, 15
    };

    @Override
    public DecodeResult decode(InputStream encoded, OutputStream decoded,
                                         COSDictionary parameters, int index) throws IOException
    {
        int value, firstByte, secondByte;
        while ((firstByte = encoded.read()) != -1)
        {
            // always after first char
            while (isWhitespace(firstByte))
            {
                firstByte = encoded.read();
            }
            if (firstByte == -1 || isEOD(firstByte))
            {
                break;
            }
       
            if (REVERSE_HEX[firstByte] == -1)
            {
                Log.e("PdfBoxAndroid", "Invalid hex, int: " + firstByte + " char: " + (char)firstByte);
            }
            value = REVERSE_HEX[firstByte] * 16;
            secondByte = encoded.read();
       
            if (secondByte == -1 || isEOD(secondByte)) 
            {
                // second value behaves like 0 in case of EOD
                decoded.write(value);
                break;
            }
            if (secondByte >= 0) 
            {
                if (REVERSE_HEX[secondByte] == -1)
                {
                	Log.e("PdfBoxAndroid", "Invalid hex, int: " + secondByte + " char: " + (char)secondByte);
                }
                value += REVERSE_HEX[secondByte];
            }
            decoded.write(value);
        }
        decoded.flush();
        return new DecodeResult(parameters);
    }

    // whitespace
    //   0  0x00  Null (NUL)
    //   9  0x09  Tab (HT)
    //  10  0x0A  Line feed (LF)
    //  12  0x0C  Form feed (FF)
    //  13  0x0D  Carriage return (CR)
    //  32  0x20  Space (SP)
    private static boolean isWhitespace(int c)
    {
        return c == 0 || c == 9 || c == 10 || c == 12 || c == 13 || c == 32;
    }

    private static boolean isEOD(int c)
    {
        return c == '>';
    }

    @Override
    public void encode(InputStream input, OutputStream encoded, COSDictionary parameters)
        throws IOException
    {
        int byteRead;
        while ((byteRead = input.read()) != -1)
        {
        	encoded.write(Hex.getBytes((byte)byteRead));
        }
        encoded.flush();
    }
}
