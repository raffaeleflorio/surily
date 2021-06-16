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

class UndefinedAuthorityTest {
  @Test
  void testIfDefinedElse() throws Throwable {
    assertTrue(new UndefinedAuthority().ifDefinedElse(x -> false, () -> true));
  }

  @Test
  void testRepresentationsException() throws Throwable {
    assertAll(
      () -> assertUndefinedAuthorityException(() -> new UndefinedAuthority().asString()),
      () -> assertUndefinedAuthorityException(() -> new UndefinedAuthority().encoded(StandardCharsets.UTF_16LE))
    );
  }

  private void assertUndefinedAuthorityException(final Executable executable) {
    assertEquals(
      "No representations for an undefined authority",
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }

  @Test
  void testUserinfo() throws Throwable {
    assertEquals(
      "userinfo is undefined",
      new UndefinedAuthority().userinfo().ifDefinedElse(x -> "not really...", () -> "userinfo is undefined")
    );
  }

  @Test
  void testHost() throws Throwable {
    assertEquals(
      "host is undefined",
      new UndefinedAuthority().host().ifDefinedElse(x -> "ops", () -> "host is undefined")
    );
  }

  @Test
  void testPort() throws Throwable {
    assertEquals(
      "port is undefined",
      new UndefinedAuthority().host().ifDefinedElse(x -> "42", () -> "port is undefined")
    );
  }
}
