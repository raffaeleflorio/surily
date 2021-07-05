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

class RelativeRefTest {
  @Test
  void testUndefinedScheme() throws Throwable {
    assertEquals(
      "always undefined",
      new RelativeRef().scheme().ifDefinedElse(x -> "mhn...", () -> "always undefined")
    );
  }

  @Test
  void testEncodedWithAllComponents() throws Throwable {
    assertEquals(
      "//authority/an/absolute/path?query#fragment",
      new RelativeRef(
        new AuthorityComponent.Fake(
          "authority",
          "",
          new UserinfoSubComponent.Fake("", ""),
          new HostSubcomponent.Fake("", ""),
          new PortSubcomponent.Fake(42)
        ),
        new PathComponent.AbsoluteFake("/an/absolute/path", "", List.of()),
        new QueryComponent.Fake("query", ""),
        new FragmentComponent.Fake("fragment", "")
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testAsStringWithAllComponents() throws Throwable {
    assertEquals(
      "//authority/an/absolute/path?query#fragment",
      new RelativeRef(
        new AuthorityComponent.Fake(
          "authority",
          "",
          new UserinfoSubComponent.Fake("", ""),
          new HostSubcomponent.Fake("", ""),
          new PortSubcomponent.Fake(42)
        ),
        new PathComponent.AbsoluteFake("/an/absolute/path", "", List.of()),
        new QueryComponent.Fake("query", ""),
        new FragmentComponent.Fake("fragment", "")
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testEmptyRef() throws Throwable {
    assertAll(
      () -> assertEquals(0, new RelativeRef().encoded(StandardCharsets.UTF_8).length()),
      () -> assertTrue(new RelativeRef().asString().isEmpty())
    );
  }

  @Test
  void testEncodedWithoutAuthority() throws Throwable {
    assertEquals(
      "/an/absolute/path?query#fragment",
      new RelativeRef(
        new PathComponent.AbsoluteFake("/an/absolute/path", "", List.of()),
        new QueryComponent.Fake("query", ""),
        new FragmentComponent.Fake("fragment", "")
      ).encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testIllegalPathException() throws Throwable {
    assertAll(
      () -> assertIllegalPathException(
        () -> new RelativeRef(
          new AuthorityComponent.Fake(
            "any",
            "",
            new UserinfoSubComponent.Fake("", ""),
            new HostSubcomponent.Fake("", ""),
            new PortSubcomponent.Fake(2)
          ),
          new PathComponent.RelativeFake("any relative path", "any relative path", List.of())
        ).encoded(StandardCharsets.US_ASCII),
        "any relative path"
      ),
      () -> assertIllegalPathException(
        () -> new RelativeRef(
          new AuthorityComponent.Fake(
            "any",
            "",
            new UserinfoSubComponent.Fake("", ""),
            new HostSubcomponent.Fake("", ""),
            new PortSubcomponent.Fake(2)
          ),
          new PathComponent.RelativeFake("any relative path", "any relative path", List.of())
        ).asString(),
        "any relative path"
      )
    );
  }

  private void assertIllegalPathException(final Executable executable, final String illegalPath) {
    assertEquals(
      String.format("Illegal path in relative-part component: <%s>", illegalPath),
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }

  @Test
  void testFragment() throws Throwable {
    assertEquals(
      "the fragment",
      new RelativeRef(new FragmentComponent.Fake("the fragment", ""))
        .fragment()
        .encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testQuery() throws Throwable {
    assertEquals(
      "the query",
      new RelativeRef(new QueryComponent.Fake("", "the query"))
        .query()
        .asString()
    );
  }

  @Test
  void testPath() throws Throwable {
    assertEquals(
      "the path",
      new RelativeRef(new PathComponent.AbsoluteFake("the path", "", List.of()))
        .path()
        .encoded(StandardCharsets.UTF_16LE)
    );
  }

  @Test
  void testAuthority() throws Throwable {
    assertEquals(
      "the authority",
      new RelativeRef(
        new AuthorityComponent.Fake(
          "",
          "the authority",
          new UserinfoSubComponent.Fake("", ""),
          new HostSubcomponent.Fake("", ""),
          new PortSubcomponent.Fake(1)
        )
      ).authority().asString()
    );
  }

  @Test
  void testRepresentationsWithOnlyQuery() throws Throwable {
    assertAll(
      () -> assertEquals(
        "?1=1",
        new RelativeRef(new QueryComponent.Fake("", "1=1")).asString()
      ),
      () -> assertEquals(
        "?1=1",
        new RelativeRef(new QueryComponent.Fake("1=1", "")).encoded(StandardCharsets.US_ASCII)
      )
    );
  }

  @Test
  void testRepresentationsWithOnlyFragment() throws Throwable {
    assertAll(
      () -> assertEquals(
        "#reference",
        new RelativeRef(new FragmentComponent.Fake("reference", "")).encoded(StandardCharsets.UTF_16LE)
      ),
      () -> assertEquals(
        "#a reference",
        new RelativeRef(new FragmentComponent.Fake("", "a reference")).asString()
      )
    );
  }

  @Test
  void testRepresentatonsWithOnlyPath() throws Throwable {
    assertAll(
      () -> assertEquals(
        "a relative path",
        new RelativeRef(new PathComponent.RelativeFake("a relative path", "", List.of()))
          .encoded(StandardCharsets.ISO_8859_1)
      ),
      () -> assertEquals(
        "./relative:path",
        new RelativeRef(new PathComponent.RelativeFake("", "./relative:path", List.of())).asString()
      ),
      () -> assertEquals(
        "/an/absolute/path",
        new RelativeRef(new PathComponent.RelativeFake("/an/absolute/path", "", List.of()))
          .encoded(StandardCharsets.UTF_8)
      ),
      () -> assertEquals(
        "/an/absolute/path",
        new RelativeRef(new PathComponent.RelativeFake("", "/an/absolute/path", List.of())).asString()
      )
    );
  }

  @Test
  void testRepresentationsWithOnlyAuthority() throws Throwable {
    assertAll(
      () -> assertEquals(
        "//authority/",
        new RelativeRef(
          new AuthorityComponent.Fake(
            "authority",
            "",
            new UserinfoSubComponent.Fake("", ""),
            new HostSubcomponent.Fake("", ""),
            new PortSubcomponent.Fake(42)
          )
        ).encoded(StandardCharsets.UTF_16)
      ),
      () -> assertEquals(
        "//an authority component/",
        new RelativeRef(
          new AuthorityComponent.Fake(
            "",
            "an authority component",
            new UserinfoSubComponent.Fake("", ""),
            new HostSubcomponent.Fake("", ""),
            new PortSubcomponent.Fake(42)
          )
        ).asString()
      )
    );
  }
}
