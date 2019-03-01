/*
 * Copyright 2019, OpenCensus Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opencensus.stats;

import com.google.auto.value.AutoValue;
import io.opencensus.stats.AggregationData.DistributionData.Exemplar;
import javax.annotation.concurrent.Immutable;

/**
 * The value of {@link Exemplar} attachment.
 *
 * <p>In Stats API we only provide one subclass {@link AttachmentValueString}. No other subclasses
 * are added because we don't want to introduce dependencies on other libraries, for example Tracing
 * APIs.
 *
 * <p>Other packages are free to extend this class to hold specific information. As an example, see
 * {@code io.opencensus.contrib.exemplar.util.AttachmentValueSpanContext}.
 *
 * @since 0.20
 */
public abstract class AttachmentValue {

  /**
   * Returns the string attachment value.
   *
   * @return the string attachment value.
   * @since 0.20
   */
  public abstract String getValue();

  /**
   * String {@link AttachmentValue}.
   *
   * @since 0.20
   */
  @AutoValue
  @Immutable
  public abstract static class AttachmentValueString extends AttachmentValue {

    AttachmentValueString() {}

    /**
     * Creates an {@link AttachmentValueString}.
     *
     * @param value the string value.
     * @return an {@code AttachmentValueString}.
     * @since 0.20
     */
    public static AttachmentValueString create(String value) {
      return new AutoValue_AttachmentValue_AttachmentValueString(value);
    }
  }
}
