/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Stephan Classen, Markus Schlichting
 * Copyright (c) 2014 François-Xavier Thoorens
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.asciidoctor.extension

import geb.Browser
import org.asciidoctor.ast.AbstractBlock
import org.jruby.RubyHash
import org.openqa.selenium.Dimension

/**
 * Block to control the browser using GEB.
 */
class DriveBrowserBlock extends BlockProcessor implements BrowserResizer {

    DriveBrowserBlock(String name, RubyHash config) {
        super(name, [contexts: [':paragraph'], content_model: ':simple'])
    }

    def process(AbstractBlock block, Reader reader, Map<String, Object> attributes) {
        final String dimension = attributes['dimension']
        if (dimension) {
            resizeBrowserWindow(dimension)
        }

        def binding = new Binding()
        binding.setVariable("Browser", Browser)
        binding.setVariable("Dimension", Dimension)

        def shell = new GroovyShell(binding)
        shell.evaluate("Browser.drive{" + reader.lines().join("\n") + "}")
        createBlock(block, "paragraph", "", [:], [:])
    }
}
