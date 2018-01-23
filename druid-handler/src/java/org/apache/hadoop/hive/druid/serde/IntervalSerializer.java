package org.apache.hadoop.hive.druid.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import org.joda.time.Interval;

import java.io.IOException;

/**
 * In druid 0.11.0 onwards, Druid deserializes intervals in UTC timezone.
 * So doing a serde of Druid does not convert intervals to user local timezone as expected by HIVE.
 * This is a workaround for making the query serde pickup hive local timezone in interval.
 */
class IntervalDeserializer extends StdDeserializer<Interval>
{
  public IntervalDeserializer()
  {
    super(Interval.class);
  }

  @Override
  public Interval deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException
  {
    return new Interval(jsonParser.getText());
  }
}
