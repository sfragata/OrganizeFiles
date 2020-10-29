package com.github.sfragata.organizefiles.resolver;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import com.github.sfragata.organizefiles.resolver.exception.UnresolvedFolderNameException;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

public class FolderNameByDateAttributesResolverUnitTest {

    private static final String FILE_NAME = "filename.jpg";

    private final FolderNameResolutionType folderNameResolutionType = FolderNameResolutionType.BY_FILE_ATTRIBUTE_DATE;

    private final FileSystem jimfileSystem = Jimfs.newFileSystem(Configuration.unix());

    @Test
    public void givenValidNamePathWhenResolveThenSuccess()
        throws IOException {

        final Path sourceFile = this.jimfileSystem.getPath(FILE_NAME);
        Files.createFile(sourceFile);

        final Instant instant = Instant.now();
        final LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        final String dateString = date.format(DateTimeFormatter.ISO_DATE);

        final String result = this.folderNameResolutionType.getResolver().resolve(sourceFile);

        assertEquals(dateString, result);

    }

    @SuppressWarnings("unchecked")
    @Test(expected = UnresolvedFolderNameException.class)
    public void givenErrorReadingAttributesWhenResolveThenThrowUnresolvedFolderNameException()
        throws IOException {

        final FileSystem fileSystem = mock(FileSystem.class);
        final FileSystemProvider fileSystemProvider = mock(FileSystemProvider.class);

        final Path sourceFile = mock(Path.class);
        when(sourceFile.getFileSystem()).thenReturn(fileSystem);
        when(fileSystem.provider()).thenReturn(fileSystemProvider);
        when(fileSystemProvider.readAttributes(any(Path.class), any(Class.class))).thenThrow(new IOException());

        this.folderNameResolutionType.getResolver().resolve(sourceFile);

    }
}
