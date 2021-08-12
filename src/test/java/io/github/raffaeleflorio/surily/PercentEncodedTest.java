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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class PercentEncodedTest {
  @Test
  void testReservedRFC3986Characters() {
    Assertions.assertEquals(
      "%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2A%2B%2C%3B%3D",
      new PercentEncoded(":/?#[]@!$&'()*+,;=").toString()
    );
  }

  @Test
  void testUnreservedRFC3986Characters() {
    var unreserved = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~";
    assertEquals(
      unreserved,
      new PercentEncoded(unreserved).toString()
    );
  }

  @Test
  void testNonPrintableASCIICharacters() {
    assertEquals(
      "%00",
      new PercentEncoded("\u0000").toString()
    );
  }

  @Test
  void testASCIIWhitespaces() {
    assertEquals(
      "%08%09%0A%0C%0D%20",
      new PercentEncoded("\b\t\n\f\r ").toString()
    );
  }

  @Test
  void testPercent() {
    assertEquals(
      "%25%25",
      new PercentEncoded("%%").toString()
    );
  }

  @Test
  void testLength() {
    assertEquals(
      6,
      new PercentEncoded("è", StandardCharsets.UTF_8).length()
    );
  }

  @Test
  void testSubsequence() {
    assertEquals(
      "A8",
      new PercentEncoded("\u00e8\00e9\u00f7", StandardCharsets.UTF_8).subSequence(4, 6).toString()
    );
  }

  @Test
  void testCharAtWithISO_8859_1() {
    assertEquals(
      'D',
      new PercentEncoded("\u00DC", StandardCharsets.ISO_8859_1).charAt(1)
    );
  }

  @Test
  void testSurrogatePair() {
    assertEquals(
      "%F0%90%B9%AD",
      new PercentEncoded("\uD803\uDE6D").toString()
    );
  }

  @Test
  void testASCIIEncodingWithSurrogatePair() {
    assertEquals(
      "%3F",
      new PercentEncoded("\uD83C\uDF09", StandardCharsets.US_ASCII).toString()
    );
  }

  @Test
  void testChars() {
    assertIterableEquals(
      List.of(0x25, 0x43, 0x33, 0x25, 0x42, 0x38),
      new PercentEncoded("\u00F8", StandardCharsets.UTF_8).chars().boxed().collect(Collectors.toUnmodifiableList())
    );
  }

  @Test
  void testCodePoints() {
    assertIterableEquals(
      List.of(0x25, 0x43, 0x33, 0x25, 0x39, 0x37),
      new PercentEncoded("\u00D7", StandardCharsets.UTF_8).codePoints().boxed().collect(Collectors.toUnmodifiableList())
    );
  }

  @Test
  void testPercentAlwaysEncoded() {
    assertEquals(
      "%25",
      new PercentEncoded("%", StandardCharsets.ISO_8859_1, Set.of('%')).toString()
    );
  }

  @Test
  void testWhitespacesAlwaysEncoded() {
    assertEquals(
      "%08%09%0A%0C%0D%20",
      new PercentEncoded("\b\t\n\f\r ", StandardCharsets.US_ASCII, Set.of(' ', '\b', '\t', '\n', '\f', '\r')).toString()
    );
  }
}
