/*
 * Copyright (c) 2019 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.model.api.stmt.compat;

import com.google.common.annotations.Beta;
import java.util.Optional;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.ActionDefinition;
import org.opendaylight.yangtools.yang.model.api.ActionNodeContainer;
import org.opendaylight.yangtools.yang.model.api.meta.DeclaredStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.SchemaTreeAwareEffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.SchemaTreeEffectiveStatement;

/**
 * Compatibility bridge between {@link ActionNodeContainer#findAction(QName)} and
 * {@link SchemaTreeAwareEffectiveStatement}.
 */
@Beta
public interface ActionNodeContainerCompat<A, D extends DeclaredStatement<A>>
        extends SchemaTreeAwareEffectiveStatement<A, D>, ActionNodeContainer {

    @Override
    default Optional<ActionDefinition> findAction(final QName qname) {
        // 'action' identifier must never collide with another element, hence if we look it up and it ends up being
        // an ActionDefinition, we have found a match.
        final SchemaTreeEffectiveStatement<?> child = get(Namespace.class, qname);
        return child instanceof ActionDefinition ? Optional.of((ActionDefinition) child) : Optional.empty();
    }
}
