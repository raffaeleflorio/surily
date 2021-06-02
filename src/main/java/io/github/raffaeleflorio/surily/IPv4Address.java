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
import java.util.List;
import java.util.regex.Pattern;

/**
 * RFC3986 compliant IPv4 address {@link HostSubcomponent}
 *
 * @author Raffaele Florio (raffaeleflorio@protonmail.com)
 * @since 1.0.0
 */
public final class IPv4Address implements HostSubcomponent {
  /**
   * Builds an IPv4 address
   *
   * @param first  The first IP octet
   * @param second The second IP octet
   * @param third  The third IP octet
   * @param fourth The fourth IP octet
   */
  public IPv4Address(final Integer first, final Integer second, final Integer third, final Integer fourth) {
    this(List.of(first.toString(), second.toString(), third.toString(), fourth.toString()));
  }

  /**
   * Builds an IPv4 address
   *
   * @param octets The octets
   */
  public IPv4Address(final List<CharSequence> octets) {
    this(String.join(".", octets));
  }

  /**
   * Builds an IPv4 address
   *
   * @param address The address
   */
  public IPv4Address(final CharSequence address) {
    this.address = address;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return address();
  }

  private String address() {
    if (legalAddress()) {
      return address.toString();
    }
    throw illegalIPAddress();
  }

  private Boolean legalAddress() {
    return Pattern.matches(
      "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$",
      address
    );
  }

  private RuntimeException illegalIPAddress() {
    return new IllegalStateException(
      String.format(
        "Illegal IPv4 address: <%s>", address.length() > 4096 ? address.toString().substring(0, 4096).concat("...") : address
      )
    );
  }

  @Override
  public String asString() {
    return address();
  }

  private final CharSequence address;
}
