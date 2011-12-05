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

package com.gs.collections.impl

import java.lang.StringBuilder

import list.mutable.FastList
import multimap.list.FastListMultimap
import org.junit.Test

import com.gs.collections.api.RichIterable
import com.gs.collections.api.collection.MutableCollection
import com.gs.collections.api.tuple.Pair
import com.gs.collections.api.multimap.MutableMultimap
import Prelude._
import com.gs.collections.api.list.MutableList

trait SynchronizedRichIterableTestTrait extends SynchronizedMutableIterableTestTrait /* with RichIterableTestTrait */
{
    val classUnderTest: RichIterable[String]

    @Test
    def size_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.size
        }
    }

    @Test
    def isEmpty_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.isEmpty
        }
    }

    @Test
    def notEmpty_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.notEmpty
        }
    }

    @Test
    def getFirst_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.getFirst
        }
    }

    @Test
    def getLast_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.getLast
        }
    }

    @Test
    def contains_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.contains(null)
        }
    }

    @Test
    def containsAllIterable_synchronized
    {
        this.assertSynchronized
        {
            val iterable: java.lang.Iterable[_] = FastList.newList[AnyRef]
            this.classUnderTest.containsAllIterable(iterable)
        }
    }

    @Test
    def containsAllArguments_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.containsAllArguments("", "", "")
        }
    }

    @Test
    def select_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.select({
                _: String => false
            })
        }
    }

    @Test
    def select_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.select({
                _: String => false
            }, FastList.newList[String])
        }
    }

    @Test
    def reject_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.reject({
                _: String => true
            })
        }
    }

    @Test
    def reject_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.reject({
                _: String => true
            }, null)
        }
    }

    @Test
    def partition_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.partition({
                _: String => true
            })
        }
    }

    @Test
    def collect_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.collect({
                _: String => null
            })
        }
    }

    @Test
    def collect_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.collect[String, MutableCollection[String]](
            {
                _: String => ""
            },
            FastList.newList[String])
        }
    }

    @Test
    def collectIf_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.collectIf({
                _: String => false
            },
            {
                _: String => ""
            })
        }
    }

    @Test
    def collectIf_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.collectIf[String, MutableCollection[String]](
            {
                _: String => false
            },
            {
                _: String => ""
            },
            FastList.newList[String])
        }
    }

    @Test
    def flatCollect_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.flatCollect({
                _: String => FastList.newList[String]
            })
        }
    }

    @Test
    def flatCollect_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.flatCollect[String, MutableList[String]]({
                _: String => FastList.newList[String]
            }, FastList.newList[String])
        }
    }

    @Test
    def detect_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.detect({
                _: String => false
            })
        }
    }

    @Test
    def detectIfNone_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.detectIfNone({
                _: String => false
            },
            {
                () => ""
            })
        }
    }

    @Test
    def count_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.count({
                _: String => false
            })
        }
    }

    @Test
    def anySatisfy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.anySatisfy({
                _: String => true
            })
        }
    }

    @Test
    def allSatisfy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.allSatisfy({
                _: String => false
            })
        }
    }

    @Test
    def injectInto_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.injectInto[String]("",
            {
                (_: String, _: String) => ""
            })
        }
    }

    @Test
    def toList_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toList
        }
    }

    @Test
    def toSortedList_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toSortedList
        }
    }

    @Test
    def toSortedList_with_comparator_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toSortedList(null)
        }
    }

    @Test
    def toSortedListBy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toSortedListBy[String]((string: String) => string)
        }
    }

    @Test
    def toSet_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toSet
        }
    }

    @Test
    def toMap_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toMap({
                _: String => ""
            },
            {
                _: String => ""
            })
        }
    }

    @Test
    def toArray_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toArray
        }
    }

    @Test
    def toArray_with_target_synchronized
    {
        this.assertSynchronized
        {
            val array: Array[String] = new Array[String](this.classUnderTest.size())
            this.classUnderTest.toArray(array)
        }
    }

    @Test
    def max_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.max({
                (_: String, _: String) => 0
            })
        }
    }

    @Test
    def min_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.min({
                (_: String, _: String) => 0
            })
        }
    }

    @Test
    def max_without_comparator_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.max()
        }
    }

    @Test
    def min_without_comparator_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.min()
        }
    }

    @Test
    def maxBy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.maxBy[String]((string: String) => string)
        }
    }

    @Test
    def minBy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.minBy[String]((string: String) => string)
        }
    }

    @Test
    def makeString_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.makeString
        }
    }

    @Test
    def makeString_with_separator_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.makeString(", ")
        }
    }

    @Test
    def makeString_with_start_separator_end_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.makeString("[", ", ", "]")
        }
    }

    @Test
    def appendString_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.appendString(new StringBuilder)
        }
    }

    @Test
    def appendString_with_separator_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.appendString(new StringBuilder, ", ")
        }
    }

    @Test
    def appendString_with_start_separator_end_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.appendString(new StringBuilder, "[", ", ", "]")
        }
    }

    @Test
    def groupBy_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.groupBy({
                _: String => ""
            })
        }
    }

    @Test
    def groupBy_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.groupBy[String, MutableMultimap[String, String]](
            {
                _: String => ""
            },
            FastListMultimap.newMultimap[String, String])
        }
    }

    @Test
    def toString_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toString
        }
    }

    @Test
    def zip_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.zip[String](FastList.newList[String])
        }
    }

    @Test
    def zip__with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.zip[String, FastList[Pair[String, String]]](FastList.newList[String](),
                FastList.newList[Pair[String, String]]())
        }
    }

    @Test
    def zipWithIndex_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.zipWithIndex()
        }
    }

    @Test
    def zipWithIndex__with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.zipWithIndex(FastList.newList[Pair[String, java.lang.Integer]]())
        }
    }
}
