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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnglishLowerCaseAlphabetTest {
  @Test
  void testIterator() throws Throwable {
    assertIterableEquals(
      Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'),
      new EnglishLowerCaseAlphabet()
    );
  }

  @Test
  void testSize() throws Throwable {
    assertEquals(
      26,
      new EnglishLowerCaseAlphabet().size()
    );
  }


  @Nested
  class ImmutabilityTest {
    @Test
    void testAdd() throws Throwable {
      assertThrows(
        UnsupportedOperationException.class,
        () -> new EnglishLowerCaseAlphabet().add('~')
      );
    }

    @Test
    void testRemove() throws Throwable {
      assertThrows(
        UnsupportedOperationException.class,
        () -> new EnglishLowerCaseAlphabet().remove('z')
      );
    }
  }
}
