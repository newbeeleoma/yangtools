/*
 * Copyright (c) 2017 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.yangtools.yang.parser.stmt.rfc7950;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.Test;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.ContainerSchemaNode;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.repo.api.StatementParserMode;
import org.opendaylight.yangtools.yang.parser.spi.meta.SomeModifiersUnresolvedException;
import org.opendaylight.yangtools.yang.stmt.StmtTestUtils;

public class Bug6868Test {
    private static final String FOO_NS = "foo";
    private static final String IMP_NS = "imp";
    private static final String IMP_REV = "2017-01-09";
    private static final Set<String> ALL_CONTAINERS = ImmutableSet.of("my-container-1", "my-container-2",
            "my-container-3", "foo", "not-foo", "imp-bar", "imp-bar-2");

    @Test
    public void ifFeatureYang11ResolutionTest() throws Exception {
        assertSchemaContextFor(null, ALL_CONTAINERS);
        assertSchemaContextFor(ImmutableSet.of(), ImmutableSet.of("my-container-1", "my-container-2", "not-foo"));
        assertSchemaContextFor(ImmutableSet.of("foo"), ImmutableSet.of("foo"));
        assertSchemaContextFor(ImmutableSet.of("baz"),
                ImmutableSet.of("my-container-1", "my-container-2", "my-container-3", "not-foo"));
        assertSchemaContextFor(ImmutableSet.of("bar", "baz"),
                ImmutableSet.of("my-container-1", "my-container-2", "my-container-3", "not-foo"));
        assertSchemaContextFor(ImmutableSet.of("foo", "bar", "baz"),
                ImmutableSet.of("my-container-1", "my-container-2", "my-container-3", "foo"));
        assertSchemaContextFor(ImmutableSet.of("foo", "bar", "baz", "imp:bar"),
                ImmutableSet.of("my-container-1", "my-container-2", "my-container-3", "foo", "imp-bar"));
        assertSchemaContextFor(ImmutableSet.of("foo", "baz", "imp:bar"),
            ImmutableSet.of("foo", "imp-bar", "imp-bar-2"));
    }

    private static void assertSchemaContextFor(final Set<String> supportedFeatures,
            final Set<String> expectedContainers) throws Exception {
        final SchemaContext schemaContext = StmtTestUtils.parseYangSources("/rfc7950/bug6868/yang11",
                supportedFeatures != null ? createFeaturesSet(supportedFeatures) : null,
                StatementParserMode.DEFAULT_MODE);
        assertNotNull(schemaContext);

        for (final String expectedContainer : expectedContainers) {
            assertThat(String.format("Expected container %s not found.", expectedContainer),
                    schemaContext.findDataTreeChild(QName.create(FOO_NS, expectedContainer)).get(),
                    instanceOf(ContainerSchemaNode.class));
        }

        final Set<String> unexpectedContainers = Sets.difference(ALL_CONTAINERS, expectedContainers);
        for (final String unexpectedContainer : unexpectedContainers) {
            assertEquals(String.format("Unexpected container %s.", unexpectedContainer), Optional.empty(),
                    schemaContext.findDataTreeChild(QName.create(FOO_NS, unexpectedContainer)));
        }
    }

    private static Set<QName> createFeaturesSet(final Set<String> featureNames) {
        final Set<QName> supportedFeatures = new HashSet<>();
        for (final String featureName : featureNames) {
            if (featureName.indexOf(':') == -1) {
                supportedFeatures.add(QName.create(FOO_NS, featureName));
            } else {
                supportedFeatures
                        .add(QName.create(IMP_NS, IMP_REV, featureName.substring(featureName.indexOf(':') + 1)));
            }
        }

        return ImmutableSet.copyOf(supportedFeatures);
    }

    @Test
    public void invalidYang10Test() throws Exception {
        try {
            StmtTestUtils.parseYangSource("/rfc7950/bug6868/invalid10.yang");
            fail("Test should fail due to invalid Yang 1.0");
        } catch (final SomeModifiersUnresolvedException e) {
            assertTrue(e.getCause().getMessage()
                    .startsWith("Invalid identifier '(not foo) or (bar and baz)' [at "));
        }
    }
}