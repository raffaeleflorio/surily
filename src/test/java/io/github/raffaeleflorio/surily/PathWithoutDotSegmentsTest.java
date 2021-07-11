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
package io.github.raffaeleflorio.surily;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class PathWithoutDotSegmentsTest {
  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "with/dot/segments",
      new PathWithoutDotSegments(
        new PathComponent.RelativeFake(
          "",
          "",
          List.of(
            new PathSegmentSubcomponent.NormalFake("path", ""),
            new PathSegmentSubcomponent.DoubleDotFake("", ""),
            new PathSegmentSubcomponent.DoubleDotFake("", ""),
            new PathSegmentSubcomponent.NormalFake("with", ""),
            new PathSegmentSubcomponent.NormalFake("dot", ""),
            new PathSegmentSubcomponent.NormalFake("segments", "")
          )
        )
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "/path",
      new PathWithoutDotSegments(
        new PathComponent.AbsoluteFake(
          "",
          "",
          List.of(
            new PathSegmentSubcomponent.SingleDotFake(".", "."),
            new PathSegmentSubcomponent.NormalFake("path", "path"),
            new PathSegmentSubcomponent.SingleDotFake("xyz", "abc"),
            new PathSegmentSubcomponent.SingleDotFake("any", "stuff")
          )
        )
      ).asString()
    );
  }

  @Test
  void testIfAbsoluteElse() throws Throwable {
    assertAll(
      () -> assertEquals(
        "relative",
        new PathWithoutDotSegments(new PathComponent.RelativeFake("", "", List.of()))
          .ifAbsoluteElse(x -> "absolute", y -> "relative")
      ),
      () -> assertEquals(
        "segment",
        new PathWithoutDotSegments(
          new PathComponent.RelativeFake(
            "",
            "",
            List.of(
              new PathSegmentSubcomponent.SingleDotFake("", ""),
              new PathSegmentSubcomponent.NormalFake("", "segment")
            )
          )
        ).ifAbsoluteElse(x -> "absolute", UriComponent::asString)
      ),
      () -> assertEquals(
        "absolute",
        new PathWithoutDotSegments(new PathComponent.AbsoluteFake("", "", List.of()))
          .ifAbsoluteElse(x -> "absolute", y -> "relative")
      ),
      () -> assertEquals(
        "/segment",
        new PathWithoutDotSegments(
          new PathComponent.AbsoluteFake(
            "",
            "",
            List.of(
              new PathSegmentSubcomponent.SingleDotFake("", ""),
              new PathSegmentSubcomponent.NormalFake("", "segment")
            )
          )
        ).ifAbsoluteElse(UriComponent::asString, y -> "relative")

      )
    );
  }

  @Test
  void testIterable() throws Throwable {
    assertIterableEquals(
      List.of(
        "segment0", "segment2"
      ),
      asString(
        new PathWithoutDotSegments(
          new PathComponent.AbsoluteFake(
            "",
            "",
            List.of(
              new PathSegmentSubcomponent.NormalFake("", "segment0"),
              new PathSegmentSubcomponent.SingleDotFake("", ""),
              new PathSegmentSubcomponent.NormalFake("", "segment2")
            )
          )
        )
      )
    );
  }

  private List<String> asString(final PathComponent path) {
    return StreamSupport.stream(path.spliterator(), false)
      .map(PathSegmentSubcomponent::asString)
      .collect(Collectors.toUnmodifiableList());
  }
}
