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

import io.github.raffaeleflorio.surily.UriComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class NonZeroPathSegmentTest {
  @Test
  void testEncoded() {
    assertIllegalNonZeroSegment(() -> new NonZeroPathSegment("").encoded(StandardCharsets.US_ASCII));
  }

  private void assertIllegalNonZeroSegment(final Executable executable) {
    assertEquals(
      "Illegal non-zero segment",
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }

  @Test
  void testAsString() {
    assertIllegalNonZeroSegment(() -> new NonZeroPathSegment("").asString());
  }

  @Test
  void testWithNonZeroSegment() {
    assertAll(
      () -> assertDoesNotThrow(() -> new NonZeroPathSegment("ok")).encoded(StandardCharsets.UTF_16BE),
      () -> assertDoesNotThrow(() -> new NonZeroPathSegment(" ")).asString()
    );
  }

  @Test
  void testIfDotElse() {
    assertAll(
      () -> assertTrue(new NonZeroPathSegment(".").<Boolean>ifDotElse(x -> true, y -> false, z -> false)),
      () -> assertTrue(new NonZeroPathSegment("..").<Boolean>ifDotElse(x -> false, y -> true, z -> false)),
      () -> assertTrue(new NonZeroPathSegment("any").<Boolean>ifDotElse(x -> false, y -> false, z -> true))
    );
  }

  @Test
  void testIfDotElseNonZeroConstraintPreserved() {
    assertIllegalNonZeroSegment(() -> new NonZeroPathSegment("").ifDotElse(x -> "", x -> "", UriComponent::asString));
  }
}
