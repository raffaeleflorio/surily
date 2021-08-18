# Surily: URI for Java, surely in OOP

[![Licensed under Apache-2.0](https://img.shields.io/github/license/raffaeleflorio/surily)](https://raw.githubusercontent.com/raffaeleflorio/surily/main/LICENSE)
[![CircleCI build status](https://img.shields.io/circleci/build/github/raffaeleflorio/surily/main?label=circleci)](https://circleci.com/gh/raffaeleflorio/surily/)
[![Codecov reports](https://img.shields.io/codecov/c/github/raffaeleflorio/surily)](https://codecov.io/gh/raffaeleflorio/surily)

Surily is collection of objects about [URI](https://datatracker.ietf.org/doc/html/rfc3986) for Java 11+. It's written
with object thinking, standardization and minimalism in mind. Excluding JDK 11+ it doesn't require any dependencies.

*It's in work in progress state*

## Examples

Here you are some examples:

```java
import io.github.raffaeleflorio.surily.*;
import io.github.raffaeleflorio.surily.authority.Authority;
import io.github.raffaeleflorio.surily.authority.RegName;
import io.github.raffaeleflorio.surily.fragment.Fragment;
import io.github.raffaeleflorio.surily.path.*;
import io.github.raffaeleflorio.surily.query.PairQuery;
import io.github.raffaeleflorio.surily.scheme.Scheme;

import java.nio.charset.StandardCharsets;

class Examples {
  void assertDogFactUri() {
    assertEquals(
      "https://dog-facts-api.herokuapp.com/api/v1/resources/dogs?number=1",
      new AbsoluteUri(
        new Scheme("https"),
        new Authority(
          new RegName("dog-facts-api.herokuapp.com")
        ),
        new AbsolutePath(
          List.of(
            new PathSegment("api"),
            new PathSegment("v1"),
            new PathSegment("resources"),
            new PathSegment("dogs")
          )
        ),
        new PairQuery("number", "1")
      ).asString()
    );
  }

  void assertSameDocumentReference() {
    assertEquals(
      "#a%20reference%20in%20a%20document",
      new SameDocumentReference(
        new Fragment("a reference in a document")
      ).encoded(StandardCharsets.UTF_8)
    );
  }

  void assertPathTraversal() {
    assertEquals(
      "../../../../etc/passwd",
      new RelativePath(
        List.of(
          new DoubleDotSegment(),
          new DoubleDotSegment(),
          new DoubleDotSegment(),
          new DoubleDotSegment(),
          new PathSegment("etc"),
          new PathSegment("passwd")
        )
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  void assertNormalizedPathTraversal() {
    assertEquals(
      "etc/passwd",
      new RelativePath(
        new NormalizedSegments(
          new RelativePath(
            List.of(
              new DoubleDotSegment(),
              new DoubleDotSegment(),
              new DoubleDotSegment(),
              new DoubleDotSegment(),
              new PathSegment("etc"),
              new PathSegment("passwd")
            )
          )
        )
      ).encoded(StandardCharsets.US_ASCII)
    );
  }

  void assertISBN() {
    assertEquals(
      "urn:isbn:0048230464",
      new AbsoluteUri(
        new Scheme("urn"),
        new RelativePath(
          List.of(
            new PathSegment("isbn:0048230464")
          )
        )
      ).encoded(StandardCharsets.UTF_8)
    );
  }
  // WIP
}
```

## Integration with Maven

Surily will be distributed through the [Maven Central Repository](https://search.maven.org/). So you can integrate it
with:

```xml
<dependency>
  <groupId>io.github.raffaeleflorio</groupId>
  <artifactId>surily</artifactId>
  <version>1.0.0</version>
</dependency>
```
