package com.hopding.pdflib.apache.pdfbox.multipdf;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSObject;
import com.hopding.pdflib.apache.pdfbox.cos.COSStream;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDDocument;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSStreamArray;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.PDStream;

/**
 * Utility class used to clone PDF objects. It keeps track of objects it has already cloned.
 */
class PDFCloneUtility
{
    private final PDDocument destination;
    private final Map<Object,COSBase> clonedVersion = new HashMap<Object,COSBase>();

    /**
     * Creates a new instance for the given target document.
     * @param dest the destination PDF document that will receive the clones
     */
    public PDFCloneUtility(PDDocument dest)
    {
        this.destination = dest;
    }

    /**
     * Returns the destination PDF document this cloner instance is set up for.
     * @return the destination PDF document
     */
    public PDDocument getDestination()
    {
        return this.destination;
    }

    /**
     * Deep-clones the given object for inclusion into a different PDF document identified by
     * the destination parameter.
     * @param base the initial object as the root of the deep-clone operation
     * @return the cloned instance of the base object
     * @throws IOException if an I/O error occurs
     */
      public COSBase cloneForNewDocument( Object base ) throws IOException
      {
          if( base == null )
          {
              return null;
          }
          COSBase retval = clonedVersion.get(base);
          if( retval != null )
          {
              //we are done, it has already been converted.
          }
          else if( base instanceof List )
          {
              COSArray array = new COSArray();
              List<?> list = (List<?>)base;
              for (Object obj : list)
              {
                  array.add(cloneForNewDocument(obj));
              }
              retval = array;
          }
          else if( base instanceof COSObjectable && !(base instanceof COSBase) )
          {
              retval = cloneForNewDocument( ((COSObjectable)base).getCOSObject() );
              clonedVersion.put( base, retval );
          }
          else if( base instanceof COSObject )
          {
              COSObject object = (COSObject)base;
              retval = cloneForNewDocument( object.getObject() );
              clonedVersion.put( base, retval );
          }
          else if( base instanceof COSArray )
          {
              COSArray newArray = new COSArray();
              COSArray array = (COSArray)base;
              for( int i=0; i<array.size(); i++ )
              {
                  newArray.add( cloneForNewDocument( array.get( i ) ) );
              }
              retval = newArray;
              clonedVersion.put( base, retval );
          }
          else if (base instanceof COSStreamArray)
          {
        	  // PDFBOX-2052
        	  COSStreamArray originalStream = (COSStreamArray) base;

              if (originalStream.size() > 0)
              {
                  throw new IllegalStateException("Cannot close stream array with items next to the streams.");
              }

              COSArray array = new COSArray();
              for (int i = 0; i < originalStream.getStreamCount(); i++)
              {
                  COSBase base2 = originalStream.get(i);
                  COSBase cloneForNewDocument = cloneForNewDocument(base2);
                  array.add(cloneForNewDocument);
              }
              retval = new COSStreamArray(array);
              clonedVersion.put(base, retval);
          }
          else if( base instanceof COSStream )
          {
              COSStream originalStream = (COSStream)base;
              PDStream stream = new PDStream( destination, originalStream.getFilteredStream(), true );
              clonedVersion.put( base, stream.getStream() );
              for( Map.Entry<COSName, COSBase> entry :  originalStream.entrySet() )
              {
                  stream.getStream().setItem(
                          entry.getKey(),
                          cloneForNewDocument(entry.getValue()));
              }
              retval = stream.getStream();
          }
          else if( base instanceof COSDictionary )
          {
              COSDictionary dic = (COSDictionary)base;
              retval = new COSDictionary();
              clonedVersion.put( base, retval );
              for( Map.Entry<COSName, COSBase> entry : dic.entrySet() )
              {
                  ((COSDictionary)retval).setItem(
                          entry.getKey(),
                          cloneForNewDocument(entry.getValue()));
              }
          }
          else
          {
              retval = (COSBase)base;
          }
          clonedVersion.put( base, retval );
          return retval;
      }


      /**
       * Merges two objects of the same type by deep-cloning its members.
       * <br/>
       * Base and target must be instances of the same class.
       * @param base the base object to be cloned
       * @param target the merge target
       * @throws IOException if an I/O error occurs
       */
      public void cloneMerge( final COSObjectable base, COSObjectable target) throws IOException
      {
          if( base == null )
          {
              return;
          }
          COSBase retval = clonedVersion.get( base );
          if( retval != null )
          {
              return;
              //we are done, it has already been converted. // ### Is that correct for cloneMerge???
          }
          else if( base instanceof List )
          {
              COSArray array = new COSArray();
              List<?> list = (List<?>)base;
              for (Object obj : list)
              {
                  array.add(cloneForNewDocument(obj));
              }
              ((List<COSArray>)target).add(array);
          }
          else if( base instanceof COSObjectable && !(base instanceof COSBase) )
          {
              cloneMerge(base.getCOSObject(), target.getCOSObject() );
              clonedVersion.put( base, retval );
          }
          else if( base instanceof COSObject )
          {
              if(target instanceof COSObject)
              {
                  cloneMerge(((COSObject) base).getObject(),((COSObject) target).getObject() );
              }
              else if(target instanceof COSDictionary)
              {
                  cloneMerge(((COSObject) base).getObject(), target);
              }
              clonedVersion.put( base, retval );
          }
          else if( base instanceof COSArray )
          {
              COSArray array = (COSArray)base;
              for( int i=0; i<array.size(); i++ )
              {
                  ((COSArray)target).add( cloneForNewDocument( array.get( i ) ) );
              }
              clonedVersion.put( base, retval );
          }
          else if( base instanceof COSStream )
          {
            // does that make sense???
              COSStream originalStream = (COSStream)base;
              PDStream stream = new PDStream( destination, originalStream.getFilteredStream(), true );
              clonedVersion.put( base, stream.getStream() );
              for( Map.Entry<COSName, COSBase> entry : originalStream.entrySet() )
              {
                  stream.getStream().setItem(
                          entry.getKey(),
                          cloneForNewDocument(entry.getValue()));
              }
              retval = stream.getStream();
              target = retval;
          }
          else if( base instanceof COSDictionary )
          {
              COSDictionary dic = (COSDictionary)base;
              clonedVersion.put( base, retval );
              for( Map.Entry<COSName, COSBase> entry : dic.entrySet() )
              {
                  COSName key = entry.getKey();
                  COSBase value = entry.getValue();
                  if (((COSDictionary)target).getItem(key) != null)
                  {
                      cloneMerge(value, ((COSDictionary)target).getItem(key));
                  }
                  else
                  {
                      ((COSDictionary)target).setItem( key, cloneForNewDocument(value));
                  }
              }
          }
          else
          {
              retval = (COSBase)base;
          }
          clonedVersion.put( base, retval );
      }
}
