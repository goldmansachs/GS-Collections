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

package com.webguys.ponzu.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.impl.block.factory.Comparators;
import com.webguys.ponzu.impl.block.factory.Functions;
import com.webguys.ponzu.impl.block.factory.Generators;
import com.webguys.ponzu.impl.block.factory.Predicates;
import com.webguys.ponzu.impl.block.factory.Procedures;
import com.webguys.ponzu.impl.block.function.primitive.IntegerFunctionImpl;
import com.webguys.ponzu.impl.block.procedure.CollectionAddProcedure;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class tests various algorithms for calculating anagrams from a list of words.
 */
public class AnagramTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AnagramTest.class);

    private static final Procedure<String> LOGGING_PROCEDURE = new Procedure<String>()
    {
        public void value(String each)
        {
            LOGGER.info(each);
        }
    };

    private static final int SIZE_THRESHOLD = 10;

    private static final Function<RichIterable<String>, String> ITERABLE_TO_FORMATTED_STRING =
            new ListToStringFunction<String>();

    private static final Function<RichIterable<String>, Integer> ITERABLE_SIZE_FUNCTION = new ListSizeFunction<String>();

    private static final Comparator<RichIterable<String>> ASCENDING_ITERABLE_SIZE = Comparators.byFunction(ITERABLE_SIZE_FUNCTION);
    private static final Comparator<RichIterable<String>> DESCENDING_ITERABLE_SIZE = Collections.reverseOrder(ASCENDING_ITERABLE_SIZE);

    private static final Predicate<RichIterable<String>> ITERABLE_SIZE_AT_THRESHOLD =
            Predicates.attributeGreaterThanOrEqualTo(ITERABLE_SIZE_FUNCTION, SIZE_THRESHOLD);

    private static final Procedure<RichIterable<String>> OUTPUT_FORMATTED_ITERABLE =
            Functions.bind(LOGGING_PROCEDURE, ITERABLE_TO_FORMATTED_STRING);

    private static final Function<String, Alphagram> ALPHAGRAM_FUNCTION = new AlphagramFunction();

    private MutableList<String> getWords()
    {
        return FastList.newListWith(
                "alerts", "alters", "artels", "estral", "laster", "ratels", "salter", "slater", "staler", "stelar", "talers",
                "least", "setal", "slate", "stale", "steal", "stela", "taels", "tales", "teals", "tesla"
        );
    }

    @Test
    public void anagramsWithMultimapInlined()
    {
        MutableList<RichIterable<String>> results = this.getWords().groupBy(ALPHAGRAM_FUNCTION)
                .multiValuesView()
                .filter(ITERABLE_SIZE_AT_THRESHOLD)
                .toSortedList(DESCENDING_ITERABLE_SIZE);
        results.asLazy()
                .transform(ITERABLE_TO_FORMATTED_STRING)
                .forEach(LOGGING_PROCEDURE);
        Verify.assertIterableSize(SIZE_THRESHOLD, results.getLast());
    }

    @Test
    public void anagramsWithMultimapGSCollections1()
    {
        MutableList<RichIterable<String>> results = this.getWords().groupBy(ALPHAGRAM_FUNCTION)
                .multiValuesView()
                .filter(ITERABLE_SIZE_AT_THRESHOLD)
                .toSortedList(DESCENDING_ITERABLE_SIZE);
        results.transform(ITERABLE_TO_FORMATTED_STRING)
                .forEach(LOGGING_PROCEDURE);
        Verify.assertIterableSize(SIZE_THRESHOLD, results.getLast());
    }

    private boolean listContainsTestGroupAtElementsOneOrTwo(MutableList<MutableList<String>> list)
    {
        return list.get(1).containsAll(this.getTestAnagramGroup())
                || list.get(2).containsAll(this.getTestAnagramGroup());
    }

    @Test
    public void anagramsWithMultiMapGSCollections3()
    {
        MutableList<RichIterable<String>> results = this.getWords().groupBy(ALPHAGRAM_FUNCTION)
                .multiValuesView()
                .toSortedList(DESCENDING_ITERABLE_SIZE);
        results.transformIf(ITERABLE_SIZE_AT_THRESHOLD, ITERABLE_TO_FORMATTED_STRING)
                .forEach(LOGGING_PROCEDURE);
        Verify.assertIterableSize(SIZE_THRESHOLD, results.getLast());
    }

    @Test
    public void anagramsWithMultimapGSCollections4()
    {
        MutableList<RichIterable<String>> results = this.getWords().groupBy(ALPHAGRAM_FUNCTION)
                .multiValuesView()
                .toSortedList(DESCENDING_ITERABLE_SIZE);
        results.forEach(Procedures.ifTrue(ITERABLE_SIZE_AT_THRESHOLD, OUTPUT_FORMATTED_ITERABLE));
        Verify.assertIterableSize(SIZE_THRESHOLD, results.getLast());
    }

    @Test
    public void anagramsWithMultiMapLazyIterable1()
    {
        MutableList<RichIterable<String>> results = this.getWords().groupBy(ALPHAGRAM_FUNCTION)
                .multiValuesView()
                .toSortedList(DESCENDING_ITERABLE_SIZE);
        results.asLazy()
                .transformIf(ITERABLE_SIZE_AT_THRESHOLD, ITERABLE_TO_FORMATTED_STRING)
                .forEach(LOGGING_PROCEDURE);
        Verify.assertIterableSize(SIZE_THRESHOLD, results.getLast());
    }

    @Test
    public void anagramsWithMultimapForEachMultiValue()
    {
        MutableList<RichIterable<String>> results = Lists.mutable.of();
        this.getWords().groupBy(ALPHAGRAM_FUNCTION)
                .multiValuesView().forEach(Procedures.ifTrue(ITERABLE_SIZE_AT_THRESHOLD, CollectionAddProcedure.on(results)));
        results.sortThis(DESCENDING_ITERABLE_SIZE).forEach(OUTPUT_FORMATTED_ITERABLE);
        Verify.assertIterableSize(SIZE_THRESHOLD, results.getLast());
    }

    @Test
    public void anagramsUsingMapGetIfAbsentPutInsteadOfGroupBy()
    {
        final MutableMap<Alphagram, MutableList<String>> map = UnifiedMap.newMap();
        this.getWords().forEach(new Procedure<String>()
        {
            public void value(String word)
            {
                map.getIfAbsentPut(new Alphagram(word), Generators.<String>newFastList()).add(word);
            }
        });
        MutableList<MutableList<String>> results =
                map.filter(ITERABLE_SIZE_AT_THRESHOLD, Lists.mutable.<MutableList<String>>of())
                        .sortThis(DESCENDING_ITERABLE_SIZE);
        results.forEach(OUTPUT_FORMATTED_ITERABLE);
        Assert.assertTrue(this.listContainsTestGroupAtElementsOneOrTwo(results));
        Verify.assertSize(SIZE_THRESHOLD, results.getLast());
    }

    private MutableList<String> getTestAnagramGroup()
    {
        return FastList.newListWith("least", "setal", "slate", "stale", "steal", "stela", "taels", "tales", "teals", "tesla");
    }

    private static class ListToStringFunction<T> implements Function<RichIterable<T>, String>
    {
        private static final long serialVersionUID = 1L;

        public String valueOf(RichIterable<T> list)
        {
            return list.size() + ": " + list;
        }
    }

    private static class ListSizeFunction<T> extends IntegerFunctionImpl<RichIterable<T>>
    {
        private static final long serialVersionUID = 1L;

        public int intValueOf(RichIterable<T> list)
        {
            return list.size();
        }
    }

    private static class AlphagramFunction implements Function<String, Alphagram>
    {
        private static final long serialVersionUID = 1L;

        public Alphagram valueOf(String string)
        {
            return new Alphagram(string);
        }

        @Override
        public String toString()
        {
            return "alphagram";
        }
    }

    private static final class Alphagram
    {
        private final char[] key;

        private Alphagram(String string)
        {
            this.key = string.toCharArray();
            Arrays.sort(this.key);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            Alphagram alphagram = (Alphagram) o;
            return Arrays.equals(this.key, alphagram.key);
        }

        @Override
        public int hashCode()
        {
            return Arrays.hashCode(this.key);
        }

        @Override
        public String toString()
        {
            return new String(this.key);
        }
    }
}
