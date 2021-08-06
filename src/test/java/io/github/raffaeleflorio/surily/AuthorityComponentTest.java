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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthorityComponentTest {
  @Nested
  class FakeTest {
    @Test
    void testEncoded() throws Throwable {
      var expected = "the encoded authority";
      assertEquals(
        expected,
        new AuthorityComponent.Fake(expected, "any").encoded(StandardCharsets.UTF_16BE)
      );
    }

    @Test
    void testAsString() throws Throwable {
      var expected = "the asString authority";
      assertEquals(expected, new AuthorityComponent.Fake("any", expected).asString());
    }

    @Test
    void testUserinfo() throws Throwable {
      var expected = new UserinfoSubComponent.Fake("e", "a");
      assertEquals(expected, new AuthorityComponent.Fake(expected).userinfo());
    }

    @Test
    void testHost() throws Throwable {
      var expected = new HostSubcomponent.Fake("encoded", "asString");
      assertEquals(expected, new AuthorityComponent.Fake(expected).host());
    }

    @Test
    void testPort() throws Throwable {
      var expected = new PortSubcomponent.Fake(234567890);
      assertEquals(expected, new AuthorityComponent.Fake(expected).port());
    }

    @Test
    void testIfDefinedElse() throws Throwable {
      assertEquals(
        "fake, but defined",
        new AuthorityComponent.Fake(
          "",
          "",
          new UserinfoSubComponent.Fake("", ""),
          new HostSubcomponent.Fake("", ""),
          new PortSubcomponent.Fake(1234)
        ).ifDefinedElse(x -> "fake, but defined", () -> "undefined")
      );
    }
  }
}
