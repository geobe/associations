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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements access to the association and assures that bidirectional
 * associations are kept consistent on both sides
 *
 * @author georg beier
 */
public class ToOne<HERE, THERE> implements IToAny<THERE> {
    private IGet<THERE> getOwn;
    private ISet<THERE> setOwn;
    private HERE self;
    private IGetOther<HERE, THERE> otherSide;

    /**
     * Create  access object for to-1 association
     *
     * @param getOwn    read access to own end of the association(Lambda expression)
     * @param setOwn    write access to own end of the association(Lambda expression)
     * @param self      object that owns association end
     * @param otherSide access function to other end of the association(Lambda expression)
     */
    public ToOne(IGet<THERE> getOwn, ISet<THERE> setOwn, HERE self, IGetOther<HERE, THERE> otherSide) {
        this.getOwn = getOwn;
        this.setOwn = setOwn;
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
        if (other == getOwn.get())
            return;
        if (otherSide != null) {
            THERE oldref = getOwn.get();
            setOwn.set(other);
            if (oldref != null) {
                otherSide.get(oldref).remove(self);
            }
            if (other != null) {
                otherSide.get(other).add(self);
            }
        } else {
            setOwn.set(other);
        }
    }

    /**
     * add a collection of elements to the association. As we are in a
     * to-1 association, we only add one element
     *
     * @param others elements to add
     */
    @Override
    public void add(Collection<THERE> others) {
        if (others == null || others.size() == 0) return;
        for (THERE o : others) {
            add(o);
            break;
        }
    }

    /**
     * add an array of elements to the association. As we are in a
     * to-1 association, we only add the first element
     *
     * @param others elements to add
     */
    @Override
    public void add(THERE[] others) {
        if (others == null || others.length == 0) return;
        add(others[0]);
    }

    /**
     * remove element from association
     *
     * @param other existing element
     */
    @Override
    public void remove(THERE other) {
        if (getOwn.get() == null || other != getOwn.get())
            return;
        setOwn.set(null);
        if (otherSide != null) {
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
        return getOwn.get();
    }

    /**
     * Fetch a collection of all elements of the association
     *
     * @return a collection with zero or one element
     */
    @Override
    public Collection<THERE> getAll() {
        ArrayList<THERE> ret = new ArrayList<>(1);
        if (getOne() != null) {
            ret.add(getOne());
        }
        return ret;
    }

    /**
     * Remove all elements from the association
     */
    @Override
    public void removeAll() {
        if (otherSide != null && getOwn.get() != null) {
            otherSide.get(getOwn.get()).remove(self);
        } else {
            setOwn.set(null);
        }
    }
}
