/*
 * Copyright 2011 Goldman Sachs & Co.
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

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class HashingStrategiesTest
{
    @Test
    public void defaultHashingStrategy()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJh\n"
                        + "dGVnaWVzJERlZmF1bHRTdHJhdGVneQAAAAAAAAABAgAAeHA=",
                HashingStrategies.<Object>defaultStrategy());
    }
}
