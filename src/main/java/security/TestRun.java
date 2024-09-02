package security;

import security.credentialstorage.implementation.windows.CredManagerBackedCredentialStore;
import security.credentialstorage.model.StoredCredential;

/**
 * https://github.com/microsoft/vsts-authentication-library-for-java/tree/master
 * 
 * https://github.com/microsoft/vsts-authentication-library-for-java/blob/master/storage/src/test/java/com/microsoft/alm/storage/windows/CredManagerBackedCredentialStoreIT.java
 */
public class TestRun  {
	
	public static void main(String[] args) {
		CredManagerBackedCredentialStore windowsCredentialManager = new CredManagerBackedCredentialStore();
		StoredCredential credential = new StoredCredential("username", "password".toCharArray());
		String key = "CredManagerTest:http://test.com:Credential";
		
		// 添加进credential store
		windowsCredentialManager.add(key, credential);
		
		// 查看
		StoredCredential one = windowsCredentialManager.get(key);
		System.out.println(one.getPassword());
		
		//删除
		windowsCredentialManager.delete(key);
		//----------------------------------------------------------------------------------------------
		
//		CredManagerBackedTokenPairStore windowsCredentialManager2 = new CredManagerBackedTokenPairStore();
//		String key2 = "http://thisisatestkey";
//		
//		StoredToken accessToken = new StoredToken("access".toCharArray(), StoredTokenType.ACCESS);
//		StoredToken refreshToken = new StoredToken("refresh".toCharArray(), StoredTokenType.REFRESH);
//		StoredTokenPair tokenPair = new StoredTokenPair(accessToken,refreshToken);
//		
//		windowsCredentialManager2.add(key2, tokenPair);
//		
//        final StoredTokenPair one2 = windowsCredentialManager2.get(key2);
//        System.out.println(one2.getAccessToken());
//        
//        windowsCredentialManager2.delete(key2);
	}


	
}
