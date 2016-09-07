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

java_library(
    name = "census-core",
    srcs = glob(["core/java/com/google/census/*.java"]),
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    deps = [
        "@guava//jar",
        "@jsr305//jar",
    ],
)

java_library(
    name = "census-core_native",
    srcs = glob(["core_native/java/com/google/census/*.java"]),
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    deps = [
        ":census-core",
        "@guava//jar",
        "@jsr305//jar",
    ],
)

java_binary(
    name = "CensusRunner",
    srcs = ["examples/java/com/google/census/CensusRunner.java"],
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    main_class = "com.google.census.examples.CensusRunner",
    deps = [
        ":census-core",
        ":census-core_native",
        "@guava//jar",
        "@jsr305//jar",
    ],
)

java_test(
    name = "CensusContextTest",
    srcs = ["core/javatests/com/google/census/CensusContextTest.java"],
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    deps = [
        ":census-core",
        ":census-core_native",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "CensusContextFactoryTest",
    srcs = ["core/javatests/com/google/census/CensusContextFactoryTest.java"],
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    deps = [
        ":census-core",
        ":census-core_native",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "CensusScopeTest",
    srcs = ["core/javatests/com/google/census/CensusScopeTest.java"],
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    deps = [
        ":census-core",
        ":census-core_native",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "MetricMapTest",
    srcs = ["core/javatests/com/google/census/MetricMapTest.java"],
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    deps = [
        ":census-core",
        ":census-core_native",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "ProviderTest",
    srcs = ["core/javatests/com/google/census/ProviderTest.java"],
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    deps = [
        ":census-core",
        ":census-core_native",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "RpcConstantsTest",
    srcs = ["core/javatests/com/google/census/RpcConstantsTest.java"],
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    deps = [
        ":census-core",
        ":census-core_native",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "TagKeyTest",
    srcs = ["core/javatests/com/google/census/TagKeyTest.java"],
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    deps = [
        ":census-core",
        ":census-core_native",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "TagTest",
    srcs = ["core/javatests/com/google/census/TagTest.java"],
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    deps = [
        ":census-core",
        ":census-core_native",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)

java_test(
    name = "TagValueTest",
    srcs = ["core/javatests/com/google/census/TagValueTest.java"],
    javacopts = [
        "-source 1.6",
        "-target 1.6",
    ],
    deps = [
        ":census-core",
        ":census-core_native",
        "@guava//jar",
        "@jsr305//jar",
        "@junit//jar",
        "@truth//jar",
    ],
)
