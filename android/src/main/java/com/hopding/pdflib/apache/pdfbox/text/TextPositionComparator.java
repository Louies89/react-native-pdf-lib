package com.hopding.pdflib.apache.pdfbox.text;

import java.util.Comparator;

/**
 * This class is a comparator for TextPosition operators.  It handles
 * pages with text in different directions by grouping the text based
 * on direction and sorting in that direction. This allows continuous text
 * in a given direction to be more easily grouped together.  
 *
 * @author Ben Litchfield
 */
public class TextPositionComparator implements Comparator<TextPosition>
{
    @Override
    public int compare(TextPosition pos1, TextPosition pos2)
    {
        // only compare text that is in the same direction
        if (pos1.getDir() < pos2.getDir())
        {
            return -1;
        }
        else if (pos1.getDir() > pos2.getDir())
        {
            return 1;
        }
        
        // get the text direction adjusted coordinates
        float x1 = pos1.getXDirAdj();
        float x2 = pos2.getXDirAdj();
        
        float pos1YBottom = pos1.getYDirAdj();
        float pos2YBottom = pos2.getYDirAdj();

        // note that the coordinates have been adjusted so 0,0 is in upper left
        float pos1YTop = pos1YBottom - pos1.getHeightDir();
        float pos2YTop = pos2YBottom - pos2.getHeightDir();

        float yDifference = Math.abs(pos1YBottom - pos2YBottom);

        // we will do a simple tolerance comparison
        if (yDifference < .1 ||
            pos2YBottom >= pos1YTop && pos2YBottom <= pos1YBottom ||
            pos1YBottom >= pos2YTop && pos1YBottom <= pos2YBottom)
        {
            if (x1 < x2)
            {
                return -1;
            }
            else if (x1 > x2)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        else if (pos1YBottom < pos2YBottom)
        {
            return - 1;
        }
        else
        {
            return 1;
        }
    }
}
