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

package com.gs.collections.api.multimap.set;

import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.set.ImmutableSet;

/**
 * @since 1.0
 */
public interface ImmutableSetMultimap<K, V>
        extends SetMultimap<K, V>, ImmutableMultimap<K, V>
{
    ImmutableSet<V> get(K key);

    ImmutableSetMultimap<K, V> newEmpty();

    ImmutableSetMultimap<K, V> newWith(K key, V value);

    ImmutableSetMultimap<K, V> newWithout(Object key, Object value);

    ImmutableSetMultimap<K, V> newWithAll(K key, Iterable<? extends V> values);

    ImmutableSetMultimap<K, V> newWithoutAll(Object key);
}
