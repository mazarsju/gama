/*********************************************************************************************
 *
 *
 * 'RandomUtils.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.common.util;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.Set;

import msi.gama.common.GamaPreferences;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.util.random.CellularAutomatonRNG;
import msi.gama.util.random.GamaRNG;
import msi.gama.util.random.JavaRNG;
import msi.gama.util.random.MersenneTwisterRNG;
import msi.gaml.operators.fastmaths.FastMath;

public class RandomUtils {

	/** The seed. */
	protected Double seed;
	private static final SecureRandom SEED_SOURCE = new SecureRandom();
	/** The generator name. */
	private String generatorName;
	/** The generator. */
	private GamaRNG generator;

	public static class State {

		public State(final Double seed, final String generatorName, final int usage) {
			this.seed = seed;
			this.generatorName = generatorName;
			this.usage = usage;
		}

		Double seed;
		String generatorName;
		int usage;

	}

	public RandomUtils(final State state) {
		setState(state);
	}

	public RandomUtils(final Double seed, final String rng) {
		setSeed(seed, false);
		setGenerator(rng, true);
	}

	public RandomUtils(final String rng) {
		this(GamaPreferences.CORE_SEED_DEFINED.getValue() ? GamaPreferences.CORE_SEED.getValue() : (Double) null, rng);
	}

	public RandomUtils() {
		this(GamaPreferences.CORE_RNG.getValue());
	}

	public State getState() {
		return new State(seed, generatorName, generator.getUsage());
	}

	public void setState(final State state) {
		setSeed(state.seed, false);
		setGenerator(state.generatorName, true);
		generator.setUsage(state.usage);
	}

	/**
	 * Inits the generator.
	 */
	private void initGenerator() {
		if (generatorName.equals(IKeyword.CELLULAR)) {
			generator = new CellularAutomatonRNG(this);
		} else if (generatorName.equals(IKeyword.JAVA)) {
			generator = new JavaRNG(this);
		} else {
			/* By default */
			generator = new MersenneTwisterRNG(this);
		}
	}

	public void setUsage(final Integer usage) {
		generator.setUsage(usage);
	}

	public Integer getUsage() {
		return generator.getUsage();
	}

	/**
	 * Creates a new Continuous Uniform Generator object.
	 *
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 *
	 * @return the continuous uniform generator
	 */
	public double createUniform(final double min, final double max) {
		return next() * (max - min) + min;
	}

	/**
	 * Creates a new Gaussian Generator object.
	 *
	 * @param mean
	 *            the mean
	 * @param stdv
	 *            the stdv
	 *
	 * @return the gaussian generator
	 */
	public double createGaussian(final double mean, final double stdv) {
		return generator.nextGaussian() * stdv + mean;
	}

	/**
	 * Creates a new Binomial Generator object.
	 *
	 * @param n
	 *            the n
	 * @param p
	 *            the p
	 *
	 * @return the binomial generator
	 */
	public int createBinomial(final int n, final double p) {

		double value = p;
		final StringBuilder bits = new StringBuilder(64);
		double bitValue = 0.5d;
		while (value > 0) {
			if (value >= bitValue) {
				bits.append('1');
				value -= bitValue;
			} else {
				bits.append('0');
			}
			bitValue /= 2;
		}
		final BitString pBits = new BitString(bits.toString());

		int trials = n;
		int totalSuccesses = 0;
		int pIndex = pBits.getLength() - 1;
		while (trials > 0 && pIndex >= 0) {
			final BitString bs = new BitString(trials, generator);
			final int successes = bs.countSetBits();
			trials -= successes;
			if (pBits.getBit(pIndex)) {
				totalSuccesses += successes;
			}
			--pIndex;
		}
		return totalSuccesses;

	}

	/**
	 * Creates a new Poisson Generator object.
	 *
	 * @param mean
	 *            the mean
	 *
	 * @return the poisson generator
	 */
	public int createPoisson(final double mean) {

		int x = 0;
		double t = 0.0;
		while (true) {
			t -= FastMath.log(next()) / mean;
			if (t > 1.0) {
				break;
			}
			++x;
		}
		return x;

	}

	private byte[] createSeed(final Double s, final int length) {
		this.seed = s;
		Double realSeed = seed;
		if (realSeed < 0) {
			realSeed *= -1;
		}
		if (realSeed < 1) {
			realSeed *= Long.MAX_VALUE;
		}
		long l = realSeed.longValue();
		// System.out.println("Initial seed: " + seed + "; normalized seed: " +
		// l);

		final byte[] result = new byte[length];
		switch (length) {
		case 4:
			for (int i1 = 0; i1 < 4; i1++) {
				result[i1] = (byte) (l & 0xff);
				l >>= 8;
			}
			break;
		case 8:
			for (int i = 0; i < 8; i++) {
				result[i] = (byte) l;
				l >>= 8;
			}
			break;
		case 16:
			for (int i = 0; i < 8; i++) {
				result[i] = result[i + 8] = (byte) (l & 0xff);
				l >>= 8;
			}
		}
		return result;
	}

	public void dispose() {
		seed = null;
		generator = null;
		// uniform = null;
	}

	public byte[] generateSeed(final int length) {
		// byte[] result;
		return createSeed(seed, length);
	}

	public void setSeed(final Double newSeed, final boolean init) {
		seed = newSeed;
		if (seed == null) {
			seed = SEED_SOURCE.nextDouble();
		}
		if (init) {
			initGenerator();
		}
	}

	/**
	 * Sets the generator.
	 *
	 * @param newGen
	 *            the new generator
	 */
	public void setGenerator(final String newGen, final boolean init) {
		generatorName = newGen;
		if (init) {
			initGenerator();
		}
	}

	public void shuffle2(final Set list) {
		final int size = list.size();
		if (size < 2) {
			return;
		}
		final Object[] a = list.toArray(new Object[size]);
		list.clear();
		for (int i = 0; i < size; i++) {
			final int change = between(i, size - 1);
			final Object helper = a[i];
			a[i] = a[change];
			a[change] = helper;
			list.add(a[i]);
		}
	}

	public List shuffle(final List list) {
		for (int i = list.size(); i > 1; i--) {
			final int i1 = i - 1;
			final int j = between(0, i - 1);
			final Object tmp = list.get(i1);
			list.set(i1, list.get(j));
			list.set(j, tmp);
		}
		return list;
	}

	public String shuffle(final String string) {
		final char[] c = string.toCharArray();
		shuffle(c);
		return String.copyValueOf(c);
	}

	public <T> T[] shuffle(final T[] array) {
		final T[] copy = array.clone();
		for (int i = array.length; i > 1; i--) {
			final int i1 = i - 1;
			final int j = between(0, i - 1);
			final T tmp = copy[i1];
			copy[i1] = copy[j];
			copy[j] = tmp;
		}
		return copy;
	}

	/**
	 * @return an uniformly distributed int random number in [from, to]
	 */
	public int between(final int min, final int max) {
		return (int) (min + (long) ((1L + max - min) * next()));
	}

	public double between(final double min, final double max) {
		// uniformly distributed double random number in [min, max]
		return min + (max + Double.MIN_VALUE - min) * next();
	}

	/**
	 * @return an uniformly distributed int random number in [min, max]
	 *         respecting the step
	 */
	public int between(final int min, final int max, final int step) {
		final int nbSteps = (max - min) / step;
		return min + between(0, nbSteps) * step;
	}

	public double between(final double min, final double max, final double step) {
		// uniformly distributed double random number in [min, max] respecting
		// the step
		final double val = between(min, max);
		final int nbStep = (int) ((val - min) / step);
		final double high = (int) (FastMath.min(max, min + (nbStep + 1.0) * step) * 1000000) / 1000000.0;
		final double low = (int) ((min + nbStep * step) * 1000000) / 1000000.0;
		return val - low < high - val ? low : high;
	}

	public double next() {
		return generator.nextDouble();
	}

	/**
	 * @param matrix
	 * @return
	 */
	public double[] shuffle(final double[] array) {
		for (int i = array.length; i > 1; i--) {
			final int i1 = i - 1;
			final int j = between(0, i - 1);
			final double tmp = array[i1];
			array[i1] = array[j];
			array[j] = tmp;
		}
		return array;
	}

	public int[] shuffle(final int[] array) {
		for (int i = array.length; i > 1; i--) {
			final int i1 = i - 1;
			final int j = between(0, i - 1);
			final int tmp = array[i1];
			array[i1] = array[j];
			array[j] = tmp;
		}
		return array;
	}

	public char[] shuffle(final char[] array) {
		for (int i = array.length; i > 1; i--) {
			final int i1 = i - 1;
			final int j = between(0, i - 1);
			final char tmp = array[i1];
			array[i1] = array[j];
			array[j] = tmp;
		}
		return array;
	}

	/**
	 * @return
	 */
	public Double getSeed() {
		return seed;
	}

	/**
	 * @return
	 */
	public String getRngName() {
		return generatorName;
	}

	public static void drawRandomValues(final double min, final double max, final double step) {
		System.out.println("Drawing 100 double between " + min + " and " + max + " step " + step);
		final RandomUtils r = new RandomUtils(100.0, "mersenne");
		for (int i = 0; i < 100; i++) {
			final double val = r.between(min, max);
			final int nbStep = (int) ((val - min) / step);
			final double high = (int) (FastMath.min(max, min + (nbStep + 1.0) * step) * 1000000) / 1000000.0;
			final double low = (int) ((min + nbStep * step) * 1000000) / 1000000.0;
			System.out.print(val - low < high - val ? low : high);
			System.out.print(" | ");
		}
		// System.out.println();
	}

	public static void drawRandomValues(final int min, final int max, final int step) {
		System.out.println("Drawing 100 int between " + min + " and " + max + " step " + step);
		final RandomUtils r = new RandomUtils(100.0, "mersenne");
		final int nbSteps = (max - min) / step;
		for (int i = 0; i < 100; i++) {
			final int val = min + r.between(0, nbSteps) * step;
			System.out.print(val);
			System.out.print(" | ");
		}
		// System.out.println();
	}

	public static void main(final String[] args) {
		final RandomUtils r1 = new RandomUtils(100.0, "mersenne");
		final RandomUtils r2 = new RandomUtils(100.0, "m{ersenne");
		for (int i = 0; i < 2000; i++) {
			System.out.println("r1 " + r1.next() + " | r2 " + r2.next());
		}
		// drawRandomValues(-0.2, 0.2, 0.1);
		// drawRandomValues(4., 5., 0.2);
		// drawRandomValues(0, 100, 3);
		// drawRandomValues(-5, 5, 3);
		// RandomUtils r = new RandomUtils(100.0, "mersenne");
		// for ( int i = 0; i < 10000000; i++ ) {
		// double d = 0.0;
		// if ( r.between(0.0, 0.1) == 0.0 ) {
		// System.out.println("0.0 !");
		// }
		// }
		// System.out.println("Finished");
	}

	private class BitString {

		private static final int WORD_LENGTH = 32;

		private final int length;

		/**
		 * Store the bits packed in an array of 32-bit ints. This field cannot
		 * be declared final because it must be cloneable.
		 */
		private final int[] data;

		/**
		 * Creates a bit string of the specified length with all bits initially
		 * set to zero (off).
		 * 
		 * @param length
		 *            The number of bits.
		 */
		public BitString(final int length) {
			if (length < 0) {
				throw new IllegalArgumentException("Length must be non-negative.");
			}
			this.length = length;
			this.data = new int[(length + WORD_LENGTH - 1) / WORD_LENGTH];
		}

		/**
		 * Creates a bit string of the specified length with each bit set
		 * randomly (the distribution of bits is uniform so long as the output
		 * from the provided RNG is also uniform). Using this constructor is
		 * more efficient than creating a bit string and then randomly setting
		 * each bit individually.
		 * 
		 * @param length
		 *            The number of bits.
		 * @param rng
		 *            A source of randomness.
		 */
		public BitString(final int length, final Random rng) {
			this(length);
			// We can set bits 32 at a time rather than calling
			// rng.nextBoolean()
			// and setting each one individually.
			for (int i = 0; i < data.length; i++) {
				data[i] = rng.nextInt();
			}
			// If the last word is not fully utilised, zero any out-of-bounds
			// bits.
			// This is necessary because the countSetBits() methods will count
			// out-of-bounds bits.
			final int bitsUsed = length % WORD_LENGTH;
			if (bitsUsed < WORD_LENGTH) {
				final int unusedBits = WORD_LENGTH - bitsUsed;
				final int mask = 0xFFFFFFFF >>> unusedBits;
				data[data.length - 1] &= mask;
			}
		}

		/**
		 * Initialises the bit string from a character string of 1s and 0s in
		 * big-endian order.
		 * 
		 * @param value
		 *            A character string of ones and zeros.
		 */
		public BitString(final String value) {
			this(value.length());
			for (int i = 0; i < value.length(); i++) {
				if (value.charAt(i) == '1') {
					setBit(value.length() - (i + 1), true);
				} else if (value.charAt(i) != '0') {
					throw new IllegalArgumentException("Illegal character at position " + i);
				}
			}
		}

		/**
		 * @return The length of this bit string.
		 */
		public int getLength() {
			return length;
		}

		/**
		 * Returns the bit at the specified index.
		 * 
		 * @param index
		 *            The index of the bit to look-up (0 is the
		 *            least-significant bit).
		 * @return A boolean indicating whether the bit is set or not.
		 * @throws IndexOutOfBoundsException
		 *             If the specified index is not a bit position in this bit
		 *             string.
		 */
		public boolean getBit(final int index) {
			assertValidIndex(index);
			final int word = index / WORD_LENGTH;
			final int offset = index % WORD_LENGTH;
			return (data[word] & 1 << offset) != 0;
		}

		/**
		 * Sets the bit at the specified index.
		 * 
		 * @param index
		 *            The index of the bit to set (0 is the least-significant
		 *            bit).
		 * @param set
		 *            A boolean indicating whether the bit should be set or not.
		 * @throws IndexOutOfBoundsException
		 *             If the specified index is not a bit position in this bit
		 *             string.
		 */
		public void setBit(final int index, final boolean set) {
			assertValidIndex(index);
			final int word = index / WORD_LENGTH;
			final int offset = index % WORD_LENGTH;
			if (set) {
				data[word] |= 1 << offset;
			} else // Unset the bit.
			{
				data[word] &= ~(1 << offset);
			}
		}

		/**
		 * Helper method to check whether a bit index is valid or not.
		 * 
		 * @param index
		 *            The index to check.
		 * @throws IndexOutOfBoundsException
		 *             If the index is not valid.
		 */
		private void assertValidIndex(final int index) {
			if (index >= length || index < 0) {
				throw new IndexOutOfBoundsException("Invalid index: " + index + " (length: " + length + ")");
			}
		}

		/**
		 * @return The number of bits that are 1s rather than 0s.
		 */
		public int countSetBits() {
			int count = 0;
			for (int x : data) {
				while (x != 0) {
					x &= x - 1; // Unsets the least significant on bit.
					++count; // Count how many times we have to unset a bit
								// before x equals zero.
				}
			}
			return count;
		}

	}

}
