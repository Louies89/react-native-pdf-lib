package com.hopding.pdflib.apache.pdfbox.contentstream.operator;

import java.io.IOException;
import java.util.List;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;

/**
 * Throw when a PDF operator is missing required operands.
 */
public final class MissingOperandException extends IOException
{
    public MissingOperandException(Operator operator, List<COSBase> operands)
    {
        super("Operator " + operator.getName() + " has too few operands: " + operands);
    }
}