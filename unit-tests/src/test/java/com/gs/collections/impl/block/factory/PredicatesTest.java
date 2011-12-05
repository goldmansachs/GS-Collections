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

import java.util.List;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.ListIterate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PredicatesTest
{
    private static final Function<List<Object>, Boolean> IS_EMPTY = new Function<List<Object>, Boolean>()
    {
        public Boolean valueOf(List<Object> list)
        {
            return list.isEmpty();
        }
    };
    private Employee alice;
    private Employee bob;
    private Employee charlie;
    private Employee diane;

    private MutableList<Employee> employees;

    @Before
    public void setUp()
    {
        this.alice = new Employee(new Address(State.ARIZONA));
        this.alice.addDependent(new Dependent(DependentType.SPOUSE));
        this.alice.addDependent(new Dependent(DependentType.CHILD));
        this.alice.addDependent(new Dependent(DependentType.CHILD));
        this.alice.addDependent(new Dependent(DependentType.PARENT));

        this.bob = new Employee(new Address(State.ALASKA));
        this.bob.addDependent(new Dependent(DependentType.SPOUSE));
        this.bob.addDependent(new Dependent(DependentType.CHILD));

        this.charlie = new Employee(new Address(State.ARIZONA));
        this.charlie.addDependent(new Dependent(DependentType.SPOUSE));
        this.charlie.addDependent(new Dependent(DependentType.CHILD));
        this.charlie.addDependent(new Dependent(DependentType.CHILD));

        this.diane = new Employee(new Address(State.ALASKA));
        this.diane.addDependent(new Dependent(DependentType.SPOUSE));
        this.diane.addDependent(new Dependent(DependentType.PARENT));
        this.diane.addDependent(new Dependent(DependentType.PARENT));
        this.diane.addDependent(new Dependent(DependentType.GRANDPARENT));

        this.employees = FastList.newListWith(this.alice, this.bob, this.charlie, this.diane);
    }

    @Test
    public void alwaysTrue()
    {
        assertAccepts(Predicates.alwaysTrue(), (Object) null);
        assertToString(Predicates.alwaysTrue());
    }

    @Test
    public void alwaysFalse()
    {
        assertRejects(Predicates.alwaysFalse(), (Object) null);
        assertToString(Predicates.alwaysFalse());
    }

    @Test
    public void instanceNot()
    {
        assertRejects(Predicates.alwaysTrue().not(), (Object) null);
        assertToString(Predicates.alwaysTrue().not());
    }

    @Test
    public void synchronizedEach()
    {
        Predicate<Object> predicate = Predicates.synchronizedEach(Predicates.alwaysTrue());
        assertAccepts(predicate, new Object());
        assertToString(predicate);
    }

    @Test
    public void adapt()
    {
        assertAccepts(Predicates.adapt(Predicates.alwaysTrue()), new Object());
        assertToString(Predicates.adapt(Predicates.alwaysTrue()));
    }

    @Test
    public void staticOr()
    {
        assertAccepts(Predicates.or(Predicates.alwaysTrue(), Predicates.alwaysFalse()), new Object());
        assertRejects(Predicates.or(Predicates.alwaysFalse(), Predicates.alwaysFalse()), new Object());
        assertAccepts(Predicates.or(Predicates.alwaysTrue(), Predicates.alwaysTrue()), new Object());

        assertToString(Predicates.or(Predicates.alwaysTrue(), Predicates.alwaysTrue()));
    }

    @Test
    public void instanceOr()
    {
        assertAccepts(Predicates.alwaysTrue().or(Predicates.alwaysFalse()), new Object());
        assertRejects(Predicates.alwaysFalse().or(Predicates.alwaysFalse()), new Object());
        assertAccepts(Predicates.alwaysTrue().or(Predicates.alwaysTrue()), new Object());

        assertToString(Predicates.alwaysTrue().or(Predicates.alwaysTrue()));
    }

    @Test
    public void collectionOr()
    {
        MutableList<Predicate<Object>> predicates =
                Lists.fixedSize.<Predicate<Object>>of(Predicates.alwaysTrue(), Predicates.alwaysFalse(), null);
        assertAccepts(Predicates.or(predicates), new Object());

        MutableList<Predicate<Object>> falsePredicates =
                Lists.fixedSize.<Predicate<Object>>of(Predicates.alwaysFalse(), Predicates.alwaysFalse());
        assertRejects(Predicates.or(falsePredicates), new Object());

        MutableList<Predicate<Object>> truePredicates =
                Lists.fixedSize.<Predicate<Object>>of(Predicates.alwaysTrue(), Predicates.alwaysTrue());
        assertAccepts(Predicates.or(truePredicates), new Object());

        assertToString(Predicates.or(truePredicates));
    }

    @Test
    public void varArgOr()
    {
        assertAccepts(Predicates.or(Predicates.alwaysTrue(), Predicates.alwaysFalse(), null), new Object());
        assertRejects(Predicates.or(Predicates.alwaysFalse(), Predicates.alwaysFalse(), Predicates.alwaysFalse()), new Object());
        assertAccepts(Predicates.or(Predicates.alwaysTrue(), Predicates.alwaysTrue(), Predicates.alwaysTrue()), new Object());

        assertToString(Predicates.or(Predicates.alwaysTrue(), Predicates.alwaysTrue(), Predicates.alwaysTrue()));
    }

    @Test
    public void staticAnd()
    {
        assertAccepts(Predicates.and(Predicates.alwaysTrue(), Predicates.alwaysTrue()), new Object());
        assertRejects(Predicates.and(Predicates.alwaysTrue(), Predicates.alwaysFalse()), new Object());
        assertRejects(Predicates.and(Predicates.alwaysFalse(), Predicates.alwaysFalse()), new Object());

        assertToString(Predicates.and(Predicates.alwaysTrue(), Predicates.alwaysTrue()));
    }

    @Test
    public void instanceAnd()
    {
        assertAccepts(Predicates.alwaysTrue().and(Predicates.alwaysTrue()), new Object());
        assertRejects(Predicates.alwaysTrue().and(Predicates.alwaysFalse()), new Object());
        assertRejects(Predicates.alwaysFalse().and(Predicates.alwaysFalse()), new Object());

        assertToString(Predicates.alwaysTrue().and(Predicates.alwaysTrue()));
    }

    @Test
    public void collectionAnd()
    {
        MutableList<Predicate<Object>> predicates =
                Lists.fixedSize.<Predicate<Object>>of(Predicates.alwaysTrue(), Predicates.alwaysTrue());
        assertAccepts(Predicates.and(predicates), new Object());

        MutableList<Predicate<Object>> tfPredicates =
                Lists.fixedSize.<Predicate<Object>>of(Predicates.alwaysTrue(), Predicates.alwaysFalse());
        assertRejects(Predicates.and(tfPredicates), new Object());

        MutableList<Predicate<Object>> falsePredicates =
                Lists.fixedSize.<Predicate<Object>>of(Predicates.alwaysFalse(), Predicates.alwaysFalse());
        assertRejects(Predicates.and(falsePredicates), new Object());

        assertToString(Predicates.and(predicates));
    }

    @Test
    public void varArgAnd()
    {
        assertAccepts(Predicates.and(Predicates.alwaysTrue(), Predicates.alwaysTrue()), new Object());
        assertRejects(Predicates.and(Predicates.alwaysTrue(), Predicates.alwaysTrue(), Predicates.alwaysFalse()), new Object());
        assertRejects(Predicates.and(Predicates.alwaysFalse(), Predicates.alwaysFalse(), Predicates.alwaysFalse()), new Object());

        assertToString(Predicates.and(Predicates.alwaysTrue(), Predicates.alwaysTrue(), null));
    }

    @Test
    public void equal()
    {
        assertAccepts(Predicates.equal(1), 1);
        assertRejects(Predicates.equal(1), 2);

        assertAccepts(Predicates.equal("test"), "test");
        assertRejects(Predicates.equal("test"), "production");

        assertToString(Predicates.equal(1));
    }

    @Test
    public void notEqual()
    {
        assertRejects(Predicates.notEqual(1), 1);
        assertAccepts(Predicates.notEqual(1), 2);

        assertRejects(Predicates.notEqual("test"), "test");
        assertAccepts(Predicates.notEqual("test"), "production");

        assertToString(Predicates.notEqual(1));
    }

    @Test
    public void not()
    {
        Predicate<Object> notTrue = Predicates.not(Predicates.alwaysTrue());
        assertRejects(notTrue, new Object());
        assertToString(notTrue);

        Predicate<Object> notFalse = Predicates.not(Predicates.alwaysFalse());
        assertAccepts(notFalse, new Object());
        assertToString(notFalse);
    }

    @Test
    public void testNull()
    {
        assertAccepts(Predicates.isNull(), (Object) null);
        assertRejects(Predicates.isNull(), new Object());
        assertToString(Predicates.isNull());
    }

    @Test
    public void notNull()
    {
        assertAccepts(Predicates.notNull(), new Object());
        assertRejects(Predicates.notNull(), (Object) null);
        assertToString(Predicates.notNull());
    }

    @Test
    public void neither()
    {
        assertRejects(Predicates.neither(Predicates.alwaysTrue(), Predicates.alwaysTrue()), new Object());
        assertRejects(Predicates.neither(Predicates.alwaysTrue(), Predicates.alwaysFalse()), new Object());
        assertAccepts(Predicates.neither(Predicates.alwaysFalse(), Predicates.alwaysFalse()), new Object());

        assertToString(Predicates.neither(Predicates.alwaysFalse(), Predicates.alwaysFalse()));
    }

    @Test
    public void collectionNoneOf()
    {
        MutableList<Predicate<Object>> trueNorTrue = Lists.fixedSize.<Predicate<Object>>of(
                Predicates.alwaysTrue(),
                Predicates.alwaysTrue(),
                Predicates.alwaysTrue());
        assertRejects(Predicates.noneOf(trueNorTrue), new Object());

        MutableList<Predicate<Object>> trueNorFalse = Lists.fixedSize.<Predicate<Object>>of(
                Predicates.alwaysTrue(),
                Predicates.alwaysTrue(),
                Predicates.alwaysFalse());
        assertRejects(Predicates.noneOf(trueNorFalse), new Object());

        MutableList<Predicate<Object>> falseNorFalse = Lists.fixedSize.<Predicate<Object>>of(
                Predicates.alwaysFalse(),
                Predicates.alwaysFalse(),
                Predicates.alwaysFalse());
        assertAccepts(Predicates.noneOf(falseNorFalse), new Object());

        assertToString(Predicates.noneOf(falseNorFalse));
    }

    @Test
    public void noneOf()
    {
        assertRejects(Predicates.noneOf(Predicates.alwaysTrue(), Predicates.alwaysTrue(), Predicates.alwaysTrue()), new Object());
        assertRejects(Predicates.noneOf(Predicates.alwaysTrue(), Predicates.alwaysTrue(), Predicates.alwaysFalse()), new Object());
        assertAccepts(Predicates.noneOf(Predicates.alwaysFalse(), Predicates.alwaysFalse(), Predicates.alwaysFalse()), new Object());

        assertToString(Predicates.noneOf(Predicates.alwaysFalse(), Predicates.alwaysFalse(), Predicates.alwaysFalse()));
    }

    @Test
    public void sameAs()
    {
        Object object = new Object();
        Predicate<Object> sameAs = Predicates.sameAs(object);

        assertAccepts(sameAs, object);
        assertRejects(sameAs, new Object());

        assertToString(sameAs);
    }

    @Test
    public void notSameAs()
    {
        Object object = new Object();
        Predicate<Object> notSameAs = Predicates.notSameAs(object);

        assertRejects(notSameAs, object);
        assertAccepts(notSameAs, new Object());

        assertToString(notSameAs);
    }

    @Test
    public void instanceOf()
    {
        Assert.assertTrue(Predicates.instanceOf(Integer.class).accept(1));
        Assert.assertFalse(Predicates.instanceOf(Integer.class).accept(1.0));
        assertToString(Predicates.instanceOf(Integer.class));
    }

    @Test
    public void assignableFrom()
    {
        assertAccepts(Predicates.assignableFrom(Number.class), 1);
        assertAccepts(Predicates.assignableFrom(Integer.class), 1);
        assertRejects(Predicates.assignableFrom(List.class), 1);

        assertToString(Predicates.assignableFrom(Number.class));
    }

    @Test
    public void notInstanceOf()
    {
        Assert.assertFalse(Predicates.notInstanceOf(Integer.class).accept(1));
        Assert.assertTrue(Predicates.notInstanceOf(Integer.class).accept(1.0));

        assertToString(Predicates.notInstanceOf(Integer.class));
    }

    @Test
    public void ifTrue()
    {
        assertIf(Predicates.<List<Object>>ifTrue(IS_EMPTY), true);
    }

    @Test
    public void ifFalse()
    {
        assertIf(Predicates.<List<Object>>ifFalse(IS_EMPTY), false);
    }

    private static void assertIf(Predicate<List<Object>> predicate, boolean bool)
    {
        Assert.assertEquals(bool, predicate.accept(Lists.fixedSize.of()));
        Assert.assertEquals(!bool, predicate.accept(FastList.newListWith((Object) null)));
        assertToString(predicate);
    }

    @Test
    public void ifTrueWithClassAndFunctionName()
    {
        Twin<Boolean> target = Tuples.twin(true, false);
        assertAccepts(Predicates.ifTrue(Functions.<Boolean>firstOfPair()), target);
        assertRejects(Predicates.ifTrue(Functions.<Boolean>secondOfPair()), target);
    }

    @Test
    public void ifFalseWithClassAndFunctionName()
    {
        Twin<Boolean> target = Tuples.twin(true, false);
        assertRejects(Predicates.ifFalse(Functions.<Boolean>firstOfPair()), target);
        assertAccepts(Predicates.ifFalse(Functions.<Boolean>secondOfPair()), target);
    }

    @Test
    public void attributeEqual()
    {
        Predicate<String> predicate = Predicates.attributeEqual(Functions.getToString(), "1");
        assertAccepts(predicate, "1");
        assertRejects(predicate, "0");
        assertToString(predicate);
    }

    @Test
    public void attributeNotEqual()
    {
        Predicate<String> predicate = Predicates.attributeNotEqual(Functions.getToString(), "1");
        assertAccepts(predicate, "0");
        assertRejects(predicate, "1");
        assertToString(predicate);
    }

    @Test
    public void attributeLessThan()
    {
        Predicate<String> predicate = Predicates.attributeLessThan(Functions.getToString(), "1");
        assertRejects(predicate, "1");
        assertAccepts(predicate, "0");
        assertToString(predicate);
    }

    @Test
    public void attributeGreaterThan()
    {
        Predicate<String> predicate = Predicates.attributeGreaterThan(Functions.getToString(), "0");
        assertAccepts(predicate, "1");
        assertRejects(predicate, "0");
        assertToString(predicate);
    }

    @Test
    public void attributeGreaterThanOrEqualTo()
    {
        Predicate<String> predicate = Predicates.attributeGreaterThanOrEqualTo(Functions.getToString(), "1");
        assertAccepts(predicate, "1", "2");
        assertRejects(predicate, "0");
        assertToString(predicate);
    }

    @Test
    public void attributeLessThanOrEqualTo()
    {
        Predicate<String> predicate = Predicates.attributeLessThanOrEqualTo(Functions.getToString(), "1");
        assertAccepts(predicate, "1", "0");
        assertRejects(predicate, "2");
        assertToString(predicate);
    }

    @Test
    public void attributeAnySatisfy()
    {
        Function<Address, String> stateAbbreviation = new Function<Address, String>()
        {
            public String valueOf(Address address)
            {
                return address.getState().getAbbreviation();
            }
        };
        Predicates<Address> inArizona = Predicates.attributeEqual(stateAbbreviation, "AZ");
        MutableCollection<Employee> azResidents = this.employees.select(Predicates.attributeAnySatisfy(Employee.TO_ADDRESSES, inArizona));
        Assert.assertEquals(FastList.newListWith(this.alice, this.charlie), azResidents);

        Predicates<Address> inAlaska = Predicates.attributeEqual(stateAbbreviation, "AK");
        MutableCollection<Employee> akResidents = this.employees.select(Predicates.attributeAnySatisfy(Employee.TO_ADDRESSES, inAlaska));
        Assert.assertEquals(FastList.newListWith(this.bob, this.diane), akResidents);
        assertToString(inArizona);
    }

    @Test
    public void attributeAllSatisfy()
    {
        MutableCollection<Employee> noExtendedDependents = this.employees.select(Predicates.attributeAllSatisfy(Employee.TO_DEPENEDENTS, Dependent.IS_IMMEDIATE));
        Assert.assertEquals(FastList.newListWith(this.bob, this.charlie), noExtendedDependents);
    }

    @Test
    public void allSatisfy()
    {
        Predicate<Iterable<Object>> allIntegers = Predicates.allSatisfy(Predicates.instanceOf(Integer.class));
        assertAccepts(allIntegers, FastList.<Object>newListWith(1, 2, 3));
        assertRejects(allIntegers, FastList.<Object>newListWith(Boolean.TRUE, Boolean.FALSE));

        assertToString(allIntegers);
    }

    @Test
    public void anySatisfy()
    {
        Predicates<Iterable<Object>> anyIntegers = Predicates.anySatisfy(Predicates.instanceOf(Integer.class));
        assertAccepts(anyIntegers, FastList.<Object>newListWith(1, 2, 3));
        assertRejects(anyIntegers, FastList.<Object>newListWith(Boolean.TRUE, Boolean.FALSE));
        assertToString(anyIntegers);
    }

    @Test
    public void noneSatisfy()
    {
        assertAccepts(Predicates.<Integer>noneSatisfy(Predicates.instanceOf(String.class)), Interval.oneTo(5));
    }

    @Test
    public void attributeIsNull()
    {
        assertAccepts(Predicates.attributeIsNull(Functions.getPassThru()), (Object) null);
        assertRejects(Predicates.attributeIsNull(Functions.getPassThru()), new Object());
    }

    @Test
    public void attributeIsNullWithFunctionName()
    {
        Twin<Integer> target = Tuples.twin(null, 1);
        assertAccepts(Predicates.attributeIsNull(Functions.<Integer>firstOfPair()), target);
        assertRejects(Predicates.attributeIsNull(Functions.<Integer>secondOfPair()), target);
    }

    @Test
    public void attributeNotNullWithFunction()
    {
        assertRejects(Predicates.attributeNotNull(Functions.getPassThru()), (Object) null);
        assertAccepts(Predicates.attributeNotNull(Functions.getPassThru()), new Object());
    }

    @Test
    public void in()
    {
        MutableList<String> list1 = Lists.fixedSize.of("1", "3");
        Predicate<Object> inList = Predicates.in(list1);
        assertAccepts(inList, "1");
        assertRejects(inList, "2");
        assertAccepts(Predicates.in(list1.toArray()), "1");

        Assert.assertEquals(FastList.newListWith("1"), ListIterate.select(Lists.fixedSize.of("1", "2"), inList));
        assertToString(inList);
    }

    @Test
    public void inInterval()
    {
        assertAccepts(Predicates.in(Interval.oneTo(3)), 2);
        assertToString(Predicates.in(Interval.oneTo(3)));
    }

    @Test
    public void attributeIn()
    {
        MutableList<String> upperList = Lists.fixedSize.of("A", "B");
        Predicate<String> in = Predicates.attributeIn(StringFunctions.toUpperCase(), upperList);
        assertAccepts(in, "a");
        assertRejects(in, "c");

        Assert.assertEquals(FastList.newListWith("a"), ListIterate.select(Lists.fixedSize.of("a", "c"), in));
        assertToString(in);
    }

    @Test
    public void notIn()
    {
        MutableList<String> odds = Lists.fixedSize.of("1", "3");
        Predicate<Object> predicate = Predicates.notIn(odds);
        assertAccepts(predicate, "2");
        assertRejects(predicate, "1");
        assertRejects(Predicates.notIn(odds.toArray()), "1");

        Assert.assertEquals(FastList.newListWith("2"), ListIterate.select(Lists.fixedSize.of("1", "2"), predicate));
        assertToString(predicate);
    }

    @Test
    public void notInInterval()
    {
        assertAccepts(Predicates.notIn(Interval.oneTo(3)), 4);
        assertToString(Predicates.notIn(Interval.oneTo(3)));
    }

    @Test
    public void attributeNotIn()
    {
        MutableList<String> lowerList = Lists.fixedSize.of("a", "b");
        Predicate<String> out = Predicates.attributeNotIn(StringFunctions.toLowerCase(), lowerList);
        assertAccepts(out, "C");
        assertRejects(out, "A");

        Assert.assertEquals(FastList.newListWith("A"), ListIterate.reject(Lists.fixedSize.of("A", "C"), out));
        assertToString(out);
    }

    @Test
    public void lessThan()
    {
        Predicate<Integer> lessThan = Predicates.lessThan(0);
        assertAccepts(lessThan, -1);
        assertRejects(lessThan, 0, 1);
        assertToString(lessThan);
    }

    @Test
    public void attributeBetweenExclusive()
    {
        Predicate<Pair<Integer, ?>> predicate = Predicates.attributeBetweenExclusive(Functions.<Integer>firstOfPair(), 9, 11);
        assertAccepts(predicate, Tuples.twin(10, 0));
        assertRejects(predicate, Tuples.twin(8, 0), Tuples.twin(9, 0), Tuples.twin(11, 0), Tuples.twin(12, 0));
    }

    @Test
    public void attributeBetweenInclusiveFrom()
    {
        Predicate<Pair<Integer, ?>> predicate = Predicates.attributeBetweenInclusiveFrom(Functions.<Integer>firstOfPair(), 9, 11);
        assertAccepts(predicate, Tuples.twin(9, 0), Tuples.twin(10, 0));
        assertRejects(predicate, Tuples.twin(8, 0), Tuples.twin(11, 0), Tuples.twin(12, 0));
    }

    @Test
    public void attributeBetweenInclusiveTo()
    {
        Predicate<Pair<Integer, ?>> predicate = Predicates.attributeBetweenInclusiveTo(Functions.<Integer>firstOfPair(), 9, 11);
        assertAccepts(predicate, Tuples.twin(10, 0), Tuples.twin(11, 0));
        assertRejects(predicate, Tuples.twin(8, 0), Tuples.twin(9, 0), Tuples.twin(12, 0));
    }

    @Test
    public void attributeBetweenInclusive()
    {
        Predicate<Pair<Integer, ?>> predicate = Predicates.attributeBetweenInclusive(Functions.<Integer>firstOfPair(), 9, 11);
        assertAccepts(predicate, Tuples.twin(9, 0), Tuples.twin(10, 0), Tuples.twin(11, 0));
        assertRejects(predicate, Tuples.twin(8, 0), Tuples.twin(12, 0));
    }

    @Test
    public void lessThanOrEqualTo()
    {
        assertAccepts(Predicates.lessThanOrEqualTo(0), 0, -1);
        assertRejects(Predicates.lessThanOrEqualTo(0), 1);
        assertToString(Predicates.lessThanOrEqualTo(0));
    }

    @Test
    public void greaterThan()
    {
        assertAccepts(Predicates.greaterThan(0), 1);
        assertRejects(Predicates.greaterThan(0), 0, -1);
        assertToString(Predicates.greaterThan(0));
    }

    @Test
    public void greaterThanOrEqualTo()
    {
        assertAccepts(Predicates.greaterThanOrEqualTo(0), 0, 1);
        assertRejects(Predicates.greaterThanOrEqualTo(0), -1);
        assertToString(Predicates.greaterThanOrEqualTo(0));
    }

    private static void assertToString(Predicate<?> predicate)
    {
        Assert.assertNotNull(predicate.toString());
    }

    private static <T> void assertAccepts(Predicate<? super T> predicate, T... elements)
    {
        for (T element : elements)
        {
            Assert.assertTrue(predicate.accept(element));
        }
    }

    private static <T> void assertRejects(Predicate<? super T> predicate, T... elements)
    {
        for (T element : elements)
        {
            Assert.assertFalse(predicate.accept(element));
        }
    }

    @Test
    public void betweenInclusiveNumber()
    {
        assertBetweenInclusive(Predicates.betweenInclusive(1, 3));
        assertBetweenInclusive(Predicates.<Integer, Integer>attributeBetweenInclusive(Functions.getIntegerPassThru(), 1, 3));
    }

    private static void assertBetweenInclusive(Predicate<Integer> oneToThree)
    {
        assertRejects(oneToThree, 0, 4);
        assertAccepts(oneToThree, 1, 2, 3);
        assertToString(oneToThree);
    }

    @Test
    public void betweenInclusiveString()
    {
        assertStringBetweenInclusive(Predicates.betweenInclusive("1", "3"));
        assertStringBetweenInclusive(Predicates.<String, String>attributeBetweenInclusive(Functions.getToString(), "1", "3"));
    }

    private static void assertStringBetweenInclusive(Predicate<String> oneToThree)
    {
        assertRejects(oneToThree, "0", "4");
        assertAccepts(oneToThree, "1", "2", "3");
        assertToString(oneToThree);
    }

    @Test
    public void betweenInclusiveFromNumber()
    {
        assertBetweenInclusiveFrom(Predicates.betweenInclusiveFrom(1, 3));
        assertBetweenInclusiveFrom(Predicates.<Integer, Integer>attributeBetweenInclusiveFrom(Functions.getIntegerPassThru(), 1, 3));
    }

    private static void assertBetweenInclusiveFrom(Predicate<Integer> oneToThree)
    {
        assertRejects(oneToThree, 0, 3, 4);
        assertAccepts(oneToThree, 1, 2);
        assertToString(oneToThree);
    }

    @Test
    public void betweenInclusiveFromString()
    {
        assertStringBetweenInclusiveFrom(Predicates.betweenInclusiveFrom("1", "3"));
        assertStringBetweenInclusiveFrom(Predicates.<String, String>attributeBetweenInclusiveFrom(Functions.getToString(), "1", "3"));
    }

    private static void assertStringBetweenInclusiveFrom(Predicate<String> oneToThree)
    {
        assertRejects(oneToThree, "0", "3", "4");
        assertAccepts(oneToThree, "1", "2");
        assertToString(oneToThree);
    }

    @Test
    public void betweenInclusiveToNumber()
    {
        assertBetweenInclusiveTo(Predicates.betweenInclusiveTo(1, 3));
        assertBetweenInclusiveTo(Predicates.<Integer, Integer>attributeBetweenInclusiveTo(Functions.getIntegerPassThru(), 1, 3));
    }

    private static void assertBetweenInclusiveTo(Predicate<Integer> oneToThree)
    {
        assertRejects(oneToThree, 0, 1, 4);
        assertAccepts(oneToThree, 2, 3);
        assertToString(oneToThree);
    }

    @Test
    public void betweenInclusiveToString()
    {
        assertStringBetweenInclusiveTo(Predicates.betweenInclusiveTo("1", "3"));
        assertStringBetweenInclusiveTo(Predicates.<String, String>attributeBetweenInclusiveTo(Functions.getToString(), "1", "3"));
    }

    private static void assertStringBetweenInclusiveTo(Predicate<String> oneToThree)
    {
        assertRejects(oneToThree, "0", "1", "4");
        assertAccepts(oneToThree, "2", "3");
        assertToString(oneToThree);
    }

    @Test
    public void betweenExclusiveNumber()
    {
        assertBetweenExclusive(Predicates.betweenExclusive(1, 3));
        assertBetweenExclusive(Predicates.<Integer, Integer>attributeBetweenExclusive(Functions.getIntegerPassThru(), 1, 3));
    }

    private static void assertBetweenExclusive(Predicate<Integer> oneToThree)
    {
        assertRejects(oneToThree, 0, 1, 3, 4);
        assertAccepts(oneToThree, 2);
        assertToString(oneToThree);
    }

    @Test
    public void betweenExclusiveString()
    {
        assertStringBetweenExclusive(Predicates.betweenExclusive("1", "3"));
        assertStringBetweenExclusive(Predicates.<String, String>attributeBetweenExclusive(Functions.getToString(), "1", "3"));
    }

    private static void assertStringBetweenExclusive(Predicate<String> oneToThree)
    {
        assertRejects(oneToThree, "0", "1", "3", "4");
        assertAccepts(oneToThree, "2");
        assertToString(oneToThree);
    }

    @Test
    public void attributeNotNull()
    {
        Twin<String> testCandidate = Tuples.twin("Hello", null);
        assertAccepts(Predicates.attributeNotNull(Functions.<String>firstOfPair()), testCandidate);
        assertRejects(Predicates.attributeNotNull(Functions.<String>secondOfPair()), testCandidate);
        assertToString(Predicates.attributeNotNull(Functions.<String>firstOfPair()));
    }

    public static final class Employee
    {
        public static final Function<Employee, MutableList<Address>> TO_ADDRESSES = new Function<Employee, MutableList<Address>>()
        {
            public MutableList<Address> valueOf(Employee employee)
            {
                return employee.addresses;
            }
        };
        public static final Function<Employee, MutableList<Dependent>> TO_DEPENEDENTS = new Function<Employee, MutableList<Dependent>>()
        {
            public MutableList<Dependent> valueOf(Employee employee)
            {
                return employee.dependents;
            }
        };
        private final MutableList<Address> addresses;
        private final MutableList<Dependent> dependents = Lists.mutable.of();

        private Employee(Address addr)
        {
            this.addresses = FastList.newListWith(addr);
        }

        private void addDependent(Dependent dependent)
        {
            this.dependents.add(dependent);
        }
    }

    public static final class Address
    {
        private final State state;

        private Address(State state)
        {
            this.state = state;
        }

        public State getState()
        {
            return this.state;
        }
    }

    public enum State
    {
        ARIZONA("AZ"),
        ALASKA("AK");

        private final String abbreviation;

        State(String abbreviation)
        {
            this.abbreviation = abbreviation;
        }

        public String getAbbreviation()
        {
            return this.abbreviation;
        }
    }

    public static final class Dependent
    {
        public static final Predicate<Dependent> IS_IMMEDIATE = new Predicate<Dependent>()
        {
            public boolean accept(Dependent dependent)
            {
                return dependent.isImmediate();
            }
        };

        private final DependentType type;

        private Dependent(DependentType type)
        {
            this.type = type;
        }

        public boolean isImmediate()
        {
            return this.type.isImmediate();
        }
    }

    public enum DependentType
    {
        SPOUSE
                {
                    @Override
                    public boolean isImmediate()
                    {
                        return true;
                    }
                },
        CHILD
                {
                    @Override
                    public boolean isImmediate()
                    {
                        return true;
                    }
                },
        PARENT
                {
                    @Override
                    public boolean isImmediate()
                    {
                        return false;
                    }
                },
        GRANDPARENT
                {
                    @Override
                    public boolean isImmediate()
                    {
                        return false;
                    }
                };

        public abstract boolean isImmediate();
    }
}
