/*
 * Copyright (c) 2017 Pantheon Technologies, s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.rfc6536.model.api;

import com.google.common.annotations.Beta;
import org.opendaylight.yangtools.yang.common.Empty;
import org.opendaylight.yangtools.yang.model.api.meta.StatementDefinition;
import org.opendaylight.yangtools.yang.model.api.stmt.UnknownStatement;

/**
 * Declared statement representation of 'default-deny-write' extension defined in
 * <a href="https://tools.ietf.org/html/rfc6536">RFC6536</a>.
 */
@Beta
public interface DefaultDenyWriteStatement extends UnknownStatement<Empty> {
    @Override
    default StatementDefinition statementDefinition() {
        return NACMStatements.DEFAULT_DENY_WRITE;
    }
}
