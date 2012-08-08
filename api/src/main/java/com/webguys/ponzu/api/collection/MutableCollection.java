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

package com.webguys.ponzu.api.collection;

import java.util.Collection;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.function.Function3;
import com.webguys.ponzu.api.block.function.Generator;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.multimap.MutableMultimap;
import com.webguys.ponzu.api.partition.PartitionMutableCollection;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.api.tuple.Twin;

/**
 * MutableCollection is an interface which extends the base java.util.Collection interface and adds several internal
 * iterator methods, from the Smalltalk Collection protocol.  These include variations of forEach, filter, filterNot,
 * find, transform, foldLeft, anySatisfy, allSatisfy. These include count, remove, partition, transformIf.  The API also
 * includes converter methods to convert a MutableCollection to a List (toList), to a sorted List (toSortedList), to a
 * Set (toSet), and to a Map (toMap).
 * <p/>
 * There are several extensions to MutableCollection, including MutableList, MutableSet, and MutableBag.
 */
public interface MutableCollection<T>
        extends Collection<T>, RichIterable<T>
{
    /**
     * This method allows mutable and fixed size collections the ability to add elements to their existing elements.
     * In order to support fixed size a new instance of a collection would have to be returned taking the elements of
     * the original collection and appending the new element to form the new collection.  In the case of mutable
     * collections, the original collection is modified, and is returned.  In order to use this method properly with
     * mutable and fixed size collections the following approach must be taken:
     * <p/>
     * <pre>
     * MutableCollection<String> list;
     * list = list.with("1");
     * list = list.with("2");
     * return list;
     * </pre>
     * In the case of {@link FixedSizeCollection} a new instance of MutableCollection will be returned by with, and any
     * variables that previously referenced the original collection will need to be redirected to reference the
     * new instance.  For other MutableCollection types you will replace the reference to collection with the same
     * collection, since the instance will return "this" after calling add on itself.
     *
     * @see #add(Object)
     */
    MutableCollection<T> with(T element);

    /**
     * This method allows mutable and fixed size collections the ability to remove elements from their existing elements.
     * In order to support fixed size a new instance of a collection would have to be returned contaning the elements
     * that would be left from the original collection after calling remove.  In the case of mutable collections, the
     * original collection is modified, and is returned.  In order to use this method properly with mutable and fixed
     * size collections the following approach must be taken:
     * <p/>
     * <pre>
     * MutableCollection<String> list;
     * list = list.without("1");
     * list = list.without("2");
     * return list;
     * </pre>
     * In the case of {@link FixedSizeCollection} a new instance of MutableCollection will be returned by without, and
     * any variables that previously referenced the original collection will need to be redirected to reference the
     * new instance.  For other MutableCollection types you will replace the reference to collection with the same
     * collection, since the instance will return "this" after calling remove on itself.
     *
     * @see #remove(Object)
     */
    MutableCollection<T> without(T element);

    /**
     * This method allows mutable and fixed size collections the ability to add multiple elements to their existing
     * elements. In order to support fixed size a new instance of a collection would have to be returned taking the
     * elements of  the original collection and appending the new elements to form the new collection.  In the case of
     * mutable collections, the original collection is modified, and is returned.  In order to use this method properly
     * with mutable and fixed size collections the following approach must be taken:
     * <p/>
     * <pre>
     * MutableCollection<String> list;
     * list = list.withAll(FastList.newListWith("1", "2"));
     * return list;
     * </pre>
     * In the case of {@link FixedSizeCollection} a new instance of MutableCollection will be returned by withAll, and
     * any variables that previously referenced the original collection will need to be redirected to reference the
     * new instance.  For other MutableCollection types you will replace the reference to collection with the same
     * collection, since the instance will return "this" after calling addAll on itself.
     *
     * @see #addAll(Collection)
     */
    MutableCollection<T> withAll(Iterable<? extends T> elements);

    /**
     * This method allows mutable and fixed size collections the ability to remove multiple elements from their existing
     * elements.  In order to support fixed size a new instance of a collection would have to be returned contaning the
     * elements that would be left from the original collection after calling removeAll.  In the case of mutable
     * collections, the original collection is modified, and is returned.  In order to use this method properly with
     * mutable and fixed size collections the following approach must be taken:
     * <p/>
     * <pre>
     * MutableCollection<String> list;
     * list = list.withoutAll(FastList.newListWith("1", "2"));
     * return list;
     * </pre>
     * In the case of {@link FixedSizeCollection} a new instance of MutableCollection will be returned by withoutAll,
     * and any variables that previously referenced the original collection will need to be redirected to reference the
     * new instance.  For other MutableCollection types you will replace the reference to collection with the same
     * collection, since the instance will return "this" after calling removeAll on itself.
     *
     * @see #removeAll(Collection)
     */
    MutableCollection<T> withoutAll(Iterable<? extends T> elements);

    /**
     * Creates a new empty mutable version of the same collection type.  For example, if this instance is a FastList,
     * this method will return a new empty FastList.  If the class of this instance is immutable or fixed size (i.e.
     * SingletonList) then a mutable alternative to the class will be provided.
     */
    MutableCollection<T> newEmpty();

    /**
     * Returns a MutableCollection with all elements that evaluate to true for the specified predicate.
     * <p/>
     * <pre>e.g.
     * return people.<b>filter</b>(new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.getAddress().getCity().equals("Metuchen");
     *     }
     * });
     * </pre>
     */
    @Override
    MutableCollection<T> filter(Predicate<? super T> predicate);

    /**
     * Returns a MutableCollection with all elements that evaluate to true for the specified predicate2 and parameter.
     * <p/>
     * <pre>e.g.
     * return integers.<b>filterWith</b>(PredicatesLite.equal(), Integer.valueOf(5));
     * </pre>
     */
    <P> MutableCollection<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter);

    /**
     * Returns a MutableCollection with all elements that evaluate to false for the specified predicate.
     * <p/>
     * <pre>e.g.
     * return people.filterNot(new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.person.getLastName().equals("Smith");
     *     }
     * });
     * </pre>
     * <p/>
     * <pre>e.g.
     * return people.filterNot(Predicates.attributeEqual("lastName", "Smith"));
     * </pre>
     */
    @Override
    MutableCollection<T> filterNot(Predicate<? super T> predicate);

    /**
     * Returns a MutableCollection with all elements that evaluate to false for the specified predicate2 and parameter.
     * <p/>
     * <pre>e.g.
     * return integers.<b>filterNotWith</b>(PredicatesLite.equal(), Integer.valueOf(5));
     * </pre>
     */
    <P> MutableCollection<T> filterNotWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter);

    /**
     * Filters a collection into two separate collections based on a predicate returned via a Pair.
     * <p/>
     * <pre>e.g.
     * return lastNames.<b>partitionWith</b>(PredicatesLite.lessThan(), "Mason");
     * </pre>
     */
    <P> Twin<MutableList<T>> partitionWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter);

    @Override
    PartitionMutableCollection<T> partition(Predicate<? super T> predicate);

    /**
     * Removes all elements in the collection that evaluate to true for the specified predicate.
     * <p/>
     * <pre>e.g.
     * return lastNames.<b>removeIf</b>(Predicates.isNull());
     * </pre>
     */
    void removeIf(Predicate<? super T> predicate);

    /**
     * Removes all elements in the collection that evaluate to true for the specified predicate2 and parameter.
     * <p/>
     * <pre>e.g.
     * return lastNames.<b>removeIfWith</b>(PredicatesLite.isNull(), null);
     * </pre>
     */
    <P> void removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter);

    /**
     * Returns a new MutableCollection with the results of applying the specified function to each element of the source
     * collection.
     * <p/>
     * <pre>e.g.
     * return people.transform(new Function&lt;Person, String&gt;()
     * {
     *     public String value(Person person)
     *     {
     *         return person.getFirstName() + " " + person.getLastName();
     *     }
     * });
     * </pre>
     */
    @Override
    <V> MutableCollection<V> transform(Function<? super T, ? extends V> function);

    /**
     * Same as {@link #transform} with a Function2 and specified parameter which is passed to the block
     * <p/>
     * <pre>e.g.
     * Function2<Integer, Integer, Integer> addParameterFunction =
     * new Function2<Integer, Integer, Integer>()
     * {
     *      public Integer value(final Integer each, final Integer parameter)
     *      {
     *          return each + parameter;
     *      }
     * };
     * FastList.newListWith(1, 2, 3).transformWith(addParameterFunction, Integer.valueOf(1));
     * </pre>
     */
    <P, V> MutableCollection<V> transformWith(
            Function2<? super T, ? super P, ? extends V> function,
            P parameter);

    /**
     * Returns a new MutableCollection with the results of applying the specified function to each element of the source
     * collection, but only for elements that evaluate to true for the specified predicate.
     * <p/>
     * <pre>e.g.
     * Lists.mutable.of().with(1, 2, 3).transformIf(Predicates.notNull(), Functions.getToString())
     * </pre>
     */
    @Override
    <V> MutableCollection<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    @Override
    <V> MutableCollection<V> flatTransform(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Returns the first element that evaluates to true for the specified predicate2 and parameter, or null if none
     * evaluate to true.
     * <p/>
     * <pre>e.g.
     * people.findWith(new Predicate2&lt;Person, String&gt;()
     * {
     *     public boolean value(Person person, String fullName)
     *     {
     *         return person.getFullName().equals(fullName);
     *     }
     * }, "John Smith");
     * </pre>
     */
    <P> T findWith(Predicate2<? super T, ? super P> predicate, P parameter);

    /**
     * Returns the first element of the collection that evaluates to true for the specified predicate2 and parameter, or
     * returns the value of evaluating the specified function.
     */
    <P> T findWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Generator<? extends T> function);

    /**
     * Returns the total number of elements that evaluate to true for the specified predicate.
     * <p/>
     * <pre>e.g.
     * return lastNames.<b>countWith</b>(PredicatesLite.equal(), "Smith");
     * </pre>
     */
    <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter);

    /**
     * Returns true if the predicate evaluates to true for any element of the collection, or return false. Returns
     * false if the collection is empty.
     */
    <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter);

    /**
     * Returns true if the predicate evaluates to true for every element of the collection, or returns false.
     */
    <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter);

    <IV, P> IV foldLeftWith(
            IV initialValue,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter);

    /**
     * Returns an unmodifiable view of this collection.  This method allows modules to provide users with "read-only"
     * access to internal collections.  Query operations on the returned collection "read through" to this collection,
     * and attempts to modify the returned collection, whether direct or via its iterator, result in an
     * <tt>UnsupportedOperationException</tt>.
     * <p/>
     * The returned collection does <i>not</i> pass the hashCode and equals operations through to the backing
     * collection, but relies on <tt>Object</tt>'s <tt>equals</tt> and <tt>hashCode</tt> methods.  This is necessary to
     * preserve the contracts of these operations in the case that the backing collection is a set or a list.<p>
     * <p/>
     * The returned collection will be serializable if this collection is serializable.
     *
     * @return an unmodifiable view of this collection.
     * @since 1.0
     */
    MutableCollection<T> asUnmodifiable();

    /**
     * Returns a synchronized (thread-safe) collection backed by this collection.  In order to guarantee serial access,
     * it is critical that <strong>all</strong> access to the backing collection is accomplished through the returned
     * collection.
     * <p/>
     * It is imperative that the user manually synchronize on the returned collection when iterating over it using the
     * standard JDK iterator or JDK 5 for loop.
     * <pre>
     *  MutableCollection collection = myCollection.asSynchronized();
     *     ...
     *  synchronized(collection)
     *  {
     *      Iterator i = c.iterator(); // Must be in the synchronized block
     *      while (i.hasNext())
     *         foo(i.next());
     *  }
     * </pre>
     * Failure to follow this advice may result in non-deterministic behavior.
     * <p/>
     * The preferred way of iterating over a synchronized collection is to use the collection.forEach() method which is
     * properly synchronized internally.
     * <pre>
     *  MutableCollection collection = myCollection.asSynchronized();
     *     ...
     *  collection.forEach(new Procedure()
     *  {
     *      public void value(Object each)
     *      {
     *          ...
     *      }
     *  });
     * </pre>
     * <p/>
     * The returned collection does <i>not</i> pass the <tt>hashCode</tt> and <tt>equals</tt> operations through to the
     * backing collection, but relies on <tt>Object</tt>'s equals and hashCode methods.  This is necessary to preserve
     * the contracts of these operations in the case that the backing collection is a set or a list.
     * <p/>
     * The returned collection will be serializable if this collection is serializable.
     *
     * @return a synchronized view of this collection.
     * @since 1.0
     */
    MutableCollection<T> asSynchronized();

    /**
     * Converts this MutableCollection to an ImmutableCollection.
     *
     * @since 1.0
     */
    ImmutableCollection<T> toImmutable();

    @Override
    <V> MutableMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    @Override
    <V> MutableMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    @Override
    <S> MutableCollection<Pair<T, S>>
    zip(Iterable<S> that);

    @Override
    MutableCollection<Pair<T, Integer>> zipWithIndex();

    /**
     * @see #addAll(Collection)
     * @since 1.0
     */
    boolean addAllIterable(Iterable<? extends T> iterable);

    /**
     * @see #removeAll(Collection)
     * @since 1.0
     */
    boolean removeAllIterable(Iterable<?> iterable);

    /**
     * @see #retainAll(Collection)
     * @since 1.0
     */
    boolean retainAllIterable(Iterable<?> iterable);
}
