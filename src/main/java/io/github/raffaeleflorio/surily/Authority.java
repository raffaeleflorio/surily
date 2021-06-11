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
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * RFC3986 compliant authority {@link AuthorityComponent} like: userinfo@host:port
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class Authority implements AuthorityComponent {
  /**
   * Builds an authority with some of its subcomponents
   *
   * @param userinfo The userinfo
   * @param host     The host
   * @since 1.0.0
   */
  public Authority(final UserinfoSubComponent userinfo, final HostSubcomponent host) {
    this(userinfo, host, new UndefinedPort());
  }

  /**
   * Builds an authority with some of its subcomponents
   *
   * @param host The host
   * @param port The port
   * @since 1.0.0
   */
  public Authority(final HostSubcomponent host, final PortSubcomponent port) {
    this(new UndefinedUserinfo(), host, port);
  }

  /**
   * Builds an authority with only the host subcomponent
   *
   * @param host The host
   * @since 1.0.0
   */
  public Authority(final HostSubcomponent host) {
    this(new UndefinedUserinfo(), host, new UndefinedPort());
  }

  /**
   * Builds an authority with all of its subcomponents
   *
   * @param userinfo The userinfo
   * @param host     The host
   * @param port     The port
   * @since 1.0.0
   */
  public Authority(final UserinfoSubComponent userinfo, final HostSubcomponent host, final PortSubcomponent port) {
    this.userinfo = userinfo;
    this.host = host;
    this.port = port;
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
    return concatenated(
      userinfo.ifDefinedElse(u -> u.encoded(charset), () -> ""),
      host.encoded(charset),
      port.ifDefinedElse(p -> p.encoded(charset), () -> "")
    );
  }

  private String concatenated(final CharSequence userinfo, final CharSequence host, final CharSequence port) {
    return String.format(
      "%s%s%s",
      userinfo.toString().concat(userinfo.length() == 0 ? "" : "@"),
      host,
      port.length() == 0 ? "" : ":".concat(port.toString())
    );
  }

  @Override
  public String asString() {
    return concatenated(
      userinfo.ifDefinedElse(UserinfoSubComponent::asString, () -> ""),
      host.asString(),
      port.ifDefinedElse(PortSubcomponent::asString, () -> "")
    );
  }

  @Override
  public <T> T ifDefinedElse(final Function<AuthorityComponent, T> fn, final Supplier<T> undefinedFn) {
    return fn.apply(this);
  }

  private final UserinfoSubComponent userinfo;
  private final HostSubcomponent host;
  private final PortSubcomponent port;
}
