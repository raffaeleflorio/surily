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
import org.junit.jupiter.api.function.Executable;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class NonColonPathSegmentTest {
  @Test
  void testIllegalSegmentEncoded() {
    var illegal = "segment:with:colon";
    assertIllegalNonColonSegment(
      illegal,
      () -> new NonColonPathSegment(illegal).encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testEncoded() {
    var expected = "ok";
    assertEquals(
      expected,
      new NonColonPathSegment(expected).encoded(StandardCharsets.US_ASCII)
    );
  }

  private void assertIllegalNonColonSegment(final String illegal, final Executable executable) {
    assertEquals(
      String.format("Illegal non-colon segment: <%s>", illegal),
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }

  @Test
  void testIllegalSegmentAsString() {
    var illegal = ":::";
    assertIllegalNonColonSegment(
      illegal,
      () -> new NonColonPathSegment(illegal).asString()
    );
  }

  @Test
  void testAsString() {
    var expected = "ok";
    assertEquals(
      expected,
      new NonColonPathSegment(expected).asString()
    );
  }

  @Test
  void testIfDotElse() {
    assertAll(
      () -> assertTrue(new NonColonPathSegment(".").<Boolean>ifDotElse(x -> true, y -> false, z -> false)),
      () -> assertTrue(new NonColonPathSegment("..").<Boolean>ifDotElse(x -> false, y -> true, z -> false)),
      () -> assertTrue(new NonColonPathSegment("./").<Boolean>ifDotElse(x -> false, y -> false, z -> true))
    );
  }

  @Test
  void testIfDotElseNonColonPreservation() {
    var illegal = ":";
    assertIllegalNonColonSegment(
      illegal,
      () -> new NonColonPathSegment(illegal).ifDotElse(x -> "", x -> "", x -> x.encoded(StandardCharsets.ISO_8859_1))
    );
  }

  @Test
  void testMaxLengthExceptionMessage() {
    assertIllegalNonColonSegment(
      ":".repeat(4096).concat("..."),
      () -> new NonColonPathSegment(":".repeat(4097)).asString()
    );
  }
}
