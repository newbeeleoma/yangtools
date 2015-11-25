/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.stmt.rfc6020;

import static org.opendaylight.yangtools.yang.parser.spi.SubstatementValidator.MAX;

import java.util.Collection;
import org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping;
import org.opendaylight.yangtools.yang.model.api.meta.EffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.EnumStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.TypeStatement;
import org.opendaylight.yangtools.yang.parser.spi.SubstatementValidator;
import org.opendaylight.yangtools.yang.parser.spi.meta.AbstractDeclaredStatement;
import org.opendaylight.yangtools.yang.parser.spi.meta.AbstractStatementSupport;
import org.opendaylight.yangtools.yang.parser.spi.meta.StmtContext;
import org.opendaylight.yangtools.yang.parser.spi.source.SourceException;
import org.opendaylight.yangtools.yang.parser.stmt.rfc6020.effective.type.EnumSpecificationEffectiveStatementImpl;

public class EnumSpecificationImpl extends AbstractDeclaredStatement<String> implements TypeStatement.EnumSpecification {
    private static final SubstatementValidator SUBSTATEMENT_VALIDATOR = SubstatementValidator.builder(Rfc6020Mapping
            .TYPE)
            .add(Rfc6020Mapping.ENUM, 1, MAX)
            .build();

    protected EnumSpecificationImpl(StmtContext<String, TypeStatement.EnumSpecification, ?> context) {
        super(context);
    }

    public static class Definition
            extends
            AbstractStatementSupport<String, TypeStatement.EnumSpecification, EffectiveStatement<String, TypeStatement.EnumSpecification>> {

        public Definition() {
            super(Rfc6020Mapping.TYPE);
        }

        @Override
        public String parseArgumentValue(StmtContext<?, ?, ?> ctx, String value) throws SourceException {
            return value;
        }

        @Override
        public TypeStatement.EnumSpecification createDeclared(
                StmtContext<String, TypeStatement.EnumSpecification, ?> ctx) {
            return new EnumSpecificationImpl(ctx);
        }

        @Override
        public EffectiveStatement<String, TypeStatement.EnumSpecification> createEffective(
                StmtContext<String, TypeStatement.EnumSpecification, EffectiveStatement<String, TypeStatement.EnumSpecification>> ctx) {
            return new EnumSpecificationEffectiveStatementImpl(ctx);
        }

        @Override
        public void onFullDefinitionDeclared(StmtContext.Mutable<String, EnumSpecification,
                EffectiveStatement<String, EnumSpecification>> stmt) throws SourceException {
            super.onFullDefinitionDeclared(stmt);
            SUBSTATEMENT_VALIDATOR.validate(stmt);
        }
    }

    @Override
    public String getName() {
        return argument();
    }

    @Override
    public Collection<? extends EnumStatement> getEnums() {
        return allDeclared(EnumStatement.class);
    }

}
