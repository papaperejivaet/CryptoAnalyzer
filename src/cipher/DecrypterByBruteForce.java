package cipher;

import exceptions.NoCoincidenceException;

import java.util.*;

public class DecrypterByBruteForce
{
    private final Encrypter encrypter = new Encrypter();

    public List<List<String>> decrypt(List<Character> alphabet, List<String> encryptedData, List<String> representativeData) throws NoCoincidenceException
    {
        List<List<String>> variationsList = new ArrayList<>();
        List<Set<String>> comparasionList = new ArrayList<>();
        int key = 0;
        Map<String, Integer> wordFrequency;
        while (key < alphabet.size())
        {
            variationsList.add(encrypter.encrypt(alphabet, encryptedData, key));
            wordFrequency = new HashMap<>();

//            for (String line : variationsList.get(key))
//            {
//                countWordFrequency(separate(line), wordFrequency);
//            }
            countWordFrequency(separate(variationsList.get(key)), wordFrequency);
            comparasionList.add(sortMapByValue(wordFrequency));    //Положить в лист 5 key по самым большим values из wordFrequency

            key++;
        }

        wordFrequency = new HashMap<>();
        countWordFrequency(separate(representativeData), wordFrequency);


        return returnResult(comparasionList, variationsList, sortMapByValue(wordFrequency));
    }


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

        for (int i = 0; i < 5; i++)
        {
            sortedSet.add(sortingList.get(i).getKey());
        }

        return sortedSet;
    }

    private List<Integer> getCypherKey(List<Set<String>> comparasionList, Set<String> representativeSet) throws NoCoincidenceException
    {
        List<Set<String>> replicaList = new ArrayList<>(comparasionList);
        Set<String> referenceSet = new HashSet<>(representativeSet);
        List<Integer> bestKeys = new ArrayList<>();
        int differences = 5;

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
        for (int i = 0; i < replicaList.size(); i++)
        {
            if (replicaList.get(i).size() == differences)
            {
                bestKeys.add(i);
            }
        }

        return bestKeys;
    }

    private List<List<String>> returnResult(List<Set<String>> comparasionList, List<List<String>> variationsList, Set<String> representativeSet) throws NoCoincidenceException
    {
        List<Integer> keys = getCypherKey(comparasionList, representativeSet);
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
