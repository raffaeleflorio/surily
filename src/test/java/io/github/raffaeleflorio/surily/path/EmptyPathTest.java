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

import io.github.raffaeleflorio.surily.authority.AuthorityComponent;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class EmptyPathTest {
  @Test
  void testEncoded() {
    assertEquals(0, new EmptyPath().encoded(StandardCharsets.US_ASCII).length());
  }

  @Test
  void testAsString() {
    assertTrue(new EmptyPath().asString().isEmpty());
  }

  @Test
  void testIterator() {
    assertFalse(new EmptyPath().iterator().hasNext());
  }

  @Test
  void testRelativePartWithoutAuthority() {
    assertTrue(new EmptyPath().relativePart().asString().isEmpty());
  }

  @Test
  void testRelativePartWithAuthority() {
    assertEquals(
      "//authority",
      new EmptyPath()
        .relativePart(new AuthorityComponent.Fake("authority", ""))
        .encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testHierPartPartWithoutAuthority() {
    assertEquals(0, new EmptyPath().hierPart().encoded(StandardCharsets.UTF_8).length());
  }

  @Test
  void testHierPartWithAuthority() {
    assertEquals(
      "//authority",
      new EmptyPath()
        .hierPart(new AuthorityComponent.Fake("", "authority"))
        .asString()
    );
  }
}
