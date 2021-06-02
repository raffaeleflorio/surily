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

class IPv6AddressTest {
  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "[::1]",
      new IPv6Address("::1").encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "[2001:0db8:0000:0000:0000:ff00:0042:8329]",
      new IPv6Address("2001:0db8:0000:0000:0000:ff00:0042:8329").asString()
    );
  }

  @Test
  void testIPv4Mapped() throws Throwable {
    assertEquals(
      "[::ffff:127.0.0.1]",
      new IPv6Address("::ffff:127.0.0.1").encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testLegalAddresses() throws Throwable {
    assertAll(
      () -> assertDoesNotThrow(() -> new IPv6Address("a::b").encoded(StandardCharsets.UTF_8)),
      () -> assertDoesNotThrow(() -> new IPv6Address("AAAA:BBBB:C::0.0.0.0").asString()),
      () -> assertDoesNotThrow(() -> new IPv6Address("F::B").encoded(StandardCharsets.UTF_8)),
      () -> assertDoesNotThrow(() -> new IPv6Address("::").asString()),
      () -> assertDoesNotThrow(() -> new IPv6Address("::127.0.0.1").encoded(StandardCharsets.US_ASCII)),
      () -> assertDoesNotThrow(() -> new IPv6Address("CafE::255.255.255.255").asString()),
      () -> assertDoesNotThrow(() -> new IPv6Address("CafE:F00d::255.255.255.255").asString()),
      () -> assertDoesNotThrow(() -> new IPv6Address("::F00d:Ffff").asString()),
      () -> assertDoesNotThrow(() -> new IPv6Address("::0:0:0").encoded(StandardCharsets.US_ASCII)),
      () -> assertDoesNotThrow(() -> new IPv6Address("::0:0:0:0").encoded(StandardCharsets.UTF_16)),
      () -> assertDoesNotThrow(() -> new IPv6Address("::0:0:0:0:0").asString()),
      () -> assertDoesNotThrow(() -> new IPv6Address("::0:0:0:0:0:0").asString()),
      () -> assertDoesNotThrow(() -> new IPv6Address("::0:0:0:0:0:0:0").encoded(StandardCharsets.ISO_8859_1)),
      () -> assertDoesNotThrow(() -> new IPv6Address("::0000:0:A:f:0:200.0.0.1").asString()),
      () -> assertDoesNotThrow(() -> new IPv6Address(List.of("A", "B", "C", "D", "E", "F", "0", "1")).asString()),
      () -> assertDoesNotThrow(() -> new IPv6Address(List.of("::0:1:2:3:4:10.20.30.40")).encoded(StandardCharsets.US_ASCII))
    );
  }

  @Test
  void testIllegalIPv6Address() throws Throwable {
    assertAll(
      () -> assertIllegalAddress(
        () -> new IPv6Address(":").asString(),
        ":"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address("a").encoded(StandardCharsets.UTF_16),
        "a"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address("0.0.0.0").asString(),
        "0.0.0.0"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address("a:a:a:a:a:a:").encoded(StandardCharsets.ISO_8859_1),
        "a:a:a:a:a:a:"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address("a:a:a:a:a:a:abcde").encoded(StandardCharsets.US_ASCII),
        "a:a:a:a:a:a:abcde"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address("a:a:a:a:a:a:z").asString(),
        "a:a:a:a:a:a:z"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address("a::b::c").encoded(StandardCharsets.UTF_8),
        "a::b::c"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address("::0:0:0:0:0:0:0:0").asString(),
        "::0:0:0:0:0:0:0:0"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address("::0000:0:0:A:f:0:200.0.0.1").encoded(StandardCharsets.US_ASCII),
        "::0000:0:0:A:f:0:200.0.0.1"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address("[::1]").asString(),
        "[::1]"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address(List.of("A", "B", "C", "D", "E", "F", "0", "z")).encoded(StandardCharsets.UTF_8),
        "A:B:C:D:E:F:0:z"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address(List.of("A", "B", "C", "D", "E", "F", "0", "30.31.32.43")).asString(),
        "A:B:C:D:E:F:0:30.31.32.43"
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address("").encoded(StandardCharsets.US_ASCII),
        ""
      ),
      () -> assertIllegalAddress(
        () -> new IPv6Address(List.of()).asString(),
        ""
      )
    );
  }

  private void assertIllegalAddress(final Executable executable, final String address) {
    assertEquals(
      String.format("Illegal IPv6 address: <%s>", address),
      assertThrows(IllegalStateException.class, executable).getMessage(
      )
    );
  }

  @Test
  void textExceptionMaxLength() throws Throwable {
    assertIllegalAddress(
      () -> new IPv6Address("A".repeat(4097)).asString(),
      "A".repeat(4096).concat("...")
    );
  }
}
