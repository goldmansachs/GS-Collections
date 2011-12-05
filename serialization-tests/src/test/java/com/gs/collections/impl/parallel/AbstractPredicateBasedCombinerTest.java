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

package com.gs.collections.impl.parallel;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class AbstractPredicateBasedCombinerTest
{
    private static final AbstractPredicateBasedCombiner<Object, Procedure<Object>> ABSTRACT_PREDICATE_BASED_COMBINER = new AbstractPredicateBasedCombiner<Object, Procedure<Object>>(false, null, 0, null)
    {
        private static final long serialVersionUID = 1L;

        public void combineOne(Procedure<Object> thingToCombine)
        {
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5BYnN0cmFjdFByZWRpY2F0\n"
                        + "ZUJhc2VkQ29tYmluZXJUZXN0JDEAAAAAAAAAAQIAAHhyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5wYXJhbGxlbC5BYnN0cmFjdFByZWRpY2F0ZUJhc2VkQ29tYmluZXIAAAAAAAAAAQIAAUwABnJl\n"
                        + "c3VsdHQAFkxqYXZhL3V0aWwvQ29sbGVjdGlvbjt4cgA6Y29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "cGFyYWxsZWwuQWJzdHJhY3RQcm9jZWR1cmVDb21iaW5lcgAAAAAAAAABAgABWgANdXNlQ29tYmlu\n"
                        + "ZU9uZXhwAHNyAC1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuRmFzdExpc3QA\n"
                        + "AAAAAAAAAQwAAHhwdwQAAAAAeA==",
                ABSTRACT_PREDICATE_BASED_COMBINER);
    }
}
