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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PathSegmentTest {
  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "file%3F.png",
      new PathSegment("file?.png").encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "/actually/a/single/segment",
      new PathSegment("/actually/a/single/segment").asString()
    );
  }

  @Test
  void testEmptySegment() throws Throwable {
    assertAll(
      () -> assertEquals("", new PathSegment().encoded(StandardCharsets.UTF_8)),
      () -> assertEquals("", new PathSegment().asString())
    );
  }

  @Test
  void testColonSegment() throws Throwable {
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
  void testUnreservedCharacters() throws Throwable {
    var unreserved = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~!$&'()*+,;=:@";
    assertEquals(
      unreserved,
      new PathSegment(unreserved).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testIfDotElse() throws Throwable {
    assertAll(
      () -> assertEquals(
        "double",
        new PathSegment("..").ifDotElse(x -> "double", y -> "nope")
      ), () -> assertEquals(
        "single",
        new PathSegment(".").ifDotElse(x -> "single", y -> "nope")
      ),
      () -> assertEquals(
        "normal segment",
        new PathSegment("...").ifDotElse(x -> "triple", y -> "normal segment")
      )
    );
  }
}
