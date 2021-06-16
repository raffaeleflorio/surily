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

class UndefinedQueryTest {
  @Test
  void testIfDefinedElse() throws Throwable {
    assertEquals(
      "this is undefined",
      new UndefinedQuery().ifDefinedElse(x -> "ops...", () -> "this is undefined")
    );
  }

  @Test
  void testRepresentationsException() throws Throwable {
    assertAll(
      () -> assertUndefinedQueryException(() -> new UndefinedQuery().encoded(StandardCharsets.UTF_8)),
      () -> assertUndefinedQueryException(() -> new UndefinedQuery().asString())
    );
  }

  private void assertUndefinedQueryException(final Executable executable) {
    assertEquals(
      "No representations for an undefined query",
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }
}
