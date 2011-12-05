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

import java.util.Map;

import com.gs.collections.api.factory.map.ImmutableMapFactory;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public final class ImmutableMapFactoryImpl implements ImmutableMapFactory
{
    public <K, V> ImmutableMap<K, V> of()
    {
        return (ImmutableMap<K, V>) ImmutableEmptyMap.INSTANCE;
    }

    public <K, V> ImmutableMap<K, V> of(K key, V value)
    {
        return new ImmutableSingletonMap<K, V>(key, value);
    }

    public <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2)
    {
        if (Comparators.nullSafeEquals(key1, key2))
        {
            return this.of(key1, value2);
        }
        return new ImmutableDoubletonMap<K, V>(key1, value1, key2, value2);
    }

    public <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3)
    {
        if (Comparators.nullSafeEquals(key1, key2) && Comparators.nullSafeEquals(key2, key3))
        {
            return this.of(key1, value3);
        }
        if (Comparators.nullSafeEquals(key1, key2))
        {
            return this.of(key1, value2, key3, value3);
        }
        if (Comparators.nullSafeEquals(key1, key3))
        {
            return this.of(key2, value2, key1, value3);
        }
        if (Comparators.nullSafeEquals(key2, key3))
        {
            return this.of(key1, value1, key2, value3);
        }

        return new ImmutableTripletonMap<K, V>(key1, value1, key2, value2, key3, value3);
    }

    public <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4)
    {
        return UnifiedMap.newWithKeysValues(key1, value1, key2, value2, key3, value3, key4, value4).toImmutable();
    }

    public <K, V> ImmutableMap<K, V> ofMap(MutableMap<K, V> map)
    {
        if (map.isEmpty())
        {
            return this.of();
        }

        if (map.size() > 3)
        {
            return new ImmutableUnifiedMap<K, V>(map);
        }

        Map.Entry<K, V>[] entries = map.entrySet().toArray(new Map.Entry[map.entrySet().size()]);
        switch (entries.length)
        {
            case 1:
                return this.of(entries[0].getKey(), entries[0].getValue());
            case 2:

                return this.of(
                        entries[0].getKey(), entries[0].getValue(),
                        entries[1].getKey(), entries[1].getValue());
            case 3:
                return this.of(
                        entries[0].getKey(), entries[0].getValue(),
                        entries[1].getKey(), entries[1].getValue(),
                        entries[2].getKey(), entries[2].getValue());
            default:
                throw new AssertionError();
        }
    }
}
