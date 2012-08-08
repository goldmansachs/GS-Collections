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

import java.util.Set;

import com.webguys.ponzu.api.LazyIterable;
import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.partition.set.PartitionSet;
import com.webguys.ponzu.api.tuple.Pair;

/**
 * A Read-only Set api, with the minor exception inherited from java.lang.Iterable (iterable.iterator().remove()).
 */
public interface SetIterable<T> extends RichIterable<T>
{
    /**
     * Returns the set of all objects that are a member of {@code this} or {@code set} or both. The union of [1, 2, 3]
     * and [2, 3, 4] is the set [1, 2, 3, 4]. If equal elements appear in both sets, then the output will contain the
     * copy from {@code this}.
     */
    SetIterable<T> union(SetIterable<? extends T> set);

    /**
     * Same as {@link #union(SetIterable)} but adds all the objects to {@code targetSet} and returns it.
     */
    <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet);

    /**
     * Returns the set of all objects that are members of both {@code this} and {@code set}. The intersection of
     * [1, 2, 3] and [2, 3, 4] is the set [2, 3]. The output will contain instances from {@code this}, not {@code set}.
     */
    SetIterable<T> intersect(SetIterable<? extends T> set);

    /**
     * Same as {@link #intersect(SetIterable)} but adds all the objects to {@code targetSet} and returns it.
     */
    <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet);

    /**
     * Returns the set of all members of {@code this} that are not members of {@code subtrahendSet}. The difference of
     * [1,2,3] and [2,3,4] is [1].
     */
    SetIterable<T> difference(SetIterable<? extends T> subtrahendSet);

    /**
     * Same as {@link #difference(SetIterable)} but adds all the objects to {@code targetSet} and returns it.
     */
    <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet);

    /**
     * Returns the set of all objects that are a member of exactly one of {@code this} and {@code setB} (elements which
     * are in one of the sets, but not in both). For instance, for the sets [1,2,3] and [2,3,4] , the symmetric
     * difference set is [1,4] . It is the set difference of the union and the intersection.
     */
    SetIterable<T> symmetricDifference(SetIterable<? extends T> setB);

    /**
     * Same as {@link #symmetricDifference(SetIterable)} but adds all the objects to {@code targetSet} and returns it.
     */
    <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet);

    /**
     * Returns {@literal true} if all the members of {@code this} are also members of {@code candidateSuperset}.
     * For example, [1,2] is a subset of [1,2,3], but [1,4] is not.
     */
    boolean isSubsetOf(SetIterable<? extends T> candidateSuperset);

    /**
     * Returns {@literal true} if all the members of {@code this} are also members of {@code candidateSuperset} and the
     * two sets are not equal. For example, [1,2] is a proper subset of [1,2,3], but [1,2,3] is not.
     */
    boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset);

    /**
     * Returns the set whose members are all possible ordered pairs (a,b) where a is a member of {@code this} and b is a
     * member of {@code set}.
     */
    <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set);

    SetIterable<T> filter(Predicate<? super T> predicate);

    SetIterable<T> filterNot(Predicate<? super T> predicate);

    PartitionSet<T> partition(Predicate<? super T> predicate);

    <S> SetIterable<Pair<T, S>> zip(Iterable<S> that);

    SetIterable<Pair<T, Integer>> zipWithIndex();
}
