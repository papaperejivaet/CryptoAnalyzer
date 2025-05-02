package validation;

import exceptions.FileIsEmptyException;
import exceptions.InvalidCipherKeyException;
import exceptions.InvalidFileNameException;
import exceptions.answer_exceptions.NegativeAnswerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Класс Validator предоставляет статические методы для валидации различных входных данных,
 * таких как имена файлов, ключи шифрования и ответы пользователя.
 * Все методы выбрасывают соответствующие исключения в случае неверных данных.
 */
public class Validator
{
    // Конструктор приватный, так как класс содержит только статические методы.
    private Validator()
    {
    }

    /**
     * Метод для проверки имени файла.
     * Проверяет, что имя файла не null, не пустое и что файл существует в указанном пути.
     *
     * @param fileName Имя файла для проверки.
     * @throws InvalidFileNameException если имя файла некорректно или файл не существует.
     */
    public static void validateFileName(String fileName) throws InvalidFileNameException
    {
        if (fileName == null || fileName.isEmpty() || !Files.exists(Path.of(fileName)))
        {
            throw new InvalidFileNameException();
        }

    }

    /**
     * Метод для валидации ключа шифра.
     * Проверяет, что ключ является числом.
     *
     * @param key Ключ шифрования в виде строки.
     * @return Целочисленный ключ шифрования.
     * @throws InvalidCipherKeyException если строка не является числом.
     */
    public static int validateCipherKey(String key) throws InvalidCipherKeyException
    {
        try
        {
            return validateNumeric(key);
        }
        catch (NumberFormatException e)
        {
            throw new InvalidCipherKeyException("Введено не целое число");
        }

    }

    /**
     * Метод для валидации размера файла.
     * Проверяет, что файл не пустой, и выбрасывает исключение, если его размер равен нулю.
     *
     * @param fileName Имя файла для проверки.
     * @throws FileIsEmptyException если файл пустой.
     */
    public static void validateFileSize(String fileName) throws FileIsEmptyException
    {
        try
        {
            if (Files.size(Path.of(fileName)) == 0)
            {
                throw new FileIsEmptyException();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Файл не найден!");
        }
    }

    /**
     * Метод для валидации ответа пользователя.
     * Проверяет, что ответ является положительным числом.
     * При неверном вводе выводит сообщение в консоль
     *
     * @param userAnswer Ответ пользователя в виде строки.
     * @return Числовой ответ пользователя.
     *
     */
    public static int validateAnswer(String userAnswer)
    {

        int choice = 0;

            try
            {
                choice = validateNumeric(userAnswer);
                if (choice < 0)
                {
                    throw new NegativeAnswerException();
                }
            }
            catch (NegativeAnswerException e)
            {
                System.out.println(e.getMessage());
            }
            catch (NumberFormatException e)
            {
                System.out.println("Введено не целое число!");
            }



        return choice;
    }

    /**
     * Приватный метод для проверки, что строка представляет собой целое число.
     *
     * @param userAnswer Строка, содержащая предполагаемое число.
     * @return Числовое значение из строки.
     * @throws InvalidCipherKeyException если строка не является целым числом.
     */
    private static int validateNumeric(String userAnswer) throws NumberFormatException
    {
        return Integer.parseInt(userAnswer);
    }
}
