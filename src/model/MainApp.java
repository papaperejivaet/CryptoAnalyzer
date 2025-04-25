package model;

import cipher.Decrypter;
import cipher.DecrypterByBruteForce;
import cipher.Encrypter;
import exceptions.NoCoincidenceException;
import file_manager.FileManager;

import java.util.*;

public class MainApp
{
    public static final List<Character> ALPHABET = Arrays.asList('а', 'б',
            'в','г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', 'А', 'Б',
            'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У',
            'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', ':', '!', '?', ' ');

    public static void main(String[] args)
    {
        FileManager fileManager = new FileManager();
        Encrypter encrypter = new Encrypter();
        Decrypter decrypter = new Decrypter();
        DecrypterByBruteForce dBBF = new DecrypterByBruteForce();
        int key = 6;

        List<String> data = fileManager.getData("Test1.txt");
        List<String> representativeData = fileManager.getData("Test2Representative.txt");
        List<String> decryptedData = encrypter.encrypt(ALPHABET, data, key);
        fileManager.writeData("Test1Input.txt", decryptedData);
        fileManager.writeData("Test1Output.txt", decrypter.decrypt(ALPHABET, decryptedData, key));

        try
        {
            List<List<String>> dBBFResult = dBBF.decrypt(ALPHABET, decryptedData, representativeData);

            if (dBBFResult.size() == 1)
            {
                fileManager.writeData("Test2Result.txt", dBBFResult.getFirst());
            }


        }
        catch (NoCoincidenceException e)
        {
            System.out.println(e.getMessage());
        }


    }
}
