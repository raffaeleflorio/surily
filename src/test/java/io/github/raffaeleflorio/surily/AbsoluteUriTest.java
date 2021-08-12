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

import io.github.raffaeleflorio.surily.authority.AuthorityComponent;
import io.github.raffaeleflorio.surily.authority.HostSubcomponent;
import io.github.raffaeleflorio.surily.authority.PortSubcomponent;
import io.github.raffaeleflorio.surily.authority.UserinfoSubComponent;
import io.github.raffaeleflorio.surily.path.PathComponent;
import io.github.raffaeleflorio.surily.query.QueryComponent;
import io.github.raffaeleflorio.surily.scheme.SchemeComponent;
import io.github.raffaeleflorio.surily.scheme.UndefinedScheme;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AbsoluteUriTest {
  @Test
  void testUndefinedFragment() {
    assertEquals(
      "the fragment is always undefined",
      new AbsoluteUri(
        new SchemeComponent.Fake("any", "value"),
        new AuthorityComponent.Fake("any", "value"),
        new PathComponent.Fake("any", "value"),
        new QueryComponent.Fake("any", "value")
      ).fragment().ifDefinedElse(
        x -> "...",
        () -> "the fragment is always undefined"
      )
    );
  }

  @Test
  void testUndefinedQuery() {
    assertAll(
      () -> assertTrue(
        new AbsoluteUri(
          new SchemeComponent.Fake("http", "http"),
          new PathComponent.Fake("any", "path")
        ).query().ifDefinedElse(
          x -> false,
          () -> true
        )
      ),
      () -> assertTrue(
        new AbsoluteUri(
          new SchemeComponent.Fake("any", "value"),
          new AuthorityComponent.Fake("any", "value")
        ).query().ifDefinedElse(
          x -> false,
          () -> true
        )
      ),
      () -> assertTrue(
        new AbsoluteUri(
          new SchemeComponent.Fake("any", "value"),
          new AuthorityComponent.Fake("any", "value"),
          new PathComponent.Fake("any", "value")
        ).query().ifDefinedElse(
          x -> false,
          () -> true
        )
      ),
      () -> assertEquals(
        "http:",
        new AbsoluteUri(
          new SchemeComponent.Fake("", "http"),
          new PathComponent.Fake(
            new UriComponent.Fake("", ""),
            new UriComponent.Fake("", "")
          )
        ).asString()
      ),
      () -> assertEquals(
        "http:a/path",
        new AbsoluteUri(
          new SchemeComponent.Fake("http", ""),
          new PathComponent.Fake(
            new UriComponent.Fake("", ""),
            new UriComponent.Fake("a/path", "")
          )
        ).encoded(StandardCharsets.US_ASCII)
      )
    );
  }

  @Test
  void testEmptyPath() {
    {
      assertAll(
        () -> assertTrue(
          new AbsoluteUri(
            new SchemeComponent.Fake("any", "value"),
            new AuthorityComponent.Fake("any", "value")
          ).path().asString().isEmpty()
        ),
        () -> assertEquals(
          0,
          new AbsoluteUri(
            new SchemeComponent.Fake("any", "value"),
            new AuthorityComponent.Fake("123", "456"),
            new QueryComponent.Fake("a query", "string")
          ).path().encoded(StandardCharsets.UTF_8).length()
        ),
        () -> assertTrue(
          new AbsoluteUri(
            new SchemeComponent.Fake("SSH", "SSH"),
            new QueryComponent.Fake("query", "parameters")
          ).path().asString().isEmpty()
        )
      );
    }
  }

  @Test
  void testScheme() {
    var expected = new SchemeComponent.Fake("https", "https");
    assertEquals(
      expected,
      new AbsoluteUri(
        expected,
        new PathComponent.Fake("", "")
      ).scheme()
    );
  }

  @Test
  void testAuthority() {
    var expected = new AuthorityComponent.Fake(
      "encoded representation",
      "asString",
      new UserinfoSubComponent.Fake("userinfo", "userinfo"),
      new HostSubcomponent.Fake("example.com", "example.com"),
      new PortSubcomponent.Fake(80)
    );
    assertEquals(
      expected,
      new AbsoluteUri(
        new SchemeComponent.Fake("any", "stuff"),
        expected
      ).authority()
    );
  }

  @Test
  void testAsStringWithAllComponents() {
    assertEquals(
      "scheme://authority/path?query",
      new AbsoluteUri(
        new SchemeComponent.Fake("", "scheme"),
        new AuthorityComponent.Fake("", "authority"),
        new PathComponent.Fake(
          new UriComponent.Fake("", ""),
          new UriComponent.Fake("", "//authority/path")
        ),
        new QueryComponent.Fake("", "query")
      ).asString()
    );
  }

  @Test
  void testEncodedWithAllComponents() {
    assertEquals(
      "scheme://authority/path?query",
      new AbsoluteUri(
        new SchemeComponent.Fake("scheme", ""),
        new AuthorityComponent.Fake("authority", ""),
        new PathComponent.Fake(
          new UriComponent.Fake("", ""),
          new UriComponent.Fake("//authority/path", "")
        ),
        new QueryComponent.Fake("query", "")
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testUndefinedScheme() {
    assertUndefinedSchemeException(
      () -> new AbsoluteUri(
        new UndefinedScheme(),
        new PathComponent.Fake("", "")
      ).asString()
    );
  }

  private void assertUndefinedSchemeException(final Executable executable) {
    assertEquals(
      "No representations for an undefined scheme",
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }
}
