import de.siphalor.spiceoffabric.util.FixedLengthIntFIFOQueue;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

public class FixedLengthIntFIFOQueueTests {
	@Test
	public void testConstructor() {
		Assertions.assertDoesNotThrow(() -> new FixedLengthIntFIFOQueue(0));
		Assertions.assertDoesNotThrow(() -> new FixedLengthIntFIFOQueue(1));
		Assertions.assertDoesNotThrow(() -> new FixedLengthIntFIFOQueue(1000));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new FixedLengthIntFIFOQueue(-1));
	}

	@Test
	public void testSize() {
		FixedLengthIntFIFOQueue queue = new FixedLengthIntFIFOQueue(2);
		Assertions.assertEquals(0, queue.size());
		queue.enqueue(1);
		Assertions.assertEquals(1, queue.size());
		queue.enqueue(1);
		Assertions.assertEquals(2, queue.size());
		queue.enqueue(1);
		Assertions.assertEquals(2, queue.size());
		queue.dequeueInt();
		Assertions.assertEquals(1, queue.size());
	}

	@Test
	public void testClear() {
		FixedLengthIntFIFOQueue queue = new FixedLengthIntFIFOQueue(2);
		queue.enqueue(1);
		queue.clear();
		Assertions.assertEquals(0, queue.size());
		Assertions.assertFalse(queue.iterator().hasNext());
	}

	@Test
	public void testIsEmpty() {
		FixedLengthIntFIFOQueue queue = new FixedLengthIntFIFOQueue(2);
		Assertions.assertTrue(queue.isEmpty());
		queue.enqueue(1);
		Assertions.assertFalse(queue.isEmpty());
		queue.enqueue(1);
		Assertions.assertFalse(queue.isEmpty());
		queue.dequeueInt();
		Assertions.assertFalse(queue.isEmpty());
		queue.dequeueInt();
		Assertions.assertTrue(queue.isEmpty());
	}

	@Test
	public void testEnqueue() {
		FixedLengthIntFIFOQueue queue = new FixedLengthIntFIFOQueue(2);
		queue.enqueue(1);
		Assertions.assertEquals(1, queue.firstInt());
		queue.enqueue(2);
		Assertions.assertEquals(1, queue.firstInt());
		queue.enqueue(3);
		Assertions.assertEquals(2, queue.firstInt());
	}

	@Test
	public void testDequeue() {
		FixedLengthIntFIFOQueue queue = new FixedLengthIntFIFOQueue(2);
		Assertions.assertThrows(NoSuchElementException.class, queue::dequeueInt);
		queue.enqueue(1);
		queue.dequeueInt();
		Assertions.assertEquals(0, queue.size());
		queue.enqueue(2);
		queue.enqueue(3);
		queue.enqueue(4);
		queue.dequeueInt();
		Assertions.assertEquals(1, queue.size());
		Assertions.assertEquals(4, queue.firstInt());
		queue.dequeueInt();
		Assertions.assertEquals(0, queue.size());
		Assertions.assertThrows(NoSuchElementException.class, queue::dequeueInt);
	}

	@Test
	public void testFirstInt() {
		FixedLengthIntFIFOQueue queue = new FixedLengthIntFIFOQueue(2);
		Assertions.assertThrows(NoSuchElementException.class, queue::firstInt);
		queue.enqueue(1);
		Assertions.assertEquals(1, queue.firstInt());
		queue.enqueue(2);
		Assertions.assertEquals(1, queue.firstInt());
		queue.enqueue(3);
		Assertions.assertEquals(2, queue.firstInt());
		queue.enqueue(4);
		Assertions.assertEquals(3, queue.firstInt());
	}

	@Test
	public void testIterator() {
		FixedLengthIntFIFOQueue queue = new FixedLengthIntFIFOQueue(2);
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);

		IntIterator iterator = queue.iterator();
		Assertions.assertTrue(iterator.hasNext());
		Assertions.assertEquals(2, iterator.nextInt());
		Assertions.assertTrue(iterator.hasNext());
		Assertions.assertEquals(3, iterator.nextInt());
		Assertions.assertFalse(iterator.hasNext());
		Assertions.assertThrows(NoSuchElementException.class, iterator::nextInt);
	}

	@Test
	public void testForEach() {
		FixedLengthIntFIFOQueue queue = new FixedLengthIntFIFOQueue(2);
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);

		IntList data = new IntArrayList(2);
		queue.forEach(data::add);
		Assertions.assertArrayEquals(new int[]{2, 3}, data.toIntArray());
	}

	@Test
	public void testSetLengthIncrease() {
		FixedLengthIntFIFOQueue queue = new FixedLengthIntFIFOQueue(3);
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);
		testSetLengthIncreaseHelper(queue);

		queue = new FixedLengthIntFIFOQueue(3);
		queue.enqueue(0);
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);
		testSetLengthIncreaseHelper(queue);
	}

	private void testSetLengthIncreaseHelper(FixedLengthIntFIFOQueue queue) {
		queue.setLength(4);
		Assertions.assertEquals(4, queue.getLength());
		Assertions.assertEquals(3, queue.size());
		IntIterator iterator = queue.iterator();
		Assertions.assertEquals(1, iterator.nextInt());
		Assertions.assertEquals(2, iterator.nextInt());
		Assertions.assertEquals(3, iterator.nextInt());
		Assertions.assertFalse(iterator.hasNext());
		queue.enqueue(4);
		queue.enqueue(5);
		Assertions.assertEquals(2, queue.dequeueInt());
		Assertions.assertEquals(3, queue.dequeueInt());
		Assertions.assertEquals(4, queue.dequeueInt());
		Assertions.assertEquals(5, queue.dequeueInt());
		Assertions.assertThrows(NoSuchElementException.class, queue::dequeueInt);
	}

	@Test
	public void testSetLengthDecrease() {
		FixedLengthIntFIFOQueue queue = new FixedLengthIntFIFOQueue(4);
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);
		queue.enqueue(4);
		testSetLengthDecreaseHelper(queue);

		queue = new FixedLengthIntFIFOQueue(4);
		queue.enqueue(0);
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);
		queue.enqueue(4);
		testSetLengthDecreaseHelper(queue);
	}

	private void testSetLengthDecreaseHelper(FixedLengthIntFIFOQueue queue) {
		queue.setLength(3);
		Assertions.assertEquals(3, queue.getLength());
		Assertions.assertEquals(3, queue.size());
		IntIterator iterator = queue.iterator();
		Assertions.assertEquals(2, iterator.nextInt());
		Assertions.assertEquals(3, iterator.nextInt());
		Assertions.assertEquals(4, iterator.nextInt());
		Assertions.assertFalse(iterator.hasNext());
		queue.enqueue(5);
		Assertions.assertEquals(3, queue.dequeueInt());
		Assertions.assertEquals(4, queue.dequeueInt());
		Assertions.assertEquals(5, queue.dequeueInt());
		Assertions.assertThrows(NoSuchElementException.class, queue::dequeueInt);
	}
}
