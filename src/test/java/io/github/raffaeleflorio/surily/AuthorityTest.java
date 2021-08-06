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

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AuthorityTest {
  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "userinfo@host:1234",
      new Authority(
        new UserinfoSubComponent.Fake("userinfo", "userinfo"),
        new HostSubcomponent.Fake("host", "host"),
        new PortSubcomponent.Fake(1234)
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testEncodedWithoutUserinfo() throws Throwable {
    assertEquals(
      "host:1234",
      new Authority(
        new HostSubcomponent.Fake("host", "host"),
        new PortSubcomponent.Fake(1234)
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testEncodedWithoutPort() throws Throwable {
    assertEquals(
      "userinfo@host",
      new Authority(
        new UserinfoSubComponent.Fake("userinfo", "userinfo"),
        new HostSubcomponent.Fake("host", "host")
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testEncodedWithOnlyHost() throws Throwable {
    assertEquals(
      "[::1]",
      new Authority(
        new HostSubcomponent.Fake("[::1]", "[::1]")
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "userinfo@host:54901",
      new Authority(
        new UserinfoSubComponent.Fake("userinfo", "userinfo"),
        new HostSubcomponent.Fake("host", "host"),
        new PortSubcomponent.Fake(54901)
      ).asString()
    );
  }

  @Test
  void testAsStringWithoutPort() throws Throwable {
    assertEquals(
      "userinfo@host",
      new Authority(
        new UserinfoSubComponent.Fake("userinfo", "userinfo"),
        new HostSubcomponent.Fake("host", "host")
      ).asString()
    );
  }


  @Test
  void testAsStringWithoutUserInfo() throws Throwable {
    assertEquals(
      "host:65432",
      new Authority(
        new HostSubcomponent.Fake("host", "host"),
        new PortSubcomponent.Fake(65432)
      ).asString()
    );
  }

  @Test
  void testAsStringWithOnlyHost() throws Throwable {
    assertEquals(
      "127.0.0.1",
      new Authority(
        new HostSubcomponent.Fake("127.0.0.1", "127.0.0.1")
      ).asString()
    );
  }

  @Test
  void testHost() throws Throwable {
    assertEquals(
      "example%20.com",
      new Authority(
        new HostSubcomponent.Fake("example%20.com", "example .com")
      ).host().encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testIfDefinedElse() throws Throwable {
    assertTrue(
      new Authority(
        new HostSubcomponent.Fake("any", "stuff")
      ).ifDefinedElse(x -> true, () -> false)
    );
  }

  @Test
  void testUndefinedComponents() throws Throwable {
    assertAll(
      () -> assertEquals(
        "undefined port",
        new Authority(new HostSubcomponent.Fake("any", "stuff"))
          .port()
          .ifDefinedElse(x -> "ops", () -> "undefined port")
      ),
      () -> assertEquals(
        "undefined userinfo",
        new Authority(new HostSubcomponent.Fake("any", "stuff"))
          .userinfo()
          .ifDefinedElse(x -> "ops", () -> "undefined userinfo")
      ),
      () -> assertEquals(
        "undefined port",
        new Authority(
          new UserinfoSubComponent.Fake("user", "info"),
          new HostSubcomponent.Fake("any", "stuff")
        ).port().ifDefinedElse(
          x -> "ops",
          () -> "undefined port"
        )
      ),
      () -> assertEquals(
        "undefined userinfo",
        new Authority(
          new HostSubcomponent.Fake("any", "stuff"),
          new PortSubcomponent.Fake(1234)
        ).userinfo().ifDefinedElse(
          x -> "ops",
          () -> "undefined userinfo"
        )
      )
    );
  }
}
