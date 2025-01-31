/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hopding.pdflib.apache.pdfbox.contentstream.operator.color;

import com.hopding.pdflib.apache.pdfbox.cos.COSBase;
import com.hopding.pdflib.apache.pdfbox.cos.COSName;
import com.hopding.pdflib.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import com.hopding.pdflib.apache.pdfbox.contentstream.operator.Operator;

import java.io.IOException;
import java.util.List;

/**
 * G: Set the stroking colour space to DeviceGray and set the gray level to use for stroking
 * operations.
 *
 * @author John Hewson
 */
public class SetStrokingDeviceGrayColor extends SetStrokingColor
{
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
        PDColorSpace cs = context.getResources().getColorSpace(COSName.DEVICEGRAY);
        context.getGraphicsState().setStrokingColorSpace(cs);
        super.process(operator, arguments);
    }

    @Override
    public String getName()
    {
        return "G";
    }
}
