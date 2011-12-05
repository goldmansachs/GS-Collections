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

package com.gs.collections.impl.list.mutable;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.RandomAccess;

import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class CompositeFastListTest
        extends AbstractListTestCase
{
    @Override
    protected <T> MutableList<T> classUnderTest()
    {
        return new CompositeFastList<T>();
    }

    @Override
    @Test
    public void testClone()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                CompositeFastListTest.this.classUnderTest().clone();
            }
        });
    }

    @Test
    public void testDefaultConstructor()
    {
        MutableList<String> list = new CompositeFastList<String>();
        list.add("1");
        list.add("2");
        Verify.assertSize(2, list);
        Verify.assertContains("1", list);
    }

    @Test
    public void testGet()
    {
        final MutableList<String> list = new CompositeFastList<String>();
        list.addAll(FastList.newListWith("1", "2", "3", "4"));
        list.addAll(FastList.newListWith("A", "B", "C", "B"));
        list.addAll(FastList.newListWith("Cat", "Dog", "Mouse", "Bird"));
        Assert.assertEquals("1", list.get(0));
        Assert.assertEquals("2", list.get(1));
        Assert.assertEquals("A", list.get(4));
        Assert.assertEquals("4", list.get(3));
        Assert.assertEquals("Cat", list.get(8));
        Assert.assertEquals("Bird", list.get(11));
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                list.get(12);
            }
        });
    }

    @Test
    public void testAddWithIndex()
    {
        MutableList<String> list = new CompositeFastList<String>();
        list.addAll(FastList.newListWith("1", "2", "3", "4"));
        list.addAll(FastList.newListWith("A", "B", "C", "B"));
        list.add(3, "NEW");
        Verify.assertSize(9, list);
        Assert.assertEquals("NEW", list.get(3));
        Assert.assertEquals("4", list.get(4));
        list.add(0, "START");
        Verify.assertSize(10, list);
        Assert.assertEquals("START", list.getFirst());
        list.add(10, "END");
        Verify.assertSize(11, list);
        Assert.assertEquals("END", list.getLast());
    }

    @Override
    @Test
    public void reverseThis()
    {
        super.reverseThis();
        CompositeFastList<Integer> composite = new CompositeFastList<Integer>();
        composite.addAll(FastList.newListWith(9, 8, 7));
        composite.addAll(FastList.newListWith(6, 5, 4));
        composite.addAll(FastList.newListWith(3, 2, 1));
        CompositeFastList<Integer> reversed = composite.reverseThis();
        Assert.assertSame(composite, reversed);
        Assert.assertEquals(Interval.oneTo(9), reversed);
    }

    @Override
    @Test
    public void addAllAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                CompositeFastListTest.super.addAllAtIndex();
            }
        });
    }

    @Override
    @Test
    public void set()
    {
        super.set();
        MutableList<String> list = new CompositeFastList<String>();
        list.addAll(FastList.newListWith("1", "2", "3", "4"));
        list.addAll(FastList.newListWith("A", "B", "C", "B"));
        Assert.assertEquals("1", list.set(0, "NEW"));
        Verify.assertSize(8, list);
        Assert.assertEquals("NEW", list.getFirst());
        Assert.assertEquals("2", list.get(1));
        Assert.assertEquals("B", list.set(7, "END"));
        Verify.assertSize(8, list);
        Assert.assertEquals("END", list.getLast());
    }

    @Test
    public void set_bugFix_off_by_one_error()
    {
        CompositeFastList<Integer> compositeList = new CompositeFastList<Integer>();
        MutableList<Integer> list1 = FastList.newListWith(1, 2, 3);
        MutableList<Integer> list2 = FastList.newListWith(4, 5);
        MutableList<Integer> list3 = FastList.newList();

        compositeList.addAll(list1);
        compositeList.addAll(list2);
        compositeList.addAll(list3);

        Assert.assertEquals(Integer.valueOf(4), compositeList.get(3));
        Assert.assertEquals(Integer.valueOf(4), compositeList.set(3, 99));
        Assert.assertEquals(Integer.valueOf(99), compositeList.get(3));
    }

    @Override
    @Test
    public void indexOf()
    {
        super.indexOf();
        MutableList<String> list = new CompositeFastList<String>();
        list.addAll(FastList.newListWith("1", "2", "3", "4"));
        list.addAll(FastList.newListWith("3", "B", "3", "B"));
        list.addAll(FastList.newListWith("3", "B", "3", "X"));

        Assert.assertEquals(2, list.indexOf("3"));
        Assert.assertEquals(5, list.indexOf("B"));
        Assert.assertEquals(11, list.indexOf("X"));

        Assert.assertEquals(-1, list.indexOf("missing"));
    }

    @Override
    @Test
    public void lastIndexOf()
    {
        super.lastIndexOf();
        MutableList<String> list = new CompositeFastList<String>();
        list.addAll(FastList.newListWith("1", "2", "3", "4"));
        list.addAll(FastList.newListWith("3", "B", "3", "B"));
        Assert.assertEquals(6, list.lastIndexOf("3"));
        Assert.assertEquals(3, list.lastIndexOf("4"));
        Assert.assertEquals(-1, list.lastIndexOf("missing"));
    }

    @Test
    public void testRemoveWithIndex()
    {
        MutableList<String> list = new CompositeFastList<String>();
        list.addAll(FastList.newListWith("1", "2", "3", "4"));
        list.addAll(FastList.newListWith("3", "B", "3", "B"));
        Assert.assertEquals("1", list.remove(0));
        Verify.assertSize(7, list);
        Assert.assertEquals("2", list.getFirst());
        Assert.assertEquals("B", list.remove(6));
        Verify.assertSize(6, list);
        Assert.assertEquals("3", list.getLast());
    }

    @Override
    @Test
    public void toArray()
    {
        super.toArray();
        MutableList<String> list = new CompositeFastList<String>();
        list.addAll(FastList.newListWith("1", "2", "3", "4"));
        list.addAll(Lists.mutable.<String>of());
        list.addAll(FastList.newListWith("3", "B", "3", "B"));
        list.addAll(Lists.mutable.<String>of());
        Assert.assertArrayEquals(new String[]{"1", "2", "3", "4", "3", "B", "3", "B"}, list.toArray());
    }

    @Test
    public void testEmptyIterator()
    {
        Assert.assertFalse(new CompositeFastList<String>().iterator().hasNext());
    }

    @Override
    @Test
    public void clear()
    {
        super.clear();
        MutableList<String> list = new CompositeFastList<String>();
        list.addAll(FastList.newListWith("1", "2", "3", "4"));
        list.addAll(FastList.newListWith("3", "B", "3", "B"));
        list.clear();
        Assert.assertTrue(list.isEmpty());
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testContainsAll()
    {
        MutableList<String> list = new CompositeFastList<String>();
        list.addAll(FastList.newListWith("1", "2", "3", "4"));
        list.addAll(FastList.newListWith("3", "B", "3", "B"));
        Assert.assertTrue(list.containsAll(FastList.newList().with("2", "B")));
    }

    @Override
    @Test
    public void removeAll()
    {
        super.removeAll();
        MutableList<String> list = new CompositeFastList<String>();
        list.addAll(FastList.newListWith("1", "2", "3", "4"));
        list.addAll(FastList.newListWith("3", "B", "3", "B"));
        list.removeAll(FastList.newList().with("2", "B"));
        Verify.assertSize(5, list);
    }

    @Override
    @Test
    public void retainAll()
    {
        super.retainAll();
        MutableList<String> list = new CompositeFastList<String>();
        list.addAll(FastList.newListWith("1", "2", "3", "4"));
        list.addAll(FastList.newListWith("3", "B", "3", "B"));
        list.retainAll(FastList.newList().with("2", "B"));
        Verify.assertSize(3, list);
    }

    @Override
    @Test
    public void forEach()
    {
        super.forEach();
        MutableList<Integer> list = FastList.newList();
        CompositeFastList<Integer> iterables = new CompositeFastList<Integer>();
        iterables.addComposited(Interval.oneTo(5).toList());
        iterables.addComposited(Interval.fromTo(6, 10).toList());
        iterables.forEach(CollectionAddProcedure.<Integer>on(list));
        Verify.assertSize(10, list);
        Verify.assertAllSatisfy(list, Predicates.greaterThan(0).and(Predicates.lessThan(11)));
    }

    @Override
    @Test
    public void forEachWithIndex()
    {
        super.forEachWithIndex();
        final MutableList<Integer> list = FastList.newList();
        CompositeFastList<Integer> iterables = new CompositeFastList<Integer>();
        iterables.addComposited(Interval.fromTo(6, 10).toList());
        iterables.addComposited(Interval.oneTo(5).toList());
        iterables.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                list.add(index, each);
            }
        });
        Verify.assertSize(10, list);
        Verify.assertAllSatisfy(list, Predicates.greaterThan(0).and(Predicates.lessThan(11)));
        Verify.assertStartsWith(list, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5);
    }

    @Override
    @Test
    public void forEachWith()
    {
        super.forEachWith();
        final MutableList<Integer> list = FastList.newList();
        CompositeFastList<Integer> iterables = new CompositeFastList<Integer>();
        iterables.addComposited(Interval.fromTo(6, 10).toList());
        iterables.addComposited(Interval.oneTo(5).toList());
        iterables.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer each, Integer parameter)
            {
                list.add(parameter.intValue(), each);
            }
        }, 0);
        Verify.assertSize(10, list);
        Verify.assertAllSatisfy(list, Predicates.greaterThan(0).and(Predicates.lessThan(11)));
        Verify.assertStartsWith(list, 5, 4, 3, 2, 1, 10, 9, 8, 7, 6);
    }

    @Test
    public void testEquals()
    {
        CompositeFastList<String> composite = new CompositeFastList<String>();
        MutableList<String> list = FastList.newList();

        Verify.assertEqualsAndHashCode(composite, list);

        MutableList<String> list2 = FastList.newListWith("one", "two", "three");

        CompositeFastList<String> composite2 = new CompositeFastList<String>();
        MutableList<String> firstBit = FastList.newListWith("one", "two");
        MutableList<String> secondBit = FastList.newListWith("three");
        composite2.addAll(firstBit);
        composite2.addAll(secondBit);

        Verify.assertEqualsAndHashCode(list2, composite2);

        Verify.assertNotEquals(firstBit, composite2);
        Verify.assertNotEquals(composite2, firstBit);

        MutableList<String> list1 = FastList.newListWith("one", null, "three");

        CompositeFastList<String> composite1 = new CompositeFastList<String>();
        MutableList<String> firstBit1 = FastList.newListWith("one", null);
        MutableList<String> secondBit1 = FastList.newListWith("three");
        composite1.addAll(firstBit1);
        composite1.addAll(secondBit1);

        Verify.assertEqualsAndHashCode(list1, composite1);
    }

    @Test
    public void testHashCode()
    {
        CompositeFastList<String> composite = new CompositeFastList<String>();
        MutableList<String> list = FastList.newList();
        Verify.assertEqualsAndHashCode(composite, list);

        MutableList<String> list2 = FastList.newListWith("one", "two", "three");

        CompositeFastList<String> composite2 = new CompositeFastList<String>();
        MutableList<String> firstBit = FastList.newListWith("one", "two");
        MutableList<String> secondBit = FastList.newListWith("three");
        composite2.addAll(firstBit);
        composite2.addAll(secondBit);

        Verify.assertEqualsAndHashCode(list2, composite2);

        MutableList<String> list1 = FastList.newListWith("one", null, "three");

        CompositeFastList<String> composite1 = new CompositeFastList<String>();
        MutableList<String> firstBit1 = FastList.newListWith("one", null);
        MutableList<String> secondBit1 = FastList.newListWith("three");
        composite1.addAll(firstBit1);
        composite1.addAll(secondBit1);

        Verify.assertEqualsAndHashCode(list1, composite1);
    }

    @Override
    @Test
    public void listIterator()
    {
        super.listIterator();
        CompositeFastList<String> composite = new CompositeFastList<String>();
        FastList<String> firstBit = FastList.newListWith("one", "two");
        FastList<String> secondBit = FastList.newListWith("three");
        composite.addAll(firstBit);
        composite.addAll(secondBit);

        ListIterator<String> listIterator = composite.listIterator();
        listIterator.add("four");
        Verify.assertSize(4, composite);
        Assert.assertTrue(listIterator.hasNext());
        String element = listIterator.next();

        Assert.assertEquals("one", element);

        String element3 = listIterator.next();

        Assert.assertEquals("two", element3);

        String element2 = listIterator.previous();
        Assert.assertEquals("two", element2);

        String element1 = listIterator.next();

        Assert.assertEquals("two", element1);

        listIterator.remove();

        Verify.assertSize(3, composite);
    }

    @Override
    @Test
    public void subList()
    {
        MutableList<String> list = this.newWith("A", "B", "C", "D");
        MutableList<String> sublist = list.subList(1, 3);
        Verify.assertPostSerializedEqualsAndHashCode(sublist);
        Verify.assertSize(2, sublist);
        Verify.assertContainsAll(sublist, "B", "C");
        sublist.add("X");
        Verify.assertSize(3, sublist);
        Verify.assertContainsAll(sublist, "B", "C", "X");
        Verify.assertSize(5, list);
        Verify.assertContainsAll(list, "A", "B", "C", "X", "D");
        sublist.remove("X");
        Verify.assertContainsAll(sublist, "B", "C");
        Verify.assertContainsAll(list, "A", "B", "C", "D");
        Assert.assertEquals("C", sublist.set(1, "R"));
        Verify.assertContainsAll(sublist, "B", "R");
        Verify.assertContainsAll(list, "A", "B", "R", "D");
        sublist.clear();
        Verify.assertEmpty(sublist);
        Verify.assertContainsAll(list, "A", "D");
    }

    @Test
    public void notRandomAccess()
    {
        Assert.assertFalse(this.classUnderTest() instanceof RandomAccess);
    }

    @Test
    public void removingFromIteratorIsCool()
    {
        CompositeFastList<String> undertest = new CompositeFastList<String>();
        undertest.addAll(FastList.newListWith("a"));
        undertest.addAll(FastList.newListWith("b", "c", "d"));

        Iterator<String> iterator1 = undertest.iterator();
        iterator1.next();
        iterator1.next();
        iterator1.next();
        iterator1.remove();
        Assert.assertEquals("d", iterator1.next());
        Assert.assertEquals(FastList.newListWith("a", "b", "d"), undertest);

        Iterator<String> iterator2 = undertest.iterator();
        iterator2.next();
        iterator2.next();
        iterator2.remove();
        Assert.assertEquals(FastList.newListWith("a", "d"), undertest);

        Iterator<String> iterator3 = undertest.iterator();
        iterator3.next();
        iterator3.remove();
        Assert.assertEquals(FastList.newListWith("d"), undertest);
        iterator3.next();
        iterator3.remove();
        Assert.assertEquals(FastList.newList(), undertest);
    }

    @Test(expected = IllegalStateException.class)
    public void removingFromIteratorIsUncoolFromEmptyIterator()
    {
        new CompositeFastList<String>().iterator().remove();
    }
}
