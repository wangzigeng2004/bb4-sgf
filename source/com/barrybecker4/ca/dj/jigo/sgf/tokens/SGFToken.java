/*
 * Copyright (C) 2001 by Dave Jarvis
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * Online at: http://www.gnu.org/copyleft/gpl.html
 */

package com.barrybecker4.ca.dj.jigo.sgf.tokens;

import com.barrybecker4.ca.dj.jigo.sgf.SGFException;

import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * The big daddy of all SGFTokens.  All SGFTokens are subclassed from this
 * token.  All subclasses must know how to parse themselves.  The most common
 * subclass is the generic TextToken (the superclass to nearly all other
 * tokens).
 */
public abstract class SGFToken
{
  public SGFToken() { }

  /**
   * Called to parse itself.  Subclasses are responsible for reading
   * everything up to and including the final closing ']'.  (Some
   * classes have multiple list items, which means multiple closing ']'.)
   *
   * @param st - The SGF stream from which characters can be read.
   */
  public boolean parse( StreamTokenizer st )
    throws IOException, SGFException
  {
    if( st.nextToken() != (int)'[' )
      return false;

     boolean result = false;

    // While parsing a token, some special characters have to become
    // "less special".  This is due to the definition of "simpletext" being
    // not so simple.
    //
    st.wordChars( '(', ')' );
    st.eolIsSignificant( true );

    try
    {
      result = parseContent( st );
    }
    catch( IOException ioe ) { }

    st.ordinaryChars( '(', ')' );
    st.eolIsSignificant( false );

    return result;
  }

  /**
   * Subclasses must read all of the content and the final closing bracket.
   * This is cool because all tokens end with a closing bracket anyway, which
   * tells them when to stop reading.  That's why there isn't a symmetrical
   * read and validate (int)']' in the parse method, above.
   *
   * @param st - The SGF stream from which characters can be read.
   * @throws SGFException - Something quite nasty happened.
   */
  protected abstract boolean parseContent( StreamTokenizer st )
    throws IOException, SGFException;
}

