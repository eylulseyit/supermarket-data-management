import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashTable<K, V> implements DictionaryInterface<K, V> {
	private TableEntry<K, V>[] hashTable;
	private int numberOfEntries;
	private int locationsUsed;
	private static final int DEFAULT_SIZE = 101;
	private double MAX_LOAD_FACTOR = 0.5;
	private boolean isSSF = false;
	private boolean isDoubleHash = true;
	public int collision =0;

	public HashTable() {
		this(DEFAULT_SIZE);
	}

	@SuppressWarnings("unchecked")
	public HashTable(int tableSize) {
		int primeSize = getNextPrime(tableSize);
		hashTable = new TableEntry[primeSize];
		numberOfEntries = 0;
		locationsUsed = 0;
	}

	public void performanceMonitoring(double load_factor, boolean isSSF, boolean isDoubleHash){
		this.MAX_LOAD_FACTOR = load_factor;
		this.isSSF = isDoubleHash;
		this.isDoubleHash =isDoubleHash;
	}

	public int getCollision(){
		return collision;
	}

	public boolean isPrime(int num) {
		boolean prime = true;
		for (int i = 2; i <= num / 2; i++) {
			if ((num % i) == 0) {
				prime = false;
				break;
			}
		}
		return prime;
	}

	public int getNextPrime(int num) {
		if (num <= 1)
			return 2;
		else if (isPrime(num))
			return num;
		boolean found = false;
		while (!found) {
			num++;
			if (isPrime(num))
				found = true;
		}
		return num;
	}

	public V add(K key, V value) {
		V oldValue;
		int index = getHashIndex(key);
		index = probe(index, key);

		if ((hashTable[index] == null) || hashTable[index].isRemoved()) {
			hashTable[index] = new TableEntry<K, V>(key, value);
			numberOfEntries++;
			locationsUsed++;
			oldValue = null;
		} else {
			oldValue = hashTable[index].getValue();
			hashTable[index].setValue(value);
		}
		if (isHashTableTooFull())
			rehash();
		return oldValue;
	}
	
	private int probe(int index, K key) {
		boolean found = false;
		int removedStateIndex = -1;
		int length = hashTable.length;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn()) {
				if (key.equals(hashTable[index].getKey()))
					found = true;
				else{
					if(!isDoubleHash)
						index = (index + 1) % length;
					else{
						index = (index + doubleHashing(key)) % length;
					}
					collision++;
				}
			} else {
				if (removedStateIndex == -1)
					removedStateIndex = index;
				index = (index + 1) % length;
			}
		}
		if (found || (removedStateIndex == -1))
			return index;
		else
			return removedStateIndex;
	}


	private int locate(int index, K key) {
		boolean found = false;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn() && key.equals(hashTable[index].getKey()))
				found = true;
			else{
					if(!isDoubleHash)
						index = (index + 1) % hashTable.length;
					else{
						index = (index + doubleHashing(key)) % hashTable.length;
					}
				}
		}
		int result = -1;
		if (found)
			result = index;
		return result;
	}

	private int getHashIndex(K key) {
		int hashIndex;
		if(isSSF)
			hashIndex = simpleSum((String)key) % hashTable.length;
		else{
			hashIndex = polyAcc((String)key) % hashTable.length;
		}
		if (hashIndex < 0)
			hashIndex = hashIndex + hashTable.length;
		return hashIndex;
	}

	private int doubleHashing(K key){
		int hashIndex = getHashIndex(key);
		return 31*(17- (hashIndex % 17));
	}

	private int simpleSum(String key){
		int sum = 0;
		int length = key.length();
		for (int i = 0; i < length; i++) {
			sum += key.charAt(i);
		}
		return sum;
	}

	private int polyAcc(String key){
		int sum = 0;
		int length = key.length();
		for (int i = 0; i < length; i++) {
			for (int j = length; j > 0; j--) {
				sum += (key.charAt(i) * (Math.pow(13,j-1)) % hashTable.length) ;
			}
		}
		return sum;
	}

	public boolean isHashTableTooFull() {
		boolean flag = (double) numberOfEntries / hashTable.length >= MAX_LOAD_FACTOR;
		return flag;
	}

	@SuppressWarnings("unchecked")
	public void rehash() {
		TableEntry<K, V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(2 * oldSize);
		hashTable = new TableEntry[newSize];
		numberOfEntries = 0;
		locationsUsed = 0;

		for (int index = 0; index < oldSize; index++) {

			if ((oldTable[index] != null) && oldTable[index].isIn()) {
				add(oldTable[index].getKey(), oldTable[index].getValue());
			}

		}
	}


	public V remove(K key) {

		V removedValue = null;
		int index = getHashIndex(key);
		index = locate(index, key);

		if (index != -1) {
			removedValue = hashTable[index].getValue();
			hashTable[index].setToRemoved();
			numberOfEntries--;
		}
		return removedValue;
	}


	public V getValue(K key) {
		V result = null;
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1)
			result = hashTable[index].getValue();
		return result;
	}

	public boolean contains(K key) {
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1){
			
			return true;
		}
		else{
			return false;
		}
	}

	public boolean isEmpty() {
		return numberOfEntries == 0;
	}

	public int getSize() {
		return numberOfEntries;
	}

	public void clear() {
		while (getKeyIterator().hasNext()) {
			remove(getKeyIterator().next());
		}
	}

	public Iterator<K> getKeyIterator() {
		return new KeyIterator();
	}

	public Iterator<V> getValueIterator() {
		return new ValueIterator();
	}

	private class TableEntry<S, T> {
		private S key;
		private T value;
		private boolean inTable;

		private TableEntry(S key, T value) {
			this.key = key;
			this.value = value;
			inTable = true;
		}

		private S getKey() {
			return key;
		}

		private T getValue() {
			return value;
		}

		private void setValue(T value) {
			this.value = value;
		}

		private boolean isRemoved() {
			return inTable == false;
		}

		private void setToRemoved() {
			inTable = false;
		}

		private boolean isIn() {
			return inTable == true;
		}
	}

	private class KeyIterator implements Iterator<K> {
		private int currentIndex;
		private int numberLeft;

		private KeyIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		}

		public boolean hasNext() {
			return numberLeft > 0;
		}

		public K next() {
			K result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				}
				result = hashTable[currentIndex].getKey();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private class ValueIterator implements Iterator<V> {
		private int currentIndex;
		private int numberLeft;

		private ValueIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		}

		public boolean hasNext() {
			return numberLeft > 0;
		}

		public V next() {
			V result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				}
				result = hashTable[currentIndex].getValue();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}