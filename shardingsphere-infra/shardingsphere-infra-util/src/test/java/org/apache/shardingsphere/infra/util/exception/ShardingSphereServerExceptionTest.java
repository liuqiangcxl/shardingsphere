/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.infra.util.exception;

import org.apache.shardingsphere.infra.util.exception.fixture.ShardingSphereServerExceptionFixture;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class ShardingSphereServerExceptionTest {
    
    @Test
    public void assertGetMessage() {
        assertThat(new ShardingSphereServerExceptionFixture().getMessage(), is("FIXTURE-00001: Fixture error message"));
    }
    
    @Test
    public void assertGetCause() {
        RuntimeException cause = new RuntimeException("Test");
        assertThat(new ShardingSphereServerExceptionFixture(cause).getCause(), is(cause));
    }
}
