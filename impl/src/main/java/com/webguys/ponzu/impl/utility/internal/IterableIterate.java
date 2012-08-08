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

package com.webguys.ponzu.impl.utility.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.function.Function3;
import com.webguys.ponzu.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.webguys.ponzu.api.block.function.primitive.IntObjectToIntFunction;
import com.webguys.ponzu.api.block.function.primitive.LongObjectToLongFunction;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.multimap.MutableMultimap;
import com.webguys.ponzu.api.partition.list.PartitionMutableList;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.api.tuple.Twin;
import com.webguys.ponzu.impl.block.factory.Comparators;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.multimap.list.FastListMultimap;
import com.webguys.ponzu.impl.utility.Iterate;

/**
 * The IterableIterate class provides iteration protocol methods for any Iterable instance
 */
public final class IterableIterate
{
    private IterableIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * @see Iterate#filter(Iterable, Predicate)
     */
    public static <T> MutableList<T> filter(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        return IterableIterate.filter(iterable, predicate, FastList.<T>newList());
    }

    /**
     * @see Iterate#partitionWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> Twin<MutableList<T>> partitionWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return IteratorIterate.partitionWith(iterable.iterator(), predicate, injectedValue);
    }

    /**
     * @see Iterate#partition(Iterable, Predicate)
     */
    public static <T> PartitionMutableList<T> partition(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        return IteratorIterate.partition(iterable.iterator(), predicate);
    }

    /**
     * @see Iterate#count(Iterable, Predicate)
     */
    public static <T> int count(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        return IteratorIterate.count(iterable.iterator(), predicate);
    }

    /**
     * @see Iterate#countWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> int countWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return IteratorIterate.countWith(iterable.iterator(), predicate, injectedValue);
    }

    /**
     * @see Iterate#transformIf(Iterable, Predicate, Function)
     */
    public static <T, V> MutableList<V> transformIf(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return IterableIterate.tranformIf(iterable, predicate, function, FastList.<V>newList());
    }

    public static boolean isEmpty(Iterable<?> iterable)
    {
        return !iterable.iterator().hasNext();
    }

    public static boolean notEmpty(Iterable<?> iterable)
    {
        return !IterableIterate.isEmpty(iterable);
    }

    public static <T> T getFirst(Iterable<T> iterable)
    {
        Iterator<T> iterator = iterable.iterator();
        if (iterator.hasNext())
        {
            return iterator.next();
        }
        return null;
    }

    public static <T> T getLast(Iterable<T> iterable)
    {
        T last = null;
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext())
        {
            last = iterator.next();
        }
        return last;
    }

    /**
     * @see Iterate#filter(Iterable, Predicate, Collection)
     */
    public static <T, R extends Collection<T>> R filter(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        return IteratorIterate.filter(iterable.iterator(), predicate, targetCollection);
    }

    /**
     * @see Iterate#filterWith(Iterable, Predicate2, Object, Collection)
     */
    public static <T, P, R extends Collection<T>> R filterWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P injectedValue,
            R targetCollection)
    {
        return IteratorIterate.filterWith(iterable.iterator(), predicate, injectedValue, targetCollection);
    }

    /**
     * @see Iterate#tranformIf(Iterable, Predicate, Function, Collection)
     */
    public static <T, V, R extends Collection<V>> R tranformIf(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function,
            R targetCollection)
    {
        return IteratorIterate.transformIf(iterable.iterator(), predicate, function, targetCollection);
    }

    /**
     * @see Iterate#filterNot(Iterable, Predicate)
     */
    public static <T> MutableList<T> filterNot(Iterable<T> collection, Predicate<? super T> predicate)
    {
        return IterableIterate.filterNot(collection, predicate, FastList.<T>newList());
    }

    /**
     * @see Iterate#filterNot(Iterable, Predicate, Collection)
     */
    public static <T, R extends Collection<T>> R filterNot(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        return IteratorIterate.filterNot(iterable.iterator(), predicate, targetCollection);
    }

    /**
     * @see Iterate#filterNotWith(Iterable, Predicate2, Object, Collection)
     */
    public static <T, P, R extends Collection<T>> R filterNotWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return IteratorIterate.filterNotWith(iterable.iterator(), predicate, parameter, targetCollection);
    }

    /**
     * @see Iterate#transform(Iterable, Function)
     */
    public static <T, V> MutableList<V> transform(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function)
    {
        return IterableIterate.transform(iterable, function, FastList.<V>newList());
    }

    /**
     * @see Iterate#transform(Iterable, Function, Collection)
     */
    public static <T, V, R extends Collection<V>> R transform(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function,
            R targetCollection)
    {
        return IteratorIterate.transform(iterable.iterator(), function, targetCollection);
    }

    /**
     * @see Iterate#flatTransform(Iterable, Function)
     */
    public static <T, V> MutableList<V> flatTransform(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function)
    {
        return IterableIterate.flatTransform(iterable, function, FastList.<V>newList());
    }

    /**
     * @see Iterate#flatTransform(Iterable, Function, Collection)
     */
    public static <T, V, R extends Collection<V>> R flatTransform(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function,
            R targetCollection)
    {
        return IteratorIterate.flatTransform(iterable.iterator(), function, targetCollection);
    }

    /**
     * @see Iterate#forEach(Iterable, Procedure)
     */
    public static <T> void forEach(Iterable<T> iterable, Procedure<? super T> procedure)
    {
        IteratorIterate.forEach(iterable.iterator(), procedure);
    }

    /**
     * @see Iterate#forEachWithIndex(Iterable, ObjectIntProcedure)
     */
    public static <T> void forEachWithIndex(Iterable<T> iterable, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        IteratorIterate.forEachWithIndex(iterable.iterator(), objectIntProcedure);
    }

    /**
     * @see Iterate#find(Iterable, Predicate)
     */
    public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        return IteratorIterate.find(iterable.iterator(), predicate);
    }

    /**
     * @see Iterate#findWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> T findWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return IteratorIterate.findWith(iterable.iterator(), predicate, injectedValue);
    }

    /**
     * @see Iterate#foldLeft(Object, Iterable, Function2)
     */
    public static <T, IV> IV foldLeft(
            IV injectValue,
            Iterable<T> iterable,
            Function2<? super IV, ? super T, ? extends IV> function)
    {
        return IteratorIterate.foldLeft(injectValue, iterable.iterator(), function);
    }

    /**
     * @see Iterate#foldLeft(int, Iterable, IntObjectToIntFunction)
     */
    public static <T> int foldLeft(
            int injectValue,
            Iterable<T> iterable,
            IntObjectToIntFunction<? super T> function)
    {
        return IteratorIterate.foldLeft(injectValue, iterable.iterator(), function);
    }

    /**
     * @see Iterate#foldLeft(long, Iterable, LongObjectToLongFunction)
     */
    public static <T> long foldLeft(
            long injectValue,
            Iterable<T> iterable,
            LongObjectToLongFunction<? super T> function)
    {
        return IteratorIterate.foldLeft(injectValue, iterable.iterator(), function);
    }

    /**
     * @see Iterate#foldLeft(double, Iterable, DoubleObjectToDoubleFunction)
     */
    public static <T> double foldLeft(
            double injectValue,
            Iterable<T> iterable,
            DoubleObjectToDoubleFunction<? super T> function)
    {
        return IteratorIterate.foldLeft(injectValue, iterable.iterator(), function);
    }

    /**
     * @see Iterate#foldLeftWith(Object, Iterable, Function3, Object)
     */
    public static <T, IV, P> IV foldLeftWith(
            IV injectValue,
            Iterable<T> iterable,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        return IteratorIterate.foldLeftWith(injectValue, iterable.iterator(), function, parameter);
    }

    /**
     * @see Iterate#anySatisfy(Iterable, Predicate)
     */
    public static <T> boolean anySatisfy(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        return IteratorIterate.anySatisfy(iterable.iterator(), predicate);
    }

    /**
     * @see Iterate#anySatisfyWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> boolean anySatisfyWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return IteratorIterate.anySatisfyWith(iterable.iterator(), predicate, injectedValue);
    }

    /**
     * @see Iterate#allSatisfy(Iterable, Predicate)
     */
    public static <T> boolean allSatisfy(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        return IteratorIterate.allSatisfy(iterable.iterator(), predicate);
    }

    /**
     * @see Iterate#allSatisfyWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> boolean allSatisfyWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return IteratorIterate.allSatisfyWith(iterable.iterator(), predicate, injectedValue);
    }

    /**
     * @see Iterate#removeIf(Iterable, Predicate)
     */
    public static <T> Iterable<T> removeIf(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        IteratorIterate.removeIf(iterable.iterator(), predicate);
        return iterable;
    }

    /**
     * @see Iterate#removeIfWith(Iterable, Predicate2, Object)
     */
    public static <T, P> Iterable<T> removeIfWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        IteratorIterate.removeIfWith(iterable.iterator(), predicate, parameter);
        return iterable;
    }

    public static <T> Iterable<T> removeIf(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            Procedure<? super T> procedure)
    {
        IteratorIterate.removeIf(iterable.iterator(), predicate, procedure);
        return iterable;
    }

    /**
     * @see Iterate#findIndex(Iterable, Predicate)
     */
    public static <T> int findIndex(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        return IteratorIterate.findIndex(iterable.iterator(), predicate);
    }

    /**
     * @see Iterate#findIndexWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> int findIndexWith(
            Iterable<T> iterable,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return IteratorIterate.findIndexWith(iterable.iterator(), predicate, injectedValue);
    }

    /**
     * @see Iterate#forEachWith(Iterable, Procedure2, Object)
     */
    public static <T, P> void forEachWith(
            Iterable<T> iterable,
            Procedure2<? super T, ? super P> procedure,
            P parameter)
    {
        IteratorIterate.forEachWith(iterable.iterator(), procedure, parameter);
    }

    /**
     * @see Iterate#transformWith(Iterable, Function2, Object)
     */
    public static <T, P, V> MutableList<V> transformWith(
            Iterable<T> iterable,
            Function2<? super T, ? super P, ? extends V> function,
            P parameter)
    {
        return IterableIterate.transformWith(iterable, function, parameter, FastList.<V>newList());
    }

    /**
     * @see Iterate#transformWith(Iterable, Function2, Object, Collection)
     */
    public static <T, P, A, R extends Collection<A>> R transformWith(
            Iterable<T> iterable,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        return IteratorIterate.transformWith(iterable.iterator(), function, parameter, targetCollection);
    }

    /**
     * @see Iterate#take(Iterable, int)
     */
    public static <T> Collection<T> take(Iterable<T> iterable, int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        return IterableIterate.take(iterable, count, FastList.<T>newList(count));
    }

    /**
     * @see Iterate#take(Iterable, int)
     */
    public static <T, R extends Collection<T>> R take(Iterable<T> iterable, int count, R targetCollection)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext() && count-- > 0)
        {
            targetCollection.add(iterator.next());
        }
        return targetCollection;
    }

    /**
     * @see Iterate#drop(Iterable, int)
     */
    public static <T> Collection<T> drop(Iterable<T> list, int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        return IterableIterate.drop(list, count, FastList.<T>newList());
    }

    /**
     * @see Iterate#drop(Iterable, int)
     */
    public static <T, R extends Collection<T>> R drop(Iterable<T> iterable, int count, R targetCollection)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        Iterator<T> iterator = iterable.iterator();
        for (int i = 0; iterator.hasNext(); i++)
        {
            T element = iterator.next();
            if (i >= count)
            {
                targetCollection.add(element);
            }
        }
        return targetCollection;
    }

    /**
     * @see RichIterable#appendString(Appendable, String, String, String)
     */
    public static <T> void appendString(
            Iterable<T> iterable,
            Appendable appendable,
            String start,
            String separator,
            String end)
    {
        try
        {
            appendable.append(start);

            Iterator<T> iterator = iterable.iterator();
            if (iterator.hasNext())
            {
                appendable.append(stringValueOfItem(iterable, iterator.next()));
                while (iterator.hasNext())
                {
                    appendable.append(separator);
                    appendable.append(stringValueOfItem(iterable, iterator.next()));
                }
            }

            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see Iterate#groupBy(Iterable, Function)
     */
    public static <T, V> FastListMultimap<V, T> groupBy(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function)
    {
        return IterableIterate.groupBy(iterable, function, new FastListMultimap<V, T>());
    }

    /**
     * @see Iterate#groupBy(Iterable, Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupBy(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function,
            R target)
    {
        return IteratorIterate.groupBy(iterable.iterator(), function, target);
    }

    /**
     * @see Iterate#groupByEach(Iterable, Function)
     */
    public static <T, V> FastListMultimap<V, T> groupByEach(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function)
    {
        return IterableIterate.groupByEach(iterable, function, new FastListMultimap<V, T>());
    }

    /**
     * @see Iterate#groupByEach(Iterable, Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupByEach(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        return IteratorIterate.groupByEach(iterable.iterator(), function, target);
    }

    public static <T> T getOnly(Iterable<T> iterable)
    {
        Iterator<? extends T> iterator = iterable.iterator();
        if (!iterator.hasNext())
        {
            throw new IllegalArgumentException("Iterable is empty.");
        }

        T result = iterator.next();
        if (iterator.hasNext())
        {
            throw new IllegalArgumentException("Iterable has multiple values");
        }

        return result;
    }

    /**
     * @see Iterate#zip(Iterable, Iterable)
     */
    public static <X, Y> Collection<Pair<X, Y>> zip(
            Iterable<X> xs,
            Iterable<Y> ys)
    {
        return IterableIterate.zip(xs, ys, FastList.<Pair<X, Y>>newList());
    }

    /**
     * @see Iterate#zip(Iterable, Iterable, Collection)
     */
    public static <X, Y, R extends Collection<Pair<X, Y>>> R zip(
            Iterable<X> xs,
            Iterable<Y> ys,
            R target)
    {
        return IteratorIterate.zip(xs.iterator(), ys.iterator(), target);
    }

    /**
     * @see Iterate#zipWithIndex(Iterable)
     */
    public static <T> Collection<Pair<T, Integer>> zipWithIndex(Iterable<T> iterable)
    {
        return IterableIterate.zipWithIndex(iterable, FastList.<Pair<T, Integer>>newList());
    }

    /**
     * @see Iterate#zipWithIndex(Iterable, Collection)
     */
    public static <T, R extends Collection<Pair<T, Integer>>> R zipWithIndex(Iterable<T> iterable, R target)
    {
        return IteratorIterate.zipWithIndex(iterable.iterator(), target);
    }

    /**
     * @see Iterate#chunk(Iterable, int)
     */
    public static <T> RichIterable<RichIterable<T>> chunk(Iterable<T> iterable, int size)
    {
        return IteratorIterate.chunk(iterable.iterator(), size);
    }

    /**
     * @see Iterate#min(Iterable, Comparator)
     */
    public static <T> T min(Iterable<T> iterable, Comparator<? super T> comparator)
    {
        return IteratorIterate.min(iterable.iterator(), comparator);
    }

    /**
     * @see Iterate#max(Iterable, Comparator)
     */
    public static <T> T max(Iterable<T> iterable, Comparator<? super T> comparator)
    {
        return IteratorIterate.max(iterable.iterator(), comparator);
    }

    /**
     * @see Iterate#min(Iterable)
     */
    public static <T> T min(Iterable<T> iterable)
    {
        return IteratorIterate.min(iterable.iterator(), Comparators.naturalOrder());
    }

    /**
     * @see Iterate#max(Iterable)
     */
    public static <T> T max(Iterable<T> iterable)
    {
        return IteratorIterate.max(iterable.iterator(), Comparators.naturalOrder());
    }

    public static <T> void forEach(Iterable<T> iterable, int from, int to, Procedure<? super T> procedure)
    {
        if (from < 0 || to < 0)
        {
            throw new IllegalArgumentException("Neither from nor to may be negative.");
        }

        Iterator<T> iterator = IteratorIterate.advanceIteratorTo(iterable.iterator(), from);
        int i = from;
        while (iterator.hasNext() && i <= to)
        {
            procedure.value(iterator.next());
            i++;
        }
    }

    public static <T> void forEachWithIndex(List<T> iterable, int from, int to, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        if (from < 0 || to < 0)
        {
            throw new IllegalArgumentException("Neither from nor to may be negative.");
        }

        Iterator<T> iterator = IteratorIterate.advanceIteratorTo(iterable.iterator(), from);
        int i = from;
        while (iterator.hasNext() && i <= to)
        {
            objectIntProcedure.value(iterator.next(), i++);
        }
    }

    public static <T> String stringValueOfItem(Iterable<T> iterable, T item)
    {
        return item == iterable
                ? "(this " + iterable.getClass().getSimpleName() + ')'
                : String.valueOf(item);
    }
}
