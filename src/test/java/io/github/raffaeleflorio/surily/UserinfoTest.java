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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserinfoTest {
  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "user%40%23%5B%5D:info",
      new Userinfo("user@#[]:info").encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "user[info]",
      new Userinfo("user[info]").asString()
    );
  }

  @Test
  void testUnreservedCharacters() throws Throwable {
    var unreserved = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~!$&'()*+,;=:";
    assertEquals(
      unreserved,
      new Userinfo(unreserved).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testEmptyUserinfo() throws Throwable {
    assertAll(
      () -> assertEquals("", new Userinfo("").encoded(StandardCharsets.UTF_8)),
      () -> assertEquals("", new Userinfo("").asString())
    );
  }
}
