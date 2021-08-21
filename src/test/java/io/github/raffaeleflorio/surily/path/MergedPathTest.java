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
package io.github.raffaeleflorio.surily.path;

import io.github.raffaeleflorio.surily.UriReference;
import io.github.raffaeleflorio.surily.authority.AuthorityComponent;
import io.github.raffaeleflorio.surily.authority.UndefinedAuthority;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MergedPathTest {
  @Test
  void testEncodedWithDefinedAuthorityAndEmptyPath() {
    assertEquals(
      "/reference/path",
      new MergedPath(
        new UriReference.Fake(
          new AuthorityComponent.Fake("", ""),
          new PathComponent.Fake("", "")
        ),
        new PathComponent.Fake(
          List.of(
            new PathSegmentSubcomponent.NormalFake("reference", ""),
            new PathSegmentSubcomponent.NormalFake("path", "")
          )
        )
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsStringWithDefinedAuthorityAndEmptyPath() {
    assertEquals(
      "/reference/path",
      new MergedPath(
        new UriReference.Fake(
          new AuthorityComponent.Fake("", ""),
          new PathComponent.Fake("", "")
        ),
        new PathComponent.Fake(
          List.of(
            new PathSegmentSubcomponent.NormalFake("", "reference"),
            new PathSegmentSubcomponent.NormalFake("", "path")
          )
        )
      ).asString()
    );
  }

  @Test
  void testIteratorWithUndefinedAuthorityAndEmptyPath() {
    var expected = List.<PathSegmentSubcomponent>of(
      new PathSegmentSubcomponent.NormalFake("reference", ""),
      new PathSegmentSubcomponent.NormalFake("path", "")
    );
    assertIterableEquals(
      expected,
      new MergedPath(
        new UriReference.Fake(
          new UndefinedAuthority(),
          new PathComponent.Fake("", "")
        ),
        new PathComponent.Fake(
          expected
        )
      )
    );
  }

  @Test
  void testRelativePartWithDefinedAuthorityAndFullPath() {
    assertEquals(
      "/first/second",
      new MergedPath(
        new UriReference.Fake(
          new AuthorityComponent.Fake("", ""),
          new AbsolutePath(
            List.of(
              new PathSegment("first"),
              new PathSegment("nope")
            )
          )
        ),
        new PathComponent.Fake(
          List.of(
            new PathSegmentSubcomponent.NormalFake("", "second")
          )
        )
      ).relativePart().asString()
    );
  }

  @Test
  void testRelativePartAuthorityWithDefinedAuthorityAndEmptyPath() {
    assertEquals(
      "//authority/the/path",
      new MergedPath(
        new UriReference.Fake(
          new AuthorityComponent.Fake("", ""),
          new PathComponent.Fake("", "")
        ),
        new PathComponent.Fake(
          List.of(
            new PathSegmentSubcomponent.NormalFake("the", ""),
            new PathSegmentSubcomponent.NormalFake("path", "")
          )
        )
      ).relativePart(
        new AuthorityComponent.Fake("authority", "")
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  @Test
  void testHierPartWithDefinedAuthorityAndFullPath() {
    assertEquals(
      "1/2/3",
      new MergedPath(
        new UriReference.Fake(
          new AuthorityComponent.Fake("", ""),
          new RelativePath(
            List.of(
              new PathSegment("1"),
              new PathSegment("2nd")
            )
          )
        ),
        new PathComponent.Fake(
          List.of(
            new PathSegmentSubcomponent.NormalFake("2", ""),
            new PathSegmentSubcomponent.NormalFake("3", "")
          )
        )
      ).hierPart().encoded(StandardCharsets.ISO_8859_1)
    );
  }

  @Test
  void testHierPartAuthorityWithDefinedAuthorityAndEmptyPath() {
    assertEquals(
      "//authority/the/path",
      new MergedPath(
        new UriReference.Fake(
          new AuthorityComponent.Fake("", ""),
          new PathComponent.Fake("", "")
        ),
        new PathComponent.Fake(
          List.of(
            new PathSegmentSubcomponent.NormalFake("", "the"),
            new PathSegmentSubcomponent.NormalFake("", "path")
          )
        )
      ).hierPart(
        new AuthorityComponent.Fake("", "authority")
      ).asString()
    );
  }

  @Test
  void testIfEmptyElseTrue() {
    assertTrue(
      new MergedPath(
        new UriReference.Fake(
          new UndefinedAuthority(),
          new RelativePath()
        ),
        new PathComponent.Fake(List.of())
      ).<Boolean>ifEmptyElse(x -> true, x -> false)
    );
  }

  @Test
  void testIfEmptyElseFalse() {
    assertFalse(
      new MergedPath(
        new UriReference.Fake(
          new AuthorityComponent.Fake("", ""),
          new PathComponent.Fake("", "")
        ),
        new PathComponent.Fake("", "")
      ).<Boolean>ifEmptyElse(x -> true, x -> false)
    );
  }

  @Test
  void testSegments() {
    assertEquals(
      "/updated/merged/path",
      new MergedPath(
        new UriReference.Fake(
          new AuthorityComponent.Fake("", ""),
          new PathComponent.Fake("", "")
        ),
        new PathComponent.Fake(
          List.of(
            new PathSegmentSubcomponent.NormalFake("reference", ""),
            new PathSegmentSubcomponent.NormalFake("path", "")
          )
        )
      ).segments(
        List.of(
          new PathSegmentSubcomponent.NormalFake("", "updated"),
          new PathSegmentSubcomponent.NormalFake("", "merged"),
          new PathSegmentSubcomponent.NormalFake("", "path")
        )
      ).asString()
    );
  }

  @Test
  void testIfAbsoluteElseTrue() {
    assertTrue(
      new MergedPath(
        new UriReference.Fake(
          new AuthorityComponent.Fake("", ""),
          new PathComponent.Fake("", "")
        ),
        new PathComponent.Fake(List.of())
      ).<Boolean>ifAbsoluteElse(x -> true, x -> false)
    );
  }

  @Test
  void testIfAbsoluteElseFalse() {
    assertFalse(
      new MergedPath(
        new UriReference.Fake(
          new AuthorityComponent.Fake("", ""),
          new PathComponent.Fake("x", "y")
        ),
        new RelativePath()
      ).<Boolean>ifAbsoluteElse(x -> true, x -> false)
    );
  }
}
