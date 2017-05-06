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
import com.google.instrumentation.common.SimpleEventQueue;
import com.google.instrumentation.common.Timestamp;
import com.google.instrumentation.internal.TestClock;
import com.google.instrumentation.stats.View.DistributionView;
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
 * Tests for {@link StatsManagerImplBase}.
 */
@RunWith(JUnit4.class)
public class StatsManagerImplTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  private static final double TOLERANCE = 1e-5;
  private static final TagKey tagKey = RpcMeasurementConstants.RPC_CLIENT_METHOD;
  private static final TagKey wrongTagKey = TagKey.create("Wrong Tag Key");
  private static final TagKey wrongTagKey2 = TagKey.create("Another wrong Tag Key");
  private static final TagValue tagValue1 = TagValue.create("some client method");
  private static final TagValue tagValue2 = TagValue.create("some other client method");
  private final TestClock clock = TestClock.create();
  private final StatsManagerImplBase statsManager =
      new StatsManagerImplBase(new SimpleEventQueue(), clock);
  private final StatsContextImpl oneTag =
      new StatsContextImpl(statsManager, ImmutableMap.of(tagKey, tagValue1));
  private final StatsContextImpl anotherTag =
      new StatsContextImpl(statsManager, ImmutableMap.of(tagKey, tagValue2));
  private final StatsContextImpl wrongTag =
      new StatsContextImpl(statsManager, ImmutableMap.of(wrongTagKey, tagValue1));
  private final StatsContextImpl wrongTag2 =
      new StatsContextImpl(
          statsManager, ImmutableMap.of(wrongTagKey, tagValue1, wrongTagKey2, tagValue2));

  @Test
  public void testRegisterAndGetView() throws Exception {
    statsManager.registerView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    View actual = statsManager.getView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    assertThat(actual.getViewDescriptor()).isEqualTo(
        RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
  }

  @Test
  public void testRegisterUnsupportedViewDescriptor() throws Exception {
    thrown.expect(UnsupportedOperationException.class);
    statsManager.registerView(RpcViewConstants.RPC_CLIENT_REQUEST_COUNT_VIEW);
  }

  @Test
  public void testRegisterViewDescriptorTwice() {
    statsManager.registerView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    statsManager.registerView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    View actual = statsManager.getView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    assertThat(actual.getViewDescriptor()).isEqualTo(
        RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
  }

  // TODO(sebright) Enable this test once we support more than one view.
  @Ignore
  @Test
  public void preventRegisteringDifferentViewDescriptorWithSameName() {
    statsManager.registerView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    ViewDescriptor view2 =
      DistributionViewDescriptor.create(
          "grpc.io/client/roundtrip_latency/distribution_cumulative",
          "This is a different description.",
          RpcMeasurementConstants.RPC_CLIENT_ROUNDTRIP_LATENCY,
          DistributionAggregationDescriptor.create(RpcViewConstants.RPC_MILLIS_BUCKET_BOUNDARIES),
          Arrays.asList(RpcMeasurementConstants.RPC_CLIENT_METHOD));
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("A different view with the same name is already registered");
    statsManager.registerView(view2);
  }

  @Test
  public void testGetNonexistentView() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    statsManager.getView(RpcViewConstants.RPC_CLIENT_REQUEST_COUNT_VIEW);
  }

  @Test
  public void testRecord() {
    clock.setTime(Timestamp.create(1, 2));
    statsManager.registerView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    for (double val : Arrays.<Double>asList(10.0, 20.0, 30.0, 40.0)) {
      statsManager.record(
          oneTag, MeasurementMap.of(RpcMeasurementConstants.RPC_CLIENT_ROUNDTRIP_LATENCY, val));
    }
    clock.setTime(Timestamp.create(3, 4));
    DistributionView view =
        (DistributionView) statsManager.getView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);

    assertThat(StatsTestUtil.canonicalizeView(view)).isEqualTo(
        DistributionView.create(
            RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW,
            Arrays.asList(
                StatsTestUtil.createDistributionAggregation(
                    Arrays.asList(Tag.create(tagKey, tagValue1)),
                    RpcViewConstants
                        .RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW
                        .getDistributionAggregationDescriptor()
                        .getBucketBoundaries(),
                    10.0, 20.0, 30.0, 40.0)),
            Timestamp.create(1, 2),
            Timestamp.create(3, 4)));
  }

  @Test
  public void testRecordMultipleTagValues() {
    clock.setTime(Timestamp.create(5, 6));
    statsManager.registerView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    statsManager.record(oneTag, MeasurementMap.of(
        RpcMeasurementConstants.RPC_CLIENT_ROUNDTRIP_LATENCY, 10.0));
    statsManager.record(anotherTag, MeasurementMap.of(
        RpcMeasurementConstants.RPC_CLIENT_ROUNDTRIP_LATENCY, 30.0));
    statsManager.record(anotherTag, MeasurementMap.of(
        RpcMeasurementConstants.RPC_CLIENT_ROUNDTRIP_LATENCY, 50.0));
    clock.setTime(Timestamp.create(7, 8));
    DistributionView view =
        (DistributionView) statsManager.getView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);

    assertThat(StatsTestUtil.canonicalizeView(view)).isEqualTo(
        DistributionView.create(
            RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW,
            Arrays.asList(
                StatsTestUtil.createDistributionAggregation(
                    Arrays.asList(Tag.create(tagKey, tagValue1)),
                    RpcViewConstants
                        .RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW
                        .getDistributionAggregationDescriptor()
                        .getBucketBoundaries(),
                    10.0),
                StatsTestUtil.createDistributionAggregation(
                    Arrays.asList(Tag.create(tagKey, tagValue2)),
                    RpcViewConstants
                        .RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW
                        .getDistributionAggregationDescriptor()
                        .getBucketBoundaries(),
                    30.0, 50.0)),
            Timestamp.create(5, 6),
            Timestamp.create(7, 8)));
  }

  @Test
  public void testRecordWithoutRegisteringView() {
    thrown.expect(IllegalArgumentException.class);
    statsManager.record(oneTag, MeasurementMap.of(
        RpcMeasurementConstants.RPC_CLIENT_ROUNDTRIP_LATENCY, 10));
  }

  @Test
  public void testRecordWithEmptyStatsContext() {
    clock.setTime(Timestamp.create(1, 2));
    statsManager.registerView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    // DEFAULT doesn't have tags. Should have TagKey "method" as defined in RpcViewConstants.
    statsManager.record(statsManager.getStatsContextFactory().getDefault(), MeasurementMap.of(
        RpcMeasurementConstants.RPC_CLIENT_ROUNDTRIP_LATENCY, 10.0));
    clock.setTime(Timestamp.create(3, 4));
    DistributionView view =
        (DistributionView) statsManager.getView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);

    assertThat(StatsTestUtil.canonicalizeView(view)).isEqualTo(
        DistributionView.create(
            RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW,
            Arrays.asList(
                StatsTestUtil.createDistributionAggregation(
                    Arrays.asList(Tag.create(tagKey, MutableView.UNKNOWN_TAG_VALUE)),
                    RpcViewConstants
                        .RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW
                        .getDistributionAggregationDescriptor()
                        .getBucketBoundaries(),
                    10.0)),
            Timestamp.create(1, 2),
            Timestamp.create(3, 4)));
  }

  @Test
  public void testRecordNonExistentMeasurementDescriptor() {
    statsManager.registerView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    statsManager.record(oneTag, MeasurementMap.of(
        RpcMeasurementConstants.RPC_SERVER_ERROR_COUNT, 10.0));
    DistributionView view =
        (DistributionView) statsManager.getView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    assertThat(view.getDistributionAggregations()).hasSize(0);
  }

  @Test
  public void testRecordTagDoesNotMatchView() {
    clock.setTime(Timestamp.create(1, 2));
    statsManager.registerView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);
    statsManager.record(wrongTag, MeasurementMap.of(
        RpcMeasurementConstants.RPC_CLIENT_ROUNDTRIP_LATENCY, 10.0));
    statsManager.record(wrongTag2, MeasurementMap.of(
        RpcMeasurementConstants.RPC_CLIENT_ROUNDTRIP_LATENCY, 50.0));
    clock.setTime(Timestamp.create(3, 4));
    DistributionView view =
        (DistributionView) statsManager.getView(RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW);

    assertThat(StatsTestUtil.canonicalizeView(view)).isEqualTo(
        DistributionView.create(
            RpcViewConstants.RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW,
            Arrays.asList(
                StatsTestUtil.createDistributionAggregation(
                    Arrays.asList(Tag.create(tagKey, MutableView.UNKNOWN_TAG_VALUE)),
                    RpcViewConstants
                        .RPC_CLIENT_ROUNDTRIP_LATENCY_VIEW
                        .getDistributionAggregationDescriptor()
                        .getBucketBoundaries(),
                    10.0, 50.0)),
            Timestamp.create(1, 2),
            Timestamp.create(3, 4)));
  }
}
