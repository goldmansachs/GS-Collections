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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

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
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.partition.list.PartitionFastList;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.internal.InternalArrayIterate;
import com.gs.collections.impl.utility.internal.RandomAccessListIterate;

/**
 * The ArrayListIterate class provides a few of the methods from the Smalltalk Collection Protocol for use with
 * ArrayLists. This includes do:, select:, reject:, collect:, inject:into:, detect:, detect:ifNone:, anySatisfy: and
 * allSatisfy:
 * <p/>
 * This class is  optimized for iterating over ArrayLists.
 */
public final class ArrayListIterate
{
    private static final Field ELEMENT_DATA_FIELD;
    private static final Field SIZE_FIELD;
    private static final int MIN_DIRECT_ARRAY_ACCESS_SIZE = 100;

    static
    {
        Field data = null;
        Field size = null;
        try
        {
            data = ArrayList.class.getDeclaredField("elementData");
            size = ArrayList.class.getDeclaredField("size");
            try
            {
                data.setAccessible(true);
                size.setAccessible(true);
            }
            catch (SecurityException ignored)
            {
                data = null;
                size = null;
            }
        }
        catch (NoSuchFieldException ignored)
        {
        }
        ELEMENT_DATA_FIELD = data;
        SIZE_FIELD = size;
    }

    private ArrayListIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * @see Iterate#select(Iterable, Predicate)
     */
    public static <T> ArrayList<T> select(ArrayList<T> list, Predicate<? super T> predicate)
    {
        return ArrayListIterate.select(list, predicate, new ArrayList<T>());
    }

    /**
     * @see Iterate#selectWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> ArrayList<T> selectWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return ArrayListIterate.selectWith(list, predicate, injectedValue, new ArrayList<T>());
    }

    /**
     * @see Iterate#select(Iterable, Predicate, Collection)
     */
    public static <T, R extends Collection<T>> R select(
            ArrayList<T> list,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i]))
                {
                    targetCollection.add(elements[i]);
                }
            }
            return targetCollection;
        }
        return RandomAccessListIterate.select(list, predicate, targetCollection);
    }

    /**
     * @see Iterate#selectWith(Iterable, Predicate2, Object, Collection)
     */
    public static <T, P, R extends Collection<T>> R selectWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i], parameter))
                {
                    targetCollection.add(elements[i]);
                }
            }
            return targetCollection;
        }
        return RandomAccessListIterate.selectWith(list, predicate, parameter, targetCollection);
    }

    /**
     * @see Iterate#count(Iterable, Predicate)
     */
    public static <T> int count(ArrayList<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            int count = 0;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i]))
                {
                    count++;
                }
            }
            return count;
        }
        return RandomAccessListIterate.count(list, predicate);
    }

    public static <T, IV> int countWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            int count = 0;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i], injectedValue))
                {
                    count++;
                }
            }
            return count;
        }
        return RandomAccessListIterate.countWith(list, predicate, injectedValue);
    }

    /**
     * @see Iterate#collectIf(Iterable, Predicate, Function)
     */
    public static <T, A> ArrayList<A> collectIf(
            ArrayList<T> list,
            Predicate<? super T> predicate,
            Function<? super T, ? extends A> function)
    {
        return ArrayListIterate.collectIf(list, predicate, function, new ArrayList<A>());
    }

    /**
     * @see Iterate#collectIf(Iterable, Predicate, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R collectIf(
            ArrayList<T> list,
            Predicate<? super T> predicate,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i]))
                {
                    targetCollection.add(function.valueOf(elements[i]));
                }
            }
            return targetCollection;
        }
        return RandomAccessListIterate.collectIf(list, predicate, function, targetCollection);
    }

    /**
     * @see Iterate#reject(Iterable, Predicate)
     */
    public static <T> ArrayList<T> reject(ArrayList<T> list, Predicate<? super T> predicate)
    {
        return ArrayListIterate.reject(list, predicate, new ArrayList<T>());
    }

    /**
     * @see Iterate#rejectWith(Iterable, Predicate2, Object)
     */
    public static <T, IV> ArrayList<T> rejectWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        return ArrayListIterate.rejectWith(list, predicate, injectedValue, new ArrayList<T>());
    }

    /**
     * @see Iterate#reject(Iterable, Predicate, Collection)
     */
    public static <T, R extends Collection<T>> R reject(
            ArrayList<T> list,
            Predicate<? super T> predicate,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i]))
                {
                    targetCollection.add(elements[i]);
                }
            }
            return targetCollection;
        }
        return RandomAccessListIterate.reject(list, predicate, targetCollection);
    }

    /**
     * @see Iterate#reject(Iterable, Predicate, Collection)
     */
    public static <T, P, R extends Collection<T>> R rejectWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P injectedValue,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i], injectedValue))
                {
                    targetCollection.add(elements[i]);
                }
            }
            return targetCollection;
        }
        return RandomAccessListIterate.rejectWith(list, predicate, injectedValue, targetCollection);
    }

    /**
     * @see Iterate#collect(Iterable, Function)
     */
    public static <T, A> ArrayList<A> collect(
            ArrayList<T> list,
            Function<? super T, ? extends A> function)
    {
        return ArrayListIterate.collect(list, function, new ArrayList<A>(list.size()));
    }

    /**
     * @see Iterate#collect(Iterable, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R collect(
            ArrayList<T> list,
            Function<? super T, ? extends A> function,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                targetCollection.add(function.valueOf(elements[i]));
            }
            return targetCollection;
        }
        return RandomAccessListIterate.collect(list, function, targetCollection);
    }

    /**
     * @see Iterate#flatCollect(Iterable, Function)
     */
    public static <T, A> ArrayList<A> flatCollect(
            ArrayList<T> list,
            Function<? super T, ? extends Iterable<A>> function)
    {
        return ArrayListIterate.flatCollect(list, function, new ArrayList<A>(list.size()));
    }

    /**
     * @see Iterate#flatCollect(Iterable, Function, Collection)
     */
    public static <T, A, R extends Collection<A>> R flatCollect(
            ArrayList<T> list,
            Function<? super T, ? extends Iterable<A>> function,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                Iterate.addAllTo(function.valueOf(elements[i]), targetCollection);
            }
            return targetCollection;
        }
        return RandomAccessListIterate.flatCollect(list, function, targetCollection);
    }

    /**
     * @see Iterate#forEach(Iterable, Procedure)
     */
    public static <T> void forEach(ArrayList<T> list, Procedure<? super T> procedure)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                procedure.value(elements[i]);
            }
        }
        else
        {
            RandomAccessListIterate.forEach(list, procedure);
        }
    }

    /**
     * Reverses over the List in reverse order executing the Procedure for each element
     */
    public static <T> void reverseForEach(ArrayList<T> list, Procedure<? super T> procedure)
    {
        if (!list.isEmpty())
        {
            ArrayListIterate.forEach(list, list.size() - 1, 0, procedure);
        }
    }

    /**
     * Interates over the section of the list covered by the specified indexes.  The indexes are both inclusive.  If the
     * from is less than the to, the list is iterated in forward order. If the from is greater than the to, then the
     * list is iterated in the reverse order.
     * <p/>
     * <p/>
     * <pre>e.g.
     * ArrayList<People> people = new ArrayList<People>(FastList.newListWith(ted, mary, bob, sally));
     * ArrayListIterate.forEach(people, 0, 1, new Procedure<Person>()
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
    public static <T> void forEach(ArrayList<T> list, int from, int to, Procedure<? super T> procedure)
    {
        ListIterate.rangeCheck(from, to, list.size());
        if (ArrayListIterate.isOptimizableArrayList(list, to - from + 1))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);

            InternalArrayIterate.forEachWithoutChecks(elements, from, to, procedure);
        }
        else
        {
            RandomAccessListIterate.forEach(list, from, to, procedure);
        }
    }

    /**
     * Interates over the section of the list covered by the specified indexes.  The indexes are both inclusive.  If the
     * from is less than the to, the list is iterated in forward order. If the from is greater than the to, then the
     * list is iterated in the reverse order. The index passed into the ProcedureWithInt is the actual index of the
     * range.
     * <p/>
     * <p/>
     * <pre>e.g.
     * ArrayList<People> people = new ArrayList<People>(FastList.newListWith(ted, mary, bob, sally));
     * ArrayListIterate.forEachWithIndex(people, 0, 1, new ProcedureWithInt<Person>()
     * {
     *     public void value(Person person, int index)
     *     {
     *          LOGGER.info(person.getName() + " at index: " + index);
     *     }
     * });
     * </pre>
     * <p/>
     * This code would output ted and mary's names.
     */
    public static <T> void forEachWithIndex(
            ArrayList<T> list,
            int from,
            int to,
            ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ListIterate.rangeCheck(from, to, list.size());
        if (ArrayListIterate.isOptimizableArrayList(list, to - from + 1))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);

            InternalArrayIterate.forEachWithIndexWithoutChecks(elements, from, to, objectIntProcedure);
        }
        else
        {
            RandomAccessListIterate.forEachWithIndex(list, from, to, objectIntProcedure);
        }
    }

    /**
     * For each element in both of the Lists, operation is evaluated with both elements as parameters.
     */
    public static <T1, T2> void forEachInBoth(
            ArrayList<T1> list1,
            ArrayList<T2> list2,
            Procedure2<? super T1, ? super T2> procedure)
    {
        RandomAccessListIterate.forEachInBoth(list1, list2, procedure);
    }

    /**
     * Iterates over a collection passing each element and the current relative int index to the specified instance of
     * ProcedureWithInt.
     */
    public static <T> void forEachWithIndex(ArrayList<T> list, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                objectIntProcedure.value(elements[i], i);
            }
        }
        else
        {
            RandomAccessListIterate.forEachWithIndex(list, objectIntProcedure);
        }
    }

    /**
     * Return the first element of the receiver for which the predicate evaluates to true when given that element as
     * an argument. The predicate will only be evaluated until such an object is found or until all of the elements
     * of the list have been used as arguments. That is, there may be elements of the receiver that are never used as
     * arguments to the predicate. The elements are traversed in the same order as they would be if the message
     * doit() had been sent to the receiver. The result is undefined if predicate does not evaluate to true for any
     * element.
     */
    public static <T> T detect(ArrayList<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                T item = elements[i];
                if (predicate.accept(item))
                {
                    return item;
                }
            }
            return null;
        }
        return RandomAccessListIterate.detect(list, predicate);
    }

    public static <T, P> T detectWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                T item = elements[i];
                if (predicate.accept(item, parameter))
                {
                    return item;
                }
            }
            return null;
        }
        return RandomAccessListIterate.detectWith(list, predicate, parameter);
    }

    /**
     * Return the first element of the receiver for which the predicate evaluates to true when given that element as
     * an argument. The predicate will only be evaluated until such an object is found or until all of the elements
     * of the list have been used as arguments. That is, there may be elements of the receiver that are never used as
     * arguments to the predicate. The elements are traversed in the same order as they would be if the message #do:
     * had been sent to the receiver. If no element causes predicate to evaluate to true, answer the result of
     * exceptionHandler value.
     */
    public static <T> T detectIfNone(ArrayList<T> list, Predicate<? super T> predicate, T ifNone)
    {
        T result = ArrayListIterate.detect(list, predicate);
        return result == null ? ifNone : result;
    }

    public static <T, IV> T detectWithIfNone(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue,
            T ifNone)
    {
        T result = ArrayListIterate.detectWith(list, predicate, injectedValue);
        return result == null ? ifNone : result;
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
            ArrayList<T> list,
            Function2<? super IV, ? super T, ? extends IV> function)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            IV result = injectValue;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                result = function.value(result, elements[i]);
            }
            return result;
        }
        return RandomAccessListIterate.injectInto(injectValue, list, function);
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
            ArrayList<T> list,
            IntObjectToIntFunction<? super T> function)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            int result = injectValue;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                result = function.intValueOf(result, elements[i]);
            }
            return result;
        }
        return RandomAccessListIterate.injectInto(injectValue, list, function);
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
            ArrayList<T> list,
            LongObjectToLongFunction<? super T> function)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            long result = injectValue;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                result = function.longValueOf(result, elements[i]);
            }
            return result;
        }
        return RandomAccessListIterate.injectInto(injectValue, list, function);
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
            ArrayList<T> list,
            DoubleObjectToDoubleFunction<? super T> function)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            double result = injectValue;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                result = function.doubleValueOf(result, elements[i]);
            }
            return result;
        }
        return RandomAccessListIterate.injectInto(injectValue, list, function);
    }

    /**
     * Return true if the predicate evaluates to true for any element of the receiver. Otherwise return false.
     * Return false if the receiver is empty.
     */
    public static <T> boolean anySatisfy(ArrayList<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i]))
                {
                    return true;
                }
            }
            return false;
        }
        return RandomAccessListIterate.anySatisfy(list, predicate);
    }

    /**
     * Return true if the predicate evaluates to true for any element of the receiver. Otherwise return false.
     * Return false if the receiver is empty.
     */
    public static <T, IV> boolean anySatisfyWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i], injectedValue))
                {
                    return true;
                }
            }
            return false;
        }
        return RandomAccessListIterate.anySatisfyWith(list, predicate, injectedValue);
    }

    /**
     * Return true if the predicate evaluates to true for every element of the receiver. Otherwise return false.
     */
    public static <T> boolean allSatisfy(ArrayList<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i]))
                {
                    return false;
                }
            }
            return true;
        }
        return RandomAccessListIterate.allSatisfy(list, predicate);
    }

    /**
     * Return true if the predicate evaluates to true for every element of the receiver. Otherwise return false.
     */
    public static <T, IV> boolean allSatisfyWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i], injectedValue))
                {
                    return false;
                }
            }
            return true;
        }
        return RandomAccessListIterate.allSatisfyWith(list, predicate, injectedValue);
    }

    public static <T, IV> Twin<MutableList<T>> selectAndRejectWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            MutableList<T> positiveResult = Lists.mutable.of();
            MutableList<T> negativeResult = Lists.mutable.of();
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                (predicate.accept(elements[i], injectedValue) ? positiveResult : negativeResult).add(elements[i]);
            }
            return Tuples.twin(positiveResult, negativeResult);
        }
        return RandomAccessListIterate.selectAndRejectWith(list, predicate, injectedValue);
    }

    public static <T> PartitionMutableList<T> partition(
            ArrayList<T> list,
            Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            PartitionFastList<T> partitionFastList = new PartitionFastList<T>(predicate);
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                partitionFastList.add(elements[i]);
            }
            return partitionFastList;
        }
        return RandomAccessListIterate.partition(list, predicate);
    }

    /**
     * Searches for the first occurence where the predicate evaluates to true.
     */
    public static <T> int detectIndex(ArrayList<T> list, Predicate<? super T> predicate)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i]))
                {
                    return i;
                }
            }
            return -1;
        }
        return RandomAccessListIterate.detectIndex(list, predicate);
    }

    /**
     * Searches for the first occurrence where the predicate evaluates to true.
     */
    public static <T, IV> int detectIndexWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super IV> predicate,
            IV injectedValue)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (predicate.accept(elements[i], injectedValue))
                {
                    return i;
                }
            }
            return -1;
        }
        return RandomAccessListIterate.detectIndexWith(list, predicate, injectedValue);
    }

    public static <T, IV, P> IV injectIntoWith(
            IV injectedValue,
            ArrayList<T> list,
            Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            P parameter)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            IV result = injectedValue;
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                result = function.value(result, elements[i], parameter);
            }
            return result;
        }
        return RandomAccessListIterate.injectIntoWith(injectedValue, list, function, parameter);
    }

    public static <T, P> void forEachWith(
            ArrayList<T> list,
            Procedure2<? super T, ? super P> procedure,
            P parameter)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                procedure.value(elements[i], parameter);
            }
        }
        else
        {
            RandomAccessListIterate.forEachWith(list, procedure, parameter);
        }
    }

    public static <T, P, A> ArrayList<A> collectWith(
            ArrayList<T> list,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter)
    {
        return ArrayListIterate.collectWith(list, function, parameter, new ArrayList<A>(list.size()));
    }

    public static <T, P, A, R extends Collection<A>> R collectWith(
            ArrayList<T> list,
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                targetCollection.add(function.value(elements[i], parameter));
            }
            return targetCollection;
        }
        return RandomAccessListIterate.collectWith(list, function, parameter, targetCollection);
    }

    public static <T> ArrayList<T> removeIf(ArrayList<T> list, Predicate<? super T> predicate)
    {
        if (list.getClass() == ArrayList.class && ArrayListIterate.SIZE_FIELD != null)
        {
            int currentFilledIndex = 0;
            int size = list.size();
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i]))
                {
                    // keep it
                    if (currentFilledIndex != i)
                    {
                        elements[currentFilledIndex] = elements[i];
                    }
                    currentFilledIndex++;
                }
            }
            wipeAndResetTheEnd(currentFilledIndex, size, elements, list);
        }
        else
        {
            RandomAccessListIterate.removeIf(list, predicate);
        }
        return list;
    }

    public static <T, P> ArrayList<T> removeIfWith(
            ArrayList<T> list,
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        if (list.getClass() == ArrayList.class && ArrayListIterate.SIZE_FIELD != null)
        {
            int currentFilledIndex = 0;
            int size = list.size();
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                if (!predicate.accept(elements[i], parameter))
                {
                    // keep it
                    if (currentFilledIndex != i)
                    {
                        elements[currentFilledIndex] = elements[i];
                    }
                    currentFilledIndex++;
                }
            }
            wipeAndResetTheEnd(currentFilledIndex, size, elements, list);
        }
        else
        {
            RandomAccessListIterate.removeIfWith(list, predicate, parameter);
        }
        return list;
    }

    private static <T> void wipeAndResetTheEnd(
            int newCurrentFilledIndex,
            int newSize,
            T[] newElements,
            ArrayList<T> list)
    {
        for (int i = newCurrentFilledIndex; i < newSize; i++)
        {
            newElements[i] = null;
        }
        try
        {
            ArrayListIterate.SIZE_FIELD.setInt(list, newCurrentFilledIndex);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(
                    "Something really bad happened on the way to pounding size into the ArrayList reflectively",
                    e);
        }
    }

    /**
     * Mutates the internal array of the ArrayList by sorting it and then returns the same ArrayList.
     */
    public static <T extends Comparable<? super T>> ArrayList<T> sortThis(ArrayList<T> list)
    {
        return ArrayListIterate.sortThis(list, Comparators.naturalOrder());
    }

    /**
     * Mutates the internal array of the ArrayList by sorting it and then returns the same ArrayList.
     */
    public static <T> ArrayList<T> sortThis(ArrayList<T> list, Comparator<? super T> comparator)
    {
        int size = list.size();
        if (size > 1)
        {
            if (ArrayListIterate.canAccessInternalArray(list))
            {
                ArrayIterate.sort(ArrayListIterate.getInternalArray(list), size, comparator);
            }
            else
            {
                Collections.sort(list, comparator);
            }
        }
        return list;
    }

    public static <T> void toArray(ArrayList<T> list, T[] target, int startIndex, int sourceSize)
    {
        if (ArrayListIterate.canAccessInternalArray(list))
        {
            System.arraycopy(ArrayListIterate.getInternalArray(list), 0, target, startIndex, sourceSize);
        }
        else
        {
            RandomAccessListIterate.toArray(list, target, startIndex, sourceSize);
        }
    }

    /**
     * @see Iterate#take(Iterable, int)
     */
    public static <T> ArrayList<T> take(ArrayList<T> list, int count)
    {
        return ArrayListIterate.take(list, count, new ArrayList<T>(count));
    }

    /**
     * @see Iterate#take(Iterable, int)
     */
    public static <T, R extends Collection<T>> R take(ArrayList<T> list, int count, R targetList)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);

            int end = Math.min(size, count);
            for (int i = 0; i < end; i++)
            {
                targetList.add(elements[i]);
            }
            return targetList;
        }
        return RandomAccessListIterate.take(list, count, targetList);
    }

    /**
     * @see Iterate#drop(Iterable, int)
     */
    public static <T> ArrayList<T> drop(ArrayList<T> list, int count)
    {
        return ArrayListIterate.drop(list, count, new ArrayList<T>(list.size() - Math.min(list.size(), count)));
    }

    /**
     * @see Iterate#drop(Iterable, int)
     */
    public static <T, R extends Collection<T>> R drop(ArrayList<T> list, int count, R targetList)
    {
        return RandomAccessListIterate.drop(list, count, targetList);
    }

    /**
     * @see Iterate#groupBy(Iterable, Function)
     */
    public static <T, V> FastListMultimap<V, T> groupBy(
            ArrayList<T> list,
            Function<? super T, ? extends V> function)
    {
        return ArrayListIterate.groupBy(list, function, FastListMultimap.<V, T>newMultimap());
    }

    /**
     * @see Iterate#groupBy(Iterable, Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupBy(
            ArrayList<T> list,
            Function<? super T, ? extends V> function,
            R target)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                target.put(function.valueOf(elements[i]), elements[i]);
            }
            return target;
        }
        return RandomAccessListIterate.groupBy(list, function, target);
    }

    /**
     * @see Iterate#groupByEach(Iterable, Function)
     */
    public static <T, V> FastListMultimap<V, T> groupByEach(
            ArrayList<T> list,
            Function<? super T, ? extends Iterable<V>> function)
    {
        return ArrayListIterate.groupByEach(list, function, FastListMultimap.<V, T>newMultimap());
    }

    /**
     * @see Iterate#groupByEach(Iterable, Function, MutableMultimap)
     */
    public static <T, V, R extends MutableMultimap<V, T>> R groupByEach(
            ArrayList<T> list,
            Function<? super T, ? extends Iterable<V>> function,
            R target)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                Iterable<V> iterable = function.valueOf(elements[i]);
                for (V key : iterable)
                {
                    target.put(key, elements[i]);
                }
            }
            return target;
        }
        return RandomAccessListIterate.groupByEach(list, function, target);
    }

    public static <X, Y> MutableList<Pair<X, Y>> zip(ArrayList<X> xs, Iterable<Y> ys)
    {
        return ArrayListIterate.zip(xs, ys, FastList.<Pair<X, Y>>newList());
    }

    public static <X, Y, R extends Collection<Pair<X, Y>>> R zip(ArrayList<X> xs, Iterable<Y> ys, R targetCollection)
    {
        int size = xs.size();
        if (ArrayListIterate.isOptimizableArrayList(xs, size))
        {
            Iterator<Y> yIterator = ys.iterator();
            X[] elements = ArrayListIterate.getInternalArray(xs);
            for (int i = 0; i < size && yIterator.hasNext(); i++)
            {
                targetCollection.add(Tuples.pair(elements[i], yIterator.next()));
            }
            return targetCollection;
        }
        return RandomAccessListIterate.zip(xs, ys, targetCollection);
    }

    public static <T> MutableList<Pair<T, Integer>> zipWithIndex(ArrayList<T> list)
    {
        return ArrayListIterate.zipWithIndex(list, FastList.<Pair<T, Integer>>newList());
    }

    public static <T, R extends Collection<Pair<T, Integer>>> R zipWithIndex(ArrayList<T> list, R targetCollection)
    {
        int size = list.size();
        if (ArrayListIterate.isOptimizableArrayList(list, size))
        {
            T[] elements = ArrayListIterate.getInternalArray(list);
            for (int i = 0; i < size; i++)
            {
                targetCollection.add(Tuples.pair(elements[i], i));
            }
            return targetCollection;
        }
        return RandomAccessListIterate.zipWithIndex(list, targetCollection);
    }

    private static boolean canAccessInternalArray(ArrayList<?> list)
    {
        return ArrayListIterate.ELEMENT_DATA_FIELD != null && list.getClass() == ArrayList.class;
    }

    private static boolean isOptimizableArrayList(ArrayList<?> list, int newSize)
    {
        return newSize > MIN_DIRECT_ARRAY_ACCESS_SIZE
                && ArrayListIterate.ELEMENT_DATA_FIELD != null
                && list.getClass() == ArrayList.class;
    }

    private static <T> T[] getInternalArray(ArrayList<T> list)
    {
        try
        {
            return (T[]) ArrayListIterate.ELEMENT_DATA_FIELD.get(list);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
