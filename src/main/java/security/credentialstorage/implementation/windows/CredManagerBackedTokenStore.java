// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.
package security.credentialstorage.implementation.windows;

import java.util.Objects;

import security.credentialstorage.model.StoredToken;
import security.credentialstorage.model.StoredTokenType;

/**
 * Credential Manager store for a token.
 */
public final class CredManagerBackedTokenStore extends CredManagerBackedSecureStore<StoredToken> {

  @Override
  protected StoredToken create(final String username, final char[] secret) {
    return new StoredToken(secret, StoredTokenType.fromDescription(username));
  }

  @Override
  public boolean add(final String key, final StoredToken secret) {
    Objects.requireNonNull(key, "key cannot be null");
    Objects.requireNonNull(secret, "secret cannot be null");

    logger.info("Adding secret for {}", key);

    return writeSecret(key, secret.getType().getDescription(), secret.getValue());
  }
}
