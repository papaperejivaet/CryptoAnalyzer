package model;

import cipher.Decrypter;
import cipher.DecrypterByBruteForce;
import cipher.Encrypter;
import exceptions.FileIsEmptyException;
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
    private final Scanner userAnswer = new Scanner(System.in);
    private final FileManager fileManager = new FileManager();
    private final Encrypter encrypter = new Encrypter();
    private final Decrypter decrypter = new Decrypter();
    private final DecrypterByBruteForce decrypterByBruteForce = new DecrypterByBruteForce();
    private final String delimiter = "*".repeat(50);
    private boolean isRunning = true;
    private boolean isWritten;
    public static int answerPool;
    public static MainApp app = new MainApp();

    public static void main(String[] args)
    {
        app.drawInterface();
    }

    private void drawInterface()
    {
        System.out.println("Добро пожаловать в программу по шифрованию!");

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

    private void firstBranch()
    {
        switch (Validator.validateAnswer(userAnswer))
        {
            case 1 -> encryptFile();
            case 2 -> drawSecondBranch();
            case 3 -> isRunning = false;
        }
    }

    private void drawSecondBranch()
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

    private void secondBranch()
    {
        answerPool = 5;

        switch (Validator.validateAnswer(userAnswer))
        {
            case 1 -> decryptFileByKey();
            case 2 -> decryptFileByBruteForce();
            case 3 -> decryptFileByAnalytics();
            case 4 -> {}
            case 5 -> isRunning = false;
        }
    }

    private void decryptFileByKey()
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

    private void decryptFileByBruteForce()
    {

    }

    private void decryptFileByAnalytics()
    {

    }

    private void encryptFile()
    {

        List<String> data = null;
        try
        {
            data = getFile("зашифровать");
        }
        catch (InvalidFileNameException | FileIsEmptyException e)
        {
            System.out.println(e.getMessage());
            return;
        }
        int key = getKey();
        System.out.println(delimiter);
        do
        {
            String fileName = getOutputFile();
            isWritten = fileManager.writeData(fileName, encrypter.encrypt(ALPHABET, data, key));
        }
        while (!isWritten);

        System.out.println("\nФайл зашифрован!");
    }

    private List<String> getFile(String choice) throws InvalidFileNameException, FileIsEmptyException
    {
        System.out.println(delimiter);
        System.out.printf("Пожалуйста введите путь к текстовому файлу," +
                " который необходимо %s: ", choice);
        String fileName = userAnswer.nextLine();
        System.out.println();

        return fileManager.getData(fileName);
    }

    private int getKey()
    {
        System.out.print("\nПожалуйста введите ключ шифрования: ");
        return Validator.validateCipherKey(userAnswer);
    }

    private String getOutputFile()
    {
        System.out.println(delimiter);
        System.out.print("Пожалуйста введите путь к файлу, " +
                "в который необходимо записать зашифрованный текст: ");
        return userAnswer.nextLine();
    }

}
