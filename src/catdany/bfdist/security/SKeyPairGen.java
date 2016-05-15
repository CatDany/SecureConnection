package catdany.bfdist.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import catdany.bfdist.log.SCLog;

public class SKeyPairGen
{
	private KeyPair pair;
	
	/**
	 * Initialize a {@link KeyPairGenerator} and generate a key pair<br>
	 * In order to get the {@link KeyPair} use //
	 * @param keySize
	 */
	public SKeyPairGen(int keySize)
	{
		try
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(keySize, random);
			this.pair = keyGen.generateKeyPair();
		}
		catch (NoSuchAlgorithmException | NoSuchProviderException t)
		{
			SCLog.t(t);
			SCLog.e("Unable to initialize a key pair generator.");
		}
	}
	
	/**
	 * Check if the KeyPair was properly generated
	 * @return <code>true</code> if {@link KeyPair} exists
	 */
	public boolean isInitialized()
	{
		return pair != null;
	}
	
	/**
	 * Get generated private key
	 * @return
	 * @see #getPrivateBytes()
	 */
	public PrivateKey getPrivateKey()
	{
		return pair.getPrivate();
	}
	
	/**
	 * Get generated public key
	 * @return
	 * @see #getPublicBytes()
	 */
	public PublicKey getPublicKey()
	{
		return pair.getPublic();
	}
	
	/**
	 * Get key pair containing {@link PublicKey} and {@link PrivateKey}
	 * @return
	 * @see #getPrivateKey()
	 * @see #getPublicKey()
	 */
	public KeyPair getPair()
	{
		return pair;
	}

	/**
	 * Equivalent to {@link PublicKey#getEncoded()} of {@link SKeyPairGen#getPublicKey()}
	 * @return
	 */
	public byte[] getPrivateBytes()
	{
		return getPrivateKey().getEncoded();
	}
	
	/**
	 * Equivalent to {@link PublicKey#getEncoded()} of {@link SKeyPairGen#getPublicKey()}
	 * @return
	 */
	public byte[] getPublicBytes()
	{
		return getPublicKey().getEncoded();
	}
}