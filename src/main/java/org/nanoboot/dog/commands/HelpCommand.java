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
package org.nanoboot.dog.commands;

import org.nanoboot.dog.Command;
import org.nanoboot.dog.DogArgs;

/**
 *
 * @author pc00289
 */
public class HelpCommand implements Command {

    public HelpCommand() {
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void run(DogArgs dogArgs) {
        String str = """
    NAME
        dog - Documentation Generator - a static web site documentation generator (from Asciidoc).
                           
    SYNOPSIS
        dog [command] [options]
        If no command is provided, then the default command gen is used. This means, if you run "dog", it is the same, as to run "dog gen".
                           
    DESCRIPTION
        Dog generates static sites from Asciidoc.
                           
    COMMAND
        gen         Generates the static website
                        OPTIONS
                            in={directory}
                                Optional. Default=. (current directory).
                                The documentation directory to be used.
                            out={directory}
                                Optional. Default={value of in option}/generated.
                                The output directory, where the generated documentation will be written to.
        help        Display help information
        version     Display version information                           
""";
        System.out.println(str);

    }

}
