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

import java.util.HashMap;
import org.nanoboot.dog.Command;
import org.nanoboot.dog.Constants;
import org.nanoboot.dog.DogArgs;
import org.asciidoctor.Asciidoctor;
import static org.asciidoctor.Asciidoctor.Factory.create;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author pc00289
 */
public class TestCommand implements Command{

    public TestCommand() {
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public void run(DogArgs dogArgs) {
        String input = Constants.TEST_ASCIIDOC;

        Asciidoctor asciidoctor = create();

        String output = asciidoctor
                .convert(input, new HashMap<String, Object>());
        System.out.println(output);

    }
}
