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
    this.first = first;
    this.second = second;
    this.third = third;
    this.fourth = fourth;
  }

  @Override
  public CharSequence encoded(final Charset charset) {
    return address();
  }

  private String address() {
    assertLegalOctets();
    return String.format("%s.%s.%s.%s", first, second, third, fourth);
  }

  private void assertLegalOctets() {
    if (!octet(first) || !octet(second) || !octet(third) || !octet(fourth)) {
      throw illegalOctets();
    }
  }

  private Boolean octet(final Integer value) {
    return value > -1 && value < 256;
  }

  private RuntimeException illegalOctets() {
    return new IllegalStateException(
      String.format(
        "Illegal IPv4 octets: <%s,%s,%s,%s>", first, second, third, fourth
      )
    );
  }

  @Override
  public String asString() {
    return address();
  }

  private final Integer first;
  private final Integer second;
  private final Integer third;
  private final Integer fourth;
}
