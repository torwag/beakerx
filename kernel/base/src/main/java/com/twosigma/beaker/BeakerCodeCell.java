/*
 *  Copyright 2014 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.beaker;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.twosigma.beaker.jvm.serialization.BeakerObjectConverter;
import com.twosigma.beaker.jvm.serialization.ObjectDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeakerCodeCell {
  
  private final static Logger logger = LoggerFactory.getLogger(BeakerCodeCell.class.getName());
    
  @JsonProperty("cell_id")
  private String cellId;
  @JsonProperty("execution_count")
  private String executionCount;
  @JsonProperty("cell_type")
  private String cellType;
  private Object outputs;
  private Object metadata;
  private String source;

  public BeakerCodeCell() { }

  public String getExecutionCount() {
    return executionCount;
  }

  public void setExecutionCount(String executionCount) {
    this.executionCount = executionCount;
  }

  public String getCellType() {
    return cellType;
  }

  public void setCellType(String cellType) {
    this.cellType = cellType;
  }

  public Object getOutputs() {
    return outputs;
  }

  public void setOutputs(Object outputs) {
    this.outputs = outputs;
  }

  public Object getMetadata() {
    return metadata;
  }

  public void setMetadata(Object metadata) {
    this.metadata = metadata;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getCellId() {
    return cellId;
  }

  public void setCellId(String cellId) {
    this.cellId = cellId;
  }

  public static class Serializer extends JsonSerializer<BeakerCodeCell> {

    private final Provider<BeakerObjectConverter> objectSerializerProvider;

    @Inject
    public Serializer(Provider<BeakerObjectConverter> osp) {
      objectSerializerProvider = osp;
    }

    private BeakerObjectConverter getObjectSerializer() {
      return objectSerializerProvider.get();
    }

    @Override
    public void serialize(BeakerCodeCell value,
        JsonGenerator jgen,
        SerializerProvider provider)
        throws IOException, JsonProcessingException {

      synchronized (value) {
        jgen.writeStartObject();
        jgen.writeStringField("type", "BeakerCodeCell");
        jgen.writeStringField("execution_count", value.executionCount);
        jgen.writeStringField("cell_type", value.cellType);
        jgen.writeStringField("cell_id", value.cellId);
        jgen.writeFieldName("outputs");
        if (!getObjectSerializer().writeObject(value.outputs, jgen, true))
          jgen.writeString(value.outputs.toString());
        jgen.writeFieldName("metadata");
        if (!getObjectSerializer().writeObject(value.metadata, jgen, true))
          jgen.writeString(value.metadata.toString());
        jgen.writeStringField("source", value.source);
        jgen.writeEndObject();
      }
    }
  }

  public static class DeSerializer implements ObjectDeserializer {

    private final BeakerObjectConverter parent;

    public DeSerializer(BeakerObjectConverter p) {
      parent = p;
      parent.addKnownBeakerType("BeakerCodeCell");
    }

    @Override
    public Object deserialize(JsonNode n, ObjectMapper mapper) {
      BeakerCodeCell o = null;
      try {
        
        o = new BeakerCodeCell();

        if (n.has("execution_count"))
          o.setExecutionCount(n.get("execution_count").asText());
        if (n.has("cell_type"))
          o.setCellType( n.get("cell_type").asText());
        if (n.has("source"))
          o.setSource( n.get("source").asText());
        if (n.has("cell_id"))
          o.setCellId( n.get("cell_id").asText());
        if (n.has("outputs"))
          o.setOutputs( parent.deserialize(n.get("outputs"), mapper));
        if (n.has("metadata"))
          o.setMetadata( parent.deserialize(n.get("metadata"), mapper));

      } catch (Exception e) {
        logger.error("exception deserializing BeakerCodeCell ", e);
        e.printStackTrace();
      }
      return o;
    }

    @Override
    public boolean canBeUsed(JsonNode n) {
      return n.has("type") && n.get("type").asText().equals("BeakerCodeCell");
    }
  }

}