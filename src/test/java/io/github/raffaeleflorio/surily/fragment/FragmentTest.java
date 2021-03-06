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
package io.github.raffaeleflorio.surily.fragment;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FragmentTest {
  @Test
  void testUnreservedCharacters() {
    var unreserved = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~!$&'()*+,;=:@";
    assertEquals(
      unreserved,
      new Fragment(unreserved).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testReservedCharacters() {
    assertEquals(
      "%23%5B%5D",
      new Fragment("#[]").encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsString() {
    var expected = "the unencoded fragment #[]";
    assertEquals(
      expected,
      new Fragment(expected).asString()
    );
  }

  @Test
  void testEmptyFragment() {
    assertEquals("", new Fragment().encoded(StandardCharsets.ISO_8859_1));
  }

  @Test
  void testIfDefinedElse() {
    assertTrue(new Fragment().ifDefinedElse(x -> true, () -> false));
  }
}
