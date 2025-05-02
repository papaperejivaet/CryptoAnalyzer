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

/**
 * Главный класс приложения, реализующий интерфейс для шифрования и расшифровки файлов с помощью различных методов.
 * Пользователь может выбрать между шифрованием файла, расшифровкой с использованием ключа, расшифровкой перебором,
 * или статистическим методом аналитической расшифровки.
 */
public class MainApp
{
    /** Алфавит, используемый для шифрования и расшифровки. */
    public static final List<Character> ALPHABET = Arrays.asList('а', 'б',
            'в','г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', 'А', 'Б',
            'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У',
            'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', ':', '!', '?', ' ', ';', '-');

    /** Сканер для получения ответов пользователя. */
    private static final Scanner userAnswer = new Scanner(System.in);

    /** Объект для работы с файлами. */
    private static final FileManager fileManager = new FileManager();

    /** Объекты для шифрования и расшифровки. */
    private static final Encrypter encrypter = new Encrypter();
    private static final Decrypter decrypter = new Decrypter(ALPHABET);
    private static final DecrypterByBruteForce decrypterByBruteForce = new DecrypterByBruteForce(ALPHABET);
    private static final DecrypterByAnalytics decrypterByAnalytics = new DecrypterByAnalytics(ALPHABET);

    /** Разделитель для визуального разделения в выводе программы. */
    private static final String DELIMITER = "*".repeat(50);

    /** Флаг работы программы. */
    private static boolean isRunning = true;

    /** Флаг записи в файл. */
    private static boolean isWritten;

    /** Строки для вывода состояния шифрования и расшифровки. */
    private static final String DECRYPT = "расшифровать";
    private static final String ENCRYPT = "зашифровать";
    private static final String DECRYPTED = "расшифрован";
    private static final String ENCRYPTED = "зашифрован";
    private static final String REPRESENTATIVE = " репрезентативный";

    /**
     * Основной метод программы, который запускает "отрисовку" интерфейса.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args)
    {
        drawInterface();
    }


    /**
     * Метод для отображения главного интерфейса программы и обработки выбора пользователя.
     */
    private static void drawInterface()
    {
        System.out.println("\nДобро пожаловать в программу по шифрованию!\n");

        while (isRunning)
        {
            isWritten = false;
            System.out.println(DELIMITER);
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
        System.out.println(DELIMITER);
        System.out.println("Спасибо за использование программы!");
    }

    /**
     * Метод для отображения интерфейса для расшифровки файла.
     */
    private static void drawSecondBranch()
    {

        System.out.println(DELIMITER);
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

    /**
     * Метод для обработки первого выбора пользователя (шифрование или расшифровка).
     */
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

    /**
     * Метод для обработки второго выбора пользователя (методы расшифровки).
     */
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

    /**
     * Метод для шифрования файла с использованием ключа.
     */
    public static void encryptFile()
    {

        List<String> data = null;
        int key = 0;

        try
        {
            data = receiveFile("",ENCRYPT);
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

    /**
     * Метод для расшифровки файла по ключу.
     */
    public static void decryptFileByKey()
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

    /**
     * Метод для расшифровки файла способом "грубого перебора".
     */
    public static void decryptFileByBruteForce()
    {
        List<String> encryptedData;
        List<String> representativeData;
        List<List<String>> decryptedData;
        try
        {
            encryptedData = receiveFile("", DECRYPT);
            representativeData = receiveFile(REPRESENTATIVE, "использовать");
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

        checkIfMultipleAndSend(decryptedData);
    }

    /**
     * Метод для расшифровки файла с использованием аналитического способа.
     */
    public static void decryptFileByAnalytics()
    {
        System.out.println(DELIMITER);
        System.out.println("""
                      Предупреждение:
        Метод расшифровки, основанный на статистическом
              анализе, может быть некорректным!""");
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

        checkIfMultipleAndSend(decryptedData);

    }

    /**
     * Метод для получения файла, который необходимо зашифровать или расшифровать.
     * Если необходим репрезентативный текст, то первым параметром передать
     * поле {@code REPRESENTATIVE}. Если нет - пустую строку.
     * Если необходимо зашифровать, то вторым параметром передать поле {@code ENCRYPT}, если
     * расшифровать - поле {@code DECRYPT}.
     * @param representative Необходим репрезентативный текст.
     * @param choice Операция с файлом ("зашифровать" или "расшифровать").
     * @return Содержимое файла в виде списка строк.
     * @throws InvalidFileNameException Если имя файла некорректно.
     * @throws FileIsEmptyException Если файл пустой.
     */
    private static List<String> receiveFile(String representative, String choice) throws InvalidFileNameException, FileIsEmptyException
    {
        System.out.println(DELIMITER);
        System.out.printf("""
                Пожалуйста введите путь к%s текстовому файлу,
                который необходимо %s:
                """, representative, choice);
        String fileName = userAnswer.nextLine();
        return fileManager.getData(fileName);
    }

    /**
     * Метод для получения ключа шифрования от пользователя.
     *
     * @return Ключ шифрования.
     * @throws InvalidCipherKeyException Если ключ некорректен.
     */
    private static int receiveKey() throws InvalidCipherKeyException
    {
        System.out.println(DELIMITER);
        System.out.println("Пожалуйста введите ключ шифрования:");
        return Validator.validateCipherKey(userAnswer.nextLine());
    }

    /**
     * Метод для получения пути к файлу для записи результата.
     *
     * @return Путь к файлу.
     */
    private static String receiveOutputFileName()
    {
        System.out.println(DELIMITER);
        System.out.print("""
                Пожалуйста введите путь к файлу,
                в который необходимо записать текст, или
                "exit" чтобы выйти из программы:
                """);
        return userAnswer.nextLine();
    }

    /**
     * Метод для записи данных в файл.
     * Если файл был расшифрован, то вторым параметром передать поле {@code ENCRYPTED},
     * если зашифрован - {@code DECRYPTED}.
     * @param data Данные для записи.
     * @param choice Статус файла (например, "зашифрован" или "расшифрован").
     */
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

    /**
     * Метод для записи данных в файл с заданным именем.
     * Если файл был расшифрован, то вторым параметром передать поле {@code ENCRYPTED},
     * если зашифрован - {@code DECRYPTED}.
     * @param data Данные для записи.
     * @param choice Статус файла (например, "зашифрован" или "расшифрован").
     * @param fileName Имя файла.
     */
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

    /**
     * Метод для записи нескольких вариантов расшифровки в файл.
     *
     * @param decryptedData Данные для записи.
     * @param fileName Имя файла.
     */
    private static void sendMultipleDataToFile(List<List<String>> decryptedData, String fileName)
    {
        List<String> delimiter = Collections.singletonList(DELIMITER + "\n");

        for (List<String> variation : decryptedData)
        {
            fileManager.writeData(fileName, variation);
            fileManager.writeData(fileName, delimiter);
        }
    }


    /**
     * Проверяет, содержит ли список несколько вариантов расшифровки и сохраняет данные в файл.
     * Если в списке только один элемент, то данные записываются в файл без разделителей.
     * Если элементов несколько, каждый вариант записывается в файл с разделителями между ними.
     *
     * @param decryptedData Список с вариантами расшифровки данных. Каждый элемент списка представляет собой отдельную строку или блок данных.
     */
    private static void checkIfMultipleAndSend(List<List<String>> decryptedData)
    {
        String fileName = receiveOutputFileName();
        if (decryptedData.size() == 1)
        {
            sendDataToFile(decryptedData.get(0), DECRYPTED, fileName);
        }
        else
        {
            sendMultipleDataToFile(decryptedData, fileName);
        }
    }
}
