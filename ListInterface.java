public interface ListInterface<T> {

    public void add(T newEntry);

    public void add(int newPosition, T newEntry);

    /** Removes the entry at a given position from this list.
    Entries originally at positions higher than the given
    position are at the next lower position within the list,
    and the list’s size is decreased by 1.
    @param givenPosition An integer that indicates the position of
    the entry to be removed.
    @return A reference to the removed entry.
    @throws IndexOutOfBoundsException if either
    givenPosition < 1 or givenPosition > getLength(). */
    public T remove(int givenPosition);

    public void clear();

    public T replace(int givenPosition,T newEntry);

    public T getEntry(int givenPosition);

    public T[] toArray();

    public boolean contains(T anEntry);

    public int getLength();

    public boolean isEmpty();
    
}
