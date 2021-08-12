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
package io.github.raffaeleflorio.surily.path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DotSegmentTest {
  @Test
  void testEncoded() {
    Assertions.assertEquals(".", new DotSegment().encoded(StandardCharsets.UTF_8));
  }

  @Test
  void testAsString() {
    assertEquals(".", new DotSegment().asString());
  }

  @Test
  void testIfDotElse() {
    assertTrue(new DotSegment().<Boolean>ifDotElse(x -> true, y -> false, z -> false));
  }
}
