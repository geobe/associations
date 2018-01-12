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

package de.geobe.util.association;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Implements access to the association and assures that bidirectional
 * associations are kept consistent on both sides
 *
 * @author georg beier
 */
public class ToMany<HERE, THERE> implements IToAny<THERE> {
    private IGet<Collection<THERE>> ownSide;
    private HERE self;
    private IGetOther<HERE, THERE> otherSide;

    /**
     * Create  access object for to-N association
     *
     * @param ownSide   read access to the collection at own end of the association(Lambda expression)
     * @param self      object that owns association end
     * @param otherSide access function to other end of the association(Lambda expression)
     */
    public ToMany(IGet<Collection<THERE>> ownSide, HERE self, IGetOther<HERE, THERE> otherSide) {
        this.ownSide = ownSide;
        this.self = self;
        this.otherSide = otherSide;
    }

    /**
     * add element to association
     *
     * @param other new element
     */
    @Override
    public void add(THERE other) {
        if (other == null) return;
        if (!ownSide.get().contains(other)) {
            ownSide.get().add(other);
            if (otherSide != null)
                otherSide.get(other).add(self);
        }
    }

    /**
     * add a collection of elements to the association
     *
     * @param others elements to add
     */
    @Override
    public void add(Collection<THERE> others) {
        if (others == null || others.size() == 0) return;
        for (THERE o : others) {
            add(o);
        }
    }

    /**
     * add an array of elements to the association
     *
     * @param others elements to add
     */
    @Override
    public void add(THERE[] others) {
        if (others == null || others.length == 0) return;
        for (THERE o : others) {
            add(o);
        }

    }

    /**
     * add element to association
     *
     * @param other new element
     */
    @Override
    public void remove(THERE other) {
        if (ownSide.get().contains(other)) {
            ownSide.get().remove(other);
            if (otherSide != null)
                otherSide.get(other).remove(self);
        }

    }

    /**
     * Fetch one element of the association
     *
     * @return an element, if exists, else null
     */
    @Override
    public THERE getOne() {
        if (ownSide.get().size() > 0)
            return ownSide.get().iterator().next();
        else
            return null;
    }

    /**
     * Fetch a collection of all elements of the association
     *
     * @return an unmodifiable collection of all elements
     */
    @Override
    public Collection<THERE> getAll() {
        return Collections.unmodifiableCollection(ownSide.get());
    }

    /**
     * Remove all elements from the association
     */
    @Override
    public void removeAll() {
        Collection<THERE> others = new HashSet<>(ownSide.get());
        ownSide.get().clear();
        if (otherSide != null) {
            for (THERE other : others) {
                otherSide.get(other).remove(self);
            }
        }
    }

}
