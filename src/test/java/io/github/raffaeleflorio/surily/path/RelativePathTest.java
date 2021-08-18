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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class RelativePathTest {
  @Test
  void testEncoded() {
    Assertions.assertEquals(
      "%2F/../../ok%5B.txt",
      new RelativePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("%2F", "/"),
          new PathSegmentSubcomponent.NormalFake("..", ".."),
          new PathSegmentSubcomponent.NormalFake("..", ".."),
          new PathSegmentSubcomponent.NormalFake("ok%5B.txt", "ok[.txt")
        )
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testFirstSegmentWithColon() {
    assertEquals(
      "x:y:z",
      new RelativePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("x:y:z", "")
        )
      ).encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testIterator() {
    assertIterableEquals(
      List.of(".", "segment:with:colon"),
      encoded(new RelativePath(
          List.of(
            new PathSegmentSubcomponent.NormalFake(".", "."),
            new PathSegmentSubcomponent.NormalFake("segment:with:colon", "segment:with:colon")
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
  void testFirstSegmentEmpty() {
    assertAll(
      () -> assertIllegalNonZeroSegment(
        () -> new RelativePath(List.of(new PathSegmentSubcomponent.NormalFake("", "")))
          .asString()
      ),
      () -> assertIllegalNonZeroSegment(
        () -> new RelativePath(List.of(new PathSegmentSubcomponent.NormalFake("", "")))
          .encoded(StandardCharsets.UTF_8)
      ),
      () -> assertIllegalNonZeroSegment(
        () -> new RelativePath(List.of(new PathSegmentSubcomponent.NormalFake("", "")))
          .iterator()
          .next()
          .asString()
      )
    );
  }

  private void assertIllegalNonZeroSegment(final Executable executable) {
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
      ".///",
      new RelativePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake(".", ""),
          new PathSegmentSubcomponent.NormalFake("", ""),
          new PathSegmentSubcomponent.NormalFake("", ""),
          new PathSegmentSubcomponent.NormalFake("", "")
        )
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testUnsupportedHierPartWithAuthority() {
    assertThrowsWithMessage(
      IllegalStateException.class,
      () -> new RelativePath(List.of()).hierPart(new AuthorityComponent.Fake("", "")),
      "Unable to build a hier-part with an authority"
    );
  }

  @Test
  void testUnsupportedRelativePartWithAuthority() {
    assertThrowsWithMessage(
      IllegalStateException.class,
      () -> new RelativePath(List.of()).relativePart(new AuthorityComponent.Fake("", "")),
      "Unable to build a relative-part with an authority"
    );
  }

  @Test
  void testRelativePartWithoutAuthority() {
    assertEquals(
      "relative/part",
      new RelativePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("relative", ""),
          new PathSegmentSubcomponent.NormalFake("part", "")
        )
      ).relativePart().encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testHierPartWithoutAuthority() {
    assertEquals(
      "./hier/part",
      new RelativePath(
        List.of(
          new PathSegmentSubcomponent.SingleDotFake("", "."),
          new PathSegmentSubcomponent.NormalFake("", "hier"),
          new PathSegmentSubcomponent.NormalFake("", "part")
        )
      ).hierPart().asString()
    );
  }

  @Test
  void testRelativePartWithColonSegment() {
    var illegal = "colon:not:allowed:here";
    assertIllegalNonColonSegment(
      illegal,
      () -> new RelativePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake(illegal, "")
        )
      ).relativePart().encoded(StandardCharsets.UTF_8)
    );
  }

  private void assertIllegalNonColonSegment(final String segment, final Executable executable) {
    assertThrowsWithMessage(
      IllegalStateException.class,
      executable,
      String.format("Illegal non-colon segment: <%s>", segment)
    );
  }

  @Test
  void testHierPartWithColonSegment() {
    var expected = "colon:allowed:in:this:context";
    assertEquals(
      expected,
      new RelativePath(
        List.of(
          new PathSegmentSubcomponent.NormalFake("", "colon:allowed:in:this:context")
        )
      ).hierPart().asString()
    );
  }

  @Test
  void testSegments() {
    assertEquals(
      "updated/relative/path",
      new RelativePath(List.of())
        .segments(
          List.of(
            new PathSegmentSubcomponent.NormalFake("updated", ""),
            new PathSegmentSubcomponent.NormalFake("relative", ""),
            new PathSegmentSubcomponent.NormalFake("path", "")
          )
        )
        .encoded(StandardCharsets.UTF_8)
    );
  }
}
