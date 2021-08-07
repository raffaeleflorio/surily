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

class ConcatenatedQueriesTest {
  @Test
  void testEncoded() {
    assertEquals(
      "first%20query&second%20query&third%20query",
      new ConcatenatedQueries(
        List.of(
          new QueryComponent.Fake("first%20query", "first query"),
          new QueryComponent.Fake("second%20query", "second query"),
          new QueryComponent.Fake("third%20query", "third query")
        )
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testAsString() {
    assertEquals(
      "first query&second query&third query",
      new ConcatenatedQueries(
        List.of(
          new QueryComponent.Fake("first%20query", "first query"),
          new QueryComponent.Fake("second%20query", "second query"),
          new QueryComponent.Fake("third%20query", "third query")
        )
      ).asString()
    );
  }

  @Test
  void testCustomDelimiter() {
    assertEquals(
      "first%20query%3B;second%20query;third%20qu%3Bery",
      new ConcatenatedQueries(
        List.of(
          new QueryComponent.Fake("first%20query;", "first query;"),
          new QueryComponent.Fake("second%20query", "second query"),
          new QueryComponent.Fake("third%20qu;ery", "third qu;ery")
        ),
        ';'
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  @Test
  void testDisallowedDelimiter() {
    assertAll(
      () -> assertThrowsWithMessage(
        IllegalStateException.class,
        () -> new ConcatenatedQueries(List.of(new QueryComponent.Fake("query", "query")), '#').encoded(StandardCharsets.UTF_8),
        illegalDelimiter('#')
      ),
      () -> assertThrowsWithMessage(
        IllegalStateException.class,
        () -> new ConcatenatedQueries(List.of(new QueryComponent.Fake("query", "query")), '\u219D').encoded(StandardCharsets.UTF_8),
        illegalDelimiter('\u219D')
      ),
      () -> assertThrowsWithMessage(
        IllegalStateException.class,
        () -> new ConcatenatedQueries(List.of(new QueryComponent.Fake("query", "query")), '%').encoded(StandardCharsets.US_ASCII),
        illegalDelimiter('%')
      ),
      () -> assertThrowsWithMessage(
        IllegalStateException.class,
        () -> new ConcatenatedQueries(List.of(new QueryComponent.Fake("query", "query")), '[').asString(),
        illegalDelimiter('[')
      )
    );
  }

  private String illegalDelimiter(final Character delimiter) {
    return String.format("Illegal delimiter: <%s>", delimiter);
  }

  private void assertThrowsWithMessage(final Class<? extends Throwable> expectedException, final Executable executable, final String expectedMessage) {
    assertEquals(
      expectedMessage,
      assertThrows(expectedException, executable).getMessage()
    );
  }

  @Test
  void testIfDefinedElse() {
    assertTrue(new ConcatenatedQueries(List.of()).ifDefinedElse(x -> true, () -> false));
  }
}
