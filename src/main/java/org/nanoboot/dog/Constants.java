///////////////////////////////////////////////////////////////////////////////////////////////
// dog: Tool generating documentation.
// Copyright (C) 2023-2023 the original author or authors.
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; version 2
// of the License only.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
///////////////////////////////////////////////////////////////////////////////////////////////

package org.nanoboot.dog;

import java.text.SimpleDateFormat;

/**
 *
 * @author pc00289
 */
public class Constants {
    public static final String INDEXHTML = "index.html";
    public static final SimpleDateFormat YYYYMMDDHHMMSSZ_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    public static String TEST_ASCIIDOC = """
                   
                       = The Dangers of Wolpertingers
                       :url-wolpertinger: https://en.wikipedia.org/wiki/Wolpertinger
                       :linkcss:
                       :stylesheet: dog.css
                       
                       
                       
                       +++
                       title = "About"
                       date = "2019-02-28"
                       menu = "main"
                       +++
                       
                       
                       
                       
                       
                       == Asciidoctor Demo
                       
                       ////
                       Big ol' comment
                       
                       sittin' right 'tween this here title 'n header metadata
                       ////
                       Dan Allen <thedoc@asciidoctor.org>
                       :description: A demo of Asciidoctor. This document +
                                     exercises numerous features of AsciiDoc +
                                     to test Asciidoctor compliance.
                       :library: Asciidoctor
                       ifdef::asciidoctor[]
                       :source-highlighter: coderay
                       endif::asciidoctor[]
                       :idprefix:
                       :stylesheet: asciidoc.css
                       :imagesdir: images
                       //:backend: docbook45
                       //:backend: html5
                       //:doctype: book
                       //:sectids!:
                       :plus: &#43;
                       
                       [role='lead']
                       This is a demonstration of {library}. And this is the preamble of this document.
                       
                       [[purpose]]
                       .Purpose
                       ****
                       This document exercises many of the features of AsciiDoc to test the {library} implementation.
                       ****
                       
                       TIP: If you want the output to look familiar, copy (or link) the AsciiDoc stylesheet, asciidoc.css, to the output directory.
                       
                       NOTE: Items marked with TODO are either not yet supported or a work in progress.
                       
                       [[first,First Steps]]
                       == First Steps with http://asciidoc.org[AsciiDoc]
                       
                       .Inline markup
                       * single quotes around a phrase place 'emphasis'
                       * astericks around a phrase make the text *bold*
                       * double astericks around one or more **l**etters in a word make those letters bold
                       * double underscore around a __sub__string in a word emphasize that substring
                       * use carrots around characters to make them ^super^script
                       * use tildes around characters to make them ~sub~script
                       ifdef::basebackend-html[]
                       * to pass through +++<u>HTML</u>+++ directly, surround the text with triple plus
                       endif::basebackend-html[]
                       ifdef::basebackend-docbook[]
                       * to pass through +++<constant>XML</constant>+++ directly, surround the text with triple plus
                       endif::basebackend-docbook[]
                       
                       // separate two adjacent lists using a line comment (only the leading // is required)
                       
                       - characters can be escaped using a {backslash}
                       * for instance, you can escape a quote inside emphasized text like 'Here\'s Johnny!'
                       - you can safely use reserved XML characters like <, > and &, which are escaped when rendering
                       - force a space{sp}between inline elements using the {sp} attribute
                       - hold text together with an intrinsic non-breaking{nbsp}space attribute, {nbsp}
                       - handle words with unicode characters like in the name Gregory Romé
                       - claim your copyright (C), registered trademark (R) or trademark (TM)
                       
                       You can write text http://example.com[with inline links], optionally{sp}using an explicit link:http://example.com[link prefix]. In either case, the link can have a http://example.com?foo=bar&lang=en[query string].
                       
                       If you want to break a line +
                       just end it in a {plus} sign +
                       and continue typing on the next line.
                       
                       === Lists Upon Lists
                       
                       .Adjacent lists
                       * this list
                       * should join
                       
                       * to have
                       * four items
                       
                       [[numbered]]
                       .Numbered lists
                       . These items
                       . will be auto-numbered
                       .. and can be nested
                       . A numbered list can nest
                       * unordered
                       * list
                       * items
                       
                       .Statement
                       I swear I left it in 'Guy\'s' car. Let\'s go look for it.
                       
                       [[defs]]
                       term::
                         definition
                       line two
                       [[another_term]]another term::
                       
                         another definition, which can be literal (indented) or regular paragraph
                       
                       This should be a standalone paragraph, not grabbed by the definition list.
                       
                       [[nested]]
                       * first level
                       written on two lines
                       * first level
                       +
                       ....
                       with this literal text
                       ....
                       +
                       ** second level
                       *** third level
                       - fourth level
                       * back to +
                       first level
                       
                       // this is just a comment
                       
                       Let's make a horizontal rule...
                       
                       '''
                       
                       then take a break.
                       
                       ////
                       We'll be right with you...
                       
                       after this brief interruption.
                       ////
                       
                       == We're back!
                       
                       Want to see a image:tiger.png[Tiger]?
                       
                       Do you feel safer with the tiger in a box?
                       
                       .Tiger in a box
                       image::tiger.png[]
                       
                       == Included Section
                       
                       Look, I came from out of the [blue]#blue#!
                       
                       --
                       I'm keepin' it open.
                       
                       An 'open block', like this one, can contain other blocks.
                       
                       It can also act as any other block. (TODO)
                       --
                       
                       
                       .Asciidoctor usage example, should contain 3 lines
                       [source, ruby]
                       ----
                       doc = Asciidoctor::Document.new("*This* is it!", :header_footer => false)
                       
                       puts doc.render
                       ----
                       
                       // FIXME: use ifdef to show output according to backend
                       Here's what it outputs (using the built-in templates):
                       
                       ....
                       <div class="paragraph">
                         <p><strong>This</strong> is it!</p>
                       </div>
                       ....
                       
                       === ``Quotes''
                       
                       ____
                       AsciiDoc is 'so' *powerful*!
                       ____
                       
                       This verse comes to mind.
                       
                       [verse]
                       La la la
                       
                       Here's another quote:
                       
                       [quote, Sir Arthur Conan Doyle, The Adventures of Sherlock Holmes]
                       ____
                       When you have eliminated all which is impossible, then whatever remains, however improbable, must be the truth.
                       ____
                       
                       Getting Literal [[literally]]
                       -----------------------------
                       
                        Want to get literal? Just prefix a line with a space (just one will do).
                       
                       ....
                       I'll join that party, too.
                       ....
                       
                       We forgot to mention in <<numbered>> that you can change the numbering style.
                       
                       .. first item (yeah!)
                       .. second item, looking `so mono`
                       .. third item, +mono+ it is!
                       
                       // This attribute line will get reattached to the next block
                       // despite being followed by a trailing blank line
                       [id='wrapup']
                       
                       == Wrap-up
                       
                       
                       
                       NOTE: AsciiDoc is quite cool, you should try it!
                       
                       [TIP]
                       .Info
                       =====
                       Go to this URL to learn more about it:
                       
                       * http://asciidoc.org
                       
                       Or you could return to the xref:first[] or <<purpose,Purpose>>.
                       =====
                       
                       Here's a reference to the definition of <<another_term>>, in case you forgot it.
                       
                       [NOTE]
                       One more thing. Happy documenting!
                       
                       [[google]]When all else fails, head over to <http://google.com>.
                       
                       """;
}
