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
import java.util.Collection;

import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.list.ImmutableList;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.multimap.Multimap;
import com.webguys.ponzu.api.multimap.list.ImmutableListMultimap;
import com.webguys.ponzu.api.multimap.list.MutableListMultimap;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.multimap.AbstractMutableMultimap;

public final class FastListMultimap<K, V>
        extends AbstractMutableMultimap<K, V, MutableList<V>>
        implements MutableListMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 1L;

    // Default from FastList
    private static final int DEFAULT_CAPACITY = 1;

    private int initialListCapacity;

    public FastListMultimap()
    {
        this.initialListCapacity = DEFAULT_CAPACITY;
    }

    public FastListMultimap(int distinctKeys, int valuesPerKey)
    {
        super(Math.max(distinctKeys * 2, 16));
        if (distinctKeys < 0 || valuesPerKey < 0)
        {
            throw new IllegalArgumentException("Both arguments must be positive.");
        }
        this.initialListCapacity = valuesPerKey;
    }

    public FastListMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        this(
                multimap.keysView().size(),
                multimap instanceof FastListMultimap
                        ? ((FastListMultimap<?, ?>) multimap).initialListCapacity
                        : DEFAULT_CAPACITY);
        this.putAll(multimap);
    }

    public FastListMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
    }

    public static <K, V> FastListMultimap<K, V> newMultimap()
    {
        return new FastListMultimap<K, V>();
    }

    public static <K, V> FastListMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new FastListMultimap<K, V>(multimap);
    }

    public static <K, V> FastListMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new FastListMultimap<K, V>(pairs);
    }

    @Override
    protected MutableMap<K, MutableList<V>> createMap()
    {
        return UnifiedMap.newMap();
    }

    @Override
    protected MutableMap<K, MutableList<V>> createMapWithKeyCount(int keyCount)
    {
        return UnifiedMap.newMap(keyCount);
    }

    @Override
    protected MutableList<V> createCollection()
    {
        return FastList.newList(this.initialListCapacity);
    }

    public void trimToSize()
    {
        for (Collection<V> collection : this.map.values())
        {
            FastList<V> fastList = (FastList<V>) collection;
            fastList.trimToSize();
        }
    }

    public FastListMultimap<K, V> newEmpty()
    {
        return new FastListMultimap<K, V>();
    }

    public MutableListMultimap<K, V> toMutable()
    {
        return new FastListMultimap<K, V>(this);
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
