package com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.io.RandomAccess;
import com.hopding.pdflib.apache.pdfbox.io.RandomAccessFile;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;

/**
 * Factory for creating a PDImageXObject containing a CCITT Fax compressed TIFF image.
 * 
 * @author Ben Litchfield
 * @author Paul King
 */
public final class CCITTFactory
{
    private CCITTFactory()
    {
    }
    
    /**
     * Creates a new CCITT Fax compressed Image XObject from the first page of
     * a TIFF file.
     *
     * @param document the document to create the image as part of.
     * @param reader the random access TIFF file which contains a suitable CCITT
     * compressed image
     * @return a new Image XObject
     * @throws IOException if there is an error reading the TIFF data.
     * 
     * @deprecated Use {@link #createFromFile(PDDocument, File)} instead.
     */
    @Deprecated
    public static PDImageXObject createFromRandomAccess(PDDocument document, RandomAccess reader)
    		throws IOException
    {
    	return createFromRandomAccessImpl(document, reader, 0);
    }

    /**
     * Creates a new CCITT Fax compressed Image XObject from a TIFF file.
     *
     * @param document the document to create the image as part of.
     * @param reader the random access TIFF file which contains a suitable CCITT
     * compressed image
     * @param number TIFF image number, starting from 0
     * @return a new Image XObject, or null if no such page
     * @throws IOException if there is an error reading the TIFF data.
     *
     * @deprecated Use {@link #createFromFile(PDDocument, File, int)} instead.
     */
    @Deprecated
    public static PDImageXObject createFromRandomAccess(PDDocument document, RandomAccess reader,
    		int number) throws IOException
    {
    	return createFromRandomAccessImpl(document, reader, number);
    }

    /**
     * Creates a new CCITT Fax compressed Image XObject from the first page of
     * a TIFF file.
     *
     * @param document the document to create the image as part of.
     * @param file the TIFF file which contains a suitable CCITT compressed image
     * @return a new Image XObject
     * @throws IOException if there is an error reading the TIFF data.
     */
    public static PDImageXObject createFromFile(PDDocument document, File file)
    		throws IOException
    {
    	return createFromRandomAccessImpl(document, new RandomAccessFile(file, "r"), 0);
    }

    /**
     * Creates a new CCITT Fax compressed Image XObject from the first page of 
     * a TIFF file.
     * 
     * @param document the document to create the image as part of.
     * @param file the TIFF file which contains a suitable CCITT compressed image
     * @param number TIFF image number, starting from 0
     * compressed image
     * @return a new Image XObject
     * @throws IOException if there is an error reading the TIFF data.
     */
    public static PDImageXObject createFromFile(PDDocument document, File file, int number)
            throws IOException
    {
    	return createFromRandomAccessImpl(document, new RandomAccessFile(file, "r"), number);
    }

    /**
     * Creates a new CCITT Fax compressed Image XObject from a TIFF file.
     * 
     * @param document the document to create the image as part of.
     * @param reader the random access TIFF file which contains a suitable CCITT
     * compressed image
     * @param number TIFF image number, starting from 0
     * @return a new Image XObject, or null if no such page
     * @throws IOException if there is an error reading the TIFF data.
     */
    private static PDImageXObject createFromRandomAccessImpl(PDDocument document,
    		RandomAccess reader,
    		int number) throws IOException
    {
        COSDictionary decodeParms = new COSDictionary();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        extractFromTiff(reader, bos, decodeParms, number);
        if (bos.size() == 0)
        {
            return null;
        }
        ByteArrayInputStream filteredByteStream = new ByteArrayInputStream(bos.toByteArray());
        PDImageXObject pdImage = new PDImageXObject(document, 
                filteredByteStream, 
                COSName.CCITTFAX_DECODE, 
                decodeParms.getInt(COSName.COLUMNS), 
                decodeParms.getInt(COSName.ROWS),
                1, PDDeviceGray.INSTANCE);
                
        
        COSDictionary dict = pdImage.getCOSStream();
        dict.setItem(COSName.DECODE_PARMS, decodeParms);
        return pdImage;
    }

    // extracts the CCITT stream from the TIFF file
    private static void extractFromTiff(RandomAccess reader, OutputStream os,
            COSDictionary params, int number) throws IOException
    {
        try
        {
            // First check the basic tiff header
            reader.seek(0);
            char endianess = (char) reader.read();
            if ((char) reader.read() != endianess)
            {
                throw new IOException("Not a valid tiff file");
            }
            // ensure that endianess is either M or I
            if (endianess != 'M' && endianess != 'I')
            {
                throw new IOException("Not a valid tiff file");
            }
            int magicNumber = readshort(endianess, reader);
            if (magicNumber != 42)
            {
                throw new IOException("Not a valid tiff file");
            }

            // Relocate to the first set of tags
            int address = readlong(endianess, reader);
            reader.seek(address);
    
            // If some higher page number is required, skip this page's tags, 
            // then read the next page's address
            for (int i = 0; i < number; i++)
            {
                int numtags = readshort(endianess, reader);
                if (numtags > 50)
                {
                    throw new IOException("Not a valid tiff file");
                }
                reader.seek(address + 2 + numtags * 12);
                address = readlong(endianess, reader);
                if (address == 0)
                {
                    return;
                }
                reader.seek(address);
            }

            int numtags = readshort(endianess, reader);

            // The number 50 is somewhat arbitary, it just stops us load up junk from somewhere
            // and tramping on
            if (numtags > 50)
            {
                throw new IOException("Not a valid tiff file");
            }

            // Loop through the tags, some will convert to items in the parms dictionary
            // Other point us to where to find the data stream
            // The only parm which might change as a result of other options is K, so
            // We'll deal with that as a special;
            int k = -1000; // Default Non CCITT compression
            int dataoffset = 0;
            int datalength = 0;

            for (int i = 0; i < numtags; i++)
            {
                int tag = readshort(endianess, reader);
                int type = readshort(endianess, reader);
                int count = readlong(endianess, reader);
                int val = readlong(endianess, reader); // See note

                // Note, we treated that value as a long. The value always occupies 4 bytes
                // But it might only use the first byte or two. Depending on endianess we might
                // need to correct.
                // Note we ignore all other types, they are of little interest for PDFs/CCITT Fax
                if (endianess == 'M')
                {
                    switch (type)
                    {
                        case 1:
                        {
                            val = val >> 24;
                            break; // byte value
                        }
                        case 3:
                        {
                            val = val >> 16;
                            break; // short value
                        }
                        case 4:
                        {
                            break; // long value
                        }
                        default:
                        {
                            // do nothing
                        }
                    }
                }
                switch (tag)
                {
                    case 256:
                    {
                        params.setInt(COSName.COLUMNS, val);
                        break;
                    }
                    case 257:
                    {
                        params.setInt(COSName.ROWS, val);
                        break;
                    }
                    case 259:
                    {
                        if (val == 4)
                        {
                            k = -1;
                        }
                        if (val == 3)
                        {
                            k = 0;
                        }
                        break; // T6/T4 Compression
                    }
                    case 262:
                    {
                        if (val == 1)
                        {
                            params.setBoolean(COSName.BLACK_IS_1, true);
                        }
                        break;
                    }
                    case 273:
                    {
                        if (count == 1)
                        {
                            dataoffset = val;
                        }
                        break;
                    }
                    case 279:
                    {
                        if (count == 1)
                        {
                            datalength = val;
                        }
                        break;
                    }
                    case 292:
                    {
                        if ((val & 1) != 0)
                        {
                            k = 50; // T4 2D - arbitary positive K value
                        }
                        // http://www.awaresystems.be/imaging/tiff/tifftags/t4options.html
                        if ((val & 4) != 0)
                        {
                            throw new IOException("CCITT Group 3 'uncompressed mode' is not supported");
                        }
                        if ((val & 2) != 0)
                        {
                            throw new IOException("CCITT Group 3 'fill bits before EOL' is not supported");
                        }
                        break;
                    }
                    case 324:
                    {
                        if (count == 1)
                        {
                            dataoffset = val;
                        }
                        break;
                    }
                    case 325:
                    {
                        if (count == 1)
                        {
                            datalength = val;
                        }
                        break;
                    }
                    default:
                    {
                        // do nothing
                    }
                }
            }

            if (k == -1000)
            {
                throw new IOException("First image in tiff is not CCITT T4 or T6 compressed");
            }
            if (dataoffset == 0)
            {
                throw new IOException("First image in tiff is not a single tile/strip");
            }

            params.setInt(COSName.K, k);

            reader.seek(dataoffset);

            byte[] buf = new byte[8192];
            int amountRead = -1;
            while ((amountRead = reader.read(buf, 0, Math.min(8192, datalength))) > 0)
            {
                datalength -= amountRead;
                os.write(buf, 0, amountRead);
            }

        }
        finally
        {
            os.close();
        }
    }

    private static int readshort(char endianess, RandomAccess raf) throws IOException
    {
        if (endianess == 'I')
        {
            return raf.read() | (raf.read() << 8);
        }
        return (raf.read() << 8) | raf.read();
    }

    private static int readlong(char endianess, RandomAccess raf) throws IOException
    {
        if (endianess == 'I')
        {
            return raf.read() | (raf.read() << 8) | (raf.read() << 16) | (raf.read() << 24);
        }
        return (raf.read() << 24) | (raf.read() << 16) | (raf.read() << 8) | raf.read();
    }
}
