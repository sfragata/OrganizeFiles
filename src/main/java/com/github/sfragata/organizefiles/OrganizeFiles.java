package com.github.sfragata.organizefiles;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sfragata.organizefiles.resolver.FolderNameResolutionType;

public class OrganizeFiles {

    private static Logger logger = LoggerFactory.getLogger(OrganizeFiles.class);

    private static Set<PosixFilePermission> PERMS = PosixFilePermissions.fromString("rwxr-x---");

    private static FileAttribute<Set<PosixFilePermission>> ATTR = PosixFilePermissions.asFileAttribute(PERMS);

    public OrganizeFiles() {

    }

    private void createOrValidateTargetFolder(
        final Path folder)
        throws IOException {

        if (!Files.exists(folder, LinkOption.NOFOLLOW_LINKS)) {
            logger.info("Creating target folder '{}'", folder);
            Files.createDirectory(folder, ATTR);
        } else {
            Validate.isTrue(Files.isDirectory(folder, LinkOption.NOFOLLOW_LINKS), "target must be a directory");
        }
    }

    public void organize(
        final Path source,
        final Path target,
        final FolderNameResolutionType folderNameResolutionType)
        throws IOException {

        Validate.notNull(source, "the source couldn't be null");
        Validate.notNull(target, "the target couldn't be null");
        Validate.isTrue(Files.isDirectory(source, LinkOption.NOFOLLOW_LINKS), "the source must be a directory");

        createOrValidateTargetFolder(target);

        logger.info("The folders will be created {}", folderNameResolutionType.name());

        Files.walk(source, FileVisitOption.FOLLOW_LINKS).filter(p -> !Files.isDirectory(p, LinkOption.NOFOLLOW_LINKS))
            .parallel().forEach((
                eachPath) -> {
                try {

                    final String fileName = folderNameResolutionType.getFolderNameResolver().resolve(eachPath);

                    final Path targetPath = Paths.get(target.toString(), fileName);

                    createOrValidateTargetFolder(targetPath);

                    logger.info("Copying file {} to {}", eachPath.toString(), targetPath.toString());

                    Files.copy(eachPath, Paths.get(targetPath.toString(), eachPath.getFileName().toString()),
                        COPY_ATTRIBUTES);

                    // logger.info("Attributes of {}", eachPath.toString());
                    // logger.info("Creation {}",
                    // formatDate(attr.creationTime().toMillis()));
                    // logger.info("Last modified {}",
                    // formatDate(attr.lastModifiedTime().toMillis()));
                    // logger.info("Size {} \n\n", attr.size());

                } catch (final Exception e) {
                    logger.error(String.format("Error in file %s", eachPath.toString()), e);
                }
            });
    }

}
