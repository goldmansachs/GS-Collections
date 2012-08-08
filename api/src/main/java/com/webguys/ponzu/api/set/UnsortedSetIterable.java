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

package com.webguys.ponzu.api.set;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.multimap.set.SetMultimap;
import com.webguys.ponzu.api.tuple.Pair;

/**
 * An iterable whose items are unique.
 */
public interface UnsortedSetIterable<T>
        extends SetIterable<T>
{
    /**
     * Returns the set whose members are all possible subsets of {@code this}. For example, the powerset of [1, 2] is
     * [[], [1], [2], [1,2]].
     */
    UnsortedSetIterable<UnsortedSetIterable<T>> powerSet();

    <V> UnsortedSetIterable<V> transform(Function<? super T, ? extends V> function);

    <V> UnsortedSetIterable<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> UnsortedSetIterable<V> flatTransform(Function<? super T, ? extends Iterable<V>> function);

    <V> SetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> SetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    UnsortedSetIterable<T> union(SetIterable<? extends T> set);

    UnsortedSetIterable<T> intersect(SetIterable<? extends T> set);

    UnsortedSetIterable<T> difference(SetIterable<? extends T> subtrahendSet);

    UnsortedSetIterable<T> symmetricDifference(SetIterable<? extends T> setB);

    UnsortedSetIterable<T> filter(Predicate<? super T> predicate);

    UnsortedSetIterable<T> filterNot(Predicate<? super T> predicate);

    <S> UnsortedSetIterable<Pair<T, S>> zip(Iterable<S> that);

    UnsortedSetIterable<Pair<T, Integer>> zipWithIndex();
}
