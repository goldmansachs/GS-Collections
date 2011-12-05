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

package com.gs.collections.api.multimap.bag;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.multimap.ImmutableMultimap;

/**
 * @since 1.0
 */
public interface ImmutableBagMultimap<K, V>
        extends BagMultimap<K, V>, ImmutableMultimap<K, V>
{
    ImmutableBagMultimap<K, V> newEmpty();

    ImmutableBag<V> get(K key);

    ImmutableBagMultimap<K, V> newWith(K key, V value);

    ImmutableBagMultimap<K, V> newWithout(Object key, Object value);

    ImmutableBagMultimap<K, V> newWithAll(K key, Iterable<? extends V> values);

    ImmutableBagMultimap<K, V> newWithoutAll(Object key);
}
