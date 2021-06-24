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

class HierPartTest {
  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "//authority/an/absolute/path",
      new HierPart(
        new AuthorityComponent.Fake(
          "authority",
          "",
          new UserinfoSubComponent.Fake("", ""),
          new HostSubcomponent.Fake("", ""),
          new PortSubcomponent.Fake(1234)
        ),
        new PathComponent.AbsoluteFake("/an/absolute/path", "/an/absolute/path", List.of())
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "//authority/an/absolute/path",
      new HierPart(
        new AuthorityComponent.Fake(
          "",
          "authority",
          new UserinfoSubComponent.Fake("", ""),
          new HostSubcomponent.Fake("", ""),
          new PortSubcomponent.Fake(1234)
        ),
        new PathComponent.AbsoluteFake("/an/absolute/path", "/an/absolute/path", List.of())
      ).asString()
    );
  }

  @Test
  void testEncodedWithOnlyRelativePath() throws Throwable {
    assertEquals(
      "a/relative/path",
      new HierPart(
        new PathComponent.AbsoluteFake("a/relative/path", "a/relative/path", List.of())
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testAsStringWithOnlyRelativePath() throws Throwable {
    assertEquals(
      "a/relative/path",
      new HierPart(
        new PathComponent.AbsoluteFake("a/relative/path", "a/relative/path", List.of())
      ).asString()
    );
  }

  @Test
  void testEncodedWithOnlyAbsolutePath() throws Throwable {
    assertEquals(
      "/an/absolute/path",
      new HierPart(
        new PathComponent.AbsoluteFake("/an/absolute/path", "", List.of())
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testAsStirngWithOnlyAbsolutePath() throws Throwable {
    assertEquals(
      "/",
      new HierPart(
        new PathComponent.AbsoluteFake("/", "/", List.of())
      ).asString()
    );
  }

  @Test
  void testIllegalPaths() throws Throwable {
    assertAll(
      () -> assertIllegalPathException(
        () -> new HierPart(
          new AuthorityComponent.Fake(
            "",
            "authority",
            new UserinfoSubComponent.Fake("", ""),
            new HostSubcomponent.Fake("", ""),
            new PortSubcomponent.Fake(1234)
          ),
          new PathComponent.RelativeFake("", "illegal path", List.of())
        ).encoded(StandardCharsets.ISO_8859_1),
        "illegal path"
      ),
      () -> assertIllegalPathException(
        () -> new HierPart(
          new AuthorityComponent.Fake(
            "",
            "authority",
            new UserinfoSubComponent.Fake("", ""),
            new HostSubcomponent.Fake("", ""),
            new PortSubcomponent.Fake(1234)
          ),
          new PathComponent.RelativeFake("", "illegal path", List.of())
        ).asString(),
        "illegal path"
      )
    );
  }

  private void assertIllegalPathException(final Executable executable, final String path) {
    assertEquals(
      String.format("Illegal path in hier-part component: <%s>", path),
      assertThrows(IllegalStateException.class, executable).getMessage()
    );
  }

  @Test
  void testExceptionMaxLength() throws Throwable {
    assertAll(
      () -> assertIllegalPathException(
        () -> new HierPart(
          new AuthorityComponent.Fake(
            "",
            "authority",
            new UserinfoSubComponent.Fake("", ""),
            new HostSubcomponent.Fake("", ""),
            new PortSubcomponent.Fake(1234)
          ),
          new PathComponent.RelativeFake("", "A".repeat(4097), List.of())
        ).encoded(StandardCharsets.UTF_16LE),
        "A".repeat(4096).concat("...")
      ),
      () -> assertIllegalPathException(
        () -> new HierPart(
          new AuthorityComponent.Fake(
            "",
            "authority",
            new UserinfoSubComponent.Fake("", ""),
            new HostSubcomponent.Fake("", ""),
            new PortSubcomponent.Fake(1234)
          ),
          new PathComponent.RelativeFake("", "A".repeat(4097), List.of())
        ).asString(),
        "A".repeat(4096).concat("...")
      )
    );
  }
}
