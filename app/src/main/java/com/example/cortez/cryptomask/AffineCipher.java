package com.example.cortez.cryptomask;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Cortez on 3/11/2017.
 */

public class AffineCipher {

    AffineCipher(){}

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

    private static boolean relativelyPrimeTest(int a, int m){

        return gcd(a, m)==1;
    }

    private static int gcd(int a, int m){
        int t;
        while(m!=0){
            t=a;
            a=m;
            m=t%m;
        }
        return a;
    }

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
