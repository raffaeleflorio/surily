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

class SameDocumentReferenceTest {
  @Test
  void testUndefinedScheme() throws Throwable {
    assertTrue(
      new SameDocumentReference(new FragmentComponent.Fake("any", "value"))
        .scheme()
        .ifDefinedElse(x -> false, () -> true)
    );
  }

  @Test
  void testUndefinedAuthority() throws Throwable {
    assertTrue(
      new SameDocumentReference(new FragmentComponent.Fake("any", "value"))
        .authority()
        .ifDefinedElse(x -> false, () -> true)
    );
  }

  @Test
  void testEmptyPath() throws Throwable {
    assertAll(
      () -> assertEquals(
        0,
        new SameDocumentReference(new FragmentComponent.Fake("any", "value"))
          .path()
          .encoded(StandardCharsets.US_ASCII)
          .length()
      ),
      () -> assertTrue(
        new SameDocumentReference(new FragmentComponent.Fake("any", "value"))
          .path()
          .asString()
          .isEmpty()
      )
    );
  }

  @Test
  void testUndefinedQuery() throws Throwable {
    assertTrue(
      new SameDocumentReference(new FragmentComponent.Fake("any", "value"))
        .query()
        .ifDefinedElse(x -> false, () -> true)
    );
  }

  @Test
  void testFragment() throws Throwable {
    var expected = new FragmentComponent.Fake("encoded", "asString");
    assertEquals(expected, new SameDocumentReference(expected).fragment());
  }

  @Test
  void testEncoded() throws Throwable {
    assertEquals(
      "#encoded%20fragment",
      new SameDocumentReference(new FragmentComponent.Fake("encoded%20fragment", ""))
        .encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsString() throws Throwable {
    assertEquals(
      "#as string representation",
      new SameDocumentReference(new FragmentComponent.Fake("", "as string representation")).asString()
    );
  }
}
