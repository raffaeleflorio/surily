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
 * RFC3986 compliant same-document {@link UriReference}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-4.4">RFC3986 definition</a>
 * @since 1.0.0
 */
public final class SameDocumentReference implements UriReference {
  /**
   * Builds a reference
   *
   * @param fragment The fragment
   * @since 1.0.0
   */
  public SameDocumentReference(final FragmentComponent fragment) {
    this(new RelativeRef(fragment));
  }

  /**
   * Builds a reference
   *
   * @param origin The relative-ref origin
   * @since 1.0.0
   */
  private SameDocumentReference(final UriReference origin) {
    this.origin = origin;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return origin.encoded(charset);
  }

  @Override
  public String asString() {
    return origin.asString();
  }

  @Override
  public SchemeComponent scheme() {
    return origin.scheme();
  }

  @Override
  public AuthorityComponent authority() {
    return origin.authority();
  }

  @Override
  public PathComponent path() {
    return origin.path();
  }

  @Override
  public QueryComponent query() {
    return origin.query();
  }

  @Override
  public FragmentComponent fragment() {
    return origin.fragment();
  }

  private final UriReference origin;
}
