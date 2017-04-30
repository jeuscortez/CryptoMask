package com.example.cortez.cryptomask;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Cortez on 3/11/2017.
 */

public class AffineCipher {

    AffineCipher(){}

    /**
     * This method is responsible for generating alpha and beta key for affine cipher
     * It genrates two randoms and then tests if Alpha is relatively prime to 26
     * If the alpha passes the test it creates the keys and stores them into an array
     */
    protected static int[] keyGenerator(){

        Random rand = new Random();
        int Alpha = rand.nextInt(500)+2;///////////////////// to avoid 0 and 1
        int Beta = rand.nextInt(26)+1;//to avoid 0
        int modulo = 26;
        int[] keys = new int[2];

        while(!relativelyPrimeTest(Alpha,modulo) == true){
            Alpha=rand.nextInt(500)+2;
        }
        keys[0] = Alpha;
        keys[1]=Beta;

        return keys;

    }

    /**
     * This method is responsible for checking if alpha and 26 are relatively prime
     *It calls the gcd method to do the testing
     *
     * @param a is the alpha key generated that will be tested to be relatively prime
     * @param m is the value of our modulo that needs to be relatively prime to alpha
     * @return True if they are relatively prime, False if they are not
     */
    private static boolean relativelyPrimeTest(int a, int m){

        return gcd(a, m)==1;
    }

    /**
     * This method is responsible for testing what the Greatest Common Denominator is of the two numbers
     *
     * @param a is the alpha key for the Affine Cipher
     * @param m is the value of the modulo for the Affine Cipher
     * @return 1 if they are relatively prime or other value which proves they are not relatively prime.
     */
    private static int gcd(int a, int m){
        int t;
        while(m!=0){
            t=a;
            a=m;
            m=t%m;
        }
        return a;
    }

    /**
     * This method is responsible for creating the encryption of the text, which is referred to as cipher text
     *
     * @param text is the users' selected text to encrypt
     * @param Alpha is the alpha key generated and tested for validation
     * @param Beta is the other key that was also generated with alpha key
     * @return The Cipher text form of the regular text provided
     */
    protected static String encryption(String text,int Alpha, int Beta){
        text=text.toLowerCase();
        StringBuilder CipherText = new StringBuilder();
        int modulo = 26;

        //(a*x+B)mod 26
        for(int i=0;i<text.length();i++){
            char x = text.charAt(i);
            char encryptedLetter ='a';//initializing as ascii
            if(Character.isLetter(x)){
                encryptedLetter=(char)((Alpha*(x-'a')+Beta)%modulo+'a');
                //ASCII code 'a' = 97.
            }
            CipherText.append(encryptedLetter);
        }
        return CipherText.toString();
    }

    /**
     * This method is responsible for decrypting the Affine Cipher Encryption
     *
     * @param cipherText is the encrypted form of the users regular text
     * @param Alpha is the key used to encrypt the user's text
     * @param Beta is the other key used to encrypt user's text as well
     * @return The Original Text that user chose to encrypt
     */
    protected static String decryption(String cipherText,int Alpha, int Beta){
        StringBuilder plainText = new StringBuilder();
        BigInteger inverse = BigInteger.valueOf(Alpha).modInverse(BigInteger.valueOf(26));

        for (int i = 0; i < cipherText.length(); i++) {
            char letter = cipherText.charAt(i);
            if(Character.isLetter(letter)){
                int decoded = inverse.intValue()*(letter-'a'-Beta+26);
                letter = (char)(decoded%26+'a'); //mod 26 part of decryption formula
            }
            plainText.append(letter);
        }
        return plainText.toString();
    }
}
