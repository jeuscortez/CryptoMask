package com.example.cortez.cryptomask;

import java.util.Random;

/**
 * Created by Cortez on 2/15/2017.
 */

public class VigenereCipher {

    VigenereCipher(){}

    protected static String encrypt(String plaintext, final String key) {

        NumberToWordConverter numberToString = new NumberToWordConverter();
        String check = "";
        String convertedText = plaintext;
        int foundNumber;

        for (int i = 0; i < plaintext.length(); i++) {
            if (Character.isDigit(plaintext.charAt(i))) {
                foundNumber = Character.getNumericValue(plaintext.charAt(i));
                check = numberToString.convertNumToString(foundNumber);
                convertedText = convertedText.replaceAll(Character.toString(plaintext.charAt(i)), check);
                i++;
            }
        }

        convertedText = convertedText.toUpperCase();
        String theKey = key.toUpperCase();
        String ciphertext = "";

        for (int i = 0, j = 0; i < convertedText.length(); i++) {
            char c = convertedText.charAt(i);
            if (c < 'A' || c > 'Z') {
                continue;
            }
            ciphertext += (char) ((c + theKey.charAt(j) - 2 * 'A') % 26 + 'A');
            j = ++j % theKey.length();
        }
        return ciphertext;
    }

    protected static String decrypt(String ciphertext, final String key) {
        ciphertext = ciphertext.toUpperCase();
        String theKey = key.toUpperCase();
        String plaintext = "";
        for (int i = 0, j = 0; i < ciphertext.length(); i++) {
            char letterValue = ciphertext.charAt(i);
            if (letterValue < 'A' || letterValue > 'Z') {
                continue;
            }
            plaintext += (char) ((letterValue - theKey.charAt(j) + 26) % 26 + 'A');
            j = ++j % theKey.length();
        }
        return plaintext;
    }

    protected static String createKey(){
        String templates[] = new String[]{
         "red","blue","black","yellow","orange","purple","brown","green","white","grey","magenta","pink"};

        String Alphabet[] = new String[]{
        "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

        String Key ="";
        Random rand = new Random();
        String tempo = templates[rand.nextInt(templates.length-1)];
        for (int i=0;i<2;i++){
            Key += Alphabet[rand.nextInt(Alphabet.length-1)];
        }

        Key += tempo;

        for (int i=0;i<2;i++){
            Key += Alphabet[rand.nextInt(Alphabet.length-1)];
        }
        return Key;
    }
}
