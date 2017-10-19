/**
 *
 */
package com.github.sfragata.organizefiles.resolver.exception;

/**
 * @author Silvio Fragata da Silva
 *
 */
public class UnresolvedFolderNameException
    extends RuntimeException {

    private static final long serialVersionUID = -482645239434646075L;

    public UnresolvedFolderNameException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnresolvedFolderNameException(final String message) {
        super(message);
    }

}
