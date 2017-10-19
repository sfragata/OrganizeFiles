package com.github.sfragata.organizefiles;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import com.github.sfragata.organizefiles.resolver.FolderNameResolutionType;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

public class OrganizeFilesUnitTest {

    private static final String EXPECTED_TARGET_FOLDER_YYYY_MM_DD = "2017-10-17";

    private static final String FILE_SOURCE = "20171017.jpg";

    private static final String SOURCE_FOLDER = "/source";

    private static final String TARGET_FOLDER = "/target";

    private final OrganizeFiles organizeFiles = new OrganizeFiles();

    private final FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());

    @Test
    public void givenExistentTargetFolderWhenOrganizeFilesWithFolderNameResolutionByFileNameWithDateThenSuccess()
        throws IOException {

        final Path folderSource = this.fileSystem.getPath(SOURCE_FOLDER);
        Files.createDirectory(folderSource);
        final Path fileSource = this.fileSystem.getPath(SOURCE_FOLDER, FILE_SOURCE);
        Files.createFile(fileSource);
        final Path folderTarget = this.fileSystem.getPath(TARGET_FOLDER);
        Files.createDirectory(folderTarget);

        this.organizeFiles.organize(folderSource, folderTarget, FolderNameResolutionType.BY_FILE_NAME_WITH_DATE);

        final Path pathResult = this.fileSystem.getPath(TARGET_FOLDER, EXPECTED_TARGET_FOLDER_YYYY_MM_DD, FILE_SOURCE);

        assertTrue(Files.exists(pathResult));

    }

    @Test
    public void givenNonExistentTargetFolderWhenOrganizeFilesWithFolderNameResolutionByFileNameWithDateThenSuccess()
        throws IOException {

        final Path folderSource = this.fileSystem.getPath(SOURCE_FOLDER);
        Files.createDirectory(folderSource);
        final Path fileSource = this.fileSystem.getPath(SOURCE_FOLDER, FILE_SOURCE);
        Files.createFile(fileSource);
        final Path folderTarget = this.fileSystem.getPath(TARGET_FOLDER);

        this.organizeFiles.organize(folderSource, folderTarget, FolderNameResolutionType.BY_FILE_NAME_WITH_DATE);

        final Path pathResult = this.fileSystem.getPath(TARGET_FOLDER, EXPECTED_TARGET_FOLDER_YYYY_MM_DD, FILE_SOURCE);

        assertTrue(Files.exists(pathResult));

    }

}
