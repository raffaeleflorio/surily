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

class RelativePathTest {
  @Test
  void testRelativeEmptyPath() throws Throwable {
    assertAll(
      () -> assertEquals("", new RelativePath().encoded(StandardCharsets.UTF_8)),
      () -> assertEquals("", new RelativePath().asString())
    );
  }

  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "%2F/../../ok%5B.txt",
      new RelativePath(
        List.of(
          new PathSegmentSubcomponent.Fake("%2F", "/"),
          new PathSegmentSubcomponent.Fake("..", ".."),
          new PathSegmentSubcomponent.Fake("..", ".."),
          new PathSegmentSubcomponent.Fake("ok%5B.txt", "ok[.txt")
        )
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testFirstSegmentWithColon() throws Throwable {
    assertEquals(
      "x%3Ay%3Az/x:y:z",
      new RelativePath(
        List.of(
          new PathSegmentSubcomponent.Fake("x:y:z", "x:y:z"),
          new PathSegmentSubcomponent.Fake("x:y:z", "x:y:z")
        )
      ).encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testIterator() throws Throwable {
    assertIterableEquals(
      List.of(".", "segment:with:colon"),
      encoded(new RelativePath(
          List.of(
            new PathSegmentSubcomponent.Fake(".", "."),
            new PathSegmentSubcomponent.Fake("segment:with:colon", "segment:with:colon")
          )
        )
      )
    );
  }

  private Iterable<?> encoded(final RelativePath path) {
    return StreamSupport.stream(path.spliterator(), false)
      .map(segment -> segment.encoded(StandardCharsets.UTF_8))
      .collect(Collectors.toUnmodifiableList());
  }

  @Test
  void testFirstSegmentEmpty() throws Throwable {
    assertAll(
      () -> assertIllegalZeroSegment(
        () -> new RelativePath(List.of(new PathSegmentSubcomponent.Fake("", ""))).asString()
      ),
      () -> assertIllegalZeroSegment(
        () -> new RelativePath(List.of(new PathSegmentSubcomponent.Fake("", ""))).encoded(StandardCharsets.UTF_8)
      ),
      () -> assertIllegalZeroSegment(
        () -> new RelativePath(List.of(new PathSegmentSubcomponent.Fake("", ""))).iterator().next().asString()
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
      ".///",
      new RelativePath(
        List.of(
          new PathSegmentSubcomponent.Fake(".", "."),
          new PathSegmentSubcomponent.Fake("", ""),
          new PathSegmentSubcomponent.Fake("", ""),
          new PathSegmentSubcomponent.Fake("", "")
        )
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testIfAbsoluteElse() throws Throwable {
    assertAll(
      () -> assertFalse(
        new RelativePath(
          List.of(
            new PathSegmentSubcomponent.Fake("", ""),
            new PathSegmentSubcomponent.Fake("", ""),
            new PathSegmentSubcomponent.Fake("", "")
          )
        ).<Boolean>ifAbsoluteElse(x -> true, y -> false)
      ),
      () -> assertFalse(new RelativePath().<Boolean>ifAbsoluteElse(x -> true, y -> false))
    );
  }
}
