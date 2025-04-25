package cipher;

import exceptions.NoCoincidenceException;

import java.util.*;

public class Decrypter
{
    private final Encrypter encrypter = new Encrypter();

    public List<String> decrypt(List<Character> alphabet, List<String> encryptedData, int key)
    {

        return encrypter.encrypt(alphabet, encryptedData, key * -1);

    }


}
