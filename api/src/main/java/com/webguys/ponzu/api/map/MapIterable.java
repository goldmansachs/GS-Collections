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

package com.webguys.ponzu.api.map;

import java.util.Map;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.function.Generator;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.tuple.Pair;

/**
 * A Read-only Map api, with the minor exception inherited from java.lang.Iterable (iterable.iterator().remove()).
 */
public interface MapIterable<K, V> extends RichIterable<V>
{
    /**
     * @see Map#get(Object)
     */
    V get(Object key);

    /**
     * @see Map#containsKey(Object)
     */
    boolean containsKey(Object key);

    /**
     * @see Map#containsValue(Object)
     */
    boolean containsValue(Object value);

    /**
     * Calls the procedure with each <em>value</em> of the map.
     * <pre>
     *     Set&lt;String&gt; result = UnifiedSet.newSet();
     *     MutableMap&lt;Integer, String&gt; map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
     *     map.<b>forEachValue</b>(new CollectionAddProcedure&lt;String&gt;(result));
     *     Verify.assertSetsEqual(UnifiedSet.newSetWith("One", "Two", "Three", "Four"), result);
     * </pre>
     */
    void forEachValue(Procedure<? super V> procedure);

    /**
     * Calls the <code>procedure</code> with each <em>key</em> of the map.
     * <pre>
     *     final Collection&lt;Integer&gt; result = new ArrayList&lt;Integer&gt;();
     *     MutableMap&lt;Integer, String&gt; map = this.newMapWithKeysValues(1, "1", 2, "2", 3, "3");
     *     map.<b>forEachKey</b>(new CollectionAddProcedure&lt;Integer&gt;(result));
     *     Verify.assertContainsAll(result, 1, 2, 3);
     * </pre>
     */
    void forEachKey(Procedure<? super K> procedure);

    /**
     * Calls the <code>procedure</code> with each <em>key-value</em> pair of the map.
     * <pre>
     *     final Collection&lt;String&gt; collection = new ArrayList&lt;String&gt;();
     *     MutableMap&lt;Integer, String&gt; map = this.newMapWithKeysValues(1, "One", 2, "Two", 3, "Three");
     *     map.<b>forEachKeyValue</b>(new Procedure2&lt;Integer, String&gt;()
     *     {
     *         public void value(final Integer key, final String value)
     *         {
     *             collection.add(String.valueOf(key) + value);
     *         }
     *     });
     *     Verify.assertContainsAll(collection, "1One", "2Two", "3Three");
     * </pre>
     */
    void forEachKeyValue(Procedure2<? super K, ? super V> procedure);

    /**
     * Return the value in the Map that corresponds to the specified key, or if there is no value at the key, return the
     * result of evaluating the specified Function0.
     */
    V getIfAbsent(K key, Generator<? extends V> function);

    /**
     * Return the value in the Map that corresponds to the specified key, or if there is no value at the key, return the
     * result of evaluating the specified function and parameter.
     */
    <P> V getIfAbsentWith(K key, Function<? super P, ? extends V> function, P parameter);

    /**
     * If there is a value in the Map that corresponds to the specified key return the result of applying the specified
     * Function on the value, otherwise return null.
     */
    <A> A ifPresentApply(K key, Function<? super V, ? extends A> function);

    /**
     * Returns an unmodifiable lazy iterable wrapped around the keySet for the map
     */
    RichIterable<K> keysView();

    /**
     * Returns an unmodifiable lazy iterable wrapped around the values for the map
     */
    RichIterable<V> valuesView();

    /**
     * Returns an unmodifiable lazy iterable of key/value pairs wrapped around the entrySet for the map
     */
    RichIterable<Pair<K, V>> keyValuesView();

    /**
     * For each key and value of the map the predicate is evaluated, if the result of the evaluation is true,
     * that key and value are returned in a new map.
     * <p/>
     * <pre>e.g.
     * peopleByCity.filter(new Predicate2&lt;City, Person&gt;()
     * {
     *     public boolean value(City city, Person person)
     *     {
     *         return city.getName().equals("Anytown") && person.getLastName().equals("Smith");
     *     }
     * });
     * </pre>
     */
    MapIterable<K, V> filter(Predicate2<? super K, ? super V> predicate);

    /**
     * For each key and value of the map the function is evaluated.  The results of these evaluations are returned in
     * a new map.  The map returned will use the values projected from the function rather than the original values.
     * <p/>
     * <pre>e.g.
     * peopleByCity.transformValues(new Function2&lt;City, Person, String&gt;()
     * {
     *     public String value(City city, Person person)
     *     {
     *         return person.getFirstName() + " " + person.getLastName();
     *     }
     * });
     * </pre>
     */
    <R> MapIterable<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function);

    /**
     * For each key and value of the map the function is evaluated.  The results of these evaluations are returned in
     * a new map.  The map returned will use the values projected from the function rather than the original values.
     * <p/>
     * <pre>e.g.
     * peopleByCity.transform(new Function2&lt;City, Person, String&gt;()
     * {
     *     public String value(City city, Person person)
     *     {
     *         return Pair.of(city.getCountry(), person.getAddress().getCity());
     *     }
     * });
     * </pre>
     */
    <K2, V2> MapIterable<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function);

    /**
     * For each key and value of the map the predicate is evaluated, if the result of the evaluation is false,
     * that key and value are returned in a new map.
     * <p/>
     * <pre>e.g.
     * peopleByCity.filterNot(new Predicate2&lt;City, Person&gt;()
     * {
     *     public boolean value(City city, Person person)
     *     {
     *         return city.getName().equals("Anytown") && person.getLastName().equals("Smith");
     *     }
     * });
     * </pre>
     */
    MapIterable<K, V> filterNot(Predicate2<? super K, ? super V> predicate);

    /**
     * Return the first key and value of the map for which the predicate evaluates to true when they are given
     * as arguments. The predicate will only be evaluated until such pair is found or until all of the keys and
     * values of the map have been used as arguments. That is, there may be keys and values of the map that are
     * never used as arguments to the predicate. The result is null if predicate does not evaluate to true for
     * any key/value combination.
     * <p/>
     * <pre>e.g.
     * peopleByCity.find(new Predicate2&lt;City, Person&gt;()
     * {
     *     public boolean value(City city, Person person)
     *     {
     *         return city.getName().equals("Anytown") && person.getLastName().equals("Smith");
     *     }
     * });
     * </pre>
     */
    Pair<K, V> find(Predicate2<? super K, ? super V> predicate);
}
