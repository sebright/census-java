/*
 * Copyright 2016, Google Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.census;

import static com.google.common.truth.Truth.assertThat;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link CensusContextFactoryImpl}.
 */
@RunWith(JUnit4.class)
public class CensusContextFactoryImplTest {
  @Test
  public void testDeserialize() throws Exception {
    String s;
    try {
      s = new String(DEFAULT.serialize().array(), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("UTF-8 not supported.");
    }
    CensusContext.Builder builder = DEFAULT.builder();
    String key = "k";
    String val = "v";
    for (int ix = 0; ix < 5; ix++) {
      builder.set(new TagKey(key), new TagValue(val));
      s += "\2" + key + "\3" + val;
      assertThat(builder.build()).isEqualTo(contextFactory.deserialize(encode(s)));
      key += key;
      val += val;
    }
  }

  @Test
  public void testDeserializeEdgeCases() {
    assertThat(contextFactory.deserialize(encode(""))).isEqualTo(DEFAULT);
    assertThat(contextFactory.deserialize(encode("\2\3")))
        .isEqualTo(DEFAULT.with(new TagKey(""), new TagValue("")));
  }

  @Test
  public void testDeserializeMalformedContext() {
    assertThat(contextFactory.deserialize(encode("g"))).isNull();
    assertThat(contextFactory.deserialize(encode("garbagedata"))).isNull();
    assertThat(contextFactory.deserialize(encode("\2key"))).isNull();
    assertThat(contextFactory.deserialize(encode("\3val"))).isNull();
    assertThat(contextFactory.deserialize(encode("\2key\2\3val"))).isNull();
    assertThat(contextFactory.deserialize(encode("\2key\3val\3"))).isNull();
    assertThat(contextFactory.deserialize(encode("\2key\3val\2key2"))).isNull();
  }

  private static final CensusContextFactory contextFactory = new CensusContextFactoryImpl();
  private static final CensusContext DEFAULT = contextFactory.getDefault();

  private static ByteBuffer encode(String s) {
    try {
      return ByteBuffer.wrap(s.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("UTF-8 not supported.");
    }
  }
}
