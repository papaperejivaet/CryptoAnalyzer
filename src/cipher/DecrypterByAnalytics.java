package cipher;

import exceptions.NoCoincidenceException;

import java.util.*;

/**
 * Класс DecrypterByAnalytics выполняет расшифровку данных с использованием аналитического метода,
 * который основывается на сравнении частоты букв в шифрованном тексте с известными частотами букв в русском языке.
 */
public class DecrypterByAnalytics
{
    /**
     * Статистические данные о частоте букв и некоторых символов в русском языке.
     */
    private static final Map<Character, Double> RUSSIAN_FREQUENCIES = Map.ofEntries(
            Map.entry('а', 8.66),
            Map.entry('б', 1.59),
            Map.entry('в', 4.54),
            Map.entry('г', 1.70),
            Map.entry('д', 2.98),
            Map.entry('е', 8.72),
            Map.entry('ё', 0.13),
            Map.entry('ж', 0.94),
            Map.entry('з', 1.65),
            Map.entry('и', 7.34),
            Map.entry('й', 1.21),
            Map.entry('к', 3.49),
            Map.entry('л', 4.40),
            Map.entry('м', 3.21),
            Map.entry('н', 6.97),
            Map.entry('о', 9.28),
            Map.entry('п', 2.81),
            Map.entry('р', 4.73),
            Map.entry('с', 5.47),
            Map.entry('т', 6.26),
            Map.entry('у', 2.62),
            Map.entry('ф', 0.26),
            Map.entry('х', 0.97),
            Map.entry('ц', 0.48),
            Map.entry('ч', 1.48),
            Map.entry('ш', 0.73),
            Map.entry('щ', 0.36),
            Map.entry('ъ', 0.04),
            Map.entry('ы', 1.90),
            Map.entry('ь', 1.74),
            Map.entry('э', 0.32),
            Map.entry('ю', 0.64),
            Map.entry('я', 2.01),
            Map.entry(' ', 15.0),
            Map.entry(',', 4.0),
            Map.entry('.', 3.0)

    );

    /**
     * Список символов алфавита, используемый для шифрования и дешифрования.
     */
    private final List<Character> alphabet;

    /**
     * Экземпляр класса Encrypter, используемый для выполнения операции дешифрования.
     */
    private static final Encrypter encrypter = new Encrypter();

    /**
     * Конструктор класса DecrypterByAnalytics.
     *
     * @param alphabet Список символов алфавита, используемого для дешифрования.
     */
    public DecrypterByAnalytics(List<Character> alphabet)
    {
        this.alphabet = alphabet;
    }

    /**
     * Метод для расшифровки данных с использованием аналитического подхода на основе частоты букв.
     * Метод создает вариации шифрования для каждого возможного сдвига и вычисляет отклонение от статистической частоты
     * букв в русском языке. Затем выбираются наиболее подходящие варианты расшифровки.
     *
     * @param encryptedData Список строк с зашифрованными данными.
     * @return Список расшифрованных вариантов данных.
     * @throws NoCoincidenceException если не удалось найти подходящий вариант расшифровки.
     */
    public List<List<String>> decrypt(List<String> encryptedData) throws NoCoincidenceException
    {
        Map<Character, Integer> currentValues = valuesForCurrentText(countTotalCharacters(encryptedData));
        List<List<String>> variations = createVariations(encryptedData);
        List<List<String>> decryptedData = new ArrayList<>();
        Map<Integer, Integer> deviationSquareSum = deviationSquareSumMap(currentValues, variations);
        int minSum = findClosestToZero(deviationSquareSum);

        // Добавление вариантов, у которых отклонения минимальны.
        for (Map.Entry<Integer, Integer> entry : deviationSquareSum.entrySet())
        {
            int sum = entry.getValue();

            if (sum == minSum)
            {
                List<String> variation = variations.get(entry.getKey());

                decryptedData.add(variation);
            }
        }

        return decryptedData;
    }

    /**
     * Метод находит минимальное отклонение среди всех вариантов расшифровок.
     *
     * @param map Мапа, где ключи — индексы вариантов расшифровки, а значения — суммы квадратов отклонений.
     * @return Минимальное отклонение.
     */
    private int findClosestToZero(Map<Integer, Integer> map)
    {
        int minSum = Integer.MAX_VALUE;

        for (int value : map.values())
        {
            if (Math.abs(value) < minSum)
            {
                minSum = value;

            }
        }

        return minSum;
    }

    /**
     * Подсчитывает общее количество символов в списке.
     *
     * @param data Список строк с данными.
     * @return Общее количество символов в данных.
     */
    private int countTotalCharacters(List<String> data)
    {
        int count = 0;
        for (String line : data)
        {
            count += line.length();
        }
        return count;
    }

    /**
     * Метод для добавления суммы квадратов отклонений для каждого варианта расшифровки в мапу.
     *
     * @param currentValues Частоты букв в исходном тексте.
     * @param variations Список вариантов расшифрованных данных.
     * @return Карта, где ключи — номера вариантов, а значения — сумма квадратов отклонений.
     * @throws NoCoincidenceException если не найдено совпадений в частотах символов.
     */
    private Map<Integer, Integer> deviationSquareSumMap(Map<Character, Integer> currentValues, List<List<String>> variations) throws NoCoincidenceException
    {
        Map<Integer, Integer> deviationSquareSum = new HashMap<>();
        int variationNumber = 0;

        // Вычисление суммы квадратов отклонений для каждого варианта.
        for (List<String> variation : variations)
        {
            int sum = calculateDeviationSquareSum(currentValues, getLettersCount(variation));
            deviationSquareSum.put(variationNumber, sum);
            variationNumber++;
        }

        return deviationSquareSum;

    }

    /**
     * Метод для вычисления суммы квадратов отклонений частоты символов между мапой частот в русском языке и в текущем варианте.
     *
     * @param statisticLettersCount Статистическая мапа частот символов.
     * @param currentLettersCount Мапа частот символов текущего варианта.
     * @return Сумма квадратов отклонений частот символов.
     * @throws NoCoincidenceException если не найдено совпадений в частотах символов.
     */
    private int calculateDeviationSquareSum(Map<Character, Integer> statisticLettersCount, Map<Character, Integer> currentLettersCount) throws NoCoincidenceException
    {

        int sum = 0;
        int check = 0;
        int deviation = 0;


        for (Map.Entry<Character, Integer> entry : currentLettersCount.entrySet())
        {
            Character lowLetter = Character.toLowerCase(entry.getKey());

            if (statisticLettersCount.containsKey(lowLetter))
            {
                deviation = currentLettersCount.get(lowLetter) - statisticLettersCount.get(lowLetter);
                check++;
            }

            sum += deviation * deviation;
        }

        if (check == 0)
        {
            throw new NoCoincidenceException();
        }
        else
        {
            return sum;
        }
    }

    /**
     * Метод для подсчета частоты символов в тексте.
     *
     * @param data Список строк для подсчета частоты символов.
     * @return Мапа частот символов.
     */
    private Map<Character, Integer> getLettersCount(List<String> data)
    {
        Map<Character, Integer> lettersCount = new HashMap<>(); //Было TreeMap

        for (String line : data)
        {
            putLetters(lettersCount, line);
        }

        return lettersCount;

    }

    /**
     * Метод для добавления символов в мапу частоты символов.
     *
     * @param lettersCount Мапа для подсчета частоты символов.
     * @param line Строка для обработки.
     */
    private void putLetters(Map<Character, Integer> lettersCount, String line)
    {
        Character[] letters = toCharacterArray(line);
        for (Character letter : letters)
        {
            if (alphabet.contains(letter))
            {
                letter = Character.toLowerCase(letter);
                lettersCount.put(letter, lettersCount.getOrDefault(letter, 0) + 1);
            }
        }
    }

    /**
     * Преобразует строку в массив символов Character.
     *
     * @param line Строка для преобразования.
     * @return Массив символов.
     */
    private Character[] toCharacterArray(String line)
    {
        Character[] characters = new Character[line.length()];

        for (int i = 0; i < line.length(); i++)
        {
            characters[i] = line.charAt(i);
        }

        return characters;
    }

    /**
     * Метод для подсчета статистической частоты символов в тексте.
     *
     * @param textSize Общая длина текста.
     * @return Мапа частоты символов для текущего текста.
     */
    private Map<Character, Integer> valuesForCurrentText(int textSize)
    {
        Map<Character, Integer> currentValues = new HashMap<>();//было TreeMap

        for (Map.Entry<Character, Double> entry : RUSSIAN_FREQUENCIES.entrySet())
        {
            currentValues.put(entry.getKey(), (int) Math.round(((double) textSize / 100) * entry.getValue()));
        }
        return currentValues;
    }

    /**
     * Метод для создания всех возможных вариантов расшифровки текста.
     *
     * @param data Список строк данных для шифрования.
     * @return Список возможных вариантов расшифровки.
     */
    private List<List<String>> createVariations(List<String> data)
    {
        List<List<String>> variations = new ArrayList<>();
        int key = alphabet.size();

        while (key > 0)
        {
            variations.add(encrypter.encrypt(alphabet, data, key));
            key--;
        }
        return variations;
    }

}


