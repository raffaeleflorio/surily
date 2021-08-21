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

import io.github.raffaeleflorio.surily.authority.AuthorityComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class AbsolutePathTest {
  @Test
  void testEncoded() {
    assertEquals(
      "/%2F/%2F/S%20%23%CF%80.xml",
      new AbsolutePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("%2F", "/"),
          new PathSegmentSubcomponent.NormalFake("%2F", "/"),
          new PathSegmentSubcomponent.NormalFake("S%20%23%CF%80.xml", "S #\u03C0.xml")
        )
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testRootPath() {
    assertEquals("/", new AbsolutePath().encoded(StandardCharsets.US_ASCII));
  }

  @Test
  void testAsString() {
    assertEquals(
      "/segment with ? \u03BB reserved characters[].json",
      new AbsolutePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("encoded representation", "segment with ? \u03BB reserved characters[].json")
        )
      ).asString()
    );
  }

  @Test
  void testIterator() {
    assertIterableEquals(
      List.of("segment 0", "segment 1", "segment 2"),
      asString(
        new AbsolutePath(
          List.of(
            new PathSegmentSubcomponent.NormalFake("encoded", "segment 0"),
            new PathSegmentSubcomponent.NormalFake("encoded", "segment 1"),
            new PathSegmentSubcomponent.NormalFake("encoded", "segment 2")
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
  void testZeroSegmentAsFirstSegment() {
    assertAll(
      () -> assertIllegalZeroSegment(
        () -> new AbsolutePath(List.of(new PathSegmentSubcomponent.NormalFake("", ""))).asString()
      ),
      () -> assertIllegalZeroSegment(
        () -> new AbsolutePath(List.of(new PathSegmentSubcomponent.NormalFake("", ""))).encoded(StandardCharsets.UTF_8)
      ),
      () -> assertIllegalZeroSegment(
        () -> new AbsolutePath(List.of(new PathSegmentSubcomponent.NormalFake("", ""))).iterator().next().asString()
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
  void testZeroSegmentsExceptFirstOne() {
    assertEquals(
      "/first//",
      new AbsolutePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("first", "first"),
          new PathSegmentSubcomponent.NormalFake("", ""),
          new PathSegmentSubcomponent.NormalFake("", "")
        )
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testRelativePartWithoutAuthority() {
    assertEquals(
      "/absolute/path",
      new AbsolutePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("absolute", ""),
          new PathSegmentSubcomponent.NormalFake("path", "")
        )
      ).relativePart().encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testHierPartWithoutAuthority() {
    assertEquals(
      "/absolute/path",
      new AbsolutePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("", "absolute"),
          new PathSegmentSubcomponent.NormalFake("", "path")
        )
      ).hierPart().asString()
    );
  }

  @Test
  void testRelativePartWithAuthority() {
    assertEquals(
      "//authority/absolute/path",
      new AbsolutePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("absolute", ""),
          new PathSegmentSubcomponent.NormalFake("path", "")
        )
      ).relativePart(
        new AuthorityComponent.Fake("authority", "")
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testHierPartWithAuthority() {
    assertEquals(
      "//authority/absolute/path",
      new AbsolutePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("", "absolute"),
          new PathSegmentSubcomponent.NormalFake("", "path")
        )
      ).hierPart(
        new AuthorityComponent.Fake("", "authority")
      ).asString()
    );
  }

  @Test
  void testSegments() {
    assertEquals(
      "/updated/absolute/path",
      new AbsolutePath()
        .segments(
          List.of(
            new PathSegmentSubcomponent.NormalFake("", "updated"),
            new PathSegmentSubcomponent.NormalFake("", "absolute"),
            new PathSegmentSubcomponent.NormalFake("", "path")
          )
        ).asString()
    );
  }

  @Test
  void testAbsoluteEmptyRepresentations() {
    assertAll(
      () -> assertEquals("/", new AbsolutePath().asString()),
      () -> assertEquals("/", new AbsolutePath().encoded(StandardCharsets.UTF_8))
    );
  }

  @Test
  void testIfEmptyElseWithAbsoluteEmpty() {
    assertFalse(new AbsolutePath().<Boolean>ifEmptyElse(x -> true, y -> false));
  }

  @Test
  void testIfEmptyElse() {
    assertFalse(
      new AbsolutePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("", "")
        )
      ).<Boolean>ifEmptyElse(x -> true, y -> false)
    );
  }

  @Test
  void testIfAbsoluteElse() {
    assertTrue(new AbsolutePath().<Boolean>ifAbsoluteElse(x -> true, x -> false));
  }
}
