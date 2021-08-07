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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UnionSetTest {
  @Test
  void testUnion() {
    assertSetEquals(
      Set.of(1, 2, 3, 9, 8, 7),
      new UnionSet<>(Set.of(1, 2, 3), Set.of(9, 8, 7))
    );
  }

  private void assertSetEquals(final Set<Integer> expected, final Set<Integer> actual) {
    assertAll(
      () -> assertEquals(expected.size(), actual.size()),
      () -> assertTrue(expected.containsAll(actual))
    );
  }

  @Test
  void testSize() {
    assertEquals(
      5,
      new UnionSet<>(Set.of(1, 2, 3), Set.of(42, 43)).size()
    );
  }

  @Test
  void testNoDuplicates() {
    assertAll(
      () -> assertEquals(
        5,
        new UnionSet<>(Set.of(1, 2, 3), Set.of(3, 42, 43)).size()
      ),
      () -> assertSetEquals(
        Set.of(1, 2, 3, 42, 43),
        new UnionSet<>(Set.of(1, 2, 3), Set.of(3, 42, 43))
      )
    );
  }
}
