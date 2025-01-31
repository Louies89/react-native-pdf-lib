package com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.predictor;

/**
*
*
* In an Uptimum encoded image, each line takes up width*bpp+1 bytes. The first
* byte holds a number that signifies which algorithm encoded the line.
*
* @author xylifyx@yahoo.co.uk
* @version $Revision: 1.1 $
*/
public class Optimum extends PredictorAlgorithm
{
   /**
    * {@inheritDoc}
    */
   public void checkBufsiz(byte[] filtered, byte[] raw)
   {
       if (filtered.length != (getWidth() * getBpp() + 1) * getHeight())
       {

           throw new IllegalArgumentException(
                   "filtered.length != (width*bpp + 1) * height, "
                           + filtered.length + " "
                           + (getWidth() * getBpp() + 1) * getHeight()
                           + "w,h,bpp=" + getWidth() + "," + getHeight() + ","
                           + getBpp());
       }
       if (raw.length != getWidth() * getHeight() * getBpp())
       {
           throw new IllegalArgumentException(
                   "raw.length != width * height * bpp, raw.length="
                           + raw.length + " w,h,bpp=" + getWidth() + ","
                           + getHeight() + "," + getBpp());
       }
   }

   /**
    * {@inheritDoc}
    */
   public void encodeLine(byte[] src, byte[] dest, int srcDy, int srcOffset,
           int destDy, int destOffset)
   {
       throw new UnsupportedOperationException("encodeLine");
   }

   /**
    * {@inheritDoc}
    */
   public void decodeLine(byte[] src, byte[] dest, int srcDy, int srcOffset,
           int destDy, int destOffset)
   {
       throw new UnsupportedOperationException("decodeLine");
   }

   /**
    * {@inheritDoc}
    */
   public void encode(byte[] src, byte[] dest)
   {
       checkBufsiz(dest, src);
       throw new UnsupportedOperationException("encode");
   }

   /**
    * Filter indexed by byte code.
    */
   PredictorAlgorithm[] filter = { new None(), new Sub(), new Up(), new Average(),
           new Paeth() };

   /**
    * {@inheritDoc}
    */
   public void setBpp(int bpp)
   {
       super.setBpp(bpp);
       for (int i = 0; i < filter.length; i++)
       {
           filter[i].setBpp(bpp);
       }
   }
   /**
    * {@inheritDoc}
    */
   public void setHeight(int height)
   {
       super.setHeight(height);
       for (int i = 0; i < filter.length; i++)
       {
           filter[i].setHeight(height);
       }
   }

   /**
    * {@inheritDoc}
    */
   public void setWidth(int width)
   {
       super.setWidth(width);
       for (int i = 0; i < filter.length; i++)
       {
           filter[i].setWidth(width);
       }
   }

   /**
    * {@inheritDoc}
    */
   public void decode(byte[] src, byte[] dest)
   {
       checkBufsiz(src, dest);
       int bpl = getWidth() * getBpp();
       int srcDy = bpl + 1;
       for (int y = 0; y < getHeight(); y++)
       {
           PredictorAlgorithm f = filter[src[y * srcDy]];
           int srcOffset = y * srcDy + 1;
           f.decodeLine(src, dest, srcDy, srcOffset, bpl, y * bpl);
       }
   }
}
