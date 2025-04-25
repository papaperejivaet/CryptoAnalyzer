package model;

import cipher.Decrypter;
import cipher.DecrypterByBruteForce;
import cipher.Encrypter;
import file_manager.FileManager;

import java.util.*;

public class MainApp
{
    public static final List<Character> ALPHABET = Arrays.asList('а', 'б',
            'в','г', 'д', 'е', 'ж', 'з', 'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»',
            ':', '!', '?', ' ');

    public static void main(String[] args)
    {
        FileManager fileManager = new FileManager();
        Encrypter encrypter = new Encrypter();
        Decrypter decrypter = new Decrypter();
        DecrypterByBruteForce dBBF = new DecrypterByBruteForce();
        int key = 6;

        List<String> data = fileManager.getData("Test1.txt");
        List<String> decryptedData = encrypter.encrypt(ALPHABET, data, key);
        fileManager.writeData("Test1Input.txt", decryptedData);
        fileManager.writeData("Test1Output.txt", decrypter.decrypt(ALPHABET, decryptedData, key));


        List<String> representativeData;






    }
}
