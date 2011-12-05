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

package com.gs.collections.impl.block.factory;

import java.util.Map;

import com.gs.collections.api.block.SerializableComparator;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.comparator.primitive.DoubleFunctionComparator;
import com.gs.collections.impl.block.comparator.primitive.IntFunctionComparator;
import com.gs.collections.impl.block.comparator.primitive.LongFunctionComparator;
import com.gs.collections.impl.block.function.CaseFunction;
import com.gs.collections.impl.block.function.IfFunction;
import com.gs.collections.impl.block.function.primitive.IntegerFunctionImpl;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.StringIterate;

public final class Functions
{
    private static final Function<Double, Double> DOUBLE_PASS_THRU_FUNCTION = new DoublePassThruFunction();
    private static final Function<Integer, Integer> INTEGER_PASS_THRU_FUNCTION = new IntegerPassThruFunction();
    private static final Function<Long, Long> LONG_PASS_THRU_FUNCTION = new LongPassThruFunction();

    private static final Function<?, ?> PASS_THRU_FUNCTION = new PassThruFunction();
    private static final Function<String, String> STRING_TRIM_FUNCTION = new StringTrimFunction();
    private static final Function<Object, Class<?>> CLASS_FUNCTION = new ClassFunction();
    private static final Function<Number, Double> MATH_SIN_FUNCTION = new MathSinFunction();
    private static final Function<Integer, Integer> SQUARED_INTEGER = new SquaredIntegerFunction();
    private static final Function<Object, String> TO_STRING_FUNCTION = new ToStringFunction();
    private static final Function<String, Integer> STRING_TO_INTEGER_FUNCTION = new StringToIntegerFunction();
    private static final Function<?, ?> MAP_KEY_FUNCTION = new MapKeyFunction();
    private static final Function<?, ?> MAP_VALUE_FUNCTION = new MapValueFunction();
    private static final Function<Iterable<?>, Integer> SIZE_FUNCTION = new SizeFunction();
    private static final FirstOfPairFunction<?> FIRST_OF_PAIR_FUNCTION = new FirstOfPairFunction();
    private static final SecondOfPairFunction<?> SECOND_OF_PAIR_FUNCTION = new SecondOfPairFunction();

    private Functions()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    private static class PassThruFunction<T> implements Function<T, T>
    {
        private static final long serialVersionUID = 1L;

        public T valueOf(T anObject)
        {
            return anObject;
        }
    }

    private static class StringTrimFunction implements Function<String, String>
    {
        private static final long serialVersionUID = 1L;

        public String valueOf(String s)
        {
            return s.trim();
        }
    }

    private static final class FixedValueFunction<T, V> implements Function<T, V>
    {
        private static final long serialVersionUID = 1L;
        private final V value;

        private FixedValueFunction(V value)
        {
            this.value = value;
        }

        public V valueOf(T object)
        {
            return this.value;
        }
    }

    private static final class ClassFunction implements Function<Object, Class<?>>
    {
        private static final long serialVersionUID = 1L;

        public Class<?> valueOf(Object anObject)
        {
            return anObject.getClass();
        }

        @Override
        public String toString()
        {
            return "object.getClass()";
        }
    }

    private static final class MathSinFunction implements Function<Number, Double>
    {
        private static final long serialVersionUID = 1L;

        public Double valueOf(Number number)
        {
            return Math.sin(number.doubleValue());
        }

        @Override
        public String toString()
        {
            return "Math.sin()";
        }
    }

    private static final class SquaredIntegerFunction implements Function<Integer, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer valueOf(Integer value)
        {
            return value * value;
        }
    }

    private static final class ToStringFunction implements Function<Object, String>
    {
        private static final long serialVersionUID = 1L;

        public String valueOf(Object anObject)
        {
            return String.valueOf(anObject);
        }

        @Override
        public String toString()
        {
            return "toString";
        }
    }

    private static final class StringToIntegerFunction implements Function<String, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer valueOf(String string)
        {
            return Integer.valueOf(string);
        }

        @Override
        public String toString()
        {
            return "stringToInteger";
        }
    }

    public static <T> Function<T, T> getPassThru()
    {
        return (Function<T, T>) PASS_THRU_FUNCTION;
    }

    public static <T, V> Function<T, V> getFixedValue(V value)
    {
        return new FixedValueFunction<T, V>(value);
    }

    public static Function<Object, Class<?>> getToClass()
    {
        return CLASS_FUNCTION;
    }

    public static Function<Number, Double> getMathSinFunction()
    {
        return MATH_SIN_FUNCTION;
    }

    public static Function<Number, Number> getNumberPassThru()
    {
        return (Function<Number, Number>) PASS_THRU_FUNCTION;
    }

    public static Function<Integer, Integer> getIntegerPassThru()
    {
        return INTEGER_PASS_THRU_FUNCTION;
    }

    public static Function<Long, Long> getLongPassThru()
    {
        return LONG_PASS_THRU_FUNCTION;
    }

    public static Function<Double, Double> getDoublePassThru()
    {
        return DOUBLE_PASS_THRU_FUNCTION;
    }

    public static Function<String, String> getStringPassThru()
    {
        return (Function<String, String>) PASS_THRU_FUNCTION;
    }

    public static Function<String, String> getStringTrim()
    {
        return STRING_TRIM_FUNCTION;
    }

    public static Function<Object, String> getToString()
    {
        return TO_STRING_FUNCTION;
    }

    public static <T> SerializableComparator<T> toIntComparator(IntFunction<T> function)
    {
        return new IntFunctionComparator<T>(function);
    }

    public static <T> SerializableComparator<T> toDoubleComparator(DoubleFunction<T> function)
    {
        return new DoubleFunctionComparator<T>(function);
    }

    public static <T> SerializableComparator<T> toLongComparator(LongFunction<T> function)
    {
        return new LongFunctionComparator<T>(function);
    }

    public static Function<String, Integer> getStringToInteger()
    {
        return STRING_TO_INTEGER_FUNCTION;
    }

    public static <T, V> Function<T, V> withDefault(Function<T, V> function, V defaultValue)
    {
        return new DefaultFunction<T, V>(function, defaultValue);
    }

    public static <V1> Function<Pair<V1, ?>, V1> firstOfPair()
    {
        return (Function<Pair<V1, ?>, V1>) FIRST_OF_PAIR_FUNCTION;
    }

    public static <V2> Function<Pair<?, V2>, V2> secondOfPair()
    {
        return (Function<Pair<?, V2>, V2>) SECOND_OF_PAIR_FUNCTION;
    }

    /**
     * Bind the input of a Procedure to the result of an function, returning a new Procedure.
     *
     * @param delegate The Procedure to delegate the invocation to.
     * @param function The Function that will create the input for the delegate
     * @return A new Procedure
     */
    public static <T1, T2> Procedure<T1> bind(
            final Procedure<? super T2> delegate,
            final Function<? super T1, T2> function)
    {
        return new Procedure<T1>()
        {
            private static final long serialVersionUID = 1L;

            public void value(T1 each)
            {
                delegate.value(function.valueOf(each));
            }
        };
    }

    /**
     * Bind the input of a ProcedureWithInt to the result of an function, returning a new ProcedureWithInt.
     *
     * @param delegate The ProcedureWithInt to delegate the invocation to.
     * @param function The Function that will create the input for the delegate
     * @return A new ProcedureWithInt
     */
    public static <T1, T2> ObjectIntProcedure<T1> bind(
            final ObjectIntProcedure<? super T2> delegate,
            final Function<? super T1, T2> function)
    {
        return new ObjectIntProcedure<T1>()
        {
            private static final long serialVersionUID = 1L;

            public void value(T1 each, int index)
            {
                delegate.value(function.valueOf(each), index);
            }
        };
    }

    /**
     * Bind the input of the first argument of a Procedure2 to the result of an function, returning a new Procedure2.
     *
     * @param delegate The Procedure2 to delegate the invocation to.
     * @param function The Function that will create the input for the delegate
     * @return A new Procedure2
     */
    public static <T1, T2, T3> Procedure2<T1, T3> bind(
            final Procedure2<? super T2, T3> delegate, final Function<? super T1, T2> function)
    {
        return new Procedure2<T1, T3>()
        {
            private static final long serialVersionUID = 1L;

            public void value(T1 each, T3 constant)
            {
                delegate.value(function.valueOf(each), constant);
            }
        };
    }

    public static Function<Integer, Integer> squaredInteger()
    {
        return SQUARED_INTEGER;
    }

    public static <T, V> Function<T, V> firstNotNullValue(Function<T, V>... functions)
    {
        return new FirstNotNullFunction<T, V>(functions);
    }

    public static <T> Function<T, String> firstNotEmptyStringValue(
            Function<T, String>... functions)
    {
        return new FirstNotEmptyStringFunction<T>(functions);
    }

    public static <T1, T2, I extends Iterable<T2>> Function<T1, I> firstNotEmptyCollectionValue(
            Function<T1, I>... functions)
    {
        return new FirstNotEmptyCollectionFunction<T1, T2, I>(functions);
    }

    public static <T, V> Function<T, V> ifTrue(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return new IfFunction<T, V>(predicate, function);
    }

    public static <T, V> Function<T, V> ifElse(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> trueFunction,
            Function<? super T, ? extends V> falseFunction)
    {
        return new IfFunction<T, V>(predicate, trueFunction, falseFunction);
    }

    public static <T extends Comparable<? super T>, V> CaseFunction<T, V> caseDefault(
            Function<? super T, ? extends V> defaultFunction)
    {
        return new CaseFunction<T, V>(defaultFunction);
    }

    public static <T extends Comparable<? super T>, V> CaseFunction<T, V> caseDefault(
            Function<? super T, ? extends V> defaultFunction,
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return Functions.<T, V>caseDefault(defaultFunction).addCase(predicate, function);
    }

    public static <T, V> Function<T, V> synchronizedEach(Function<T, V> function)
    {
        return new SynchronizedFunction<T, V>(function);
    }

    private static final class FirstNotNullFunction<T, V> implements Function<T, V>
    {
        private static final long serialVersionUID = 1L;

        private final Function<T, V>[] functions;

        private FirstNotNullFunction(Function<T, V>... functions)
        {
            this.functions = functions;
        }

        public V valueOf(T object)
        {
            for (Function<T, V> function : this.functions)
            {
                V result = function.valueOf(object);
                if (result != null)
                {
                    return result;
                }
            }
            return null;
        }
    }

    private static final class FirstNotEmptyStringFunction<T> implements Function<T, String>
    {
        private static final long serialVersionUID = 1L;

        private final Function<T, String>[] functions;

        private FirstNotEmptyStringFunction(Function<T, String>... functions)
        {
            this.functions = functions;
        }

        public String valueOf(T object)
        {
            for (Function<T, String> function : this.functions)
            {
                String result = function.valueOf(object);
                if (StringIterate.notEmpty(result))
                {
                    return result;
                }
            }
            return null;
        }
    }

    private static final class FirstNotEmptyCollectionFunction<T1, T2, I extends Iterable<T2>> implements Function<T1, I>
    {
        private static final long serialVersionUID = 1L;

        private final Function<T1, I>[] functions;

        private FirstNotEmptyCollectionFunction(Function<T1, I>[] functions)
        {
            this.functions = functions;
        }

        public I valueOf(T1 object)
        {
            for (Function<T1, I> function : this.functions)
            {
                I result = function.valueOf(object);
                if (Iterate.notEmpty(result))
                {
                    return result;
                }
            }
            return null;
        }
    }

    private static final class SynchronizedFunction<T, V> implements Function<T, V>
    {
        private static final long serialVersionUID = 1L;

        private final Function<T, V> function;

        private SynchronizedFunction(Function<T, V> function)
        {
            this.function = function;
        }

        public V valueOf(T each)
        {
            synchronized (each)
            {
                return this.function.valueOf(each);
            }
        }
    }

    public static <T1, T2, T3> FunctionChain<T1, T2, T3> chain(Function<T1, T2> function1, Function<? super T2, T3> function2)
    {
        return new FunctionChain<T1, T2, T3>(function1, function2);
    }

    private static class DoublePassThruFunction implements Function<Double, Double>, DoubleFunction<Double>
    {
        private static final long serialVersionUID = 1L;

        public double doubleValueOf(Double each)
        {
            return each.doubleValue();
        }

        public Double valueOf(Double each)
        {
            return each;
        }

        @Override
        public String toString()
        {
            return DoublePassThruFunction.class.getSimpleName();
        }
    }

    private static class IntegerPassThruFunction implements Function<Integer, Integer>, IntFunction<Integer>
    {
        private static final long serialVersionUID = 1L;

        public int intValueOf(Integer each)
        {
            return each.intValue();
        }

        public Integer valueOf(Integer each)
        {
            return each;
        }

        @Override
        public String toString()
        {
            return IntegerPassThruFunction.class.getSimpleName();
        }
    }

    private static class LongPassThruFunction implements Function<Long, Long>, LongFunction<Long>
    {
        private static final long serialVersionUID = 1L;

        public long longValueOf(Long each)
        {
            return each.longValue();
        }

        public Long valueOf(Long each)
        {
            return each;
        }

        @Override
        public String toString()
        {
            return LongPassThruFunction.class.getSimpleName();
        }
    }

    private static final class DefaultFunction<T, V> implements Function<T, V>
    {
        private static final long serialVersionUID = 1L;
        private final Function<T, V> function;
        private final V defaultValue;

        private DefaultFunction(Function<T, V> newFunction, V newDefaultValue)
        {
            this.function = newFunction;
            this.defaultValue = newDefaultValue;
        }

        public V valueOf(T anObject)
        {
            V returnValue = this.function.valueOf(anObject);
            if (returnValue == null)
            {
                return this.defaultValue;
            }
            return returnValue;
        }
    }

    public static <T, V1, V2> Function<T, Pair<V1, V2>> pair(
            final Function<T, V1> function1,
            final Function<T, V2> function2)
    {
        return new Function<T, Pair<V1, V2>>()
        {
            public Pair<V1, V2> valueOf(T t)
            {
                return Tuples.pair(function1.valueOf(t), function2.valueOf(t));
            }
        };
    }

    /**
     * @return A function that gets the key out of a {@link Map.Entry}
     */
    public static <K> Function<Map.Entry<K, ?>, K> getKeyFunction()
    {
        return (Function<Map.Entry<K, ?>, K>) MAP_KEY_FUNCTION;
    }

    /**
     * @return A function that gets the value out of a {@link Map.Entry}
     */
    public static <V> Function<Map.Entry<?, V>, V> getValueFunction()
    {
        return (Function<Map.Entry<?, V>, V>) MAP_VALUE_FUNCTION;
    }

    private static class MapKeyFunction<K> implements Function<Map.Entry<K, ?>, K>
    {
        private static final long serialVersionUID = 1L;

        public K valueOf(Map.Entry<K, ?> entry)
        {
            return entry.getKey();
        }
    }

    private static class MapValueFunction<V> implements Function<Map.Entry<?, V>, V>
    {
        private static final long serialVersionUID = 1L;

        public V valueOf(Map.Entry<?, V> entry)
        {
            return entry.getValue();
        }
    }

    /**
     * @return A function that gets the size of an {@code Iterable}
     */
    public static Function<Iterable<?>, Integer> getSizeOf()
    {
        return SIZE_FUNCTION;
    }

    public static class SizeFunction extends IntegerFunctionImpl<Iterable<?>>
    {
        private static final long serialVersionUID = 1L;

        public int intValueOf(Iterable<?> iterable)
        {
            return Iterate.sizeOf(iterable);
        }
    }

    public static final class FunctionChain<T1, T2, T3> implements Function<T1, T3>
    {
        private static final long serialVersionUID = 1L;
        private final Function<T1, T2> function1;
        private final Function<? super T2, T3> function2;

        private FunctionChain(Function<T1, T2> function1, Function<? super T2, T3> function2)
        {
            this.function1 = function1;
            this.function2 = function2;
        }

        public T3 valueOf(T1 object)
        {
            return this.function2.valueOf(this.function1.valueOf(object));
        }

        public <T4> FunctionChain<T1, T3, T4> chain(Function<? super T3, T4> function)
        {
            return new FunctionChain<T1, T3, T4>(this, function);
        }
    }

    private static class FirstOfPairFunction<T> implements Function<Pair<T, ?>, T>
    {
        private static final long serialVersionUID = 1L;

        public T valueOf(Pair<T, ?> pair)
        {
            return pair.getOne();
        }
    }

    private static class SecondOfPairFunction<T> implements Function<Pair<?, T>, T>
    {
        private static final long serialVersionUID = 1L;

        public T valueOf(Pair<?, T> pair)
        {
            return pair.getTwo();
        }
    }
}
