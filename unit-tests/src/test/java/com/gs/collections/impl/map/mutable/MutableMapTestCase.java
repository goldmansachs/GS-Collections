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

package com.gs.collections.impl.map.mutable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.MapIterableTestCase;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.test.domain.Key;
import com.gs.collections.impl.tuple.ImmutableEntry;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * Abstract JUnit TestCase for {@link MutableMap}s.
 */
public abstract class MutableMapTestCase extends MapIterableTestCase
{
    @Override
    protected <K, V> MutableMap<K, V> newMap()
    {
        return null;
    }

    @Override
    protected <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return null;
    }

    @Override
    protected <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        return null;
    }

    @Override
    protected <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return null;
    }

    protected <K, V> MutableMap<K, V> newMapWithKeyValue(K key, V value)
    {
        return null;
    }

    @Test
    public void testNewMap()
    {
        MutableMap<Integer, Integer> map = this.newMap();
        Verify.assertEmpty(map);
        Verify.assertSize(0, map);
    }

    @Test
    public void testNewMapWithKeyValue()
    {
        MutableMap<Integer, String> map = this.newMapWithKeyValue(1, "One");
        Verify.assertNotEmpty(map);
        Verify.assertSize(1, map);
        Verify.assertContainsKeyValue(1, "One", map);
    }

    @Test
    public void toImmutable()
    {
        MutableMap<Integer, String> map = this.newMapWithKeyValue(1, "One");
        ImmutableMap<Integer, String> immutable = map.toImmutable();
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, "One"), immutable);
    }

    @Test
    public void newMapWithWith()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two");
        Verify.assertNotEmpty(map);
        Verify.assertSize(2, map);
        Verify.assertContainsAllKeyValues(map, 1, "One", 2, "Two");
    }

    @Test
    public void newMapWithWithWith()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three");
        Verify.assertNotEmpty(map);
        Verify.assertSize(3, map);
        Verify.assertContainsAllKeyValues(map, 1, "One", 2, "Two", 3, "Three");
    }

    @Test
    public void newMapWithWithWithWith()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        Verify.assertNotEmpty(map);
        Verify.assertSize(4, map);
        Verify.assertContainsAllKeyValues(map, 1, "One", 2, "Two", 3, "Three", 4, "Four");
    }

    @Test
    public void clear()
    {
        MutableMap<Integer, Object> map =
                this.<Integer, Object>newMapWithKeysValues(1, "One", 2, "Two", 3, "Three");
        map.clear();
        Verify.assertEmpty(map);

        MutableMap<Object, Object> map2 = this.newMap();
        map2.clear();
        Verify.assertEmpty(map2);
    }

    @Test
    public void iterator()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Iterator<Integer> iterator = map.iterator();
        Assert.assertTrue(iterator.hasNext());
        int sum = 0;
        while (iterator.hasNext())
        {
            sum += iterator.next();
        }
        Assert.assertFalse(iterator.hasNext());
        Assert.assertEquals(6, sum);
    }

    @Test
    public void removeObject()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        map.remove("Two");
        Verify.assertContainsAllKeyValues(map, "One", 1, "Three", 3);
    }

    @Test
    public void removeFromEntrySet()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertTrue(map.entrySet().remove(ImmutableEntry.of("Two", 2)));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);

        Assert.assertFalse(map.entrySet().remove(ImmutableEntry.of("Four", 4)));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);

        Assert.assertFalse(map.entrySet().remove(null));

        MutableMap<String, Integer> mapWithNullKey = this.newMapWithKeysValues("One", 1, null, 2, "Three", 3);
        Assert.assertTrue(mapWithNullKey.entrySet().remove(new ImmutableEntry<String, Integer>(null, 2)));
    }

    @Test
    public void removeAllFromEntrySet()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertTrue(map.entrySet().removeAll(FastList.newListWith(
                ImmutableEntry.of("One", 1),
                ImmutableEntry.of("Three", 3))));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("Two", 2), map);

        Assert.assertFalse(map.entrySet().removeAll(FastList.newListWith(ImmutableEntry.of("Four", 4))));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("Two", 2), map);

        Assert.assertFalse(map.entrySet().remove(null));

        MutableMap<String, Integer> mapWithNullKey = this.newMapWithKeysValues("One", 1, null, 2, "Three", 3);
        Assert.assertTrue(mapWithNullKey.entrySet().removeAll(FastList.newListWith(ImmutableEntry.of(null, 2))));
    }

    @Test
    public void retainAllFromEntrySet()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.entrySet().retainAll(FastList.newListWith(
                ImmutableEntry.of("One", 1),
                ImmutableEntry.of("Two", 2),
                ImmutableEntry.of("Three", 3),
                ImmutableEntry.of("Four", 4))));

        Assert.assertTrue(map.entrySet().retainAll(FastList.newListWith(
                ImmutableEntry.of("One", 1),
                ImmutableEntry.of("Three", 3),
                ImmutableEntry.of("Four", 4))));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void clearEntrySet()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        map.entrySet().clear();
        Verify.assertEmpty(map);
    }

    @Test
    public void entrySetEqualsAndHashCode()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Verify.assertEqualsAndHashCode(
                UnifiedSet.newSetWith(
                        ImmutableEntry.of("One", 1),
                        ImmutableEntry.of("Two", 2),
                        ImmutableEntry.of("Three", 3)),
                map.entrySet());
    }

    @Test
    public void removeFromKeySet()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.keySet().remove("Four"));

        Assert.assertTrue(map.keySet().remove("Two"));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void removeNullFromKeySet()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.keySet().remove(null));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Two", 2, "Three", 3), map);
        map.put(null, 4);
        Assert.assertTrue(map.keySet().remove(null));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Two", 2, "Three", 3), map);
    }

    @Test
    public void removeAllFromKeySet()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.keySet().removeAll(FastList.newListWith("Four")));

        Assert.assertTrue(map.keySet().removeAll(FastList.newListWith("Two", "Four")));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void retainAllFromKeySet()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.keySet().retainAll(FastList.newListWith("One", "Two", "Three", "Four")));

        Assert.assertTrue(map.keySet().retainAll(FastList.newListWith("One", "Three")));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void clearKeySet()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        map.keySet().clear();
        Verify.assertEmpty(map);
    }

    @Test
    public void keySetEqualsAndHashCode()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Verify.assertEqualsAndHashCode(UnifiedSet.newSetWith("One", "Two", "Three"), map.keySet());
    }

    @Test
    public void keySetToArray()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        MutableList<String> expected = FastList.newListWith("One", "Two", "Three").toSortedList();
        Set<String> keySet = map.keySet();
        Assert.assertEquals(expected, FastList.newListWith(keySet.toArray()).toSortedList());
        Assert.assertEquals(expected, FastList.newListWith(keySet.toArray(new String[keySet.size()])).toSortedList());
    }

    @Test
    public void removeFromValues()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.values().remove(4));

        Assert.assertTrue(map.values().remove(2));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void removeNullFromValues()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.values().remove(null));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Two", 2, "Three", 3), map);
        map.put("Four", null);
        Assert.assertTrue(map.values().remove(null));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Two", 2, "Three", 3), map);
    }

    @Test
    public void removeAllFromValues()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.values().removeAll(FastList.newListWith(4)));

        Assert.assertTrue(map.values().removeAll(FastList.newListWith(2, 4)));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void retainAllFromValues()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertFalse(map.values().retainAll(FastList.newListWith(1, 2, 3, 4)));

        Assert.assertTrue(map.values().retainAll(FastList.newListWith(1, 3)));
        Assert.assertEquals(UnifiedMap.newWithKeysValues("One", 1, "Three", 3), map);
    }

    @Test
    public void forEachKeyValue()
    {
        final MutableBag<String> result = Bags.mutable.of();
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three");
        map.forEachKeyValue(new Procedure2<Integer, String>()
        {
            public void value(Integer key, String value)
            {
                result.add(key + value);
            }
        });
        Assert.assertEquals(Bags.mutable.of("1One", "2Two", "3Three"), result);
    }

    @Test
    public void put()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two");
        Assert.assertNull(map.put(3, "Three"));
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, "One", 2, "Two", 3, "Three"), map);
    }

    @Test
    public void putAll()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "2");
        MutableMap<Integer, String> toAdd = this.newMapWithKeysValues(2, "Two", 3, "Three");

        map.putAll(toAdd);
        Verify.assertSize(3, map);
        Verify.assertContainsAllKeyValues(map, 1, "One", 2, "Two", 3, "Three");

        //Testing JDK map
        MutableMap<Integer, String> map2 = this.newMapWithKeysValues(1, "One", 2, "2");
        HashMap<Integer, String> hashMaptoAdd = new HashMap(toAdd);
        map2.putAll(hashMaptoAdd);
        Verify.assertSize(3, map2);
        Verify.assertContainsAllKeyValues(map2, 1, "One", 2, "Two", 3, "Three");
    }

    @Test
    public void putAllFromCollection()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");
        MutableList<Integer> toAdd = FastList.newListWith(2, 3);
        map.collectKeysAndValues(toAdd, Functions.getIntegerPassThru(), Functions.getToString());
        Verify.assertSize(3, map);
        Verify.assertContainsAllKeyValues(map, 1, "1", 2, "2", 3, "3");
    }

    @Test
    public void removeKey()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "Two");

        Assert.assertEquals("1", map.removeKey(1));
        Verify.assertSize(1, map);
        Verify.denyContainsKey(1, map);

        Assert.assertNull(map.removeKey(42));
        Verify.assertSize(1, map);

        Assert.assertEquals("Two", map.removeKey(2));
        Verify.assertEmpty(map);
    }

    @Test
    public void getIfAbsentPut()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsentPut(4, new PassThruFunction0<String>("4")));
        Assert.assertEquals("3", map.getIfAbsentPut(3, new PassThruFunction0<String>("3")));
        Verify.assertContainsKeyValue(4, "4", map);
    }

    @Test
    public void getIfAbsentPutWith()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertNull(map.get(4));
        Assert.assertEquals("4", map.getIfAbsentPutWith(4, Functions.getToString(), 4));
        Assert.assertEquals("3", map.getIfAbsentPutWith(3, Functions.getToString(), 3));
        Verify.assertContainsKeyValue(4, "4", map);
    }

    @Test
    public void getIfAbsentPut_block_throws()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Verify.assertThrows(RuntimeException.class, new Runnable()
        {
            public void run()
            {
                map.getIfAbsentPut(4, new Function0<String>()
                {
                    public String value()
                    {
                        throw new RuntimeException();
                    }
                });
            }
        });
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }

    @Test
    public void getIfAbsentPutWith_block_throws()
    {
        final MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Verify.assertThrows(RuntimeException.class, new Runnable()
        {
            public void run()
            {
                map.getIfAbsentPutWith(4, new Function<Object, String>()
                {
                    public String valueOf(Object object)
                    {
                        throw new RuntimeException();
                    }
                }, null);
            }
        });
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }

    @Test
    public void getKeysAndGetValues()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Verify.assertContainsAll(map.keySet(), 1, 2, 3);
        Verify.assertContainsAll(map.values(), "1", "2", "3");
    }

    @Test
    public void testEquals()
    {
        MutableMap<Integer, String> map1 = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        MutableMap<Integer, String> map2 = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        MutableMap<Integer, String> map3 = this.newMapWithKeysValues(2, "2", 3, "3", 4, "4");
        Verify.assertMapsEqual(map1, map2);
        Verify.assertNotEquals(map2, map3);
    }

    @Test
    public void testHashCode()
    {
        MutableMap<Integer, String> map1 = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        MutableMap<Integer, String> map2 = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Verify.assertEqualsAndHashCode(map1, map2);
    }

    @Test
    public void equalsAndHashCode()
    {
        Map<Integer, String> hashMap = new HashMap<Integer, String>();
        hashMap.put(1, "One");
        hashMap.put(2, "Two");

        MutableMap<Integer, String> mutableMap = this.newMapWithKeysValues(1, "One", 2, "Two");

        Verify.assertEqualsAndHashCode(hashMap, mutableMap);
    }

    @Test
    public void serialization()
    {
        MutableMap<Integer, String> original = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        MutableMap<Integer, String> copy = SerializeTestHelper.serializeDeserialize(original);
        Verify.assertSize(3, copy);
        Verify.assertMapsEqual(original, copy);
    }

    @Test
    public void containsValue()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertTrue(map.containsValue("1"));
        Assert.assertFalse(map.containsValue("4"));
    }

    @Test
    public void containsKey()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertTrue(map.containsKey(1));
        Assert.assertFalse(map.containsKey(4));
    }

    @Test
    public void newEmpty()
    {
        MutableMap<Integer, Integer> map = this.newMapWithKeysValues(1, 1, 2, 2);
        Verify.assertEmpty(map.newEmpty());
    }

    @Test
    public void keysView()
    {
        MutableList<Integer> keys = this.newMapWithKeysValues(1, 1, 2, 2).keysView().toSortedList();
        Assert.assertEquals(FastList.newListWith(1, 2), keys);
    }

    @Test
    public void valuesView()
    {
        MutableList<Integer> values = this.newMapWithKeysValues(1, 1, 2, 2).valuesView().toSortedList();
        Assert.assertEquals(FastList.newListWith(1, 2), values);
    }

    @Test
    public void asUnmodifiable()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                MutableMapTestCase.this.newMapWithKeysValues(1, 1, 2, 2).asUnmodifiable().put(3, 3);
            }
        });
    }

    @Test
    public void asSynchronized()
    {
        MutableMap<Integer, Integer> map = this.newMapWithKeysValues(1, 1, 2, 2).asSynchronized();
        Verify.assertInstanceOf(SynchronizedMutableMap.class, map);
    }

    @Test
    public void test_toString()
    {
        MapIterable<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three");

        String stringToSearch = map.toString();
        Verify.assertContains("1=One", stringToSearch);
        Verify.assertContains("2=Two", stringToSearch);
        Verify.assertContains("3=Three", stringToSearch);
    }

    @Test
    public void testClone()
    {
        MutableMap<Integer, String> map = this.newMapWithKeysValues(1, "One", 2, "Two");
        MutableMap<Integer, String> clone = map.clone();
        Assert.assertNotSame(map, clone);
        Verify.assertEqualsAndHashCode(map, clone);
    }

    @Test
    public void keyPreservation()
    {
        Key key = new Key("key");

        Key duplicateKey1 = new Key("key");
        MutableMap<Key, Integer> map1 = this.newMapWithKeysValues(key, 1, duplicateKey1, 2);
        Verify.assertSize(1, map1);
        Verify.assertContainsKeyValue(key, 2, map1);
        Assert.assertSame(key, map1.keysView().getFirst());

        Key duplicateKey2 = new Key("key");
        MutableMap<Key, Integer> map2 = this.newMapWithKeysValues(key, 1, duplicateKey1, 2, duplicateKey2, 3);
        Verify.assertSize(1, map2);
        Verify.assertContainsKeyValue(key, 3, map2);
        Assert.assertSame(key, map1.keysView().getFirst());

        Key duplicateKey3 = new Key("key");
        MutableMap<Key, Integer> map3 = this.newMapWithKeysValues(key, 1, new Key("not a dupe"), 2, duplicateKey3, 3);
        Verify.assertSize(2, map3);
        Verify.assertContainsAllKeyValues(map3, key, 3, new Key("not a dupe"), 2);
        Assert.assertSame(key, map3.keysView().detect(Predicates.equal(key)));

        Key duplicateKey4 = new Key("key");
        MutableMap<Key, Integer> map4 = this.newMapWithKeysValues(key, 1, new Key("still not a dupe"), 2, new Key("me neither"), 3, duplicateKey4, 4);
        Verify.assertSize(3, map4);
        Verify.assertContainsAllKeyValues(map4, key, 4, new Key("still not a dupe"), 2, new Key("me neither"), 3);
        Assert.assertSame(key, map4.keysView().detect(Predicates.equal(key)));

        MutableMap<Key, Integer> map5 = this.newMapWithKeysValues(key, 1, duplicateKey1, 2, duplicateKey3, 3, duplicateKey4, 4);
        Verify.assertSize(1, map5);
        Verify.assertContainsKeyValue(key, 4, map5);
        Assert.assertSame(key, map5.keysView().getFirst());
    }

    @Override
    @Test
    public void partition_value()
    {
        MapIterable<String, Integer> map = this.newMapWithKeysValues(
                "A", 1,
                "B", 2,
                "C", 3,
                "D", 4);
        PartitionIterable<Integer> partition = map.partition(IntegerPredicates.isEven());
        Assert.assertEquals(iSet(2, 4), partition.getSelected().toSet());
        Assert.assertEquals(iSet(1, 3), partition.getRejected().toSet());
    }

    @Test
    public void withKeyValue()
    {
        MutableMap<String, Integer> map = this.newMapWithKeyValue("A", 1);

        MutableMap<String, Integer> mapWith = map.withKeyValue("B", 2);
        Assert.assertSame(map, mapWith);
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues("A", 1, "B", 2), mapWith);

        MutableMap<String, Integer> mapWith2 = mapWith.withKeyValue("A", 11);
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues("A", 11, "B", 2), mapWith);
    }

    @Test
    public void withAllKeyValues()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("A", 1, "B", 2);
        MutableMap<String, Integer> mapWith = map.withAllKeyValues(
                FastList.<Pair<String, Integer>>newListWith(Tuples.pair("B", 22), Tuples.pair("C", 3)));
        Assert.assertSame(map, mapWith);
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues("A", 1, "B", 22, "C", 3), mapWith);
    }

    @Test
    public void withAllKeyValueArguments()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("A", 1, "B", 2);
        MutableMap<String, Integer> mapWith = map.withAllKeyValueArguments(Tuples.pair("B", 22), Tuples.pair("C", 3));
        Assert.assertSame(map, mapWith);
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues("A", 1, "B", 22, "C", 3), mapWith);
    }

    @Test
    public void withoutKey()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("A", 1, "B", 2);
        MutableMap<String, Integer> mapWithout = map.withoutKey("B");
        Assert.assertSame(map, mapWithout);
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues("A", 1), mapWithout);
    }

    @Test
    public void withoutAllKeys()
    {
        MutableMap<String, Integer> map = this.newMapWithKeysValues("A", 1, "B", 2, "C", 3);
        MutableMap<String, Integer> mapWithout = map.withoutAllKeys(FastList.newListWith("A", "C"));
        Assert.assertSame(map, mapWithout);
        Verify.assertMapsEqual(UnifiedMap.newWithKeysValues("B", 2), mapWithout);
    }
}
