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

package com.webguys.ponzu.impl.collection.mutable

import org.junit.Test

import com.webguys.ponzu.api.list.MutableList
import com.webguys.ponzu.api.collection.MutableCollection
import com.webguys.ponzu.impl.Prelude._
import com.webguys.ponzu.impl.{SynchronizedCollectionTestTrait, SynchronizedRichIterableTestTrait}
import com.webguys.ponzu.impl.list.mutable.FastList

trait SynchronizedMutableCollectionTestTrait
        extends SynchronizedRichIterableTestTrait
        with SynchronizedCollectionTestTrait
        with MutableCollectionTestTrait
{
    val classUnderTest: MutableCollection[String]

    @Test
    def newEmpty_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.newEmpty
        }
    }

    /**
     * {@link SynchronizedRichIterableTestTrait} and {@link SynchronizedCollectionTestTrait} both define these methods
     * the same way.  They need to be overridden to point to one.  Which one to pick was an arbitrary choice.
     */
    override def size_synchronized = super[SynchronizedRichIterableTestTrait].size_synchronized

    override def isEmpty_synchronized = super[SynchronizedRichIterableTestTrait].isEmpty_synchronized

    override def contains_synchronized = super[SynchronizedRichIterableTestTrait].contains_synchronized

    override def iterator_not_synchronized = super[SynchronizedRichIterableTestTrait].iterator_not_synchronized

    override def toArray_synchronized = super[SynchronizedRichIterableTestTrait].toArray_synchronized

    override def toArray_with_target_synchronized = super[SynchronizedRichIterableTestTrait].toArray_with_target_synchronized

    @Test
    def addAllIterable_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.addAllIterable(FastList.newList[String])
        }
    }

    @Test
    def removeAllIterable_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.removeAllIterable(FastList.newList[String])
        }
    }

    @Test
    def retainAllIterable_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.retainAllIterable(FastList.newList[String])
        }
    }

    @Test
    def filterWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.filterWith({
                (_: String, _: String) => false
            }, "")
        }
    }

    @Test
    def filterWith_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.filterWith({
                (_: String, _: String) => false
            }, "", FastList.newList[String])
        }
    }

    @Test
    def filterNotWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.filterNotWith({
                (_: String, _: String) => true
            }, "")
        }
    }

    @Test
    def filterNotWith_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.filterNotWith({
                (_: String, _: String) => true
            }, "", FastList.newList[String])
        }
    }

    @Test
    def filterAndRejectWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.partitionWith({
                (_: String, _: String) => true
            }, "")
        }
    }

    @Test
    def removeIf_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.removeIf({
                (_: String) => false
            })
        }
    }

    @Test
    def removeIfWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.removeIfWith({
                (_: String, _: String) => false
            }, "")
        }
    }

    @Test
    def transformWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.transformWith({
                (_: String, _: String) => ""
            }, "")
        }
    }

    @Test
    def transformWith_with_target_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.transformWith[String, String, MutableList[String]](
            {
                (_: String, _: String) => ""
            },
            "",
            FastList.newList[String])
        }
    }

    @Test
    def findWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.findWith({
                (_: String, _: String) => true
            }, "")
        }
    }

    @Test
    def findWithIfNone_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.findWithIfNone({
                (_: String, _: String) => true
            }, "", null)
        }
    }

    @Test
    def countWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.countWith({
                (_: String, _: String) => true
            }, "")
        }
    }

    @Test
    def anySatisfyWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.anySatisfyWith({
                (_: String, _: String) => true
            }, "")
        }
    }

    @Test
    def allSatisfyWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.allSatisfyWith({
                (_: String, _: String) => true
            }, "")
        }
    }

    @Test
    def foldLeftWith_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.foldLeftWith[String, String]("",
            {
                (_: String, _: String, _: String) => ""
            }, "")
        }
    }

    @Test
    def asUnmodifiable_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.asUnmodifiable
        }
    }

    @Test
    def asSynchronized_not_synchronized
    {
        this.assertNotSynchronized
        {
            this.classUnderTest.asSynchronized
        }
    }

    @Test
    def toImmutable_synchronized
    {
        this.assertSynchronized
        {
            this.classUnderTest.toImmutable
        }
    }

    @Test
    def with_synchronized
    {
        this.assertSynchronized(this.classUnderTest.`with`("4"))
    }

    @Test
    def withAll_synchronized
    {
        this.assertSynchronized(this.classUnderTest.withAll(FastList.newListWith("4")))
    }

    @Test
    def without_synchronized
    {
        this.assertSynchronized(this.classUnderTest.without("4"))
    }

    @Test
    def withoutAll_synchronized
    {
        this.assertSynchronized(this.classUnderTest.withoutAll(FastList.newListWith("4")))
    }
}
