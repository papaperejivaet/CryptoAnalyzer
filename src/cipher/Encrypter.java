package cipher;

import java.util.*;

public class Encrypter
{

    /**
     * Шифрует список строк с использованием символьного алфавита и сдвига (шифр Цезаря).
     * Для каждой строки из списка {@code data} метод заменяет символы, найденные в списке {@code alphabet},
     * на символы, сдвинутые на {@code key} позиций вправо. При отрицательном значении {@code key} сдвиг произойдет влево.
     * Символы, которых нет в алфавите, остаются без изменений.
     *
     * @param alphabet список допустимых символов, по которому выполняется шифрование
     * @param data список строк, подлежащих шифрованию
     * @param key значение сдвига для шифра
     * @return список зашифрованных строк
     */
    public List<String> encrypt(List<Character> alphabet, List<String> data, int key)
    {
        List<String> result = new ArrayList<>();
        StringBuilder resultLine;

        for (String line : data)
        {
            resultLine = new StringBuilder();
            int letterIndex;

            for (char letter : line.toCharArray())
            {
                letterIndex = alphabet.indexOf(letter);

                if (letterIndex != -1)
                {
                    resultLine.append(shift(letterIndex, key, alphabet));
                }
                else
                {
                    resultLine.append(letter);
                }
            }

            result.add(resultLine.toString());
        }

        return result;
    }


    /**
     * Выполняет циклический сдвиг символа по алфавиту на заданное количество позиций.
     *
     * @param letterNumber индекс символа в алфавите, который нужно сдвигать
     * @param key количество позиций, на которые нужно сдвигать символ
     * @param alphabet список символов (алфавит), в пределах которого выполняется сдвиг
     * @return символ, который получается после сдвига
     */
    private char shift(int letterNumber, int key, List<Character> alphabet)
    {
        int size = alphabet.size();
        return alphabet.get(((letterNumber + key) % size + size) % size);
    }

}
