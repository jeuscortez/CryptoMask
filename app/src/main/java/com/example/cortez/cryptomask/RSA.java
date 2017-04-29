package com.example.cortez.cryptomask;

import android.util.Base64;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Cortez on 3/27/2017.
 */

public class RSA {

    public BigInteger[] generateKeys() {

        Random rand = new Random();
        int IterationTests = 2; //number of test runs on the numbers
        //creation of two BigIntegers that will be tested by Fermat Primality Test
        BigInteger t = new BigInteger(128, 15, rand);
        BigInteger u = new BigInteger(128, 15, rand);

        boolean prime = runFermatPrimalityTest(t, IterationTests);
        boolean prime2 = runFermatPrimalityTest(u, IterationTests);

        if (prime && prime2) { //if both are prime then we proceed to create RSA keys
            BigInteger primes[] = {t, u};
            //creates phi(n) and then generates Key e to then creates Key d
            BigInteger[] keys = getKeys(primes);
            return keys;
        } else {
            //returns an empty BigInteger array to notify the activty to re-run function
            BigInteger zero[] = {BigInteger.ZERO};
            return zero;
        }
    }

    public boolean runFermatPrimalityTest(BigInteger n, int iteration) {
        BigInteger two = new BigInteger("2");
        Random rand = new Random();

        // base case
        if (n.equals(BigInteger.ZERO) || n.equals(BigInteger.ONE)) {
            return false;
        }
        // base case - 2 is prime
        if (n.equals(BigInteger.valueOf(2))) {
            return true;
        }
        // an even number other than 2 is composite
        if (n.mod(two).equals(BigInteger.ZERO)) {
            return false;
        }
        //runs the Fermat Primality test on any number that the other conditions could not verify
        if (!fermatPrimalityTest(n, iteration, rand)) {
            return false;
        }
        //if all test are passed it will verify by sending true to the key generator
        return true;
    }

    public boolean fermatPrimalityTest(BigInteger n, int Iterations, Random rand) {
        if (n.equals(BigInteger.ONE)) {
            return false;
        }
        for (int i = 0; i < Iterations; i++) {
            BigInteger a = generateRandFermatA(n, rand);
            //test if a is prime by using Math Formula: a^(n-1)mod n
            a = a.modPow(n.subtract(BigInteger.ONE), n);
            if (!a.equals(BigInteger.ONE)) {
                return false;
            }
        }
        return true;
    }

    public BigInteger generateRandFermatA(BigInteger n, Random rand) {
        while (true) {
            BigInteger a = new BigInteger(n.bitLength(), rand);
            //we are look for a to follow condition: 1<a<n
            if (BigInteger.ONE.compareTo(a) <= 0 && a.compareTo(n) < 0) {
                return a; //this means it follows the condition for a
            }
        }
    }

    public BigInteger[] getKeys(BigInteger primes[]) {

        BigInteger p = primes[0];
        BigInteger q = primes[1];
        Random rand = new Random();
        //N = p * q
        BigInteger n = p.multiply(q);
        //Phi(n) = (p-1)*(q-1)
        BigInteger PhiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        // creating the e according the condition that it is between 1< e < phi(n), and relatively prime to phi(n)
        BigInteger e = BigInteger.probablePrime(127, rand);

        while (PhiN.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(PhiN) < 0) {
            e.add(BigInteger.ONE);
        }
        //d is the modular inverse of e (mod Phi(n))
        BigInteger d = e.modInverse(PhiN);
        //the keys for the RSA are created and will be assigned to the BigInteger array
        BigInteger keys[] = {n, e, d};
        return keys;
    }

    public String encryption(String plaintext, BigInteger e, BigInteger N) {
        byte[] message = plaintext.getBytes();
        //create the ciphertext by using the Math formula: (msgLetter)^e(mod N) for each letter
        byte[] ciphertext = (new BigInteger(message)).modPow(e, N).toByteArray();
        String Ctext = Base64.encodeToString(ciphertext,Base64.DEFAULT);
        return Ctext;
    }

    public String encryptionNumberform(String plaintext, BigInteger e, BigInteger N){
        byte[] message = plaintext.getBytes();
        //create the ciphertext by using the Math formula: (msgLetter)^e(mod N) for each letter
        byte[] ciphertext = (new BigInteger(message)).modPow(e, N).toByteArray();

        return byteToString(ciphertext);
    }

    public String byteToString(byte[] encryption) {
        //this method is just to display the original message to the screen
        String temp = "";

        for (byte b : encryption) {
            temp += Byte.toString(b);
        }
        return temp;
    }

    public String decryption(String ciphertext, BigInteger d, BigInteger N) {
        byte[] msg = Base64.decode(ciphertext, Base64.DEFAULT);
        //recovers the original plaintext by using Math formula: (ciphertextLetter)^d(mod N) for each cipher letter
        byte[] message = (new BigInteger(msg)).modPow(d, N).toByteArray();
        String plaintext = new String(message);
        return plaintext;
    }
}
