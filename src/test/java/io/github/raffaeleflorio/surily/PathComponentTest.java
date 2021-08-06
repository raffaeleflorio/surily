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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathComponentTest {
  @Nested
  class FakeTest {
    @Test
    void testIterator() throws Throwable {
      var expected = List.<PathSegmentSubcomponent>of(
        new PathSegmentSubcomponent.NormalFake("encoded", "asString"),
        new PathSegmentSubcomponent.NormalFake("encoded1", "asString"),
        new PathSegmentSubcomponent.NormalFake("encoded2", "asString")
      );
      assertIterableEquals(expected, new PathComponent.Fake(expected));
    }

    @Test
    void testAsString() throws Throwable {
      var expected = "this path is encoded";
      assertEquals(
        expected,
        new PathComponent.Fake(expected, "any stuff").encoded(StandardCharsets.UTF_16BE)
      );
    }

    @Test
    void testEncoded() throws Throwable {
      var expected = "this is asString";
      assertEquals(expected, new PathComponent.Fake("xyz", expected).asString());
    }

    @Test
    void testRelativePart() {
      assertAll(
        () -> assertEquals(
          "relative part",
          new PathComponent.Fake(
            new UriComponent.Fake("relative part", ""),
            new UriComponent.Fake("hier", "part")
          ).relativePart().encoded(StandardCharsets.UTF_8)
        ),
        () -> assertEquals(
          "relative part",
          new PathComponent.Fake(
            new UriComponent.Fake("", "relative part"),
            new UriComponent.Fake("hier", "part")
          ).relativePart(
            new AuthorityComponent.Fake("", "")
          ).asString()
        )
      );
    }

    @Test
    void testHierPart() {
      assertAll(
        () -> assertEquals(
          "hier part",
          new PathComponent.Fake(
            new UriComponent.Fake("relative", "part"),
            new UriComponent.Fake("hier part", "")
          ).hierPart().encoded(StandardCharsets.UTF_8)
        ),
        () -> assertEquals(
          "hier part",
          new PathComponent.Fake(
            new UriComponent.Fake("relative", "part"),
            new UriComponent.Fake("", "hier part")
          ).hierPart(
            new AuthorityComponent.Fake("", "")
          ).asString()
        )
      );
    }
  }
}
