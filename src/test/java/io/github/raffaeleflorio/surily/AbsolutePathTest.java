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
import org.junit.jupiter.api.function.Executable;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class AbsolutePathTest {
  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "/%2F/%2F/S%20%23%CF%80.xml",
      new AbsolutePath(
        List.of(
          new PathSegmentSubcomponent.Fake("%2F", "/"),
          new PathSegmentSubcomponent.Fake("%2F", "/"),
          new PathSegmentSubcomponent.Fake("S%20%23%CF%80.xml", "S #\u03C0.xml")
        )
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testRootPath() throws Throwable {
    assertEquals("/", new AbsolutePath().encoded(StandardCharsets.US_ASCII));
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "/segment with ? \u03BB reserved characters[].json",
      new AbsolutePath(
        List.of(
          new PathSegmentSubcomponent.Fake("encoded representation", "segment with ? \u03BB reserved characters[].json")
        )
      ).asString()
    );
  }

  @Test
  void testIterator() throws Throwable {
    assertIterableEquals(
      List.of("segment 0", "segment 1", "segment 2"),
      asString(
        new AbsolutePath(
          List.of(
            new PathSegmentSubcomponent.Fake("encoded", "segment 0"),
            new PathSegmentSubcomponent.Fake("encoded", "segment 1"),
            new PathSegmentSubcomponent.Fake("encoded", "segment 2")
          )
        )
      )
    );
  }

  private Iterable<?> asString(final AbsolutePath absolutePath) {
    return StreamSupport.stream(absolutePath.spliterator(), false)
      .map(PathSegmentSubcomponent::asString)
      .collect(Collectors.toUnmodifiableList());
  }

  @Test
  void testZeroSegmentAsFirstSegment() throws Throwable {
    assertAll(
      () -> assertIllegalZeroSegment(
        () -> new AbsolutePath(List.of(new PathSegmentSubcomponent.Fake("", ""))).asString()
      ),
      () -> assertIllegalZeroSegment(
        () -> new AbsolutePath(List.of(new PathSegmentSubcomponent.Fake("", ""))).encoded(StandardCharsets.UTF_8)
      ),
      () -> assertIllegalZeroSegment(
        () -> new AbsolutePath(List.of(new PathSegmentSubcomponent.Fake("", ""))).iterator().next().asString()
      )
    );
  }

  private void assertIllegalZeroSegment(final Executable executable) {
    assertThrowsWithMessage(
      IllegalStateException.class,
      executable,
      "Illegal non-zero segment"
    );
  }

  private void assertThrowsWithMessage(final Class<? extends Throwable> expectedException, final Executable executable, final String expectedMessage) {
    assertEquals(
      expectedMessage,
      assertThrows(expectedException, executable).getMessage()
    );
  }

  @Test
  void testZeroSegmentsExceptFirstOne() throws Throwable {
    assertEquals(
      "/first//",
      new AbsolutePath(
        List.of(
          new PathSegmentSubcomponent.Fake("first", "first"),
          new PathSegmentSubcomponent.Fake("", ""),
          new PathSegmentSubcomponent.Fake("", "")
        )
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testIfAbsoluteElse() throws Throwable {
    assertAll(
      () -> assertTrue(
        new AbsolutePath(List.of(new PathSegmentSubcomponent.Fake("x", "x")))
          .<Boolean>ifAbsoluteElse(x -> true, x -> false)
      ),
      () -> assertTrue(new AbsolutePath().<Boolean>ifAbsoluteElse(x -> true, x -> false))
    );
  }
}
