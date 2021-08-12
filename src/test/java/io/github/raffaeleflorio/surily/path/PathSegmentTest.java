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

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PathSegmentTest {
  @Test
  void testEncoded() {
    assertEquals(
      "file%3F.png",
      new PathSegment("file?.png").encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testAsString() {
    assertEquals(
      "/actually/a/single/segment",
      new PathSegment("/actually/a/single/segment").asString()
    );
  }

  @Test
  void testEmptySegment() {
    assertAll(
      () -> assertEquals("", new PathSegment().encoded(StandardCharsets.UTF_8)),
      () -> assertEquals("", new PathSegment().asString())
    );
  }

  @Test
  void testColonSegment() {
    assertAll(
      () -> assertEquals(
        "segment:with:colon",
        new PathSegment("segment:with:colon").encoded(StandardCharsets.UTF_8)
      ),
      () -> assertEquals(
        "segment:with:colon",
        new PathSegment("segment:with:colon").asString()
      )
    );
  }

  @Test
  void testUnreservedCharacters() {
    var unreserved = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~!$&'()*+,;=:@";
    assertEquals(
      unreserved,
      new PathSegment(unreserved).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testIfDotElse() {
    assertAll(
      () -> assertEquals(
        "double",
        new PathSegment("..").ifDotElse(x -> "single", y -> "double", z -> "normal")
      ), () -> assertEquals(
        "single",
        new PathSegment(".").ifDotElse(x -> "single", y -> "double", z -> "normal")
      ),
      () -> assertEquals(
        "normal segment",
        new PathSegment("...").ifDotElse(x -> "single", y -> "double", z -> "normal segment")
      )
    );
  }
}
