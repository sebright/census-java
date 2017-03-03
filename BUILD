# Copyright 2016, Google Inc.
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#     http://www.apache.org/licenses/LICENSE-2.0
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Description:
#   Open source Census for cloud services.

# Error Prone warnings that this project enforces.
# See http://errorprone.info/bugpatterns .  We need to list all of them,
# because there is no way to enable all Error Prone warnings in Bazel:
# https://github.com/bazelbuild/bazel/issues/2237
error_prone_warnings = [
    "AmbiguousMethodReference",
    "BadAnnotationImplementation",
    "BadComparable",
    "BoxedPrimitiveConstructor",
    "CannotMockFinalClass",
    "ClassCanBeStatic",
    "ClassNewInstance",
    "DefaultCharset",
    "DoubleCheckedLocking",
    "ElementsCountedInLoop",
    "EqualsHashCode",
    "EqualsIncompatibleType",
    "Finally",
    "FloatingPointLiteralPrecision",
    "FragmentInjection",
    "FragmentNotInstantiable",
    "FunctionalInterfaceClash",
    "FutureReturnValueIgnored",
    "GetClassOnEnum",
    "ImmutableAnnotationChecker",
    "ImmutableEnumChecker",
    "IncompatibleModifiers",
    "InjectOnConstructorOfAbstractClass",
    "InputStreamSlowMultibyteRead",
    "IterableAndIterator",
    "JUnit3FloatingPointComparisonWithoutDelta",
    "JUnitAmbiguousTestClass",
    "LiteralClassName",
    "MissingFail",
    "MissingOverride",
    "MutableConstantField",
    "NarrowingCompoundAssignment",
    "NonAtomicVolatileUpdate",
    "NonOverridingEquals",
    "NullableConstructor",
    "NullablePrimitive",
    "NullableVoid",
    "OperatorPrecedence",
    "OverridesGuiceInjectableMethod",
    "PreconditionsInvalidPlaceholder",
    "ProtoFieldPreconditionsCheckNotNull",
    "ProtocolBufferOrdinal",
    "ReferenceEquality",
    "RequiredModifiers",
    "ShortCircuitBoolean",
    "SimpleDateFormatConstant",
    "StaticGuardedByInstance",
    "SynchronizeOnNonFinalField",
    "TruthConstantAsserts",
    "TypeParameterShadowing",
    "TypeParameterUnusedInFormals",
    "URLEqualsHashCode",
    "UnsynchronizedOverridesSynchronized",
    "WaitNotInLoop",
]

# Treat all Error Prone warnings as errors, so that Bazel enforces them.
javac_options = ["-Xep:" + err + ":ERROR" for err in error_prone_warnings]

java_library(
    name = "common-core",
    srcs = glob(["core/src/main/java/com/google/instrumentation/common/*.java"]),
    javacopts = javac_options,
    deps = [
        "@guava//jar",
        "@jsr305//jar",
    ],
)

java_library(
    name = "shared",
    srcs = glob(["shared/src/main/java/com/google/io/base/*.java"]),
    javacopts = javac_options,
)

java_library(
    name = "stats-core",
    srcs = glob(["core/src/main/java/com/google/instrumentation/stats/*.java"]),
    javacopts = javac_options,
    deps = [
        ":common-core",
        "@jsr305//jar",
    ],
)

java_library(
    name = "trace-core",
    srcs = glob(["core/src/main/java/com/google/instrumentation/trace/*.java"]),
    javacopts = javac_options,
    deps = [
        ":common-core",
        "@guava//jar",
        "@jsr305//jar",
    ],
)

java_library(
    name = "trace-core_context_impl",
    srcs = glob(["core_context_impl/src/main/java/com/google/instrumentation/trace/*.java"]),
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@grpc_context//jar",
        "@jsr305//jar",
    ],
)

java_library(
    name = "stats-core_impl",
    srcs = glob(["core_impl/src/main/java/com/google/instrumentation/stats/*.java"]),
    javacopts = javac_options,
    deps = [
        ":shared",
        ":stats-core",
        "//proto:stats_context-proto-java",
        "@jsr305//jar",
        "@protobuf//jar",
    ],
)

java_binary(
    name = "StatsRunner",
    srcs = ["examples/src/main/java/com/google/instrumentation/stats/StatsRunner.java"],
    javacopts = javac_options,
    main_class = "com.google.instrumentation.stats.StatsRunner",
    deps = [
        ":stats-core",
        ":stats-core_impl",
        "@grpc_context//jar",
        "@guava//jar",
        "@jsr305//jar",
    ],
)

java_binary(
    name = "BasicTracing",
    srcs = ["examples/src/main/java/com/google/instrumentation/trace/BasicTracing.java"],
    javacopts = javac_options,
    main_class = "com.google.instrumentation.trace.BasicTracing",
    deps = [
        ":trace-core",
    ],
)

java_binary(
    name = "BasicContextTracing",
    srcs = ["examples/src/main/java/com/google/instrumentation/trace/BasicContextTracing.java"],
    javacopts = javac_options,
    main_class = "com.google.instrumentation.trace.BasicContextTracing",
    deps = [
        ":common-core",
        ":trace-core",
    ],
)

java_binary(
    name = "BasicScopedTracing",
    srcs = ["examples/src/main/java/com/google/instrumentation/trace/BasicScopedTracing.java"],
    javacopts = javac_options,
    main_class = "com.google.instrumentation.trace.BasicScopedTracing",
    deps = [
        ":common-core",
        ":trace-core",
    ],
)

java_binary(
    name = "MultiSpansTracing",
    srcs = ["examples/src/main/java/com/google/instrumentation/trace/MultiSpansTracing.java"],
    javacopts = javac_options,
    main_class = "com.google.instrumentation.trace.MultiSpansTracing",
    deps = [
        ":trace-core",
    ],
)

java_binary(
    name = "MultiSpansContextTracing",
    srcs = ["examples/src/main/java/com/google/instrumentation/trace/MultiSpansContextTracing.java"],
    javacopts = javac_options,
    main_class = "com.google.instrumentation.trace.MultiSpansContextTracing",
    deps = [
        ":common-core",
        ":trace-core",
    ],
)

java_binary(
    name = "MultiSpansScopedTracing",
    srcs = ["examples/src/main/java/com/google/instrumentation/trace/MultiSpansScopedTracing.java"],
    javacopts = javac_options,
    main_class = "com.google.instrumentation.trace.MultiSpansScopedTracing",
    deps = [
        ":common-core",
        ":trace-core",
    ],
)

java_test(
    name = "DistributionAggregationDescriptorTest",
    srcs = [
        "core/src/test/java/com/google/instrumentation/stats/DistributionAggregationDescriptorTest.java",
    ],
    javacopts = javac_options,
    deps = [
        ":stats-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "DistributionAggregationTest",
    srcs = [
        "core/src/test/java/com/google/instrumentation/stats/DistributionAggregationTest.java",
    ],
    javacopts = javac_options,
    deps = [
        ":stats-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "IntervalAggregationDescriptorTest",
    srcs = [
        "core/src/test/java/com/google/instrumentation/stats/IntervalAggregationDescriptorTest.java",
    ],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":stats-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "IntervalAggregationTest",
    srcs = [
        "core/src/test/java/com/google/instrumentation/stats/IntervalAggregationTest.java",
    ],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":stats-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "MeasurementMapTest",
    srcs = ["core/src/test/java/com/google/instrumentation/stats/MeasurementMapTest.java"],
    javacopts = javac_options,
    deps = [
        ":stats-core",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "MeasurementDescriptorTest",
    srcs = ["core/src/test/java/com/google/instrumentation/stats/MeasurementDescriptorTest.java"],
    javacopts = javac_options,
    deps = [
        ":stats-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "RpcConstantsTest",
    srcs = ["core/src/test/java/com/google/instrumentation/stats/RpcConstantsTest.java"],
    javacopts = javac_options,
    deps = [
        ":stats-core",
        ":stats-core_impl",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "StatsContextTest",
    srcs = ["core/src/test/java/com/google/instrumentation/stats/StatsContextTest.java"],
    javacopts = javac_options,
    deps = [
        ":stats-core",
        ":stats-core_impl",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "StatsContextFactoryTest",
    srcs = ["core/src/test/java/com/google/instrumentation/stats/StatsContextFactoryTest.java"],
    javacopts = javac_options,
    deps = [
        ":shared",
        ":stats-core",
        ":stats-core_impl",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "StringUtilTest",
    srcs = ["core/src/test/java/com/google/instrumentation/stats/StringUtilTest.java"],
    javacopts = javac_options,
    deps = [
        ":stats-core",
        ":stats-core_impl",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "TagKeyTest",
    srcs = ["core/src/test/java/com/google/instrumentation/stats/TagKeyTest.java"],
    javacopts = javac_options,
    deps = [
        ":stats-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "TagTest",
    srcs = ["core/src/test/java/com/google/instrumentation/stats/TagTest.java"],
    javacopts = javac_options,
    deps = [
        ":stats-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "TagValueTest",
    srcs = ["core/src/test/java/com/google/instrumentation/stats/TagValueTest.java"],
    javacopts = javac_options,
    deps = [
        ":stats-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "DurationTest",
    srcs = ["core/src/test/java/com/google/instrumentation/common/DurationTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "ProviderTest",
    srcs = ["core/src/test/java/com/google/instrumentation/common/ProviderTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":stats-core",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "TimestampTest",
    srcs = ["core/src/test/java/com/google/instrumentation/common/TimestampTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "TimestampFactoryTest",
    srcs = ["core/src/test/java/com/google/instrumentation/common/TimestampFactoryTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "ViewDescriptorTest",
    srcs = ["core/src/test/java/com/google/instrumentation/stats/ViewDescriptorTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":stats-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "ViewTest",
    srcs = ["core/src/test/java/com/google/instrumentation/stats/ViewTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":stats-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "BlankSpanTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/BlankSpanTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "EndSpanOptionsTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/EndSpanOptionsTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "LabelValueTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/LabelValueTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "LabelsTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/LabelsTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "NetworkEventTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/NetworkEventTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "SamplersTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/SamplersTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "ScopedSpanHandleTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/ScopedSpanHandleTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@mockito//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "SpanContextTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/SpanContextTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "SpanTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/SpanTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@mockito//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "StartSpanOptionsTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/StartSpanOptionsTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "StatusTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/StatusTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "TraceIdTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/TraceIdTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "TraceOptionsTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/TraceOptionsTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "TracerTest",
    srcs = ["core/src/test/java/com/google/instrumentation/trace/TracerTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@mockito//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "ContextSpanHandlerImplTest",
    srcs = ["core_context_impl/src/test/java/com/google/instrumentation/trace/ContextSpanHandlerImplTest.java"],
    javacopts = javac_options,
    deps = [
        ":common-core",
        ":trace-core",
        ":trace-core_context_impl",
        "@grpc_context//jar",
        "@guava//jar",
        "@guava_testlib//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@mockito//jar",
        "@truth//jar",
    ],
)
