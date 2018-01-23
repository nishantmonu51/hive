/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hive.druid.serde;

import io.druid.query.spec.LegacySegmentSpec;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.joda.time.Interval;

import java.io.IOException;
import java.util.List;

/**
 * In druid 0.11.0 onwards, Druid deserializes intervals in UTC timezone.
 * So doing a serde of Druid does not convert intervals to user local timezone as expected by HIVE.
 * This is a workaround for making the query serde pickup hive local timezone in interval.
 */
public class LegacySegmentSpecDeSerializer extends JsonDeserializer<LegacySegmentSpec> {

  @Override
  public LegacySegmentSpec deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    List<String> intervals = Lists.newArrayList();
    if(jsonParser.getCurrentToken() != JsonToken.START_ARRAY && jsonParser.nextToken() != JsonToken.START_ARRAY){
      throw new IllegalStateException();
    }
    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
      intervals.add(jsonParser.getValueAsString());
    }
    return new LegacySegmentSpec(
        Lists.transform(intervals, interval -> new Interval(interval)));
  }
}


