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
package io.github.raffaeleflorio.surily.authority;

import io.github.raffaeleflorio.surily.UriComponent;

import java.nio.charset.Charset;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Authority component of an URI
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc3986#section-3.2">RFC3986 about authority component</a>
 * @since 1.0.0
 */
public interface AuthorityComponent extends UriComponent {
  /**
   * Uses a function if the authority is defined otherwise another one
   *
   * @param fn          The function used when the authority is defined
   * @param undefinedFn The supplier used when the authority is undefined
   * @param <T>         The result type
   * @return The result
   */
  <T> T ifDefinedElse(Function<AuthorityComponent, T> fn, Supplier<T> undefinedFn);

  /**
   * Builds the host subcomponent
   *
   * @return The subcomponent
   * @since 1.0.0
   */
  HostSubcomponent host();

  /**
   * Builds the port subcomponent
   *
   * @return The subcomponent
   * @since 1.0.0
   */
  PortSubcomponent port();

  /**
   * Builds the userinfo subcomponent
   *
   * @return The subcomponent
   * @since 1.0.0
   */
  UserinfoSubComponent userinfo();

  /**
   * {@link AuthorityComponent} for testing purpose
   *
   * @author Raffaele Florio (raffaeleflorio@protonmail.com)
   * @since 1.0.0
   */
  final class Fake implements AuthorityComponent {
    /**
     * Builds a fake
     *
     * @param port The port
     * @since 1.0.0
     */
    public Fake(final PortSubcomponent port) {
      this(
        "",
        "",
        new UserinfoSubComponent.Fake("", ""),
        new HostSubcomponent.Fake("", ""),
        port
      );
    }

    /**
     * Builds a fake
     *
     * @param host The host
     * @since 1.0.0
     */
    public Fake(final HostSubcomponent host) {
      this(
        "",
        "",
        new UserinfoSubComponent.Fake("", ""),
        host,
        new PortSubcomponent.Fake(0)
      );
    }

    /**
     * Builds a fake
     *
     * @param userinfo The user info
     * @since 1.0.0
     */
    public Fake(final UserinfoSubComponent userinfo) {
      this(
        "",
        "",
        userinfo,
        new HostSubcomponent.Fake("", ""),
        new PortSubcomponent.Fake(0)
      );
    }

    /**
     * Builds a fake
     *
     * @param encoded  The encoded representation
     * @param asString The asString representation
     * @since 1.0.0
     */
    public Fake(final CharSequence encoded, final String asString) {
      this(
        encoded,
        asString,
        new UserinfoSubComponent.Fake("", ""),
        new HostSubcomponent.Fake("", ""),
        new PortSubcomponent.Fake(0)
      );
    }

    /**
     * Builds a fake
     *
     * @param encoded  The encoded representation
     * @param asString The asString representation
     * @param userinfo The userinfo
     * @param host     The host
     * @param port     The port
     * @since 1.0.0
     */
    public Fake(
      final CharSequence encoded,
      final String asString,
      final UserinfoSubComponent userinfo,
      final HostSubcomponent host,
      final PortSubcomponent port
    ) {
      this.encoded = encoded;
      this.asString = asString;
      this.userinfo = userinfo;
      this.host = host;
      this.port = port;
    }

    @Override
    public <T> T ifDefinedElse(final Function<AuthorityComponent, T> fn, final Supplier<T> undefinedFn) {
      return fn.apply(this);
    }

    @Override
    public HostSubcomponent host() {
      return host;
    }

    @Override
    public PortSubcomponent port() {
      return port;
    }

    @Override
    public UserinfoSubComponent userinfo() {
      return userinfo;
    }

    @Override
    public CharSequence encoded(final Charset charset) {
      return encoded;
    }

    @Override
    public String asString() {
      return asString;
    }

    private final CharSequence encoded;
    private final String asString;
    private final UserinfoSubComponent userinfo;
    private final HostSubcomponent host;
    private final PortSubcomponent port;
  }
}
