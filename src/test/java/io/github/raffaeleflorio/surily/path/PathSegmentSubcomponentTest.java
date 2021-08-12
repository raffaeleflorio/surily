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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class PathSegmentSubcomponentTest {
  @Nested
  class NormalFakeTest {
    @Test
    void testEncoded() {
      var expected = "the encoded representation";
      assertEquals(
        expected,
        new PathSegmentSubcomponent.NormalFake(expected, "xyz").encoded(StandardCharsets.UTF_16BE)
      );
    }

    @Test
    void testAsString() {
      var expected = "the asString representation";
      assertEquals(
        expected,
        new PathSegmentSubcomponent.NormalFake("any", expected).asString()
      );
    }

    @Test
    void testIfDotElse() {
      assertAll(
        () -> assertFalse(
          new PathSegmentSubcomponent.NormalFake("", "").<Boolean>ifDotElse(x -> true, y -> true, z -> false)
        ),
        () -> assertFalse(
          new PathSegmentSubcomponent.NormalFake("..", "..").<Boolean>ifDotElse(x -> true, y -> true, z -> false)
        )
      );
    }
  }

  @Nested
  class SingleDotFakeTest {
    @Test
    void assertEncoded() {
      var expected = "encoded single dot";
      assertEquals(
        expected,
        new PathSegmentSubcomponent.SingleDotFake(expected, "").encoded(StandardCharsets.US_ASCII)
      );
    }

    @Test
    void assertAsString() {
      var expected = "asString representaiton";
      assertEquals(
        expected,
        new PathSegmentSubcomponent.SingleDotFake("", expected).asString()
      );
    }

    @Test
    void testIfDotElse() {
      var expected = "a single dot";
      assertEquals(
        expected,
        new PathSegmentSubcomponent.SingleDotFake("", "")
          .ifDotElse(
            x -> expected,
            y -> "not a double dot",
            z -> "neither a normal segment"
          )
      );
    }
  }

  @Nested
  class DoubleDotFakeTest {
    @Test
    void assertEncoded() {
      var expected = "encoded double dot";
      assertEquals(
        expected,
        new PathSegmentSubcomponent.DoubleDotFake(expected, "").encoded(StandardCharsets.US_ASCII)
      );
    }

    @Test
    void assertAsString() {
      var expected = "asString representaiton";
      assertEquals(
        expected,
        new PathSegmentSubcomponent.DoubleDotFake("", expected).asString()
      );
    }

    @Test
    void testIfDotElse() {
      var expected = "a double dot";
      assertEquals(
        expected,
        new PathSegmentSubcomponent.DoubleDotFake("", "")
          .ifDotElse(
            x -> "not a single dot",
            y -> expected,
            z -> "not a normal segment"
          )
      );
    }
  }
}
