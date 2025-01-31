package com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.state;

import android.graphics.Paint;

import java.io.IOException;

import com.hopding.pdflib.apache.pdfbox.cos.COSArray;
import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSDictionary;
import com.hopding.pdflib.apache.pdfbox.cos.COSFloat;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.cos.COSNumber;
import com.hopding.pdflib.apache.pdfbox.pdmodel.common.COSObjectable;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.PDFontSetting;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.PDLineDashPattern;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.blend.BlendMode;

/**
 * An extended graphics state dictionary.
 * 
 * @author Ben Litchfield
 */
public class PDExtendedGraphicsState implements COSObjectable
{
	private final COSDictionary dict;

	/**
	 * Default constructor, creates blank graphics state.
	 */
	public PDExtendedGraphicsState()
	{
		dict = new COSDictionary();
		dict.setItem( COSName.TYPE, COSName.EXT_G_STATE );
	}

	/**
	 * Create a graphics state from an existing dictionary.
	 *
	 * @param dictionary The existing graphics state.
	 */
	public PDExtendedGraphicsState( COSDictionary dictionary )
	{
		dict = dictionary;
	}

	/**
	 * This will implement the gs operator.
	 *
	 * @param gs The state to copy this dictionaries values into.
	 *
	 * @throws IOException If there is an error copying font information.
	 */
	public void copyIntoGraphicsState( PDGraphicsState gs ) throws IOException
	{
		for( COSName key : dict.keySet() )
		{
			if( key.equals( COSName.LW ) )
			{
				gs.setLineWidth( getLineWidth() );
			}
			else if( key.equals( COSName.LC ) )
			{
				gs.setLineCap( getLineCapStyle() );
			}
			else if( key.equals( COSName.LJ ) )
			{
				gs.setLineJoin( getLineJoinStyle() );
			}
			else if( key.equals( COSName.ML ) )
			{
				gs.setMiterLimit( getMiterLimit() );
			}
			else if( key.equals( COSName.D ) )
			{
				gs.setLineDashPattern( getLineDashPattern() );
			}
			else if( key.equals( COSName.RI ) )
			{
				gs.setRenderingIntent( getRenderingIntent() );
			}
			else if( key.equals( COSName.OPM ) )
			{
				gs.setOverprintMode( getOverprintMode().doubleValue() );
			}
			else if( key.equals( COSName.FONT ) )
			{
				PDFontSetting setting = getFontSetting();
				if (setting != null)
				{
					gs.getTextState().setFont( setting.getFont() );
					gs.getTextState().setFontSize( setting.getFontSize() );
				}
			}
			else if( key.equals( COSName.FL ) )
			{
				gs.setFlatness( getFlatnessTolerance() );
			}
			else if( key.equals( COSName.SM ) )
			{
				gs.setSmoothness( getSmoothnessTolerance() );
			}
			else if( key.equals( COSName.SA ) )
			{
				gs.setStrokeAdjustment( getAutomaticStrokeAdjustment() );
			}
			else if( key.equals( COSName.CA ) )
			{
				gs.setAlphaConstant(getStrokingAlphaConstant());
			}
			else if( key.equals( COSName.CA_NS ) )
			{
				gs.setNonStrokeAlphaConstant(getNonStrokingAlphaConstant() );
			}
			else if( key.equals( COSName.AIS ) )
			{
				gs.setAlphaSource( getAlphaSourceFlag() );
			}
			else if( key.equals( COSName.TK ) )
			{
				gs.getTextState().setKnockoutFlag( getTextKnockoutFlag() );
			}
			else if( key.equals( COSName.SMASK ) ) 
			{
				gs.setSoftMask(getSoftMask());
			}
			else if( key.equals( COSName.BM ) ) 
			{
				gs.setBlendMode( getBlendMode() );
			}
		}
	}

	/**
	 * This will get the underlying dictionary that this class acts on.
	 *
	 * @return The underlying dictionary for this class.
	 */
	public COSDictionary getCOSDictionary()
	{
		return dict;
	}

	/**
	 * Convert this standard java object to a COS object.
	 *
	 * @return The cos object that matches this Java object.
	 */
	public COSBase getCOSObject()
	{
		return dict;
	}

	/**
	 * This will get the line width.  This will return null if there is no line width
	 *
	 * @return null or the LW value of the dictionary.
	 */
	public Float getLineWidth()
	{
		return getFloatItem( COSName.LW );
	}

	/**
	 * This will set the line width.
	 *
	 * @param width The line width for the object.
	 */
	public void setLineWidth( Float width )
	{
		setFloatItem( COSName.LW, width );
	}

	/**
	 * This will get the line cap style.
	 *
	 * @return null or the LC value of the dictionary.
	 */
	public Paint.Cap getLineCapStyle()
	{
        switch(dict.getInt( COSName.LC ))  {
            case 0:
                return Paint.Cap.BUTT;
            case 1:
                return Paint.Cap.ROUND;
            case 2:
                return Paint.Cap.SQUARE;
            default:
                return null;
        }
	}

	/**
	 * This will set the line cap style for the graphics state.
	 *
	 * @param style The new line cap style to set.
	 */
	public void setLineCapStyle( int style )
	{
		dict.setInt( COSName.LC, style );
	}

	/**
	 * This will get the line join style.
	 *
	 * @return null or the LJ value in the dictionary.
	 */
	public Paint.Join getLineJoinStyle()
	{
        switch(dict.getInt( COSName.LJ ))  {
            case 0:
                return Paint.Join.MITER;
            case 1:
                return Paint.Join.ROUND;
            case 2:
                return Paint.Join.BEVEL;
            default:
                return null;
        }
	}

	/**
	 * This will set the line join style.
	 *
	 * @param style The new line join style.
	 */
	public void setLineJoinStyle( int style )
	{
		dict.setInt( COSName.LJ, style );
	}


	/**
	 * This will get the miter limit.
	 *
	 * @return null or the ML value in the dictionary.
	 */
	public Float getMiterLimit()
	{
		return getFloatItem( COSName.ML );
	}

	/**
	 * This will set the miter limit for the graphics state.
	 *
	 * @param miterLimit The new miter limit value
	 */
	public void setMiterLimit( Float miterLimit )
	{
		setFloatItem( COSName.ML, miterLimit );
	}

	/**
	 * This will get the dash pattern.
	 *
	 * @return null or the D value in the dictionary.
	 */
	public PDLineDashPattern getLineDashPattern()
	{
		PDLineDashPattern retval = null;
		COSArray dp = (COSArray)dict.getDictionaryObject( COSName.D );
		if( dp != null )
		{
			COSArray array = new COSArray();
			dp.addAll(dp);
			dp.remove(dp.size() - 1);
			int phase = dp.getInt(dp.size() - 1);

			retval = new PDLineDashPattern( array, phase );
		}
		return retval;
	}

	/**
	 * This will set the dash pattern for the graphics state.
	 *
	 * @param dashPattern The dash pattern
	 */
	public void setLineDashPattern( PDLineDashPattern dashPattern )
	{
		dict.setItem(COSName.D, dashPattern.getCOSObject());
	}

	/**
	 * This will get the rendering intent.
	 *
	 * @return null or the RI value in the dictionary.
	 */
	public RenderingIntent getRenderingIntent()
	{
		String ri = dict.getNameAsString( "RI" );
		if (ri != null)
		{
			return RenderingIntent.fromString(ri);
		}
		else
		{
			return null;
		}
	}

	/**
	 * This will set the rendering intent for the graphics state.
	 *
	 * @param ri The new rendering intent
	 */
	public void setRenderingIntent( String ri )
	{
		dict.setName("RI", ri);
	}

	/**
	 * This will get the overprint control.
	 *
	 * @return The overprint control or null if one has not been set.
	 */
	public boolean getStrokingOverprintControl()
	{
		return dict.getBoolean(COSName.OP, false);
	}

	/**
	 * This will get the overprint control(OP).
	 *
	 * @param op The overprint control.
	 */
	public void setStrokingOverprintControl( boolean op )
	{
		dict.setBoolean(COSName.OP, op);
	}

	/**
	 * This will get the overprint control for non stroking operations.  If this
	 * value is null then the regular overprint control value will be returned.
	 *
	 * @return The overprint control or null if one has not been set.
	 */
	public boolean getNonStrokingOverprintControl()
	{
		return dict.getBoolean( COSName.OP_NS, getStrokingOverprintControl() );
	}

	/**
	 * This will get the overprint control(OP).
	 *
	 * @param op The overprint control.
	 */
	public void setNonStrokingOverprintControl( boolean op )
	{
		dict.setBoolean( COSName.OP_NS, op );
	}

	/**
	 * This will get the overprint control mode.
	 *
	 * @return The overprint control mode or null if one has not been set.
	 */
	public Float getOverprintMode()
	{
		return getFloatItem(COSName.OPM);
	}

	/**
	 * This will get the overprint mode(OPM).
	 *
	 * @param overprintMode The overprint mode
	 */
	public void setOverprintMode( Float overprintMode )
	{
		setFloatItem(COSName.OPM, overprintMode);
	}

	/**
	 * This will get the font setting of the graphics state.
	 *
	 * @return The font setting.
	 */
	public PDFontSetting getFontSetting()
	{
		PDFontSetting setting = null;
		COSBase base = dict.getDictionaryObject(COSName.FONT);
		if (base instanceof COSArray)
		{
			COSArray font = (COSArray) base;
			setting = new PDFontSetting(font);
		}
		return setting;
	}

	/**
	 * This will set the font setting for this graphics state.
	 *
	 * @param fs The new font setting.
	 */
	public void setFontSetting( PDFontSetting fs )
	{
		dict.setItem(COSName.FONT, fs);
	}

	/**
	 * This will get the flatness tolerance.
	 *
	 * @return The flatness tolerance or null if one has not been set.
	 */
	public Float getFlatnessTolerance()
	{
		return getFloatItem( COSName.FL );
	}

	/**
	 * This will get the flatness tolerance.
	 *
	 * @param flatness The new flatness tolerance
	 */
	public void setFlatnessTolerance( Float flatness )
	{
		setFloatItem(COSName.FL, flatness);
	}

	/**
	 * This will get the smothness tolerance.
	 *
	 * @return The smothness tolerance or null if one has not been set.
	 */
	public Float getSmoothnessTolerance()
	{
		return getFloatItem( COSName.SM );
	}

	/**
	 * This will get the smoothness tolerance.
	 *
	 * @param smoothness The new smoothness tolerance
	 */
	public void setSmoothnessTolerance( Float smoothness )
	{
		setFloatItem( COSName.SM, smoothness );
	}

	/**
	 * This will get the automatic stroke adjustment flag.
	 *
	 * @return The automatic stroke adjustment flag or null if one has not been set.
	 */
	public boolean getAutomaticStrokeAdjustment()
	{
		return dict.getBoolean(COSName.SA, false);
	}

	/**
	 * This will get the automatic stroke adjustment flag.
	 *
	 * @param sa The new automatic stroke adjustment flag.
	 */
	public void setAutomaticStrokeAdjustment( boolean sa )
	{
		dict.setBoolean(COSName.SA, sa);
	}

	/**
	 * This will get the stroking alpha constant.
	 *
	 * @return The stroking alpha constant or null if one has not been set.
	 */
	public Float getStrokingAlphaConstant()
	{
		return getFloatItem(COSName.CA);
	}

	/**
	 * This will get the stroking alpha constant.
	 *
	 * @param alpha The new stroking alpha constant.
	 */
	public void setStrokingAlphaConstant( Float alpha )
	{
		setFloatItem(COSName.CA, alpha);
	}

	/**
	 * This will get the non stroking alpha constant.
	 *
	 * @return The non stroking alpha constant or null if one has not been set.
	 */
	public Float getNonStrokingAlphaConstant()
	{
		return getFloatItem( COSName.CA_NS );
	}

	/**
	 * This will get the non stroking alpha constant.
	 *
	 * @param alpha The new non stroking alpha constant.
	 */
	public void setNonStrokingAlphaConstant( Float alpha )
	{
		setFloatItem( COSName.CA_NS, alpha );
	}

	/**
	 * This will get the alpha source flag.
	 *
	 * @return The alpha source flag.
	 */
	public boolean getAlphaSourceFlag()
	{
		return dict.getBoolean(COSName.AIS, false);
	}

	/**
	 * This will get the alpha source flag.
	 *
	 * @param alpha The alpha source flag.
	 */
	public void setAlphaSourceFlag( boolean alpha )
	{
		dict.setBoolean(COSName.AIS, alpha);
	}

	/**
	 * Returns the blending mode stored in the COS dictionary
	 *
	 * @return the blending mode
	 */
	public BlendMode getBlendMode()
	{
		return BlendMode.getInstance(dict.getDictionaryObject(COSName.BM));
	}

	/**
	 * Returns the soft mask stored in the COS dictionary
	 *
	 * @return the soft mask
	 */
	public PDSoftMask getSoftMask()
	{
		return PDSoftMask.create(dict.getDictionaryObject(COSName.SMASK));
	}

	/**

    /**
	 * This will get the text knockout flag.
	 *
	 * @return The text knockout flag.
	 */
	public boolean getTextKnockoutFlag()
	{
		return dict.getBoolean( COSName.TK,true );
	}

	/**
	 * This will get the text knockout flag.
	 *
	 * @param tk The text knockout flag.
	 */
	public void setTextKnockoutFlag( boolean tk )
	{
		dict.setBoolean( COSName.TK, tk );
	}

	/**
	 * This will get a float item from the dictionary.
	 *
	 * @param key The key to the item.
	 *
	 * @return The value for that item.
	 */
	private Float getFloatItem( COSName key )
	{
		Float retval = null;
		COSNumber value = (COSNumber)dict.getDictionaryObject( key );
		if( value != null )
		{
			retval = value.floatValue();
		}
		return retval;
	}

	/**
	 * This will set a float object.
	 *
	 * @param key The key to the data that we are setting.
	 * @param value The value that we are setting.
	 */
	private void setFloatItem( COSName key, Float value )
	{
		if( value == null )
		{
			dict.removeItem( key );
		}
		else
		{
			dict.setItem( key, new COSFloat( value) );
		}
	}
}
