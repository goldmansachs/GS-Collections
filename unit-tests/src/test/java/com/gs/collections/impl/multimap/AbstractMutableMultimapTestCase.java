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

package com.gs.collections.impl.multimap;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Functions0;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Helper class for testing {@link Multimap}s
 */
public abstract class AbstractMutableMultimapTestCase extends AbstractMultimapTestCase
{
    @Override
    public abstract <K, V> MutableMultimap<K, V> newMultimap();

    @Override
    public abstract <K, V> MutableMultimap<K, V> newMultimapWithKeyValue(
            K key, V value);

    @Override
    public abstract <K, V> MutableMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2);

    @Override
    public abstract <K, V> MutableMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3);

    @Override
    public abstract <K, V> MutableMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4);

    protected abstract <V> MutableCollection<V> createCollection(V... args);

    @Test
    public void testPutAndGrowMultimap()
    {
        MutableMultimap<Integer, Integer> multimap = this.newMultimap();
        multimap.put(1, 1);
        multimap.put(2, 2);
        Verify.assertContainsEntry(1, 1, multimap);
        Verify.assertContainsEntry(2, 2, multimap);
    }

    @Override
    @Test
    public void testNewMultimap()
    {
        Multimap<Integer, Integer> multimap = this.newMultimap();
        Verify.assertEmpty(multimap);
        Verify.assertSize(0, multimap);
    }

    @Override
    @Test
    public void testNewMultimapWithKeyValue()
    {
        Multimap<Integer, String> multimap = this.newMultimapWithKeyValue(1, "One");
        Verify.assertNotEmpty(multimap);
        Verify.assertSize(1, multimap);
        Verify.assertContainsEntry(1, "One", multimap);
    }

    @Override
    @Test
    public void testNewMultimapWithWith()
    {
        Multimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "One", 2, "Two");
        Verify.assertNotEmpty(multimap);
        Verify.assertSize(2, multimap);
        Verify.assertContainsAllEntries(multimap, 1, "One", 2, "Two");
    }

    @Override
    @Test
    public void testNewMultimapWithWithWith()
    {
        Multimap<Integer, String> multimap =
                this.newMultimapWithKeysValues(1, "One", 2, "Two", 3, "Three");
        Verify.assertNotEmpty(multimap);
        Verify.assertSize(3, multimap);
        Verify.assertContainsAllEntries(multimap, 1, "One", 2, "Two", 3, "Three");
    }

    @Override
    @Test
    public void testNewMultimapWithWithWithWith()
    {
        Multimap<Integer, String> multimap =
                this.newMultimapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        Verify.assertNotEmpty(multimap);
        Verify.assertSize(4, multimap);
        Verify.assertContainsAllEntries(multimap, 1, "One", 2, "Two", 3, "Three", 4, "Four");
    }

    @Override
    @Test
    public void isEmpty()
    {
        Verify.assertEmpty(this.newMultimap());
        Verify.assertNotEmpty(this.newMultimapWithKeyValue(1, 1));
        Assert.assertTrue(this.newMultimapWithKeyValue(1, 1).notEmpty());
    }

    @Test
    public void testClear()
    {
        MutableMultimap<Integer, Object> multimap =
                this.<Integer, Object>newMultimapWithKeysValues(1, "One", 2, "Two", 3, "Three", null, null);
        multimap.clear();
        Verify.assertEmpty(multimap);
    }

    @Test
    public void testRemoveObject()
    {
        MutableMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        multimap.removeAll("Two");
        Verify.assertContainsAllEntries(multimap, "One", 1, "Three", 3);
    }

    @Override
    @Test
    public void forEachKeyValue()
    {
        final MutableList<String> collection = Lists.mutable.of();
        Multimap<Integer, String> multimap =
                this.newMultimapWithKeysValues(1, "One", 2, "Two", 3, "Three");
        multimap.forEachKeyValue(new Procedure2<Integer, String>()
        {
            public void value(Integer key, String value)
            {
                collection.add(key + value);
            }
        });
        Assert.assertEquals(FastList.newListWith("1One", "2Two", "3Three"), collection);
    }

    @Override
    @Test
    public void forEachValue()
    {
        MutableList<String> collection = Lists.mutable.of();
        Multimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        multimap.forEachValue(CollectionAddProcedure.on(collection));
        Assert.assertEquals(FastList.newListWith("1", "2", "3"), collection);
    }

    @Override
    @Test
    public void forEachKey()
    {
        MutableList<Integer> collection = Lists.mutable.of();
        Multimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        multimap.forEachKey(CollectionAddProcedure.on(collection));
        Assert.assertEquals(FastList.newListWith(1, 2, 3), collection);
    }

    @Test
    public void testPutAll()
    {
        MutableMultimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "One", 2, "2");
        Multimap<Integer, String> toAdd = this.newMultimapWithKeysValues(2, "Two", 3, "Three");
        Multimap<Integer, String> toAddImmutable = this.newMultimapWithKeysValues(4, "Four", 5, "Five").toImmutable();
        Assert.assertTrue(multimap.putAll(toAdd));
        Assert.assertTrue(multimap.putAll(toAddImmutable));
        Assert.assertEquals(this.newMultimapWithKeysValues(1, "One", 2, "2", 2, "Two", 3, "Three").toImmutable().newWith(4, "Four").newWith(5, "Five"), multimap);
    }

    @Test
    public void testPutAllFromCollection()
    {
        MutableMultimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "One", 2, "Two");
        Assert.assertTrue(multimap.putAll(1, Lists.fixedSize.of("Three", "Four")));
        Assert.assertEquals(this.newMultimapWithKeysValues(1, "One", 2, "Two", 1, "Three", 1, "Four"), multimap);
        Assert.assertFalse(multimap.putAll(1, UnifiedSet.<String>newSet()));
        Assert.assertEquals(this.newMultimapWithKeysValues(1, "One", 2, "Two", 1, "Three", 1, "Four"), multimap);
    }

    @Test
    public void testPutAllFromIterable()
    {
        MutableMultimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "One", 2, "Two");
        Assert.assertTrue(multimap.putAll(1, Lists.fixedSize.of("Three", "Four").asLazy()));
        Assert.assertEquals(this.newMultimapWithKeysValues(1, "One", 2, "Two", 1, "Three", 1, "Four"), multimap);
    }

    @Test
    public void testRemoveKey()
    {
        MutableMultimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "1", 2, "Two");

        Verify.assertSetsEqual(UnifiedSet.newSetWith("1"), UnifiedSet.newSet(multimap.removeAll(1)));
        Verify.assertSize(1, multimap);
        Assert.assertFalse(multimap.containsKey(1));

        Verify.assertIterableEmpty(multimap.removeAll(42));
        Verify.assertSize(1, multimap);

        Verify.assertSetsEqual(UnifiedSet.newSetWith("Two"), UnifiedSet.newSet(multimap.removeAll(2)));
        Verify.assertEmpty(multimap);
    }

    @Test
    public void testContainsValue()
    {
        MutableMultimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "One", 2, "Two");
        Assert.assertTrue(multimap.containsValue("Two"));
        Assert.assertFalse(multimap.containsValue("Three"));
    }

    @Test
    public void testGetIfAbsentPut()
    {
        MutableMultimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        Verify.assertIterableEmpty(multimap.get(4));
        Assert.assertTrue(multimap.put(4, "4"));
        Verify.assertContainsEntry(4, "4", multimap);
    }

    @Test
    public void testRemove()
    {
        MutableMultimap<Integer, Integer> map = this.newMultimapWithKeysValues(1, 1, 1, 2, 3, 3, 4, 5);
        Assert.assertFalse(map.remove(4, 4));
        Assert.assertEquals(this.newMultimapWithKeysValues(1, 1, 1, 2, 3, 3, 4, 5), map);
        Assert.assertTrue(map.remove(4, 5));
        Assert.assertEquals(this.newMultimapWithKeysValues(1, 1, 1, 2, 3, 3), map);
        Assert.assertTrue(map.remove(1, 2));
        Assert.assertEquals(this.newMultimapWithKeysValues(1, 1, 3, 3), map);
    }

    @Test
    public void testReplaceValues()
    {
        MutableMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        RichIterable<Integer> oldValues2 = multimap.replaceValues("Two", UnifiedSet.newSetWith(4));
        Assert.assertEquals(Bags.mutable.of(2), oldValues2.toBag());
        Verify.assertEqualsAndHashCode(this.newMultimapWithKeysValues("One", 1, "Two", 4, "Three", 3), multimap);

        RichIterable<Integer> oldValues3 = multimap.replaceValues("Three", UnifiedSet.<Integer>newSet());
        Assert.assertEquals(Bags.mutable.of(3), oldValues3.toBag());
        Verify.assertEqualsAndHashCode(this.newMultimapWithKeysValues("One", 1, "Two", 4), multimap);
    }

    @Test
    public void testReplaceValues_absent_key()
    {
        MutableMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        RichIterable<Integer> oldValues = multimap.replaceValues("Four", UnifiedSet.newSetWith(4));
        Assert.assertEquals(HashBag.<Integer>newBag(), oldValues.toBag());
        Verify.assertEqualsAndHashCode(this.newMultimapWithKeysValues("One", 1, "Two", 2, "Three", 3, "Four", 4), multimap);
    }

    @Test
    public void toMap()
    {
        MutableMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Two", 2);
        UnifiedMap<String, RichIterable<Integer>> expected = UnifiedMap.newMap();
        expected.put("One", this.<Integer>createCollection(1));
        expected.put("Two", this.<Integer>createCollection(2, 2));
        Assert.assertEquals(expected, multimap.toMap());
    }

    @Test
    public void toMapWithTarget()
    {
        MutableMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Two", 2);
        MutableMap<String, RichIterable<Integer>> expected = UnifiedMap.newMap();
        expected.put("One", UnifiedSet.newSetWith(1));
        expected.put("Two", UnifiedSet.newSetWith(2, 2));
        MutableMap<String, MutableSet<Integer>> map = multimap.toMap(Functions0.<Integer>newUnifiedSet());
        Assert.assertEquals(expected, map);
    }

    @Test
    public void toMutable()
    {
        MutableMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Two", 2);
        MutableMultimap<String, Integer> mutableCopy = multimap.toMutable();
        Assert.assertNotSame(multimap, mutableCopy);
        Assert.assertEquals(multimap, mutableCopy);
    }

    @Test
    public void testToString()
    {
        MutableMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2);
        Assert.assertEquals("{One=[1], Two=[2]}", multimap.toString());
    }
}
