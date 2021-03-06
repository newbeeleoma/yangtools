/*
 * Copyright (c) 2016 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.repo;

import static org.junit.Assert.assertNotNull;
import static org.opendaylight.yangtools.util.concurrent.FluentFutures.immediateFluentFuture;

import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.opendaylight.yangtools.yang.common.Revision;
import org.opendaylight.yangtools.yang.model.api.EffectiveModelContext;
import org.opendaylight.yangtools.yang.model.repo.api.RevisionSourceIdentifier;
import org.opendaylight.yangtools.yang.model.repo.api.SchemaContextFactoryConfiguration;
import org.opendaylight.yangtools.yang.model.repo.api.SourceIdentifier;
import org.opendaylight.yangtools.yang.model.repo.api.YangTextSchemaSource;
import org.opendaylight.yangtools.yang.model.repo.spi.PotentialSchemaSource;
import org.opendaylight.yangtools.yang.parser.rfc7950.ir.IRSchemaSource;
import org.opendaylight.yangtools.yang.parser.rfc7950.repo.TextToIRTransformer;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class SharedEffectiveModelContextFactoryTest {

    private final SharedSchemaRepository repository = new SharedSchemaRepository("test");

    private final SchemaContextFactoryConfiguration config = SchemaContextFactoryConfiguration.getDefault();
    private SourceIdentifier s1;
    private SourceIdentifier s2;

    @Before
    public void setUp() {
        final YangTextSchemaSource source1 = YangTextSchemaSource.forResource("/ietf/ietf-inet-types@2010-09-24.yang");
        final YangTextSchemaSource source2 = YangTextSchemaSource.forResource("/ietf/iana-timezones@2012-07-09.yang");
        s1 = RevisionSourceIdentifier.create("ietf-inet-types", Revision.of("2010-09-24"));
        s2 = RevisionSourceIdentifier.create("iana-timezones", Revision.of("2012-07-09"));

        final TextToIRTransformer transformer = TextToIRTransformer.create(repository, repository);
        repository.registerSchemaSourceListener(transformer);

        repository.registerSchemaSource(sourceIdentifier -> immediateFluentFuture(source1),
            PotentialSchemaSource.create(s1, YangTextSchemaSource.class, 1));

        repository.registerSchemaSource(sourceIdentifier -> immediateFluentFuture(source2),
            PotentialSchemaSource.create(s2, YangTextSchemaSource.class, 1));
    }

    @Test
    public void testCreateSchemaContextWithDuplicateRequiredSources() throws InterruptedException, ExecutionException {
        final SharedEffectiveModelContextFactory sharedSchemaContextFactory =
            new SharedEffectiveModelContextFactory(repository, config);
        final ListenableFuture<EffectiveModelContext> schemaContext =
                sharedSchemaContextFactory.createEffectiveModelContext(s1, s1, s2);
        assertNotNull(schemaContext.get());
    }

    @Test
    public void testSourceRegisteredWithDifferentSI() throws Exception {
        final YangTextSchemaSource source1 = YangTextSchemaSource.forResource("/ietf/ietf-inet-types@2010-09-24.yang");
        final YangTextSchemaSource source2 = YangTextSchemaSource.forResource("/ietf/iana-timezones@2012-07-09.yang");
        s1 = source1.getIdentifier();
        s2 = source2.getIdentifier();

        final SettableSchemaProvider<IRSchemaSource> provider =
                SharedSchemaRepositoryTest.getImmediateYangSourceProviderFromResource(
                    "/no-revision/imported@2012-12-12.yang");
        provider.setResult();
        provider.register(repository);

        // Register the same provider under source id without revision
        final SourceIdentifier sIdWithoutRevision = RevisionSourceIdentifier.create(provider.getId().getName());
        repository.registerSchemaSource(provider, PotentialSchemaSource.create(
                sIdWithoutRevision, IRSchemaSource.class, PotentialSchemaSource.Costs.IMMEDIATE.getValue()));

        final SharedEffectiveModelContextFactory sharedSchemaContextFactory =
            new SharedEffectiveModelContextFactory(repository, config);
        final ListenableFuture<EffectiveModelContext> schemaContext =
                sharedSchemaContextFactory.createEffectiveModelContext(sIdWithoutRevision, provider.getId());
        assertNotNull(schemaContext.get());
    }
}
