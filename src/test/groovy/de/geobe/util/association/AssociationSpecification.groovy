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

import de.geobe.util.association.test.*
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
        when: 'b1 is associated with a'
        b1.one1bi.add a
        then: 'both objects are associated'
        assert a.one2bi.one == b1
        assert a.one2bi.all[0] == b1
        assert b1.one1bi.one == a
        when: 'b2 is associated with a'
        b2.one1bi.add a
        then: 'both objects are associated and old association is released'
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
        when: 'two objects are added to aas array'
        a.one2bi.add([b1, b2].toArray())
        then: 'only the first should be added'
        assert a.one2bi.one == b1
        assert b1.one1bi.one == a
        assert b2.one1bi.one == null
    }

    def 'unidirectional to-1 associations should work'() {
        setup: 'a couple of classes'
        One1 a = new One1()
        One2 b1 = new One2()
        One2 b2 = new One2()
        when: 'b1 is associated with a'
        a.one2uni.add b1
        then: 'both objects are associated'
        assert a.one2uni.one == b1
        assert a.one2uni.all[0] == b1
        when: 'b2 is associated with a'
        a.one2uni.add b2
        then: 'both objects are associated and old association is released'
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

    def 'bidirectional N-1 associations should work'() {
        setup: 'a couple of classes'
        One1 a = new One1()
        Many2 b1 = new Many2()
        Many2 b2 = new Many2()
        Many2 b3 = new Many2()
        when: 'b1 and b2 are associated with a'
        b1.one1bi.add a
        a.many2bi.add b2
        then: 'both objects are associated'
        assert [b1, b2].contains(a.many2bi.one)
        assert a.many2bi.all.containsAll([b1, b2])
        assert b1.one1bi.one == a
        when: 'b2 is removed from a'
        a.many2bi.remove b2
        then:
        assert b2.one1bi.one == null
        assert a.many2bi.one == b1
        when: 'all associated objects are removed'
        a.many2bi.removeAll()
        then: 'association is empty'
        assert b1.one1bi.one == null
        assert a.many2bi.one == null
        assert a.many2bi.all.size() == 0
        when: 'three objects are associated with a'
        a.many2bi.add([b1, b2, b3])
        then: 'only the first should be added'
        assert a.many2bi.all.size() == 3
        assert b1.one1bi.one == a
        assert b2.one1bi.one == a
        assert b3.one1bi.one == a
    }

    def 'unidirectional to-M associations should work'() {
        setup: 'a couple of classes'
        Many1 a = new Many1()
        Any b1 = new Any()
        Any b2 = new Any()
        when: 'b1 is associated with a'
        a.anybi.add b1
        then: 'both objects are associated'
        assert a.anybi.one == b1
        when: 'b2 is also associated with a'
        a.anybi.add b2
        then: 'both objects are associated '
        assert a.anybi.all.containsAll([b1, b2])
        when: 'b1 is removed from a'
        a.anybi.remove b1
        then:
        assert a.anybi.one == b2
        when: 'two objects are associated with a'
        a.anybi.add([b1, b2])
        then: 'both should be added'
        assert a.anybi.all.containsAll([b1, b2])
        when: 'all associated objects are removed'
        a.anybi.removeAll()
        then: 'association is empty'
        assert a.anybi.one == null
        assert a.anybi.all.size() == 0
    }

    def 'bidirectional M-N associations should work'() {
        setup: 'a couple of classes'
        Many1 a1 = new Many1()
        Many1 a2 = new Many1()
        Many1 a3 = new Many1()
        Many2 b1 = new Many2()
        Many2 b2 = new Many2()
        Many2 b3 = new Many2()
        when: 'b1 and b2 are associated with a'
        a1.many2bi.add b1
        a1.many2bi.add b2
        a1.many2bi.add b1
        a1.many2bi.add b2
        a1.many2bi.add b3
        a2.many2bi.add b1
        a2.many2bi.add b2
        a3.many2bi.add b1
        then: 'all objects are associated'
        assert [b1, b2, b3].contains(a1.many2bi.one)
        assert a1.many2bi.all.containsAll([b1, b2, b3])
        assert a1.many2bi.all.size() == 3
        assert b1.many1bi.all.containsAll([a1, a2, a3])
        assert b3.many1bi.one == a1
        when: 'b2 is removed from a1'
        a1.many2bi.remove b2
        then:
        assert !b2.many1bi.all.contains(a1)
        assert !a1.many2bi.all.contains(b2)
        assert a1.many2bi.all.containsAll([b1, b3])
        assert b2.many1bi.all.contains(a2)
        assert b1.many1bi.all.contains(a1)
        when: 'all associated objects are removed'
        a1.many2bi.removeAll()
        then: 'association is emptied on both sides'
        assert b1.many1bi.all.containsAll([a2, a3])
        assert !b1.many1bi.all.contains(a1)
        assert a1.many2bi.one == null
        assert a1.many2bi.all.size() == 0
        when: 'three objects are associated with a1'
        b1.many1bi.removeAll()
        b2.many1bi.removeAll()
        b3.many1bi.removeAll()
        a1.many2bi.add([b1, b2, b3, b3, b3, b1, b2])
        then: 'all should be added exactly once'
        assert a1.many2bi.all.containsAll([b1, b2, b3])
        assert a1.many2bi.all.size() == 3
        assert b1.many1bi.one == a1
        assert b1.many1bi.all.size() == 1
        assert b2.many1bi.one == a1
        assert b3.many1bi.one == a1
        when: 'three objects are added as array to a1'
        b1.many1bi.removeAll()
        b2.many1bi.removeAll()
        b3.many1bi.removeAll()
        a1.many2bi.add([b1, b2, b3, b3, b3, b1, b2].toArray())
        then: 'all should be added exactly once'
        assert a1.many2bi.all.containsAll([b1, b2, b3])
        assert a1.many2bi.all.size() == 3
        assert b1.many1bi.one == a1
        assert b1.many1bi.all.size() == 1
        assert b2.many1bi.one == a1
        assert b3.many1bi.one == a1
    }
}
