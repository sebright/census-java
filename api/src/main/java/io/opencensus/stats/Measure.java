/*
 * Copyright 2017, OpenCensus Authors
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

import static com.google.common.base.Preconditions.checkArgument;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.VisibleForTesting;
import io.opencensus.common.Function;
import io.opencensus.internal.NullnessUtils;
import io.opencensus.internal.StringUtil;
import javax.annotation.concurrent.Immutable;

/*>>>
import org.checkerframework.checker.nullness.qual.Nullable;
*/

/** The definition of the {@link Measurement} that is taken by OpenCensus library. */
@Immutable
public abstract class Measure {

  @VisibleForTesting static final int NAME_MAX_LENGTH = 255;

  /** Applies the given match function to the underlying data type. */
  public abstract <T> T match(
      Function<? super MeasureDouble, T> p0,
      Function<? super MeasureLong, T> p1,
      Function<? super Measure, T> defaultFunction);

  /**
   * Name of measure, as a {@code String}. Should be a ASCII string with a length no greater than
   * 255 characters.
   *
   * <p>Suggested format for name: {@code <web_host>/<path>}.
   */
  public abstract String getName();

  /** Detailed description of the measure, used in documentation. */
  public abstract String getDescription();

  /**
   * The units in which {@link Measure} values are measured.
   *
   * <p>The suggested grammar for a unit is as follows:
   *
   * <ul>
   *   <li>Expression = Component { "." Component } {"/" Component };
   *   <li>Component = [ PREFIX ] UNIT [ Annotation ] | Annotation | "1";
   *   <li>Annotation = "{" NAME "}" ;
   * </ul>
   *
   * <p>For example, string “MBy{transmitted}/ms” stands for megabytes per milliseconds, and the
   * annotation transmitted inside {} is just a comment of the unit.
   */
  // TODO(songya): determine whether we want to check the grammar on string unit.
  public abstract String getUnit();

  // Prevents this class from being subclassed anywhere else.
  private Measure() {}

  /** {@link Measure} with {@code Double} typed values. */
  @Immutable
  @AutoValue
  // Suppress Checker Framework warning about missing @Nullable in generated equals method.
  @AutoValue.CopyAnnotations
  @SuppressWarnings("nullness")
  public abstract static class MeasureDouble extends Measure {

    MeasureDouble() {}

    /**
     * Constructs a new {@link MeasureDouble}.
     *
     * @param name name of {@code Measure}. Suggested format: {@code <web_host>/<path>}.
     * @param description description of {@code Measure}.
     * @param unit unit of {@code Measure}.
     * @return a {@code MeasureDouble}.
     */
    public static MeasureDouble create(String name, String description, String unit) {
      checkArgument(
          StringUtil.isPrintableString(name) && name.length() <= NAME_MAX_LENGTH,
          "Name should be a ASCII string with a length no greater than "
              + NAME_MAX_LENGTH
              + " characters.");
      return new AutoValue_Measure_MeasureDouble(name, description, unit);
    }

    @Override
    public <T> T match(
        Function<? super MeasureDouble, T> p0,
        Function<? super MeasureLong, T> p1,
        Function<? super Measure, T> defaultFunction) {
      return NullnessUtils.<MeasureDouble, T>removeSuper(p0).apply(this);
    }

    @Override
    public abstract String getName();

    @Override
    public abstract String getDescription();

    @Override
    public abstract String getUnit();
  }

  /** {@link Measure} with {@code Long} typed values. */
  @Immutable
  @AutoValue
  // Suppress Checker Framework warning about missing @Nullable in generated equals method.
  @AutoValue.CopyAnnotations
  @SuppressWarnings("nullness")
  public abstract static class MeasureLong extends Measure {

    MeasureLong() {}

    /**
     * Constructs a new {@link MeasureLong}.
     *
     * @param name name of {@code Measure}. Suggested format: {@code <web_host>/<path>}.
     * @param description description of {@code Measure}.
     * @param unit unit of {@code Measure}.
     * @return a {@code MeasureLong}.
     */
    public static MeasureLong create(String name, String description, String unit) {
      checkArgument(
          StringUtil.isPrintableString(name) && name.length() <= NAME_MAX_LENGTH,
          "Name should be a ASCII string with a length no greater than "
              + NAME_MAX_LENGTH
              + " characters.");
      return new AutoValue_Measure_MeasureLong(name, description, unit);
    }

    @Override
    public <T> T match(
        Function<? super MeasureDouble, T> p0,
        Function<? super MeasureLong, T> p1,
        Function<? super Measure, T> defaultFunction) {
      return NullnessUtils.<MeasureLong, T>removeSuper(p1).apply(this);
    }

    @Override
    public abstract String getName();

    @Override
    public abstract String getDescription();

    @Override
    public abstract String getUnit();
  }
}
