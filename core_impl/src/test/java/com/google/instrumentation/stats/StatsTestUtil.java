/*
 * Copyright 2017, Google Inc.
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

package com.google.instrumentation.stats;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import com.google.instrumentation.common.Function;
import com.google.instrumentation.common.SimpleEventQueue;
import com.google.instrumentation.common.Timestamp;
import com.google.instrumentation.internal.TestClock;
import com.google.instrumentation.stats.View.DistributionView;
import com.google.instrumentation.stats.View.IntervalView;
import com.google.instrumentation.stats.ViewDescriptor.DistributionViewDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Stats test utilities.
 */
public final class StatsTestUtil {
  private StatsTestUtil() {}

  public static View canonicalizeView(View view) {
    return view.match(
        new Function<View.DistributionView, View>() {
          @Override
          public View apply(DistributionView view) {
            List<DistributionAggregation> distributionAggregations =
                new ArrayList<DistributionAggregation>(view.getDistributionAggregations());
            Collections.sort(distributionAggregations, new Comparator<DistributionAggregation>() {
                @Override
                public int compare(DistributionAggregation agg1, DistributionAggregation agg2) {
                  List<String> tagValues1 = tagValueNames(agg1.getTags());
                  List<String> tagValues2 = tagValueNames(agg2.getTags());
                  return Ordering
                      .<String>natural()
                      .lexicographical()
                      .compare(tagValues1, tagValues2);
                }
              });
            return DistributionView.create(
                view.getViewDescriptor(),
                distributionAggregations,
                view.getStart(),
                view.getEnd());
          }
        },
        new Function<View.IntervalView, View>() {
          @Override
          public View apply(IntervalView view) {
            throw new UnsupportedOperationException("not implemented yet");
          }
        });
  }

  private static List<String> tagValueNames(List<Tag> tags) {
    List<String> names = new ArrayList<String>();
    for (Tag tag : tags) {
      names.add(tag.getValue().asString());
    }
    return names;
  }

  public static DistributionAggregation createDistributionAggregation(
      List<Tag> tags, List<Double> buckets, double ... values) {
    MutableDistribution mdist = MutableDistribution.create(BucketBoundaries.create(buckets));
    for (double value : values) {
      mdist.add(value);
    }
    MutableDistribution.Range range = mdist.getRange();
    return DistributionAggregation.create(
        mdist.getCount(),
        mdist.getMean(),
        mdist.getSum(),
        DistributionAggregation.Range.create(range.getMin(), range.getMax()),
        tags,
        mdist.getBucketCounts());
  }
}
