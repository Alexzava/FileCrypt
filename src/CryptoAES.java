/*
 *
 *  FileCrypt
 *
 *  Github: https://github.com/alexzava/filecrypt
 *
 *  License: Apache License 2.0
 *
 *  Copyright 2018 alexzava
 *
 */

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

public class CryptoAES {

    private static int IV_LENGTH = 12;
    private static int SALT_LENGTH = 64;

    private static int PBKDF2_ITERATIONS = 50000;
    private static int KEY_LENGTH = 256;

    //Encrypt
    public static void Encrypt(String filename, char[] password) {
        Security.addProvider(new BouncyCastleProvider());
        try {

            System.out.println("\nGenerating Encryption Key...");

            //Generate Encryption Key
            byte[] salt = generateSecureBytes(SALT_LENGTH);
            byte[] encryptionKey = PBKDF2(password, salt);

            //Generate the IV
            byte[] IV = generateSecureBytes(IV_LENGTH);

            //Get file size
            long fileSize = new File(filename).length();

            System.out.println("Encrypting Data...\n");

            //AES Cipher Settings
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
            SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV));

            //Read the file and Encrypt
            FileInputStream plainFile = new FileInputStream(filename);
            FileOutputStream encFile = new FileOutputStream(filename + ".enc");
            CipherOutputStream cipherOutputStream = new CipherOutputStream(encFile, cipher);

            //Save Salt and IV
            encFile.write(salt);
            encFile.write(IV);

            byte[] buffer = new byte[8192];
            int actualSize = 0;
            int progress;
            int c;
            while((c = plainFile.read(buffer)) > 0)
            {
                cipherOutputStream.write(buffer, 0, c);

                actualSize += c;
                progress = (int)(actualSize * 100.0 / fileSize + 0.5);
                System.out.print("Progress: " + actualSize + " / " + fileSize + " - " + progress + "%\r");
            }
            cipherOutputStream.close();
            plainFile.close();
            encFile.close();

            System.out.println("\n\nDone!");
        }
        catch (Exception error)
        {
            System.out.println("Error: " + error.getMessage());
        }
    }

    //Decrypt
    public static void Decrypt(String filename, char[] password) {
        Security.addProvider(new BouncyCastleProvider());
        try {

            FileInputStream encFile = new FileInputStream(filename);
            FileOutputStream plainFile = new FileOutputStream(filename.replace(".enc", ""));

            //Read Salt and IV
            byte[] salt = new byte[SALT_LENGTH];
            encFile.read(salt);

            byte[] IV = new byte[IV_LENGTH];
            encFile.read(IV);

            //Get file size
            long fileSize = new File(filename).length();

            System.out.println("\nGenerating Encryption Key...");

            //Generate the Encryption Key
            byte[] encryptionKey = PBKDF2(password, salt);

            System.out.println("Decrypting Data...\n");

            //AES Cipher Settings
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
            SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(IV));

            //Read the file and Decrypt
            CipherInputStream cipherInputStream = new CipherInputStream(encFile, cipher);
            cipherInputStream.skip(SALT_LENGTH + IV_LENGTH);
            byte[] buffer = new byte[8192];
            int actualSize = 0;
            int progress;
            int c;
            while((c = cipherInputStream.read(buffer)) > 0)
            {
                plainFile.write(buffer, 0, c);

                actualSize += c;
                progress = (int)(actualSize * 100.0 / fileSize + 0.5);
                System.out.print("Progress: " + actualSize + " / " + fileSize + " - " + progress + "%\r");
            }
            cipherInputStream.close();
            encFile.close();
            plainFile.close();

            System.out.println("\n\nDone!");

        }
        catch (Exception error)
        {
            System.out.println("Error: " + error.getMessage());
        }
    }

    //Generate a PBKDF2 hash
    private static byte[] PBKDF2(char[] password, byte[] salt) {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, KEY_LENGTH);
            SecretKey secretKey = secretKeyFactory.generateSecret(spec);

            return secretKey.getEncoded();
        }
        catch(Exception error)
        {
            System.out.println("Error: " + error.getMessage());
            return null;
        }
    }

    //Generate a random secure bytes
    private static byte[] generateSecureBytes(int size) {
        byte[] secureBytes = new byte[size];
        SecureRandom secRand = new SecureRandom();
        secRand.nextBytes(secureBytes);
        return secureBytes;
    }
}
