package security.credentialstorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import security.credentialstorage.implementation.macosx.KeychainSecurityBackedCredentialStore;
import security.credentialstorage.implementation.macosx.KeychainSecurityBackedTokenPairStore;
import security.credentialstorage.implementation.macosx.KeychainSecurityBackedTokenStore;
import security.credentialstorage.implementation.macosx.KeychainSecurityCliStore;
import security.credentialstorage.implementation.memory.InsecureInMemoryStore;
import security.credentialstorage.implementation.posix.keyring.GnomeKeyringBackedCredentialStore;
import security.credentialstorage.implementation.posix.keyring.GnomeKeyringBackedSecureStore;
import security.credentialstorage.implementation.posix.keyring.GnomeKeyringBackedTokenPairStore;
import security.credentialstorage.implementation.posix.keyring.GnomeKeyringBackedTokenStore;
import security.credentialstorage.implementation.posix.libsecret.LibSecretBackedCredentialStore;
import security.credentialstorage.implementation.posix.libsecret.LibSecretBackedSecureStore;
import security.credentialstorage.implementation.posix.libsecret.LibSecretBackedTokenPairStore;
import security.credentialstorage.implementation.posix.libsecret.LibSecretBackedTokenStore;
import security.credentialstorage.implementation.windows.CredManagerBackedCredentialStore;
import security.credentialstorage.implementation.windows.CredManagerBackedSecureStore;
import security.credentialstorage.implementation.windows.CredManagerBackedTokenPairStore;
import security.credentialstorage.implementation.windows.CredManagerBackedTokenStore;
import security.credentialstorage.model.StoredCredential;
import security.credentialstorage.model.StoredSecret;
import security.credentialstorage.model.StoredToken;
import security.credentialstorage.model.StoredTokenPair;

/**
 * Returns a store for credential, token or token pair for the requested security level.
 */
public final class StorageProvider {

  private static final Logger logger = LoggerFactory.getLogger(StorageProvider.class);
  private static final List<SecretStore<StoredToken>> PERSISTED_TOKEN_STORE_CANDIDATES;
  private static final List<SecretStore<StoredTokenPair>> PERSISTED_TOKENPAIR_STORE_CANDIDATES;
  private static final List<SecretStore<StoredCredential>> PERSISTED_CREDENTIAL_STORE_CANDIDATES;

  static {
    final List<SecretStore<StoredCredential>> credentialStoreCandidates = new ArrayList<>();
    final List<SecretStore<StoredToken>> tokenStoreCandidates = new ArrayList<>();
    final List<SecretStore<StoredTokenPair>> tokenPairStoreCandidates = new ArrayList<>();

    if (CredManagerBackedSecureStore.isSupported()) {
      credentialStoreCandidates.add(new CredManagerBackedCredentialStore());
      tokenStoreCandidates.add(new CredManagerBackedTokenStore());
      tokenPairStoreCandidates.add(new CredManagerBackedTokenPairStore());
    }

    if (KeychainSecurityCliStore.isSupported()) {
      credentialStoreCandidates.add(new KeychainSecurityBackedCredentialStore());
      tokenStoreCandidates.add(new KeychainSecurityBackedTokenStore());
      tokenPairStoreCandidates.add(new KeychainSecurityBackedTokenPairStore());
    }

    if (LibSecretBackedSecureStore.isSupported()) {
      credentialStoreCandidates.add(new LibSecretBackedCredentialStore());
      tokenStoreCandidates.add(new LibSecretBackedTokenStore());
      tokenPairStoreCandidates.add(new LibSecretBackedTokenPairStore());
    } else if (GnomeKeyringBackedSecureStore.isSupported()) {
      credentialStoreCandidates.add(new GnomeKeyringBackedCredentialStore());
      tokenStoreCandidates.add(new GnomeKeyringBackedTokenStore());
      tokenPairStoreCandidates.add(new GnomeKeyringBackedTokenPairStore());
    }

    PERSISTED_TOKEN_STORE_CANDIDATES = Collections.unmodifiableList(tokenStoreCandidates);
    PERSISTED_TOKENPAIR_STORE_CANDIDATES = Collections.unmodifiableList(tokenPairStoreCandidates);
    PERSISTED_CREDENTIAL_STORE_CANDIDATES = Collections.unmodifiableList(credentialStoreCandidates);
  }

  private StorageProvider() {
  }

  /**
   * Returns a token store for the specific requirements.
   *
   * @param persist true for persistent storage
   * @param secureOption secure or non-secure storage
   * @return store
   */
  public static SecretStore<StoredToken> getTokenStorage(final boolean persist,
      final SecureOption secureOption) {
    Objects.requireNonNull(secureOption, "secureOption cannot be null");

    logger.info("Getting a {} token store that {} be secure",
        persist ? "persistent" : "non-persistent",
        secureOption == SecureOption.REQUIRED ? "required" : "preferred");

    final NonPersistentStoreGenerator<StoredToken> inMemoryStoreGenerator = new NonPersistenceStoreForStoredToken();

    return getStore(persist, secureOption, PERSISTED_TOKEN_STORE_CANDIDATES,
        inMemoryStoreGenerator);
  }

  /**
   * Returns a token pair store for the specific requirements.
   *
   * @param persist true for persistent storage
   * @param secureOption secure or non-secure storage
   * @return store
   */
  public static SecretStore<StoredTokenPair> getTokenPairStorage(final boolean persist,
      final SecureOption secureOption) {
    Objects.requireNonNull(secureOption, "secureOption cannot be null");

    logger.info("Getting a {} tokenPair store that {} be secure",
        persist ? "persistent" : "non-persistent",
        secureOption == SecureOption.REQUIRED ? "required" : "preferred");

    final NonPersistentStoreGenerator<StoredTokenPair> inMemoryStoreGenerator = new NonPersistenceStoreForStoredTokenPair();

    return getStore(persist, secureOption, PERSISTED_TOKENPAIR_STORE_CANDIDATES,
        inMemoryStoreGenerator);
  }

  /**
   * Returns a credential store for the specific requirements.
   *
   * @param persist true for persistent storage
   * @param secureOption secure or non-secure storage
   * @return store
   */
  public static SecretStore<StoredCredential> getCredentialStorage(final boolean persist,
      final SecureOption secureOption) {
    Objects.requireNonNull(secureOption, "secureOption cannot be null");

    logger.info("Getting a {} credential store that {} be secure",
        persist ? "persistent" : "non-persistent",
        secureOption == SecureOption.REQUIRED ? "required" : "preferred");

    final NonPersistentStoreGenerator<StoredCredential> inMemoryStoreGenerator = new NonPersistenceStoreForStoredCredential();

    return getStore(persist, secureOption, PERSISTED_CREDENTIAL_STORE_CANDIDATES,
        inMemoryStoreGenerator);
  }

  private static <E extends StoredSecret> SecretStore<E> findSecureStore(
      final List<SecretStore<E>> stores) {
    for (final SecretStore<E> store : stores) {
      if (store.isSecure()) {
        return store;
      }
    }

    return null;
  }

  private static <E extends StoredSecret> SecretStore<E> findPersistedStore(
      final SecureOption secureOption, final List<SecretStore<E>> stores) {
    SecretStore<E> candidate = findSecureStore(stores);

    if (candidate == null && secureOption == SecureOption.PREFERRED) {
      // just return any store from the list since none of them is secure
      if (!stores.isEmpty()) {
        candidate = stores.get(0);
      }
    }

    // nullable
    return candidate;
  }

  static <E extends StoredSecret> SecretStore<E> getStore(final boolean persist,
      final SecureOption secureOption, final List<SecretStore<E>> stores,
      final NonPersistentStoreGenerator<E> nonPersistentStoreGenerator) {
    Objects.requireNonNull(nonPersistentStoreGenerator,
        "nonPersistentStoreGenerator cannot be null.");
    Objects.requireNonNull(stores, "stores cannot be null.");

    SecretStore<E> candidate;
    if (persist) {
      candidate = findPersistedStore(secureOption, stores);
    } else {
      // not persisted
      candidate = nonPersistentStoreGenerator.getSecureNonPersistentStore();
      if (candidate == null && secureOption == SecureOption.PREFERRED) {
        candidate = nonPersistentStoreGenerator.getInsecureNonPersistentStore();
      }
    }

    return candidate;
  }

  /**
   * Option for requesting a store defining whether it is required to be secure or not.
   */
  public enum SecureOption {
    /**
     * The store must be secure, i.e. generally the storage needs to be password protected
     * and data potentially is encrypted
     *
     * However, this program makes no assertion on *how* secure the storage really is. It's only
     * an attribute on the storage
     */
    REQUIRED,

    /**
     * Prefer a secure storage, but if none is available, an unprotected, non-secure storage will be returned
     */
    PREFERRED
  }

  interface NonPersistentStoreGenerator<E extends StoredSecret> {

    SecretStore<E> getInsecureNonPersistentStore();

    SecretStore<E> getSecureNonPersistentStore();
  }

  public static class NonPersistenceStoreForStoredToken implements
      StorageProvider.NonPersistentStoreGenerator<StoredToken> {

    @Override
    public SecretStore<StoredToken> getInsecureNonPersistentStore() {
      return new InsecureInMemoryStore<>();
    }

    @Override
    public SecretStore<StoredToken> getSecureNonPersistentStore() {
      logger.warn("Do not have any secure non-persistent stores available.");
      return null;
    }
  }

  public static class NonPersistenceStoreForStoredTokenPair implements
      StorageProvider.NonPersistentStoreGenerator<StoredTokenPair> {

    @Override
    public SecretStore<StoredTokenPair> getInsecureNonPersistentStore() {
      return new InsecureInMemoryStore<>();
    }

    @Override
    public SecretStore<StoredTokenPair> getSecureNonPersistentStore() {
      logger.warn("Do not have any secure non-persistent stores available.");
      return null;
    }
  }

  public static class NonPersistenceStoreForStoredCredential implements
      StorageProvider.NonPersistentStoreGenerator<StoredCredential> {

    @Override
    public SecretStore<StoredCredential> getInsecureNonPersistentStore() {
      return new InsecureInMemoryStore<>();
    }

    @Override
    public SecretStore<StoredCredential> getSecureNonPersistentStore() {
      logger.warn("Do not have any secure non-persistent stores available.");
      return null;
    }
  }


}
