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
import io.github.raffaeleflorio.surily.fragment.FragmentComponent;
import io.github.raffaeleflorio.surily.path.PathComponent;
import io.github.raffaeleflorio.surily.query.QueryComponent;
import io.github.raffaeleflorio.surily.scheme.SchemeComponent;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UriTest {
  @Test
  void testEncodedWithAllComponents() {
    assertEquals(
      "scheme://authority/path?query#fragment",
      new Uri(
        new SchemeComponent.Fake("scheme", ""),
        new AuthorityComponent.Fake("authority", ""),
        new PathComponent.Fake(
          new UriComponent.Fake("", ""),
          new UriComponent.Fake("//authority/path", "")
        ),
        new QueryComponent.Fake("query", ""),
        new FragmentComponent.Fake("fragment", "")
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsStringWithAllComponents() {
    assertEquals(
      "scheme://authority/path?query#fragment",
      new Uri(
        new SchemeComponent.Fake("", "scheme"),
        new AuthorityComponent.Fake("", "authority"),
        new PathComponent.Fake(
          new UriComponent.Fake("", ""),
          new UriComponent.Fake("", "//authority/path")
        ),
        new QueryComponent.Fake("", "query"),
        new FragmentComponent.Fake("", "fragment")
      ).asString()
    );
  }

  @Test
  void testEncodedWithSchemeAndAuthority() {
    assertEquals(
      "scheme://authority",
      new Uri(
        new SchemeComponent.Fake("scheme", ""),
        new AuthorityComponent.Fake("authority", "")
      ).encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testAsStringWithSchemeAndPath() {
    assertEquals(
      "scheme:hier/part",
      new Uri(
        new SchemeComponent.Fake("", "scheme"),
        new PathComponent.Fake(
          new UriComponent.Fake("", ""),
          new UriComponent.Fake("", "hier/part")
        )
      ).asString()
    );
  }

  @Test
  void testEncodedWithSchemeAndQuery() {
    assertEquals(
      "SCM:?query",
      new Uri(
        new SchemeComponent.Fake("SCM", ""),
        new QueryComponent.Fake("query", "")
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testEncodedWithSchemeAndFragment() {
    assertEquals(
      "x:#fragment",
      new Uri(
        new SchemeComponent.Fake("", "x"),
        new FragmentComponent.Fake("", "fragment")
      ).asString()
    );
  }

  @Test
  void testAsStringWithSchemePathAndFragment() {
    assertEquals(
      "scheme:hier/part#fragment",
      new Uri(
        new SchemeComponent.Fake("", "scheme"),
        new PathComponent.Fake(
          new UriComponent.Fake("", ""),
          new UriComponent.Fake("", "hier/part")
        ),
        new FragmentComponent.Fake("", "fragment")
      ).asString()
    );
  }

  @Test
  void testAsStringWithSchemePathAndQuery() {
    assertEquals(
      "scheme:hier/part?query",
      new Uri(
        new SchemeComponent.Fake("", "scheme"),
        new PathComponent.Fake(
          new UriComponent.Fake("", ""),
          new UriComponent.Fake("", "hier/part")
        ),
        new QueryComponent.Fake("", "query")
      ).asString()
    );
  }

  @Test
  void testEncodedWithSchemeAuthorityAndQuery() {
    assertEquals(
      "scheme://authority?query",
      new Uri(
        new SchemeComponent.Fake("scheme", ""),
        new AuthorityComponent.Fake("authority", ""),
        new QueryComponent.Fake("query", "")
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testEncodedWithSchemeAuthorityAndFragment() {
    assertEquals(
      "scheme://authority#fragment",
      new Uri(
        new SchemeComponent.Fake("scheme", ""),
        new AuthorityComponent.Fake("authority", ""),
        new FragmentComponent.Fake("fragment", "")
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsStringWithSchemeAuthorityPathFragment() {
    assertEquals(
      "scheme://authority/path#fragment",
      new Uri(
        new SchemeComponent.Fake("", "scheme"),
        new AuthorityComponent.Fake("", "authority"),
        new PathComponent.Fake(
          new UriComponent.Fake("", ""),
          new UriComponent.Fake("", "//authority/path")
        ),
        new FragmentComponent.Fake("", "fragment")
      ).asString()
    );
  }

  @Test
  void testAsStringWithSchemeQueryAndFragment() {
    assertEquals(
      "scheme:?query#fragment",
      new Uri(
        new SchemeComponent.Fake("", "scheme"),
        new QueryComponent.Fake("", "query"),
        new FragmentComponent.Fake("", "fragment")
      ).asString()
    );
  }

  @Test
  void testEncodedWithSchemeAuthorityQueryAndFragment() {
    assertEquals(
      "scheme://authority?query#fragment",
      new Uri(
        new SchemeComponent.Fake("", "scheme"),
        new AuthorityComponent.Fake("", "authority"),
        new QueryComponent.Fake("", "query"),
        new FragmentComponent.Fake("", "fragment")
      ).asString()
    );
  }

  @Test
  void testSchemeWithMinimalCtor() {
    var expected = new SchemeComponent.Fake("https", "https");
    assertEquals(expected, new Uri(expected).scheme());
  }

  @Test
  void testUndefinedAuthorityWithMinimalCtor() {
    assertEquals(
      "undefined",
      new Uri(
        new SchemeComponent.Fake("any", "stuff"))
        .authority()
        .ifDefinedElse(
          x -> "defined :/",
          () -> "undefined"
        )
    );
  }

  @Test
  void testDefinedAuthorityWithMinimalCtor() {
    var expected = new AuthorityComponent.Fake("expected", "authority");
    assertEquals(
      expected,
      new Uri(new SchemeComponent.Fake("any", "stuff"), expected).authority()
    );
  }

  @Test
  void testEmptyPathWithMinimalCtor() {
    assertTrue(
      new Uri(new SchemeComponent.Fake("any", "value"))
        .path()
        .<Boolean>ifEmptyElse(
          x -> true,
          y -> false
        )
    );
  }

  @Test
  void testPathWithMinimalCtor() {
    var expected = new PathComponent.Fake("expected", "query");
    assertEquals(
      expected,
      new Uri(new SchemeComponent.Fake("any", "value"), expected).path()
    );
  }

  @Test
  void testUndefinedQueryWithMinimalCtor() {
    assertEquals(
      "undefined",
      new Uri(new SchemeComponent.Fake("http", "http"))
        .query()
        .ifDefinedElse(
          x -> "defined",
          () -> "undefined"
        )
    );
  }

  @Test
  void testDefinedQueryWithMinimalCtor() {
    var expected = new QueryComponent.Fake("expected", "query");
    assertEquals(
      expected,
      new Uri(new SchemeComponent.Fake("any", "value"), expected).query()
    );
  }

  @Test
  void testUndefinedFragmentWithMinimalCtor() {
    assertEquals(
      "undefined",
      new Uri(new SchemeComponent.Fake("any", "value"))
        .fragment()
        .ifDefinedElse(
          x -> "defined",
          () -> "undefined"
        )
    );
  }

  @Test
  void testDefinedFragmentWithMinimalCtor() {
    var expected = new FragmentComponent.Fake("expected", "fragment");
    assertEquals(
      expected,
      new Uri(new SchemeComponent.Fake("any", "value"), expected).fragment()
    );
  }
}
