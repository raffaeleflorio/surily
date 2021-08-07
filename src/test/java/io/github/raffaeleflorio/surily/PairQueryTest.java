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

class PairQueryTest {
  @Test
  void testUnreservedKeyCharacters() {
    var unreserved = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~!$&'()*+,;:@";
    assertEquals(
      unreserved.concat("=any"),
      new PairQuery(unreserved, "any").encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testUnreservedValueCharacters() {
    var unreserved = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~!$&'()*+,;=:@";
    assertEquals(
      "any=".concat(unreserved),
      new PairQuery("any", unreserved).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testReservedKeyCharacters() {
    assertEquals(
      "%23%5B%5D%3D=any",
      new PairQuery("#[]=", "any").encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testReservedValueCharacters() {
    assertEquals(
      "any=%23%5B%5D",
      new PairQuery("any", "#[]").encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testDefaultDelimiter() {
    assertEquals(
      "%3D%3D====",
      new PairQuery("==", "===").encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testDelimiterFromUnreservedCharactersSet() {
    assertEquals(
      "the%20key%20with%20%40@the%20value%20with%20@",
      new PairQuery("the key with @", "the value with @", '@').encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsString() {
    assertEquals(
      "key?=value with multiple\nwhitespaces",
      new PairQuery("key?", "value with multiple\nwhitespaces").asString()
    );
  }

  @Test
  void testDisallowedDelimiters() {
    assertAll(
      () -> assertThrowsWithMessage(
        IllegalStateException.class,
        () -> new PairQuery("key", "value", '#').encoded(StandardCharsets.UTF_8),
        illegalDelimiter('#')
      ),
      () -> assertThrowsWithMessage(
        IllegalStateException.class,
        () -> new PairQuery("key", "value", '\u219D').encoded(StandardCharsets.UTF_8),
        illegalDelimiter('\u219D')
      ),
      () -> assertThrowsWithMessage(
        IllegalStateException.class,
        () -> new PairQuery("key", "value", '%').asString(),
        illegalDelimiter('%')
      )
    );
  }

  private String illegalDelimiter(final Character delimiter) {
    return String.format("Illegal delimiter: <%s>", delimiter);
  }

  private void assertThrowsWithMessage(final Class<? extends Throwable> expectedException, final Executable executable, final String expectedMessage) {
    assertEquals(
      expectedMessage,
      assertThrows(expectedException, executable).getMessage()
    );
  }

  @Test
  void testEmptyKeyValue() {
    assertEquals(
      "=",
      new PairQuery("", "").encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testIfDefinedElse() {
    assertTrue(new PairQuery("k", "v").ifDefinedElse(x -> true, () -> false));
  }
}
