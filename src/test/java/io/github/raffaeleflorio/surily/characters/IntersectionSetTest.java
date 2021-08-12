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

class IntersectionSetTest {
  @Test
  void testIterator() {
    assertSetEquals(
      Set.of("common", "another common element"),
      new IntersectionSet<>(
        Set.of("common", "uncommon", "another common element", "nope"),
        Set.of("not really common", "xyz", "common", "another common element")
      )
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
      1,
      new IntersectionSet<>(
        Set.of(1),
        Set.of(1, 2, 3)
      ).size()
    );
  }

  @Test
  void testEmptyIntersection() {
    assertEquals(0, new IntersectionSet<>(Set.of("the question"), Set.of()).size());
  }
}
