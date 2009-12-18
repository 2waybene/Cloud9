package edu.umd.cloud9.util;

import java.util.RandomAccess;

/**
 * Object representing a list of shorts, backed by an resizable-array.
 */

public class ArrayListOfShorts implements RandomAccess, Cloneable {
	private transient short[] mArray;
	private int size;

	/**
	 * Constructs an empty list with the specified initial capacity.
	 * 
	 * @param initialCapacity
	 *            the initial capacity of the list
	 * @exception IllegalArgumentException
	 *                if the specified initial capacity is negative
	 */
	public ArrayListOfShorts(int initialCapacity) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		this.mArray = new short[initialCapacity];
	}

	/**
	 * Constructs an empty list with an initial capacity of ten.
	 */
	public ArrayListOfShorts() {
		this(10);
	}

	/**
	 * Trims the capacity of this object to be the list's current size. An
	 * application can use this operation to minimize the memory footprint of
	 * this object.
	 */
	public void trimToSize() {
		int oldCapacity = mArray.length;
		if (size < oldCapacity) {
			
			// TODO: this is a Java 1.6 feature
			// mArray = Arrays.copyOf(mArray, size);
			
			short[] arr = new short[size];
			System.arraycopy(mArray, 0, arr, 0, size);
			mArray = arr;
			
		}
	}

	/**
	 * Increases the capacity of this object, if necessary, to ensure that it
	 * can hold at least the number of elements specified by the minimum
	 * capacity argument.
	 * 
	 * @param minCapacity
	 *            the desired minimum capacity
	 */
	public void ensureCapacity(int minCapacity) {
		int oldCapacity = mArray.length;
		if (minCapacity > oldCapacity) {
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;
			
			// TODO: this is a Java 1.6 feature
			//mArray = Arrays.copyOf(mArray, newCapacity);
			
			short[] arr = new short[newCapacity];
			System.arraycopy(mArray, 0, arr, 0, mArray.length);
			mArray = arr;
		}
	}

	/**
	 * Returns the number of elements in this list.
	 * 
	 * @return the number of elements in this list
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns <tt>true</tt> if this list contains no elements.
	 * 
	 * @return <tt>true</tt> if this list contains no elements
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns <tt>true</tt> if this list contains the specified element.
	 * 
	 * @param n
	 *            element whose presence in this list is to be tested
	 * @return <tt>true</tt> if this list contains the specified element
	 */
	public boolean contains(short n) {
		return indexOf(n) >= 0;
	}

	/**
	 * Returns the index of the first occurrence of the specified element in
	 * this list, or -1 if this list does not contain the element.
	 */
	public int indexOf(short n) {
		for (int i = 0; i < size; i++)
			if (n == mArray[i])
				return i;
		return -1;
	}

	/**
	 * Returns the index of the last occurrence of the specified element in this
	 * list, or -1 if this list does not contain the element.
	 */
	public int lastIndexOf(short n) {
		for (int i = size - 1; i >= 0; i--)
			if (n == mArray[i])
				return i;
		return -1;
	}

	/**
	 * Returns a clone of this object.
	 * 
	 * @return a clone of this object
	 */
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the element at the specified position in this list.
	 * 
	 * @param index
	 *            index of the element to return
	 * @return the element at the specified position in this list
	 */
	public short get(int index) {
		return mArray[index];
	}

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 * 
	 * @param index
	 *            index of the element to replace
	 * @param element
	 *            element to be stored at the specified position
	 * @return the element previously at the specified position
	 */
	public short set(int index, short element) {
		short oldValue = mArray[index];
		mArray[index] = element;
		return oldValue;
	}

	/**
	 * Appends the specified element to the end of this list.
	 * 
	 * @param e
	 *            element to be appended to this list
	 */
	public void add(short e) {
		ensureCapacity(size + 1); // Increments modCount!!
		mArray[size++] = e;
	}

	/**
	 * Inserts the specified element at the specified position in this list.
	 * Shifts the element currently at that position (if any) and any subsequent
	 * elements to the right (adds one to their indices).
	 * 
	 * @param index
	 *            index at which the specified element is to be inserted
	 * @param element
	 *            element to be inserted
	 */
	public void add(int index, short element) {
		if (index > size || index < 0)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

		ensureCapacity(size + 1); // Increments modCount!!
		System.arraycopy(mArray, index, mArray, index + 1, size - index);
		mArray[index] = element;
		size++;
	}

	/**
	 * Removes the element at the specified position in this list. Shifts any
	 * subsequent elements to the left (subtracts one from their indices).
	 * 
	 * @param index
	 *            the index of the element to be removed
	 * @return the element that was removed from the list
	 */
	public short remove(int index) {
		short oldValue = mArray[index];

		int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(mArray, index + 1, mArray, index, numMoved);

		return oldValue;
	}

	/**
	 * Removes all of the elements from this list. The list will be empty after
	 * this call returns.
	 */
	public void clear() {
		size = 0;
	}

	public short[] getArray() {
		return mArray;
	}
}