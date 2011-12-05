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

package com.gs.collections.impl.collection.mutable;

import java.util.Collection;
import java.util.Collections;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.Counter;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.StringPredicates;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for {@link UnmodifiableMutableCollection}.
 */
public class UnmodifiableMutableCollectionTest
{
    private static final String METALLICA = "Metallica";

    private MutableCollection<String> mutableCollection;
    private MutableCollection<String> unmodifiableCollection;

    @Before
    public void setUp()
    {
        this.mutableCollection = FastList.<String>newList().with(METALLICA, "Bon Jovi", "Europe", "Scorpions");
        this.unmodifiableCollection = new UnmodifiableMutableCollection<String>(this.mutableCollection);
    }

    @Test
    public void delegatingMethods()
    {
        Assert.assertEquals(this.mutableCollection.notEmpty(), this.unmodifiableCollection.notEmpty());
        Assert.assertEquals(this.mutableCollection.isEmpty(), this.unmodifiableCollection.isEmpty());
        Assert.assertEquals(this.mutableCollection.size(), this.unmodifiableCollection.size());
        Assert.assertEquals(this.mutableCollection.getFirst(), this.unmodifiableCollection.getFirst());
        Assert.assertEquals(this.mutableCollection.getLast(), this.unmodifiableCollection.getLast());
        Assert.assertEquals(this.mutableCollection.count(Predicates.alwaysTrue()),
                this.unmodifiableCollection.count(Predicates.alwaysTrue()));
        Verify.assertSize(4, this.unmodifiableCollection.select(Predicates.alwaysTrue()));
        Verify.assertSize(4, this.unmodifiableCollection.select(Predicates.alwaysTrue(), FastList.<String>newList()));
        Verify.assertSize(1, this.unmodifiableCollection.selectWith(Predicates2.equal(), METALLICA));
        Verify.assertSize(1,
                this.unmodifiableCollection.selectWith(Predicates2.equal(),
                        METALLICA,
                        FastList.<String>newList()));
        Verify.assertSize(2, this.unmodifiableCollection.reject(StringPredicates.contains("p")));
        Verify.assertSize(2,
                this.unmodifiableCollection.reject(StringPredicates.contains("p"), FastList.<String>newList()));
        Verify.assertSize(3, this.unmodifiableCollection.rejectWith(Predicates2.equal(), METALLICA));
        Verify.assertSize(3,
                this.unmodifiableCollection.rejectWith(Predicates2.equal(),
                        METALLICA,
                        FastList.<String>newList()));
        Verify.assertSize(4, this.unmodifiableCollection.collect(Functions.getStringPassThru()));
        Verify.assertSize(4,
                this.unmodifiableCollection.collect(Functions.getStringPassThru(),
                        FastList.<String>newList()));

        Function<String, Collection<String>> flattenFunction = new Function<String, Collection<String>>()
        {
            public Collection<String> valueOf(String object)
            {
                return FastList.newListWith(object, object);
            }
        };
        Verify.assertSize(8, this.unmodifiableCollection.flatCollect(flattenFunction));
        Verify.assertSize(8, this.unmodifiableCollection.flatCollect(flattenFunction, FastList.<String>newList()));

        Verify.assertSize(4, this.unmodifiableCollection.collectIf(Predicates.alwaysTrue(), Functions.getStringPassThru()));
        Verify.assertSize(4,
                this.unmodifiableCollection.collectIf(Predicates.alwaysTrue(),
                        Functions.getStringPassThru(),
                        FastList.<String>newList()));
        Assert.assertEquals(METALLICA, this.unmodifiableCollection.detect(StringPredicates.contains("allic")));
        Assert.assertEquals("Not found", this.unmodifiableCollection.detectIfNone(StringPredicates.contains("donna"),
                new PassThruFunction0<String>("Not found")));
        Assert.assertEquals(METALLICA, this.unmodifiableCollection.detectWith(Predicates2.equal(), METALLICA));
        Assert.assertEquals("Not found", this.unmodifiableCollection.detectWithIfNone(Predicates2.equal(), "Madonna",
                new PassThruFunction0<String>("Not found")));
        Assert.assertEquals(4, this.unmodifiableCollection.count(Predicates.alwaysTrue()));
        Assert.assertEquals(1, this.unmodifiableCollection.countWith(Predicates2.equal(), METALLICA));
        Assert.assertTrue(this.unmodifiableCollection.anySatisfy(StringPredicates.contains("allic")));
        Assert.assertTrue(this.unmodifiableCollection.anySatisfyWith(Predicates2.equal(), METALLICA));
        Assert.assertTrue(this.unmodifiableCollection.allSatisfy(Predicates.notNull()));
        Assert.assertTrue(this.unmodifiableCollection.allSatisfyWith(Predicates2.alwaysTrue(), ""));
        Assert.assertEquals(this.mutableCollection, this.unmodifiableCollection.toList());
        Verify.assertListsEqual(Lists.mutable.of("Bon Jovi", "Europe", METALLICA, "Scorpions"),
                this.unmodifiableCollection.toSortedList());
        Verify.assertListsEqual(Lists.mutable.of("Scorpions", METALLICA, "Europe", "Bon Jovi"),
                this.unmodifiableCollection.toSortedList(Collections.reverseOrder()));
        Verify.assertSize(4, this.unmodifiableCollection.toSet());
        Verify.assertSize(4, this.unmodifiableCollection.toMap(Functions.getStringPassThru(), Functions.getStringPassThru()));
    }

    @Test
    public void nullCollection()
    {
        Verify.assertThrows(NullPointerException.class, new Runnable()
        {
            public void run()
            {
                new UnmodifiableMutableCollection<Object>(null);
            }
        });
    }

    @Test
    public void add()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableCollectionTest.this.unmodifiableCollection.add("Madonna");
            }
        });
    }

    @Test
    public void remove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableCollectionTest.this.unmodifiableCollection.remove(METALLICA);
            }
        });
    }

    @Test
    public void addAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableCollectionTest.this.unmodifiableCollection.addAll(FastList.<String>newList().with("Madonna"));
            }
        });
    }

    @Test
    public void removeAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableCollectionTest.this.unmodifiableCollection.removeAll(FastList.<String>newList().with(
                        METALLICA));
            }
        });
    }

    @Test
    public void retainAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableCollectionTest.this.unmodifiableCollection.retainAll(FastList.<String>newList().with(
                        METALLICA));
            }
        });
    }

    @Test
    public void clear()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableCollectionTest.this.unmodifiableCollection.clear();
            }
        });
    }

    @Test
    public void transparencyOfMutableChanges()
    {
        this.mutableCollection.remove(METALLICA);
        Verify.assertSize(this.mutableCollection.size(), this.unmodifiableCollection);
    }

    @Test
    public void collectWith()
    {
        Function2<String, String, String> function = new Function2<String, String, String>()
        {
            public String value(String band, String parameter)
            {
                return parameter + band.charAt(0);
            }
        };
        Assert.assertEquals(
                FastList.newListWith(">M", ">B", ">E", ">S"),
                this.unmodifiableCollection.collectWith(function, ">"));
        Assert.assertEquals(FastList.newListWith("*M", "*B", "*E", "*S"), this.unmodifiableCollection.collectWith(function, "*", FastList.<String>newList()));
    }

    @Test
    public void injectInto()
    {
        Function2<String, String, String> function = new Function2<String, String, String>()
        {
            public String value(String injectValue, String band)
            {
                return injectValue + band.charAt(0);
            }
        };
        Assert.assertEquals(">MBES", this.unmodifiableCollection.injectInto(">", function));
    }

    @Test
    public void injectIntoWith()
    {
        Function3<String, String, String, String> function =
                new Function3<String, String, String, String>()
                {
                    public String value(String injectValue, String band, String parameter)
                    {
                        return injectValue + band.charAt(0) + parameter;
                    }
                };
        Assert.assertEquals(">M*B*E*S*", this.unmodifiableCollection.injectIntoWith(">", function, "*"));
    }

    @Test
    public void removeIf()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableCollectionTest.this.unmodifiableCollection.removeIf(Predicates.notNull());
            }
        });
    }

    @Test
    public void removeIfWith()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                UnmodifiableMutableCollectionTest.this.unmodifiableCollection.removeIfWith(Predicates2.alwaysTrue(),
                        METALLICA);
            }
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void with()
    {
        this.unmodifiableCollection.with(METALLICA);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void withAll()
    {
        this.unmodifiableCollection.withAll(this.mutableCollection);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void without()
    {
        this.unmodifiableCollection.without(METALLICA);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void withoutAll()
    {
        this.unmodifiableCollection.withoutAll(this.mutableCollection);
    }

    @Test
    public void iterator()
    {
        Counter counter = new Counter();
        for (Object each : this.unmodifiableCollection)
        {
            counter.increment();
        }
        Assert.assertEquals(4, counter.getCount());
    }

    @Test
    public void forEach()
    {
        final Counter counter = new Counter();
        this.unmodifiableCollection.forEach(new Procedure<String>()
        {
            public void value(String band)
            {
                counter.increment();
            }
        });
        Assert.assertEquals(4, counter.getCount());
    }

    @Test
    public void forEachWith()
    {
        final StringBuilder buf = new StringBuilder();
        this.unmodifiableCollection.forEachWith(new Procedure2<String, String>()
        {
            public void value(String band, String param)
            {
                buf.append(param).append('<').append(band).append('>');
            }
        }, "GreatBand");
        Assert.assertEquals("GreatBand<Metallica>GreatBand<Bon Jovi>GreatBand<Europe>GreatBand<Scorpions>", buf.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        final Counter counter = new Counter();
        this.unmodifiableCollection.forEachWithIndex(new ObjectIntProcedure<String>()
        {
            public void value(String band, int index)
            {
                counter.add(index);
            }
        });
        Assert.assertEquals(6, counter.getCount());
    }

    @Test
    public void selectAndRejectWith()
    {
        Twin<MutableList<String>> twin =
                this.unmodifiableCollection.selectAndRejectWith(Predicates2.equal(), METALLICA);
        Verify.assertSize(1, twin.getOne());
        Verify.assertSize(3, twin.getTwo());
    }

    @Test
    public void groupBy()
    {
        RichIterable<Integer> list = this.newWith(1, 2, 3, 4, 5, 6, 7);
        Function<Integer, Boolean> isOddFunction = new Function<Integer, Boolean>()
        {
            public Boolean valueOf(Integer object)
            {
                return IntegerPredicates.isOdd().accept(object);
            }
        };

        MutableMap<Boolean, RichIterable<Integer>> expected =
                UnifiedMap.<Boolean, RichIterable<Integer>>newWithKeysValues(
                        Boolean.TRUE, FastList.newListWith(1, 3, 5, 7),
                        Boolean.FALSE, FastList.newListWith(2, 4, 6));

        Multimap<Boolean, Integer> multimap = list.groupBy(isOddFunction);
        Assert.assertEquals(expected, multimap.toMap());

        Multimap<Boolean, Integer> multimap2 = list.groupBy(isOddFunction, FastListMultimap.<Boolean, Integer>newMultimap());
        Assert.assertEquals(expected, multimap2.toMap());
    }

    private <T> UnmodifiableMutableCollection<T> newWith(T... elements)
    {
        return new UnmodifiableMutableCollection<T>(FastList.newListWith(elements));
    }

    @Test
    public void toSortedSet()
    {
        this.unmodifiableCollection = this.newWith("2", "4", "1", "3");
        MutableSortedSet<String> set = this.unmodifiableCollection.toSortedSet();
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith("1", "2", "3", "4"), set);
    }

    @Test
    public void toSortedSet_with_comparator()
    {
        this.unmodifiableCollection = this.newWith("2", "4", "4", "2", "1", "4", "1", "3");
        MutableSortedSet<String> set = this.unmodifiableCollection.toSortedSet(Collections.<String>reverseOrder());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Collections.<String>reverseOrder(), "1", "2", "3", "4"), set);
    }

    @Test
    public void toSortedSetBy()
    {
        this.unmodifiableCollection = this.newWith("2", "4", "1", "3");
        MutableSortedSet<String> set = this.unmodifiableCollection.toSortedSetBy(Functions.getStringToInteger());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith("1", "2", "3", "4"), set);
    }
}
