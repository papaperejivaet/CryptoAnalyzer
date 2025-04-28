package cipher;


import java.util.*;

public class Decrypter
{
    private final Encrypter encrypter = new Encrypter();
    private final List<Character> alphabet;

    public Decrypter(List<Character> alphabet)
    {
        this.alphabet = alphabet;
    }

    public List<String> decrypt(List<String> encryptedData, int key)
    {

        return encrypter.encrypt(alphabet, encryptedData, key * -1);

    }


}
