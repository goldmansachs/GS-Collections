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

import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.set.MutableSet;

/**
 * @since 1.0
 */
public interface MutableSetMultimap<K, V>
        extends SetMultimap<K, V>, MutableMultimap<K, V>
{
    MutableSet<V> replaceValues(K key, Iterable<? extends V> values);

    MutableSet<V> removeAll(Object key);

    MutableSetMultimap<K, V> newEmpty();

    MutableSet<V> get(K key);

    MutableSetMultimap<K, V> toMutable();

    ImmutableSetMultimap<K, V> toImmutable();
}
