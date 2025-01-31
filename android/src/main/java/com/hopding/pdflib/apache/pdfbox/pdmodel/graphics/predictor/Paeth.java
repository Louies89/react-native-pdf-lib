package com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.predictor;

/**
 * From http://www.w3.org/TR/PNG-Filters.html: The Paeth filter computes a
 * simple linear function of the three neighboring pixels (left, above, upper
 * left), then chooses as predictor the neighboring pixel closest to the
 * computed value. This technique is due to Alan W. Paeth [PAETH].
 *
 * To compute the Paeth filter, apply the following formula to each byte of the
 * scanline:
 *
 * <code>Paeth(i,j) = Raw(i,j) - PaethPredictor(Raw(i-1,j), Raw(i,j-1), Raw(i-1,j-1))</code>
 *
 * To decode the Paeth filter
 *
 * <code>Raw(i,j) = Paeth(i,j) - PaethPredictor(Raw(i-1,j), Raw(i,j-1), Raw(i-1,j-1))</code>
 *
 * @author xylifyx@yahoo.co.uk
 * @version $Revision: 1.3 $
 */
public class Paeth extends PredictorAlgorithm
{
    /**
     * The paeth predictor function.
     *
     * This function is taken almost directly from the PNG definition on
     * http://www.w3.org/TR/PNG-Filters.html
     *
     * @param a
     *            left
     * @param b
     *            above
     * @param c
     *            upper left
     * @return The result of the paeth predictor.
     */
    public int paethPredictor(int a, int b, int c)
    {
        int p = a + b - c; // initial estimate
        int pa = Math.abs(p - a); // distances to a, b, c
        int pb = Math.abs(p - b);
        int pc = Math.abs(p - c);
        // return nearest of a,b,c,
        // breaking ties in order a,b,c.
        if (pa <= pb && pa <= pc)
        {
            return a;
        }
        else if (pb <= pc)
        {
            return b;
        }
        else
        {
            return c;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void encodeLine(byte[] src, byte[] dest, int srcDy, int srcOffset,
            int destDy, int destOffset)
    {
        int bpl = getWidth() * getBpp();
        for (int x = 0; x < bpl; x++)
        {
            dest[x + destOffset] = (byte) (src[x + srcOffset] - paethPredictor(
                    leftPixel(src, srcOffset, srcDy, x), abovePixel(src,
                            srcOffset, srcDy, x), aboveLeftPixel(src,
                            srcOffset, srcDy, x)));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void decodeLine(byte[] src, byte[] dest, int srcDy, int srcOffset,
            int destDy, int destOffset)
    {
        int bpl = getWidth() * getBpp();
        for (int x = 0; x < bpl; x++)
        {
            dest[x + destOffset] = (byte) (src[x + srcOffset] + paethPredictor(
                    leftPixel(dest, destOffset, destDy, x), abovePixel(dest,
                            destOffset, destDy, x), aboveLeftPixel(dest,
                            destOffset, destDy, x)));
        }
    }
}
