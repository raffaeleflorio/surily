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

class AbsoluteUriTest {
  @Test
  void testUndefinedFragment() throws Throwable {
    assertEquals(
      "the fragment is always undefined",
      new AbsoluteUri(
        new SchemeComponent.Fake("any", "value"),
        new AuthorityComponent.Fake(
          "123",
          "456",
          new UserinfoSubComponent.Fake("x", "y"),
          new HostSubcomponent.Fake("alpha", "beta"),
          new PortSubcomponent.Fake(0)
        ),
        new PathComponent.AbsoluteFake("abcd", "efg", List.of()),
        new QueryComponent.Fake("query", "query")
      ).fragment().ifDefinedElse(x -> "...", () -> "the fragment is always undefined")
    );
  }

  @Test
  void testUndefinedQuery() throws Throwable {
    assertAll(
      () -> assertTrue(
        new AbsoluteUri(
          new SchemeComponent.Fake("http", "http"),
          new PathComponent.AbsoluteFake("any", "path", List.of())
        ).query().ifDefinedElse(x -> false, () -> true)
      ),
      () -> assertTrue(
        new AbsoluteUri(
          new SchemeComponent.Fake("any", "value"),
          new AuthorityComponent.Fake(
            "123",
            "456",
            new UserinfoSubComponent.Fake("x", "y"),
            new HostSubcomponent.Fake("alpha", "beta"),
            new PortSubcomponent.Fake(0)
          )
        ).query().ifDefinedElse(x -> false, () -> true)
      ),
      () -> assertTrue(
        new AbsoluteUri(
          new SchemeComponent.Fake("any", "value"),
          new AuthorityComponent.Fake(
            "123",
            "456",
            new UserinfoSubComponent.Fake("x", "y"),
            new HostSubcomponent.Fake("alpha", "beta"),
            new PortSubcomponent.Fake(0)
          ),
          new PathComponent.AbsoluteFake("abcd", "efg", List.of())
        ).query().ifDefinedElse(x -> false, () -> true)
      ),
      () -> assertEquals(
        "http:",
        new AbsoluteUri(
          new SchemeComponent.Fake("", "http"),
          new PathComponent.AbsoluteFake("", "", List.of())
        ).asString()
      ),
      () -> assertEquals(
        "http:a/path",
        new AbsoluteUri(
          new SchemeComponent.Fake("http", ""),
          new PathComponent.AbsoluteFake("a/path", "", List.of())
        ).encoded(StandardCharsets.US_ASCII)
      )
    );
  }

  @Test
  void testEmptyPath() throws Throwable {
    {
      assertAll(
        () -> assertTrue(
          new AbsoluteUri(
            new SchemeComponent.Fake("any", "value"),
            new AuthorityComponent.Fake(
              "123",
              "456",
              new UserinfoSubComponent.Fake("x", "y"),
              new HostSubcomponent.Fake("alpha", "beta"),
              new PortSubcomponent.Fake(0)
            )
          ).path().asString().isEmpty()
        ),
        () -> assertEquals(
          0,
          new AbsoluteUri(
            new SchemeComponent.Fake("any", "value"),
            new AuthorityComponent.Fake(
              "123",
              "456",
              new UserinfoSubComponent.Fake("x", "y"),
              new HostSubcomponent.Fake("alpha", "beta"),
              new PortSubcomponent.Fake(0)
            ),
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
  void testScheme() throws Throwable {
    var expected = new SchemeComponent.Fake("https", "https");
    assertEquals(
      expected,
      new AbsoluteUri(expected, new PathComponent.AbsoluteFake("", "", List.of())).scheme()
    );
  }

  @Test
  void testAuthority() throws Throwable {
    var expected = new AuthorityComponent.Fake(
      "encoded representation",
      "asString",
      new UserinfoSubComponent.Fake("userinfo", "userinfo"),
      new HostSubcomponent.Fake("example.com", "example.com"),
      new PortSubcomponent.Fake(80)
    );
    assertEquals(
      expected,
      new AbsoluteUri(new SchemeComponent.Fake("any", "stuff"), expected).authority()
    );
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "scheme://authority/path?query",
      new AbsoluteUri(
        new SchemeComponent.Fake("", "scheme"),
        new AuthorityComponent.Fake(
          "",
          "authority",
          new UserinfoSubComponent.Fake("", "userinfo"),
          new HostSubcomponent.Fake("", "example.com"),
          new PortSubcomponent.Fake(80)
        ),
        new PathComponent.AbsoluteFake("", "/path", List.of()),
        new QueryComponent.Fake("", "query")
      ).asString()
    );
  }

  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "scheme://authority/path?query",
      new AbsoluteUri(
        new SchemeComponent.Fake("scheme", ""),
        new AuthorityComponent.Fake(
          "authority",
          "",
          new UserinfoSubComponent.Fake("userinfo", ""),
          new HostSubcomponent.Fake("example.com", ""),
          new PortSubcomponent.Fake(80)
        ),
        new PathComponent.AbsoluteFake("/path", "", List.of()),
        new QueryComponent.Fake("query", "")
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testIllegalPath() throws Throwable {
    assertIllegalPathException(
      () -> new AbsoluteUri(
        new SchemeComponent.Fake("", ""),
        new AuthorityComponent.Fake(
          "",
          "",
          new UserinfoSubComponent.Fake("", ""),
          new HostSubcomponent.Fake("", ""),
          new PortSubcomponent.Fake(80)
        ),
        new PathComponent.RelativeFake("", "", List.of())
      ).asString(),
      ""
    );
  }

  private void assertIllegalPathException(final Executable executable, final String path) {
    assertEquals(
      String.format("Illegal path in hier-part component: <%s>", path),
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }

  @Test
  void testUndefinedScheme() throws Throwable {
    assertUndefinedSchemeException(
      () -> new AbsoluteUri(
        new UndefinedScheme(),
        new PathComponent.RelativeFake("", "", List.of())
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
