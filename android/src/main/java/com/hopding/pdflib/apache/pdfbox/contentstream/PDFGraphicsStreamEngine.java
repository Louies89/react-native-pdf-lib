package com.hopding.pdflib.apache.pdfbox.contentstream;

import java.io.IOException;

import com.hopding.pdflib.apache.pdfbox.contentstream.operator.color.SetNonStrokingColor;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.color.SetNonStrokingColorSpace;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.color.SetNonStrokingDeviceGrayColor;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.color.SetNonStrokingDeviceRGBColor;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.color.SetStrokingColor;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.color.SetStrokingColorSpace;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.color.SetStrokingDeviceGrayColor;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.color.SetStrokingDeviceRGBColor;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.AppendRectangleToPath;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.CloseAndStrokePath;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.CloseFillEvenOddAndStrokePath;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.CloseFillNonZeroAndStrokePath;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.ClosePath;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.CurveTo;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.CurveToReplicateFinalPoint;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.CurveToReplicateInitialPoint;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.DrawObject;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.EndPath;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.LineTo;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.MoveTo;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.ShadingFill;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.graphics.StrokePath;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.Concatenate;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.Restore;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.Save;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.SetFlatness;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.SetGraphicsStateParameters;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.SetLineCapStyle;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.SetLineDashPattern;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.SetLineJoinStyle;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.SetLineMiterLimit;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.SetLineWidth;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.SetMatrix;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.state.SetRenderingIntent;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.BeginText;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.EndText;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.MoveText;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.MoveTextSetLeading;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.NextLine;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.SetCharSpacing;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.SetFontAndSize;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.SetTextHorizontalScaling;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.SetTextLeading;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.SetTextRenderingMode;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.SetTextRise;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.SetWordSpacing;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.ShowText;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.ShowTextAdjusted;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.ShowTextLine;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.text.ShowTextLineAndSpace;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.PDPage;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.image.PDImage;

import android.graphics.PointF;

/**
 * PDFStreamEngine subclass for advanced processing of graphics.
 * This class should be subclasses by end users looking to hook into graphics operations.
 *
 * @author John Hewson
 */
public abstract class PDFGraphicsStreamEngine extends PDFStreamEngine {
    // may be null, for example if the stream is a tiling pattern
    private final PDPage page;

    /**
     * Constructor.
     */
    protected PDFGraphicsStreamEngine(PDPage page) {
        this.page = page;

        addOperator(new CloseFillNonZeroAndStrokePath());
//        addOperator(new FillNonZeroAndStrokePath());TODO
        addOperator(new CloseFillEvenOddAndStrokePath());
//        addOperator(new FillEvenOddAndStrokePath());TODO
//        addOperator(new BeginInlineImage());TODO
        addOperator(new BeginText());
        addOperator(new CurveTo());
        addOperator(new Concatenate());
        addOperator(new SetStrokingColorSpace());
        addOperator(new SetNonStrokingColorSpace());
        addOperator(new SetLineDashPattern());
        addOperator(new DrawObject()); // special graphics version
        addOperator(new EndText());
//        addOperator(new FillNonZeroRule());TODO
//        addOperator(new LegacyFillNonZeroRule());TODO
//        addOperator(new FillEvenOddRule());TODO
        addOperator(new SetStrokingDeviceGrayColor());
        addOperator(new SetNonStrokingDeviceGrayColor());
        addOperator(new SetGraphicsStateParameters());
        addOperator(new ClosePath());
        addOperator(new SetFlatness());
        addOperator(new SetLineJoinStyle());
        addOperator(new SetLineCapStyle());
//        addOperator(new SetStrokingDeviceCMYKColor());TODO
//        addOperator(new SetNonStrokingDeviceCMYKColor());TODO
        addOperator(new LineTo());
        addOperator(new MoveTo());
        addOperator(new SetLineMiterLimit());
        addOperator(new EndPath());
        addOperator(new Save());
        addOperator(new Restore());
        addOperator(new AppendRectangleToPath());
        addOperator(new SetStrokingDeviceRGBColor());
        addOperator(new SetNonStrokingDeviceRGBColor());
        addOperator(new SetRenderingIntent());
        addOperator(new CloseAndStrokePath());
        addOperator(new StrokePath());
        addOperator(new SetStrokingColor());
        addOperator(new SetNonStrokingColor());
//        addOperator(new SetStrokingColorN());TODO
//        addOperator(new SetNonStrokingColorN());TODO
        addOperator(new ShadingFill());
        addOperator(new NextLine());
        addOperator(new SetCharSpacing());
        addOperator(new MoveText());
        addOperator(new MoveTextSetLeading());
        addOperator(new SetFontAndSize());
        addOperator(new ShowText());
        addOperator(new ShowTextAdjusted());
        addOperator(new SetTextLeading());
        addOperator(new SetMatrix());
        addOperator(new SetTextRenderingMode());
        addOperator(new SetTextRise());
        addOperator(new SetWordSpacing());
        addOperator(new SetTextHorizontalScaling());
        addOperator(new CurveToReplicateInitialPoint());
        addOperator(new SetLineWidth());
//        addOperator(new ClipNonZeroRule());TODO
//        addOperator(new ClipEvenOddRule());TODO
        addOperator(new CurveToReplicateFinalPoint());
        addOperator(new ShowTextLine());
        addOperator(new ShowTextLineAndSpace());
    }

    /**
     * Returns the page.
     */
    protected final PDPage getPage() {
        return page;
    }

    /**
     * Append a rectangle to the current path.
     */
    public abstract void appendRectangle(PointF p0, PointF p1,
                                         PointF p2, PointF p3) throws IOException;

    /**
     * Draw the image.
     *
     * @param pdImage The image to draw.
     */
    public abstract void drawImage(PDImage pdImage) throws IOException;

    /**
     * Modify the current clipping path by intersecting it with the current path.
     * The clipping path will not be updated until the succeeding painting operator is called.
     *
     * @param windingRule The winding rule which will be used for clipping.
     */
    public abstract void clip(int windingRule) throws IOException;

    /**
     * Starts a new path at (x,y).
     */
    public abstract void moveTo(float x, float y) throws IOException;

    /**
     * Draws a line from the current point to (x,y).
     */
    public abstract void lineTo(float x, float y) throws IOException;

    /**
     * Draws a curve from the current point to (x3,y3) using (x1,y1) and (x2,y2) as control points.
     */
    public abstract void curveTo(float x1, float y1,
                                 float x2, float y2,
                                 float x3, float y3) throws IOException;

    /**
     * Returns the current point of the current path.
     */
    public abstract PointF getCurrentPoint() throws IOException;

    /**
     * Closes the current path.
     */
    public abstract void closePath() throws IOException;

    /**
     * Ends the current path without filling or stroking it. The clipping path is updated here.
     */
    public abstract void endPath() throws IOException;

    /**
     * Stroke the path.
     *
     * @throws IOException If there is an IO error while stroking the path.
     */
    public abstract void strokePath() throws IOException;

    /**
     * Fill the path.
     *
     * @param windingRule The winding rule this path will use.
     */
    public abstract void fillPath(int windingRule) throws IOException;

    /**
     * Fills and then strokes the path.
     *
     * @param windingRule The winding rule this path will use.
     */
    public abstract void fillAndStrokePath(int windingRule) throws IOException;

    /**
     * Fill with Shading.
     *
     * @param shadingName The name of the Shading Dictionary to use for this fill instruction.
     */
    public abstract void shadingFill(COSName shadingName) throws IOException;
}
