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
    public static int validateCipherKey(Scanner key)
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

    public static int validateAnswer(Scanner userAnswer)
    {
        boolean isWrong = true;
        int choice = 0;

        while (isWrong)
        {
            choice = validateNumeric(userAnswer);
            try
            {
                if (choice < 0)
                {
                    throw new NegativeAnswerException();
                }
                else if (choice == 0 || choice > MainApp.answerPool)
                {
                    throw new IncorrectAnswerException();
                }
                isWrong = false;
            }
            catch (NegativeAnswerException | IncorrectAnswerException e)
            {
                System.out.println(e.getMessage());
            }
        }


        return choice;
    }

    private static int validateNumeric(Scanner userAnswer)
    {
        boolean isNumber = false;
        int choice = 0;

        while (!isNumber)
        {
            try
            {
                choice = Integer.parseInt(userAnswer.nextLine());
                isNumber = true;
            }
            catch (NumberFormatException e)
            {
                System.out.print("\nПожалуйста, введите целое число: ");
            }
        }
        return choice;
    }
}
