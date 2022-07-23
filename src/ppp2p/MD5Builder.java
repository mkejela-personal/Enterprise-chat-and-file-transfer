package ppp2p;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class MD5Builder {

	static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 *Generate MD5 code
	 * 
	 * @param file
	 *            File need to be encrypted
	 * @return md5 code
	 */
	public static String getMD5(File file) {
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			fis = new FileInputStream(file);
			byte[] buffer = new byte[2048];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			byte[] b = md.digest();
			return byteToHexString(b);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Convert byte array to 16 hex string
	 */
	private static String byteToHexString(byte[] tmp) {
		String s;
		char str[] = new char[16 * 2];

		int k = 0;
		for (int i = 0; i < 16; i++) {

			byte byte0 = tmp[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];

			str[k++] = hexDigits[byte0 & 0xf];
		}
		s = new String(str);
		return s;
	}
}