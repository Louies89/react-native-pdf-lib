package com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.image;

import java.io.IOException;
import java.io.InputStream;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDMemoryStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Reads a sampled image from a PDF file.
 * @author John Hewson
 */
final class SampledImageReader
{
	private SampledImageReader()
	{
	}
	
    /**
     * Returns an ARGB image filled with the given paint and using the given image as a mask.
     * @param paint the paint to fill the visible portions of the image with
     * @return a masked image filled with the given paint
     * @throws IOException if the image cannot be read
     * @throws IllegalStateException if the image is not a stencil.
     */
//    public static BufferedImage getStencilImage(PDImage pdImage, Paint paint) throws IOException
//    {
//        // get mask (this image)
//        BufferedImage mask = getRGBImage(pdImage, null);
//
//        // compose to ARGB
//        BufferedImage masked = new BufferedImage(mask.getWidth(), mask.getHeight(),
//                BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g = masked.createGraphics();
//
//        // draw the mask
//        //g.drawImage(mask, 0, 0, null);
//
//        // fill with paint using src-in
//        //g.setComposite(AlphaComposite.SrcIn);
//        g.setPaint(paint);
//        g.fillRect(0, 0, mask.getWidth(), mask.getHeight());
//        g.dispose();
//
//        // set the alpha
//        int width = masked.getWidth();
//        int height = masked.getHeight();
//        WritableRaster raster = masked.getRaster();
//        WritableRaster alpha = mask.getRaster();
//
//        final float[] transparent = new float[4];
//        float[] alphaPixel = null;
//        for (int y = 0; y < height; y++)
//        {
//            for (int x = 0; x < width; x++)
//            {
//                alphaPixel = alpha.getPixel(x, y, alphaPixel);
//                if (alphaPixel[0] == 255)
//                {
//                    raster.setPixel(x, y, transparent);
//                }
//            }
//        }
//
//        return masked;
//    }TODO

    /**
     * Returns the content of the given image as an AWT buffered image with an RGB color space.
     * If a color key mask is provided then an ARGB image is returned instead.
     * This method never returns null.
     * @param pdImage the image to read
     * @param colorKey an optional color key mask
     * @return content of this image as an RGB buffered image
     * @throws IOException if the image cannot be read
     */
    public static Bitmap getRGBImage(PDImage pdImage, COSArray colorKey) throws IOException
    {
    	Log.e("PdfBoxAndroid", "getting image");
        if (pdImage.getStream() instanceof PDMemoryStream)
        {
            // for inline images
            if (pdImage.getStream().getLength() == 0)
            {
                throw new IOException("Image stream is empty");
            }
        }
        else if (pdImage.getStream().getStream().getFilteredLength() == 0)
        {
            throw new IOException("Image stream is empty");
        }

        // get parameters, they must be valid or have been repaired
//        final PDColorSpace colorSpace = pdImage.getColorSpace(); TODO
//        final int numComponents = 3; // colorSpace.getNumberOfComponents(); TODO
//        final int width = pdImage.getWidth();
//        final int height = pdImage.getHeight();
//        final int bitsPerComponent = pdImage.getBitsPerComponent();
//        final float[] decode = getDecodeArray(pdImage);

        //
        // An AWT raster must use 8/16/32 bits per component. Images with < 8bpc
        // will be unpacked into a byte-backed raster. Images with 16bpc will be reduced
        // in depth to 8bpc as they will be drawn to TYPE_INT_RGB images anyway. All code
        // in PDColorSpace#toRGBImage expects and 8-bit range, i.e. 0-255.
        //
//        LOG.error("getting raster");
//        WritableRaster raster = Raster.createBandedRaster(DataBuffer.TYPE_BYTE, width, height,
//                numComponents, new Point(0, 0));
//        LOG.error("got raster");
        // convert image, faster path for non-decoded, non-colormasked 8-bit images
        //final float[] defaultDecode = pdImage.getColorSpace().getDefaultDecode(8);
//        final float[] defaultDecode = { 0, 1, 0, 1, 0, 1 }; // TODO: For RGB
//        if (bitsPerComponent == 8 && Arrays.equals(decode, defaultDecode) && colorKey == null)
//        {
//            return from8bit(pdImage, raster);
        
        // Other code probably not necessary
        InputStream input = pdImage.getStream().createInputStream();
        
        return BitmapFactory.decodeStream(input);
//        }
//        else if (bitsPerComponent == 1 && colorKey == null)
//        {
//            return from1Bit(pdImage, raster);
//        }
//        else
//        {
//            return fromAny(pdImage, raster, colorKey);
//        }
    }
    
//    private static BufferedImage from1Bit(PDImage pdImage, WritableRaster raster)
//            throws IOException
//    {
//        final PDColorSpace colorSpace = pdImage.getColorSpace();
//        final int width = pdImage.getWidth();
//        final int height = pdImage.getHeight();
//        final float[] decode = getDecodeArray(pdImage);
//        byte[] output = ((DataBufferByte) raster.getDataBuffer()).getData();
//
//        // read bit stream
//        InputStream iis = null;
//        try
//        {
//            // create stream
//            iis = pdImage.getStream().createInputStream();
//            final boolean isIndexed = colorSpace instanceof PDIndexed;
//
//            int rowLen = width / 8;
//            if (width % 8 > 0)
//            {
//                rowLen++;
//            }
//
//            // read stream
//            byte value0;
//            byte value1;
//            if (isIndexed || decode[0] < decode[1])
//            {
//                value0 = 0;
//                value1 = (byte) 255;
//            }
//            else
//            {
//                value0 = (byte) 255;
//                value1 = 0;
//            }
//            byte[] buff = new byte[rowLen];
//            int idx = 0;
//            for (int y = 0; y < height; y++)
//            {
//                int x = 0;
//                iis.read(buff);
//                for (int r = 0; r < rowLen; r++)
//                {
//                    int value = buff[r];
//                    int mask = 128;
//                    for (int i = 0; i < 8; i++)
//                    {
//                        int bit = value & mask;
//                        mask >>= 1;
//                        output[idx++] = bit == 0 ? value0 : value1;
//                        x++;
//                        if (x == width)
//                        {
//                            break;
//                        }
//                    }
//                }
//            }
//
//            // use the color space to convert the image to RGB
//            BufferedImage rgbImage = colorSpace.toRGBImage(raster);
//
//            return rgbImage;
//        }
//        finally
//        {
//            if (iis != null)
//            {
//                iis.close();
//            }
//        }
//    }TODO

    // faster, 8-bit non-decoded, non-colormasked image conversion
//    private static Bitmap from8bit(PDImage pdImage, WritableRaster raster)
//            throws IOException
//    {
//        InputStream input = pdImage.getStream().createInputStream();
//        try
//        {
//            // get the raster's underlying byte buffer
//            byte[][] banks = ((DataBufferByte) raster.getDataBuffer()).getBankData();
//            byte[] source = IOUtils.toByteArray(input);
//
//            final int width = pdImage.getWidth();
//            final int height = pdImage.getHeight();
//            final int numComponents = 8; //pdImage.getColorSpace().getNumberOfComponents(); TODO
//            int max = width * height;
//
//            for (int c = 0; c < numComponents; c++)
//            {
//                int sourceOffset = c;
//                for (int i = 0; i < max; i++)
//                {
//                    banks[c][i] = source[sourceOffset];
//                    sourceOffset += numComponents;
//                }
//            }
//
////            // use the color space to convert the image to RGB
////            return pdImage.getColorSpace().toRGBImage(raster);
//        	return BitmapFactory.decodeStream(input);
//        }
//        finally
//        {
//            IOUtils.closeQuietly(input);
//        }
//    } TODO : Not at all correct
    
    // slower, general-purpose image conversion from any image format
//    private static BufferedImage fromAny(PDImage pdImage, WritableRaster raster, COSArray colorKey)
//            throws IOException
//    {
//        final PDColorSpace colorSpace = pdImage.getColorSpace();
//        final int numComponents = colorSpace.getNumberOfComponents();
//        final int width = pdImage.getWidth();
//        final int height = pdImage.getHeight();
//        final int bitsPerComponent = pdImage.getBitsPerComponent();
//        final float[] decode = getDecodeArray(pdImage);
//
//        // read bit stream
//        ImageInputStream iis = null;
//        try
//        {
//            // create stream
//            iis = new MemoryCacheImageInputStream(pdImage.getStream().createInputStream());
//            final float sampleMax = (float)Math.pow(2, bitsPerComponent) - 1f;
//            final boolean isIndexed = colorSpace instanceof PDIndexed;
//
//            // init color key mask
//            float[] colorKeyRanges = null;
//            BufferedImage colorKeyMask = null;
//            if (colorKey != null)
//            {
//                colorKeyRanges = colorKey.toFloatArray();
//                colorKeyMask = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
//            }
//
//            // calculate row padding
//            int padding = 0;
//            if (width * numComponents * bitsPerComponent % 8 > 0)
//            {
//                padding = 8 - (width * numComponents * bitsPerComponent % 8);
//            }
//
//            // read stream
//            byte[] srcColorValues = new byte[numComponents];
//            byte[] alpha = new byte[1];
//            for (int y = 0; y < height; y++)
//            {
//                for (int x = 0; x < width; x++)
//                {
//                    boolean isMasked = true;
//                    for (int c = 0; c < numComponents; c++)
//                    {
//                        int value = (int)iis.readBits(bitsPerComponent);
//
//                        // color key mask requires values before they are decoded
//                        if (colorKeyRanges != null)
//                        {
//                            isMasked &= value >= colorKeyRanges[c * 2] &&
//                                        value <= colorKeyRanges[c * 2 + 1];
//                        }
//
//                        // decode array
//                        final float dMin = decode[c * 2];
//                        final float dMax = decode[(c * 2) + 1];
//
//                        // interpolate to domain
//                        float output = dMin + (value * ((dMax - dMin) / sampleMax));
//
//                        if (isIndexed)
//                        {
//                            // indexed color spaces get the raw value, because the TYPE_BYTE
//                            // below cannot be reversed by the color space without it having
//                            // knowledge of the number of bits per component
//                            srcColorValues[c] = (byte)Math.round(output);
//                        }
//                        else
//                        {
//                            // interpolate to TYPE_BYTE
//                            int outputByte = Math.round(((output - Math.min(dMin, dMax)) /
//                                    Math.abs(dMax - dMin)) * 255f);
//
//                            srcColorValues[c] = (byte)outputByte;
//                        }
//                    }
//                    raster.setDataElements(x, y, srcColorValues);
//
//                    // set alpha channel in color key mask, if any
//                    if (colorKeyMask != null)
//                    {
//                        alpha[0] = (byte)(isMasked ? 255 : 0);
//                        colorKeyMask.getRaster().setDataElements(x, y, alpha);
//                    }
//                }
//
//                // rows are padded to the nearest byte
//                iis.readBits(padding);
//            }
//
//            // use the color space to convert the image to RGB
//            BufferedImage rgbImage = colorSpace.toRGBImage(raster);
//
//            // apply color mask, if any
//            if (colorKeyMask != null)
//            {
//                return applyColorKeyMask(rgbImage, colorKeyMask);
//            }
//            else
//            {
//                return rgbImage;
//            }
//        }
//        finally
//        {
//            if (iis != null)
//            {
//                iis.close();
//            }
//        }
//    }TODO

    // color key mask: RGB + Binary -> ARGB
//    private static BufferedImage applyColorKeyMask(BufferedImage image, BufferedImage mask)
//            throws IOException
//    {
//        int width = image.getWidth();
//        int height = image.getHeight();
//
//        // compose to ARGB
//        BufferedImage masked = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//
//        WritableRaster src = image.getRaster();
//        WritableRaster dest = masked.getRaster();
//        WritableRaster alpha = mask.getRaster();
//
//        float[] rgb = new float[3];
//        float[] rgba = new float[4];
//        float[] alphaPixel = null;
//        for (int y = 0; y < height; y++)
//        {
//            for (int x = 0; x < width; x++)
//            {
//                src.getPixel(x, y, rgb);
//
//                rgba[0] = rgb[0];
//                rgba[1] = rgb[1];
//                rgba[2] = rgb[2];
//                alphaPixel = alpha.getPixel(x, y, alphaPixel);
//                rgba[3] = 255 - alphaPixel[0];
//
//                dest.setPixel(x, y, rgba);
//            }
//        }
//
//        return masked;
//    }TODO

    // gets decode array from dictionary or returns default
    private static float[] getDecodeArray(PDImage pdImage) throws IOException
    {
        final COSArray cosDecode = pdImage.getDecode();
        float[] decode = null;

        if (cosDecode != null)
        {
            int numberOfComponents = 8; //pdImage.getColorSpace().getNumberOfComponents();
            if (cosDecode.size() != numberOfComponents * 2)
            {
                if (pdImage.isStencil() && cosDecode.size() >= 2
                        && cosDecode.get(0) instanceof COSNumber
                        && cosDecode.get(1) instanceof COSNumber)
                {
                    float decode0 = ((COSNumber) cosDecode.get(0)).floatValue();
                    float decode1 = ((COSNumber) cosDecode.get(1)).floatValue();
                    if (decode0 >= 0 && decode0 <= 1 && decode1 >= 0 && decode1 <= 1)
                    {
                    	Log.w("PdfBoxAndroid", "decode array " + cosDecode
                                + " not compatible with color space, using the first two entries");
                        return new float[]
                        {
                            decode0, decode1
                        };
                    }
                }
                Log.e("PdfBoxAndroid", "decode array " + cosDecode
                        + " not compatible with color space, using default");
            }
            else
            {
                decode = cosDecode.toFloatArray();
            }
        }

        // use color space default
        if (decode == null)
        {
        	 return new float[] { 0, 1, 0, 1, 0, 1 }; // for RGB TODO
            //return pdImage.getColorSpace().getDefaultDecode(pdImage.getBitsPerComponent());
        }

        return decode;
    }
}
