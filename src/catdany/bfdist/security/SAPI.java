package catdany.bfdist.security;

import java.security.PublicKey;

public class SAPI
{
	/**
	 * Generate a key pair
	 * @param keySize
	 * @return
	 */
	public static SKeyPairGen generateKeyPair(int keySize)
	{
		return new SKeyPairGen(keySize);
	}
	
	/**
	 * Encode a byte array with a given public key
	 * @param key
	 * @param data
	 */
	public static void encode(PublicKey key, byte[] data)
	{
		//
	}
	
	public static PublicKey getPublicKey(byte[] encoded)
	{
		return null;
	}
}