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

package com.gs.collections.impl.map.immutable;

import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ImmutableTripletonMap}.
 */
public class ImmutableTripletonMapTest extends ImmutableMemoryEfficientMapTestCase
{
    @Override
    protected ImmutableMap<Integer, String> classUnderTest()
    {
        return new ImmutableTripletonMap<Integer, String>(1, "1", 2, "2", 3, "3");
    }

    @Override
    protected int size()
    {
        return 3;
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();
        ImmutableMap<Integer, String> map1 = new ImmutableTripletonMap<Integer, String>(1, "One", 2, "Two", 3, "Three");
        ImmutableMap<Integer, String> map2 = new ImmutableTripletonMap<Integer, String>(1, "One", 2, "Two", 3, "Three");
        Verify.assertEqualsAndHashCode(map1, map2);
    }

    @Override
    @Test
    public void forEachValue()
    {
        super.forEachValue();
        MutableList<String> collection = Lists.mutable.of();
        this.classUnderTest().forEachValue(CollectionAddProcedure.on(collection));
        Assert.assertEquals(FastList.newListWith("1", "2", "3"), collection);
    }

    @Override
    @Test
    public void forEachKey()
    {
        super.forEachKey();
        MutableList<Integer> collection = Lists.mutable.of();
        this.classUnderTest().forEachKey(CollectionAddProcedure.on(collection));
        Assert.assertEquals(FastList.newListWith(1, 2, 3), collection);
    }

    @Override
    @Test
    public void getIfAbsent()
    {
        super.getIfAbsent();
        ImmutableMap<Integer, String> map = this.classUnderTest();
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsent(4, new PassThruFunction0<String>("4")));
        Assert.assertNull(map.get(4));
    }

    @Override
    @Test
    public void ifPresentApply()
    {
        super.ifPresentApply();
        ImmutableMap<Integer, String> map = this.classUnderTest();
        Assert.assertNull(map.ifPresentApply(4, Functions.<String>getPassThru()));
        Assert.assertEquals("1", map.ifPresentApply(1, Functions.<String>getPassThru()));
        Assert.assertEquals("2", map.ifPresentApply(2, Functions.<String>getPassThru()));
        Assert.assertEquals("3", map.ifPresentApply(3, Functions.<String>getPassThru()));
    }

    @Override
    @Test
    public void notEmpty()
    {
        super.notEmpty();
        Assert.assertTrue(this.classUnderTest().notEmpty());
    }

    @Override
    @Test
    public void forEachWith()
    {
        super.forEachWith();
        final MutableList<Integer> result = Lists.mutable.of();
        ImmutableMap<Integer, Integer> map = new ImmutableTripletonMap<Integer, Integer>(1, 1, 2, 2, 3, 3);
        map.forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 10);
        Assert.assertEquals(FastList.newListWith(11, 12, 13), result);
    }

    @Override
    @Test
    public void forEachWithIndex()
    {
        super.forEachWithIndex();
        final MutableList<String> result = Lists.mutable.of();
        ImmutableMap<Integer, String> map = new ImmutableTripletonMap<Integer, String>(1, "One", 2, "Two", 3, "Three");
        map.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String value, int index)
            {
                result.add(value);
                result.add(String.valueOf(index));
            }
        });
        Assert.assertEquals(FastList.newListWith("One", "0", "Two", "1", "Three", "2"), result);
    }

    @Override
    @Test
    public void keyValuesView()
    {
        super.keyValuesView();
        MutableList<String> result = Lists.mutable.of();
        ImmutableMap<Integer, String> map = new ImmutableTripletonMap<Integer, String>(1, "One", 2, "Two", 3, "Three");
        for (Pair<Integer, String> keyValue : map.keyValuesView())
        {
            result.add(keyValue.getTwo());
        }
        Assert.assertEquals(FastList.newListWith("One", "Two", "Three"), result);
    }

    @Override
    @Test
    public void valuesView()
    {
        super.valuesView();
        MutableList<String> result = Lists.mutable.of();
        ImmutableMap<Integer, String> map = new ImmutableTripletonMap<Integer, String>(1, "One", 2, "Two", 3, "Three");
        for (String value : map.valuesView())
        {
            result.add(value);
        }
        Assert.assertEquals(FastList.newListWith("One", "Two", "Three"), result);
    }

    @Override
    @Test
    public void keysView()
    {
        super.keysView();
        MutableList<Integer> result = Lists.mutable.of();
        ImmutableMap<Integer, String> map = new ImmutableTripletonMap<Integer, String>(1, "One", 2, "Two", 3, "Three");
        for (Integer key : map.keysView())
        {
            result.add(key);
        }
        Assert.assertEquals(FastList.newListWith(1, 2, 3), result);
    }

    @Override
    @Test
    public void testToString()
    {
        ImmutableMap<Integer, String> map = new ImmutableTripletonMap<Integer, String>(1, "One", 2, "Two", 3, "Three");
        Assert.assertEquals("{1=One, 2=Two, 3=Three}", map.toString());
    }

    @Override
    public void select()
    {
        ImmutableMap<Integer, String> map = this.classUnderTest();

        ImmutableMap<Integer, String> empty = map.select(Predicates2.alwaysFalse());
        Verify.assertInstanceOf(ImmutableEmptyMap.class, empty);

        ImmutableMap<Integer, String> full = map.select(Predicates2.alwaysTrue());
        Verify.assertInstanceOf(ImmutableTripletonMap.class, full);
        Assert.assertEquals(map, full);

        ImmutableMap<Integer, String> one = map.select(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "1".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableSingletonMap.class, one);
        Assert.assertEquals(new ImmutableSingletonMap<Integer, String>(1, "1"), one);

        ImmutableMap<Integer, String> two = map.select(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "2".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableSingletonMap.class, two);
        Assert.assertEquals(new ImmutableSingletonMap<Integer, String>(2, "2"), two);

        ImmutableMap<Integer, String> three = map.select(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "3".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableSingletonMap.class, three);
        Assert.assertEquals(new ImmutableSingletonMap<Integer, String>(3, "3"), three);

        ImmutableMap<Integer, String> oneAndThree = map.select(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "1".equals(argument2) || "3".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableDoubletonMap.class, oneAndThree);
        Assert.assertEquals(new ImmutableDoubletonMap<Integer, String>(1, "1", 3, "3"), oneAndThree);

        ImmutableMap<Integer, String> oneAndTwo = map.select(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "1".equals(argument2) || "2".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableDoubletonMap.class, oneAndTwo);
        Assert.assertEquals(new ImmutableDoubletonMap<Integer, String>(1, "1", 2, "2"), oneAndTwo);

        ImmutableMap<Integer, String> twoAndThree = map.select(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "2".equals(argument2) || "3".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableDoubletonMap.class, twoAndThree);
        Assert.assertEquals(new ImmutableDoubletonMap<Integer, String>(2, "2", 3, "3"), twoAndThree);
    }

    @Override
    public void reject()
    {
        ImmutableMap<Integer, String> map = this.classUnderTest();

        ImmutableMap<Integer, String> empty = map.reject(Predicates2.alwaysTrue());
        Verify.assertInstanceOf(ImmutableEmptyMap.class, empty);

        ImmutableMap<Integer, String> full = map.reject(Predicates2.alwaysFalse());
        Verify.assertInstanceOf(ImmutableTripletonMap.class, full);
        Assert.assertEquals(map, full);

        ImmutableMap<Integer, String> one = map.reject(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "2".equals(argument2) || "3".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableSingletonMap.class, one);
        Assert.assertEquals(new ImmutableSingletonMap<Integer, String>(1, "1"), one);

        ImmutableMap<Integer, String> two = map.reject(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "1".equals(argument2) || "3".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableSingletonMap.class, two);
        Assert.assertEquals(new ImmutableSingletonMap<Integer, String>(2, "2"), two);

        ImmutableMap<Integer, String> three = map.reject(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "1".equals(argument2) || "2".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableSingletonMap.class, three);
        Assert.assertEquals(new ImmutableSingletonMap<Integer, String>(3, "3"), three);

        ImmutableMap<Integer, String> oneAndThree = map.reject(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "2".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableDoubletonMap.class, oneAndThree);
        Assert.assertEquals(new ImmutableDoubletonMap<Integer, String>(1, "1", 3, "3"), oneAndThree);

        ImmutableMap<Integer, String> oneAndTwo = map.reject(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "3".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableDoubletonMap.class, oneAndTwo);
        Assert.assertEquals(new ImmutableDoubletonMap<Integer, String>(1, "1", 2, "2"), oneAndTwo);

        ImmutableMap<Integer, String> twoAndThree = map.reject(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "1".equals(argument2);
            }
        });
        Verify.assertInstanceOf(ImmutableDoubletonMap.class, twoAndThree);
        Assert.assertEquals(new ImmutableDoubletonMap<Integer, String>(2, "2", 3, "3"), twoAndThree);
    }

    @Override
    public void detect()
    {
        ImmutableMap<Integer, String> map = this.classUnderTest();

        Pair<Integer, String> one = map.detect(Predicates2.alwaysTrue());
        Assert.assertEquals(Tuples.pair(1, "1"), one);

        Pair<Integer, String> two = map.detect(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "2".equals(argument2);
            }
        });
        Assert.assertEquals(Tuples.pair(2, "2"), two);

        Pair<Integer, String> three = map.detect(new Predicate2<Integer, String>()
        {
            public boolean accept(Integer argument1, String argument2)
            {
                return "3".equals(argument2);
            }
        });
        Assert.assertEquals(Tuples.pair(3, "3"), three);

        Assert.assertNull(map.detect(Predicates2.alwaysFalse()));
    }

    @Override
    protected <K, V> ImmutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return new ImmutableTripletonMap<K, V>(key1, value1, key2, value2, null, null);
    }

    @Override
    protected <K, V> ImmutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return new ImmutableTripletonMap<K, V>(key1, value1, key2, value2, key3, value3);
    }
}
