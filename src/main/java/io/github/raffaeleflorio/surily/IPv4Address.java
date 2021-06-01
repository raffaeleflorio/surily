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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    var octets = octets();
    return String.format("%s.%s.%s.%s", octets.get(0), octets.get(1), octets.get(2), octets.get(3));
  }

  private List<Integer> octets() {
    assertMaxLength();
    return Arrays.stream(address.toString().split("\\.", 4))
      .map(this::number)
      .map(this::octet)
      .collect(Collectors.toUnmodifiableList());
  }

  private void assertMaxLength() {
    if (address.length() > 15) {
      throw illegalIPAddress();
    }
  }

  private Integer number(final String s) {
    try {
      return Integer.parseInt(s);
    } catch (Exception e) {
      throw illegalIPAddress();
    }
  }

  private Integer octet(final Integer i) {
    if (i < 0 || i > 255) {
      throw illegalIPAddress();
    }
    return i;
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
