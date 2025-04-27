package model;

import cipher.Decrypter;
import cipher.DecrypterByBruteForce;
import cipher.Encrypter;
import exceptions.FileIsEmptyException;
import exceptions.InvalidCipherKeyException;
import exceptions.InvalidFileNameException;
import exceptions.NoCoincidenceException;
import file_manager.FileManager;
import validation.Validator;

import java.util.*;

public class MainApp
{
    public static final List<Character> ALPHABET = Arrays.asList('а', 'б',
            'в','г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', 'А', 'Б',
            'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У',
            'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', ':', '!', '?', ' ');
    private static final Scanner userAnswer = new Scanner(System.in);
    private static final FileManager fileManager = new FileManager();
    private static final Encrypter encrypter = new Encrypter();
    private static final Decrypter decrypter = new Decrypter();
    private final DecrypterByBruteForce decrypterByBruteForce = new DecrypterByBruteForce();
    private static final String delimiter = "*".repeat(50);
    private static boolean isRunning = true;
    private static boolean isWritten;
    public static int answerPool;

    public static void main(String[] args)
    {
        drawInterface();
    }

    private static void drawInterface()
    {
        System.out.println("\nДобро пожаловать в программу по шифрованию!\n");

        while (isRunning)
        {
            answerPool = 3;
            isWritten = false;
            System.out.println(delimiter);
            System.out.println("Пожалуйста выберите что вы хотите сделать:");
            System.out.println("""
                    1.Зашифровать файл
                    2.Расшифровать файл
                    3.Завершить программу""");
            firstBranch();
        }
        System.out.println(delimiter);
        System.out.println("Спасибо за использование программы!");
    }

    private static void firstBranch()
    {
        switch (Validator.validateAnswer(userAnswer.nextLine()  ))
        {
            case 1 -> encryptFile();
            case 2 -> drawSecondBranch();
            case 3 -> isRunning = false;
        }
    }

    private static void drawSecondBranch()
    {

        System.out.println(delimiter);
        System.out.println("""
                    1.Расшифровать файл по ключу
                    2.Расшифровать файл перебором
                    3.Расшифровать файл аналитически
                    4.Вернуться к предыдущему выбору
                    5.Завершить программу
                    """);
        secondBranch();
    }

    private static void secondBranch()
    {
        answerPool = 5;

        switch (Validator.validateAnswer(userAnswer.nextLine()))
        {
            case 1 -> decryptFileByKey();
            case 2 -> decryptFileByBruteForce();
            case 3 -> decryptFileByAnalytics();
            case 4 -> {}
            case 5 -> isRunning = false;
        }
    }

    private static void decryptFileByKey()
    {
        System.out.println(delimiter);
        List<String> encryptedData = null;
        try
        {
            encryptedData = getFile("расшифровать");
        }
        catch (InvalidFileNameException | FileIsEmptyException e)
        {
            System.out.println(e.getMessage());
            return;
        }
        int key = getKey();
        String fileName = getOutputFile();
        fileManager.writeData(fileName, decrypter.decrypt(ALPHABET, encryptedData, key));
    }

    private static void decryptFileByBruteForce()
    {

    }

    private static void decryptFileByAnalytics()
    {

    }

    private static void encryptFile()
    {

        List<String> data = null;
        int key = 0;
        String fileName = null;
        boolean isWritten = false;
        try
        {
            data = getFile("зашифровать");
            key = getKey();

        }
        catch (InvalidFileNameException | FileIsEmptyException | InvalidCipherKeyException e)
        {
            System.out.println(e.getMessage());
            return;
        }
        List<String> encryptedData = encrypter.encrypt(ALPHABET, data, key);

        while (!isWritten)
        {
            fileName = getOutputFile();
            if (fileName.equals("exit"))
            {
                isRunning = false;
                return;
            }
            isWritten = fileManager.writeData(fileName, encryptedData);
        }

        System.out.println("Файл зашифрован!");
    }

    private static List<String> getFile(String choice) throws InvalidFileNameException, FileIsEmptyException
    {
        System.out.println(delimiter);
        System.out.printf("""
                Пожалуйста введите путь к текстовому файлу,
                который необходимо %s:
                """, choice);
        String fileName = userAnswer.nextLine();

        return fileManager.getData(fileName);
    }

    private static int getKey() throws InvalidCipherKeyException
    {
        System.out.println(delimiter);
        System.out.println("Пожалуйста введите ключ шифрования:");
        return Validator.validateCipherKey(userAnswer.nextLine());
    }

    private static String getOutputFile()
    {
        System.out.println(delimiter);
        System.out.print("""
                Пожалуйста введите путь к файлу,
                в который необходимо записать зашифрованный текст, или
                "exit" чтобы выйти из программы:
                """);
        String answer = userAnswer.nextLine();
        return answer;
    }

}
