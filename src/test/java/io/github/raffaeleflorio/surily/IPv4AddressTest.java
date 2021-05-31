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
  void testIllegalIPv4Address() throws Throwable {
    assertAll(
      () -> assertIllegalIPv4Octets(
        () -> new IPv4Address(256, 0, 0, 1).asString(),
        256, 0, 0, 1
      ),
      () -> assertIllegalIPv4Octets(
        () -> new IPv4Address(0, 0, 0, -1).encoded(StandardCharsets.US_ASCII),
        0, 0, 0, -1
      ),
      () -> assertIllegalIPv4Octets(
        () -> new IPv4Address(255, 255, 987, 255).encoded(StandardCharsets.UTF_8),
        255, 255, 987, 255
      ),
      () -> assertIllegalIPv4Octets(
        () -> new IPv4Address(0, 3_000, 0, 0).asString(),
        0, 3_000, 0, 0
      )
    );
  }

  private void assertIllegalIPv4Octets(final Executable executable, final Integer first, final Integer second, final Integer third, final Integer fourth) {
    assertEquals(
      String.format("Illegal IPv4 octets: <%s,%s,%s,%s>", first, second, third, fourth),
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }
}
