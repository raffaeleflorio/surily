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

import java.nio.charset.Charset;

/**
 * RFC3986 compliant hier-part
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class HierPart implements UriComponent {
  /**
   * Builds a hier-part
   *
   * @param path The path
   * @since 1.0.0
   */
  public HierPart(final PathComponent path) {
    this(new UndefinedAuthority(), path);
  }

  /**
   * Builds a hier-part
   *
   * @param authority The authority
   * @param path      The path
   * @since 1.0.0
   */
  public HierPart(final AuthorityComponent authority, final PathComponent path) {
    this.authority = authority;
    this.path = path;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return authority.ifDefinedElse(x -> encodedWithAuthority(x, charset), () -> encodedWithoutAuthority(charset));
  }

  private String encodedWithAuthority(final AuthorityComponent authority, final Charset charset) {
    return concatenated(
      authority.encoded(charset),
      path.ifAbsoluteElse(
        x -> x.encoded(charset),
        y -> {
          throw illegalPathException();
        })
    );
  }

  private String concatenated(final CharSequence authority, final CharSequence path) {
    return String.format("//%s%s", authority, path);
  }

  private RuntimeException illegalPathException() {
    var illegalPath = path.asString();
    return new IllegalStateException(
      String.format(
        "Illegal path in hier-part component: <%s>",
        illegalPath.length() > 4096 ? illegalPath.substring(0, 4096).concat("...") : illegalPath
      )
    );
  }

  private CharSequence encodedWithoutAuthority(final Charset charset) {
    return path.encoded(charset);
  }

  @Override
  public String asString() {
    return authority.ifDefinedElse(this::asStringWithAuthority, this::asStringWithoutAuthority);
  }

  private String asStringWithAuthority(final AuthorityComponent authority) {
    return concatenated(
      authority.asString(),
      path.ifAbsoluteElse(
        PathComponent::asString,
        y -> {
          throw illegalPathException();
        })
    );
  }

  private String asStringWithoutAuthority() {
    return path.asString();
  }

  private final AuthorityComponent authority;
  private final PathComponent path;
}
