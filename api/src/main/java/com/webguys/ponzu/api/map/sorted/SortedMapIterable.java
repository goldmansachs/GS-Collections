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

package com.webguys.ponzu.api.map.sorted;

import java.util.Comparator;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.list.ListIterable;
import com.webguys.ponzu.api.map.MapIterable;
import com.webguys.ponzu.api.multimap.list.ListMultimap;
import com.webguys.ponzu.api.partition.list.PartitionList;
import com.webguys.ponzu.api.tuple.Pair;

/**
 * An iterable whose elements are unique.
 */
public interface SortedMapIterable<K, V>
        extends MapIterable<K, V>
{
    Comparator<? super K> comparator();

    SortedMapIterable<K, V> filter(Predicate2<? super K, ? super V> predicate);

    SortedMapIterable<K, V> filterNot(Predicate2<? super K, ? super V> predicate);

    <R> SortedMapIterable<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function);

    ListIterable<V> filter(Predicate<? super V> predicate);

    ListIterable<V> filterNot(Predicate<? super V> predicate);

    PartitionList<V> partition(Predicate<? super V> predicate);

    <S> ListIterable<Pair<V, S>> zip(Iterable<S> that);

    ListIterable<Pair<V, Integer>> zipWithIndex();

    <VV> ListMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function);

    <VV> ListMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function);
}
