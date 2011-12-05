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

package com.gs.collections.api.list;

import java.util.List;
import java.util.ListIterator;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.partition.list.PartitionList;
import com.gs.collections.api.tuple.Pair;

/**
 * An iterable whose items are ordered and may be accessed directly by index.  A reverseForEach
 * internal iterator is available iterating over the indexed iterable in reverse, starting from
 * the end and going to the beginning.  Additionally, internal iterators are available for batching
 * style iteration which is useful for parallel processing.
 */
public interface ListIterable<T>
        extends RichIterable<T>
{
    /**
     * Iterates over the section of the list covered by the specified inclusive indexes.  The indexes are
     * both inclusive.
     * <p/>
     * <p/>
     * <pre>e.g.
     * ListIterable<People> people = FastList.newListWith(ted, mary, bob, sally)
     * people.forEach(0, 1, new Procedure<Person>()
     * {
     *     public void value(Person person)
     *     {
     *          LOGGER.info(person.getName());
     *     }
     * });
     * </pre>
     * <p/>
     * This code would output ted and mary's names.
     */
    void forEach(int startIndex, int endIndex, Procedure<? super T> procedure);

    /**
     * Iterates over the section of the list covered by the specified inclusive indexes.  The indexes are
     * both inclusive.
     * <p/>
     * <p/>
     * <pre>e.g.
     * ListIterable<People> people = FastList.newListWith(ted, mary, bob, sally)
     * people.forEachWithIndex(0, 1, new ProcedureWithInt<Person>()
     * {
     *     public void value(Person person, int index)
     *     {
     *          LOGGER.info(person.getName());
     *     }
     * });
     * </pre>
     * <p/>
     * This code would output ted and mary's names.
     */
    void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure);

    /**
     * For each item of the receiver iterated in reverse order, the operation is evaluated with
     * the item as the parameter.
     * <p/>
     * <pre>e.g.
     * people.reverseForEach(new Procedure<Person>()
     * {
     *     public void value(Person person)
     *     {
     *         LOGGER.info(person.getName());
     *     }
     * });
     * </pre>
     */
    void reverseForEach(Procedure<? super T> procedure);

    /**
     * Returns the item at the specified position in this list iterable.
     */
    T get(int index);

    /**
     * Returns the index of the first occurrence of the specified item
     * in this list, or -1 if this list does not contain the item.
     */
    int indexOf(Object o);

    /**
     * Returns the index of the last occurrence of the specified item
     * in this list, or -1 if this list does not contain the item.
     */
    int lastIndexOf(Object o);

    /**
     * Returns the value of the size of this iterable
     */
    int size();

    /**
     * Returns the item at index 0 of the container.  If the container is empty, null is returned.  If null
     * is a valid item of the container, then a developer will need to check to see if the container is
     * empty first.
     */
    T getFirst();

    /**
     * Returns the item at index (size() - 1) of the container.  If the container is empty, null is returned.  If null
     * is a valid item of the container, then a developer will need to check to see if the container is
     * empty first.
     */
    T getLast();

    /**
     * @see List#listIterator()
     * @since 1.0.
     */
    ListIterator<T> listIterator();

    /**
     * @see List#listIterator(int)
     * @since 1.0.
     */
    ListIterator<T> listIterator(int index);

    ListIterable<T> select(Predicate<? super T> predicate);

    ListIterable<T> reject(Predicate<? super T> predicate);

    PartitionList<T> partition(Predicate<? super T> predicate);

    <V> ListIterable<V> collect(Function<? super T, ? extends V> function);

    <V> ListIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    <V> ListIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    <V> ListMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    <V> ListMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    <S> ListIterable<Pair<T, S>> zip(Iterable<S> that);

    ListIterable<Pair<T, Integer>> zipWithIndex();
}
