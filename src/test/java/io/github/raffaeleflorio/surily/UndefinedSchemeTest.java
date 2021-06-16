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
import org.junit.jupiter.api.function.Executable;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class UndefinedSchemeTest {
  @Test
  void testIfDefinedElse() throws Throwable {
    assertEquals(
      "this is undefined",
      new UndefinedScheme().ifDefinedElse(x -> "", () -> "this is undefined")
    );
  }

  @Test
  void testRepresentationsException() throws Throwable {
    assertAll(
      () -> assertUndefinedSchemeException(() -> new UndefinedScheme().encoded(StandardCharsets.UTF_8)),
      () -> assertUndefinedSchemeException(() -> new UndefinedScheme().asString())
    );
  }

  private void assertUndefinedSchemeException(final Executable executable) {
    assertEquals(
      "No representations for an undefined scheme",
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }
}
