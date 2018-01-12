/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018.  Georg Beier. All rights reserved.
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

package de.geobe.util.association

import de.geobe.util.association.test.One1
import de.geobe.util.association.test.One2
import groovy.util.logging.Slf4j
import spock.lang.Specification

/**
 * Created by georg beier on 12.01.2018.
 */
@Slf4j
class AssociationSpecification extends Specification {

    def 'bidirectional 1-1 associations should work'() {
        setup: 'a couple of classes'
        One1 a = new One1()
        One2 b1 = new One2()
        One2 b2 = new One2()
        when: 'b1 is associated vith a'
        b1.one1bi.add a
        then: 'both objects are associated'
        assert a.one2bi.one == b1
        assert a.one2bi.all[0] == b1
        assert b1.one1bi.one == a
        when: 'b2 is associated vith a'
        b2.one1bi.add a
        then: 'both objects are associatedand old association is released'
        assert a.one2bi.one == b2
        assert b1.one1bi.one == null
        when: 'b2 is removed from a'
        a.one2bi.remove b2
        then:
        assert b2.one1bi.one == null
        assert a.one2bi.one == null
        when: 'two objects are associated with a'
        a.one2bi.add([b1, b2])
        then: 'only the first should be added'
        assert a.one2bi.one == b1
        assert b1.one1bi.one == a
        assert b2.one1bi.one == null
        when: 'all associated objects are removed'
        a.one2bi.removeAll()
        then: 'association is empty'
        assert b1.one1bi.one == null
        assert a.one2bi.one == null
    }

    def 'unidirectional 1-1 associations should work'() {
        setup: 'a couple of classes'
        One1 a = new One1()
        One2 b1 = new One2()
        One2 b2 = new One2()
        when: 'b1 is associated vith a'
        a.one2uni.add b1
        then: 'both objects are associated'
        assert a.one2uni.one == b1
        assert a.one2uni.all[0] == b1
        when: 'b2 is associated vith a'
        a.one2uni.add b2
        then: 'both objects are associatedand old association is released'
        assert a.one2uni.one == b2
        when: 'b2 is removed from a'
        a.one2uni.remove b2
        then:
        assert a.one2uni.one == null
        when: 'two objects are associated with a'
        a.one2uni.add([b1, b2])
        then: 'only the first should be added'
        assert a.one2uni.one == b1
        when: 'all associated objects are removed'
        a.one2uni.removeAll()
        then: 'association is empty'
        assert a.one2uni.one == null
    }
}
