/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.spi.source;

import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yangtools.yang.parser.spi.meta.NamespaceBehaviour;
import org.opendaylight.yangtools.yang.parser.spi.meta.ParserNamespace;

/**
 * Source-specific mapping of prefixes to namespaces.
 */
public interface BelongsToPrefixToModuleName extends ParserNamespace<String, String> {
    NamespaceBehaviour<String, String, @NonNull BelongsToPrefixToModuleName> BEHAVIOUR =
            NamespaceBehaviour.sourceLocal(BelongsToPrefixToModuleName.class);
}
