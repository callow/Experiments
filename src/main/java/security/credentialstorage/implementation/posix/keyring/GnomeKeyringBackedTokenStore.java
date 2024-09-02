// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.
package security.credentialstorage.implementation.posix.keyring;

import java.util.Objects;

import security.credentialstorage.model.StoredToken;
import security.credentialstorage.model.StoredTokenType;

/**
 * GNOME Keyring store for a token.
 */
public final class GnomeKeyringBackedTokenStore extends GnomeKeyringBackedSecureStore<StoredToken> {

  @Override
  protected StoredToken create(final String username, final char[] secret) {
    return new StoredToken(secret, StoredTokenType.fromDescription(username));
  }

  @Override
  public boolean add(final String key, final StoredToken token) {
    Objects.requireNonNull(key, "key cannot be null");
    Objects.requireNonNull(token, "secret cannot be null");

    logger.info("Adding a {} for {}", getType(), key);

    final int result = writeSecret(key, token.getType().getDescription(), token.getValue());
    return checkResult(result, "Could not save secret to the storage.");
  }

  @Override
  protected String getType() {
    return "Token";
  }
}
