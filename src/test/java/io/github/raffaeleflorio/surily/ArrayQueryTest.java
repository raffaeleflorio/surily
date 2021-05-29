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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayQueryTest {
  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "key=value&key=value1&key=another%20one",
      new ArrayQuery("key", List.of("value", "value1", "another one")).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "key[]=3&key[]=2",
      new ArrayQuery("key[]", List.of("3", "2")).asString()
    );
  }

  @Test
  void testWithEmptyValues() throws Throwable {
    assertEquals(
      "",
      new ArrayQuery("name", List.of()).encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testCustomKeyValueDelimiter() throws Throwable {
    assertEquals(
      "name~1&name~2",
      new ArrayQuery("name", List.of("1", "2"), '~').encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testCustomPairsDelimiter() throws Throwable {
    assertEquals(
      "name=1;name=2",
      new ArrayQuery("name", List.of("1", "2"), '=', ';').encoded(StandardCharsets.ISO_8859_1)
    );
  }
}
