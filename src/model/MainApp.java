package model;

import cipher.DecrypterByAnalytics;
import cipher.Decrypter;
import cipher.DecrypterByBruteForce;
import cipher.Encrypter;
import exceptions.FileIsEmptyException;
import exceptions.InvalidCipherKeyException;
import exceptions.InvalidFileNameException;
import exceptions.NoCoincidenceException;
import exceptions.answer_exceptions.IncorrectAnswerException;
import file_manager.FileManager;
import validation.Validator;

import java.nio.file.Path;
import java.util.*;

public class MainApp
{
    public static final List<Character> ALPHABET = Arrays.asList('а', 'б',
            'в','г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', 'А', 'Б',
            'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У',
            'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', ':', '!', '?', ' ', ';', '-');
    private static final Scanner userAnswer = new Scanner(System.in);
    private static final FileManager fileManager = new FileManager();
    private static final Encrypter encrypter = new Encrypter();
    private static final Decrypter decrypter = new Decrypter(ALPHABET);
    private static final DecrypterByBruteForce decrypterByBruteForce = new DecrypterByBruteForce(ALPHABET);
    private static final DecrypterByAnalytics decrypterByAnalytics = new DecrypterByAnalytics(ALPHABET);
    private static final String delimiter = "*".repeat(50);
    private static boolean isRunning = true;
    private static boolean isWritten;
    private static final String DECRYPT = "расшифровать";
    private static final String DECRYPTED = "расшифрован";
    private static final String ENCRYPTED = "зашифрован";

    private static Path filePath;

    public static void main(String[] args)
    {
        drawInterface();
    }

    private static void drawInterface()
    {
        System.out.println("\nДобро пожаловать в программу по шифрованию!\n");

        while (isRunning)
        {
            isWritten = false;
            filePath = null;
            System.out.println(delimiter);
            System.out.println("Пожалуйста выберите что вы хотите сделать:");
            System.out.println("""
                    1.Зашифровать файл
                    2.Расшифровать файл
                    3.Завершить программу""");
            try
            {
                firstBranch();
            }
            catch (IncorrectAnswerException e)
            {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(delimiter);
        System.out.println("Спасибо за использование программы!");
    }

    private static void drawSecondBranch()
    {

        System.out.println(delimiter);
        System.out.println("""
                    1.Расшифровать файл по ключу
                    2.Расшифровать файл перебором
                    3.Расшифровать файл аналитически
                    4.Вернуться к предыдущему выбору
                    5.Завершить программу""");
        try
        {
            secondBranch();
        }
        catch (IncorrectAnswerException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void firstBranch()
    {
        switch (Validator.validateAnswer(userAnswer.nextLine()  ))
        {
            case 1 -> encryptFile();
            case 2 -> drawSecondBranch();
            case 3 -> isRunning = false;
            default -> throw new IncorrectAnswerException();
        }
    }

    private static void secondBranch()
    {

        switch (Validator.validateAnswer(userAnswer.nextLine()))
        {
            case 1 -> decryptFileByKey();
            case 2 -> decryptFileByBruteForce();
            case 3 -> decryptFileByAnalytics();
            case 4 ->
            {
            }
            case 5 -> isRunning = false;
            default ->  throw new IncorrectAnswerException();
        }
    }

    private static void encryptFile()
    {

        List<String> data = null;
        int key = 0;

        try
        {
            data = receiveFile("","зашифровать");
            key = receiveKey();

        }
        catch (InvalidFileNameException | FileIsEmptyException | InvalidCipherKeyException e)
        {
            System.out.println(e.getMessage());
            return;
        }
        List<String> encryptedData = encrypter.encrypt(ALPHABET, data, key);
        sendDataToFile(encryptedData, ENCRYPTED);

    }

    private static void decryptFileByKey()
    {
        List<String> encryptedData = null;
        int key;
        try
        {
            encryptedData = receiveFile("", DECRYPT);
            key = receiveKey();
        }
        catch (InvalidFileNameException | FileIsEmptyException | InvalidCipherKeyException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        String fileName = receiveOutputFileName();
        List<String> decryptedData = decrypter.decrypt(encryptedData, key);
        sendDataToFile(decryptedData, DECRYPTED, fileName);
    }

    private static void decryptFileByBruteForce()
    {
        List<String> encryptedData;
        List<String> representativeData;
        List<List<String>> decryptedData;
        try
        {
            encryptedData = receiveFile("", DECRYPT);
            representativeData = receiveFile(" репрезентативный", "использовать");
        }
        catch (InvalidFileNameException | FileIsEmptyException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        try
        {
            decryptedData = decrypterByBruteForce.decrypt(encryptedData, representativeData);

        }
        catch (NoCoincidenceException e)
        {
            System.out.println(e.getMessage());
            return;
        }


        String fileName = receiveOutputFileName();
        if (decryptedData.size() == 1)
        {
            sendDataToFile(decryptedData.getFirst(), DECRYPTED, fileName);
        }
        else
        {
            sendMultipleDataToFile(decryptedData, fileName);
        }

    }


    private static void decryptFileByAnalytics()
    {
        List<String> encryptedData;
        List<List<String>> decryptedData;
        try
        {
            encryptedData = receiveFile("", DECRYPT);
        }
        catch (InvalidFileNameException | FileIsEmptyException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        try
        {
            decryptedData = decrypterByAnalytics.decrypt(encryptedData);

        }
        catch (NoCoincidenceException e)
        {
            System.out.println(e.getMessage());
            return;
        }
        String fileName = receiveOutputFileName();
        if (decryptedData.size() == 1)
        {
            sendDataToFile(decryptedData.getFirst(), DECRYPTED, fileName);
        }
        else
        {
            sendMultipleDataToFile(decryptedData, fileName);
        }

    }

    private static List<String> receiveFile(String representative, String choice) throws InvalidFileNameException, FileIsEmptyException
    {
        System.out.println(delimiter);
        System.out.printf("""
                Пожалуйста введите путь к%s текстовому файлу,
                который необходимо %s:
                """, representative, choice);
        String fileName = userAnswer.nextLine();
        filePath = Path.of(fileName);
        return fileManager.getData(fileName);
    }

    private static int receiveKey() throws InvalidCipherKeyException
    {
        System.out.println(delimiter);
        System.out.println("Пожалуйста введите ключ шифрования:");
        return Validator.validateCipherKey(userAnswer.nextLine());
    }

    private static String receiveOutputFileName()
    {
        System.out.println(delimiter);
        System.out.print("""
                Пожалуйста введите путь к файлу,
                в который необходимо записать текст, или
                "exit" чтобы выйти из программы:
                """);
        return userAnswer.nextLine();
    }

    private static void sendDataToFile(List<String> data, String choice)
    {
        String fileName;
        while (!isWritten)
        {
            fileName = receiveOutputFileName();
            if (fileName.equals("exit"))
            {
                isRunning = false;
                return;
            }
            isWritten = fileManager.writeData(fileName, data);
        }

        System.out.printf("Файл %s%n", choice);
    }

    private static void sendDataToFile(List<String> data, String choice, String fileName)
    {
        while (!isWritten)
        {
            isWritten = fileManager.writeData(fileName, data);
            if (isWritten)
            {
                continue;
            }
            fileName = receiveOutputFileName();
            if (fileName.equals("exit"))
            {
                isRunning = false;
                return;
            }
        }

        System.out.printf("Файл %s%n", choice);
    }

    private static void sendMultipleDataToFile(List<List<String>> decryptedData, String fileName)
    {

        for (List<String> variation : decryptedData)
        {
            fileManager.writeData(fileName, variation);
        }
    }

}
