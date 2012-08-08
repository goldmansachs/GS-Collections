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

package com.webguys.ponzu.impl.multimap.list;

import java.io.Externalizable;

import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.list.ImmutableList;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.multimap.Multimap;
import com.webguys.ponzu.api.multimap.list.ImmutableListMultimap;
import com.webguys.ponzu.api.multimap.list.MutableListMultimap;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.map.mutable.ConcurrentMutableHashMap;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.multimap.AbstractSynchronizedPutMultimap;
import com.webguys.ponzu.impl.utility.ArrayIterate;

/**
 * A Multimap that is optimized for parallel writes, but is not protected for concurrent reads.
 */
public final class SynchronizedPutFastListMultimap<K, V>
        extends AbstractSynchronizedPutMultimap<K, V, MutableList<V>>
        implements MutableListMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 42L;

    public SynchronizedPutFastListMultimap()
    {
    }

    public SynchronizedPutFastListMultimap(int initialCapacity, float loadFactor, int concurrencyLevel)
    {
        super(ConcurrentMutableHashMap.<K, MutableList<V>>newMap(initialCapacity, loadFactor, concurrencyLevel));
    }

    public SynchronizedPutFastListMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        this.putAll(multimap);
    }

    public SynchronizedPutFastListMultimap(Pair<K, V>... pairs)
    {
        this();
        ArrayIterate.forEach(pairs, new Procedure<Pair<K, V>>()
        {
            public void value(Pair<K, V> pair)
            {
                put(pair.getOne(), pair.getTwo());
            }
        });
    }

    @Override
    protected MutableList<V> createCollection()
    {
        return FastList.newList(1);
    }

    public static <K, V> SynchronizedPutFastListMultimap<K, V> newMultimap()
    {
        return new SynchronizedPutFastListMultimap<K, V>();
    }

    public static <K, V> SynchronizedPutFastListMultimap<K, V> newMultimap(int initiaCapacity, float loadFactor, int concurrencyLevel)
    {
        return new SynchronizedPutFastListMultimap<K, V>(initiaCapacity, loadFactor, concurrencyLevel);
    }

    public static <K, V> SynchronizedPutFastListMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new SynchronizedPutFastListMultimap<K, V>(multimap);
    }

    public static <K, V> SynchronizedPutFastListMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new SynchronizedPutFastListMultimap<K, V>(pairs);
    }

    public SynchronizedPutFastListMultimap<K, V> newEmpty()
    {
        return new SynchronizedPutFastListMultimap<K, V>();
    }

    public MutableListMultimap<K, V> toMutable()
    {
        return new SynchronizedPutFastListMultimap<K, V>(this);
    }

    public ImmutableListMultimap<K, V> toImmutable()
    {
        final MutableMap<K, ImmutableList<V>> map = UnifiedMap.newMap();

        this.map.forEachKeyValue(new Procedure2<K, MutableList<V>>()
        {
            public void value(K key, MutableList<V> list)
            {
                map.put(key, list.toImmutable());
            }
        });

        return new ImmutableListMultimapImpl<K, V>(map);
    }
}
