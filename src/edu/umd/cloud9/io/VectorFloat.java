package edu.umd.cloud9.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 * <p>
 * Serializable object representing a vector of float values. This generic
 * class, based on the Java {@link HashMap}, supports the use of any class for
 * the features (i.e., component of the vector), but all values are floats.
 * </p>
 * 
 * @param <F>
 *            type of feature
 */
public class VectorFloat<F extends WritableComparable> extends HashMap<F, Float> implements
		Writable {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a VectorFloat object.
	 */
	public VectorFloat() {
		super();
	}

	/**
	 * Deserializes the vector.
	 * 
	 * @param in
	 *            source for raw byte representation
	 */
	@SuppressWarnings("unchecked")
	public void readFields(DataInput in) throws IOException {

		this.clear();

		int numEntries = in.readInt();
		if (numEntries == 0)
			return;

		String keyClassName = in.readUTF();

		F objK;
		try {
			Class keyClass = Class.forName(keyClassName);
			for (int i = 0; i < numEntries; i++) {
				objK = (F) keyClass.newInstance();
				objK.readFields(in);
				float s = in.readFloat();
				put(objK, s);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Serializes the vector.
	 * 
	 * @param out
	 *            where to write the raw byte representation
	 */
	public void write(DataOutput out) throws IOException {
		// Write out the number of entries in the map
		out.writeInt(size());
		if (size() == 0)
			return;

		// Write out the class names for keys and values
		// assuming that data is homogeneous (i.e., all entries have same types)
		Set<Map.Entry<F, Float>> entries = entrySet();
		Map.Entry<F, Float> first = entries.iterator().next();
		F objK = first.getKey();
		out.writeUTF(objK.getClass().getCanonicalName());

		// Then write out each key/value pair
		for (Map.Entry<F, Float> e : entrySet()) {
			e.getKey().write(out);
			out.writeFloat(e.getValue());
		}
	}

	/**
	 * Sets the value of a feature.
	 * 
	 * @param f
	 *            the feature
	 * @param v
	 *            the value
	 */
	public void set(F f, float v) {
		super.put(f, v);
	}

	/**
	 * Returns the value of a feature.
	 * 
	 * @param f
	 *            the feature
	 * @return the value of the feature
	 */
	public float get(F f) {
		return super.get(f);
	}

	/**
	 * Adds another vector to this vector, based on feature-wise addition.
	 * 
	 * @param v
	 *            vector to add
	 */
	public void plus(VectorFloat<F> v) {
		for (Map.Entry<F, Float> e : v.entrySet()) {
			F key = e.getKey();

			if (this.containsKey(key)) {
				this.put(key, this.get(key) + e.getValue());
			} else {
				this.put(key, e.getValue());
			}
		}
	}

	/**
	 * Returns feature-value entries sorted by descending value. Ties broken by
	 * the natural sort order of the feature.
	 * 
	 * @return feature-value entries sorted by descending value
	 */
	public SortedSet<Map.Entry<F, Float>> getSortedEntries() {
		SortedSet<Map.Entry<F, Float>> entries = new TreeSet<Map.Entry<F, Float>>(
				new Comparator<Map.Entry<F, Float>>() {
					@SuppressWarnings("unchecked")
					public int compare(Map.Entry<F, Float> e1, Map.Entry<F, Float> e2) {
						if (e1.getValue() > e2.getValue()) {
							return -1;
						} else if (e1.getValue() < e2.getValue()) {
							return 1;
						}
						return e1.getKey().compareTo(e2.getKey());
					}
				});

		for (Map.Entry<F, Float> entry : this.entrySet()) {
			entries.add(entry);
		}

		return Collections.unmodifiableSortedSet(entries);
	}

}