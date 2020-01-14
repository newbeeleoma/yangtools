/*
 * Copyright (c) 2016 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.stmt.yin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Optional;
import org.junit.Test;
import org.opendaylight.yangtools.yang.model.api.ContainerSchemaNode;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.RpcDefinition;
import org.opendaylight.yangtools.yang.stmt.TestUtils;

public class YinFileRpcStmtTest extends AbstractYinModulesTest {

    @Test
    public void testRpc() {
        Module testModule = TestUtils.findModule(context, "ietf-netconf-monitoring").get();

        Collection<? extends RpcDefinition> rpcs = testModule.getRpcs();
        assertEquals(1, rpcs.size());

        RpcDefinition rpc = rpcs.iterator().next();
        assertEquals("get-schema", rpc.getQName().getLocalName());
        assertEquals(Optional.of("This operation is used to retrieve a schema from the\n"
                + "NETCONF server.\n"
                + "\n"
                + "Positive Response:\n"
                + "The NETCONF server returns the requested schema.\n"
                + "\n"
                + "Negative Response:\n"
                + "If requested schema does not exist, the <error-tag> is\n"
                + "'invalid-value'.\n"
                + "\n"
                + "If more than one schema matches the requested parameters, the\n"
                + "<error-tag> is 'operation-failed', and <error-app-tag> is\n"
                + "'data-not-unique'."), rpc.getDescription());

        ContainerSchemaNode input = rpc.getInput();
        assertNotNull(input);
        assertEquals(3, input.getChildNodes().size());

        ContainerSchemaNode output = rpc.getOutput();
        assertNotNull(output);
        assertEquals(1, output.getChildNodes().size());
    }
}
