import java.util.Arrays;

public class Alist<T> implements ListInterface<T> {
    private T[] list;
    private int numberOfEntries;
    private boolean initialized = false;
    private static final int DEFAULT_CAPACITY = 25;
    private static final int MAX_CAPACITY = 10000;

    public Alist() {
        this(DEFAULT_CAPACITY);
    }

    public Alist(int initialCapacity) {
        if (initialCapacity < DEFAULT_CAPACITY)
            initialCapacity = DEFAULT_CAPACITY;
        else
            checkCapacity(initialCapacity);

        @SuppressWarnings("unchecked")
        T[] tempList = (T[]) new Object[initialCapacity + 1];
        list = tempList;
        numberOfEntries = 0;
        initialized = true;
    }

    public void add(T newEntry) {
        checkInitialization();
        list[numberOfEntries + 1] = newEntry;
        numberOfEntries++;
        ensureCapacity();
    }

    public void add(int newPosition, T newEntry) {
        checkInitialization();
        if ((newPosition >= 1) && (newPosition <= numberOfEntries + 1)) {
            if (newPosition <= numberOfEntries)
                makeRoom(newPosition);
            list[newPosition] = newEntry;
            numberOfEntries++;
            ensureCapacity();
        } else
            throw new IndexOutOfBoundsException(
                    "Given position of add's new entry is out of bounds.");
    }

    public void addChronologicaly(T newEntry){
        if(numberOfEntries == 0){
            add(newEntry);
        }else if(numberOfEntries == 1){
            if(compareDates(list[1].toString(), newEntry.toString()) == -1){
                add(1,newEntry);
            }else{
                add(newEntry);
            }
        }else{
            boolean found = false;
            int index = 1;
            
            while (!found) {
                if(compareDates(list[index].toString(), newEntry.toString()) <= 0){
                    add(index, newEntry);
                    break;
                }
                else if (compareDates((list[index+ 1]).toString(), newEntry.toString()) >= 0){
                    add(index +1, newEntry);
                    break;
                }
                index++;
            }
        }
        
    }
    

    private int compareDates(String date1, String date2){ // returns 1 if date1 is older than date2, if newer returns -1, if equal 0
        String[] d1 = date1.split("-");
        String[] d2 = date2.split("-");
        for (int i = 0; i < 3; i++) {
           if(Integer.parseInt(d1[i]) > Integer.parseInt(d2[i])){
              return 1;
           }else if(Integer.parseInt(d1[i]) < Integer.parseInt(d2[i])){
              return -1;
           }else if(i == 2){
              return 0;
           }
        }
        return 0;
     }

    private void makeRoom(int newPosition) {
        assert (newPosition >= 1) && (newPosition <= numberOfEntries + 1);
        int newIndex = newPosition;
        int lastIndex = numberOfEntries;
        for (int index = lastIndex; index >= newIndex; index--)
            list[index + 1] = list[index];
    }

    public T remove(int givenPosition) {
        checkInitialization();
        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            assert !isEmpty();
            T result = list[givenPosition];
            if (givenPosition < numberOfEntries)
                removeGap(givenPosition);
            numberOfEntries--;
            return result;
        } else
            throw new IndexOutOfBoundsException(
                    "Illegal position given to remove operation.");
    }

    private void removeGap(int givenPosition) {
        assert (givenPosition >= 1) && (givenPosition < numberOfEntries);
        int removedIndex = givenPosition;
        int lastIndex = numberOfEntries;
        for (int index = removedIndex; index < lastIndex; index++)
            list[index] = list[index + 1];
    }

    public void clear() {
    }

    public T replace(int givenPosition, T newEntry) {
        checkInitialization();
        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            assert !isEmpty();
            T originalEntry = list[givenPosition];
            list[givenPosition] = newEntry;
            return originalEntry;
        } else
            throw new IndexOutOfBoundsException(
                    "Illegal position given to replace operation.");
    }

    public T getEntry(int givenPosition) {
        checkInitialization();
        if ((givenPosition >= 1) && (givenPosition <= numberOfEntries)) {
            assert !isEmpty();
            return list[givenPosition];
        } else
            throw new IndexOutOfBoundsException(
                    "Illegal position given to getEntry operation.");
    }

    public T[] toArray() { // ! Even though this class seems like it constructs an array, it's a list and
                           // first elements starts with arr[1].
        checkInitialization();

        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[numberOfEntries];
        for (int i = 0; i < numberOfEntries; i++) {
            result[i] = list[i + 1];
        }
        return result;
    }

    public boolean contains(T anEntry) {
        checkInitialization();
        boolean found = false;
        int index = 1;
        while (!found && (index <= numberOfEntries)) {
            if (anEntry.equals(list[index]))
                found = true;
            index++;
        } // end while
        return found;

    }

    public int getLength() {
        return numberOfEntries;
    }

    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    private void ensureCapacity() {
        int capacity = list.length - 1;
        if (numberOfEntries >= capacity) {
            int newCapacity = capacity * 2;
            checkCapacity(newCapacity);
            list = Arrays.copyOf(list, newCapacity + 1);
        }
    }

    private void checkCapacity(int capacity) {
        if (capacity > MAX_CAPACITY)
            throw new IllegalStateException("Attempt to create a list whose " +
                    "capacity exeeds allowed " +
                    "maximum of " + MAX_CAPACITY);
    }

    private void checkInitialization() {
        if (!initialized)
            throw new SecurityException("ArrayList object is not initialized " +
                    "properly.");
    }
}
