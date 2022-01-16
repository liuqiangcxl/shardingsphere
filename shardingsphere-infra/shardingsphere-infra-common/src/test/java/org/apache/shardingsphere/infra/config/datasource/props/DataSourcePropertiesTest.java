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

package org.apache.shardingsphere.infra.config.datasource.props;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.shardingsphere.test.mock.MockedDataSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class DataSourcePropertiesTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void assertEquals() {
        assertThat(new DataSourceProperties(MockedDataSource.class.getName(), createUserProperties("root")), 
                is(new DataSourceProperties(MockedDataSource.class.getName(), createUserProperties("root"))));
    }
    
    @Test
    public void assertNotEqualsWithNullValue() {
        assertFalse(new DataSourceProperties(MockedDataSource.class.getName(), new HashMap<>()).equals(null));
    }
    
    @Test
    public void assertNotEqualsWithDifferentDataSourceClassName() {
        assertThat(new DataSourceProperties("FooDataSourceClass", new HashMap<>()), not(new DataSourceProperties("BarDataSourceClass", new HashMap<>())));
    }
    
    @Test
    public void assertNotEqualsWithDifferentProperties() {
        DataSourceProperties actual = new DataSourceProperties(MockedDataSource.class.getName(), createUserProperties("foo"));
        DataSourceProperties expected = new DataSourceProperties(MockedDataSource.class.getName(), createUserProperties("bar"));
        assertThat(actual, not(expected));
    }
    
    @Test
    public void assertSameHashCode() {
        assertThat(new DataSourceProperties(MockedDataSource.class.getName(), createUserProperties("root")).hashCode(), 
                is(new DataSourceProperties(MockedDataSource.class.getName(), createUserProperties("root")).hashCode()));
    }
    
    @Test
    public void assertDifferentHashCodeWithDifferentDataSourceClassName() {
        assertThat(new DataSourceProperties("FooDataSourceClass", createUserProperties("foo")).hashCode(),
                not(new DataSourceProperties("BarDataSourceClass", createUserProperties("foo")).hashCode()));
    }
    
    @Test
    public void assertDifferentHashCodeWithDifferentProperties() {
        assertThat(new DataSourceProperties(MockedDataSource.class.getName(), createUserProperties("foo")).hashCode(), 
                not(new DataSourceProperties(MockedDataSource.class.getName(), createUserProperties("bar")).hashCode()));
    }
    
    private Map<String, Object> createUserProperties(final String username) {
        Map<String, Object> result = new LinkedHashMap<>(1, 1);
        result.put("username", username);
        return result;
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void assertGetDataSourceConfigurationWithConnectionInitSqls() {
        BasicDataSource actualDataSource = new BasicDataSource();
        actualDataSource.setDriverClassName(MockedDataSource.class.getCanonicalName());
        actualDataSource.setUrl("jdbc:mock://127.0.0.1/foo_ds");
        actualDataSource.setUsername("root");
        actualDataSource.setPassword("root");
        actualDataSource.setConnectionInitSqls(Arrays.asList("set names utf8mb4;", "set names utf8;"));
        DataSourceProperties actual = DataSourcePropertiesCreator.create(actualDataSource);
        assertThat(actual.getDataSourceClassName(), is(BasicDataSource.class.getName()));
        assertThat(actual.getProps().get("driverClassName").toString(), is(MockedDataSource.class.getCanonicalName()));
        assertThat(actual.getProps().get("url").toString(), is("jdbc:mock://127.0.0.1/foo_ds"));
        assertThat(actual.getProps().get("username").toString(), is("root"));
        assertThat(actual.getProps().get("password").toString(), is("root"));
        assertNull(actual.getProps().get("loginTimeout"));
        assertThat(actual.getProps().get("connectionInitSqls"), instanceOf(List.class));
        List<String> actualConnectionInitSql = (List<String>) actual.getProps().get("connectionInitSqls");
        assertThat(actualConnectionInitSql, hasItem("set names utf8mb4;"));
        assertThat(actualConnectionInitSql, hasItem("set names utf8;"));
    }
    
    @Test
    public void assertGetAllProperties() {
        DataSourceProperties originalDataSourceProps = new DataSourceProperties(MockedDataSource.class.getName(), getProperties());
        Map<String, Object> actualAllProperties = originalDataSourceProps.getAllProperties();
        assertNotNull(actualAllProperties);
        assertThat(actualAllProperties.size(), is(7));
        assertTrue(actualAllProperties.containsKey("driverClassName"));
        assertTrue(actualAllProperties.containsValue(MockedDataSource.class.getName()));
        assertTrue(actualAllProperties.containsKey("jdbcUrl"));
        assertTrue(actualAllProperties.containsValue("jdbc:mock://127.0.0.1/foo_ds"));
        assertTrue(actualAllProperties.containsKey("username"));
        assertTrue(actualAllProperties.containsValue("root"));
        assertTrue(actualAllProperties.containsKey("password"));
        assertTrue(actualAllProperties.containsValue("root"));
        assertTrue(actualAllProperties.containsKey("loginTimeout"));
        assertTrue(actualAllProperties.containsValue("5000"));
        assertTrue(actualAllProperties.containsKey("maximumPoolSize"));
        assertTrue(actualAllProperties.containsValue("30"));
        assertTrue(actualAllProperties.containsKey("idleTimeout"));
        assertTrue(actualAllProperties.containsValue("30000"));
    }
    
    private Map<String, Object> getProperties() {
        Map<String, Object> result = new HashMap<>(7, 1);
        result.put("driverClassName", MockedDataSource.class.getCanonicalName());
        result.put("jdbcUrl", "jdbc:mock://127.0.0.1/foo_ds");
        result.put("username", "root");
        result.put("password", "root");
        result.put("loginTimeout", "5000");
        result.put("maximumPoolSize", "30");
        result.put("idleTimeout", "30000");
        return result;
    }
}