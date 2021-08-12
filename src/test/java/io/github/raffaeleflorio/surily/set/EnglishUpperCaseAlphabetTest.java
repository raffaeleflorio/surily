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
package io.github.raffaeleflorio.surily.set;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnglishUpperCaseAlphabetTest {
  @Test
  void testIterator() {
    assertSetEquals(
      Set.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'),
      new EnglishUpperCaseAlphabet()
    );
  }

  private void assertSetEquals(final Set<Character> expected, final Set<Character> actual) {
    assertAll(
      () -> assertEquals(expected.size(), actual.size()),
      () -> assertTrue(expected.containsAll(actual))
    );
  }

  @Test
  void testSize() {
    assertEquals(
      26,
      new EnglishUpperCaseAlphabet().size()
    );
  }

  @Nested
  class ImmutabilityTest {
    @Test
    void testAdd() {
      assertThrows(
        UnsupportedOperationException.class,
        () -> new EnglishUpperCaseAlphabet().add('~')
      );
    }

    @Test
    void testRemove() {
      assertThrows(
        UnsupportedOperationException.class,
        () -> new EnglishUpperCaseAlphabet().remove('A')
      );
    }
  }
}
