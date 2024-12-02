package dpapi;

import com.github.windpapi4j.WinDPAPI;
import com.github.windpapi4j.WinDPAPI.CryptProtectFlag;
/**
 * 
 * 使用 DPAPI 来加密 UUID，而不需要将其存储在 Credential Manager 中。这将减少外部依赖，同时利用 DPAPI 的用户/系统绑定特性确保数据的安全性
 * 
 * 手动指定 加密后的密文直接存储在本地文件或注册表中
 * 
 * 使用面很狭窄 不推荐使用
 */
public class Test {

	public static void main(String[] args) throws Exception  {
		 if(WinDPAPI.isPlatformSupported()) {
            WinDPAPI winDPAPI = WinDPAPI.newInstance(CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);

            String message = "Hello World!";
            String charsetName = "UTF-8";

            byte[] clearTextBytes = message.getBytes(charsetName); // 把明文转换成byte[]

            byte[] cipherTextBytes = winDPAPI.protectData(clearTextBytes); // 加密(无盐)

            byte[] decryptedBytes = winDPAPI.unprotectData(cipherTextBytes); // 解密

            String decryptedMessage = new String(decryptedBytes, charsetName); // 解密后把byte[]转换成明文

            if(! message.equals(decryptedMessage) ) {
                // should not happen
                throw new IllegalStateException(message + " != " + decryptedMessage); 
            }

            System.out.println(decryptedMessage);

        } else {
            System.err.println("ERROR: platform not supported");
        }
	}
}
