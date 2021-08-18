/*
   Copyright 2021 Raffaele Florio

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package io.github.raffaeleflorio.surily.path;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class NormalizedSegmentsTest {
  @Test
  void testIterator() {
    assertIterableEquals(
      List.of("ok"),
      asString(
        new NormalizedSegments(
          new PathComponent.Fake(
            List.of(
              new PathSegmentSubcomponent.SingleDotFake("", ""),
              new PathSegmentSubcomponent.NormalFake("", ""),
              new PathSegmentSubcomponent.DoubleDotFake("", ""),
              new PathSegmentSubcomponent.NormalFake("ok", "ok"),
              new PathSegmentSubcomponent.SingleDotFake("", "")
            )
          )
        )
      )
    );
  }

  private Iterable<String> asString(final Iterable<PathSegmentSubcomponent> segments) {
    return StreamSupport.stream(segments.spliterator(), false)
      .map(PathSegmentSubcomponent::asString)
      .collect(Collectors.toUnmodifiableList());
  }

  @Test
  void testEmptyOrigin() {
    assertFalse(new NormalizedSegments(List.of()).iterator().hasNext());
  }

  @Test
  void testDoubleDotAsFirstSegment() {
    assertIterableEquals(
      List.of("ok", "ok2"),
      asString(
        new NormalizedSegments(
          new PathComponent.Fake(
            List.of(
              new PathSegmentSubcomponent.DoubleDotFake("", ""),
              new PathSegmentSubcomponent.NormalFake("", ""),
              new PathSegmentSubcomponent.DoubleDotFake("", ""),
              new PathSegmentSubcomponent.NormalFake("", "ok"),
              new PathSegmentSubcomponent.NormalFake("", "ok2")
            )
          )
        )
      )
    );
  }

  @Test
  void testSize() {
    assertEquals(
      1,
      new NormalizedSegments(
        List.of(
          new PathSegmentSubcomponent.DoubleDotFake("", ""),
          new PathSegmentSubcomponent.DoubleDotFake("", ""),
          new PathSegmentSubcomponent.NormalFake("normal", "normal"),
          new PathSegmentSubcomponent.SingleDotFake("", "")
        )
      ).size()
    );
  }

  @Test
  void testGet() {
    assertEquals(
      "normal",
      new NormalizedSegments(
        List.of(
          new PathSegmentSubcomponent.DoubleDotFake("", ""),
          new PathSegmentSubcomponent.DoubleDotFake("", ""),
          new PathSegmentSubcomponent.NormalFake("normal", "normal"),
          new PathSegmentSubcomponent.SingleDotFake("", "")
        )
      ).get(0).asString()
    );
  }
}
