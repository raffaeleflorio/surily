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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
            new PathSegmentSubcomponent.Fake("path", ""),
            /*new PathSegmentSubcomponent.Fake("..", ""),
            new PathSegmentSubcomponent.Fake("..", ""),
            */
            new DoubleDotSegment(),
            new DoubleDotSegment(),
            new PathSegmentSubcomponent.Fake("with", ""),
            new PathSegmentSubcomponent.Fake("dot", ""),
            new PathSegmentSubcomponent.Fake("segments", "")
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
            //new PathSegmentSubcomponent.Fake(".", ""),
            new DotSegment(),
            new PathSegmentSubcomponent.Fake("path", "path"),
            /*new PathSegmentSubcomponent.Fake(".", ""),
            new PathSegmentSubcomponent.Fake(".", "")*/
            new DotSegment(),
            new DotSegment()
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
        "absolute",
        new PathWithoutDotSegments(new PathComponent.AbsoluteFake("", "", List.of()))
          .ifAbsoluteElse(x -> "absolute", y -> "relative")
      )
    );
  }
}
