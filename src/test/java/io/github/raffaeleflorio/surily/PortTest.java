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

class PortTest {
  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "80",
      new Port(80).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "1234",
      new Port(1234).asString()
    );
  }

  @Test
  void testIllegalPorts() throws Throwable {
    assertAll(
      () -> assertIllegalPort(() -> new Port(-5678).asString(), "-5678"),
      () -> assertIllegalPort(() -> new Port(65537).encoded(StandardCharsets.US_ASCII), "65537"),
      () -> assertIllegalPort(() -> new Port("a").asString(), "a"),
      () -> assertIllegalPort(() -> new Port(" 80").asString(), " 80"),
      () -> assertIllegalPort(() -> new Port("443 ").asString(), "443 "),
      () -> assertIllegalPort(() -> new Port("1234567890").asString(), "1234567890"),
      () -> assertIllegalPort(() -> new Port("a123").asString(), "a123"),
      () -> assertIllegalPort(() -> new Port("123a").asString(), "123a"),
      () -> assertIllegalPort(() -> new Port("65536").asString(), "65536"),
      () -> assertIllegalPort(() -> new Port("9999999999999999").asString(), "9999999999999999")
    );
  }

  private void assertIllegalPort(final Executable executable, final CharSequence port) {
    assertEquals(
      String.format("Illegal port: <%s>", port),
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }

  @Test
  void testExceptionMaxLength() throws Throwable {
    assertIllegalPort(
      () -> new Port("A".repeat(4097)).encoded(StandardCharsets.UTF_16),
      "A".repeat(4096).concat("...")
    );
  }

  @Test
  void testEmptyPort() throws Throwable {
    assertAll(
      () -> assertEquals("", new Port().asString()),
      () -> assertEquals("", new Port().encoded(StandardCharsets.US_ASCII))
    );
  }
}
