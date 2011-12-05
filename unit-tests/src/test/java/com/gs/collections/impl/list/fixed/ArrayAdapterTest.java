/*
 * Copyright 2011 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.list.fixed;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.AbstractListTestCase;
import com.gs.collections.impl.list.mutable.AddToList;
import com.gs.collections.impl.list.mutable.ArrayListAdapter;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.SynchronizedMutableList;
import com.gs.collections.impl.list.mutable.UnmodifiableMutableList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit test for {@link ArrayAdapter}.
 */
public class ArrayAdapterTest extends AbstractListTestCase
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayAdapterTest.class);

    @Override
    protected <T> MutableList<T> classUnderTest()
    {
        return ArrayAdapter.newArray();
    }

    @Override
    protected <T> MutableList<T> newWith(T one)
    {
        return ArrayAdapter.newArrayWith(one);
    }

    @Override
    protected <T> MutableList<T> newWith(T one, T two)
    {
        return ArrayAdapter.newArrayWith(one, two);
    }

    @Override
    protected <T> MutableList<T> newWith(T one, T two, T three)
    {
        return ArrayAdapter.newArrayWith(one, two, three);
    }

    @Override
    protected <T> MutableList<T> newWith(T... littleElements)
    {
        return ArrayAdapter.newArrayWith(littleElements);
    }

    @Test
    public void testNewList()
    {
        MutableList<Integer> collection = this.newArray();
        Verify.assertEmpty(collection);
        Verify.assertSize(0, collection);
    }

    private MutableList<Integer> newArray()
    {
        return ArrayAdapter.newArray();
    }

    private MutableList<Integer> newArrayWith(Integer... elements)
    {
        return ArrayAdapter.newArrayWith(elements);
    }

    @Test
    public void testAsSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableList.class, ArrayAdapter.newArray().asSynchronized());
    }

    @Test
    public void testAdapt()
    {
        MutableList<Integer> collection = ArrayAdapter.newArrayWith();
        Verify.assertEmpty(collection);
    }

    @Test
    public void testNewListWith()
    {
        MutableList<Integer> collection = ArrayAdapter.newArrayWith(1);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(1, collection);
        Verify.assertContains(1, collection);
    }

    @Test
    public void testNewListWithWith()
    {
        MutableList<Integer> collection = ArrayAdapter.newArrayWith(1, 2);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(2, collection);
        Verify.assertContainsAll(collection, 1, 2);
    }

    @Test
    public void testNewListWithWithWith()
    {
        MutableList<Integer> collection = ArrayAdapter.newArrayWith(1, 2, 3);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(3, collection);
        Verify.assertContainsAll(collection, 1, 2, 3);
    }

    @Test
    public void testNewListWithVarArgs()
    {
        MutableList<Integer> collection = this.newArrayWith(1, 2, 3, 4);
        Verify.assertNotEmpty(collection);
        Verify.assertSize(4, collection);
        Verify.assertContainsAll(collection, 1, 2, 3, 4);
    }

    @Test
    public void testForEach()
    {
        List<Integer> result = new ArrayList<Integer>();
        MutableList<Integer> collection = this.newArrayWith(1, 2, 3, 4);
        collection.forEach(CollectionAddProcedure.on(result));
        Verify.assertSize(4, result);
        Verify.assertContainsAll(result, 1, 2, 3, 4);
    }

    @Test
    public void testForEachFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> collection = this.newArrayWith(1, 2, 3, 4);
        collection.forEach(2, 3, CollectionAddProcedure.on(result));
        Verify.assertSize(2, result);
        Verify.assertContainsAll(result, 3, 4);
    }

    @Test
    public void testForEachWithIndex()
    {
        final List<Integer> result = new ArrayList<Integer>();
        MutableList<Integer> collection = this.newArrayWith(1, 2, 3, 4);
        collection.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                result.add(object + index);
            }
        });
        Verify.assertContainsAll(result, 1, 3, 5, 7);
    }

    @Test
    public void testSelect()
    {
        Verify.assertContainsAll(this.newArrayWith(1, 2, 3, 4, 5).select(Predicates.lessThan(3)), 1, 2);
        Verify.denyContainsAny(this.newArrayWith(-1, 2, 3, 4, 5).select(Predicates.lessThan(3)), 3, 4, 5);
        Verify.assertContainsAll(this.newArrayWith(1, 2, 3, 4, 5).select(Predicates.lessThan(3),
                UnifiedSet.<Integer>newSet()), 1, 2);
    }

    @Test
    public void testReject()
    {
        Verify.assertContainsAll(this.newArrayWith(1, 2, 3, 4).reject(Predicates.lessThan(3)), 3, 4);
        Verify.assertContainsAll(this.newArrayWith(1, 2, 3, 4).reject(Predicates.lessThan(3),
                UnifiedSet.<Integer>newSet()), 3, 4);
    }

    @Test
    public void testCollect()
    {
        Verify.assertContainsAll(this.newArrayWith(1, 2, 3, 4).collect(Functions.getToString()),
                "1",
                "2",
                "3",
                "4");
        Verify.assertContainsAll(this.newArrayWith(1, 2, 3, 4).collect(Functions.getToString(),
                UnifiedSet.<String>newSet()), "1", "2", "3", "4");
    }

    @Test
    public void testDetect()
    {
        Assert.assertEquals(Integer.valueOf(3), this.newArrayWith(1, 2, 3, 4, 5).detect(Predicates.equal(3)));
        Assert.assertNull(this.newArrayWith(1, 2, 3, 4, 5).detect(Predicates.equal(6)));
    }

    @Test
    public void testDetectIfNoneWithBlock()
    {
        Function0<Integer> function = new PassThruFunction0<Integer>(6);
        Assert.assertEquals(Integer.valueOf(3), this.newArrayWith(1, 2, 3, 4, 5).detectIfNone(Predicates.equal(3), function));
        Assert.assertEquals(Integer.valueOf(6), this.newArrayWith(1, 2, 3, 4, 5).detectIfNone(Predicates.equal(6), function));
    }

    @Test
    public void testAdd()
    {
        final MutableList<String> collection = ArrayAdapter.newArray();
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                collection.add(null);
            }
        });
    }

    @Test
    public void testAddAtIndex()
    {
        final MutableList<String> collection = ArrayAdapter.newArray();
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                collection.add(0, null);
            }
        });
    }

    @Test
    public void testAddAll()
    {
        final MutableList<String> collection = ArrayAdapter.newArray();
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                collection.addAll(Lists.fixedSize.of(""));
            }
        });
    }

    @Test
    public void testAddAllAtIndex()
    {
        final MutableList<String> collection = ArrayAdapter.newArray();
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                collection.addAll(0, Lists.fixedSize.of(""));
            }
        });
    }

    @Test
    public void testAllSatisfy()
    {
        Assert.assertTrue(this.newArrayWith(1, 2, 3).allSatisfy(Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(this.newArrayWith(1, 2, 3).allSatisfy(Predicates.equal(1)));
    }

    @Test
    public void testAnySatisfy()
    {
        Assert.assertFalse(this.newArrayWith(1, 2, 3).anySatisfy(Predicates.instanceOf(String.class)));
        Assert.assertTrue(this.newArrayWith(1, 2, 3).anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void testCount()
    {
        Assert.assertEquals(3, this.newArrayWith(1, 2, 3).count(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void testCollectIf()
    {
        Verify.assertContainsAll(this.newArrayWith(1, 2, 3).collectIf(Predicates.instanceOf(Integer.class),
                Functions.getToString()), "1", "2", "3");
        Verify.assertContainsAll(this.newArrayWith(1, 2, 3).collectIf(Predicates.instanceOf(Integer.class),
                Functions.getToString(),
                new ArrayList<String>()), "1", "2", "3");
    }

    @Test
    public void testGetFirst()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newArrayWith(1, 2, 3).getFirst());
        Verify.assertNotEquals(Integer.valueOf(3), this.newArrayWith(1, 2, 3).getFirst());
    }

    @Test
    public void testGetLast()
    {
        Verify.assertNotEquals(Integer.valueOf(1), this.newArrayWith(1, 2, 3).getLast());
        Assert.assertEquals(Integer.valueOf(3), this.newArrayWith(1, 2, 3).getLast());
    }

    @Test
    public void testIsEmpty()
    {
        Verify.assertEmpty(this.newArray());
        Verify.assertNotEmpty(this.newArrayWith(1, 2));
        Assert.assertTrue(this.newArrayWith(1, 2).notEmpty());
    }

    @Test
    public void testRemoveAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ArrayAdapterTest.this.newArrayWith(1, 2, 3).removeAll(Lists.fixedSize.of(1, 2));
            }
        });
    }

    @Test
    public void testRetainAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ArrayAdapterTest.this.newArrayWith(1, 2, 3).retainAll(Lists.fixedSize.of(1, 2));
            }
        });
    }

    @Test
    public void testClear()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ArrayAdapterTest.this.newArrayWith(1, 2, 3).clear();
            }
        });
    }

    @Test
    public void testIterator()
    {
        MutableList<Integer> objects = this.newArrayWith(1, 2, 3);
        Iterator<Integer> iterator = objects.iterator();
        for (int i = objects.size(); i-- > 0; )
        {
            Integer integer = iterator.next();
            Assert.assertEquals(3, integer.intValue() + i);
        }
    }

    @Test
    public void testInjectInto()
    {
        MutableList<Integer> objects = this.newArrayWith(1, 2, 3);
        Integer result = objects.injectInto(1, AddFunction.INTEGER);
        Assert.assertEquals(Integer.valueOf(7), result);
    }

    @Test
    public void testToArray()
    {
        MutableList<Integer> objects = this.newArrayWith(1, 2, 3);
        Object[] array = objects.toArray();
        Verify.assertSize(3, array);
        Integer[] array2 = objects.toArray(new Integer[3]);
        Verify.assertSize(3, array2);
        Integer[] array3 = objects.toArray(new Integer[1]);
        Verify.assertSize(3, array3);
        Integer[] expected = {1, 2, 3};
        Assert.assertArrayEquals(expected, array);
        Assert.assertArrayEquals(expected, array2);
        Assert.assertArrayEquals(expected, array3);
    }

    @Test
    public void testRemoveObject()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ArrayAdapterTest.this.newArrayWith(1, 2, 3).remove(3);
            }
        });
    }

    @Test
    public void testSelectAndRejectWith()
    {
        MutableList<Integer> objects = this.newArrayWith(1, 2);
        Twin<MutableList<Integer>> result = objects.selectAndRejectWith(Predicates2.equal(), 1);
        Verify.assertSize(1, result.getOne());
        Verify.assertSize(1, result.getTwo());
    }

    @Test
    public void testRemove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ArrayAdapter.newArrayWith(1, 2, 3, null).removeIf(Predicates.isNull());
            }
        });
    }

    @Test
    public void testRemoveAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ArrayAdapter.newArrayWith(1, 2, 3, null).remove(0);
            }
        });
    }

    @Test
    public void testRemoveIfWith()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ArrayAdapter.newArrayWith(1, 2, 3, null).removeIfWith(Predicates2.isNull(), null);
            }
        });
    }

    @Test
    public void testIndexOf()
    {
        MutableList<Integer> objects = ArrayAdapter.newArrayWith(1, 2, 3);
        Assert.assertEquals(1, objects.indexOf(2));
    }

    @Test
    public void testLastIndexOf()
    {
        MutableList<Integer> objects = ArrayAdapter.newArrayWith(1, 2, 3);
        Assert.assertEquals(1, objects.lastIndexOf(2));
    }

    @Test
    public void testSet()
    {
        MutableList<Integer> objects = ArrayAdapter.newArrayWith(1, 2, 3);
        Assert.assertEquals(Integer.valueOf(2), objects.set(1, 4));
        Assert.assertEquals(FastList.newListWith(1, 4, 3), objects);
    }

    @Test
    public void testEqualsAndHashCode()
    {
        ArrayAdapter<Integer> array1 = ArrayAdapter.newArrayWith(1, 2, 3, 4);
        ArrayAdapter<Integer> array2 = ArrayAdapter.newArrayWith(1, 2, 3, 4);
        ArrayAdapter<Integer> array3 = ArrayAdapter.newArrayWith(2, 3, 4);
        Verify.assertNotEquals(array1, null);
        Verify.assertEqualsAndHashCode(array1, array1);
        Verify.assertEqualsAndHashCode(array1, array2);
        Verify.assertNotEquals(array2, array3);
        Verify.assertEqualsAndHashCode(array1, new ArrayList<Integer>(array1));
        Verify.assertEqualsAndHashCode(array1, new LinkedList<Integer>(array1));
        Verify.assertEqualsAndHashCode(array1, ArrayListAdapter.<Integer>newList().with(1, 2, 3, 4));
        Verify.assertEqualsAndHashCode(array1, FastList.<Integer>newList().with(1, 2, 3, 4));
    }

    @Test
    public void testForEachWith()
    {
        final List<Integer> result = new ArrayList<Integer>();
        MutableList<Integer> collection = ArrayAdapter.newArrayWith(1, 2, 3, 4);
        collection.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 0);
        Verify.assertSize(4, result);
        Verify.assertContainsAll(result, 1, 2, 3, 4);
    }

    @Test
    public void testSelectWith()
    {
        Verify.assertContainsAll(ArrayAdapter.<Integer>newArrayWith(1, 2, 3, 4, 5).selectWith(Predicates2.<Integer>lessThan(),
                3), 1, 2);
        Verify.denyContainsAny(ArrayAdapter.<Integer>newArrayWith(-1, 2, 3, 4, 5).selectWith(Predicates2.<Integer>lessThan(),
                3), 3, 4, 5);
        Verify.assertContainsAll(ArrayAdapter.<Integer>newArrayWith(1, 2, 3, 4, 5).selectWith(Predicates2.<Integer>lessThan(),
                3,
                UnifiedSet.<Integer>newSet()), 1, 2);
    }

    @Test
    public void testRejectWith()
    {
        Verify.assertContainsAll(ArrayAdapter.<Integer>newArrayWith(1, 2, 3, 4).rejectWith(Predicates2.<Integer>lessThan(), 3),
                3,
                4);
        Verify.assertContainsAll(ArrayAdapter.<Integer>newArrayWith(1, 2, 3, 4).rejectWith(Predicates2.<Integer>lessThan(),
                3,
                UnifiedSet.<Integer>newSet()), 3, 4);
    }

    @Test
    public void testDetectWith()
    {
        Assert.assertEquals(Integer.valueOf(3),
                ArrayAdapter.<Integer>newArrayWith(1, 2, 3, 4, 5).detectWith(Predicates2.equal(), 3));
        Assert.assertNull(ArrayAdapter.<Integer>newArrayWith(1, 2, 3, 4, 5).detectWith(Predicates2.equal(), 6));
    }

    @Test
    public void testDetectWithIfNone()
    {
        MutableList<Integer> list = ArrayAdapter.newArrayWith(1, 2, 3, 4, 5);
        Assert.assertNull(list.detectWithIfNone(Predicates2.equal(), 6, new PassThruFunction0<Integer>(null)));
    }

    @Test
    public void testAllSatisfyWith()
    {
        Assert.assertTrue(ArrayAdapter.<Integer>newArrayWith(1, 2, 3).allSatisfyWith(Predicates2.instanceOf(),
                Integer.class));
        Assert.assertFalse(ArrayAdapter.<Integer>newArrayWith(1, 2, 3).allSatisfyWith(Predicates2.equal(), 1));
    }

    @Test
    public void testAnySatisfyWith()
    {
        Assert.assertFalse(ArrayAdapter.<Integer>newArrayWith(1, 2, 3).anySatisfyWith(Predicates2.instanceOf(),
                String.class));
        Assert.assertTrue(ArrayAdapter.<Integer>newArrayWith(1, 2, 3).anySatisfyWith(Predicates2.instanceOf(),
                Integer.class));
    }

    @Test
    public void testCountWith()
    {
        Assert.assertEquals(3,
                ArrayAdapter.<Integer>newArrayWith(1, 2, 3).countWith(Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void testCollectWith()
    {
        Function2<Integer, Integer, Integer> addBlock =
                new Function2<Integer, Integer, Integer>()
                {
                    public Integer value(Integer each, Integer parameter)
                    {
                        return each + parameter;
                    }
                };
        Assert.assertEquals(
                FastList.newListWith(2, 3, 4),
                ArrayAdapter.newArrayWith(1, 2, 3).collectWith(addBlock, 1));
        Assert.assertEquals(
                FastList.newListWith(2, 3, 4),
                ArrayAdapter.newArrayWith(1, 2, 3).collectWith(addBlock, 1, FastList.<Integer>newList()));
    }

    @Test
    public void testInjectIntoWith()
    {
        MutableList<Integer> objects = ArrayAdapter.newArrayWith(1, 2, 3);
        Integer result = objects.injectIntoWith(1, new Function3<Integer, Integer, Integer, Integer>()
        {
            public Integer value(Integer injectedValued, Integer item, Integer parameter)
            {
                return injectedValued + item + parameter;
            }
        }, 0);
        Assert.assertEquals(Integer.valueOf(7), result);
    }

    @Test
    public void testSerialization()
    {
        MutableList<Integer> collection = ArrayAdapter.newArrayWith(1, 2, 3, 4, 5);
        MutableList<Integer> deserializedCollection = SerializeTestHelper.serializeDeserialize(collection);
        Verify.assertSize(5, deserializedCollection);
        Verify.assertStartsWith(deserializedCollection, 1, 2, 3, 4, 5);
        Verify.assertListsEqual(collection, deserializedCollection);
    }

    @Test
    public void testBAOSSize()
    {
        MutableList<Integer> mutableArrayList = ArrayAdapter.newArray();

        List<Integer> arrayList = new ArrayList<Integer>();
        ByteArrayOutputStream stream2 = SerializeTestHelper.getByteArrayOutputStream(arrayList);
        LOGGER.info("ArrayList size: " + stream2.size());
        LOGGER.info(stream2.toString());

        ByteArrayOutputStream stream1 = SerializeTestHelper.getByteArrayOutputStream(mutableArrayList);
        LOGGER.info("ArrayAdapter size: " + stream1.size());
        LOGGER.info(stream1.toString());
    }

    @Override
    @Test
    public void testToString()
    {
        Assert.assertEquals(
                FastList.newList(this.newArrayWith(1, 2, 3, 4)).toString(),
                this.newArrayWith(1, 2, 3, 4).toString());
    }

    @Override
    @Test
    public void makeString()
    {
        Assert.assertEquals(
                FastList.newList(this.newArrayWith(1, 2, 3, 4)).makeString(),
                this.newArrayWith(1, 2, 3, 4).makeString());
    }

    @Override
    @Test
    public void appendString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        this.newArrayWith(1, 2, 3, 4).appendString(stringBuilder);

        Assert.assertEquals(
                FastList.newList(this.newArrayWith(1, 2, 3, 4)).makeString(),
                stringBuilder.toString());
    }

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableList.class, this.classUnderTest().asSynchronized());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableMutableList.class, this.classUnderTest().asUnmodifiable());
    }

    @Test
    public void testForEachWithIndexWithFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        this.newArrayWith(1, 2, 3).forEachWithIndex(1, 2, new AddToList(result));
        Assert.assertEquals(FastList.newListWith(2, 3), result);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void clear()
    {
        this.newArray().clear();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void addAtIndex()
    {
        this.newArray().add(0, null);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void addAllAtIndex()
    {
        this.newArray().addAll(0, FastList.<Integer>newList());
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void addAll()
    {
        this.newArray().addAll(FastList.<Integer>newList());
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void addAllIterable()
    {
        this.newArray().addAllIterable(FastList.<Integer>newList());
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeObject()
    {
        this.newArray().remove(null);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeIndex()
    {
        this.newArray().remove(0);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void remove()
    {
        this.newArray().removeIf(Predicates.alwaysFalse());
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeIfWith()
    {
        this.newArray().removeIfWith(Predicates2.alwaysFalse(), null);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeAll()
    {
        this.newArray().removeAll(FastList.<Object>newList());
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void removeAllIterable()
    {
        this.newArray().removeAllIterable(FastList.<Object>newList());
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void retainAll()
    {
        this.newArray().retainAll(FastList.<Object>newList());
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void retainAllIterable()
    {
        this.newArray().retainAllIterable(FastList.<Object>newList());
    }

    @Override
    @Test
    public void forEachOnRange()
    {
        final MutableList<Integer> list = this.newArrayWith(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        this.validateForEachOnRange(list, 0, 0, FastList.newListWith(0));
        this.validateForEachOnRange(list, 3, 5, FastList.newListWith(3, 4, 5));
        this.validateForEachOnRange(list, 4, 6, FastList.newListWith(4, 5, 6));
        this.validateForEachOnRange(list, 9, 9, FastList.newListWith(9));
        this.validateForEachOnRange(list, 0, 9, FastList.newListWith(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ArrayAdapterTest.this.validateForEachOnRange(list, 10, 10, FastList.<Integer>newList());
            }
        });
    }

    @Override
    @Test
    public void forEachWithIndexOnRange()
    {
        final MutableList<Integer> list = this.newArrayWith(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        this.validateForEachWithIndexOnRange(list, 0, 0, FastList.newListWith(0));
        this.validateForEachWithIndexOnRange(list, 3, 5, FastList.newListWith(3, 4, 5));
        this.validateForEachWithIndexOnRange(list, 4, 6, FastList.newListWith(4, 5, 6));
        this.validateForEachWithIndexOnRange(list, 9, 9, FastList.newListWith(9));
        this.validateForEachWithIndexOnRange(list, 0, 9, FastList.newListWith(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ArrayAdapterTest.this.validateForEachWithIndexOnRange(list, 10, 10, FastList.<Integer>newList());
            }
        });
    }

    @Override
    @Test
    public void subList()
    {
        MutableList<String> list = this.newWith("A", "B", "C", "D");
        MutableList<String> sublist = list.subList(1, 3);
        Verify.assertSize(2, sublist);
        Verify.assertContainsAll(sublist, "B", "C");
    }

    @Override
    @Test
    public void with()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 3);
        MutableCollection<Integer> collWith = coll.with(4);
        Assert.assertNotSame(coll, collWith);
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4), collWith);
    }

    @Override
    @Test
    public void withAll()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 3);
        MutableCollection<Integer> collWith = coll.withAll(FastList.newListWith(4, 5));
        Assert.assertNotSame(coll, collWith);
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4, 5), collWith);
        Assert.assertSame(collWith, collWith.withAll(FastList.<Integer>newList()));
    }

    @Override
    @Test
    public void without()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 3, 2);
        MutableCollection<Integer> collWithout = coll.without(2);
        Assert.assertNotSame(coll, collWithout);
        Assert.assertEquals(FastList.newListWith(1, 3, 2), collWithout);
        Assert.assertSame(collWithout, collWithout.without(9));
    }

    @Override
    @Test
    public void withoutAll()
    {
        MutableCollection<Integer> coll = this.newWith(1, 2, 4, 2, 3, 4, 5);
        MutableCollection<Integer> collWithout = coll.withoutAll(FastList.newListWith(2, 4));
        Assert.assertNotSame(coll, collWithout);
        Assert.assertEquals(FastList.newListWith(1, 3, 5), collWithout);
        Assert.assertSame(collWithout, collWithout.withoutAll(FastList.newListWith(8, 9)));
        Assert.assertSame(collWithout, collWithout.withoutAll(FastList.<Integer>newList()));
    }
}
