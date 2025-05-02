package cipher;

import exceptions.NoCoincidenceException;

import java.util.*;
/**
 * Класс DecrypterByBruteForce выполняет расшифровку данных с использованием метода "грубого перебора",
 * где для каждого возможного сдвига вычисляется частотный анализ слов в репрезентативном тексте
 * и осуществляется поиск совпадений.
 */
public class DecrypterByBruteForce
{
    /**
     * Экземпляр класса Encrypter, используемый для выполнения операции дешифрования.
     */
    private final Encrypter encrypter = new Encrypter();

    /**
     * Список символов алфавита, используемого для шифрования и дешифрования.
     */
    private final List<Character> alphabet;

    /**
     * Количество наиболее часто встречающихся слов, используемых для сравнения.
     */
    private static final int COINCIDENCE = 10;

    /**
     * Конструктор класса DecrypterByBruteForce.
     *
     * @param alphabet Список символов алфавита, используемого для дешифрования.
     */
    public DecrypterByBruteForce(List<Character> alphabet)
    {
        this.alphabet = alphabet;
    }

    /**
     * Метод для расшифровки данных с использованием метода "грубого перебора" и анализа совпадений.
     * Для каждого ключа (сдвига) производится расшифровка данных и анализ частоты слов.
     * Затем на основе частотных характеристик производится выбор подходящих вариантов расшифровки.
     *
     * @param encryptedData Список строк с зашифрованными данными.
     * @param representativeData Список строк с представительными данными для анализа.
     * @return Список вариантов расшифровок, которые имеют наибольшее совпадение с репрезентативными данными.
     * @throws NoCoincidenceException если не найдено совпадений при расшифровке.
     */
    public List<List<String>> decrypt(List<String> encryptedData, List<String> representativeData) throws NoCoincidenceException
    {
        List<List<String>> variationsList = new ArrayList<>();
        List<Set<String>> comparasionList = new ArrayList<>();
        int key = alphabet.size();
        Map<String, Integer> wordFrequency;
        Map<String, Integer> representativeWordFrequency;

        // Перебор всех возможных ключей (сдвигов).
        while (key > 0)
        {
            variationsList.add(encrypter.encrypt(alphabet, encryptedData, key));
            wordFrequency = new HashMap<>();


            countWordFrequency(separate(variationsList.get(alphabet.size() - key)), wordFrequency);
            comparasionList.add(sortMapByValue(wordFrequency));

            key--;
        }

        // Подсчет частоты слов в расшифрованных данных.
        representativeWordFrequency = new HashMap<>();
        countWordFrequency(separate(representativeData), representativeWordFrequency);

        // Возврат результата с наиболее подходящими вариантами.
        return returnResult(comparasionList, variationsList, sortMapByValue(representativeWordFrequency));
    }

    /**
     * Разделяет строки на отдельные слова, используя разделители.
     *
     * @param list Список строк для разделения.
     * @return Список слов.
     */
    private List<String> separate(List<String> list)
    {
        List<String> dividedLine = new LinkedList<>();
        for (String line : list)
        {

            StringTokenizer separator = new StringTokenizer(line, " ,.!?");

            while (separator.hasMoreTokens())
            {
                dividedLine.add(separator.nextToken());
            }
        }
        return dividedLine;
    }

    /**
     * Подсчитывает частоту встречаемости слов в списке и записывает
     * результат в переданную мапу.
     *
     * @param words Список слов для подсчета частоты.
     * @param wordFrequency Мапа для хранения частоты каждого слова.
     */
    private void countWordFrequency(List<String> words, Map<String, Integer> wordFrequency)
    {
        int frequency;
        String word;

        for (int i = 0; i < words.size(); i++)
        {
            word = words.get(i);
            frequency = Collections.frequency(words, word);
            wordFrequency.put(word, frequency);
            words.removeAll(Collections.singletonList(word));
            i--;
        }
    }

    /**
     * Сортирует мапу по значению (по убыванию) и возвращает сет из наиболее часто встречающихся слов.
     *
     * @param map Мапа, содержащая слова и их частоты.
     * @return Сет наиболее часто встречающихся слов.
     */
    private Set<String> sortMapByValue(Map<String, Integer> map)
    {
        List<Map.Entry<String, Integer>> sortingList = new ArrayList<>(map.entrySet());
        sortingList.sort(new Comparator<Map.Entry<String, Integer>>()
        {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
            {
                return o2.getValue() - o1.getValue();
            }
        });

        Set<String> sortedSet = new HashSet<>();

        // Возвращаем только COINCIDENCE наиболее часто встречающихся слов.
        for (int i = 0; i < COINCIDENCE; i++)
        {
            sortedSet.add(sortingList.get(i).getKey());
        }

        return sortedSet;
    }

    /**
     * Находит ключи (сдвиги), которые дают наибольшее совпадение между расшифрованными данными и представительными данными.
     *
     * @param comparisonList Список сетов наиболее часто встречающихся слов для каждого сдвига.
     * @param representativeSet Сет наиболее часто встречающихся слов для представительных данных.
     * @return Список индексов сдвигов, которые дали наибольшее совпадение.
     * @throws NoCoincidenceException если не найдено совпадений.
     */
    private List<Integer> getCypherKey(List<Set<String>> comparisonList, Set<String> representativeSet) throws NoCoincidenceException
    {
        List<Set<String>> replicaList = new ArrayList<>(comparisonList);
        Set<String> referenceSet = new HashSet<>(representativeSet);
        List<Integer> bestKeys = new ArrayList<>();
        int differences = 5;

        // Подсчитываем количество различий между наборами.
        for (Set<String> suspectSet : replicaList)
        {
            suspectSet.removeAll(referenceSet);
            int size = suspectSet.size();
            if (size < differences)
            {
                differences = size;
            }
        }

        if (differences == 5)
        {
            throw new NoCoincidenceException();
        }

        // Собираем индексы с минимальным количеством различий.
        for (int i = 0; i < replicaList.size(); i++)
        {
            if (replicaList.get(i).size() == differences)
            {
                bestKeys.add(i);
            }
        }

        return bestKeys;
    }

    /**
     * Возвращает результат расшифровки, учитывая наиболее подходящие ключи.
     *
     * @param comparisonList Список наборов наиболее часто встречающихся слов для каждого сдвига.
     * @param variationsList Список вариантов расшифрованных данных для каждого сдвига.
     * @param representativeSet Набор наиболее часто встречающихся слов для представительных данных.
     * @return Список возможных расшифровок.
     * @throws NoCoincidenceException если не найдено совпадений.
     */
    private List<List<String>> returnResult(List<Set<String>> comparisonList, List<List<String>> variationsList, Set<String> representativeSet) throws NoCoincidenceException
    {
        List<Integer> keys = getCypherKey(comparisonList, representativeSet);
        List<List<String>> results = new ArrayList<>();
        if (keys.size() == 1)
        {
            results.add(variationsList.get(keys.getFirst()));
        }
        else
        {
            for (Integer key : keys)
            {
                results.add(variationsList.get(key));
            }
        }

        return results;
    }
}
