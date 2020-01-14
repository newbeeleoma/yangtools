/*
 * Copyright (c) 2016 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.stmt;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import org.junit.Test;
import org.opendaylight.yangtools.yang.model.api.Module;

public class TwoRevisionsTest {
    @Test
    public void testTwoRevisions() throws Exception {
        Collection<? extends Module> modules = TestUtils.loadModules(getClass().getResource("/ietf").toURI())
                .getModules();
        //FIXME: following assert needs module revisions .equals() solution first
        assertEquals(2, TestUtils.findModules(modules, "network-topology").size());
    }
}
