/*
 * Copyright (c) 2017 Pantheon Technologies, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.rfc7950.reactor;

import static org.opendaylight.yangtools.yang.common.YangVersion.VERSION_1;
import static org.opendaylight.yangtools.yang.common.YangVersion.VERSION_1_1;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yangtools.yang.common.YangVersion;
import org.opendaylight.yangtools.yang.parser.openconfig.stmt.OpenConfigVersionSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.namespace.ModuleQNameToPrefix;
import org.opendaylight.yangtools.yang.parser.rfc7950.namespace.YangNamespaceContextNamespace;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.XPathSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.augment.AugmentImplicitHandlingNamespace;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.augment.AugmentStatementRFC6020Support;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.augment.AugmentStatementRFC7950Support;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.deviate.DeviateStatementRFC6020Support;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.deviate.DeviateStatementRFC7950Support;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.extension.ExtensionStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.if_feature.IfFeatureStatementRFC6020Support;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.if_feature.IfFeatureStatementRFC7950Support;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.import_.ImportStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.import_.ImportedVersionNamespace;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.key.KeyStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.leaf_list.LeafListStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.list.ConfigListWarningNamespace;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.list.ListStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.ActionStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.AnydataStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.AnyxmlStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.ArgumentStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.BaseStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.BelongsToStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.BitStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.CaseStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.ChoiceStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.ConfigStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.ContactStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.ContainerStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.DefaultStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.DescriptionStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.DeviationStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.EnumStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.ErrorAppTagStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.ErrorMessageStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.FeatureStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.FractionDigitsStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.GroupingStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.IdentityStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.IncludeStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.InputStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.LeafStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.LengthStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.MandatoryStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.MaxElementsStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.MinElementsStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.ModifierStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.NamespaceStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.OrderedByStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.OrganizationStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.OutputStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.PositionStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.PrefixStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.PresenceStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.RangeStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.ReferenceStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.RequireInstanceStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.RevisionDateStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.RevisionStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.RpcStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.StatusStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.TypedefStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.UniqueStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.UnitsStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.ValueStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.WhenStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.YangVersionStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.meta.YinElementStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.module.ModuleStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.module.QNameModuleNamespace;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.must.MustStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.notification.NotificationStatementRFC6020Support;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.notification.NotificationStatementRFC7950Support;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.path.PathStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.pattern.PatternStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.refine.RefineStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.refine.RefineTargetNamespace;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.submodule.SubmoduleStatementSupport;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.type.BaseTypeNamespace;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.type.TypeStatementRFC6020Support;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.type.TypeStatementRFC7950Support;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.uses.SourceGroupingNamespace;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.uses.UsesStatementSupport;
import org.opendaylight.yangtools.yang.parser.spi.ExtensionNamespace;
import org.opendaylight.yangtools.yang.parser.spi.FeatureNamespace;
import org.opendaylight.yangtools.yang.parser.spi.GroupingNamespace;
import org.opendaylight.yangtools.yang.parser.spi.IdentityNamespace;
import org.opendaylight.yangtools.yang.parser.spi.ModuleNamespace;
import org.opendaylight.yangtools.yang.parser.spi.NamespaceToModule;
import org.opendaylight.yangtools.yang.parser.spi.PreLinkageModuleNamespace;
import org.opendaylight.yangtools.yang.parser.spi.SchemaTreeNamespace;
import org.opendaylight.yangtools.yang.parser.spi.SubmoduleNamespace;
import org.opendaylight.yangtools.yang.parser.spi.TypeNamespace;
import org.opendaylight.yangtools.yang.parser.spi.meta.ModelProcessingPhase;
import org.opendaylight.yangtools.yang.parser.spi.meta.SemanticVersionModuleNamespace;
import org.opendaylight.yangtools.yang.parser.spi.meta.SemanticVersionNamespace;
import org.opendaylight.yangtools.yang.parser.spi.meta.StatementDefinitionNamespace;
import org.opendaylight.yangtools.yang.parser.spi.meta.StatementSupportBundle;
import org.opendaylight.yangtools.yang.parser.spi.source.BelongsToModuleContext;
import org.opendaylight.yangtools.yang.parser.spi.source.BelongsToPrefixToModuleCtx;
import org.opendaylight.yangtools.yang.parser.spi.source.BelongsToPrefixToModuleName;
import org.opendaylight.yangtools.yang.parser.spi.source.ImpPrefixToNamespace;
import org.opendaylight.yangtools.yang.parser.spi.source.ImportPrefixToModuleCtx;
import org.opendaylight.yangtools.yang.parser.spi.source.ImportPrefixToSemVerSourceIdentifier;
import org.opendaylight.yangtools.yang.parser.spi.source.ImportedModuleContext;
import org.opendaylight.yangtools.yang.parser.spi.source.IncludedModuleContext;
import org.opendaylight.yangtools.yang.parser.spi.source.IncludedSubmoduleNameToModuleCtx;
import org.opendaylight.yangtools.yang.parser.spi.source.ModuleCtxToModuleQName;
import org.opendaylight.yangtools.yang.parser.spi.source.ModuleCtxToSourceIdentifier;
import org.opendaylight.yangtools.yang.parser.spi.source.ModuleNameToModuleQName;
import org.opendaylight.yangtools.yang.parser.spi.source.ModuleNameToNamespace;
import org.opendaylight.yangtools.yang.parser.spi.source.ModuleNamespaceForBelongsTo;
import org.opendaylight.yangtools.yang.parser.spi.source.ModuleQNameToModuleName;
import org.opendaylight.yangtools.yang.parser.spi.source.ModulesDeviatedByModules;
import org.opendaylight.yangtools.yang.parser.spi.source.PrefixToModule;
import org.opendaylight.yangtools.yang.parser.spi.source.SupportedFeaturesNamespace;
import org.opendaylight.yangtools.yang.parser.spi.validation.ValidationBundlesNamespace;
import org.opendaylight.yangtools.yang.parser.spi.validation.ValidationBundlesNamespace.ValidationBundleType;
import org.opendaylight.yangtools.yang.parser.stmt.reactor.CrossSourceStatementReactor;
import org.opendaylight.yangtools.yang.xpath.api.YangXPathParserFactory;

/**
 * Utility class holding entrypoints for assembling RFC6020/RFC7950 statement {@link CrossSourceStatementReactor}s.
 *
 * @author Robert Varga
 */
@Beta
public final class RFC7950Reactors {
    private static final ImmutableSet<YangVersion> SUPPORTED_VERSIONS = Sets.immutableEnumSet(VERSION_1, VERSION_1_1);

    private static final StatementSupportBundle INIT_BUNDLE = StatementSupportBundle.builder(SUPPORTED_VERSIONS)
            .addSupport(ValidationBundlesNamespace.BEHAVIOUR)
            .addSupport(SupportedFeaturesNamespace.BEHAVIOUR)
            .addSupport(ModulesDeviatedByModules.BEHAVIOUR)
            .build();

    private static final StatementSupportBundle PRE_LINKAGE_BUNDLE = StatementSupportBundle.derivedFrom(INIT_BUNDLE)
            .addVersionSpecificSupport(VERSION_1, ModuleStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, ModuleStatementSupport.rfc7950Instance())
            .addVersionSpecificSupport(VERSION_1, SubmoduleStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, SubmoduleStatementSupport.rfc7950Instance())
            .addSupport(NamespaceStatementSupport.getInstance())
            .addVersionSpecificSupport(VERSION_1, ImportStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, ImportStatementSupport.rfc7950Instance())
            .addVersionSpecificSupport(VERSION_1, IncludeStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, IncludeStatementSupport.rfc7950Instance())
            .addSupport(BelongsToStatementSupport.getInstance())
            .addSupport(PrefixStatementSupport.getInstance())
            .addSupport(YangVersionStatementSupport.getInstance())
            .addSupport(RevisionStatementSupport.getInstance())
            .addSupport(RevisionDateStatementSupport.getInstance())
            .addSupport(ModuleNameToNamespace.BEHAVIOUR)
            .addSupport(PreLinkageModuleNamespace.BEHAVIOUR)
            .addSupport(ImpPrefixToNamespace.BEHAVIOUR)
            .addSupport(ModuleCtxToModuleQName.BEHAVIOUR)
            .addSupport(QNameModuleNamespace.BEHAVIOUR)
            .addSupport(ImportedVersionNamespace.BEHAVIOUR)
            .build();

    private static final StatementSupportBundle LINKAGE_BUNDLE = StatementSupportBundle
            .derivedFrom(PRE_LINKAGE_BUNDLE)
            .addSupport(DescriptionStatementSupport.getInstance())
            .addSupport(ReferenceStatementSupport.getInstance())
            .addSupport(ContactStatementSupport.getInstance())
            .addSupport(OrganizationStatementSupport.getInstance())
            .addSupport(ModuleNamespace.BEHAVIOUR)
            .addSupport(ModuleNamespaceForBelongsTo.BEHAVIOUR)
            .addSupport(SubmoduleNamespace.BEHAVIOUR)
            .addSupport(NamespaceToModule.BEHAVIOUR)
            .addSupport(ModuleNameToModuleQName.BEHAVIOUR)
            .addSupport(ModuleCtxToSourceIdentifier.BEHAVIOUR)
            .addSupport(ModuleQNameToModuleName.BEHAVIOUR)
            .addSupport(PrefixToModule.BEHAVIOUR)
            .addSupport(ImportedModuleContext.BEHAVIOUR)
            .addSupport(IncludedModuleContext.BEHAVIOUR)
            .addSupport(IncludedSubmoduleNameToModuleCtx.BEHAVIOUR)
            .addSupport(ImportPrefixToModuleCtx.BEHAVIOUR)
            .addSupport(BelongsToPrefixToModuleCtx.BEHAVIOUR)
            .addSupport(ModuleQNameToPrefix.BEHAVIOUR)
            .addSupport(BelongsToModuleContext.BEHAVIOUR)
            .addSupport(BelongsToPrefixToModuleName.BEHAVIOUR)
            .build();

    private static final StatementSupportBundle STMT_DEF_BUNDLE = StatementSupportBundle
            .derivedFrom(LINKAGE_BUNDLE)
            .addSupport(YinElementStatementSupport.getInstance())
            .addSupport(ArgumentStatementSupport.getInstance())
            .addSupport(ExtensionStatementSupport.getInstance())
            .addSupport(SchemaTreeNamespace.getInstance())
            .addSupport(ExtensionNamespace.BEHAVIOUR)
            .addSupport(TypedefStatementSupport.getInstance())
            .addSupport(TypeNamespace.BEHAVIOUR)
            .addVersionSpecificSupport(VERSION_1, IdentityStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, IdentityStatementSupport.rfc7950Instance())
            .addSupport(IdentityNamespace.BEHAVIOUR)
            .addSupport(DefaultStatementSupport.getInstance())
            .addSupport(StatusStatementSupport.getInstance())
            .addSupport(BaseTypeNamespace.BEHAVIOUR)
            .addVersionSpecificSupport(VERSION_1, TypeStatementRFC6020Support.getInstance())
            .addVersionSpecificSupport(VERSION_1_1, TypeStatementRFC7950Support.getInstance())
            .addSupport(UnitsStatementSupport.getInstance())
            .addSupport(RequireInstanceStatementSupport.getInstance())
            .addVersionSpecificSupport(VERSION_1, BitStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, BitStatementSupport.rfc7950Instance())
            .addSupport(PathStatementSupport.strictInstance())
            .addVersionSpecificSupport(VERSION_1, EnumStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, EnumStatementSupport.rfc7950Instance())
            .addSupport(LengthStatementSupport.getInstance())
            .addVersionSpecificSupport(VERSION_1, PatternStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, PatternStatementSupport.rfc7950Instance())
            .addVersionSpecificSupport(VERSION_1_1, ModifierStatementSupport.getInstance())
            .addSupport(RangeStatementSupport.getInstance())
            .addSupport(KeyStatementSupport.getInstance())
            .addVersionSpecificSupport(VERSION_1, ContainerStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, ContainerStatementSupport.rfc7950Instance())
            .addVersionSpecificSupport(VERSION_1, GroupingStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, GroupingStatementSupport.rfc7950Instance())
            .addVersionSpecificSupport(VERSION_1, ListStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, ListStatementSupport.rfc7950Instance())
            .addSupport(ConfigListWarningNamespace.BEHAVIOUR)
            .addSupport(UniqueStatementSupport.getInstance())
            .addVersionSpecificSupport(VERSION_1_1, ActionStatementSupport.getInstance())
            .addVersionSpecificSupport(VERSION_1, RpcStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, RpcStatementSupport.rfc7950Instance())
            .addVersionSpecificSupport(VERSION_1, InputStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, InputStatementSupport.rfc7950Instance())
            .addVersionSpecificSupport(VERSION_1, OutputStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, OutputStatementSupport.rfc7950Instance())
            .addVersionSpecificSupport(VERSION_1, NotificationStatementRFC6020Support.getInstance())
            .addVersionSpecificSupport(VERSION_1_1, NotificationStatementRFC7950Support.getInstance())
            .addSupport(FractionDigitsStatementSupport.getInstance())
            .addSupport(BaseStatementSupport.getInstance())
            .addSupport(StatementDefinitionNamespace.BEHAVIOUR)
            .build();


    private RFC7950Reactors() {
        // Hidden on purpose
    }

    /**
     * Returns a pre-built {@link CrossSourceStatementReactor} supporting RFC6020 and RFC7950, along with OpenConfig
     * semantic version extension. This is useful for parsing near-vanilla YANG models while providing complete
     * support for semantic versions.
     *
     * @return A shared reactor instance.
     */
    public static @NonNull CrossSourceStatementReactor defaultReactor() {
        return ServiceLoaderState.DefaultReactor.INSTANCE;
    }

    /**
     * Returns a partially-configured {@link CustomCrossSourceStatementReactorBuilder}, with RFC6020/RFC7950
     * and OpenConfig semantic version support enabled.
     *
     * @return A new {@link CustomCrossSourceStatementReactorBuilder}.
     */
    public static @NonNull CustomCrossSourceStatementReactorBuilder defaultReactorBuilder() {
        return addExtensions(vanillaReactorBuilder());
    }

    public static @NonNull CustomCrossSourceStatementReactorBuilder defaultReactorBuilder(
            final YangXPathParserFactory xpathFactory) {
        return addExtensions(vanillaReactorBuilder(xpathFactory));
    }

    private static @NonNull CustomCrossSourceStatementReactorBuilder addExtensions(
            final @NonNull CustomCrossSourceStatementReactorBuilder builder) {
        return builder
                // Semantic version support
                .addStatementSupport(ModelProcessingPhase.SOURCE_LINKAGE, OpenConfigVersionSupport.getInstance())
                .addNamespaceSupport(ModelProcessingPhase.SOURCE_LINKAGE, SemanticVersionNamespace.BEHAVIOUR)
                .addNamespaceSupport(ModelProcessingPhase.SOURCE_LINKAGE, SemanticVersionModuleNamespace.BEHAVIOUR)
                .addNamespaceSupport(ModelProcessingPhase.SOURCE_LINKAGE,
                    ImportPrefixToSemVerSourceIdentifier.BEHAVIOUR);
    }

    /**
     * Returns a pre-built {@link CrossSourceStatementReactor} supporting both RFC6020 and RFC7950. This is useful
     * for parsing vanilla YANG models without any semantic support for extensions. Notably missing is the semantic
     * version extension, hence attempts to use semantic version mode will cause failures.
     *
     * @return A shared reactor instance.
     */
    public static @NonNull CrossSourceStatementReactor vanillaReactor() {
        return ServiceLoaderState.VanillaReactor.INSTANCE;
    }

    /**
     * Returns a partially-configured {@link CustomCrossSourceStatementReactorBuilder}, with vanilla RFC6020/RFC7950
     * support enabled.
     *
     * @return A new {@link CustomCrossSourceStatementReactorBuilder}.
     */
    public static @NonNull CustomCrossSourceStatementReactorBuilder vanillaReactorBuilder() {
        return vanillaReactorBuilder(ServiceLoaderState.XPath.INSTANCE);
    }

    public static @NonNull CustomCrossSourceStatementReactorBuilder vanillaReactorBuilder(
            final @NonNull YangXPathParserFactory xpathFactory) {
        return vanillaReactorBuilder(new XPathSupport(xpathFactory));
    }

    private static @NonNull CustomCrossSourceStatementReactorBuilder vanillaReactorBuilder(
            final @NonNull XPathSupport xpathSupport) {
        final StatementSupportBundle fullDeclarationBundle = fullDeclarationBundle(xpathSupport);
        return new CustomCrossSourceStatementReactorBuilder(SUPPORTED_VERSIONS)
                .addAllSupports(ModelProcessingPhase.INIT, INIT_BUNDLE)
                .addAllSupports(ModelProcessingPhase.SOURCE_PRE_LINKAGE, PRE_LINKAGE_BUNDLE)
                .addAllSupports(ModelProcessingPhase.SOURCE_LINKAGE, LINKAGE_BUNDLE)
                .addAllSupports(ModelProcessingPhase.STATEMENT_DEFINITION, STMT_DEF_BUNDLE)
                .addAllSupports(ModelProcessingPhase.FULL_DECLARATION, fullDeclarationBundle)
                .addAllSupports(ModelProcessingPhase.EFFECTIVE_MODEL, fullDeclarationBundle)
                .addValidationBundle(ValidationBundleType.SUPPORTED_REFINE_SUBSTATEMENTS,
                    YangValidationBundles.SUPPORTED_REFINE_SUBSTATEMENTS)
                .addValidationBundle(ValidationBundleType.SUPPORTED_AUGMENT_TARGETS,
                    YangValidationBundles.SUPPORTED_AUGMENT_TARGETS)

                // FIXME: 7.0.0: we do not seem to need this validation bundle
                .addValidationBundle(ValidationBundleType.SUPPORTED_CASE_SHORTHANDS,
                    YangValidationBundles.SUPPORTED_CASE_SHORTHANDS)

                .addValidationBundle(ValidationBundleType.SUPPORTED_DATA_NODES,
                    YangValidationBundles.SUPPORTED_DATA_NODES);
    }

    private static @NonNull StatementSupportBundle fullDeclarationBundle(final XPathSupport xpathSupport) {
        return StatementSupportBundle
            .derivedFrom(STMT_DEF_BUNDLE)
            .addSupport(LeafStatementSupport.getInstance())
            .addSupport(ConfigStatementSupport.getInstance())
            .addSupport(DeviationStatementSupport.getInstance())
            .addVersionSpecificSupport(VERSION_1, DeviateStatementRFC6020Support.getInstance())
            .addVersionSpecificSupport(VERSION_1_1, DeviateStatementRFC7950Support.getInstance())
            .addVersionSpecificSupport(VERSION_1, ChoiceStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, ChoiceStatementSupport.rfc7950Instance())
            .addVersionSpecificSupport(VERSION_1, CaseStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, CaseStatementSupport.rfc7950Instance())
            .addSupport(MustStatementSupport.createInstance(xpathSupport))
            .addSupport(MandatoryStatementSupport.getInstance())
            .addSupport(AnyxmlStatementSupport.getInstance())
            .addVersionSpecificSupport(VERSION_1_1, AnydataStatementSupport.getInstance())
            .addSupport(FeatureNamespace.BEHAVIOUR)
            .addVersionSpecificSupport(VERSION_1, IfFeatureStatementRFC6020Support.getInstance())
            .addVersionSpecificSupport(VERSION_1_1, IfFeatureStatementRFC7950Support.getInstance())
            .addSupport(GroupingNamespace.BEHAVIOUR)
            .addSupport(SourceGroupingNamespace.BEHAVIOUR)
            .addSupport(UsesStatementSupport.getInstance())
            .addSupport(ErrorMessageStatementSupport.getInstance())
            .addSupport(ErrorAppTagStatementSupport.getInstance())
            .addVersionSpecificSupport(VERSION_1, LeafListStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, LeafListStatementSupport.rfc7950Instance())
            .addSupport(PresenceStatementSupport.getInstance())
            .addSupport(MaxElementsStatementSupport.getInstance())
            .addSupport(MinElementsStatementSupport.getInstance())
            .addSupport(OrderedByStatementSupport.getInstance())
            .addSupport(WhenStatementSupport.createInstance(xpathSupport))
            .addSupport(AugmentImplicitHandlingNamespace.BEHAVIOUR)
            .addVersionSpecificSupport(VERSION_1, AugmentStatementRFC6020Support.getInstance())
            .addVersionSpecificSupport(VERSION_1_1, AugmentStatementRFC7950Support.getInstance())
            .addSupport(RefineTargetNamespace.BEHAVIOUR)
            .addVersionSpecificSupport(VERSION_1, RefineStatementSupport.rfc6020Instance())
            .addVersionSpecificSupport(VERSION_1_1, RefineStatementSupport.rfc7950Instance())
            .addSupport(FeatureStatementSupport.getInstance())
            .addSupport(PositionStatementSupport.getInstance())
            .addSupport(ValueStatementSupport.getInstance())
            .addSupport(YangNamespaceContextNamespace.BEHAVIOUR)
            .build();
    }
}
