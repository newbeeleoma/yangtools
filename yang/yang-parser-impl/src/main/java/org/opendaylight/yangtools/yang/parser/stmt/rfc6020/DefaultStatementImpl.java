/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.stmt.rfc6020;

import javax.annotation.Nonnull;
import org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping;
import org.opendaylight.yangtools.yang.model.api.meta.EffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.DefaultStatement;
import org.opendaylight.yangtools.yang.parser.spi.SubstatementValidator;
import org.opendaylight.yangtools.yang.parser.spi.meta.AbstractDeclaredStatement;
import org.opendaylight.yangtools.yang.parser.spi.meta.AbstractStatementSupport;
import org.opendaylight.yangtools.yang.parser.spi.meta.StmtContext;
import org.opendaylight.yangtools.yang.parser.spi.source.SourceException;
import org.opendaylight.yangtools.yang.parser.stmt.rfc6020.effective.DefaultEffectiveStatementImpl;

public class DefaultStatementImpl extends AbstractDeclaredStatement<String> implements
        DefaultStatement {
    private static final SubstatementValidator SUBSTATEMENT_VALIDATOR = SubstatementValidator.builder(Rfc6020Mapping
            .DEFAULT)
            .build();

    protected DefaultStatementImpl(
            StmtContext<String, DefaultStatement, ?> context) {
        super(context);
    }

    public static class Definition extends AbstractStatementSupport<String,DefaultStatement,EffectiveStatement<String,DefaultStatement>> {

        public Definition() {
            super(Rfc6020Mapping.DEFAULT);
        }

        @Override public String parseArgumentValue(
                StmtContext<?, ?, ?> ctx, String value) throws SourceException {
            return value;
        }

        @Override public DefaultStatement createDeclared(
                StmtContext<String, DefaultStatement, ?> ctx) {
            return new DefaultStatementImpl(ctx);
        }

        @Override public EffectiveStatement<String, DefaultStatement> createEffective(
                StmtContext<String, DefaultStatement, EffectiveStatement<String, DefaultStatement>> ctx) {
            return new DefaultEffectiveStatementImpl(ctx);
        }

        @Override
        public void onFullDefinitionDeclared(StmtContext.Mutable<String, DefaultStatement,
                EffectiveStatement<String, DefaultStatement>> stmt) throws SourceException {
            super.onFullDefinitionDeclared(stmt);
            SUBSTATEMENT_VALIDATOR.validate(stmt);
        }
    }

    @Nonnull @Override
    public String getValue() {
        return rawArgument();
    }
}
