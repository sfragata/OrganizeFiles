package com.github.sfragata.organizefiles.resolver;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;

import org.junit.Test;

public class FolderNameByNameResolverUnitTest {

    private static final String EXPECTED_FOLDER_RESULT = "2017-10-17";

    private static final String INVALID_FILE_NAME_WITH_DATE = "201.jpg";

    private static final String FILE_NAME_WITH_DATE = "20171017.jpg";

    private final FolderNameResolutionType folderNameResolutionType = FolderNameResolutionType.BY_FILE_NAME_WITH_DATE;

    @Test
    public void givenValidNamePathWhenResolveThenSuccess() {

        final String result =
            this.folderNameResolutionType.getFolderNameResolver().resolve(Paths.get(FILE_NAME_WITH_DATE));

        assertEquals(EXPECTED_FOLDER_RESULT, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenInvalidNamePathWhenResolveThenThrowIllegalArgumentException() {

        this.folderNameResolutionType.getFolderNameResolver().resolve(Paths.get(INVALID_FILE_NAME_WITH_DATE));

    }

    @Test(expected = NullPointerException.class)
    public void givenNullPathWhenResolveThenThrowNullPointerException() {

        this.folderNameResolutionType.getFolderNameResolver().resolve(null);

    }

}
