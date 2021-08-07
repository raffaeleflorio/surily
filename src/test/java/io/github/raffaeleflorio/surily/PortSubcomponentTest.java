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

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PortSubcomponentTest {
  @Nested
  class FakeTest {
    @Test
    void testEncoded() {
      assertEquals(
        "1234",
        new PortSubcomponent.Fake(1234).encoded(StandardCharsets.UTF_16BE)
      );
    }

    @Test
    void testAsString() {
      assertEquals(
        "-42",
        new PortSubcomponent.Fake(-42).asString()
      );
    }

    @Test
    void testIfDefinedElse() {
      assertTrue(
        new PortSubcomponent.Fake(123).ifDefinedElse(x -> true, () -> false)
      );
    }
  }
}
