package validation;

import exceptions.FileIsEmptyException;
import exceptions.InvalidCipherKeyException;
import exceptions.InvalidFileNameException;
import exceptions.answer_exceptions.IncorrectAnswerException;
import exceptions.answer_exceptions.NegativeAnswerException;
import model.MainApp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Validator
{
    private Validator()
    {
    }

    public static void validateFileName(String fileName) throws InvalidFileNameException
    {
        if (fileName == null || fileName.isEmpty() || !Files.exists(Path.of(fileName)))
        {
            throw new InvalidFileNameException();
        }

    }

//Валидирует ключ шифра. Отсеивает пустые строки и null, проверяет является ли строка числом.
    public static int validateCipherKey(String key) throws InvalidCipherKeyException
    {
        return validateNumeric(key);
    }

    //Валидирует размер файла. Если пустой - бросает исключение
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
                else if (choice == 0 || choice > MainApp.answerPool)
                {
                    throw new IncorrectAnswerException();
                }
            }
            catch (NegativeAnswerException | IncorrectAnswerException | InvalidCipherKeyException e)
            {
                System.out.println(e.getMessage());
            }



        return choice;
    }

    private static int validateNumeric(String userAnswer) throws InvalidCipherKeyException
    {
        int choice = 0;

            try
            {
                choice = Integer.parseInt(userAnswer);

            }
            catch (NumberFormatException e)
            {
                throw new InvalidCipherKeyException("Введено не целое число");
            }

        return choice;
    }
}
