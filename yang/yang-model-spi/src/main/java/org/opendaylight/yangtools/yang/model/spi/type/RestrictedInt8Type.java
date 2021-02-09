/*
 * Copyright (c) 2017 Pantheon Technologies s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.model.spi.type;

import java.util.Collection;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.UnknownSchemaNode;
import org.opendaylight.yangtools.yang.model.api.type.Int8TypeDefinition;
import org.opendaylight.yangtools.yang.model.api.type.RangeConstraint;

final class RestrictedInt8Type extends AbstractRangeRestrictedType<Int8TypeDefinition, Byte>
        implements Int8TypeDefinition {
    RestrictedInt8Type(final Int8TypeDefinition baseType, final QName qname,
            final Collection<? extends UnknownSchemaNode> unknownSchemaNodes,
            final @Nullable RangeConstraint<Byte> rangeConstraint) {
        super(baseType, qname, unknownSchemaNodes, rangeConstraint);
    }

    private RestrictedInt8Type(final RestrictedInt8Type original, final QName qname) {
        super(original, qname);
    }

    @Override
    RestrictedInt8Type bindTo(final QName newQName) {
        return new RestrictedInt8Type(this, newQName);
    }

    @Override
    public int hashCode() {
        return Int8TypeDefinition.hashCode(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return Int8TypeDefinition.equals(this, obj);
    }

    @Override
    public String toString() {
        return Int8TypeDefinition.toString(this);
    }
}