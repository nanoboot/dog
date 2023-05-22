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
public class VersionCommand implements Command {

    public VersionCommand() {
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public void run(DogArgs dogArgs) {
        System.out.println("Dog 0.0.0-SNAPSHOT");
    }
    
}
