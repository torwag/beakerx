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

var bkUtils = require('./../shared/bkUtils');

module.exports = {
  displayHTML: displayHTML,
  displayD3: displayD3,
  displayD32: displayD32
};

function displayHTML(outputArea, html) {
  if (html) {
    var outputContainer = _getOutputContainer(outputArea);
    $(outputContainer).append(html);
  }
}

function displayD3(outputArea, d3, width, height) {
  var genId = bkUtils.guidGenerator();
  var wrapperHtml = $('<div id="'+genId+'" class="beakerx-d3-wrapper"></div>');
  var outputContainer = _getOutputContainer(outputArea);

  var chartHeight = height === undefined ? 500 : height;
  var parentWidth = width === undefined ? '100%' : width;
  var viewBoxWidth = 500;

  if (outputContainer) {
    viewBoxWidth = outputContainer.width();
  }

  this.displayHTML(outputArea, wrapperHtml);

  var svg = d3.select('#'+genId)
    .append("svg")
    .style('width', parentWidth)
    .style('height', chartHeight+'px')
    .attr('viewBox','0 0 '+Math.min(viewBoxWidth,chartHeight)+' '+Math.min(viewBoxWidth,chartHeight))
    .attr('preserveAspectRatio','xMinYMin');

  return svg;
}

function displayD32(outputArea, width, height) {
  var genId = bkUtils.guidGenerator();
  var wrapperHtml = $('<div id="'+genId+'" class="beakerx-d3-wrapper"></div>');
  var outputContainer = _getOutputContainer(outputArea);

  var chartHeight = height === undefined ? 500 : height;
  var parentWidth = width === undefined ? '100%' : width;
  var viewBoxWidth = 500;

  if (outputContainer) {
    viewBoxWidth = outputContainer.width();
  }


  var aspectSize = Math.min(viewBoxWidth,chartHeight);
  // var svg = $('<svg width="'+parentWidth+'" height="'+chartHeight+'" viewBox="0 0 '+viewBoxWidth+' '+chartHeight+'" preserveAspectRatio="xMinYMin"></svg>');
  var svg = $('<svg width="'+parentWidth+'" height="'+chartHeight+'" viewBox="0 0 '+aspectSize+' '+aspectSize+'" preserveAspectRatio="xMinYMin"></svg>');
  // var svg = $('<svg></svg>')
  //   .attr('width', parentWidth)
  //   .attr('height', chartHeight)
  //   .attr('viewBox','0 0 '+Math.min(viewBoxWidth,chartHeight)+' '+Math.min(viewBoxWidth,chartHeight))
  //   .attr('preserveAspectRatio','xMinYMin');

  wrapperHtml.append(svg);

  this.displayHTML(outputArea, wrapperHtml);

  return genId;
}

function _getOutputContainer(outputArea) {
  var container = null;

  if (outputArea && outputArea.element) {
    var subarea = $(outputArea.element).children('.output_area').children('.output_subarea');
    container = subarea.length ? subarea : outputArea.element;
  }

  return container;
}