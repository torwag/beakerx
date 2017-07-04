/*
 *  Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.twosigma.beakerx.scala.kernel;

import com.twosigma.beakerx.DefaultJVMVariables;

/**
 * @author konst
 */
public class ScalaDefaultVariables extends DefaultJVMVariables {

  public ScalaDefaultVariables() {
    removeImports("com.twosigma.beakerx.easyform.EasyForm",
        "com.twosigma.beakerx.chart.xychart.*",
        "com.twosigma.beakerx.chart.xychart.plotitem.*",
        "com.twosigma.beakerx.table.*"
    );

    addImports("com.twosigma.beakerx.scala.chart.xychart._",
        "com.twosigma.beakerx.scala.chart.xychart.plotitem._",
        "com.twosigma.beakerx.scala.easyform.EasyForm",
        "com.twosigma.beakerx.easyform.formitem._",
        "com.twosigma.beakerx.scala.table._",
        "com.twosigma.beakerx.scala.fileloader._");

  }


}
