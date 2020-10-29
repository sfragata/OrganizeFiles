package com.github.sfragata.organizefiles.resolver;

import java.util.stream.Stream;

import org.apache.commons.lang3.Validate;

public enum FolderNameResolutionType {
        BY_FILE_ATTRIBUTE_DATE(new FolderNameByDateAttributesResolver()),
        BY_FILE_NAME_WITH_DATE(new FolderNameByNameResolver());

    private final FolderNameResolver folderNameResolver;

    FolderNameResolutionType(final FolderNameResolver pFolderNameResolver) {
        this.folderNameResolver = pFolderNameResolver;
    }

    public FolderNameResolver getResolver() {

        return this.folderNameResolver;
    }

    public static FolderNameResolutionType findByName(
        final String pResolveType) {

        Validate.notNull(pResolveType);

        return Stream.of(FolderNameResolutionType.values())
            .filter(resolveType -> resolveType.name().equals(pResolveType)).findFirst()
            .orElse(FolderNameResolutionType.BY_FILE_ATTRIBUTE_DATE);

    }
}
