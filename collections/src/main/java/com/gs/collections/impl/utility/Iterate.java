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

package com.gs.collections.impl.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.SortedSet;

import com.gs.collections.api.InternalIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.block.procedure.MapCollectProcedure;
import com.gs.collections.impl.block.procedure.MaxComparatorProcedure;
import com.gs.collections.impl.block.procedure.MinComparatorProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.utility.internal.DefaultSpeciesNewStrategy;
import com.gs.collections.impl.utility.internal.IterableIterate;
import com.gs.collections.impl.utility.internal.RandomAccessListIterate;

/**
 * The Iterate class provides a few of the methods from the Smalltalk Collection Protocol.  This includes do:, select:,
 * reject:, collect:, inject:into:, detect:, detect:ifNone:, anySatisfy: and allSatisfy:
 * <p/>
 * The do: method could not be implemented in Java because it is a reserved word.  The forEach() method is the
 * equivalent of the do: method in the Smalltalk Collection protocol.
 */
public final class Iterate
{
    private Iterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * For each element of the receiver, operation is evaluated with the element as the parameter.
     * <p/>
     * <pre>e.g.
     * Iterate.forEach(people, new Procedure<Person>()
     * {
     *     public void value(Person person)
     *     {
     *         LOGGER.info(person.getName());
     *     }
     * });
     * </pre>
     */
    public static <T> void forEach(Iterable<T> iterable, Procedure<? super T> procedure)
    {
        if (iterable instanceof InternalIterable)
        {
            ((InternalIterable<T>) iterable).forEach(procedure);
        }
        else if (iterable instanceof ArrayList)
        {
            ArrayListIterate.forEach((ArrayList<T>) iterable, procedure);
        }
        else if (iterable instanceof RandomAccess)
        {
            RandomAccessListIterate.forEach((List<T>) iterable, procedure);
        }
        else if (iterable != null)
        {
            IterableIterate.forEach(iterable, procedure);
        }
        else
        {
            throw new IllegalArgumentException("Cannot perform a forEach on null");
        }
    }

    /**
     * For each element of the receiver, operation is evaluated with the element as the first argument, and the
     * specified parameter as the second argument.
     * <p/>
     * <pre>e.g.
     * Iterate.forEachWith(people, new Procedure2<Person, Person>()
     * {
     *     public void value(Person person, Person other)
     *     {
     *         if (person.isRelatedTo(other))
     *         {
     *              LOGGER.info(person.getName());
     *         }
     *     }
     * }, fred);
     * </pre>
     */
    public static <T, P> void forEachWith(
            Iterable<T> iterable,
            Procedure2<? super T, ? super P> procedure,
            P parameter)
    {
        if (iterable instanceof InternalIterable)
        {
            ((InternalIterable<T>) iterable).forEachWith(procedure, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            ArrayListIterate.forEachWith((ArrayList<T>) iterable, procedure, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            RandomAccessListIterate.forEachWith((List<T>) iterable, procedure, parameter);
        }
        else if (iterable != null)
        {
            IterableIterate.forEachWith(iterable, procedure, parameter);
        }
        else
        {
            throw new IllegalArgumentException("Cannot perform a forEachWith on null");
        }
    }

    /**
     * Iterates over a collection passing each element and the current relative int index to the specified instance of
     * ProcedureWithInt.
     */
    public static <T> void forEachWithIndex(Iterable<T> iterable, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        if (iterable instanceof InternalIterable)
        {
            ((InternalIterable<T>) iterable).forEachWithIndex(objectIntProcedure);
        }
        else if (iterable instanceof ArrayList)
        {
            ArrayListIterate.forEachWithIndex((ArrayList<T>) iterable, objectIntProcedure);
        }
        else if (iterable instanceof RandomAccess)
        {
            RandomAccessListIterate.forEachWithIndex((List<T>) iterable, objectIntProcedure);
        }
        else if (iterable != null)
        {
            IterableIterate.forEachWithIndex(iterable, objectIntProcedure);
        }
        else
        {
            throw new IllegalArgumentException("Cannot perform a forEachWithIndex on null");
        }
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to true is included in the new collection.
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>select</b>(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.getAddress().getCity().equals("Metuchen");
     *     }
     * });
     * </pre>
     */
    public static <T> Collection<T> select(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).select(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.select((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.select((List<T>) iterable, predicate);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, Collection<T>>select(
                    iterable,
                    predicate,
                    DefaultSpeciesNewStrategy.INSTANCE.<T>speciesNew((Collection<T>) iterable));
        }
        else if (iterable != null)
        {
            return IterableIterate.select(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform a select on null");
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to true is included in the new collection.
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>selectWith</b>(integers, Predicates2.equal(), new Integer(5));
     * </pre>
     */
    public static <T, IV> Collection<T> selectWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).selectWith(predicate, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.selectWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.selectWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, IV, Collection<T>>selectWith(
                    iterable,
                    predicate,
                    parameter,
                    DefaultSpeciesNewStrategy.INSTANCE.<T>speciesNew((Collection<T>) iterable));
        }
        else if (iterable != null)
        {
            return IterableIterate.selectWith(iterable, predicate, parameter, FastList.<T>newList());
        }
        throw new IllegalArgumentException("Cannot perform a selectWith on null");
    }

    /**
     * Filters a collection into two separate collections based on a predicate returned via a Twin.
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>selectAndRejectWith</b>(lastNames, Predicates2.lessThan(), "Mason");
     * </pre>
     */
    public static <T, IV> Twin<MutableList<T>> selectAndRejectWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).selectAndRejectWith(predicate, injectedValue);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.selectAndRejectWith((ArrayList<T>) iterable, predicate, injectedValue);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.selectAndRejectWith((List<T>) iterable, predicate, injectedValue);
        }
        else if (iterable != null)
        {
            return IterableIterate.selectAndRejectWith(iterable, predicate, injectedValue);
        }
        throw new IllegalArgumentException("Cannot perform a selectAndRejectWith on null");
    }

    /**
     * Filters a collection into two separate collections based on a predicate.
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>partition</b>(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.getAddress().getState().getName().equals("New York");
     *     }
     * });
     * </pre>
     */
    public static <T> PartitionIterable<T> partition(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof RichIterable<?>)
        {
            return ((RichIterable<T>) iterable).partition(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.partition((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.partition((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.partition(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform a partition on null");
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. For each element
     * which causes predicate to evaluate to true the count is incremented
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>count</b>(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.getAddress().getState().getName().equals("New York");
     *     }
     * });
     * </pre>
     */
    public static <T> int count(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).count(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.count((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.count((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.count(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot get a count from null");
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. For each element
     * which causes predicate to evaluate to true the count is incremented
     * <p/>
     * <pre>e.g.
     * return Iterate.<b>countWith</b>(lastNames, Predicates2.equal(), "Smith");
     * </pre>
     */
    public static <T, IV> int countWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).countWith(predicate, injectedValue);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.countWith((ArrayList<T>) iterable, predicate, injectedValue);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.countWith((List<T>) iterable, predicate, injectedValue);
        }
        else if (iterable != null)
        {
            return IterableIterate.countWith(iterable, predicate, injectedValue);
        }
        throw new IllegalArgumentException("Cannot get a count from null");
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to true has the specified Function applied it and the result
     * is added to the target collection.
     */
    public static <T, V> Collection<V> collectIf(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).collectIf(predicate, function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.collectIf((ArrayList<T>) iterable, predicate, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.collectIf((List<T>) iterable, predicate, function);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, V, Collection<V>>collectIf(
                    iterable,
                    predicate,
                    function,
                    DefaultSpeciesNewStrategy.INSTANCE.<V>speciesNew((Collection<T>) iterable));
        }
        else if (iterable != null)
        {
            return IterableIterate.collectIf(iterable, predicate, function);
        }
        throw new IllegalArgumentException("Cannot perform a collectIf on null");
    }

    /**
     * Same as the collectIf method with three parameters but uses the specified target collection.
     */
    public static <T, V, R extends Collection<V>> R collectIf(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R target)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).collectIf(predicate, function, target);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.collectIf((ArrayList<T>) iterable, predicate, function, target);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.collectIf((List<T>) iterable, predicate, function, target);
        }
        else if (iterable != null)
        {
            return IterableIterate.collectIf(iterable, predicate, function, target);
        }
        throw new IllegalArgumentException("Cannot perform a collectIf on null");
    }

    /**
     * Same as the select method with two parameters but uses the specified target collection
     * <p/>
     * <pre>e.g.
     * return Iterate.select(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.person.getLastName().equals("Smith");
     *     }
     * }, new ArrayList());
     * </pre>
     * <p/>
     * <pre>e.g.
     * return Iterate.select(collection, Predicates.attributeEqual("lastName", "Smith"), new ArrayList());
     * </pre>
     */
    public static <T, R extends Collection<T>> R select(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).select(predicate, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.select((ArrayList<T>) iterable, predicate, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.select((List<T>) iterable, predicate, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.select(iterable, predicate, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a select on null");
    }

    /**
     * Same as the selectWith method with two parameters but uses the specified target collection.
     */
    public static <T, P, R extends Collection<T>> R selectWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).selectWith(predicate, parameter, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.selectWith((ArrayList<T>) iterable, predicate, parameter, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.selectWith((List<T>) iterable, predicate, parameter, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.selectWith(iterable, predicate, parameter, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a selectWith on null");
    }

    /**
     * Returns the first count elements of the receiver or the receiver itself if count is greater than the length of
     * the receiver.
     *
     * @param iterable the collection to take from.
     * @param count    the number of items to take.
     * @return a new list with the items take from the given collection.
     */
    public static <T> Collection<T> take(Iterable<T> iterable, int count)
    {
        if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.take((ArrayList<T>) iterable, count);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.take((List<T>) iterable, count);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, Collection<T>>take(
                    iterable,
                    count,
                    DefaultSpeciesNewStrategy.INSTANCE.<T>speciesNew((Collection<T>) iterable, count));
        }
        else if (iterable != null)
        {
            return IterableIterate.take(iterable, count);
        }
        throw new IllegalArgumentException("Cannot perform a take on null");
    }

    /**
     * Returns a collection without the first count elements of the receiver or the receiver itself if count is
     * non-positive.
     *
     * @param iterable the collection to drop from.
     * @param count    the number of items to drop.
     * @return a new list with the items dropped from the given collection.
     */
    public static <T> Collection<T> drop(Iterable<T> iterable, int count)
    {
        if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.drop((List<T>) iterable, count);
        }
        else if (iterable != null)
        {
            return IterableIterate.drop(iterable, count);
        }
        throw new IllegalArgumentException("Cannot perform a drop on null");
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to false is included in the new collection. The elements are traversed in
     * the same order as they would be if the message forEach() message had been sent to the receiver.
     * <p/>
     * <pre>e.g.
     * return Iterate.reject(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.person.getLastName().equals("Smith");
     *     }
     * });
     * </pre>
     * <p/>
     * <pre>e.g.
     * return Iterate.reject(collection, Predicates.attributeEqual("lastName", "Smith"));
     * </pre>
     */
    public static <T> Collection<T> reject(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).reject(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.reject((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.reject((List<T>) iterable, predicate);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, Collection<T>>reject(
                    iterable,
                    predicate,
                    DefaultSpeciesNewStrategy.INSTANCE.<T>speciesNew((Collection<T>) iterable));
        }
        else if (iterable != null)
        {
            return IterableIterate.reject(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform a reject on null");
    }

    /**
     * Sort is a mutating method.  The List passed in is also returned.
     */
    public static <T extends Comparable<? super T>, L extends List<T>> L sortThis(L list)
    {
        if (list instanceof MutableList<?>)
        {
            ((MutableList<T>) list).sortThis();
        }
        else if (list instanceof ArrayList)
        {
            ArrayListIterate.sortThis((ArrayList<T>) list);
        }
        else
        {
            if (list.size() > 1)
            {
                Collections.sort(list);
            }
        }
        return list;
    }

    /**
     * Sort is a mutating method.  The List passed in is also returned.
     */
    public static <T, L extends List<T>> L sortThis(L list, Comparator<? super T> comparator)
    {
        if (list instanceof MutableList)
        {
            ((MutableList<T>) list).sortThis(comparator);
        }
        else if (list instanceof ArrayList)
        {
            ArrayListIterate.sortThis((ArrayList<T>) list, comparator);
        }
        else
        {
            if (list.size() > 1)
            {
                Collections.sort(list, comparator);
            }
        }
        return list;
    }

    /**
     * Sort is a mutating method.  The List passed in is also returned.
     */
    public static <T, L extends List<T>> L sortThis(L list, final Predicate2<? super T, ? super T> predicate)
    {
        return Iterate.sortThis(
                list, new Comparator<T>()
        {
            public int compare(T o1, T o2)
            {
                if (predicate.accept(o1, o2))
                {
                    return -1;
                }
                if (predicate.accept(o2, o1))
                {
                    return 1;
                }
                return 0;
            }
        });
    }

    /**
     * Sort the list by comparing an attribute defined by the function.
     * Sort is a mutating method.  The List passed in is also returned.
     */
    public static <T, V extends Comparable<V>, L extends List<T>> L sortThisBy(L list, Function<? super T, ? extends V> function)
    {
        return Iterate.sortThis(list, Comparators.byFunction(function));
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to true is removed from the source collection
     */
    public static <T> Collection<T> removeIf(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof MutableCollection)
        {
            MutableCollection<T> mutableCollection = (MutableCollection<T>) iterable;
            mutableCollection.removeIf(predicate);
            return mutableCollection;
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.removeIf((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.removeIf((List<T>) iterable, predicate);
        }
        else if (iterable instanceof Collection)
        {
            IterableIterate.removeIf(iterable, predicate);
            return (Collection<T>) iterable;
        }
        else if (iterable != null)
        {
            IterableIterate.removeIf(iterable, predicate);
            // TODO: should this method return Iterable instead?  Would seem less useful if it did
            return null;
        }
        throw new IllegalArgumentException("Cannot perform a remove on null");
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element and the specified parameter. Each
     * element and parameter which causes predicate to evaluate to true is removed from the source collection
     */
    public static <T, P> Collection<T> removeIfWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            MutableCollection<T> mutableCollection = (MutableCollection<T>) iterable;
            mutableCollection.removeIfWith(predicate, parameter);
            return mutableCollection;
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.removeIfWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.removeIfWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof Collection)
        {
            IterableIterate.removeIfWith(iterable, predicate, parameter);
            return (Collection<T>) iterable;
        }
        else if (iterable != null)
        {
            IterableIterate.removeIfWith(iterable, predicate, parameter);
            // TODO: should this method return Iterarable instead?  Would seem less useful if it did
            return null;
        }
        throw new IllegalArgumentException("Cannot perform a remove on null");
    }

    /**
     * For each element of the receiver, predicate is evaluated with the element as the parameter. Each element
     * which causes predicate to evaluate to false is included in the new collection.
     */
    public static <T, IV> Collection<T> rejectWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).rejectWith(predicate, injectedValue);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.rejectWith((ArrayList<T>) iterable, predicate, injectedValue);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.rejectWith((List<T>) iterable, predicate, injectedValue);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, IV, Collection<T>>rejectWith(
                    iterable,
                    predicate,
                    injectedValue,
                    DefaultSpeciesNewStrategy.INSTANCE.<T>speciesNew((Collection<T>) iterable));
        }
        else if (iterable != null)
        {
            return IterableIterate.rejectWith(iterable, predicate, injectedValue, FastList.<T>newList());
        }
        throw new IllegalArgumentException("Cannot perform a rejectWith on null");
    }

    /**
     * Same as the reject method with two parameters but uses the specified target collection
     * <p/>
     * <pre>e.g.
     * return Iterate.reject(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.person.getLastName().equals("Smith");
     *     }
     * }, new ArrayList());
     * </pre>
     */
    public static <T, R extends Collection<T>> R reject(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).reject(predicate, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.reject((ArrayList<T>) iterable, predicate, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.reject((List<T>) iterable, predicate, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.reject(iterable, predicate, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a reject on null");
    }

    /**
     * Same as the reject method with two parameters but uses the specified target collection.
     */
    public static <T, P, R extends Collection<T>> R rejectWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).rejectWith(predicate, parameter, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.rejectWith(
                    (ArrayList<T>) iterable,
                    predicate,
                    parameter,
                    targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.rejectWith((List<T>) iterable, predicate, parameter, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.rejectWith(iterable, predicate, parameter, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a rejectWith on null");
    }

    /**
     * Add all elements from the source Iterable to the target collection, return the target collection
     */
    public static <T, R extends Collection<T>> R addAllTo(Iterable<? extends T> iterable, R targetCollection)
    {
        Iterate.addAllIterable(iterable, targetCollection);
        return targetCollection;
    }

    public static <T> boolean addAllIterable(Iterable<? extends T> iterable, Collection<T> targetCollection)
    {
        if (iterable == null)
        {
            throw new NullPointerException();
        }
        if (iterable instanceof Collection<?>)
        {
            return targetCollection.addAll((Collection<T>) iterable);
        }
        int oldSize = targetCollection.size();
        Iterate.forEach(iterable, CollectionAddProcedure.on(targetCollection));
        return targetCollection.size() != oldSize;
    }

    /**
     * For each element of the receiver, transformer is evaluated with the element as the parameter. The results of
     * these evaluations are collected into a new collection. The elements are traversed in the same order as they would
     * be if the message #do: had been sent to the receiver.
     * <p/>
     * <pre>e.g.
     * return Iterate.collect(collection, new Function&lt;Person, String&gt;()
     * {
     *     public String value(Person person)
     *     {
     *         return person.getFirstName() + " " + person.getLastName();
     *     }
     * });
     * </pre>
     */
    public static <T, V> Collection<V> collect(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).collect(function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.collect((ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.collect((List<T>) iterable, function);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, V, Collection<V>>collect(
                    iterable,
                    function,
                    DefaultSpeciesNewStrategy.INSTANCE.<V>speciesNew(
                            (Collection<T>) iterable,
                            ((Collection<T>) iterable).size()));
        }
        else if (iterable != null)
        {
            return IterableIterate.collect(iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform a collect on null");
    }

    public static <T, V> Collection<V> flatCollect(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).flatCollect(function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.flatCollect((ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.flatCollect((List<T>) iterable, function);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, V, Collection<V>>flatCollect(
                    iterable,
                    function,
                    DefaultSpeciesNewStrategy.INSTANCE.<V>speciesNew(
                            (Collection<T>) iterable,
                            ((Collection<T>) iterable).size()));
        }
        else if (iterable != null)
        {
            return IterableIterate.flatCollect(iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform a flatCollect on null");
    }

    /**
     * Same as the collect method with two parameters, except that the results are gathered into the specified
     * targetCollection
     * <p/>
     * <pre>e.g.
     * return Iterate.collect(collection, new Function&lt;Person, String&gt;()
     * {
     *     public String value(Person person)
     *     {
     *         return person.getFirstName() + " " + person.getLastName();
     *     }
     * }, new ArrayList());
     * </pre>
     */
    public static <T, A, R extends Collection<A>> R collect(
            Iterable<T> iterable,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).collect(function, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.collect((ArrayList<T>) iterable, function, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.collect((List<T>) iterable, function, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.collect(iterable, function, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a collect on null");
    }

    public static <T, A, R extends Collection<A>> R flatCollect(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<A>> function,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).flatCollect(function, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.flatCollect((ArrayList<T>) iterable, function, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.flatCollect((List<T>) iterable, function, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.flatCollect(iterable, function, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a flatCollect on null");
    }

    /**
     * Same as collect with a Function2 and specified parameter which is passed to the function
     */
    public static <T, P, A> Collection<A> collectWith(
            Iterable<T> iterable,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).collectWith(function, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.collectWith((ArrayList<T>) iterable, function, parameter);
        }
        else if (iterable instanceof List<?>)
        {
            return ListIterate.collectWith((List<T>) iterable, function, parameter);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, P, A, Collection<A>>collectWith(
                    iterable,
                    function,
                    parameter,
                    DefaultSpeciesNewStrategy.INSTANCE.<A>speciesNew(
                            (Collection<T>) iterable,
                            ((Collection<T>) iterable).size()));
        }
        else if (iterable != null)
        {
            return IterableIterate.collectWith(iterable, function, parameter);
        }
        throw new IllegalArgumentException("Cannot perform a collectWith on null");
    }

    /**
     * Same as collectWith but with a targetCollection parameter
     */
    public static <T, P, A, R extends Collection<A>> R collectWith(
            Iterable<T> iterable,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).collectWith(function, parameter, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.collectWith((ArrayList<T>) iterable, function, parameter, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.collectWith((List<T>) iterable, function, parameter, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.collectWith(iterable, function, parameter, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a collectWith on null");
    }

    /**
     * Flattens a collection of collections into one "flat" collection.
     *
     * @param iterable A list of lists, e.g. { { 1,2,3 } , { 4,5 }, { 6 } }
     * @return A flattened list, e.g. { 1,2,3,4,5,6 }
     */
    public static <T> Collection<T> flatten(Iterable<? extends Iterable<T>> iterable)
    {
        return Iterate.flatCollect(iterable, Functions.<Iterable<T>>getPassThru());
    }

    /**
     * Same as {@link #flatten(Iterable)} except that the results are gathered into the specified targetCollection.
     */
    public static <T, R extends Collection<T>> R flatten(Iterable<? extends Iterable<T>> iterable, R targetCollection)
    {
        return Iterate.flatCollect(iterable, Functions.<Iterable<T>>getPassThru(), targetCollection);
    }

    /**
     * Returns the first element of a collection.  In the case of a List it is the element at the first index.  In the
     * case of any other Collection, it is the first element that would be returned during an iteration. If the
     * Collection is null, or empty, the result is <code>null</code>.
     * <p/>
     * WARNING!!! The order of Sets are not guaranteed (except for TreeSets and other Ordered Set implementations), so
     * if you use this method, the first element could be any element from the Set.
     */
    public static <T> T getFirst(Iterable<T> iterable)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).getFirst();
        }
        else if (iterable instanceof List)
        {
            return ListIterate.getFirst((List<T>) iterable);
        }
        else if (iterable instanceof SortedSet && !((SortedSet<T>) iterable).isEmpty())
        {
            return ((SortedSet<T>) iterable).first();
        }
        else if (iterable instanceof Collection)
        {
            return Iterate.isEmpty(iterable) ? null : iterable.iterator().next();
        }
        else if (iterable != null)
        {
            return IterableIterate.getFirst(iterable);
        }
        throw new IllegalArgumentException("Cannot get first from null");
    }

    /**
     * A null-safe check on a collection to see if it isEmpty.  A null collection results in a true.
     */
    public static boolean isEmpty(Iterable<?> iterable)
    {
        if (iterable == null)
        {
            return true;
        }
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<?>) iterable).isEmpty();
        }
        if (iterable instanceof Collection)
        {
            return ((Collection<?>) iterable).isEmpty();
        }
        return IterableIterate.isEmpty(iterable);
    }

    /**
     * A null-safe check on a collection to see if it is notEmpty.  A null collection results in a false.
     */
    public static boolean notEmpty(Iterable<?> iterable)
    {
        return !Iterate.isEmpty(iterable);
    }

    /**
     * Returns the last element of a collection.  In the case of a List it is the element at the last index.  In the
     * case of any other Collection, it is the last element that would be returned during an iteration. If the
     * Collection is null, or empty, the result is <code>null</code>.
     * <p/>
     * WARNING!!! The order of Sets are not guaranteed (except for TreeSets and other Ordered Set implementations), so
     * if you use this method, the last element could be any element from the Set.
     */
    public static <T> T getLast(Iterable<T> iterable)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).getLast();
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.getLast((List<T>) iterable);
        }
        else if (iterable instanceof SortedSet && !((SortedSet<T>) iterable).isEmpty())
        {
            return ((SortedSet<T>) iterable).last();
        }
        else if (iterable instanceof LinkedList && !((LinkedList<T>) iterable).isEmpty())
        {
            return ((LinkedList<T>) iterable).getLast();
        }
        else if (iterable != null)
        {
            return IterableIterate.getLast(iterable);
        }
        throw new IllegalArgumentException("Cannot get last from null");
    }

    /**
     * Return the first element of the receiver for which the predicate evaluates to true when given that element as
     * an argument. The predicate will only be evaluated until such an object is found or until all of the elements
     * of the collection have been used as arguments. That is, there may be elements of the receiver that are never used
     * as arguments to the predicate. The result is null if predicate does not evaluate to true for any
     * element.
     * <p/>
     * <pre>e.g.
     * return Iterate.detect(collection, new Predicate&lt;Person&gt;()
     * {
     *     public boolean value(Person person)
     *     {
     *         return person.getFirstName().equals("John") && person.getLastName().equals("Smith");
     *     }
     * });
     * </pre>
     */
    public static <T> T detect(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).detect(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.detect((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.detect((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.detect(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform detect on null");
    }

    /**
     * Return the first element of the receiver for which the predicate evaluates to true when given that element as
     * an argument. The predicate will only be evaluated until such an object is found or until all of the elements
     * of the collection have been used as arguments. That is, there may be elements of the receiver that are never used
     * as arguments to the predicate. The result is null if predicate does not evaluate to true for any
     * element.
     * <p/>
     * <pre>e.g.
     * Iterate.detectWith(collection, new Predicate2&lt;Person, String&gt;()
     * {
     *     public boolean value(Person person, String fullName)
     *     {
     *         return person.getFullName().equals(fullName);
     *     }
     * }, "John Smith");
     * </pre>
     */
    public static <T, P> T detectWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).detectWith(predicate, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.detectWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.detectWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable != null)
        {
            return IterableIterate.detectWith(iterable, predicate, parameter);
        }
        throw new IllegalArgumentException("Cannot perform detectWith on null");
    }

    /**
     * Return the first element of the receiver for which the predicate evaluates to true when given that element as
     * an argument. The predicate will only be evaluated until such an object is found or until all of the elements
     * of the collection have been used as arguments. That is, there may be elements of the receiver that are never used
     * as arguments to the predicate. If no element causes predicate to evaluate to true, answer the result of
     * ifNone value.
     */
    public static <T> T detectIfNone(Iterable<T> iterable, Predicate<? super T> predicate, T ifNone)
    {
        T result = Iterate.detect(iterable, predicate);
        return result == null ? ifNone : result;
    }

    /**
     * Return the first element of the receiver for which the predicate evaluates to true when given that element as
     * an argument. The predicate will only be evaluated until such an object is found or until all of the elements
     * of the collection have been used as arguments. That is, there may be elements of the receiver that are never used
     * as arguments to the predicate. If no element causes predicate to evaluate to true, answer the result of
     * ifNone value.
     */
    public static <T, P> T detectWithIfNone(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            T ifNone)
    {
        T result = Iterate.detectWith(iterable, predicate, parameter);
        return result == null ? ifNone : result;
    }

    /**
     * Searches for the first occurrence where the predicate evaluates to true.
     */
    public static <T> int detectIndex(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof ArrayList<?>)
        {
            return ArrayListIterate.detectIndex((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof List<?>)
        {
            return ListIterate.detectIndex((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.detectIndex(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform detectIndex on null");
    }

    /**
     * Searches for the first occurrence where the predicate evaluates to true.
     */
    public static <T, P> int detectIndexWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (iterable instanceof ArrayList<?>)
        {
            return ArrayListIterate.detectIndexWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof List<?>)
        {
            return ListIterate.detectIndexWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable != null)
        {
            return IterableIterate.detectIndexWith(iterable, predicate, parameter);
        }
        throw new IllegalArgumentException("Cannot perform detectIndexWith on null");
    }

    /**
     * Returns the final result of evaluating function using each element of the receiver and the previous evaluation
     * result as the parameters. The first evaluation of function is performed with initialValue as the first
     * parameter, and the first element of the receiver as the second parameter. Subsequent evaluations are done with
     * the result of the previous evaluation as the first parameter, and the next element as the second parameter. The
     * result of the last evaluation is answered.
     */
    public static <T, IV> IV injectInto(
            IV injectValue,
            Iterable<T> iterable,
            Function2<? super IV, ? super T, ? extends IV> function)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).injectInto(injectValue, function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.injectInto(injectValue, (ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.injectInto(injectValue, (List<T>) iterable, function);
        }
        else if (iterable != null)
        {
            return IterableIterate.injectInto(injectValue, iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform an injectInto on null");
    }

    /**
     * Returns the final result of evaluating function using each element of the receiver and the previous evaluation
     * result as the parameters. The first evaluation of function is performed with initialValue as the first
     * parameter, and the first element of the receiver as the second parameter. Subsequent evaluations are done with
     * the result of the previous evaluation as the first parameter, and the next element as the second parameter. The
     * result of the last evaluation is answered.
     */
    public static <T> int injectInto(
            int injectValue,
            Iterable<T> iterable,
            IntObjectToIntFunction<? super T> function)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).injectInto(injectValue, function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.injectInto(injectValue, (ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.injectInto(injectValue, (List<T>) iterable, function);
        }
        else if (iterable != null)
        {
            return IterableIterate.injectInto(injectValue, iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform an injectInto on null");
    }

    /**
     * Returns the final result of evaluating function using each element of the receiver and the previous evaluation
     * result as the parameters. The first evaluation of function is performed with initialValue as the first
     * parameter, and the first element of the receiver as the second parameter. Subsequent evaluations are done with
     * the result of the previous evaluation as the first parameter, and the next element as the second parameter. The
     * result of the last evaluation is answered.
     */
    public static <T> long injectInto(
            long injectValue,
            Iterable<T> iterable,
            LongObjectToLongFunction<? super T> function)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).injectInto(injectValue, function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.injectInto(injectValue, (ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.injectInto(injectValue, (List<T>) iterable, function);
        }
        else if (iterable != null)
        {
            return IterableIterate.injectInto(injectValue, iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform an injectInto on null");
    }

    /**
     * Returns the final result of evaluating function using each element of the receiver and the previous evaluation
     * result as the parameters. The first evaluation of function is performed with initialValue as the first
     * parameter, and the first element of the receiver as the second parameter. Subsequent evaluations are done with
     * the result of the previous evaluation as the first parameter, and the next element as the second parameter. The
     * result of the last evaluation is answered.
     */
    public static <T> double injectInto(
            double injectValue,
            Iterable<T> iterable,
            DoubleObjectToDoubleFunction<? super T> function)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).injectInto(injectValue, function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.injectInto(injectValue, (ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.injectInto(injectValue, (List<T>) iterable, function);
        }
        else if (iterable != null)
        {
            return IterableIterate.injectInto(injectValue, iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform an injectInto on null");
    }

    /**
     * Answer the final result of evaluating operation function using each element of the receiver, the previous evaluation
     * result and the additional parameter as inputs to function. The first evaluation of operation is performed with
     * initialValue as the first parameter, the first element of the receiver as the second parameter, and the
     * additional parameter as the third. Subsequent evaluations are done with the result of the previous evaluation as
     * the first parameter, the next element as the second parameter, and the additional parameter as the third. The
     * result of the last evaluation is answered.
     */
    public static <T, IV, P> IV injectIntoWith(
            IV injectValue,
            Iterable<T> iterable,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).injectIntoWith(injectValue, function, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.injectIntoWith(injectValue, (ArrayList<T>) iterable, function, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.injectIntoWith(injectValue, (List<T>) iterable, function, parameter);
        }
        else if (iterable != null)
        {
            return IterableIterate.injectIntoWith(injectValue, iterable, function, parameter);
        }
        throw new IllegalArgumentException("Cannot perform an injectIntoWith on null");
    }

    /**
     * Return true if the predicate evaluates to true for any element of the receiver. Otherwise return false.
     * Return false if the receiver is empty.
     */
    public static <T> boolean anySatisfy(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).anySatisfy(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.anySatisfy((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.anySatisfy((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.anySatisfy(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform an anySatisfy on null");
    }

    /**
     * Return true if the predicate evaluates to true for any element of the receiver. Otherwise return false.
     * Return false if the receiver is empty.
     */
    public static <T, P> boolean anySatisfyWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).anySatisfyWith(predicate, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.anySatisfyWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.anySatisfyWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable != null)
        {
            return IterableIterate.anySatisfyWith(iterable, predicate, parameter);
        }
        throw new IllegalArgumentException("Cannot perform an anySatisfyWith on null");
    }

    /**
     * Return true if the predicate evaluates to true for every element of the receiver. Otherwise return false.
     */
    public static <T> boolean allSatisfy(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).allSatisfy(predicate);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.allSatisfy((ArrayList<T>) iterable, predicate);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.allSatisfy((List<T>) iterable, predicate);
        }
        else if (iterable != null)
        {
            return IterableIterate.allSatisfy(iterable, predicate);
        }
        throw new IllegalArgumentException("Cannot perform an allSatisfy on null");
    }

    /**
     * Return true if the predicate evaluates to true for every element of the receiver. Otherwise return false.
     */
    public static <T, P> boolean allSatisfyWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).allSatisfyWith(predicate, parameter);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.allSatisfyWith((ArrayList<T>) iterable, predicate, parameter);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.allSatisfyWith((List<T>) iterable, predicate, parameter);
        }
        else if (iterable != null)
        {
            return IterableIterate.allSatisfyWith(iterable, predicate, parameter);
        }
        throw new IllegalArgumentException("Cannot perform an allSatisfyWith on null");
    }

    /**
     * Iterate over the specified collection applying the specified Function to each element to calculate
     * a key and return the results as a Map.
     */
    public static <T, K> MutableMap<K, T> toMap(
            Iterable<T> iterable,
            Function<? super T, ? extends K> keyFunction)
    {
        MutableMap<K, T> map = UnifiedMap.newMap();
        Iterate.forEach(iterable, new MapCollectProcedure<T, K, T>(map, keyFunction));
        return map;
    }

    /**
     * Iterate over the specified collection applying the specified Functions to each element to calculate
     * a key and value, and return the results as a Map.
     */
    public static <T, K, V> MutableMap<K, V> toMap(
            Iterable<T> iterable,
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction)
    {
        return Iterate.addToMap(iterable, keyFunction, valueFunction, UnifiedMap.<K, V>newMap());
    }

    /**
     * Iterate over the specified collection applying a specific Function to each element to calculate a
     * key, and return the results as a Map.
     */
    public static <T, K, V, M extends Map<K, V>> M addToMap(
            Iterable<T> iterable,
            Function<? super T, ? extends K> keyFunction,
            M map)
    {
        Iterate.forEach(iterable, new MapCollectProcedure<T, K, V>(map, keyFunction));
        return map;
    }

    /**
     * Iterate over the specified collection applying the specified Functions to each element to calculate
     * a key and value, and return the results as a Map.
     */
    public static <T, K, V, M extends Map<K, V>> M addToMap(
            Iterable<T> iterable,
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction,
            M map)
    {
        Iterate.forEach(iterable, new MapCollectProcedure<T, K, V>(map, keyFunction, valueFunction));
        return map;
    }

    /**
     * Return the specified collection as a sorted List.
     */
    public static <T extends Comparable<? super T>> MutableList<T> toSortedList(Iterable<T> iterable)
    {
        return Iterate.toSortedList(iterable, Comparators.naturalOrder());
    }

    /**
     * Return the specified collection as a sorted List using the specified Comparator.
     */
    public static <T> MutableList<T> toSortedList(Iterable<T> iterable, Comparator<? super T> comparator)
    {
        return FastList.<T>newList(iterable).sortThis(comparator);
    }

    public static int sizeOf(Iterable<?> iterable)
    {
        if (iterable instanceof Collection)
        {
            return ((Collection<?>) iterable).size();
        }
        else if (iterable instanceof RichIterable)
        {
            return ((RichIterable<?>) iterable).size();
        }
        return Iterate.count(iterable, Predicates.alwaysTrue());
    }

    public static boolean contains(Iterable<?> iterable, Object value)
    {
        if (iterable instanceof Collection)
        {
            return ((Collection<?>) iterable).contains(value);
        }
        else if (iterable instanceof RichIterable)
        {
            return ((RichIterable<?>) iterable).contains(value);
        }
        return IterableIterate.detectIndex(iterable, Predicates.equal(value)) > -1;
    }

    public static <T> Object[] toArray(Iterable<T> iterable)
    {
        if (iterable == null)
        {
            throw new NullPointerException();
        }
        if (iterable instanceof Collection)
        {
            return ((Collection<T>) iterable).toArray();
        }
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).toArray();
        }
        MutableList<T> result = Lists.mutable.of();
        Iterate.addAllTo(iterable, result);
        return result.toArray();
    }

    public static <T> T[] toArray(Iterable<? extends T> iterable, T[] target)
    {
        if (iterable instanceof Collection)
        {
            return ((Collection<T>) iterable).toArray(target);
        }
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).toArray(target);
        }
        MutableList<T> result = Lists.mutable.of();
        Iterate.addAllTo(iterable, result);
        return result.toArray(target);
    }

    /**
     * @see RichIterable#groupBy(Function)
     */
    public static <T, V> MutableMultimap<V, T> groupBy(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).groupBy(function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.groupBy((ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.groupBy((List<T>) iterable, function);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.groupBy(iterable, function, FastListMultimap.<V, T>newMultimap());
        }
        else if (iterable != null)
        {
            return IterableIterate.groupBy(iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform a groupBy on null");
    }

    /**
     * @see RichIterable#groupBy(Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupBy(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function,
            R targetMultimap)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).groupBy(function, targetMultimap);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.groupBy((ArrayList<T>) iterable, function, targetMultimap);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.groupBy((List<T>) iterable, function, targetMultimap);
        }
        else if (iterable != null)
        {
            return IterableIterate.groupBy(iterable, function, targetMultimap);
        }
        throw new IllegalArgumentException("Cannot perform a groupBy on null");
    }

    /**
     * @see RichIterable#groupByEach(Function)
     */
    public static <T, V> MutableMultimap<V, T> groupByEach(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).groupByEach(function);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.groupByEach((ArrayList<T>) iterable, function);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.groupByEach((List<T>) iterable, function);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.groupByEach(iterable, function, FastListMultimap.<V, T>newMultimap());
        }
        else if (iterable != null)
        {
            return IterableIterate.groupByEach(iterable, function);
        }
        throw new IllegalArgumentException("Cannot perform a groupByEach on null");
    }

    /**
     * @see RichIterable#groupByEach(Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupByEach(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).groupByEach(function, targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.groupByEach((ArrayList<T>) iterable, function, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.groupByEach((List<T>) iterable, function, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.groupByEach(iterable, function, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a groupBy on null");
    }

    public static <T> T min(Iterable<T> iterable, Comparator<? super T> comparator)
    {
        MinComparatorProcedure<T> procedure = new MinComparatorProcedure<T>(comparator);
        Iterate.forEach(iterable, procedure);
        return procedure.getResult();
    }

    public static <T> T max(Iterable<T> iterable, Comparator<? super T> comparator)
    {
        MaxComparatorProcedure<T> procedure = new MaxComparatorProcedure<T>(comparator);
        Iterate.forEach(iterable, procedure);
        return procedure.getResult();
    }

    public static <T> T min(Iterable<T> iterable)
    {
        return Iterate.min(iterable, Comparators.naturalOrder());
    }

    public static <T> T max(Iterable<T> iterable)
    {
        return Iterate.max(iterable, Comparators.naturalOrder());
    }

    public static <T> T getOnly(Iterable<T> iterable)
    {
        if (iterable != null)
        {
            return IterableIterate.getOnly(iterable);
        }
        throw new IllegalArgumentException("Cannot perform getOnly on null");
    }

    public static <X, Y> Collection<Pair<X, Y>> zip(Iterable<X> xs, Iterable<Y> ys)
    {
        if (xs instanceof MutableCollection)
        {
            return ((MutableCollection<X>) xs).zip(ys);
        }
        else if (xs instanceof ArrayList)
        {
            return ArrayListIterate.zip((ArrayList<X>) xs, ys);
        }
        else if (xs instanceof RandomAccess)
        {
            return RandomAccessListIterate.zip((List<X>) xs, ys);
        }
        else if (xs != null)
        {
            return IterableIterate.zip(xs, ys);
        }
        throw new IllegalArgumentException("Cannot perform a zip on null");
    }

    public static <X, Y, R extends Collection<Pair<X, Y>>> R zip(
            Iterable<X> xs,
            Iterable<Y> ys,
            R targetCollection)
    {
        if (xs instanceof RichIterable)
        {
            return ((RichIterable<X>) xs).zip(ys, targetCollection);
        }
        else if (xs instanceof ArrayList)
        {
            return ArrayListIterate.zip((ArrayList<X>) xs, ys, targetCollection);
        }
        else if (xs instanceof RandomAccess)
        {
            return RandomAccessListIterate.zip((List<X>) xs, ys, targetCollection);
        }
        else if (xs != null)
        {
            return IterableIterate.zip(xs, ys, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a zip on null");
    }

    public static <T> Collection<Pair<T, Integer>> zipWithIndex(Iterable<T> iterable)
    {
        if (iterable instanceof MutableCollection)
        {
            return ((MutableCollection<T>) iterable).zipWithIndex();
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.zipWithIndex((ArrayList<T>) iterable);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.zipWithIndex((List<T>) iterable);
        }
        else if (iterable instanceof Collection)
        {
            return IterableIterate.<T, Collection<Pair<T, Integer>>>zipWithIndex(
                    iterable,
                    DefaultSpeciesNewStrategy.INSTANCE.<Pair<T, Integer>>speciesNew(
                            (Collection<T>) iterable,
                            ((Collection<T>) iterable).size()));
        }
        else if (iterable != null)
        {
            return IterableIterate.zipWithIndex(iterable);
        }
        throw new IllegalArgumentException("Cannot perform a zipWithIndex on null");
    }

    public static <T, R extends Collection<Pair<T, Integer>>> R zipWithIndex(
            Iterable<T> iterable,
            R targetCollection)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).zipWithIndex(targetCollection);
        }
        else if (iterable instanceof ArrayList)
        {
            return ArrayListIterate.zipWithIndex((ArrayList<T>) iterable, targetCollection);
        }
        else if (iterable instanceof RandomAccess)
        {
            return RandomAccessListIterate.zipWithIndex((List<T>) iterable, targetCollection);
        }
        else if (iterable != null)
        {
            return IterableIterate.zipWithIndex(iterable, targetCollection);
        }
        throw new IllegalArgumentException("Cannot perform a zipWithIndex on null");
    }

    public static <T> RichIterable<RichIterable<T>> chunk(Iterable<T> iterable, int size)
    {
        if (iterable instanceof RichIterable)
        {
            return ((RichIterable<T>) iterable).chunk(size);
        }
        else if (iterable != null)
        {
            return IterableIterate.chunk(iterable, size);
        }
        throw new IllegalArgumentException("Cannot perform a chunk on null");
    }
}
