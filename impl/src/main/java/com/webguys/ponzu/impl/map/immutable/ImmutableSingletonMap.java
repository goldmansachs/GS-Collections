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

package com.webguys.ponzu.impl.map.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.map.ImmutableMap;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.block.factory.Comparators;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.factory.Maps;
import com.webguys.ponzu.impl.factory.Sets;
import com.webguys.ponzu.impl.tuple.Tuples;

final class ImmutableSingletonMap<K, V>
        extends AbstractImmutableMap<K, V>
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final K key1;
    private final V value1;

    ImmutableSingletonMap(K key1, V value1)
    {
        this.key1 = key1;
        this.value1 = value1;
    }

    public int size()
    {
        return 1;
    }

    public RichIterable<K> keysView()
    {
        return Lists.immutable.of(this.key1).asLazy();
    }

    public RichIterable<V> valuesView()
    {
        return Lists.immutable.of(this.value1).asLazy();
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return Lists.immutable.<Pair<K, V>>of(Tuples.pair(this.key1, this.value1)).asLazy();
    }

    public boolean containsKey(Object key)
    {
        return Comparators.nullSafeEquals(this.key1, key);
    }

    public boolean containsValue(Object value)
    {
        return Comparators.nullSafeEquals(this.value1, value);
    }

    public V get(Object key)
    {
        if (Comparators.nullSafeEquals(this.key1, key))
        {
            return this.value1;
        }

        return null;
    }

    public Set<K> keySet()
    {
        return Sets.immutable.of(this.key1).castToSet();
    }

    public Collection<V> values()
    {
        return Lists.immutable.of(this.value1).castToList();
    }

    @Override
    public String toString()
    {
        return "{" + this.key1 + '=' + this.value1 + '}';
    }

    @Override
    public int hashCode()
    {
        return this.keyAndValueHashCode(this.key1, this.value1);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Map))
        {
            return false;
        }
        Map<K, V> that = (Map<K, V>) other;
        return that.size() == this.size() && this.keyAndValueEquals(this.key1, this.value1, that);
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        procedure.value(this.key1, this.value1);
    }

    @Override
    public void forEachKey(Procedure<? super K> procedure)
    {
        procedure.value(this.key1);
    }

    @Override
    public void forEachValue(Procedure<? super V> procedure)
    {
        procedure.value(this.value1);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        objectIntProcedure.value(this.value1, 0);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        procedure.value(this.value1, parameter);
    }

    @Override
    public ImmutableMap<K, V> filter(Predicate2<? super K, ? super V> predicate)
    {
        if (predicate.accept(this.key1, this.value1))
        {
            return this;
        }
        return Maps.immutable.of();
    }

    @Override
    public ImmutableMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate)
    {
        if (predicate.accept(this.key1, this.value1))
        {
            return Maps.immutable.of();
        }
        return this;
    }

    @Override
    public <K2, V2> ImmutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        Pair<K2, V2> pair = function.value(this.key1, this.value1);
        return Maps.immutable.of(pair.getOne(), pair.getTwo());
    }

    @Override
    public <R> ImmutableMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return Maps.immutable.of(this.key1, function.value(this.key1, this.value1));
    }

    @Override
    public Pair<K, V> find(Predicate2<? super K, ? super V> predicate)
    {
        if (predicate.accept(this.key1, this.value1))
        {
            return Tuples.pair(this.key1, this.value1);
        }
        return null;
    }
}
