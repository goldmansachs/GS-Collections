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

package com.gs.collections.impl.map.fixed;

import java.util.Map;

import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.FixedSizeMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link DoubletonMap}.
 */
public class DoubletonMapTest extends AbstractMemoryEfficientMutableMapTest
{
    @Override
    protected MutableMap<String, String> classUnderTest()
    {
        return new DoubletonMap<String, String>("1", "One", "2", "Two");
    }

    @Override
    @Test
    public void containsValue()
    {
        Assert.assertTrue(classUnderTest().containsValue("One"));
    }

    @Override
    @Test
    public void forEachKeyValue()
    {
        final MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "One", 2, "Two");
        map.forEachKeyValue(new Procedure2<Integer, String>()
        {
            public void value(Integer key, String value)
            {
                collection.add(key + value);
            }
        });
        Assert.assertEquals(FastList.newListWith("1One", "2Two"), collection);
    }

    @Override
    @Test
    public void nonUniqueWithKeyValue()
    {
        Twin<String> twin1 = Tuples.twin("1", "1");
        Twin<String> twin2 = Tuples.twin("2", "2");

        DoubletonMap<Twin<String>, Twin<String>> map = new DoubletonMap<Twin<String>, Twin<String>>(twin1, twin1, twin2, twin2);
        Assert.assertSame(map.getKey1(), twin1);
        Assert.assertSame(map.getKey2(), twin2);

        Twin<String> twin3 = Tuples.twin("1", "1");
        map.withKeyValue(twin3, twin3);
        Assert.assertSame(map.get(twin1), twin3);

        Twin<String> twin4 = Tuples.twin("2", "2");
        map.withKeyValue(twin4, twin4);
        Assert.assertSame(map.get(twin2), twin4);
    }

    @Override
    public void withKeyValue()
    {
        MutableMap<Integer, String> map1 = new DoubletonMap<Integer, String>(1, "A", 2, "B").withKeyValue(3, "C");
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "A", 2, "B", 3, "C"), map1);
        Verify.assertInstanceOf(TripletonMap.class, map1);

        MutableMap<Integer, String> map2 = new DoubletonMap<Integer, String>(1, "A", 2, "B");
        MutableMap<Integer, String> map2with = map2.withKeyValue(1, "AA");
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "AA", 2, "B"), map2with);
        Assert.assertSame(map2, map2with);
    }

    @Override
    public void withAllKeyValueArguments()
    {
        MutableMap<Integer, String> map1 = new DoubletonMap<Integer, String>(1, "A", 2, "B").withAllKeyValueArguments(
                Tuples.pair(1, "AA"), Tuples.pair(3, "C"));
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "AA", 2, "B", 3, "C"), map1);
        Verify.assertInstanceOf(TripletonMap.class, map1);

        MutableMap<Integer, String> map2 = new DoubletonMap<Integer, String>(1, "A", 2, "B");
        MutableMap<Integer, String> map2with = map2.withAllKeyValueArguments(Tuples.pair(1, "AA"));
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "AA", 2, "B"), map2with);
        Assert.assertSame(map2, map2with);
    }

    @Override
    public void withoutKey()
    {
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "A", 2, "B");
        MutableMap<Integer, String> mapWithout = map.withoutKey(3);
        Assert.assertSame(map, mapWithout);
        mapWithout = map.withoutKey(1);
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(2, "B"), mapWithout);
        Verify.assertInstanceOf(SingletonMap.class, mapWithout);
    }

    @Override
    public void withoutAllKeys()
    {
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "A", 2, "B");
        MutableMap<Integer, String> mapWithout = map.withoutAllKeys(FastList.newListWith(3, 4));
        Assert.assertSame(map, mapWithout);
        mapWithout = map.withoutAllKeys(FastList.newListWith(2, 3));
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues(1, "A"), mapWithout);
        Verify.assertInstanceOf(SingletonMap.class, mapWithout);
    }

    @Override
    @Test
    public void forEachValue()
    {
        MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "1", 2, "2");
        map.forEachValue(CollectionAddProcedure.on(collection));
        Assert.assertEquals(FastList.newListWith("1", "2"), collection);
    }

    @Override
    @Test
    public void forEach()
    {
        MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "1", 2, "2");
        map.forEach(CollectionAddProcedure.on(collection));
        Assert.assertEquals(FastList.newListWith("1", "2"), collection);
    }

    @Override
    @Test
    public void forEachKey()
    {
        MutableList<Integer> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "1", 2, "2");
        map.forEachKey(CollectionAddProcedure.on(collection));
        Assert.assertEquals(FastList.newListWith(1, 2), collection);
    }

    @Override
    @Test
    public void getIfAbsentPut()
    {
        final MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "1", 2, "2");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.getIfAbsentPut(4, new PassThruFunction0<String>("4"));
            }
        });
        Assert.assertEquals("1", map.getIfAbsentPut(1, new PassThruFunction0<String>("1")));
    }

    @Override
    @Test
    public void getIfAbsentPutWith()
    {
        final MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "1", 2, "2");
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                map.getIfAbsentPutWith(4, Functions.getToString(), 4);
            }
        });
        Assert.assertEquals("1", map.getIfAbsentPutWith(1, Functions.getToString(), 1));
    }

    @Override
    @Test
    public void getIfAbsent()
    {
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "1", 2, "2");
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsent(4, new PassThruFunction0<String>("4")));
        Assert.assertNull(map.get(4));
    }

    @Override
    @Test
    public void getIfAbsentWith()
    {
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "1", 2, "2");
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsentWith(4, Functions.getToString(), 4));
        Assert.assertNull(map.get(4));
    }

    @Override
    @Test
    public void ifPresentApply()
    {
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "1", 2, "2");
        Assert.assertNull(map.ifPresentApply(4, Functions.<String>getPassThru()));
        Assert.assertEquals("1", map.ifPresentApply(1, Functions.<String>getPassThru()));
        Assert.assertEquals("2", map.ifPresentApply(2, Functions.<String>getPassThru()));
    }

    @Override
    @Test
    public void notEmpty()
    {
        Assert.assertTrue(new DoubletonMap<Integer, String>(1, "1", 2, "2").notEmpty());
    }

    @Override
    @Test
    public void forEachWith()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        MutableMap<Integer, Integer> map = new DoubletonMap<Integer, Integer>(1, 1, 2, 2);
        map.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 10);
        Assert.assertEquals(FastList.newListWith(11, 12), result);
    }

    @Override
    @Test
    public void forEachWithIndex()
    {
        final MutableList<String> result = Lists.mutable.of();
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "One", 2, "Two");
        map.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String value, int index)
            {
                result.add(value);
                result.add(String.valueOf(index));
            }
        });
        Assert.assertEquals(FastList.newListWith("One", "0", "Two", "1"), result);
    }

    @Override
    @Test
    public void entrySet()
    {
        MutableList<String> result = Lists.mutable.of();
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "One", 2, "Two");
        for (Map.Entry<Integer, String> entry : map.entrySet())
        {
            result.add(entry.getValue());
        }
        Assert.assertEquals(FastList.newListWith("One", "Two"), result);
    }

    @Override
    @Test
    public void values()
    {
        MutableList<String> result = Lists.mutable.of();
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "One", 2, "Two");
        for (String value : map.values())
        {
            result.add(value);
        }
        Assert.assertEquals(FastList.newListWith("One", "Two"), result);
    }

    @Override
    @Test
    public void keySet()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "One", 2, "Two");
        for (Integer key : map.keySet())
        {
            result.add(key);
        }
        Assert.assertEquals(FastList.newListWith(1, 2), result);
    }

    @Override
    @Test
    public void testToString()
    {
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "One", 2, "Two");
        Assert.assertEquals("{1=One, 2=Two}", map.toString());
    }

    @Override
    @Test
    public void asLazyKeys()
    {
        MutableList<Integer> keys = Maps.fixedSize.of(1, 1, 2, 2).keysView().toSortedList();
        Assert.assertEquals(FastList.newListWith(1, 2), keys);
    }

    @Override
    @Test
    public void asLazyValues()
    {
        MutableList<Integer> values = Maps.fixedSize.of(1, 1, 2, 2).valuesView().toSortedList();
        Assert.assertEquals(FastList.newListWith(1, 2), values);
    }

    @Override
    @Test
    public void testEqualsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(
                UnifiedMap.newWithKeysValues("1", "One", "2", "Two"),
                this.classUnderTest());
    }

    @Override
    @Test
    public void select()
    {
        MutableMap<String, String> map = this.classUnderTest();

        MutableMap<String, String> empty = map.select(Predicates2.alwaysFalse());
        Verify.assertInstanceOf(EmptyMap.class, empty);

        MutableMap<String, String> full = map.select(Predicates2.alwaysTrue());
        Assert.assertEquals(map, full);

        MutableMap<String, String> one = map.select(new Predicate2<String, String>()
        {
            public boolean accept(String argument1, String argument2)
            {
                return "1".equals(argument1);
            }
        });
        Verify.assertInstanceOf(SingletonMap.class, one);
        Assert.assertEquals(new SingletonMap<String, String>("1", "One"), one);

        MutableMap<String, String> two = map.select(new Predicate2<String, String>()
        {
            public boolean accept(String argument1, String argument2)
            {
                return "2".equals(argument1);
            }
        });
        Verify.assertInstanceOf(SingletonMap.class, two);
        Assert.assertEquals(new SingletonMap<String, String>("2", "Two"), two);
    }

    @Override
    @Test
    public void reject()
    {
        MutableMap<String, String> map = this.classUnderTest();

        MutableMap<String, String> empty = map.reject(Predicates2.alwaysTrue());
        Verify.assertInstanceOf(EmptyMap.class, empty);

        MutableMap<String, String> full = map.reject(Predicates2.alwaysFalse());
        Verify.assertInstanceOf(DoubletonMap.class, full);
        Assert.assertEquals(map, full);

        MutableMap<String, String> one = map.reject(new Predicate2<String, String>()
        {
            public boolean accept(String argument1, String argument2)
            {
                return "2".equals(argument1);
            }
        });
        Verify.assertInstanceOf(SingletonMap.class, one);
        Assert.assertEquals(new SingletonMap<String, String>("1", "One"), one);

        MutableMap<String, String> two = map.reject(new Predicate2<String, String>()
        {
            public boolean accept(String argument1, String argument2)
            {
                return "1".equals(argument1);
            }
        });
        Verify.assertInstanceOf(SingletonMap.class, two);
        Assert.assertEquals(new SingletonMap<String, String>("2", "Two"), two);
    }

    @Override
    @Test
    public void detect()
    {
        MutableMap<String, String> map = this.classUnderTest();

        Pair<String, String> one = map.detect(Predicates2.alwaysTrue());
        Assert.assertEquals(Tuples.pair("1", "One"), one);

        Pair<String, String> two = map.detect(new Predicate2<String, String>()
        {
            public boolean accept(String argument1, String argument2)
            {
                return "2".equals(argument1);
            }
        });
        Assert.assertEquals(Tuples.pair("2", "Two"), two);

        Assert.assertNull(map.detect(Predicates2.alwaysFalse()));
    }

    @Override
    protected <K, V> FixedSizeMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return new DoubletonMap<K, V>(key1, value1, key2, value2);
    }

    @Override
    protected <K, V> FixedSizeMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new DoubletonMap<K, V>(key1, value1, key2, value2);
    }

    @Override
    @Test
    public void iterator()
    {
        MutableList<String> collection = Lists.mutable.of();
        MutableMap<Integer, String> map = new DoubletonMap<Integer, String>(1, "1", 2, "2");
        for (String eachValue : map)
        {
            collection.add(eachValue);
        }
        Assert.assertEquals(FastList.newListWith("1", "2"), collection);
    }
}
