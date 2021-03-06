/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang2sources.spi;

import org.apache.maven.project.MavenProject;
import org.opendaylight.yangtools.plugin.generator.api.FileGeneratorFactory;

/**
 * Bridge for plugins which need access to the underlying maven project.
 *
 * @deprecated Use {@link FileGeneratorFactory} instead.
 */
@Deprecated
public interface MavenProjectAware {
    /**
     * Provided maven project object. Any additional information about current
     * maven project can be accessed from it.
     */
    void setMavenProject(MavenProject project);
}
