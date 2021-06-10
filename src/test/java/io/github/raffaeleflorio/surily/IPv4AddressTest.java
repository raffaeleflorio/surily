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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IPv4AddressTest {
  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "127.0.0.1",
      new IPv4Address(127, 0, 0, 1).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "255.255.255.0",
      new IPv4Address(255, 255, 255, 0).asString()
    );
  }

  @Test
  void testIllegalIPv4Addresses() throws Throwable {
    assertAll(
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address(256, 0, 0, 1).asString(),
        "256.0.0.1"
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address(0, 0, 0, -1).encoded(StandardCharsets.US_ASCII),
        "0.0.0.-1"
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address(255, 255, 987, 255).encoded(StandardCharsets.UTF_8),
        "255.255.987.255"
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address(0, 3_000, 0, 0).asString(),
        "0.3000.0.0"
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address("a.b.c.d").asString(),
        "a.b.c.d"
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address("").encoded(StandardCharsets.US_ASCII),
        ""
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address("any").asString(),
        "any"
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address("1.2.3.4.5").encoded(StandardCharsets.US_ASCII),
        "1.2.3.4.5"
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address("1.2.3.4.b").asString(),
        "1.2.3.4.b"
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address("1.2.3.4 ").asString(),
        "1.2.3.4 "
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address(" 1.2.3.4").encoded(StandardCharsets.US_ASCII),
        " 1.2.3.4"
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address("...").asString(),
        "..."
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address(".2.3.4").encoded(StandardCharsets.UTF_16),
        ".2.3.4"
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address("").asString(),
        ""
      ),
      () -> assertIllegalIPv4Address(
        () -> new IPv4Address(List.of()).encoded(StandardCharsets.UTF_16BE),
        ""
      )
    );
  }

  private void assertIllegalIPv4Address(final Executable executable, final String address) {
    assertEquals(
      String.format("Illegal IPv4 address: <%s>", address),
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }

  @Test
  void testExceptionMaxLength() throws Throwable {
    assertIllegalIPv4Address(
      () -> new IPv4Address("A".repeat(4097)).asString(),
      "A".repeat(4096).concat("...")
    );
  }

  @Test
  void testIfDefinedElse() throws Throwable {
    assertTrue(new IPv4Address("31.32.33.255").ifDefinedElse(x -> true, () -> false));
  }
}
