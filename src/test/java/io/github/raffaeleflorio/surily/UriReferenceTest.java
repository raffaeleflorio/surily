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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UriReferenceTest {
  @Nested
  class FakeTest {
    @Test
    void testEncoded() {
      var expected = "encoded";
      assertEquals(
        expected,
        new UriReference.Fake(expected, "any").encoded(StandardCharsets.ISO_8859_1)
      );
    }

    @Test
    void testAsString() {
      var expected = "asString";
      assertEquals(
        expected,
        new UriReference.Fake("any", expected).asString()
      );
    }

    @Test
    void testScheme() {
      var expected = new SchemeComponent.Fake("the", "scheme");
      assertEquals(
        expected,
        new UriReference.Fake(expected).scheme()
      );
    }

    @Test
    void testAuthority() {
      var expected = new AuthorityComponent.Fake("the", "authority");
      assertEquals(
        expected,
        new UriReference.Fake(expected).authority()
      );
    }

    @Test
    void testPath() {
      var expected = new PathComponent.Fake("the", "path");
      assertEquals(
        expected,
        new UriReference.Fake(expected).path()
      );
    }

    @Test
    void testQuery() {
      var expected = new QueryComponent.Fake("the", "query");
      assertEquals(
        expected,
        new UriReference.Fake(expected).query()
      );
    }

    @Test
    void testFragment() {
      var expected = new FragmentComponent.Fake("the", "fragment");
      assertEquals(
        expected,
        new UriReference.Fake(expected).fragment()
      );
    }
  }
}
