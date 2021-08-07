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

class RelativeRefTest {
  @Test
  void testUndefinedScheme() {
    assertEquals(
      "always undefined",
      new RelativeRef().scheme().ifDefinedElse(x -> "mhn...", () -> "always undefined")
    );
  }

  @Test
  void testEncodedWithAllComponents() {
    assertEquals(
      "//authority/an/absolute/path?query#fragment",
      new RelativeRef(
        new AuthorityComponent.Fake("", "authority"),
        new PathComponent.Fake(
          new UriComponent.Fake("//authority/an/absolute/path", ""),
          new UriComponent.Fake("", "")
        ),
        new QueryComponent.Fake("query", ""),
        new FragmentComponent.Fake("fragment", "")
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testAsStringWithAllComponents() {
    assertEquals(
      "//authority/an/absolute/path?query#fragment",
      new RelativeRef(
        new AuthorityComponent.Fake("", "authority"),
        new PathComponent.Fake(
          new UriComponent.Fake("", "//authority/an/absolute/path"),
          new UriComponent.Fake("", "")
        ),
        new QueryComponent.Fake("", "query"),
        new FragmentComponent.Fake("", "fragment")
      ).asString()
    );
  }

  @Test
  void testEmptyRef() {
    assertAll(
      () -> assertEquals(0, new RelativeRef().encoded(StandardCharsets.UTF_8).length()),
      () -> assertTrue(new RelativeRef().asString().isEmpty())
    );
  }

  @Test
  void testEncodedWithoutAuthority() {
    assertEquals(
      "/an/absolute/path?query#fragment",
      new RelativeRef(
        new PathComponent.Fake(
          new UriComponent.Fake("/an/absolute/path", ""),
          new UriComponent.Fake("", "")
        ),
        new QueryComponent.Fake("query", ""),
        new FragmentComponent.Fake("fragment", "")
      ).encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testFragment() {
    assertEquals(
      "the fragment",
      new RelativeRef(new FragmentComponent.Fake("the fragment", ""))
        .fragment()
        .encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testQuery() {
    assertEquals(
      "the query",
      new RelativeRef(new QueryComponent.Fake("", "the query"))
        .query()
        .asString()
    );
  }

  @Test
  void testPath() {
    assertEquals(
      "the path",
      new RelativeRef(new PathComponent.Fake("the path", ""))
        .path()
        .encoded(StandardCharsets.UTF_16LE)
    );
  }

  @Test
  void testAuthority() {
    assertEquals(
      "the authority",
      new RelativeRef(
        new AuthorityComponent.Fake("", "the authority")
      ).authority().asString()
    );
  }

  @Test
  void testRepresentationsWithOnlyQuery() {
    assertAll(
      () -> assertEquals(
        "?1=1",
        new RelativeRef(
          new QueryComponent.Fake("", "1=1")
        ).asString()
      ),
      () -> assertEquals(
        "?1=1",
        new RelativeRef(
          new QueryComponent.Fake("1=1", "")
        ).encoded(StandardCharsets.US_ASCII)
      )
    );
  }

  @Test
  void testRepresentationsWithOnlyFragment() {
    assertAll(
      () -> assertEquals(
        "#reference",
        new RelativeRef(
          new FragmentComponent.Fake("reference", "")
        ).encoded(StandardCharsets.UTF_16LE)
      ),
      () -> assertEquals(
        "#a reference",
        new RelativeRef(
          new FragmentComponent.Fake("", "a reference")
        ).asString()
      )
    );
  }

  @Test
  void testRepresentatonsWithOnlyPath() {
    assertAll(
      () -> assertEquals(
        "a relative path",
        new RelativeRef(
          new PathComponent.Fake(
            new UriComponent.Fake("a relative path", ""),
            new UriComponent.Fake("", "")
          )
        ).encoded(StandardCharsets.ISO_8859_1)
      ),
      () -> assertEquals(
        "./relative:path",
        new RelativeRef(
          new PathComponent.Fake(
            new UriComponent.Fake("", "./relative:path"),
            new UriComponent.Fake("", "")
          )
        ).asString()
      ),
      () -> assertEquals(
        "/an/absolute/path",
        new RelativeRef(
          new PathComponent.Fake(
            new UriComponent.Fake("/an/absolute/path", ""),
            new UriComponent.Fake("", "")
          )
        ).encoded(StandardCharsets.UTF_8)
      ),
      () -> assertEquals(
        "/an/absolute/path",
        new RelativeRef(
          new PathComponent.Fake(
            new UriComponent.Fake("", "/an/absolute/path"),
            new UriComponent.Fake("", "")
          )
        ).asString()
      )
    );
  }

  @Test
  void testRepresentationsWithOnlyAuthority() {
    assertAll(
      () -> assertEquals(
        "//authority",
        new RelativeRef(
          new AuthorityComponent.Fake("authority", "")
        ).encoded(StandardCharsets.UTF_16)
      ),
      () -> assertEquals(
        "//an authority component",
        new RelativeRef(
          new AuthorityComponent.Fake("", "an authority component")
        ).asString()
      )
    );
  }
}
