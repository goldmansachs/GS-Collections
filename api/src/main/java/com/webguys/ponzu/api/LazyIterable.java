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

package com.webguys.ponzu.api;

import java.util.Collection;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.tuple.Pair;

/**
 * Any methods that do not return a LazyIterable when called will cause evaluation to be forced.
 * A LazyIterable is RichIterable which will defer evaluation for certain methods like filte, filterNot, transform, etc.
 *
 * @since 1.0
 */
public interface LazyIterable<T>
        extends RichIterable<T>
{
    /**
     * Creates a deferred iterable for filtering elements from the current iterable.
     */
    LazyIterable<T> filter(Predicate<? super T> predicate);

    /**
     * Creates a deferred iterable for filtering out elements from the current iterable.
     */
    LazyIterable<T> filterNot(Predicate<? super T> predicate);

    /**
     * Creates a deferred iterable for collecting elements from the current iterable.
     */
    <V> LazyIterable<V> transform(Function<? super T, ? extends V> function);

    /**
     * Creates a deferred iterable for filtering and transforming elements from the current iterable.
     */
    <V> LazyIterable<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    /**
     * Creates a deferred take iterable for the current iterable using the specified count as the limit.
     */
    LazyIterable<T> take(int count);

    /**
     * Creates a deferred drop iterable for the current iterable using the specified count as the limit.
     */
    LazyIterable<T> drop(int count);

    /**
     * Creates a deferred flattening iterable for the current iterable.
     */
    <V> LazyIterable<V> flatTransform(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Creates a deferred iterable that will join this iterable with the specified iterable.
     */
    LazyIterable<T> concatenate(Iterable<T> iterable);

    /**
     * Creates a deferred zip iterable.
     */
    <S> LazyIterable<Pair<T, S>> zip(Iterable<S> that);

    /**
     * Creates a deferred zipWithIndex iterable.
     */
    LazyIterable<Pair<T, Integer>> zipWithIndex();

    /**
     * Creates a deferred chunking iterable.
     */
    LazyIterable<RichIterable<T>> chunk(int size);

    /**
     * Iterates over this iterable adding all elements into the target collection.
     */
    <R extends Collection<T>> R into(R target);
}
