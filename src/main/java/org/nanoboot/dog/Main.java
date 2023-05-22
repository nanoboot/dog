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

import org.nanoboot.dog.commands.TestCommand;
import org.nanoboot.dog.commands.HelpCommand;
import org.nanoboot.dog.commands.GenCommand;
import org.nanoboot.dog.commands.VersionCommand;
import org.nanoboot.dog.commands.NewCommand;
import org.nanoboot.dog.commands.ServerCommand;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.0.0
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Dog - documentation generator");
        
        DogArgs dogArgs = new DogArgs(args);
        String command = dogArgs.getCommand();
        
        Set<Command> commandImplementations = new HashSet<>();
        commandImplementations.add(new GenCommand());
        commandImplementations.add(new ServerCommand());
        commandImplementations.add(new NewCommand());
        commandImplementations.add(new HelpCommand());
        commandImplementations.add(new VersionCommand());
        commandImplementations.add(new TestCommand());
        Command foundCommand = null;
        for(Command e:commandImplementations) {
            if(e.getName().equals(command)) {
                foundCommand = e;
                break;
            }
        }
        if(foundCommand == null) {
            throw new DogException("Command \"" + command + "\" is not supported.");
        }
        foundCommand.run(dogArgs);
    }


}
