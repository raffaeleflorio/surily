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

import static org.junit.jupiter.api.Assertions.*;

class SchemeTest {
  @Test
  void testLowerCaseEncoded() {
    assertEquals(
      "https",
      new Scheme("HTTPS").encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsString() {
    assertEquals(
      "HTTP",
      new Scheme("HTTP").asString()
    );
  }

  @Test
  void testDisallowedFirstCharacter() {
    assertAll(
      () -> assertIllegalScheme(() -> new Scheme("1abcd").encoded(StandardCharsets.US_ASCII), "1abcd"),
      () -> assertIllegalScheme(() -> new Scheme("+xyz").asString(), "+xyz"),
      () -> assertIllegalScheme(() -> new Scheme("-f").asString(), "-f"),
      () -> assertIllegalScheme(() -> new Scheme(".lf").encoded(StandardCharsets.UTF_8), ".lf")
    );
  }

  private void assertIllegalScheme(final Executable executable, final CharSequence scheme) {
    assertThrowsWithMessage(
      IllegalStateException.class,
      executable,
      String.format("Illegal scheme: <%s>", scheme)
    );
  }

  private void assertThrowsWithMessage(final Class<? extends Throwable> expectedException, final Executable executable, final String expectedMessage) {
    assertEquals(
      expectedMessage,
      assertThrows(expectedException, executable).getMessage()
    );
  }

  @Test
  void testEmptyScheme() {
    assertIllegalScheme(() -> new Scheme("").asString(), "");
  }

  @Test
  void testDisallowedCharacters() {
    assertAll(
      () -> assertIllegalScheme(() -> new Scheme("ht%ps").encoded(StandardCharsets.UTF_8), "ht%ps"),
      () -> assertIllegalScheme(() -> new Scheme("ft\b").encoded(StandardCharsets.ISO_8859_1), "ft\b"),
      () -> assertIllegalScheme(() -> new Scheme("sch\u00DCme").asString(), "sch\u00DCme"),
      () -> assertIllegalScheme(() -> new Scheme("ht?#").asString(), "ht?#")
    );
  }

  @Test
  void testExceptionMaxLength() {
    assertThrowsWithMessage(
      IllegalStateException.class,
      () -> new Scheme("1".repeat(4097)).asString(),
      String.format("Illegal scheme: <%s...>", "1".repeat(4096))
    );
  }

  @Test
  void testIfDefinedElse() {
    assertTrue(new Scheme("ssh").ifDefinedElse(x -> true, () -> false));
  }
}
