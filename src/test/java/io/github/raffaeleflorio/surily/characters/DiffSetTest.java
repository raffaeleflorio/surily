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
package io.github.raffaeleflorio.surily.characters;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DiffSetTest {
  @Test
  void testDifference() {
    assertSetEquals(
      Set.of("42"),
      new DiffSet<>(Set.of("A", "B", "C", "D", "42"), Set.of("A", "B", "C", "D"))
    );
  }

  private void assertSetEquals(final Set<String> expected, final Set<String> actual) {
    assertAll(
      () -> assertEquals(expected.size(), actual.size()),
      () -> assertTrue(expected.containsAll(actual))
    );
  }

  @Test
  void testSize() {
    assertEquals(
      2,
      new DiffSet<>(Set.of("a string", "a number", "another string"), Set.of("a number")).size()
    );
  }
}
