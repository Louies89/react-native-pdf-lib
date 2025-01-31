package com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.predictor;

/**
 * The sub algorithm.
 *
 * <code>Sub(i,j) = Raw(i,j) - Raw(i-1,j)</code>
 *
 * <code>Raw(i,j) = Sub(i,j) + Raw(i-1,j)</code>
 *
 * @author xylifyx@yahoo.co.uk
 * @version $Revision: 1.3 $
 */
public class Sub extends PredictorAlgorithm
{
    /**
     * {@inheritDoc}
     */
    public void encodeLine(byte[] src, byte[] dest, int srcDy, int srcOffset,
            int destDy, int destOffset)
    {
        int bpl = getWidth()*getBpp();
        int bpp = getBpp();
        // case: x < bpp
        for (int x = 0; x < bpl && x < bpp; x++)
        {
            dest[x + destOffset] = src[x + srcOffset];
        }
        // otherwise
        for (int x = getBpp(); x < bpl; x++)
        {
            dest[x + destOffset] = (byte) (src[x + srcOffset] - src[x
                    + srcOffset - bpp]);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void decodeLine(byte[] src, byte[] dest, int srcDy, int srcOffset,
            int destDy, int destOffset)
    {
        int bpl = getWidth()*getBpp();
        int bpp = getBpp();
        // case: x < bpp
        for (int x = 0; x < bpl && x < bpp; x++)
        {
            dest[x + destOffset] = src[x + srcOffset];
        }
        // otherwise
        for (int x = getBpp(); x < bpl; x++)
        {
            dest[x + destOffset] = (byte) (src[x + srcOffset] + dest[x
                    + destOffset - bpp]);
        }
    }
}
