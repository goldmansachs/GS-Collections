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

package com.webguys.ponzu.impl.list.fixed;

import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.impl.list.mutable.UnmodifiableMutableListTestCase;

public class ArrayAdapterAsUnmodifiableTest extends UnmodifiableMutableListTestCase
{
    @Override
    protected MutableList<Integer> getCollection()
    {
        return ArrayAdapter.<Integer>newArrayWith(1).asUnmodifiable();
    }
}
